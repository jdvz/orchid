insert into users(first_name, last_name, email, password) VALUES ('olga', 'getmanenko', 'ol.lynx@gmail.com', '123');
insert into users(first_name, last_name, email, password) VALUES ('dmitri', 'zaporozhtsev', 'dmitri.zera@gmail.com', '123');
INSERT INTO roles(role_name) VALUES ('admin');
INSERT INTO roles(role_name) VALUES ('customer');
INSERT INTO user_roles(user_id, role_id) select u.id, r.id from users u inner join roles r ON (r.role_name = 'admin');
INSERT INTO user_roles(user_id, role_id) select u.id, r.id from users u inner join roles r ON (r.role_name = 'customer') WHERE u.first_name = 'olga';
