CREATE TABLE cms_entries (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(128) NOT NULL,
  pretty_url VARCHAR(128) NOT NULL,
  discriminator VARCHAR(32) NOT NULL DEFAULT 'CmsPage',
  enabled BIT(1) DEFAULT 1,
  tagline VARCHAR(256) NULL,
  PRIMARY KEY (id),
  UNIQUE unique_url (pretty_url)
);
CREATE TABLE pages (
  id INT NOT NULL,
  template VARCHAR(64) NOT NULL,
  main_page_id INT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY(id) REFERENCES cms_entries(id)
);
CREATE TABLE cms_pages (
  id INT NOT NULL,
  content_page_id INT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY(id) REFERENCES pages(id)
);
CREATE TABLE content_pages (
  id INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY(id) REFERENCES pages(id)
);
CREATE TABLE multi_cms_pages (
  id INT NOT NULL,
  page_num INT NOT NULL DEFAULT 0,
  next_page_id INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY(id) REFERENCES cms_pages(id)
);

INSERT INTO cms_entries (name, pretty_url, discriminator) VALUES ('main content', 'how-to-start', 'ContentPage');
INSERT INTO cms_entries (name, pretty_url) VALUES ('first page', 'how-to-start-show');

INSERT INTO pages (id, template) select id, 'main' from cms_entries;
UPDATE pages set main_page_id = (SELECT id FROM cms_entries WHERE pretty_url = 'how-to-start');

INSERT INTO content_pages(id) SELECT id FROM cms_entries WHERE pretty_url = 'how-to-start';
INSERT INTO cms_pages(id) SELECT page.id FROM cms_entries page WHERE page.pretty_url = 'how-to-start-show';
UPDATE cms_pages SET content_page_id = (SELECT content_page.id FROM content_pages content_page);