create table configuration (
  name VARCHAR(64) NOT NULL,
  value VARCHAR(256),
  PRIMARY KEY (name),
  UNIQUE unique_username (name)
);
