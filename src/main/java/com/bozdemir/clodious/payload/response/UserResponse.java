package com.bozdemir.clodious.payload.response;

public record UserResponse(Long id, String email, String username, String name, String surname) {

    public static final class UserResponseBuilder {
        private Long id;
        private String email;
        private String username;
        private String name;
        private String surname;


        private UserResponseBuilder() {
        }

        public static UserResponseBuilder anUserResponse() {
            return new UserResponseBuilder();
        }

        public UserResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserResponseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserResponseBuilder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public UserResponse build() {
            return new UserResponse(id, email, username, name, surname);
        }
    }
}
