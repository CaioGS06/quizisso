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
  const urlParams = new URLSearchParams(window.location.search);
  const questionarioId = urlParams.get('id');

  if (!questionarioId) {
    alert('ID do questionário não encontrado!');
    return;
  }

  fetch(`/excluir-questionario?id=${questionarioId}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    }
  })
    .then(response => response.json())
    .then(data => {
      if (data.sucesso) {
        alert(data.mensagem || 'Questionário excluído com sucesso!');
        window.location.href = '/meus-questionarios';
      } else {
        alert(data.erro || 'Erro ao excluir questionário');
      }
    })
    .catch(error => {
      alert('Erro ao excluir questionário: ' + error.message);
    });
}

function abrirModalExcluirResultado() {
  document.getElementById('modalExcluirResultado').classList.add('ativo');
}

function fecharModalExcluirResultado() {
  document.getElementById('modalExcluirResultado').classList.remove('ativo');
}

function confirmarExclusaoResultado() {
  const urlParams = new URLSearchParams(window.location.search);
  const resultadoId = urlParams.get('id');

  if (!resultadoId) {
    alert('Erro: ID do resultado não encontrado');
    return;
  }

  fetch(`/excluir-resultado?id=${resultadoId}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    }
  })
    .then(response => response.json())
    .then(data => {
      if (data.sucesso) {
        alert('Resultado excluído com sucesso!');
        window.location.href = '/questionarios-respondidos';
      } else {
        alert('Erro ao excluir resultado: ' + data.mensagem);
        fecharModalExcluirResultado();
      }
    })
    .catch(error => {
      alert('Erro ao excluir resultado: ' + error.message);
      fecharModalExcluirResultado();
    });
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

// Funções de Seleção de Banner
let bannerSelecionadoTemp = null;

function abrirModalSelecionarBanner() {
  const bannerAtual = document.getElementById('bannerPreview').src;
  const bannerIdMatch = bannerAtual.match(/banner(\d+)\.jpg/);
  bannerSelecionadoTemp = bannerIdMatch ? bannerIdMatch[1] : '1';

  // Marcar o banner atual
  const radios = document.querySelectorAll('input[name="bannerId"]');
  radios.forEach(radio => {
    if (radio.value === bannerSelecionadoTemp) {
      radio.checked = true;
    }
  });

  document.getElementById('modalSelecionarBanner').classList.add('ativo');
}

function fecharModalSelecionarBanner() {
  document.getElementById('modalSelecionarBanner').classList.remove('ativo');
}

function atualizarBannerPreview(bannerId) {
  bannerSelecionadoTemp = bannerId;
}

function confirmarBanner() {
  if (bannerSelecionadoTemp) {
    const bannerPreview = document.getElementById('bannerPreview');
    bannerPreview.src = '/img/banner' + bannerSelecionadoTemp + '.jpg';
  }
  fecharModalSelecionarBanner();
}

// Funções de Edição de Questionário
function removerQuestao(botao) {
  if (confirm('Tem certeza que deseja remover esta questão?')) {
    botao.closest('.questao-card').remove();
    atualizarNumerosQuestoes();
    atualizarPontuacaoTotal();
  }
}

function atualizarNumerosQuestoes() {
  const questoes = document.querySelectorAll('.questao-card');
  questoes.forEach((questao, index) => {
    const numeroQuestao = questao.querySelector('.questao-numero');
    if (numeroQuestao) {
      numeroQuestao.textContent = 'Questão ' + (index + 1);
    }
    // Atualizar também os names dos radio buttons de alternativas
    const radios = questao.querySelectorAll('input[type="radio"]');
    radios.forEach(radio => {
      radio.name = 'correta' + (index + 1);
    });
  });
}

