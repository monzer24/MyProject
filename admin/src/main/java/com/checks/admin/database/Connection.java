package com.checks.admin.database;

import com.checks.admin.model.AdminUser;
import com.checks.admin.model.Check;

public interface Connection {

    void logIn(AdminUser adminUser);

    void accept(Check check);

    void reject(Check check);

}
