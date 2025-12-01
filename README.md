# 🎯 Quizisso - Sistema de Questionários

Projeto desenvolvido para a disciplina de **Programação Orientada a Objetos**, aplicando os conhecimentos da aula de 3/11 sobre **Spring Boot**, **Thymeleaf** e desenvolvimento web com Java.

## 📋 Sobre o Projeto

**Quizisso** é um sistema de questionários online que permite aos usuários visualizarem, criarem e responderem questionários. O projeto implementa uma landing page moderna com menu de navegação e páginas auxiliares, utilizando as melhores práticas de desenvolvimento web com Spring Framework.

## ✨ Funcionalidades

- 🏠 **Landing Page**: Página inicial exibindo todos os questionários disponíveis com informações do criador
- 📚 **Meus Questionários**: Visualização e criação dos questionários criados pelo usuário
- ✅ **Questionários Respondidos**: Histórico de questionários já respondidos com notas e criador
- 📝 **Responder Questionário**: Interface para responder questões de múltipla escolha e dissertativas
- ✏️ **Criar/Editar Questionário**: Formulário unificado para criação e edição de questionários com adição/remoção de questões
- 📊 **Visualizar Resultados**: Visualização detalhada de todos os resultados de um questionário com dados dos respondentes e métricas
-  **Menu de Usuário**: Opções para alterar foto, nome e deslogar (com modais de confirmação)
- 🗑️ **Exclusão com Confirmação**: Modais de confirmação para excluir questionários e resultados
- 🎨 **Design Responsivo**: Interface adaptável para diferentes tamanhos de tela com breakpoint em 768px
  - Layout mobile otimizado com logo à esquerda e perfil à direita
  - Nome do usuário visível em dispositivos móveis
  - Menu de navegação responsivo que se reorganiza automaticamente
- 🔄 **Navegação Dinâmica**: Menu com destaque da página ativa
- 💡 **Sistema de Modais**: Implementação completa com controle de eventos e prevenção de comportamento padrão

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.5.8**
  - Spring Web
  - Spring Data JDBC
  - Spring Boot Validation
  - Thymeleaf
- **PostgreSQL** - Banco de dados relacional
- **Maven** - Gerenciamento de dependências
- **Docker** - Containerização da aplicação
- **HTML5/CSS3** - Estruturação e estilização
- **JavaScript** - Interatividade do frontend
- **Tabler Icons** - Biblioteca de ícones
- **Google Fonts (Cabin & Monofett)** - Tipografia customizada

## 📁 Estrutura do Projeto

