INSERT INTO T_CART(id, tenant_id, merchant_id, account_id)
    VALUES
        (10010, 1, 100, 10),
        (10011, 1, 100, 11),
        (10012, 1, 100, 12),
        (11013, 1, 110, 13),
        (12013, 1, 120, 13),
        (20020, 2, 200, 20)
    ;

INSERT INTO T_PRODUCT(cart_fk, product_id, quantity)
    VALUES
        (10010, 1, 3),

        (10011, 2, 1),

        (11013, 1, 10),
        (11013, 2, 1)
    ;
