-- Adiciona colunas na tabela products (uma por vez)
ALTER TABLE products ADD COLUMN IF NOT EXISTS description TEXT;
ALTER TABLE products ADD COLUMN IF NOT EXISTS sku VARCHAR(255);
ALTER TABLE products ADD COLUMN IF NOT EXISTS cost_price NUMERIC(38,2);
ALTER TABLE products ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE NOT NULL;
ALTER TABLE products ADD COLUMN IF NOT EXISTS created_at TIMESTAMP(6);
ALTER TABLE products ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP(6);

-- Adiciona constraint de unicidade no SKU
ALTER TABLE products ADD CONSTRAINT IF NOT EXISTS uc_products_sku UNIQUE (sku);

-- Adiciona colunas na tabela categories
ALTER TABLE categories ADD COLUMN IF NOT EXISTS created_at TIMESTAMP(6);
ALTER TABLE categories ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP(6);

-- Adiciona coluna na tabela carts
ALTER TABLE carts ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'OPEN' NOT NULL;

-- Adiciona coluna na tabela inventory_transactions
ALTER TABLE inventory_transactions ADD COLUMN IF NOT EXISTS reference_id BIGINT;

-- Atualiza os registros existentes para ter uma data de criação (evita nulls)
UPDATE products SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
UPDATE categories SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;