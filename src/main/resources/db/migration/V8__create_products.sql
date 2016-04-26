CREATE TABLE products (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(256) NOT NULL,
  description VARCHAR(2000),
  shoot_count INT NULL,
  blossom_status VARCHAR(24) NOT NULL DEFAULT 'empty',
  leaf BIT(1) DEFAULT 0,
  enabled BIT(1) DEFAULT 1,
  PRIMARY KEY (id)
);

CREATE TABLE orchids (
  id INT NOT NULL,
  category_id INT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES products(id)
);

CREATE TABLE hybrids (
  id INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES products(id)
);

CREATE TABLE categories (
  id INT NOT NULL,
  name VARCHAR(256),
  PRIMARY KEY (id)
);

CREATE TABLE h2crels (
  hybrid_id INT NOT NULL,
  category_id INT NOT NULL,
  FOREIGN KEY (hybrid_id) REFERENCES hybrids(id),
  FOREIGN KEY (category_id) REFERENCES categories(id),
  UNIQUE unique_hybrid_category (hybrid_id, category_id)
);