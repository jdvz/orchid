ALTER TABLE cms_entries ADD version INT NOT NULL DEFAULT 1;
UPDATE cms_entries set version = 1;
ALTER TABLE products ADD version INT NOT NULL DEFAULT 1;
UPDATE products set version = 1;
ALTER TABLE users ADD version INT NOT NULL DEFAULT 1;
UPDATE users set version = 1;
ALTER TABLE roles ADD version INT NOT NULL DEFAULT 1;
UPDATE roles set version = 1;