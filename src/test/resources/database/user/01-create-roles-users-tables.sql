CREATE TABLE roles (
                       id BIGINT PRIMARY KEY,
                       role_name VARCHAR(50)
);

CREATE TABLE users (
                       id BIGINT PRIMARY KEY,
                       email VARCHAR(255),
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       password VARCHAR(255),
                       is_deleted BOOLEAN,
                       tg_chat_id BIGINT
);

CREATE TABLE users_roles (
                             user_id BIGINT,
                             role_id BIGINT,
                             PRIMARY KEY (user_id, role_id),
                             FOREIGN KEY (user_id) REFERENCES users (id),
                             FOREIGN KEY (role_id) REFERENCES roles (id)
);
