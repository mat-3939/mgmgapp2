CREATE TABLE IF NOT EXISTS admins (
    -- 管理者ID（主キー）
    id SERIAL PRIMARY KEY,
    
    -- 管理者名（not null、一意）
    user_name VARCHAR(50) NOT NULL UNIQUE,
    
    -- 管理者パスワード（not null、一意）
    password VARCHAR(255) NOT NULL UNIQUE
);
