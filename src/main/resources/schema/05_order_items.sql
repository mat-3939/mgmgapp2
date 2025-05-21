CREATE TABLE IF NOT EXISTS order_items (
    -- 注文商品ID（主キー）
    id SERIAL PRIMARY KEY,
    
    -- 注文ID（not null、多対一の関連）
    order_id INTEGER NOT NULL REFERENCES orders(id),
    
    -- 商品名（not null）
    product_name VARCHAR(30) NOT NULL,
    
    -- 注文数量（not null、デフォルト1）
    quantity INTEGER NOT NULL DEFAULT 1 CHECK (quantity > 0),
    
    -- 注文時の商品単価（not null、小数点以下2桁まで）
    price NUMERIC(10, 2) NOT NULL
);