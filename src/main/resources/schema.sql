CREATE TABLE IF NOT EXISTS task (
    description VARCHAR(64) NOT NULL,
    completed   VARCHAR(30) NOT NULL);

CREATE TABLE IF NOT EXISTS restaurants (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    is_kosher BOOLEAN NOT NULL,
    cuisines varchar[]);


CREATE TABLE IF NOT EXISTS dishes (
    id SERIAL PRIMARY KEY,
    restaurant_id INTEGER NOT NULL REFERENCES restaurants(id),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price NUMERIC(5, 2) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE);


CREATE TABLE IF NOT EXISTS ratings (
    id SERIAL PRIMARY KEY,
    restaurant_id INTEGER NOT NULL REFERENCES restaurants(id),
    rating NUMERIC(3, 1) NOT NULL);


CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY,
    restaurant_id INTEGER NOT NULL REFERENCES restaurants(id));


CREATE TABLE IF NOT EXISTS order_items (
    id SERIAL PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES orders(id),
    dish_id INTEGER NOT NULL REFERENCES dishes(id),
    amount INTEGER NOT NULL);


COMMENT ON COLUMN "restaurants"."cuisines" IS 'Array of cuisines';

COMMENT ON COLUMN "dishes"."description" IS 'Description of the dish';

COMMENT ON COLUMN "dishes"."price" IS 'Price of the dish';

COMMENT ON COLUMN "ratings"."rating" IS 'Rating given by customers';

COMMENT ON COLUMN "order_items"."amount" IS 'Quantity of the dish ordered';