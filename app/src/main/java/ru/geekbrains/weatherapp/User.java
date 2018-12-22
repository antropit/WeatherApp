package ru.geekbrains.weatherapp;

import android.media.Image;

import java.net.URI;
import java.net.URL;

class User {
    private String userName;
    private String userEmail;
    private String userPhone;
    private URI userAvatar;


    User (String userName, String userEmail) {
        setUserName(userName);
        setUserEmail(userEmail);
    }

    User (String userName, String userEmail, String userPhone) {
        setUserName(userName);
        setUserEmail(userEmail);
        setUserPhone(userPhone);
    }

    User (String userName, String userEmail, String userPhone, URI userAvatar) {
        setUserName(userName);
        setUserEmail(userEmail);
        setUserPhone(userPhone);
        setUserAvatar(userAvatar);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String emailAddress) {
        this.userEmail = emailAddress;
    }

    public String getUserPhone() { return userPhone; }

    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public URI getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(URI userAvatar) {
        this.userAvatar = userAvatar;
    }
}
