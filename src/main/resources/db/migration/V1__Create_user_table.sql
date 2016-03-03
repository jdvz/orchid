create table users (
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(64) NOT NULL,
  last_name VARCHAR(64) NOT NULL,
  email VARCHAR(64) NOT NULL,
  password VARCHAR(64) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE unique_username (email)
);

create table roles (
  id INT NOT NULL AUTO_INCREMENT,
  role_name VARCHAR(32) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE unique_rolename (role_name)
);

create table user_roles (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (role_id) REFERENCES roles(id),
  UNIQUE unique_user_role (user_id, role_id)
);