```
quizisso/
├── Dockerfile                                 # Configuração Docker para deploy
├── pom.xml                                    # Configuração Maven e dependências
├── mvnw / mvnw.cmd                           # Maven Wrapper
├── diagrama_casos_de_uso.puml                # Diagrama UML de casos de uso
├── diagrama_classes.puml                     # Diagrama UML de classes
├── src/
│   ├── main/
│   │   ├── java/br/com/caiogs06/poo/avaliacao/quiz/
│   │   │   ├── QuizApplication.java          # Classe principal Spring Boot
│   │   │   ├── controller/                   # Camada de Controllers (MVC)
│   │   │   │   ├── BaseController.java       # Controller base com métodos comuns
│   │   │   │   ├── HomeController.java       # Controller da página inicial
│   │   │   │   ├── LoginController.java      # Controller de autenticação
│   │   │   │   ├── MeusQuestionariosController.java
│   │   │   │   ├── FormularioQuestionarioController.java
│   │   │   │   ├── ResponderQuestionarioController.java
│   │   │   │   ├── QuestionariosRespondidosController.java
│   │   │   │   ├── VisualizarResultadoController.java
│   │   │   │   └── VisualizarResultadosController.java
│   │   │   ├── model/                        # Camada de Model (Entidades)
│   │   │   │   ├── Usuario.java              # Entidade de usuário
│   │   │   │   ├── Questionario.java         # Entidade de questionário
│   │   │   │   ├── Item.java                 # Classe abstrata para questões
│   │   │   │   ├── QuestaoAlternativa.java   # Questão de múltipla escolha
│   │   │   │   ├── QuestaoDissertativa.java  # Questão dissertativa
│   │   │   │   ├── Alternativa.java          # Alternativa de questão
│   │   │   │   ├── Resposta.java             # Classe abstrata para respostas
│   │   │   │   ├── RespostaAlternativa.java  # Resposta de múltipla escolha
│   │   │   │   ├── RespostaDissertativa.java # Resposta dissertativa
│   │   │   │   ├── ResultadoQuestionario.java # Resultado final
│   │   │   │   └── Imagens.java              # Entidade de imagens
│   │   │   ├── repository/                   # Camada de Repository (DAO)
│   │   │   │   ├── UsuarioDAO.java           # Acesso a dados de usuários
│   │   │   │   ├── QuestionarioDAO.java      # Acesso a dados de questionários
│   │   │   │   ├── ItemDAO.java              # Acesso a dados de itens/questões
│   │   │   │   ├── AlternativaDAO.java       # Acesso a dados de alternativas
│   │   │   │   ├── RespostaDAO.java          # Acesso a dados de respostas
│   │   │   │   └── ResultadoDAO.java         # Acesso a dados de resultados
│   │   │   └── service/                      # Camada de Service (Regras de negócio)
│   │   │       ├── UsuarioService.java       # Lógica de negócio de usuários
│   │   │       ├── QuestionarioService.java  # Lógica de negócio de questionários
│   │   │       ├── ItemService.java          # Lógica de negócio de itens
│   │   │       ├── RespostaService.java      # Lógica de negócio de respostas
│   │   │       └── ResultadoService.java     # Lógica de negócio de resultados
│   │   └── resources/
│   │       ├── application.yaml              # Configurações da aplicação
│   │       ├── schema-postgresql.sql         # Schema do banco de dados
│   │       ├── static/                       # Arquivos estáticos
│   │       │   ├── css/
│   │       │   │   └── style.css             # Estilos globais
│   │       │   ├── img/                      # Imagens do projeto
│   │       │   └── js/
│   │       │       └── script.js             # Scripts JavaScript
│   │       └── templates/                    # Templates Thymeleaf
│   │           ├── fragments/
│   │           │   └── cabecalho.html        # Fragmento reutilizável do header
│   │           ├── login.html                # Página de login
│   │           ├── home.html                 # Página inicial (landing page)
│   │           ├── meus-questionarios.html   # Página de questionários do usuário
│   │           ├── formulario-questionario.html # Página unificada de criação/edição
│   │           ├── responder-questionario.html # Página para responder
│   │           ├── questionarios-respondidos.html # Histórico de respostas
│   │           ├── visualizar-resultado.html # Visualização de resultado individual
│   │           └── visualizar-resultados.html # Visualização de todos os resultados
│   └── test/
│       └── java/br/com/caiogs06/poo/avaliacao/quiz/
│           └── QuizApplicationTests.java     # Testes da aplicação
└── target/                                    # Diretório de build (gerado)
```

## � Estrutura de Páginas

### Páginas Principais
- `home.html` - Landing page com todos os questionários (6 cards com info do criador)
- `meus-questionarios.html` - Questionários criados pelo usuário (3 cards + botão criar)
- `questionarios-respondidos.html` - Questionários respondidos com notas (4 cards)

### Páginas de Ação
- `responder-questionario.html` - Interface para responder questionários (múltipla escolha + dissertativa)
- `formulario-questionario.html` - Criação e edição de questionários com modal de exclusão
- `visualizar-resultado.html` - Visualização de resultado individual com gabarito e modal de exclusão
- `visualizar-resultados.html` - Visualização de todos os resultados de um questionário com dados dos respondentes

### Componentes
- `fragments/cabecalho.html` - Fragment Thymeleaf com:
  - Header e navegação responsiva
  - Menu de usuário com foto e nome
  - Exibição do nome do usuário em mobile
  - 3 modais integrados (alterar foto, alterar nome, logout)
  - Estrutura otimizada para reutilização via `th:fragment`

## 🏗️ Arquitetura do Projeto

O projeto segue a arquitetura **MVC (Model-View-Controller)** em camadas:

### **Controller** (Camada de Apresentação)
Responsável por receber as requisições HTTP, processar e retornar as views apropriadas:
- `BaseController.java` - Métodos comuns compartilhados entre controllers
- `LoginController.java` - Autenticação de usuários
- `HomeController.java` - Página inicial com listagem de questionários
- `MeusQuestionariosController.java` - Gerenciamento de questionários do usuário
- `FormularioQuestionarioController.java` - Criação e edição de questionários
- `ResponderQuestionarioController.java` - Interface para responder questionários
- `QuestionariosRespondidosController.java` - Histórico de questionários respondidos
- `VisualizarResultadoController.java` - Visualização individual de resultado
- `VisualizarResultadosController.java` - Visualização de todos os resultados de um questionário

