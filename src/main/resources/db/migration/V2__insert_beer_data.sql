-- Beer Data Migration Script for JunieMVC Application
-- This script populates the beer table with initial data for famous beers from around the world

-- Insert statements for famous beers
-- Each beer includes: beer_name, beer_style, upc, quantity_on_hand, price
-- All fields are populated with realistic data

-- American Beers
INSERT INTO beer (beer_name, beer_style, upc, quantity_on_hand, price, created_date, update_date, version)
VALUES ('Sierra Nevada Pale Ale', 'Pale Ale', '83783375213', 120, 8.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO beer (beer_name, beer_style, upc, quantity_on_hand, price, created_date, update_date, version)
VALUES ('Samuel Adams Boston Lager', 'Lager', '83783375214', 200, 7.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO beer (beer_name, beer_style, upc, quantity_on_hand, price, created_date, update_date, version)
VALUES ('Goose Island IPA', 'IPA', '83783375215', 150, 9.49, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- European Beers
INSERT INTO beer (beer_name, beer_style, upc, quantity_on_hand, price, created_date, update_date, version)
VALUES ('Guinness Draught', 'Stout', '83783375216', 180, 10.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO beer (beer_name, beer_style, upc, quantity_on_hand, price, created_date, update_date, version)
VALUES ('Heineken Original', 'Lager', '83783375217', 250, 8.49, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO beer (beer_name, beer_style, upc, quantity_on_hand, price, created_date, update_date, version)
VALUES ('Stella Artois', 'Pilsner', '83783375218', 220, 9.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- Belgian Beers
INSERT INTO beer (beer_name, beer_style, upc, quantity_on_hand, price, created_date, update_date, version)
VALUES ('Chimay Blue', 'Belgian Strong Ale', '83783375219', 100, 12.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO beer (beer_name, beer_style, upc, quantity_on_hand, price, created_date, update_date, version)
VALUES ('Duvel', 'Belgian Golden Ale', '83783375220', 90, 11.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- German Beers
INSERT INTO beer (beer_name, beer_style, upc, quantity_on_hand, price, created_date, update_date, version)
VALUES ('Weihenstephaner Hefeweissbier', 'Hefeweizen', '83783375221', 110, 10.49, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO beer (beer_name, beer_style, upc, quantity_on_hand, price, created_date, update_date, version)
VALUES ('Paulaner Oktoberfest', 'Märzen', '83783375222', 130, 9.79, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- Japanese Beer
INSERT INTO beer (beer_name, beer_style, upc, quantity_on_hand, price, created_date, update_date, version)
VALUES ('Asahi Super Dry', 'Lager', '83783375223', 160, 8.29, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- Mexican Beer
INSERT INTO beer (beer_name, beer_style, upc, quantity_on_hand, price, created_date, update_date, version)
VALUES ('Corona Extra', 'Lager', '83783375224', 240, 7.49, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- This script inserts 12 famous beers from different countries and of different styles
-- Each beer has a unique UPC, realistic price, and quantity on hand
-- The version field is initialized to 0 for all beers
-- Created and updated timestamps are set to the current time