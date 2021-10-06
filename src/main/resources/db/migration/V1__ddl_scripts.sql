-- create tables
CREATE TABLE CUSTOMERS(
  id SERIAL UNIQUE,
  name TEXT NOT NULL
);

CREATE TABLE PRODUCTS(
  id SERIAL UNIQUE,
  name TEXT NOT NULL,
  price NUMERIC NOT NULL
);

CREATE TABLE ORDERS(
  id SERIAL UNIQUE,
  customer_id INT NOT NULL,
  product_id INT NOT NULL,
  quantity INT NOT NULL,
  checked_out BOOLEAN NOT NULL DEFAULT false,
  CONSTRAINT FK_CUSTOMER FOREIGN KEY (customer_id) REFERENCES CUSTOMERS(id),
  CONSTRAINT FK_PRODUCT FOREIGN KEY (product_id) REFERENCES PRODUCTS(id)
);

-- insert sample customer and product
INSERT INTO CUSTOMERS(id, name) VALUES (1, 'Michael M.'), (2, 'Demo Customer');
INSERT INTO PRODUCTS(id, name, price) VALUES
 (1, 'Google Pixel-4', 250.0),
 (2, 'Dish Washer', 150.0),
 (3, 'Office Chair', 50);
