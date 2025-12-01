-- =============================================
-- LIMPEZA COMPLETA DO BANCO DE DADOS
-- =============================================

-- Deletar todas as tabelas (ordem reversa devido às foreign keys)
DROP TABLE IF EXISTS resposta CASCADE;
DROP TABLE IF EXISTS resultado_questionario CASCADE;
DROP TABLE IF EXISTS alternativa CASCADE;
DROP TABLE IF EXISTS item CASCADE;
DROP TABLE IF EXISTS questionario CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;

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
    gabarito TEXT, -- para dissertativas (pode ser NULL)
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
    -- se quiser permitir múltiplas submissões, remover a UNIQUE abaixo
    UNIQUE (questionario_id, respondente_id),
    CONSTRAINT fk_resultado_questionario_q FOREIGN KEY (questionario_id) REFERENCES questionario(id) ON DELETE CASCADE,
    CONSTRAINT fk_resultado_questionario_user FOREIGN KEY (respondente_id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Tabela de Respostas (cada resposta pertence a um resultado e a um item)
CREATE TABLE IF NOT EXISTS resposta (
    id SERIAL PRIMARY KEY,
    resultado_id INT NOT NULL,
    item_id INT NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('DISSERTATIVA', 'ALTERNATIVA')),
    texto_resposta TEXT, -- para dissertativa
    alternativa_id INT NULL, -- para alternativa
    pontuacao_obtida NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    UNIQUE (resultado_id, item_id),
    CONSTRAINT fk_resposta_resultado FOREIGN KEY (resultado_id) REFERENCES resultado_questionario(id) ON DELETE CASCADE,
    CONSTRAINT fk_resposta_item FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE,
    CONSTRAINT fk_resposta_alternativa FOREIGN KEY (alternativa_id) REFERENCES alternativa(id) ON DELETE SET NULL
);

-- Índices (mantidos)
CREATE INDEX IF NOT EXISTS idx_questionario_criador ON questionario(criador_id);
CREATE INDEX IF NOT EXISTS idx_item_questionario ON item(questionario_id);
CREATE INDEX IF NOT EXISTS idx_alternativa_item ON alternativa(item_id);
CREATE INDEX IF NOT EXISTS idx_resultado_questionario ON resultado_questionario(questionario_id);
CREATE INDEX IF NOT EXISTS idx_resultado_respondente ON resultado_questionario(respondente_id);
CREATE INDEX IF NOT EXISTS idx_resposta_resultado ON resposta(resultado_id);
CREATE INDEX IF NOT EXISTS idx_resposta_item ON resposta(item_id);

-- =============================================
-- INSERTS DE DADOS INICIAIS
-- =============================================

-- Inserir Usuários (4 usuários)
INSERT INTO usuario (nome, email, senha, foto_id) VALUES
('João Silva', 'joao.silva@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCu', 1),
('Maria Santos', 'maria.santos@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCu', 2),
('Pedro Oliveira', 'pedro.oliveira@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCu', 3),
('Ana Costa', 'ana.costa@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCu', 4);

-- =============================================
-- Questionários do Usuário 1 (João Silva) - 4 questionários
-- =============================================

-- Questionário 1: Java Básico
INSERT INTO questionario (titulo, descricao, criador_id, banner_id) VALUES
('Java Básico', 'Questionário sobre conceitos fundamentais de Java', 1, 1);

INSERT INTO item (questionario_id, tipo, enunciado, gabarito, pontuacao_maxima, ordem) VALUES
(1, 'ALTERNATIVA', 'Qual é a palavra-chave correta para declarar uma classe em Java?', NULL, 1.00, 1),
(1, 'ALTERNATIVA', 'Qual método é o ponto de entrada de um programa Java?', NULL, 1.00, 2),
(1, 'DISSERTATIVA', 'Explique a diferença entre JDK, JRE e JVM.', 'JDK é o kit de desenvolvimento, JRE é o ambiente de execução e JVM é a máquina virtual que executa o bytecode', 2.00, 3),
(1, 'ALTERNATIVA', 'Qual é o tipo primitivo para números inteiros em Java?', NULL, 1.00, 4),
(1, 'DISSERTATIVA', 'O que são modificadores de acesso em Java? Cite três exemplos.', 'Modificadores de acesso controlam a visibilidade de classes, métodos e atributos. Exemplos: public, private, protected', 2.00, 5);

