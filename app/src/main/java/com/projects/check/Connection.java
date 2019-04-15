package com.projects.check;

import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

public interface Connection<K, V> {

    void uploadImage(byte[] bytes);

    String addCheck(String imageURL, Map<K, V> info);

    User logIn(User user);

    boolean signUp(User user);

}
