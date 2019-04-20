package com.projects.check.database;

import com.projects.check.model.User;

import java.util.Map;

public interface Connection<K, V> {

    void uploadImage(byte[] bytes, Map <String, Object> info, User user);

    String addCheck(String imageURL, Map<K, V> info, User user);

    void logIn(User user);

    void signUp(User user);

    void retrieveCheck(String id, User user);

    void cashCheck(String id, User user);

}
