CREATE DATABASE db_user;
CREATE USER ecommerce_user WITH PASSWORD 'ecommerce';
\connect db_user;
GRANT USAGE ON SCHEMA public TO ecommerce_user;
GRANT CREATE ON SCHEMA public TO ecommerce_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO ecommerce_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO ecommerce_user;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO ecommerce_user;
REVOKE ALL PRIVILEGES ON DATABASE postgres FROM ecommerce_user;

CREATE DATABASE db_catalog;
CREATE USER ecommerce_catalog WITH PASSWORD 'ecommerce';
\connect db_catalog;
GRANT USAGE ON SCHEMA public TO ecommerce_catalog;
GRANT CREATE ON SCHEMA public TO ecommerce_catalog;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO ecommerce_catalog;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO ecommerce_catalog;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO ecommerce_catalog;
REVOKE ALL PRIVILEGES ON DATABASE postgres FROM ecommerce_catalog;

CREATE DATABASE db_cart;
CREATE USER ecommerce_cart WITH PASSWORD 'ecommerce';
GRANT ALL PRIVILEGES ON DATABASE db_cart TO ecommerce_cart;

CREATE DATABASE db_order;
CREATE USER ecommerce_order WITH PASSWORD 'ecommerce';
GRANT ALL PRIVILEGES ON DATABASE db_order TO ecommerce_order;

DO $$ BEGIN
  RAISE NOTICE 'Initialization completed successfully!';
END $$;