INSERT INTO alternativa (item_id, descricao, esta_correta, ordem) VALUES
(1, 'class', TRUE, 1),
(1, 'Class', FALSE, 2),
(1, 'CLASS', FALSE, 3),
(1, 'define', FALSE, 4),
(2, 'start()', FALSE, 1),
(2, 'main()', TRUE, 2),
(2, 'run()', FALSE, 3),
(2, 'execute()', FALSE, 4),
(4, 'integer', FALSE, 1),
(4, 'int', TRUE, 2),
(4, 'Integer', FALSE, 3),
(4, 'number', FALSE, 4);

-- Questionário 2: Programação Orientada a Objetos
INSERT INTO questionario (titulo, descricao, criador_id, banner_id) VALUES
('POO em Java', 'Avaliação sobre Programação Orientada a Objetos', 1, 2);

INSERT INTO item (questionario_id, tipo, enunciado, gabarito, pontuacao_maxima, ordem) VALUES
(2, 'DISSERTATIVA', 'Explique o conceito de herança em POO.', 'Herança é um mecanismo que permite criar novas classes baseadas em classes existentes, reutilizando código e estabelecendo uma hierarquia', 2.00, 1),
(2, 'ALTERNATIVA', 'Qual palavra-chave é usada para herdar de uma classe em Java?', NULL, 1.00, 2),
(2, 'DISSERTATIVA', 'O que é polimorfismo? Dê um exemplo.', 'Polimorfismo permite que objetos de diferentes classes sejam tratados de forma uniforme. Exemplo: sobrescrita de métodos', 2.00, 3),
(2, 'ALTERNATIVA', 'Qual é a palavra-chave para criar uma classe que não pode ser instanciada?', NULL, 1.00, 4),
(2, 'ALTERNATIVA', 'Qual modificador impede que um método seja sobrescrito?', NULL, 1.00, 5),
(2, 'DISSERTATIVA', 'Explique o conceito de encapsulamento.', 'Encapsulamento é o princípio de ocultar os detalhes internos de implementação e expor apenas uma interface pública', 2.00, 6);

INSERT INTO alternativa (item_id, descricao, esta_correta, ordem) VALUES
(7, 'inherits', FALSE, 1),
(7, 'extends', TRUE, 2),
(7, 'implements', FALSE, 3),
(7, 'super', FALSE, 4),
(9, 'abstract', TRUE, 1),
(9, 'static', FALSE, 2),
(9, 'final', FALSE, 3),
(9, 'private', FALSE, 4),
(10, 'static', FALSE, 1),
(10, 'final', TRUE, 2),
(10, 'const', FALSE, 3),
(10, 'abstract', FALSE, 4);

-- Questionário 3: Estruturas de Dados
INSERT INTO questionario (titulo, descricao, criador_id, banner_id) VALUES
('Estruturas de Dados', 'Quiz sobre estruturas de dados em Java', 1, 3);

INSERT INTO item (questionario_id, tipo, enunciado, gabarito, pontuacao_maxima, ordem) VALUES
(3, 'ALTERNATIVA', 'Qual interface representa uma coleção ordenada que permite duplicatas?', NULL, 1.00, 1),
(3, 'ALTERNATIVA', 'Qual estrutura de dados segue o princípio LIFO (Last In, First Out)?', NULL, 1.00, 2),
(3, 'DISSERTATIVA', 'Qual a diferença entre ArrayList e LinkedList?', 'ArrayList usa array interno (acesso rápido por índice), LinkedList usa nós encadeados (inserção/remoção rápida)', 2.00, 3),
(3, 'ALTERNATIVA', 'Qual coleção não permite elementos duplicados?', NULL, 1.00, 4),
(3, 'DISSERTATIVA', 'Explique o conceito de HashMap em Java.', 'HashMap é uma estrutura que armazena pares chave-valor, permitindo acesso rápido aos valores através das chaves', 2.00, 5);

INSERT INTO alternativa (item_id, descricao, esta_correta, ordem) VALUES
(12, 'Set', FALSE, 1),
(12, 'List', TRUE, 2),
(12, 'Map', FALSE, 3),
(12, 'Queue', FALSE, 4),
(13, 'Queue', FALSE, 1),
(13, 'Stack', TRUE, 2),
(13, 'List', FALSE, 3),
(13, 'Set', FALSE, 4),
(15, 'List', FALSE, 1),
(15, 'Set', TRUE, 2),
(15, 'Map', FALSE, 3),
(15, 'Array', FALSE, 4);

