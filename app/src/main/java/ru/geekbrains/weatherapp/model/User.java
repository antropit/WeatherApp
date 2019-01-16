package ru.geekbrains.weatherapp.model;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;

import java.net.URI;
import java.net.URL;

public class User {
    private String userName;
    private String userEmail;
    private Uri userAvatarUri;


    public User (String userName, String userEmail) {
        setUserName(userName);
        setUserEmail(userEmail);
    }

    public User (String userName, String userEmail, Uri userAvatarUri) {
        setUserName(userName);
        setUserEmail(userEmail);
        setUserAvatarUri(userAvatarUri);
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

    public Uri getUserAvatarUri() {
        if (userAvatarUri == null) {
            return Uri.parse("http://img3.wikia.nocookie.net/__cb20131019015927/marvelheroes/images/b/b1/Spiderman_Superior.png");
        }
        return userAvatarUri;
    }

    public void setUserAvatarUri(Uri userAvatarUri) {
        this.userAvatarUri = userAvatarUri;
    }
}
