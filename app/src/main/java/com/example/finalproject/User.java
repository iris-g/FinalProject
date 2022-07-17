package com.example.finalproject;


    public class User
    {
        public String name;
        public String email;

// need empty constractor

        public User()
        {}

        public User(String name,String email)
        {
            this.name = name;
            this.email = email;
        }

        public String getName()
        {
            return name;
        }

        public String getEmail()
        {
            return email;
        }
}