-- Questionário 4: Banco de Dados
INSERT INTO questionario (titulo, descricao, criador_id, banner_id) VALUES
('SQL e Banco de Dados', 'Conhecimentos em SQL e modelagem de dados', 1, 4);

INSERT INTO item (questionario_id, tipo, enunciado, gabarito, pontuacao_maxima, ordem) VALUES
(4, 'ALTERNATIVA', 'Qual comando SQL é usado para recuperar dados de uma tabela?', NULL, 1.00, 1),
(4, 'DISSERTATIVA', 'Explique o que é uma chave primária.', 'Chave primária é um campo ou conjunto de campos que identificam unicamente cada registro em uma tabela', 2.00, 2),
(4, 'ALTERNATIVA', 'Qual cláusula SQL é usada para filtrar resultados?', NULL, 1.00, 3),
(4, 'DISSERTATIVA', 'O que é normalização de banco de dados?', 'Normalização é o processo de organizar dados para reduzir redundância e melhorar a integridade dos dados', 2.00, 4),
(4, 'ALTERNATIVA', 'Qual tipo de JOIN retorna todos os registros quando há correspondência em qualquer uma das tabelas?', NULL, 1.00, 5);

INSERT INTO alternativa (item_id, descricao, esta_correta, ordem) VALUES
(17, 'GET', FALSE, 1),
(17, 'SELECT', TRUE, 2),
(17, 'RETRIEVE', FALSE, 3),
(17, 'FETCH', FALSE, 4),
(19, 'FILTER', FALSE, 1),
(19, 'WHERE', TRUE, 2),
(19, 'HAVING', FALSE, 3),
(19, 'IF', FALSE, 4),
(21, 'INNER JOIN', FALSE, 1),
(21, 'FULL OUTER JOIN', TRUE, 2),
(21, 'LEFT JOIN', FALSE, 3),
(21, 'CROSS JOIN', FALSE, 4);

-- =============================================
-- Questionários do Usuário 2 (Maria Santos) - 3 questionários
-- =============================================

-- Questionário 5: HTML e CSS
INSERT INTO questionario (titulo, descricao, criador_id, banner_id) VALUES
('Fundamentos de HTML e CSS', 'Quiz sobre desenvolvimento web front-end', 2, 5);

INSERT INTO item (questionario_id, tipo, enunciado, gabarito, pontuacao_maxima, ordem) VALUES
(5, 'ALTERNATIVA', 'Qual tag HTML é usada para criar um hiperlink?', NULL, 1.00, 1),
(5, 'ALTERNATIVA', 'Qual propriedade CSS define a cor de fundo?', NULL, 1.00, 2),
(5, 'DISSERTATIVA', 'Explique a diferença entre margin e padding em CSS.', 'Margin é o espaço externo ao redor do elemento, padding é o espaço interno entre o conteúdo e a borda', 2.00, 3),
(5, 'ALTERNATIVA', 'Qual tag HTML5 é usada para definir uma seção de navegação?', NULL, 1.00, 4),
(5, 'DISSERTATIVA', 'O que é o Box Model em CSS?', 'Box Model é o conceito que define como os elementos são renderizados, incluindo content, padding, border e margin', 2.00, 5);

INSERT INTO alternativa (item_id, descricao, esta_correta, ordem) VALUES
(22, '<link>', FALSE, 1),
(22, '<a>', TRUE, 2),
(22, '<href>', FALSE, 3),
(22, '<url>', FALSE, 4),
(23, 'bg-color', FALSE, 1),
(23, 'background-color', TRUE, 2),
(23, 'color-background', FALSE, 3),
(23, 'bgcolor', FALSE, 4),
(25, '<navigation>', FALSE, 1),
(25, '<nav>', TRUE, 2),
(25, '<menu>', FALSE, 3),
(25, '<navbar>', FALSE, 4);

-- Questionário 6: JavaScript
INSERT INTO questionario (titulo, descricao, criador_id, banner_id) VALUES
('JavaScript Essencial', 'Avaliação sobre JavaScript básico', 2, 6);

INSERT INTO item (questionario_id, tipo, enunciado, gabarito, pontuacao_maxima, ordem) VALUES
(6, 'DISSERTATIVA', 'Explique a diferença entre var, let e const.', 'var tem escopo de função, let e const têm escopo de bloco. const não pode ser reatribuído', 2.00, 1),
(6, 'ALTERNATIVA', 'Qual método adiciona um elemento ao final de um array?', NULL, 1.00, 2),
(6, 'ALTERNATIVA', 'Qual operador verifica igualdade de valor e tipo?', NULL, 1.00, 3),
(6, 'DISSERTATIVA', 'O que são arrow functions?', 'Arrow functions são uma sintaxe mais concisa para escrever funções em JavaScript, usando a sintaxe () => {}', 2.00, 4),
(6, 'ALTERNATIVA', 'Qual método converte uma string em um número inteiro?', NULL, 1.00, 5);

