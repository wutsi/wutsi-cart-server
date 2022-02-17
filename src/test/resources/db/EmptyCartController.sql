INSERT INTO T_CART(id, tenant_id, merchant_id, account_id)
    VALUES
        (200, 1, 200, 1)
    ;

INSERT INTO T_PRODUCT(cart_fk, product_id, quantity)
    VALUES
        (200, 1, 3)
    ;
