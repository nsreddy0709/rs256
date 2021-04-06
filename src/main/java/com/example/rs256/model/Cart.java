package com.example.rs256.model;


import javax.persistence.*;

@Entity
@Table(name = "cart")

public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "p_id")
    private int pid;
    @Column(name = "ip_add")
    private String ip_add;
    @Column(name = "user_id")
    private int uid;
    @Column(name = "product_title")
    private String title;
    @Column(name = "product_image")
    private String image;
    @Column(name = "qty")
    private int qty;
    @Column(name = "price")
    private int price;
    @Column(name = "total_amount")
    private int amount;

    public Cart() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIp_add() {
        return ip_add;
    }

    public void setIp_add(String ip_add) {
        this.ip_add = ip_add;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

}