function atualizarPontuacaoTotal() {
  const questoes = document.querySelectorAll('.questao-card');
  let total = 0;

  questoes.forEach(questao => {
    const campoPontuacao = questao.querySelector('.campo-pontuacao');
    if (campoPontuacao) {
      const pontuacao = parseFloat(campoPontuacao.value) || 0;
      total += pontuacao;
    }
  });

  const pontuacaoTotalElement = document.getElementById('pontuacaoTotal');
  if (pontuacaoTotalElement) {
    pontuacaoTotalElement.textContent = total.toFixed(1);
  }
}

function removerAlternativa(botao) {
  const alternativasContainer = botao.closest('.alternativas-edicao');
  const alternativas = alternativasContainer.querySelectorAll('.alternativa-edicao');

  if (alternativas.length > 2) {
    botao.closest('.alternativa-edicao').remove();
  } else {
    alert('Uma questão de alternativas deve ter pelo menos 2 opções.');
  }
}

function adicionarAlternativa(botao) {
  const questaoCard = botao.closest('.questao-card');
  const alternativasContainer = questaoCard.querySelector('.alternativas-edicao');
  const questaoNumero = Array.from(document.querySelectorAll('.questao-card')).indexOf(questaoCard) + 1;

  const novaAlternativa = document.createElement('div');
  novaAlternativa.className = 'alternativa-edicao';
  novaAlternativa.innerHTML = `
    <input type="radio" name="correta${questaoNumero}">
    <input type="text" class="campo-alternativa" placeholder="Digite a alternativa">
    <button type="button" class="botao-remover-alternativa" onclick="removerAlternativa(this)">
      <i class="ti ti-x"></i>
    </button>
  `;

  // Inserir antes do botão "Adicionar Alternativa"
  botao.parentElement.insertBefore(novaAlternativa, botao);
}

function trocarTipoQuestao(selectElement) {
  const questaoCard = selectElement.closest('.questao-card');
  const tipoAtual = questaoCard.querySelector('.alternativas-edicao, .campo-gabarito');
  const novoTipo = selectElement.value;

  // Se já existe conteúdo e está trocando o tipo
  if (tipoAtual) {
    const temConteudo = tipoAtual.classList.contains('alternativas-edicao')
      ? tipoAtual.querySelectorAll('.campo-alternativa').length > 0
      : tipoAtual.value.trim() !== '';

    if (temConteudo && !confirm('Trocar o tipo da questão apagará os dados já preenchidos. Deseja continuar?')) {
      // Reverter seleção
      selectElement.value = tipoAtual.classList.contains('alternativas-edicao') ? 'alternativa' : 'dissertativa';
      return;
    }
  }

  // Remover conteúdo atual
  const alternativasDiv = questaoCard.querySelector('.alternativas-edicao');
  const gabaritoTextarea = questaoCard.querySelector('.campo-gabarito');
  if (alternativasDiv) alternativasDiv.remove();
  if (gabaritoTextarea) gabaritoTextarea.remove();

  const enunciadoInput = questaoCard.querySelector('.campo-enunciado');
  const questaoNumero = Array.from(document.querySelectorAll('.questao-card')).indexOf(questaoCard) + 1;

  if (novoTipo === 'alternativa') {
    // Criar alternativas
    const alternativasDiv = document.createElement('div');
    alternativasDiv.className = 'alternativas-edicao';
    alternativasDiv.innerHTML = `
      <div class="alternativa-edicao">
        <input type="radio" name="correta${questaoNumero}" checked>
        <input type="text" class="campo-alternativa" placeholder="Digite a alternativa">
        <button type="button" class="botao-remover-alternativa" onclick="removerAlternativa(this)">
          <i class="ti ti-x"></i>
        </button>
      </div>
      <div class="alternativa-edicao">
        <input type="radio" name="correta${questaoNumero}">
        <input type="text" class="campo-alternativa" placeholder="Digite a alternativa">
        <button type="button" class="botao-remover-alternativa" onclick="removerAlternativa(this)">
          <i class="ti ti-x"></i>
        </button>
      </div>
      <button type="button" class="botao-adicionar-alternativa" onclick="adicionarAlternativa(this)">
        <i class="ti ti-plus"></i>
        Adicionar Alternativa
      </button>
    `;
    enunciadoInput.after(alternativasDiv);
  } else {
    // Criar gabarito
    const gabaritoTextarea = document.createElement('textarea');
    gabaritoTextarea.className = 'campo-gabarito';
    gabaritoTextarea.placeholder = 'Digite o gabarito/resposta esperada...';
    gabaritoTextarea.rows = 4;
    enunciadoInput.after(gabaritoTextarea);
  }
}

