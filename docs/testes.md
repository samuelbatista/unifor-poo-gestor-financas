# Testes

Tabela dos testes pedidos no enunciado (seção 4). As colunas "Obtido" e "Evidência" serão preenchidas quando a tela estiver pronta, com capturas da aplicação.

Valores em reais; no código, todo dinheiro é guardado em centavos (`long`).

| Nº | Teste | Entrada | Resultado esperado | Obtido | Evidência |
|---|---|---|---|---|---|
| 1 | Caso de referência | Participantes Ana (R$ 70,00), Bruno (R$ 60,00) e Carla (R$ 70,00). Despesas: Jantar fixa R$ 90,00 (todos), Mercado fixa R$ 60,00 (todos), Táxi fixa R$ 30,00 (Ana e Carla), Ingresso por pessoa R$ 10,00 (todos) | Ana R$ 75,00, Bruno R$ 60,00, Carla R$ 75,00; custo total R$ 210,00. Ana e Carla excedem em R$ 5,00; Bruno está dentro (total igual ao orçamento) | | |
| 2 | Centavos no rateio | Despesa fixa de R$ 100,00 para os participantes de códigos 1, 2 e 3 | R$ 33,34 (código 1), R$ 33,33 (código 2) e R$ 33,33 (código 3); soma R$ 100,00 | | |
| 3 | Entrada ou vínculo inválido | Tentar cadastrar despesa sem participantes, com valor zero ou negativo, com descrição vazia, ou com participante não cadastrado | A operação é recusada com mensagem que explica o motivo, e os dados existentes não mudam | | |
| 4 | Alteração de orçamento | No caso de referência, alterar o orçamento de Ana de R$ 70,00 para R$ 80,00 | Ana continua com total de R$ 75,00 e passa a estar dentro do orçamento | | |
| 5 | Exclusão de despesa | No caso de referência, excluir o Táxi (fixa R$ 30,00, Ana e Carla) | Ana, Bruno e Carla passam a R$ 60,00 cada; custo total R$ 180,00 | | |
| 6 | Salvar e reiniciar | Cadastrar o caso de referência, salvar, fechar a aplicação e abrir de novo, carregando o arquivo | Todos os participantes, despesas, vínculos e resultados voltam iguais, sem registros duplicados | | |
| 7 | Problema no arquivo | Tentar carregar um arquivo inexistente e um arquivo inválido (texto qualquer ou corrompido) | Mensagem clara, sem travar a aplicação; o planejamento que estava aberto continua igual | | |
| 8 | Cenário próprio | Ver "Cenário próprio — proposta" abaixo | Ver o resultado calculado à mão abaixo | | |

## Cenário próprio — proposta
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

Os testes do modelo (sem a tela) estão em `src/app/TestesManuais.java`. Para rodar, compile como explicado no README e execute:

    java -cp out app.TestesManuais

A saída mostra o resumo do caso de referência (teste 1), as recusas de entradas inválidas com o retrato antes e depois (teste 3), o salvar e carregar (testes 6 e 7) e o resumo do cenário próprio (teste 8). Os valores aparecem em centavos.


## Decisões

- **Registrar para revisão:** a aba Resumo tem o botão "Marcar para revisão", que grava uma marca no `Planejamento`. O modelo só aceita a marca se algum participante estiver acima do orçamento; o botão "Retirar marca de revisão" a remove.