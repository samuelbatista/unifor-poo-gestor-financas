# Cabe no bolso — planejador de despesas em grupo

Aplicação em Java com interface gráfica que ajuda um grupo a planejar as despesas de um passeio. Cada participante tem um orçamento; as despesas podem ser **fixas** (o valor total é dividido entre quem participa) ou **por pessoa** (cada participante paga o mesmo valor). O programa mostra quanto cada um vai gastar, o custo total e quem passou do orçamento, e permite salvar e abrir o planejamento em arquivo.

Trabalho da disciplina N685 — Programação Orientada a Objetos (Unifor).

## Requisitos
* **JDK 17.** Nenhuma biblioteca além do próprio JDK.
* Confira a versão no terminal com `java -version` e `javac -version`: os dois devem mostrar 17.

## Instalação do JDK 17
* **Pelo IntelliJ (mais simples):** em File → Project Structure → SDKs, use **+ → Download JDK** e escolha a versão 17.
* **Manual:** baixe o JDK 17 em https://adoptium.net (Temurin 17), instale e confira com `java -version`.

## Estrutura de pastas
* `src/app` — porta de entrada (`Main`) e os testes do modelo sem tela (`TestesManuais`)
* `src/modelo` — regras de negócio e cálculos: `Planejamento`, `Participante`, `Despesa` (abstrata), `DespesaFixa`, `DespesaPorPessoa` e a exceção `ValidacaoException`
* `src/persistencia` — contrato de persistência (`PersistenciaPlanejamento`), gravação em arquivo por serialização (`PersistenciaEmArquivo`) e a exceção `PersistenciaException`
* `src/visao` — telas em Swing: janela principal com menu Arquivo e as abas Participantes, Despesas e Resumo
* `src/controle` — controladores que ligam cada tela ao modelo
* `docs/uml.md` — **diagramas de classes** (modelo e organização MVC), com a justificativa das relações e as decisões de projeto; as imagens estão em `docs/img/`
* `docs/testes.md` — tabela dos testes, com entrada, resultado esperado, obtido e evidência
* `docs/capturas/` — capturas de tela usadas como evidência dos testes
* `docs/mapa-do-projeto.md` — visão geral do projeto
* `exemplo/planejamento-exemplo.dat` — arquivo de exemplo com dados prontos

## Como compilar e executar pelo terminal
Sempre na pasta do projeto (a que contém `src`).

**macOS e Linux (Terminal):**
```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out app.Main
```

**Windows (PowerShell):**
```powershell
Get-ChildItem -Recurse -Filter *.java src | Resolve-Path -Relative | Set-Content fontes.txt
javac -encoding UTF-8 -d out "@fontes.txt"
java -cp out app.Main
```

O `-encoding UTF-8` é obrigatório: sem ele, os acentos do código podem quebrar.

### Se o terminal não encontrar o `java` ou o `javac`
O JDK instalado pelo IntelliJ normalmente não fica no PATH do sistema.
* **macOS:** rode `export JAVA_HOME=$(/usr/libexec/java_home -v 17)` e `export PATH="$JAVA_HOME/bin:$PATH"` em cada janela nova do Terminal.
* **Windows (PowerShell):** rode `$env:JAVA_HOME = "CAMINHO"` e `$env:Path = "$env:JAVA_HOME\bin;$env:Path"`, trocando CAMINHO pela pasta do JDK 17 (no IntelliJ: File → Project Structure → SDKs).

## Como executar pelo IntelliJ
1. Abra a pasta do projeto no IntelliJ (File → Open).
2. Em File → Project Structure → Project, escolha o JDK 17 como SDK.
3. Abra `src/app/Main.java` e clique no triângulo verde ao lado do `main`.

## Como usar
* **Participantes:** informe código, nome e orçamento (ex.: `70,00`) e clique em Cadastrar. Clique numa linha para alterar.
* **Despesas:** informe descrição, valor e tipo, marque os participantes e clique em Incluir. Clique numa linha para editar ou excluir.
* **Resumo:** mostra as despesas, o total e a situação de cada participante, o custo total e permite marcar o planejamento para revisão.
* **Arquivo:** Novo planejamento, Abrir... e Salvar...

## Arquivo de exemplo
`exemplo/planejamento-exemplo.dat` contém o cenário próprio da equipe (um passeio fictício ao Centro Dragão do Mar, em Fortaleza): 6 participantes e 8 despesas, com custo total de R$ 674,00. Para usar, execute o programa e escolha **Arquivo → Abrir...** e o arquivo. O resultado esperado está em `docs/testes.md` (teste 8).

## Como reproduzir os testes
* **Pela tela:** siga a coluna "Entrada" de cada teste em [docs/testes.md](docs/testes.md) e compare com as colunas "Resultado esperado" e "Obtido". As capturas estão em `docs/capturas/`.
* **Sem tela:** depois de compilar, execute `java -cp out app.TestesManuais`. A saída mostra os resultados do modelo (valores em centavos).

## Uso de inteligência artificial
Usamos o Claude (Anthropic), pelo aplicativo Claude Code, como tutor durante todo o desenvolvimento. Para cada etapa, a IA explicou os conceitos, propôs o código, revisou e conferiu o resultado antes de cada pull request. A equipe digitou o código, executou os testes, revisou e aceitou cada pull request. As dúvidas de cada etapa foram discutidas pelo grupo. Todo o código foi lido e é explicado pela equipe.

## Integrantes
Andressa, Guilherme, Luana, Lucas, Malu e Samuel.
