USE car_rental;

CREATE TABLE IF NOT EXISTS cars (
    id INT AUTO_INCREMENT PRIMARY KEY,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    color VARCHAR(30) NOT NULL,
    price_per_day DECIMAL(10, 2) NOT NULL,
    available BOOLEAN DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS rentals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    car_id INT NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    customer_email VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (car_id) REFERENCES cars(id)
    );


INSERT IGNORE INTO cars (make, model, year, color, price_per_day) VALUES
('Toyota', 'Camry', 2022, 'Silver', 45.99),
('Honda', 'Civic', 2023, 'Blue', 39.99),
('Ford', 'Mustang', 2021, 'Red', 89.99),
('Tesla', 'Model 3', 2023, 'White', 99.99),
('Jeep', 'Wrangler', 2022, 'Black', 79.99);