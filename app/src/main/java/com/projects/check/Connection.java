package com.projects.check;

import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

public interface Connection<K, V> {

    String uploadImage(byte[] bytes);

    String addCheck(String imageURL, Map<K, V> info);

    boolean logIn(String bankNo, String password);

    boolean signUp(User user);

}
