package com.example.rs256.resource;


import com.example.rs256.config.*;

import com.example.rs256.model.*;
import com.example.rs256.repository.*;
import com.example.rs256.model.Cart;
import com.example.rs256.repository.CartRepository;
import com.example.rs256.repository.OrdersRepository;
import com.example.rs256.repository.ProductsRepository;
import com.example.rs256.repository.UsersRepository;
import com.sun.javafx.collections.MappingChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController

@RequestMapping(value = "/v1")
public class UserResource {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductsRepository productsRepository;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    Orders1Repository orders1Repository;



    @PostMapping(value = "/register")
    public ResponseEntity<?> persist(@RequestBody final Users users){
        int c = 0;
        String first_name = users.getFirst_name();
        if(first_name.length() < 4)
        {
            first_name = "please enter valid_name with no less than 3 alphabets";
            c++;
        }
        else {
            first_name = "";
        }
        String last_name = users.getFirst_name();
        if(last_name.length() < 4)
        {
            last_name = "please enter valid_name with no less than 3 alphabets";
            c++;
        }
        else
        {
            last_name = "";
        }
        String email = users.getEmail();
        try{
            Users u = usersRepository.findUsersByEmail(email);
            System.out.println(u.getEmail());
            if(u!=null)
            {
                email = "User with this emailID already exists";
                return ResponseEntity.ok(new RegisterError("","",email,"","","",""));
            }
        }
        catch (NullPointerException e)
        {
            String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            if(matcher.matches() == false)
            {
                email = "Please enter valid emailID";
                c++;
                return ResponseEntity.ok(email);
            }
            else {

                email = "";

            }
        }

        String password = users.getPassword();

        String regex1 = "((?=.*[a-z])(?=.*[0-9])(?=.*[A-Z]).{8,})";
        Pattern pattern1 = Pattern.compile(regex1);
        Matcher matcher1 = pattern1.matcher(password);
        System.out.println(matcher1);

        if(matcher1.matches() == false)
        {
            password = "Password must contain minimum eight characters, at least one uppercase letter, one lowercase letter and one number";
            c++;
        }
        else {
            password = "";
        }
        String mobile_number = users.getMobile();
        String regex2 = "(^$|[7-9]{1}[0-9]{9})";
        Pattern pattern2 = Pattern.compile(regex2);
        Matcher matcher2 = pattern2.matcher(mobile_number);
        if(matcher2.matches() == false)
        {
            mobile_number = "Mobile number must be a valid 10 digit number";
            c++;
        }
        else {
            mobile_number = "";
        }
        String addess1 = users.getAddress1();
        if(addess1.length() < 5)
        {
            addess1 = "Please enter valid address";
            c++;
        }
        else {
            addess1 = "";
        }
        String address2 = users.getAddress2();
        if(address2.length()!=0 && address2.length() < 5)
        {
            address2 = "Please enter valid address";
            c++;
        }
        else {
            address2 = "";
        }
        if(c == 0)
        {
            usersRepository.save(users);
            return ResponseEntity.ok(new RegisterError(first_name,last_name,"New User",password,mobile_number,addess1,address2));
        }


        return ResponseEntity.ok(new RegisterError(first_name,last_name,email,password,mobile_number,addess1,address2));

    }

    @GetMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<?> isValid(@RequestParam String email,@RequestParam String password) {
        Users u = null;
        Users p = null;
        try{

            u = usersRepository.findUsersByEmailAndPassword(email,password);
            //p = usersRepository.findByPassword(password);
            System.out.println(u);
            System.out.println(email);
        }
        catch (Exception e){
            return ResponseEntity.ok("Invaid credentials");
        }
        if(u!=null){

            final String token = jwtTokenUtil.generateToken(u.getEmail());
            //final PublicKey token1 = jwtTokenUtil.getPublicKey();
            //System.out.println(token1);
            //byte[] byte_pubkey = token1.getEncoded();
            //String token4 = Arrays.toString(byte_pubkey);
            //System.out.println(token4);
            return ResponseEntity.ok(new JwtResponse(token));
        }
        return ResponseEntity.ok(new JwtResponse1("Invalid credential"));
    }


