package ru.yandex.ApiData;

public class UserData {

    public String name;
    public String email;
    public String password;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserData(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserData getLoginInfo() {
        return new UserData(this.email, this.password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
}
