# Testes

Tabela dos testes pedidos no enunciado (seção 4) e do cenário próprio da equipe. Todos foram executados **pela interface gráfica**, na versão final do programa, começando cada teste por **Arquivo → Novo planejamento** ou abrindo o arquivo do caso de referência. As capturas estão em `docs/capturas/`.

Valores em reais; no código, todo dinheiro é guardado em centavos (`long`).

| Nº | Teste | Entrada | Resultado esperado | Obtido | Evidência |
|---|---|---|---|---|---|
| 1 | Caso de referência | Participantes Ana (R$ 70,00), Bruno (R$ 60,00) e Carla (R$ 70,00). Despesas: Jantar fixa R$ 90,00 (todos), Mercado fixa R$ 60,00 (todos), Táxi fixa R$ 30,00 (Ana e Carla), Ingresso por pessoa R$ 10,00 (todos) | Ana R$ 75,00, Bruno R$ 60,00, Carla R$ 75,00; custo total R$ 210,00. Ana e Carla excedem em R$ 5,00; Bruno está dentro (total igual ao orçamento) | Igual ao esperado: Ana R$ 75,00 e Carla R$ 75,00 com "Excede em R$ 5,00"; Bruno R$ 60,00 "Dentro do orçamento"; custo total R$ 210,00 | `teste-1-caso.png` |
| 2 | Centavos no rateio | Despesa fixa "Conta" de R$ 100,00 para os participantes de códigos 1, 2 e 3 (Ana, Bruno e Carla) | R$ 33,34 (código 1), R$ 33,33 (código 2) e R$ 33,33 (código 3); soma R$ 100,00 | Ana (código 1) R$ 33,34; Bruno (2) R$ 33,33; Carla (3) R$ 33,33; custo total R$ 100,00 | `teste-2-centavos.png` |
| 3 | Entrada ou vínculo inválido | Com o caso de referência aberto, 11 tentativas inválidas (detalhadas abaixo) e um participante com orçamento zero | Cada tentativa é recusada com mensagem que explica o motivo, e os dados não mudam; orçamento zero é aceito | Todas as 11 tentativas recusadas com mensagem clara; o resumo continuou 75,00 / 60,00 / 75,00 e R$ 210,00. Davi com orçamento R$ 0,00 foi aceito | `teste-3-*.png` (ver detalhe) |
| 4 | Alteração de orçamento | No caso de referência, alterar o orçamento de Ana de R$ 70,00 para R$ 80,00 | Ana continua com total de R$ 75,00 e passa a estar dentro do orçamento; custo total continua R$ 210,00 | Ana R$ 75,00 "Dentro do orçamento"; Carla continua "Excede em R$ 5,00"; custo total R$ 210,00 | `teste-4-orcamento.png` |
| 5 | Exclusão de despesa | No caso de referência, excluir o Táxi (fixa R$ 30,00, Ana e Carla) | Ana, Bruno e Carla passam a R$ 60,00 cada; custo total R$ 180,00 | 60,00 / 60,00 / 60,00; custo total R$ 180,00; "Todos dentro do orçamento". (O orçamento da Ana estava em R$ 80,00, vindo do teste 4; isso não altera os totais.) | `teste-5-exclusao.png` |
| 6 | Salvar e reiniciar | Salvar o caso de referência, fechar o programa, abrir de novo e carregar o arquivo; carregar uma segunda vez; cadastrar o código 1; excluir o Táxi e incluir "Taxa" fixa de R$ 3,00 para os três | Todos os participantes, despesas, vínculos e resultados voltam iguais, sem registros duplicados; o código 1 é recusado; depois, 61,00 para cada | Ao abrir: 3 participantes, 4 despesas, 75,00 / 60,00 / 75,00 e R$ 210,00. Ao abrir de novo: continuaram 3 e 4 (sem duplicar). Código 1: "Já existe um participante com o código 1." Com a Taxa: 61,00 para cada, R$ 183,00, e o Bruno passou a exceder em R$ 1,00 | `teste-6-aberto.png`, `teste-6-taxa.png` |
| 7 | Problema no arquivo | Com um planejamento na tela, abrir um arquivo de texto, um vazio (0 bytes), um truncado (60 bytes) e um que foi apagado; salvar por cima de um arquivo somente leitura; usar Novo planejamento | Mensagem clara, sem travar a aplicação; o planejamento que estava aberto continua igual; é possível iniciar um planejamento vazio | Nos quatro arquivos: janela "Não foi possível abrir" com "Não foi possível carregar o planejamento de …: arquivo ausente ou inválido." e a tela continuou igual. Somente leitura: janela "Não foi possível salvar" e a barra de status não mostrou "salvo". Novo planejamento: com "Não", nada mudou; com "Sim", as três abas ficaram vazias e a barra mostrou "Novo planejamento (vazio)." | `teste-7-lixo.png`, `teste-7-vazio.png`, `teste-7-truncado.png`, `teste-7-inexistente.png`, `teste-7-salvar.png` |
| 8 | Cenário próprio | Ver "Cenário próprio (teste 8)" abaixo | Ver o resultado calculado à mão abaixo | Igual ao cálculo à mão: Andressa 143,50 (excede 43,50), Guilherme 91,34, Luana 141,00, Lucas 107,33, Malu 103,50 (excede 3,50), Samuel 87,33 (excede 7,33); custo total R$ 674,00 | `teste-8-cenario.png` |

