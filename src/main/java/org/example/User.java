package org.example;

public class User {


    public User(String name, String email, String password) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    private String email;
    private String password;
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }


}
