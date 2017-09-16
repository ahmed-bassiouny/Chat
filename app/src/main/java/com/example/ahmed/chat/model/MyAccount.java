package com.example.ahmed.chat.model;

import android.net.Uri;


/**
 * Created by ahmed on 16/09/17.
 */

public class MyAccount {

    private static String Id;
    private static String userName;
    private static String email;
    private static Uri image;

    public static String getId() {
        return Id;
    }

    public static void setId(String id) {
        Id = id;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        MyAccount.userName = userName;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        MyAccount.email = email;
    }

    public static Uri getImage() {
        return image;
    }

    public static void setImage(Uri image) {
        MyAccount.image = image;
    }
}
