DELETE FROM cars;
DELETE FROM rentals;

CREATE TABLE IF NOT EXISTS cars (
                      id BIGINT PRIMARY KEY,
                      model VARCHAR(50),
                      brand VARCHAR(50),
                      car_type VARCHAR(50),
                      inventory INT,
                      fee_usd BIGINT,
                      is_deleted BOOLEAN
);

CREATE TABLE IF NOT EXISTS rentals (
                         id BIGINT PRIMARY KEY,
                         rental_date DATE,
                         return_date DATE,
                         actual_return_date DATE,
                         car_id BIGINT,
                         user_id BIGINT,
                         is_deleted BOOLEAN,
                         is_active BOOLEAN
);

INSERT INTO cars (
    id,
    model,
    brand,
    car_type,
    inventory,
    fee_usd,
    is_deleted
) VALUES (
             1,
             'f10',
             'bmw',
             'SEDAN',
             100,
             500,
             false
         );

INSERT INTO cars (
    id,
    model,
    brand,
    car_type,
    inventory,
    fee_usd,
    is_deleted
) VALUES (
             2,
             'f90',
             'bmw',
             'SEDAN',
             100,
             700,
             false
         );

INSERT INTO rentals (
    id,
    rental_date,
    return_date,
    actual_return_date,
    car_id,
    user_id,
    is_deleted,
    is_active
) VALUES (
             1,
             '2024-12-11',
             '2024-12-15',
            '2024-12-18',
             1,
             2,
             false,
             true
         );

INSERT INTO rentals (
    id,
    rental_date,
    return_date,
    actual_return_date,
    car_id,
    user_id,
    is_deleted,
    is_active
) VALUES (
             2,
             '2024-12-11',
             '2024-12-15',
             '2024-12-17',
             2,
             2,
             false,
             true
         );
