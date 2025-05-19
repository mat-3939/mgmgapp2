CREATE TABLE IF NOT EXISTS orders (
    -- 注文ID（主キー）
    id SERIAL PRIMARY KEY,
    
    -- 名（not null）
    first_name VARCHAR(30) NOT NULL,
    
    -- 姓（not null）
    last_name VARCHAR(30) NOT NULL,
    
    -- メールアドレス（not null）
    email VARCHAR(50) NOT NULL,
    
    -- 郵便番号（not null）
    postcode VARCHAR(10) NOT NULL,
    
    -- 都道府県（not null）
    prefecture VARCHAR(10) NOT NULL,
    
    -- 市区町村（not null）
    city VARCHAR(50) NOT NULL,
    
    -- 住所（not null）
    address_line VARCHAR(50) NOT NULL,
    
    -- 建物名
    building VARCHAR(50),
    
    -- 電話番号
    tel VARCHAR(50) ,
    
    -- 支払方法
    pay_method VARCHAR(10) DEFAULT 'クレジットカード',

    -- 支払い方法（not null）
    payment_method VARCHAR(10) NOT NULL DEFAULT 'クレジットカード',

    -- 合計金額（not null）
    total_price NUMERIC(10, 2) ,
    
    -- 注文日時（not null）
    order_date TIMESTAMP WITHOUT TIME ZONE,
    
    -- 注文ステータス
    status BOOLEAN DEFAULT 'false'
    );