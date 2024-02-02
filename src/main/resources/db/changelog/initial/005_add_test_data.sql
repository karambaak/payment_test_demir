insert into AUTHORITIES(AUTHORITY)
values ( 'user' );

insert into USERS (LOGIN, PASSWORD, BALANCE, ENABLED, AUTHORITY_ID, FAILED_LOGIN_ATTEMPTS, LOGIN_DISABLED)
values ('user', '$2a$10$fUg6lbqr2QrXM2q2OV5txuYPturzD5Xnyf/GbKmNa4dsSEM7xmhhm', 8, true,
        (select id from AUTHORITIES where AUTHORITY ilike 'user'), 0, false );