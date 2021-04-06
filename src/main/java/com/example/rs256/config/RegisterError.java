package com.example.rs256.config;
import java.io.Serializable;
public class RegisterError implements Serializable{
    private static final long serialVersionUID = -8091879091924046844L;
    private final String first_name;
    private final String last_name;
    private final String email;
    private final String password;
    private final String mobile;
    private final String address1;
    private final String address2;

    public RegisterError(String first_name, String last_name, String email, String password, String mobile, String address1,String address2) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.address1 = address1;
        this.address2 = address2;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }
}