## Detalhe do teste 3 — entradas inválidas

Todas feitas com o caso de referência aberto. Depois de cada recusa, o resumo continuou 75,00 / 60,00 / 75,00 e R$ 210,00.

| # | Tentativa | Mensagem obtida | Evidência |
|---|---|---|---|
| a | Cadastrar outro participante com o código 1 | Já existe um participante com o código 1. | `teste-3-codigo-repetido.png` |
| b | Cadastrar participante com orçamento -10 | O orçamento não pode ser negativo (recebido: -1000). | `teste-3-orcamento-negativo.png` |
| c | Cadastrar participante com nome vazio | O nome do participante não pode ser vazio. | `teste-3-nome-vazio-criacao.png` |
| d | Cadastrar participante com nome só com espaços | O nome do participante não pode ser vazio. | `teste-3-nome-espacos.png` (os espaços não aparecem na imagem) |
| e | Alterar o orçamento da Ana para -10 | O orçamento não pode ser negativo (recebido: -1000). | `teste-3-orcamento-negativo-alteracao.png` |
| f | Alterar o nome da Ana para vazio | O nome do participante não pode ser vazio. A Ana continuou na tabela com o nome “Ana”. | `teste-3-nome-vazio-alteracao.png` |
| g | Incluir despesa sem nenhum participante | A despesa precisa ter pelo menos um participante. | `teste-3-sem-participantes.png` |
| h | Incluir despesa de valor 0 | O valor da despesa precisa ser maior que zero (recebido: 0). | `teste-3-valor-zero.png` |
| i | Incluir despesa de valor -5 | O valor da despesa precisa ser maior que zero (recebido: -500). | `teste-3-despesa-valor-negativo-criacao.png` |
| j | Incluir despesa com descrição vazia | A despesa precisa ter uma descrição. | `teste-3-despesa-descricao-vazia-criacao.png` |
| k | Excluir sem nenhuma despesa selecionada | Selecione uma despesa na tabela para excluir. | `teste-3-despesa-excluir-vazia.png` |
| — | Cadastrar Davi (código 4) com orçamento R$ 0,00 | Aceito (zero é um orçamento válido) | `teste-3-participante-orcamento-zero-criacao.png` |

**Observações:**
- Nos itens b, e e i, a mensagem mostra o valor em centavos (-1000 e -500), e não em reais (-10,00 e -5,00). A recusa está correta, mas a mensagem poderia ser mais clara para o usuário; fica registrada como melhoria possível.
- "Participante repetido na mesma despesa" não pode ser provocado pela tela, porque cada participante tem uma única caixa de marcar. A regra existe no modelo (construtor da `Despesa`) e é conferida pelo Validador.
- "Despesa com participante não cadastrado" também não pode ser provocado pela tela, que só lista participantes cadastrados. A regra existe no modelo (`Planejamento`) e é conferida pelo Validador.

## Observação sobre o teste 7

O seletor de arquivos do macOS não deixa escolher um arquivo que não existe. Para simular o arquivo ausente, o arquivo foi selecionado no seletor e apagado pelo Terminal antes de clicar em "Abrir". Os arquivos de teste foram criados numa pasta compartilhada (`/Users/Shared/cabe-no-bolso`), para que o caminho mostrado nas mensagens não exponha dados pessoais.

