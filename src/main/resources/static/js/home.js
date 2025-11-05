function alternarMenuUsuario() {
  const menuUsuario = document.getElementById('menuUsuario');
  menuUsuario.classList.toggle('active');
}

// Fecha o menu quando clicar fora dele
document.addEventListener('click', function (evento) {
  const secaoUsuario = document.querySelector('.secao-usuario');
  const menuUsuario = document.getElementById('menuUsuario');

  if (secaoUsuario && !secaoUsuario.contains(evento.target)) {
    menuUsuario.classList.remove('active');
  }
});

// Modais de Exclusão
function abrirModalExcluirQuestionario() {
  document.getElementById('modalExcluirQuestionario').classList.add('ativo');
}

function fecharModalExcluirQuestionario() {
  document.getElementById('modalExcluirQuestionario').classList.remove('ativo');
}

function confirmarExclusaoQuestionario() {
  // Lógica de exclusão aqui
  alert('Questionário excluído!');
  window.location.href = '/meus-questionarios';
}

function abrirModalExcluirResultado() {
  document.getElementById('modalExcluirResultado').classList.add('ativo');
}

function fecharModalExcluirResultado() {
  document.getElementById('modalExcluirResultado').classList.remove('ativo');
}

function confirmarExclusaoResultado() {
  // Lógica de exclusão aqui
  alert('Resultado excluído!');
  window.location.href = '/questionarios-respondidos';
}

// Modais de Usuário
function abrirModalAlterarFoto(evento) {
  if (evento) evento.preventDefault();
  document.getElementById('modalAlterarFoto').classList.add('ativo');
  fecharMenuUsuario();
}

function fecharModalAlterarFoto() {
  document.getElementById('modalAlterarFoto').classList.remove('ativo');
}

function confirmarAlterarFoto() {
  // Lógica de alteração de foto aqui
  alert('Foto alterada com sucesso!');
  fecharModalAlterarFoto();
}

function abrirModalAlterarNome(evento) {
  if (evento) evento.preventDefault();
  document.getElementById('modalAlterarNome').classList.add('ativo');
  fecharMenuUsuario();
}

function fecharModalAlterarNome() {
  document.getElementById('modalAlterarNome').classList.remove('ativo');
}

function confirmarAlterarNome() {
  // Lógica de alteração de nome aqui
  const novoNome = document.getElementById('campoNovoNome').value;
  if (novoNome.trim()) {
    alert('Nome alterado para: ' + novoNome);
    fecharModalAlterarNome();
  }
}

function abrirModalLogout(evento) {
  if (evento) evento.preventDefault();
  document.getElementById('modalLogout').classList.add('ativo');
  fecharMenuUsuario();
}

function fecharModalLogout() {
  document.getElementById('modalLogout').classList.remove('ativo');
}

function confirmarLogout() {
  // Lógica de logout aqui
  alert('Logout realizado!');
  window.location.href = '/';
}

function fecharMenuUsuario() {
  document.getElementById('menuUsuario').classList.remove('active');
}

// Funções de Edição de Questionário
function removerQuestao(botao) {
  if (confirm('Tem certeza que deseja remover esta questão?')) {
    botao.closest('.questao-card').remove();
  }
}

function removerAlternativa(botao) {
  const alternativasContainer = botao.closest('.alternativas-edicao');
  const alternativas = alternativasContainer.querySelectorAll('.alternativa-edicao');

  if (alternativas.length > 2) {
    botao.closest('.alternativa-edicao').remove();
  } else {
    alert('É necessário ter pelo menos 2 alternativas!');
  }
}

function adicionarQuestao() {
  alert('Adicionar nova questão (funcionalidade a ser implementada)');
}

// Fecha modais ao clicar fora deles
window.onclick = function (evento) {
  const modais = document.querySelectorAll('.modal');
  modais.forEach(modal => {
    if (evento.target === modal) {
      modal.classList.remove('ativo');
    }
  });
}
