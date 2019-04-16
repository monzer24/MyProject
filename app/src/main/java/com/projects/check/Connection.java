package com.projects.check;

import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

public interface Connection<K, V> {

    void uploadImage(byte[] bytes, Map <String, Object> info);

    String addCheck(String imageURL, Map<K, V> info);

    User logIn(User user);

    boolean signUp(User user);

    Check retrieveCheck(String id);

}