    @PostMapping(value = "/addtocart")
    public ResponseEntity<?> persist(@RequestBody final AddToCart addToCart ){
        String title = addToCart.getTitle();
        String name = addToCart.getToken();
        name = jwtTokenUtil.extractUsername(name);
        System.out.println(name);
        List<String> list = Arrays.asList(new String[]{"Already added to cart"});

        System.out.println(title);
        Users users = null;


        Products product = productsRepository.findProductsByPtitle(title);
        System.out.println(product);
        System.out.println(name);
        users = usersRepository.findUsersByEmail(name);
        System.out.println(users);


        Cart cart1 = cartRepository.findCartByUidAndPid(users.getUser_id(),product.getProduct_id());

        if(cart1!=null){
            return ResponseEntity.ok("Already added to cart");
        }
        else {


            Cart cart = new Cart();
            cart.setPid(product.getProduct_id());
            cart.setUid(users.getUser_id());
            cart.setIp_add("imiim");
            cart.setTitle(product.getPtitle());
            cart.setImage(product.getProduct_image());
            cart.setQty(1);
            cart.setPrice(product.getProduct_price());
            cart.setAmount(product.getProduct_price());

            cartRepository.save(cart);


            return ResponseEntity.ok("Successfully added to cart");
        }
    }

    @PostMapping(value = "/getcartdetails")
    public List<Cart> getCart(@RequestBody GetCart getCart)
    {
        String name = jwtTokenUtil.extractUsername(getCart.getToken());
        Users users = usersRepository.findUsersByEmail(name);
        return cartRepository.findCartByUid(users.getUser_id());
    }

    @PostMapping(value = "/updateqyt")
    public List<Cart> update(@RequestBody UpdateQty updateQty)
    {
        String email = jwtTokenUtil.extractUsername(updateQty.getToken());
        Users users = usersRepository.findUsersByEmail(email);

        Products product = productsRepository.findProductsByPtitle(updateQty.getTitle());
        System.out.println(product);
        Cart cart = cartRepository.findCartByUidAndPid(users.getUser_id(),product.getProduct_id());

        int qty=updateQty.getQty();
        cart.setQty(qty);
        cart.setAmount(qty * cart.getPrice());
        cartRepository.save(cart);
        return cartRepository.findCartByUid(users.getUser_id());
    }
    @PostMapping(value = "/getamount")
    public Integer toalAmount(@RequestBody GetAmount th)
    {
        String name = jwtTokenUtil.extractUsername(th.getToken());
        Users users = usersRepository.findUsersByEmail(name);
        List<Orders> orders = ordersRepository.findOrdersByUid(users.getUser_id());
        int sum = 0;
        for (Orders o:
                orders) {
            sum = sum + o.getPrice();
        }

        return sum;
    }

    @PostMapping(value = "/deletecart")
    public ResponseEntity delete(@RequestBody AddToCart addToCart){

            String email = jwtTokenUtil.extractUsername(addToCart.getToken());
            Users users = usersRepository.findUsersByEmail(email);
            Products product = productsRepository.findProductsByPtitle(addToCart.getTitle());
            cartRepository.deleteCartByUidAndPid(users.getUser_id(),product.getProduct_id());
            return ResponseEntity.ok(new LoginError("Cart deleted succesfully"));





    }

    //@DeleteMapping
    //public void deletecarts(@RequestParam String email){
      //  Users users = usersRepository.findUsersByEmail(email);
        //cartRepository.deleteCartByUid(users.getUser_id());
    //}

    @PostMapping(value = "/checkout")
    public List<Orders> checkout(@RequestBody GetCart getCart ){
        String email = jwtTokenUtil.extractUsername(getCart.getToken());
        Users users = usersRepository.findUsersByEmail(email);
        List<Cart> cart = cartRepository.findCartByUid(users.getUser_id());
        System.out.println(cart);
        for (Cart c:
                cart) {
            Orders orders = new Orders();
            orders.setUid(c.getUid());
            orders.setPid(c.getPid());
            orders.setPname(c.getTitle());
            orders.setPrice(c.getAmount());
            orders.setQty(c.getQty());
            ordersRepository.save(orders);
        }
        return ordersRepository.findOrdersByUid(users.getUser_id());
    }



}
