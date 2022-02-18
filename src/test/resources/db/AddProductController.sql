INSERT INTO T_CART(id, tenant_id, merchant_id, account_id)
    VALUES
        (100, 1, 100, 1)
    ;

INSERT INTO T_PRODUCT(cart_fk, product_id, quantity)
    VALUES
        (100, 2, 2)
    ;
