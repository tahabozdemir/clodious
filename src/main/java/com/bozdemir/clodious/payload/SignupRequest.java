package com.bozdemir.clodious.payload;

import com.bozdemir.clodious.model.ERole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

public record SignupRequest(String username,
                            String name,
                            String surname,
                            String email,
                            String password) {

    public static final class SignupRequestBuilder {
        private String username;
        @JsonIgnore
        private String password;
        private String name;
        private String surname;
        private String email;
        private Set<ERole> roles;

        private SignupRequestBuilder() {
        }

        public static SignupRequestBuilder aSignupRequest() {
            return new SignupRequestBuilder();
        }

        public SignupRequestBuilder username(String username) {
            this.username = username;
            return this;
        }

        public SignupRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public SignupRequestBuilder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public SignupRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public SignupRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public SignupRequestBuilder roles(Set<ERole> roles) {
            this.roles = roles;
            return this;
        }

        public SignupRequest build() {
            return new SignupRequest(username, name, surname, email, password);
        }
    }
}