### **Service** (Camada de Negócio)
Contém a lógica de negócio da aplicação:
- `UsuarioService.java` - Validação e operações com usuários
- `QuestionarioService.java` - Validação e operações com questionários
- `ItemService.java` - Gerenciamento de questões (alternativas e dissertativas)
- `RespostaService.java` - Processamento de respostas
- `ResultadoService.java` - Cálculo e armazenamento de resultados

### **Repository/DAO** (Camada de Persistência)
Acesso e manipulação dos dados no banco PostgreSQL usando Spring Data JDBC:
- `UsuarioDAO.java` - CRUD de usuários
- `QuestionarioDAO.java` - CRUD de questionários
- `ItemDAO.java` - CRUD de itens/questões
- `AlternativaDAO.java` - CRUD de alternativas
- `RespostaDAO.java` - CRUD de respostas
- `ResultadoDAO.java` - CRUD de resultados

### **Model** (Camada de Entidades)
Classes que representam as entidades do domínio:
- **Usuario** - Dados do usuário (nome, email, senha, foto)
- **Questionario** - Informações do questionário (título, descrição, criador)
- **Item** (abstrato) - Base para questões
  - **QuestaoAlternativa** - Questão de múltipla escolha
  - **QuestaoDissertativa** - Questão aberta/dissertativa
- **Alternativa** - Opções de resposta para questões alternativas
- **Resposta** (abstrato) - Base para respostas
  - **RespostaAlternativa** - Resposta de múltipla escolha
  - **RespostaDissertativa** - Resposta dissertativa
- **ResultadoQuestionario** - Resultado final com nota e estatísticas
- **Imagens** - Armazenamento de imagens associadas

## 🎯 Como Usar

### Pré-requisitos

- Java JDK 21 ou superior
- Maven 3.6+ (ou usar o wrapper `mvnw` incluído)
- PostgreSQL 12+ (para executar localmente)
- Docker (opcional, para executar via container)

### Passos para Execução Local

1. **Clone o repositório**
```bash
git clone https://github.com/CaioGS06/quizisso.git
cd quizisso
```

2. **Configure o banco de dados PostgreSQL**
   
Crie um banco de dados PostgreSQL e atualize as configurações em `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/seu_banco
    username: seu_usuario
    password: sua_senha
```

3. **Execute o projeto**
```bash
./mvnw spring-boot:run
```
Ou no Windows:
```cmd
mvnw.cmd spring-boot:run
```

4. **Acesse no navegador**
```
http://localhost:8080
```

### Execução com Docker

1. **Build da imagem Docker**
```bash
docker build -t quizisso:latest .
```

2. **Execute o container**
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/seu_banco \
  -e SPRING_DATASOURCE_USERNAME=seu_usuario \
  -e SPRING_DATASOURCE_PASSWORD=sua_senha \
  quizisso:latest
```

3. **Acesse no navegador**
```
http://localhost:8080
```

## 🎨 Características Técnicas Implementadas

### 1. **Fragmentos Thymeleaf**
Utilização de fragmentos reutilizáveis para o cabeçalho, seguindo as boas práticas do Thymeleaf. Os modais são incluídos dentro do fragmento principal para garantir que sejam carregados em todas as páginas:

```html
<div th:fragment="cabecalho">
  <header class="cabecalho">
    <!-- Conteúdo do header -->
  </header>
  <!-- Modais incluídos no fragmento -->
  <div id="modalAlterarFoto" class="modal">...</div>
  <div id="modalAlterarNome" class="modal">...</div>
  <div id="modalLogout" class="modal">...</div>
</div>
```

Uso nas páginas:
```html
<th:block th:replace="~{fragments/cabecalho :: cabecalho}"></th:block>
```

### 2. **Controller com Model Attributes**
Passagem de atributos dinâmicos para controlar o estado da navegação:

```java
@GetMapping("/")
public String home(Model model) {
    model.addAttribute("paginaAtual", "home");
    return "home";
}
```

### 3. **Menu Dinâmico com Thymeleaf**
Destaque automático da página ativa usando expressões condicionais:

```html
<a href="/" class="link-navegacao" 
   th:classappend="${paginaAtual == 'home'} ? 'active' : ''">
