# Mapa do projeto: planejador de despesas

## 1. O problema, em uma frase
Criar uma aplicação para planejar as despesas de um grupo de pessoas que terão orçamento pessoal definido e comparar com as despesas que serão lançadas de acordo com o tipo de despesa (Fixa ou Por Pessoa) e a participação das pessoas nestas despesas (para ver como cada orçamento pessoal será afetado).

## 2. As cinco funcionalidades
- Gerenciar os participantes e seus respectivos orçamentos
- Gerenciar despesas com descrição, valor, tipo e participantes
- Rateio de custos para dividir o valor entre os participantes envolvidos naquela despesa
- Resumo do planejamento exibindo as despesas realizadas, custo total e individual, situação de cada participante em relação ao respectivo orçamento (se o mesmo foi respeitado, atingindo ou ultrapassado)
- Persistir as informações (participantes, despesas, rateios) em um arquivo local

## 3. Regras de cálculo e validação
1. Participação: cada despesa terá pelo menos um participante cadastrado, sem repetições. Quem não participa terá contribuição zero. Ex: Se eu tentar cadastrar uma despesa sem um participante, a mesma não será criada e o usuário será alertado do problema ao tentar criar a despesa.

2. Valores válidos:
   a. Orçamentos devem aceitar valores maiores ou iguais a zero e recusar campos vazios. Ex: Se eu tentar criar um orçamento com valor vazio ou negativo, um alerta deverá informar que "Orçamentos precisam ter valores iguais ou maiores que zero"; em caso de atualização, os valores anteriores não mudam.
   b. Despesas precisam ter valor maior que zero e recusar campos vazios. Ex: Se eu tentar criar uma despesa com valor vazio, negativo ou igual a zero, um alerta deverá informar que "Despesas precisam ter valores maiores que zero"; em caso de atualização, os valores anteriores não mudam.
   c. Não podem existir dois participantes com o mesmo código, e a recusa não altera o estado anterior. Ex: Se o Bruno já tem o código 2 e eu tentar cadastrar a Carla também com o código 2, deverá ser informado que "Já existe um participante com este código" e a Carla não será cadastrada.

3. Centavos: utilizar inteiros ou representação decimal precisa. No rateio fixo, distribuir os centavos restantes pela ordem crescente dos códigos dos participantes selecionados. Ex: Se eu lançar uma despesa fixa de R$ 100 para os participantes 1, 2 e 3, ela será dividida com R$ 33,34 para o participante 1, R$ 33,33 para o participante 2 e R$ 33,33 para o participante 3.

4. Orçamento: total igual ao limite está dentro do orçamento. Acima dele, indicar o valor excedido e permitir registrar o planejamento para revisão. Ex: Se o participante 1 tem um orçamento de R$ 75 e for lançada uma despesa por pessoa de R$ 80 para ele, deverá ser informado que seu orçamento foi ultrapassado em R$ 5,00. Se a despesa fosse de exatamente R$ 75, ele estaria dentro do orçamento.

5. Alterações: recalcular os totais após editar ou excluir despesas ou alterar orçamentos. Se for permitido excluir participantes, impedir a exclusão enquanto houver despesas vinculadas. Ex: Para excluir o participante 3, precisarei antes removê-lo de todas as despesas lançadas para ele; os totais de todos os participantes serão recalculados.

## 4. Caso de referência
- Total de Ana, de Bruno e de Carla: Ana (R$ 30 + R$ 20 + R$ 15 + R$ 10 = R$ 75), Bruno (R$ 30 + R$ 20 + R$ 10 = R$ 60) e Carla (R$ 30 + R$ 20 + R$ 15 + R$ 10 = R$ 75)
- Custo total: R$ 90 + R$ 60 + R$ 30 + R$ 30 = R$ 210
- Quem excede o orçamento, e em quanto: Ana e Carla excedem em R$ 5 seu orçamento de R$ 70

## 5. Os sete testes
1. Centavos no rateio:
* O que faço: Cadastro uma despesa fixa de R$ 100,00 para os códigos 1, 2 e 3.
* O que deve acontecer: R$ 33,34, R$ 33,33 e R$ 33,33, respectivamente; soma R$ 100,00.

2. Entrada ou vínculo inválido:
* O que faço: Tento cadastrar uma despesa sem participantes, com valor negativo ou com campo vazio.
* O que deve acontecer: A despesa é recusada, uma mensagem explica o motivo e os dados existentes não são alterados.

3. Alteração de orçamento:
* O que faço: No caso de referência, altero o orçamento de Ana de R$ 70 para R$ 80.
* O que deve acontecer: O custo de Ana continua R$ 75, e agora ela está dentro do orçamento.

4. Exclusão de despesa:
* O que faço: No caso de referência, excluo a atividade recreativa (fixa de R$ 30, apenas Ana e Carla).
* O que deve acontecer: Ana, Bruno e Carla passam a pagar R$ 60 cada, e o custo total cai para R$ 180.

5. Salvar e reiniciar:
* O que faço: Cadastro o caso de referência, salvo, fecho a aplicação e abro de novo.
* O que deve acontecer: Todos os dados, vínculos e resultados do caso de referência são recuperados, sem registros duplicados.

6. Problema no arquivo:
* O que faço: Tento carregar um arquivo que não existe (ausente) ou que está corrompido (inválido).
* O que deve acontecer: Uma mensagem explica o problema, sem travar a aplicação. Posso iniciar um planejamento vazio, e se eu já tinha um planejamento aberto, ele é preservado.

7. Caso de referência:
* O que faço: Cadastro Ana (R$ 70), Bruno (R$ 60) e Carla (R$ 70) e as 4 despesas descritas na seção 4.
* O que deve acontecer: Ana paga R$ 75, Bruno R$ 60 e Carla R$ 75, com custo total de R$ 210. Ana e Carla excedem seus orçamentos em R$ 5,00 cada, e Bruno está dentro do limite (total igual ao orçamento).

## 6. O que entregar, e até quando
- Relatório: Relatório em PDF com até 10 páginas seguindo o padrão ABNT e contemplando: Apresentação, Modelagem e
  organização, Implementação, Testes e resultados, Avaliação e melhorias, Referências. Cada integrante deve enviar o PDF no AVA até 27/11/2026 às 23h59 (quem não enviar fica com zero).
- Repositório: https://github.com/samuelbatista/unifor-poo-gestor-financas


## 7. A equipe
- Integrantes (só o primeiro nome): Andressa, Guilherme, Luana, Lucas, Maria e Samuel.
- Canal de conversa: Grupo de Whatsapp
- Horário combinado: Toda segunda às 20h