INSERT INTO alternativa (item_id, descricao, esta_correta, ordem) VALUES
(28, 'append()', FALSE, 1),
(28, 'push()', TRUE, 2),
(28, 'add()', FALSE, 3),
(28, 'insert()', FALSE, 4),
(29, '==', FALSE, 1),
(29, '===', TRUE, 2),
(29, '=', FALSE, 3),
(29, 'equals()', FALSE, 4),
(31, 'parseInteger()', FALSE, 1),
(31, 'parseInt()', TRUE, 2),
(31, 'toInt()', FALSE, 3),
(31, 'Number.int()', FALSE, 4);

-- Questionário 7: Git e Controle de Versão
INSERT INTO questionario (titulo, descricao, criador_id, banner_id) VALUES
('Git Essencial', 'Conhecimentos sobre controle de versão com Git', 2, 7);

INSERT INTO item (questionario_id, tipo, enunciado, gabarito, pontuacao_maxima, ordem) VALUES
(7, 'ALTERNATIVA', 'Qual comando Git é usado para clonar um repositório?', NULL, 1.00, 1),
(7, 'DISSERTATIVA', 'Explique o que é um commit no Git.', 'Commit é um snapshot do estado do projeto em um determinado momento, com uma mensagem descritiva', 2.00, 2),
(7, 'ALTERNATIVA', 'Qual comando mostra o histórico de commits?', NULL, 1.00, 3),
(7, 'ALTERNATIVA', 'Qual comando cria uma nova branch?', NULL, 1.00, 4),
(7, 'DISSERTATIVA', 'O que é merge em Git?', 'Merge é a operação que combina mudanças de diferentes branches em uma única branch', 2.00, 5);

INSERT INTO alternativa (item_id, descricao, esta_correta, ordem) VALUES
(32, 'git copy', FALSE, 1),
(32, 'git clone', TRUE, 2),
(32, 'git download', FALSE, 3),
(32, 'git get', FALSE, 4),
(34, 'git history', FALSE, 1),
(34, 'git log', TRUE, 2),
(34, 'git show', FALSE, 3),
(34, 'git commits', FALSE, 4),
(35, 'git new branch', FALSE, 1),
(35, 'git branch', TRUE, 2),
(35, 'git create', FALSE, 3),
(35, 'git checkout -b', FALSE, 4);

-- =============================================
-- Questionários do Usuário 3 (Pedro Oliveira) - 2 questionários
-- =============================================

-- Questionário 8: Python Básico
INSERT INTO questionario (titulo, descricao, criador_id, banner_id) VALUES
('Python para Iniciantes', 'Quiz sobre fundamentos de Python', 3, 8);

INSERT INTO item (questionario_id, tipo, enunciado, gabarito, pontuacao_maxima, ordem) VALUES
(8, 'ALTERNATIVA', 'Qual função é usada para exibir texto no console em Python?', NULL, 1.00, 1),
(8, 'DISSERTATIVA', 'Explique a diferença entre lista e tupla em Python.', 'Listas são mutáveis (podem ser alteradas), tuplas são imutáveis (não podem ser alteradas após criação)', 2.00, 2),
(8, 'ALTERNATIVA', 'Qual palavra-chave é usada para definir uma função?', NULL, 1.00, 3),
(8, 'ALTERNATIVA', 'Qual operador é usado para exponenciação em Python?', NULL, 1.00, 4),
(8, 'DISSERTATIVA', 'O que são list comprehensions?', 'List comprehensions são uma forma concisa de criar listas usando uma sintaxe similar a loops em uma única linha', 2.00, 5),
(8, 'ALTERNATIVA', 'Qual método adiciona um elemento ao final de uma lista?', NULL, 1.00, 6);

INSERT INTO alternativa (item_id, descricao, esta_correta, ordem) VALUES
(37, 'echo()', FALSE, 1),
(37, 'print()', TRUE, 2),
(37, 'console.log()', FALSE, 3),
(37, 'write()', FALSE, 4),
(39, 'function', FALSE, 1),
(39, 'def', TRUE, 2),
(39, 'func', FALSE, 3),
(39, 'define', FALSE, 4),
(40, '^', FALSE, 1),
(40, '**', TRUE, 2),
(40, 'pow', FALSE, 3),
(40, '^^', FALSE, 4),
(42, 'add()', FALSE, 1),
(42, 'append()', TRUE, 2),
(42, 'push()', FALSE, 3),
(42, 'insert()', FALSE, 4);

