-- 商品情報を管理するテーブル
CREATE TABLE IF NOT EXISTS products (
    -- 商品ID（主キー）
    id SERIAL PRIMARY KEY,
    
    -- 商品名（not null、最大30文字、一意）
    name VARCHAR(30) NOT NULL UNIQUE,
    
    -- 商品説明（最大200文字）
    description TEXT,
    
    -- 価格（not null、小数点以下2桁まで）
    price NUMERIC(10, 2) NOT NULL,
    
    -- 在庫数（デフォルト100）
    stock INTEGER DEFAULT 100,
    
    -- 商品画像のパス（not null）
    image_path TEXT NOT NULL,
    
    -- 商品カテゴリID（not null、外部キー参照）
    category_id INTEGER NOT NULL REFERENCES categories(id),
    
    -- 作成日時
    created_at TIMESTAMP WITHOUT TIME ZONE,
    
    -- 更新日時
    updated_at TIMESTAMP WITHOUT TIME ZONE
); 