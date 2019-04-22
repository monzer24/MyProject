package com.checks.admin;

public interface Connection {

    void logIn(AdminUser adminUser);

    void accept(Check check);

    void reject(Check check);

}
