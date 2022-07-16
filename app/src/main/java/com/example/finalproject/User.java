package com.example.finalproject;


    public class User
    {
        public String Name;
        public String Email;

// need empty constractor

        public User()
        {}

        public User(String name,String email)
        {
            Name = name;
            email = email;
        }

        public String getName()
        {
            return Name;
        }

        public String getEmail()
        {
            return Email;
        }
}
