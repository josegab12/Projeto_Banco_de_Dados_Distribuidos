CREATE DATABASE IF NOT EXISTS ddb_distributed;
USE ddb_distributed;

CREATE TABLE IF NOT EXISTS produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    quantidade INT NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Dados iniciais para teste
INSERT INTO produtos (nome, quantidade, preco) VALUES ('Servidor Nó A', 1, 1500.00);