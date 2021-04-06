package com.example.rs256.config;

import java.io.Serializable;

public class RegistrationSuccess implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String message;

    public RegistrationSuccess(String message) {
        this.message = message;
    }

    public String getToken() {
        return this.message;
    }

}