function adicionarQuestao() {
  const formulario = document.querySelector('.formulario-questionario');
  const acoesFormulario = formulario.querySelector('.acoes-formulario');
  const questoes = document.querySelectorAll('.questao-card');
  const novoNumero = questoes.length + 1;

  const novaQuestao = document.createElement('div');
  novaQuestao.className = 'questao-card edicao';
  novaQuestao.innerHTML = `
    <div class="questao-header">
      <span class="questao-numero">Questão ${novoNumero}</span>
      <div class="questao-controles">
        <div class="campo-pontuacao-container">
          <label>Pontuação:</label>
          <input type="number" class="campo-pontuacao" min="0" step="0.5" value="1.0" onchange="atualizarPontuacaoTotal()">
          <span>pts</span>
        </div>
        <select class="select-tipo-questao" onchange="trocarTipoQuestao(this)">
          <option value="alternativa" selected>Alternativa</option>
          <option value="dissertativa">Dissertativa</option>
        </select>
        <button type="button" class="botao-remover-questao" onclick="removerQuestao(this)">
          <i class="ti ti-x"></i>
        </button>
      </div>
    </div>
    <input type="text" class="campo-enunciado" placeholder="Digite o enunciado...">
    <div class="alternativas-edicao">
      <div class="alternativa-edicao">
        <input type="radio" name="correta${novoNumero}" checked>
        <input type="text" class="campo-alternativa" placeholder="Digite a alternativa">
        <button type="button" class="botao-remover-alternativa" onclick="removerAlternativa(this)">
          <i class="ti ti-x"></i>
        </button>
      </div>
      <div class="alternativa-edicao">
        <input type="radio" name="correta${novoNumero}">
        <input type="text" class="campo-alternativa" placeholder="Digite a alternativa">
        <button type="button" class="botao-remover-alternativa" onclick="removerAlternativa(this)">
          <i class="ti ti-x"></i>
        </button>
      </div>
      <button type="button" class="botao-adicionar-alternativa" onclick="adicionarAlternativa(this)">
        <i class="ti ti-plus"></i>
        Adicionar Alternativa
      </button>
    </div>
  `;

  acoesFormulario.before(novaQuestao);
  atualizarPontuacaoTotal();
}

