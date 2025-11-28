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

function abrirModalAlterarNome(evento) {
  if (evento) evento.preventDefault();
  document.getElementById('modalAlterarNome').classList.add('ativo');
  fecharMenuUsuario();
}

function fecharModalAlterarNome() {
  document.getElementById('modalAlterarNome').classList.remove('ativo');
}

function abrirModalLogout(evento) {
  if (evento) evento.preventDefault();
  document.getElementById('modalLogout').classList.add('ativo');
  fecharMenuUsuario();
}

function fecharModalLogout() {
  document.getElementById('modalLogout').classList.remove('ativo');
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

// Validação do formulário de responder questionário
document.addEventListener('DOMContentLoaded', function () {
  const formularioQuestionario = document.querySelector('.formulario-questionario');

  if (formularioQuestionario) {
    formularioQuestionario.addEventListener('submit', function (evento) {
      // Verifica se todas as questões foram respondidas
      let todasRespondidas = true;
      let questaoNaoRespondida = 0;

      // Verifica questões de alternativa (radio buttons)
      const questoesRadio = formularioQuestionario.querySelectorAll('.alternativas');
      questoesRadio.forEach((questao, index) => {
        const radios = questao.querySelectorAll('input[type="radio"]');
        const algumMarcado = Array.from(radios).some(radio => radio.checked);
        if (!algumMarcado && todasRespondidas) {
          todasRespondidas = false;
          questaoNaoRespondida = index + 1;
        }
      });

      // Verifica questões dissertativas
      const questoesDissertativas = formularioQuestionario.querySelectorAll('.campo-dissertativa');
      questoesDissertativas.forEach((campo, index) => {
        if (campo.value.trim() === '' && todasRespondidas) {
          todasRespondidas = false;
          questaoNaoRespondida = questoesRadio.length + index + 1;
        }
      });

      if (!todasRespondidas) {
        evento.preventDefault();
        alert(`Por favor, responda a questão ${questaoNaoRespondida} antes de enviar.`);
        return false;
      }

      // Confirmação antes de enviar
      if (!confirm('Tem certeza que deseja enviar suas respostas? Você não poderá alterá-las depois.')) {
        evento.preventDefault();
        return false;
      }
    });
  }
});
