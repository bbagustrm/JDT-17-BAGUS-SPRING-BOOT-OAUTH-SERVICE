package com.jdt17.oauth.service.data.utility.table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MtUsersVariable {

    public static final String TABLE_MT_USERS = "mt_users";

    public static final String COLUMN_MT_USERS_ID = "users_id";
    public static final String COLUMN_MT_USERS_FULL_NAME = "users_full_name";
    public static final String COLUMN_MT_USERS_CREDENTIALS = "users_credentials";
    public static final String COLUMN_MT_USERS_EMAIL = "users_email";
}
