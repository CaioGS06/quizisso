# 🎯 Quizisso - Sistema de Questionários

Projeto desenvolvido para a disciplina de **Programação Orientada a Objetos**, aplicando os conhecimentos da aula de 3/11 sobre **Spring Boot**, **Thymeleaf** e desenvolvimento web com Java.

## 📋 Sobre o Projeto

**Quizisso** é um sistema de questionários online que permite aos usuários visualizarem, criarem e responderem questionários. O projeto implementa uma landing page moderna com menu de navegação e páginas auxiliares, utilizando as melhores práticas de desenvolvimento web com Spring Framework.

## ✨ Funcionalidades

- 🏠 **Landing Page**: Página inicial exibindo todos os questionários disponíveis com informações do criador
- 📚 **Meus Questionários**: Visualização e criação dos questionários criados pelo usuário
- ✅ **Questionários Respondidos**: Histórico de questionários já respondidos com notas e criador
- 📝 **Responder Questionário**: Interface para responder questões de múltipla escolha e dissertativas
- ✏️ **Editar Questionário**: Sistema completo de edição de questionários com adição/remoção de questões
- � **Visualizar Resultado**: Visualização detalhada de respostas com gabarito e correção
- ➕ **Criar Questionário**: Interface para criação de novos questionários
-  **Menu de Usuário**: Opções para alterar foto, nome e deslogar (com modais de confirmação)
- 🗑️ **Exclusão com Confirmação**: Modais de confirmação para excluir questionários e resultados
- 🎨 **Design Responsivo**: Interface adaptável para diferentes tamanhos de tela com breakpoint em 768px
  - Layout mobile otimizado com logo à esquerda e perfil à direita
  - Nome do usuário visível em dispositivos móveis
  - Menu de navegação responsivo que se reorganiza automaticamente
- 🔄 **Navegação Dinâmica**: Menu com destaque da página ativa
- 💡 **Sistema de Modais**: Implementação completa com controle de eventos e prevenção de comportamento padrão

## 🛠️ Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 3.x**
  - Spring Web
  - Spring Boot DevTools
- **Thymeleaf** - Template engine para renderização de páginas HTML
- **HTML5/CSS3** - Estruturação e estilização
- **JavaScript** - Interatividade do frontend
- **Tabler Icons** - Biblioteca de ícones
- **Google Fonts (Cabin & Monofett)** - Tipografia customizada

## 📁 Estrutura do Projeto

```
quiz/
├── src/
│   ├── main/
│   │   ├── java/br/com/caiogs06/poo/avaliacao/quiz/
│   │   │   ├── QuizApplication.java          # Classe principal
│   │   │   └── controller/
│   │   │       └── HomeController.java       # Controller das rotas
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css             # Estilos globais
│   │       │   ├── img/                      # Imagens do projeto
│   │       │   └── js/
│   │       │       └── script.js             # Scripts JavaScript
│   │       └── templates/
│   │           ├── fragments/
│   │           │   └── cabecalho.html        # Fragmento reutilizável do header
│   │           ├── home.html                 # Página inicial
│   │           ├── meus-questionarios.html   # Página de questionários do usuário
│   │           └── questionarios-respondidos.html
│   └── test/
└── pom.xml
```

## � Estrutura de Páginas

### Páginas Principais
- `home.html` - Landing page com todos os questionários (6 cards com info do criador)
- `meus-questionarios.html` - Questionários criados pelo usuário (3 cards + botão criar)
- `questionarios-respondidos.html` - Questionários respondidos com notas (4 cards)

### Páginas de Ação
- `responder-questionario.html` - Interface para responder questionários (múltipla escolha + dissertativa)
- `editar-questionario.html` - Edição de questionários com modal de exclusão
- `visualizar-resultado.html` - Visualização de resultados com gabarito e modal de exclusão
- `criar-questionario.html` - Criação de novos questionários

### Componentes
- `fragments/cabecalho.html` - Fragment Thymeleaf com:
  - Header e navegação responsiva
  - Menu de usuário com foto e nome
  - Exibição do nome do usuário em mobile
  - 3 modais integrados (alterar foto, alterar nome, logout)
  - Estrutura otimizada para reutilização via `th:fragment`

## 🎯 Como Usar

### Pré-requisitos

- Java JDK 17 ou superior
- Maven 3.6+

### Passos

1. **Clone o repositório**
```bash
git clone https://github.com/seu-usuario/quiz.git
cd quiz
```

2. **Execute o projeto**
```bash
./mvnw spring-boot:run
```
Ou no Windows:
```cmd
mvnw.cmd spring-boot:run
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
- ✅ Estrutura MVC (Model-View-Controller)
- ✅ Roteamento com `@GetMapping`
- ✅ Utilização do Thymeleaf para templates
- ✅ Passagem de dados do Controller para a View com `Model`
- ✅ Servir arquivos estáticos (CSS, JS, imagens)
- ✅ Organização de recursos em `static/` e `templates/`
- ✅ Navegação entre páginas

## 🎯 Páginas Implementadas

| Rota | Descrição | Template |
|------|-----------|----------|
| `/` | Landing page com todos os questionários | `home.html` |
| `/meus-questionarios` | Questionários criados pelo usuário | `meus-questionarios.html` |
| `/questionarios-respondidos` | Histórico de questionários respondidos | `questionarios-respondidos.html` |
| `/alterar-foto` | Redirecionamento para alteração de foto | - |
| `/alterar-nome` | Redirecionamento para alteração de nome | - |
| `/logout` | Logout do sistema | - |

## 🔧 Melhorias Futuras

- [ ] Implementar autenticação de usuários
- [ ] Criar sistema de banco de dados
- [ ] Desenvolver funcionalidade de criação de questionários
- [ ] Adicionar sistema de respostas e correção automática
- [ ] Implementar upload de imagens para questionários

## 👨‍💻 Autor

**Caio Greiffo Sampaio**
- Curso: Programação Orientada a Objetos
- Instituição: Faculdade de Tecnologia da Baixada Santista “Rubens Lara”
- GitHub: [@caiogs06](https://github.com/caiogs06)

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais como parte da disciplina de Programação Orientada a Objetos.

---

⭐ **Projeto desenvolvido aplicando os conhecimentos da aula de Spring Boot e Thymeleaf!**