## Cenário próprio (teste 8)
**Situação:** passeio da equipe ao Centro Dragão do Mar de Arte e Cultura, em Fortaleza. Parte do grupo vai de carro, parte de ônibus e uma pessoa de Uber. Todos vão ao cinema, depois racham um happy hour e, em seguida, quase todos vão a um show. Os valores são fictícios.

**Participantes**

| Código | Nome | Orçamento |
|---|---|---|
| 1 | Andressa | R$ 100,00 |
| 2 | Guilherme | R$ 100,00 |
| 3 | Luana | R$ 150,00 |
| 4 | Lucas | R$ 150,00 |
| 5 | Malu | R$ 100,00 |
| 6 | Samuel | R$ 80,00 |

**Despesas**

| Id | Descrição | Tipo | Valor | Participantes |
|---|---|---|---|---|
| 1 | Gasolina da ida | Fixa | R$ 25,00 | Guilherme, Lucas, Samuel |
| 2 | Ônibus da ida | Por pessoa | R$ 4,50 | Andressa, Malu |
| 3 | Uber da ida | Fixa | R$ 18,00 | Luana |
| 4 | Cinema (meia estudantil) | Por pessoa | R$ 8,00 | Todos |
| 5 | Combo pipoca + refrigerante | Por pessoa | R$ 16,00 | Andressa, Lucas, Malu, Samuel |
| 6 | Happy hour | Fixa | R$ 330,00 | Todos |
| 7 | Show | Por pessoa | R$ 20,00 | Todos, menos Samuel |
| 8 | Uber da volta | Fixa | R$ 80,00 | Andressa, Luana |

Na volta, Guilherme, Lucas, Malu e Samuel voltam de carona, sem custo.

**Resultado calculado à mão**

| Participante | Conta | Total | Orçamento | Situação |
|---|---|---|---|---|
| Andressa | 4,50 (ônibus) + 8,00 (cinema) + 16,00 (combo) + 55,00 (happy hour) + 20,00 (show) + 40,00 (Uber volta) | R$ 143,50 | R$ 100,00 | Excede em R$ 43,50 |
| Guilherme | 8,34 (gasolina) + 8,00 (cinema) + 55,00 (happy hour) + 20,00 (show) | R$ 91,34 | R$ 100,00 | Dentro |
| Luana | 18,00 (Uber ida) + 8,00 (cinema) + 55,00 (happy hour) + 20,00 (show) + 40,00 (Uber volta) | R$ 141,00 | R$ 150,00 | Dentro |
| Lucas | 8,33 (gasolina) + 8,00 (cinema) + 16,00 (combo) + 55,00 (happy hour) + 20,00 (show) | R$ 107,33 | R$ 150,00 | Dentro |
| Malu | 4,50 (ônibus) + 8,00 (cinema) + 16,00 (combo) + 55,00 (happy hour) + 20,00 (show) | R$ 103,50 | R$ 100,00 | Excede em R$ 3,50 |
| Samuel | 8,33 (gasolina) + 8,00 (cinema) + 16,00 (combo) + 55,00 (happy hour) | R$ 87,33 | R$ 80,00 | Excede em R$ 7,33 |

Custo total do passeio: R$ 674,00 (gasolina 25,00 + ônibus 9,00 + Uber ida 18,00 + cinema 48,00 + combo 64,00 + happy hour 330,00 + show 100,00 + Uber volta 80,00).

Conferência:
A soma dos totais individuais também dá R$ 674,00.

## Como reproduzir

**Pela tela (recomendado):** compile e execute o programa como explicado no README e siga a coluna "Entrada" de cada teste. Para o teste 8, use **Arquivo → Abrir...** e escolha `exemplo/planejamento-exemplo.dat`, que já contém o cenário próprio.

**Sem tela:** os testes do modelo estão em `src/app/TestesManuais.java`. Execute:

    java -cp out app.TestesManuais

A saída mostra o resumo do caso de referência, as recusas de entradas inválidas com o retrato antes e depois, o salvar e carregar, as alterações, a edição de despesa, a revisão e a abertura de arquivo. Os valores aparecem em centavos.

## Decisões

- **Registrar para revisão:** a aba Resumo tem o botão "Marcar para revisão", que grava uma marca no `Planejamento`. O modelo só aceita a marca se algum participante estiver acima do orçamento; o botão "Retirar marca de revisão" a remove.
