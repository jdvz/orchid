CREATE TABLE cms_page_content (
  id INT NOT NULL,
  path VARCHAR(512),
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES cms_entries(id)
);

INSERT INTO cms_page_content(id, path) SELECT page.id, entry.pretty_url FROM cms_pages page INNER JOIN cms_entries entry ON (page.id = entry.id);
