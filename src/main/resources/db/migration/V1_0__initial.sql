CREATE TABLE T_CART(
    id              SERIAL NOT NULL,

    tenant_id       BIGINT NOT NULL,
    merchant_id     BIGINT NOT NULL,
    account_id      BIGINT NOT NULL,

    created         TIMESTAMPTZ NOT NULL DEFAULT now(),

    UNIQUE(tenant_id, merchant_id, account_id),
    PRIMARY KEY (id)
);

CREATE TABLE T_PRODUCT(
    id              SERIAL NOT NULL,

    cart_fk         BIGINT NOT NULL REFERENCES T_CART(id),
    product_id      BIGINT NOT NULL,
    quantity        INT NOT NULL DEFAULT 1,

    created         TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated         TIMESTAMPTZ NOT NULL DEFAULT now(),

    UNIQUE(cart_fk, product_id),
    PRIMARY KEY (id)
);

CREATE OR REPLACE FUNCTION product_updated()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_product_updated
BEFORE UPDATE ON T_PRODUCT
FOR EACH ROW
EXECUTE PROCEDURE product_updated();
