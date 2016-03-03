CREATE TABLE images (
  id INT NOT NULL,
  real_name VARCHAR(128) NOT NULL UNIQUE,
  mime VARCHAR(16) NULL,
  PRIMARY KEY (id),
  FOREIGN KEY(id) REFERENCES cms_entries(id)
);