```

### 4. **CSS Responsivo**
Design adaptativo com media queries para diferentes resoluções. O layout se reorganiza automaticamente em telas menores que 768px:

```css
@media (max-width: 768px) {
  .cabecalho {
    flex-wrap: wrap;
  }
  
  .logo {
    order: 1;
    flex: 0 0 auto;
  }
  
  .secao-usuario {
    order: 2;
    flex: 0 0 auto;
  }
  
  .menu-navegacao {
    order: 3;
    flex-basis: 100%; /* Força quebra de linha */
    margin-top: 15px;
  }
  
  .nome-usuario-mobile {
    display: block; /* Exibe nome em mobile */
  }
}
```

### 5. **JavaScript Vanilla**
Interatividade sem dependências externas para o menu do usuário e sistema de modais:

```javascript
// Abertura de modais com prevenção de comportamento padrão
function abrirModalAlterarFoto(evento) {
  if (evento) evento.preventDefault();
  document.getElementById('modalAlterarFoto').classList.add('ativo');
  fecharMenuUsuario();
}

// Alternância do menu do usuário
function alternarMenuUsuario() {
  const menuUsuario = document.getElementById('menuUsuario');
  menuUsuario.classList.toggle('active');
}

// Fechamento ao clicar fora
window.onclick = function (evento) {
  const modais = document.querySelectorAll('.modal');
  modais.forEach(modal => {
    if (evento.target === modal) {
      modal.classList.remove('ativo');
    }
  });
}
```

## 📚 Conhecimentos Aplicados (Aula 3/11)

- ✅ Configuração de projeto Spring Boot
- ✅ Estrutura MVC (Model-View-Controller) em camadas
- ✅ **Spring Data JDBC** para persistência de dados
- ✅ **PostgreSQL** como banco de dados relacional
- ✅ Roteamento com `@GetMapping` e `@PostMapping`
- ✅ Utilização do Thymeleaf para templates
- ✅ Passagem de dados do Controller para a View com `Model`
- ✅ Injeção de dependências com `@Autowired`
- ✅ Camada de Service para lógica de negócio
- ✅ Camada Repository (DAO) para acesso a dados
- ✅ Validação de dados com Spring Validation
- ✅ Servir arquivos estáticos (CSS, JS, imagens)
- ✅ Organização de recursos em `static/` e `templates/`
- ✅ Navegação entre páginas
- ✅ **Containerização com Docker**

## 🎯 Páginas Implementadas

| Rota | Descrição | Template | Controller |
|------|-----------|----------|------------|
| `/login` | Página de autenticação | `login.html` | `LoginController` |
| `/` | Landing page com todos os questionários | `home.html` | `HomeController` |
| `/meus-questionarios` | Questionários criados pelo usuário | `meus-questionarios.html` | `MeusQuestionariosController` |
| `/criar-questionario` | Criação de novos questionários | `formulario-questionario.html` | `FormularioQuestionarioController` |
| `/editar-questionario/{id}` | Edição de questionário existente | `formulario-questionario.html` | `FormularioQuestionarioController` |
| `/responder-questionario/{id}` | Interface para responder questionário | `responder-questionario.html` | `ResponderQuestionarioController` |
| `/questionarios-respondidos` | Histórico de questionários respondidos | `questionarios-respondidos.html` | `QuestionariosRespondidosController` |
| `/visualizar-resultado/{id}` | Visualização individual de resultado | `visualizar-resultado.html` | `VisualizarResultadoController` |
| `/visualizar-resultados/{id}` | Visualização de todos os resultados | `visualizar-resultados.html` | `VisualizarResultadosController` |
| `/alterar-foto` | Atualização de foto do usuário | - | `BaseController` |
| `/alterar-nome` | Atualização de nome do usuário | - | `BaseController` |
| `/logout` | Logout do sistema | - | `LoginController` |

## 🔧 Melhorias Futuras

- [ ] Sistema de tags/categorias para questionários
- [ ] Filtros e busca avançada
- [ ] Sistema de comentários e avaliações
- [ ] Exportação de resultados (PDF/Excel)
- [ ] Dashboard com estatísticas e gráficos
- [ ] Sistema de notificações
- [ ] Modo escuro (dark mode)
- [ ] API RESTful para integração externa
- [ ] Testes unitários e de integração completos

## 👨‍💻 Autor

**Caio Greiffo Sampaio**
- Curso: Programação Orientada a Objetos
- Instituição: Faculdade de Tecnologia da Baixada Santista "Rubens Lara"
- GitHub: [@CaioGS06](https://github.com/CaioGS06)
- Email: caiogreiffo@gmail.com

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais como parte da disciplina de Programação Orientada a Objetos.

---

⭐ **Projeto desenvolvido aplicando os conhecimentos da aula de Spring Boot e Thymeleaf!**
