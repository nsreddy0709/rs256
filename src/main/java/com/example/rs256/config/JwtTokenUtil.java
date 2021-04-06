package com.example.rs256.config;

import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;



import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.KeyGenerator;

@Service
public class JwtTokenUtil {
    //private String secret = "xadmin";
    PublicKey publicKey;
    PrivateKey privateKey;



    public JwtTokenUtil() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, InvalidKeyException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair kp = keyPairGenerator.genKeyPair();
        this.publicKey = kp.getPublic();
        this.privateKey = kp.getPrivate();
    }
////        String pr = "-----BEGIN RSA PRIVATE KEY-----\n" +
////                "MIICWwIBAAKBgQDgFUDjtvMdsalYcO5cQKEJAsCLM1kO3gUVaITgADivDdKxsbJW\n" +
////                "3biSbD4RCX3UKkOOT1y1K+s+fEU5cLCEpQrM3nkw2AMxgNvxdnuEj0GwMpxXiJkG\n" +
////                "tkrLX5Ik0WObZG4zSPxNNi4KSpDNGKHXVRCYv0VripVYyCvOl4fFMmrM4wIDAQAB\n" +
////                "AoGAX+T4hfqvA9AZ1n1NpDEMORzyZR+uRwyARG8cHfg6Sb+yoNSYq0/rQs4LqaLU\n" +
////                "NE1555bS571JyM3JmhE9mSY//isleYN8vpEc+XA8BM/Uy7C0xNsE7rjWuv83B4rS\n" +
////                "jJ4fa2bYeb5bT+3UhjcIvkgItP6A88mXWIDGKtu1iZrW6DECQQD7E71ztNqsPfZ/\n" +
////                "3TKWGVNO9OiF5Fda6V9Qqb4IcCtQNwaByePe60D4XHVUGn/2HT1LmTCQPuSVYXww\n" +
////                "f1QtljIvAkEA5HoE2Do9OBQW/jxsaK96UnGeqPHAs7Ts3wEq6FTpBFnYzKNkCgLj\n" +
////                "hqF5otaPIpHmF896wieN2LqNY0l01LQnjQJAMJnbnGAvzBOaeZnJxwBT/f6uW+yd\n" +
////                "dD2kc6rH3D5KRBbCrbD06RvE0c6j74nJp0x1pwKaQfHKsZfTQFgFZWWjhQJAA895\n" +
////                "JzYJcVb8by8Iy9IpYxicActOYHDjBnEZixhnbnglInyTHYugyc0Fn06ewn/Wa409\n" +
////                "SeGo/vVViFNgTG/YvQJAGmNwo22qNY5YHSVRs5KHPkFRkZBGmaYhT3/Axw4zflyK\n" +
////                "I6svdtAUCDYN0zl8AhW/ThJuRsOIojzA+AXtNQ7kLg==\n" +
////                "-----END RSA PRIVATE KEY-----";
////
////        String pk = "-----BEGIN PUBLIC KEY-----\n" +
////                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgFUDjtvMdsalYcO5cQKEJAsCL\n" +
////                "M1kO3gUVaITgADivDdKxsbJW3biSbD4RCX3UKkOOT1y1K+s+fEU5cLCEpQrM3nkw\n" +
////                "2AMxgNvxdnuEj0GwMpxXiJkGtkrLX5Ik0WObZG4zSPxNNi4KSpDNGKHXVRCYv0Vr\n" +
////                "ipVYyCvOl4fFMmrM4wIDAQAB\n" +
////                "-----END PUBLIC KEY-----";
////        com.sun.org.apache.xml.internal.security.Init.init();
////
////        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//
//
//        //byte[] privateBytes = Base64.decode(pr);
//        //byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
////        PKCS8EncodedKeySpec keySpec1 = new PKCS8EncodedKeySpec(Base64.decode(pr));
////
////        this.privateKey = keyFactory.generatePrivate(keySpec1);
////
////        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(pk));
////        //KeyFactory keyFactory = KeyFactory.getInstance("RSA");
////        this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
//
//
//    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        // here it will check if the token has created before time limit. i.e 10 hours then will will return true else false
        return extractExpiration(token).before(new Date());
    }

    // this method is for generating token. as argument is username. so as user first time send request with usernamr and password
    // so here we will fetch the username , so based on that username we are going to create one token
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // in this method createToken subject argument is username
    // here we are setting the time for 10 hours to expire the token.
    // and you can see we are using HS256 algorithmn
    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.RS256, privateKey).compact();
    }

    // here we are validation the token
    public Boolean validateToken(String token, UserDetails userDetails) {
        // basically token will be generated in encrpted string and from that string . we extract our usename and password using extractUsername method
        final String username = extractUsername(token);
        System.out.println(userDetails.getUsername());
        // here we are validation the username and then check the token is expired or not
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    //public PublicKey getPublicKey(){
        //return this.publicKey;
    //}
//    public String extractUsername1(String token) {
//
//        String[] splitToken = token.split("\\.");
//        String unsignedToken = splitToken[0] + "." + splitToken[1] + ".";
//
//        DefaultJwtParser parser = new DefaultJwtParser();
//        Jwt<?, ?> jwt = parser.parse(unsignedToken);
//        Claims claims = (Claims) jwt.getBody();
//        return claims.getSubject();
//    }


}


