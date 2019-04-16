package com.projects.check;

import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

public interface Connection<K, V> {

    void uploadImage(byte[] bytes, Map <String, Object> info);

    String addCheck(String imageURL, Map<K, V> info);

    void logIn(User user);

    boolean signUp(User user);

    void retrieveCheck(String id, User user);

}
