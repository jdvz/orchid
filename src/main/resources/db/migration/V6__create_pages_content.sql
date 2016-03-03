CREATE TABLE cms_page_content (
  id INT NOT NULL AUTO_INCREMENT,
  page_id INT NOT NULL REFERENCES cms_pages(id),
  content_order INT NOT NULL DEFAULT 0,
  content VARCHAR(2000),
  PRIMARY KEY (id)
);

INSERT INTO cms_page_content(page_id, content_order, content) SELECT page.id, 0, '<div id="test1"><h3>header</h3></div>' FROM cms_pages page;
INSERT INTO cms_page_content(page_id, content_order, content) SELECT page.id, 1, '<div id="test2"><h4>footer</h4></div>' FROM cms_pages page;