-- Questionário 9: Redes de Computadores
INSERT INTO questionario (titulo, descricao, criador_id, banner_id) VALUES
('Fundamentos de Redes', 'Conceitos básicos de redes de computadores', 3, 9);

INSERT INTO item (questionario_id, tipo, enunciado, gabarito, pontuacao_maxima, ordem) VALUES
(9, 'DISSERTATIVA', 'Explique o que é o protocolo TCP/IP.', 'TCP/IP é um conjunto de protocolos de comunicação usado para interconectar dispositivos de rede na Internet', 2.00, 1),
(9, 'ALTERNATIVA', 'Qual camada do modelo OSI é responsável pelo endereçamento lógico?', NULL, 1.00, 2),
(9, 'ALTERNATIVA', 'Qual porta padrão é usada pelo protocolo HTTP?', NULL, 1.00, 3),
(9, 'DISSERTATIVA', 'O que é DNS?', 'DNS (Domain Name System) é o sistema que traduz nomes de domínio em endereços IP', 2.00, 4),
(9, 'ALTERNATIVA', 'Qual protocolo é usado para transferência segura de arquivos?', NULL, 1.00, 5);

INSERT INTO alternativa (item_id, descricao, esta_correta, ordem) VALUES
(44, 'Camada de Aplicação', FALSE, 1),
(44, 'Camada de Rede', TRUE, 2),
(44, 'Camada de Transporte', FALSE, 3),
(44, 'Camada Física', FALSE, 4),
(45, '8080', FALSE, 1),
(45, '80', TRUE, 2),
(45, '443', FALSE, 3),
(45, '21', FALSE, 4),
(47, 'FTP', FALSE, 1),
(47, 'SFTP', TRUE, 2),
(47, 'HTTP', FALSE, 3),
(47, 'SMTP', FALSE, 4);

-- =============================================
-- Questionários do Usuário 4 (Ana Costa) - 1 questionário
-- =============================================

-- Questionário 10: Spring Boot
INSERT INTO questionario (titulo, descricao, criador_id, banner_id) VALUES
('Spring Boot Fundamentals', 'Conhecimentos sobre Spring Boot e desenvolvimento web', 4, 10);

INSERT INTO item (questionario_id, tipo, enunciado, gabarito, pontuacao_maxima, ordem) VALUES
(10, 'ALTERNATIVA', 'Qual anotação marca uma classe como um controlador REST?', NULL, 1.00, 1),
(10, 'DISSERTATIVA', 'Explique o conceito de Injeção de Dependência no Spring.', 'Injeção de Dependência é um padrão onde o Spring gerencia a criação e fornecimento de objetos necessários para outras classes', 2.00, 2),
(10, 'ALTERNATIVA', 'Qual anotação é usada para mapear requisições HTTP GET?', NULL, 1.00, 3),
(10, 'DISSERTATIVA', 'O que é Spring Data JPA?', 'Spring Data JPA é um módulo que facilita a implementação de repositórios baseados em JPA para acesso a dados', 2.00, 4),
(10, 'ALTERNATIVA', 'Qual arquivo de configuração padrão é usado no Spring Boot?', NULL, 1.00, 5),
(10, 'ALTERNATIVA', 'Qual anotação marca uma classe como um componente gerenciado pelo Spring?', NULL, 1.00, 6);

INSERT INTO alternativa (item_id, descricao, esta_correta, ordem) VALUES
(48, '@Controller', FALSE, 1),
(48, '@RestController', TRUE, 2),
(48, '@Service', FALSE, 3),
(48, '@Component', FALSE, 4),
(50, '@Get', FALSE, 1),
(50, '@GetMapping', TRUE, 2),
(50, '@RequestGet', FALSE, 3),
(50, '@HttpGet', FALSE, 4),
(52, 'config.properties', FALSE, 1),
(52, 'application.properties', TRUE, 2),
(52, 'spring.properties', FALSE, 3),
(52, 'settings.properties', FALSE, 4),
(53, '@Bean', FALSE, 1),
(53, '@Component', TRUE, 2),
(53, '@Managed', FALSE, 3),
(53, '@Spring', FALSE, 4);