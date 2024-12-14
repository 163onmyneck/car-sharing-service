INSERT INTO cars (
    id,
    model,
    brand,
    car_type,
    inventory,
    fee_usd,
    is_deleted
    ) values (
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
) values (
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
) values (
          1,
          '2024-12-11',
          '2024-12-15',
          '2024-12-17',
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
) values (
             2,
             '2024-12-11',
             '2024-12-15',
             '2024-12-17',
             2,
             2,
             false,
             true
         );