// Validação e submissão do formulário de questionário
document.addEventListener('DOMContentLoaded', function () {
  const formularioQuestionario = document.querySelector('.formulario-questionario');

  if (formularioQuestionario) {
    // Calcular pontuação total inicial
    atualizarPontuacaoTotal();
    // Validação apenas para páginas de criação/edição (que têm campo de título)
    const titulo = document.querySelector('.campo-titulo-edicao');
    if (titulo) {
      formularioQuestionario.addEventListener('submit', function (e) {
        e.preventDefault();

        if (titulo.value.trim() === '') {
          alert('Por favor, digite um título para o questionário.');
          titulo.focus();
          return false;
        }

        const questoes = document.querySelectorAll('.questao-card');
        if (questoes.length === 0) {
          alert('Adicione pelo menos uma questão ao questionário.');
          return false;
        }

        let valido = true;
        questoes.forEach((questao, index) => {
          const enunciado = questao.querySelector('.campo-enunciado');
          if (!enunciado || enunciado.value.trim() === '') {
            alert(`Por favor, preencha o enunciado da Questão ${index + 1}.`);
            enunciado.focus();
            valido = false;
            return;
          }

          const alternativasDiv = questao.querySelector('.alternativas-edicao');
          if (alternativasDiv) {
            // Validar questão de alternativas
            const alternativas = alternativasDiv.querySelectorAll('.campo-alternativa');
            let todasPreenchidas = true;
            alternativas.forEach(alt => {
              if (alt.value.trim() === '') {
                todasPreenchidas = false;
              }
            });

            if (!todasPreenchidas) {
              alert(`Por favor, preencha todas as alternativas da Questão ${index + 1}.`);
              valido = false;
              return;
            }

            if (alternativas.length < 2) {
              alert(`A Questão ${index + 1} deve ter pelo menos 2 alternativas.`);
              valido = false;
              return;
            }

            const radioMarcado = questao.querySelector('input[type="radio"]:checked');
            if (!radioMarcado) {
              alert(`Por favor, marque a alternativa correta da Questão ${index + 1}.`);
              valido = false;
              return;
            }
          } else {
            // Validar questão dissertativa
            const gabarito = questao.querySelector('.campo-gabarito');
            if (!gabarito || gabarito.value.trim() === '') {
              alert(`Por favor, preencha o gabarito da Questão ${index + 1}.`);
              gabarito.focus();
              valido = false;
              return;
            }
          }
        });

        if (valido) {
          // Coletar dados e enviar
          const dados = coletarDadosQuestionario();
          enviarQuestionario(dados);
        }
      });
    }
  }
});

function coletarDadosQuestionario() {
  const titulo = document.querySelector('.campo-titulo-edicao').value.trim();
  const descricao = document.querySelector('.campo-descricao-edicao').value.trim();
  const bannerPreview = document.getElementById('bannerPreview');
  const bannerIdMatch = bannerPreview.src.match(/banner(\d+)\.jpg/);
  const bannerId = bannerIdMatch ? parseInt(bannerIdMatch[1]) : 1;

  const questoes = [];

  document.querySelectorAll('.questao-card').forEach((questaoCard, index) => {
    const enunciado = questaoCard.querySelector('.campo-enunciado').value.trim();
    const tipo = questaoCard.querySelector('.select-tipo-questao').value;
    const pontuacaoMaxima = parseFloat(questaoCard.querySelector('.campo-pontuacao').value) || 1.0;

    const questao = {
      ordem: index + 1,
      enunciado: enunciado,
      tipo: tipo,
      pontuacaoMaxima: pontuacaoMaxima
    };

    if (tipo === 'alternativa') {
      const alternativas = [];
      const alternativasDivs = questaoCard.querySelectorAll('.alternativa-edicao');
      alternativasDivs.forEach((altDiv, altIndex) => {
        const descricao = altDiv.querySelector('.campo-alternativa').value.trim();
        const correta = altDiv.querySelector('input[type="radio"]').checked;
        alternativas.push({
          ordem: altIndex + 1,
          descricao: descricao,
          correta: correta
        });
      });
      questao.alternativas = alternativas;
    } else {
      questao.gabarito = questaoCard.querySelector('.campo-gabarito').value.trim();
    }

    questoes.push(questao);
  });

  return {
    titulo: titulo,
    descricao: descricao,
    bannerId: bannerId,
    questoes: questoes
  };
}

function enviarQuestionario(dados) {
  // Determinar se é criação ou edição baseado no atributo data-modo do formulário
  const form = document.querySelector('.formulario-questionario');
  const modo = form.dataset.modo; // 'criar' ou 'editar'
  const questionarioId = form.dataset.questionarioId;

  const url = modo === 'editar' ? '/editar-questionario' : '/criar-questionario';
  const method = 'POST';

  if (modo === 'editar' && questionarioId) {
    dados.id = parseInt(questionarioId);
  }

  fetch(url, {
    method: method,
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(dados)
  })
    .then(response => {
      if (!response.ok) {
        return response.json().then(err => Promise.reject(err));
      }
      return response.json();
    })
    .then(data => {
      if (data.requerConfirmacao) {
        // Questionário possui resultados, solicitar confirmação
        if (confirm(data.mensagem)) {
          // Usuário confirmou, enviar novamente com flag de confirmação
          dados.confirmarExclusao = true;
          enviarQuestionario(dados);
        }
      } else if (data.sucesso) {
        alert(data.mensagem || 'Questionário salvo com sucesso!');
        window.location.href = '/meus-questionarios';
      } else {
        alert(data.erro || 'Erro ao salvar questionário');
      }
    })
    .catch(error => {
      alert('Erro ao salvar questionário: ' + (error.erro || error.message));
    });
}

