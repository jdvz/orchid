ALTER TABLE images ADD real_dir VARCHAR(128) NOT NULL;
UPDATE images set real_dir = 'dir1';