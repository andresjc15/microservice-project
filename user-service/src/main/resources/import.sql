INSERT INTO users (username, password, enabled, name, lastname, email) VALUES ('andresjc', '$2a$10$uim/LPZ0Ws7d/3LTZToB9.lRGWoEV7R7fPXC0lcalr1R78vcNRI9K', 1, 'andres', 'castelo', 'andres.jcp15@gmail.com');
INSERT INTO users (username, password, enabled, name, lastname, email) VALUES ('admin', '$2a$10$uim/LPZ0Ws7d/3LTZToB9.lRGWoEV7R7fPXC0lcalr1R78vcNRI9K', 1, 'admin', 'root', 'admin.jcp15@gmail.com');

INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO users_to_roles (user_id, role_id) VALUES (1, 1)
INSERT INTO users_to_roles (user_id, role_id) VALUES (2, 2)
INSERT INTO users_to_roles (user_id, role_id) VALUES (2, 1)