// Funções de Correção de Resultados
function atualizarNotaFinal() {
  const campos = document.querySelectorAll('.campo-pontuacao-correcao');
  let notaTotal = 0;

  campos.forEach(campo => {
    const valor = parseFloat(campo.value) || 0;
    notaTotal += valor;
  });

  // Buscar pontuação máxima total
  let pontuacaoMaxima = 0;
  campos.forEach(campo => {
    const max = parseFloat(campo.getAttribute('data-pontuacao-maxima')) || 0;
    pontuacaoMaxima += max;
  });

  // Atualizar display da nota
  const display = document.getElementById('notaFinalDisplay');
  if (display) {
    display.textContent = notaTotal.toFixed(1) + '/' + pontuacaoMaxima.toFixed(1);
  }
}

function salvarCorrecao() {
  const urlParams = new URLSearchParams(window.location.search);
  const resultadoId = urlParams.get('id');

  if (!resultadoId) {
    alert('Erro: ID do resultado não encontrado');
    return;
  }

  // Coletar todas as pontuações alteradas
  const pontuacoes = {};
  const campos = document.querySelectorAll('.campo-pontuacao-correcao');
  let validacaoOk = true;

  campos.forEach(campo => {
    const itemId = campo.getAttribute('data-item-id');
    const pontuacao = parseFloat(campo.value) || 0;
    const pontuacaoMaxima = parseFloat(campo.getAttribute('data-pontuacao-maxima')) || 0;

    // Validar que a pontuação não excede o máximo
    if (pontuacao > pontuacaoMaxima) {
      alert(`A pontuação para a questão não pode exceder ${pontuacaoMaxima} pontos`);
      validacaoOk = false;
      return;
    }

    pontuacoes[itemId] = pontuacao;
  });

  // Verificar se a validação passou
  if (!validacaoOk) {
    return;
  }

  // Enviar para o servidor
  fetch('/salvar-correcao', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      resultadoId: resultadoId,
      pontuacoes: pontuacoes
    })
  })
    .then(response => response.json())
    .then(data => {
      if (data.sucesso) {
        alert(data.mensagem || 'Correção salva com sucesso!');
        // Atualizar estatísticas antes de redirecionar
        if (data.quantidadeAcertos !== undefined && data.quantidadeErros !== undefined) {
          // Atualizar os números no resumo
          const acertosElement = document.querySelector('.resumo-resultado .estatistica:nth-child(1) strong');
          const errosElement = document.querySelector('.resumo-resultado .estatistica:nth-child(2) strong');

          if (acertosElement) acertosElement.textContent = data.quantidadeAcertos;
          if (errosElement) errosElement.textContent = data.quantidadeErros;

          // Aguardar um momento para o usuário ver a atualização
          setTimeout(() => {
            const questionarioId = data.questionarioId;
            if (questionarioId) {
              window.location.href = '/visualizar-resultados?id=' + questionarioId;
            } else {
              location.reload();
            }
          }, 800);
        } else {
          // Redirecionar imediatamente se não houver dados de estatísticas
          const questionarioId = data.questionarioId;
          if (questionarioId) {
            window.location.href = '/visualizar-resultados?id=' + questionarioId;
          } else {
            location.reload();
          }
        }
      } else {
        alert(data.mensagem || 'Erro ao salvar correção');
      }
    })
    .catch(error => {
      alert('Erro ao salvar correção: ' + error.message);
    });
}
