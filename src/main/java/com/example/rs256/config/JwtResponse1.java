package com.example.rs256.config;

import java.io.Serializable;
import java.security.PublicKey;
import java.io.Serializable;

public class JwtResponse1 implements Serializable {



        private static final long serialVersionUID = -8091879091924046844L;
        private final String jwttoken;


        public JwtResponse1(String jwttoken) {
            this.jwttoken = jwttoken;

        }

        public String getToken() {
            return this.jwttoken;
        }
}
