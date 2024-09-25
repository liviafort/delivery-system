CREATE TABLE IF NOT EXISTS Restaurant (
                            id UUID PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            address VARCHAR(255) NOT NULL,
                            category VARCHAR(100) NOT NULL,
                            cnpj VARCHAR(20) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS Restaurant_Item (
                                 id UUID PRIMARY KEY,
                                 restaurant_id UUID NOT NULL,
                                 name VARCHAR(255) NOT NULL,
                                 price DECIMAL(10, 2) NOT NULL,
                                 FOREIGN KEY (restaurant_id) REFERENCES Restaurant(id)
);
CREATE TABLE IF NOT EXISTS Customer (
                          id UUID PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          phone VARCHAR(20) UNIQUE NOT NULL,
                          address VARCHAR(50) NOT NULL
);
CREATE TABLE IF NOT EXISTS Orders (
                        id UUID PRIMARY KEY,
                        restaurant_id UUID NOT NULL,
                        customer_id UUID NOT NULL,
                        tracking_code VARCHAR(10) UNIQUE NOT NULL,
                        total_price DECIMAL(10, 2) NOT NULL,
                        FOREIGN KEY (restaurant_id) REFERENCES Restaurant(id),
                        FOREIGN KEY (customer_id) REFERENCES Customer(id)
);
CREATE TABLE IF NOT EXISTS Order_Item (
                            id UUID PRIMARY KEY,
                            order_id UUID NOT NULL,
                            restaurant_item_id UUID NOT NULL,
                            quantity INT NOT NULL,
                            FOREIGN KEY (order_id) REFERENCES Orders(id),
                            FOREIGN KEY (restaurant_item_id) REFERENCES Restaurant_Item(id)
);
CREATE TABLE IF NOT EXISTS Deliveryman (
                             id UUID PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             phone VARCHAR(20) UNIQUE NOT NULL,
                             vehicle VARCHAR(50) NOT NULL
);
CREATE TABLE IF NOT EXISTS Route (
                       id UUID PRIMARY KEY,
                       destination VARCHAR(255) NOT NULL,
                       deliveryman_id UUID NOT NULL,
                       order_id UUID NOT NULL,
                       status VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
                       identifier VARCHAR(5) UNIQUE NOT NULL,
                       FOREIGN KEY (deliveryman_id) REFERENCES Deliveryman(id),
                       FOREIGN KEY (order_id) REFERENCES Orders(id)
);