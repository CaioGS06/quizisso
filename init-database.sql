-- =============================================
-- SCRIPT DE INICIALIZAÇÃO DO BANCO DE DADOS
-- Execute este script UMA VEZ no banco do Render
-- =============================================

-- =============================================
-- CRIAÇÃO DAS TABELAS
-- =============================================

-- Tabela de Usuários
CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    foto_id INT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Questionários
CREATE TABLE IF NOT EXISTS questionario (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    descricao TEXT,
    criador_id INT NOT NULL,
    banner_id INT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_questionario_criador FOREIGN KEY (criador_id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Tabela de Itens (Questões) com discriminator (tipo)
CREATE TABLE IF NOT EXISTS item (
    id SERIAL PRIMARY KEY,
    questionario_id INT NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('DISSERTATIVA', 'ALTERNATIVA')),
    enunciado TEXT NOT NULL,
    gabarito TEXT,
    pontuacao_maxima NUMERIC(10,2) NOT NULL DEFAULT 1.00,
    ordem INT NOT NULL,
    CONSTRAINT fk_item_questionario FOREIGN KEY (questionario_id) REFERENCES questionario(id) ON DELETE CASCADE
);

-- Tabela de Alternativas (apenas para itens do tipo ALTERNATIVA)
CREATE TABLE IF NOT EXISTS alternativa (
    id SERIAL PRIMARY KEY,
    item_id INT NOT NULL,
    descricao TEXT NOT NULL,
    esta_correta BOOLEAN NOT NULL DEFAULT FALSE,
    ordem INT NOT NULL,
    CONSTRAINT fk_alternativa_item FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE
);

-- Tabela de Resultados (submissões de questionários)
CREATE TABLE IF NOT EXISTS resultado_questionario (
    id SERIAL PRIMARY KEY,
    questionario_id INT NOT NULL,
    respondente_id INT NOT NULL,
    nota_final NUMERIC(5,2),
    data_submissao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (questionario_id, respondente_id),
    CONSTRAINT fk_resultado_questionario_q FOREIGN KEY (questionario_id) REFERENCES questionario(id) ON DELETE CASCADE,
    CONSTRAINT fk_resultado_questionario_user FOREIGN KEY (respondente_id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Tabela de Respostas
CREATE TABLE IF NOT EXISTS resposta (
    id SERIAL PRIMARY KEY,
    resultado_id INT NOT NULL,
    item_id INT NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('DISSERTATIVA', 'ALTERNATIVA')),
    texto_resposta TEXT,
    alternativa_id INT NULL,
    pontuacao_obtida NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    UNIQUE (resultado_id, item_id),
    CONSTRAINT fk_resposta_resultado FOREIGN KEY (resultado_id) REFERENCES resultado_questionario(id) ON DELETE CASCADE,
    CONSTRAINT fk_resposta_item FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE,
    CONSTRAINT fk_resposta_alternativa FOREIGN KEY (alternativa_id) REFERENCES alternativa(id) ON DELETE SET NULL
);

-- =============================================
-- ÍNDICES
-- =============================================

CREATE INDEX IF NOT EXISTS idx_questionario_criador ON questionario(criador_id);
CREATE INDEX IF NOT EXISTS idx_item_questionario ON item(questionario_id);
CREATE INDEX IF NOT EXISTS idx_alternativa_item ON alternativa(item_id);
CREATE INDEX IF NOT EXISTS idx_resultado_questionario ON resultado_questionario(questionario_id);
CREATE INDEX IF NOT EXISTS idx_resultado_respondente ON resultado_questionario(respondente_id);
CREATE INDEX IF NOT EXISTS idx_resposta_resultado ON resposta(resultado_id);
CREATE INDEX IF NOT EXISTS idx_resposta_item ON resposta(item_id);

-- =============================================
-- DADOS INICIAIS
-- =============================================

-- Inserir Usuários (apenas se não existirem)
INSERT INTO usuario (nome, email, senha, foto_id) 
SELECT 'João Silva', 'joao.silva@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCu', 1
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'joao.silva@email.com');

INSERT INTO usuario (nome, email, senha, foto_id) 
SELECT 'Maria Santos', 'maria.santos@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCu', 2
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'maria.santos@email.com');

INSERT INTO usuario (nome, email, senha, foto_id) 
SELECT 'Pedro Oliveira', 'pedro.oliveira@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCu', 3
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'pedro.oliveira@email.com');

INSERT INTO usuario (nome, email, senha, foto_id) 
SELECT 'Ana Costa', 'ana.costa@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCu', 4
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'ana.costa@email.com');
