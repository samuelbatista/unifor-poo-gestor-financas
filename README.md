# Cabe no bolso — planejador de despesas em grupo

Aplicação em Java que ajuda um grupo a planejar despesas: cada participante tem um orçamento, as despesas (fixas ou por pessoa) são divididas entre quem participa, e o programa mostra quanto cada um vai gastar e quem passou do orçamento. O planejamento pode ser salvo e carregado de um arquivo.

Trabalho da disciplina N685 — Programação Orientada a Objetos (Unifor).

## Requisitos
* JDK 17 (confira com `java -version` e `javac -version`: os dois devem dizer 17)

## Estrutura de pastas
* `src/app` — porta de entrada (`Main`) e os testes manuais do modelo (`TestesManuais`)
* `src/modelo` — regras de negócio: participante, despesas, planejamento e a exceção de validação
* `src/persistencia` — contrato de persistência, gravação em arquivo e a exceção de arquivo
* `src/visao` — telas (Swing), em construção
* `src/controle` — ligação entre telas e modelo, em construção
* `docs` — mapa do projeto, diagrama UML e tabela de testes

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

Para rodar os testes manuais do modelo, troque a última linha por:
```bash
java -cp out app.TestesManuais
```

### Se o terminal não encontrar o `java` ou o `javac`
O JDK instalado pelo IntelliJ normalmente não fica no PATH do sistema.
* **macOS:** rode `export JAVA_HOME=$(/usr/libexec/java_home -v 17)` e `export PATH="$JAVA_HOME/bin:$PATH"` em cada janela nova do Terminal.
* **Windows (PowerShell):** rode `$env:JAVA_HOME = "CAMINHO"` e `$env:Path = "$env:JAVA_HOME\bin;$env:Path"`, trocando CAMINHO pela pasta do JDK 17 (no IntelliJ: File → Project Structure → SDKs).

## Como executar pelo IntelliJ
1. Abra a pasta do projeto no IntelliJ (File → Open).
2. Em File → Project Structure → Project, escolha o JDK 17 como SDK.
3. Abra `src/app/Main.java` e clique no triângulo verde ao lado do `main`. Para os testes, faça o mesmo em `src/app/TestesManuais.java`.

## Testes
A tabela com os testes pedidos no enunciado, a entrada, o resultado esperado e as evidências está em [docs/testes.md](docs/testes.md).