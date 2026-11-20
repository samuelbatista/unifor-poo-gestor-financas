# Diagramas de classes v3

## Observações
* Utilizamos o Mermaid para que os diagramas de classe possam ser vistos em tempo real e versionados junto com o projeto
* Linhas com `-` indicam que é `private`
* Linhas com `+` indicam que é `public`
* São dois diagramas: o **1. Modelo** mostra as classes de dados e de regra (com atributos e métodos); o **2. Organização MVC** mostra os papéis de cada pacote e quem conhece quem (sem atributos e métodos, para ficar legível)
* Imagens para o relatório:
  * `docs/img/uml-modelo.png`
  * `docs/img/uml-modelo.svg`
  * `docs/img/uml-mvc.png`
  * `docs/img/uml-mvc.svg`

## 1. Diagrama do modelo

```mermaid
classDiagram
    
    %% ===== Classes =====
    class Planejamento {
        %% Atributos
        - participantes: List~Participante~
        - despesas: List~Despesa~
        - ultimoIdDespesa: int
        - marcadoParaRevisao: boolean
        
        %% Método para o CRUD de Participante
        + adicionarParticipante(participante: Participante) void
        + buscarParticipante(codigo: int) Participante
        + alterarParticipante(codigo: int, novoNome: String, novoOrcamentoEmCentavos: long) void
        %% Delete (Pendente / Não obrigatório)
        
        %% Getters: Traz os valores
        + getParticipantes() List~Participante~
        + getDespesas() List~Despesa~
        
        %% Método para o CRUD de Despesa
        + gerarIdDespesa() int
        + adicionarDespesa(despesa: Despesa) void
        + buscarDespesa(id: int) Despesa
        + editarDespesa(id: int, novaDespesa: Despesa) void
        + removerDespesa(id: int) void
        
        %% Cálculos (Resumo)
        + calcularTotalDe(participante: Participante) long
        + calcularCustoTotal() long
        + calcularExcessoDe(participante: Participante) long
        + estaDentroDoOrcamento(participante: Participante) boolean
        + existeParticipanteAcimaDoOrcamento() boolean
        + substituirConteudoPor(outro: Planejamento) void

        %% Revisão do planejamento
        + marcarParaRevisao() void
        + desmarcarRevisao() void
        + isMarcadoParaRevisao() boolean
        
        %% Ajudante interno: recusa despesa com participante não cadastrado
        - conferirParticipantesCadastrados(despesa: Despesa) void
    }
    
    class Participante {
        %% Atributos da Classe
        - codigo: int
        - nome: String
        - orcamentoEmCentavos: long
        
        %% Construtor: Cria um participante
        + Participante(codigo: int, nome: String, orcamentoEmCentavos: long)
        
        %% Getters: Traz os valores
        + getCodigo() int
        + getNome() String
        + getOrcamentoEmCentavos() long
        
        %% Altera nome e orçamento juntos ("tudo ou nada")
        + alterar(novoNome: String, novoOrcamentoEmCentavos: long) void
        
        %% Setters: Define os valores
        + setNome(nome: String) void
        + setOrcamentoEmCentavos(orcamentoEmCentavos: long) void
    }
    
    class ValidacaoException {
        + ValidacaoException(mensagem: String)
    }
    
    class Despesa {
        <<abstract>>
        %% Atributos
        - id: int
        - descricao: String
        - valorEmCentavos: long
        - participantes: List~Participante~
        
        %% Construtor que será utilizado pelas classes filhas para criar novas despesas
        + Despesa(id: int, descricao: String, valorEmCentavos: long, participantes: List~Participante~)
        
        %% Getters
        + getId() int
        + getDescricao() String
        + getValorEmCentavos() long
        + getParticipantes() List~Participante~
        
        %% Método para calcular a despesa de acordo com o tipo (Fixa/Por pessoa)
        + calcularValorDe(participante: Participante)* long

        %% Método para calcular o valor total da despesa independente do tipo
        + calcularTotal() long
    }
    
    class DespesaFixa {
        %% Construtor: repassa os dados para a Despesa (super), que valida e guarda
        + DespesaFixa(id: int, descricao: String, valorEmCentavos: long, participantes: List~Participante~)
        
        %% Método para calcular o valor da despesa fixa por participante
        + calcularValorDe(participante: Participante) long
    }

    class DespesaPorPessoa {
        %% Construtor: repassa os dados para a Despesa (super), que valida e guarda
        + DespesaPorPessoa(id: int, descricao: String, valorEmCentavos: long, participantes: List~Participante~)
        
        %% Método para calcular o valor da despesa (não fixa) por participante
        + calcularValorDe(participante: Participante) long
    }
    
    class PersistenciaPlanejamento {
        <<interface>>
        + salvar(planejamento: Planejamento, caminho: String) void
        + carregar(caminho: String) Planejamento
    }
    
    class PersistenciaEmArquivo {
        + salvar(planejamento: Planejamento, caminho: String) void
        + carregar(caminho: String) Planejamento
    }
    
    class PersistenciaException {
        + PersistenciaException(mensagem: String, causa: Throwable)
    }

    class Serializable {
        <<interface>>
        %% Interface do Java sem métodos: só "marca" as classes que podem ser gravadas em arquivo
    }

    %% ===== Heranças =====
    Despesa <|-- DespesaFixa
    Despesa <|-- DespesaPorPessoa
    Exception <|-- ValidacaoException
    Exception <|-- PersistenciaException

    %% ===== Implementações =====
    PersistenciaPlanejamento <|.. PersistenciaEmArquivo
    Serializable <|.. Planejamento
    Serializable <|.. Participante
    Serializable <|.. Despesa

    %% ===== Composições e associações =====
    Planejamento "1" *-- "0..*" Participante : cadastra
    Planejamento "1" *-- "0..*" Despesa : registra
    Despesa "0..*" --> "1..*" Participante : divide entre

    %% ===== Dependências =====
    PersistenciaPlanejamento ..> Planejamento : salva e carrega
    Participante ..> ValidacaoException : lança
    Despesa ..> ValidacaoException : lança
    Planejamento ..> ValidacaoException : lança
    PersistenciaPlanejamento ..> PersistenciaException : lança
```

## Justificativa das relações do modelo

### Composições e associações
* `Planejamento *-- Participante` (composição, `1` para `0..*`): os participantes só existem dentro do planejamento: são cadastrados nele, guardados na sua lista privada, salvos e carregados junto com ele e descartados quando um novo planejamento começa. Pode haver planejamento ainda sem participantes (`0..*`).
* `Planejamento *-- Despesa` (composição, `1` para `0..*`): mesma lógica: a despesa nasce ao ser registrada no planejamento e deixa de existir quando é removida dele.
* `Despesa --> Participante` (associação, `0..*` para `1..*`): a despesa só aponta para quem divide o valor; ela não é dona dos participantes (apagar uma despesa não apaga ninguém). Toda despesa tem pelo menos um participante (`1..*`), e um participante pode estar em nenhuma ou em várias despesas (`0..*`).

### Heranças
* `Despesa <|-- DespesaFixa` e `DespesaPorPessoa`: as duas "são uma" Despesa: herdam id, descrição, valor, participantes e as validações, e cada uma só redefine a regra de cálculo (calcularValorDe), o que permite o polimorfismo.
* `Exception <|-- ValidacaoException` e `PersistenciaException`: exceções próprias do domínio, que herdam de Exception o comportamento de qualquer exceção (mensagem, causa) e separam erros de regra de negócio de erros de arquivo.

### Implementações (realização)
* `PersistenciaPlanejamento <|.. PersistenciaEmArquivo`: a classe de arquivo cumpre o contrato (salvar e carregar). O resto do sistema conhece só a interface, então a forma de gravar pode mudar sem afetar o restante.
* `Serializable <|.. Planejamento`, `Participante` e `Despesa`: o "carimbo" que autoriza o Java a gravar esses objetos em arquivo (as filhas da Despesa herdam o carimbo).

### Dependências
* `PersistenciaPlanejamento ..> Planejamento`: a persistência usa o planejamento só de passagem (parâmetro do salvar, retorno do carregar), sem guardá-lo como atributo.
* `Participante`, `Despesa` e `Planejamento ..> ValidacaoException`: lançam a exceção quando recusam uma entrada inválida, mas não a guardam.
* `PersistenciaPlanejamento ..> PersistenciaException`: o salvar e o carregar lançam a exceção quando o arquivo falha.

## Justificativa das coleções
* Planejamento.participantes: List (ArrayList): mantém a ordem de cadastro, que é a ordem em que a tela vai listar. Não usamos Set porque a regra de unicidade é pelo código (não pelo objeto inteiro) e precisa recusar com mensagem, o que o porteiro do adicionarParticipante faz. Não usamos Map (código → participante) porque o grupo é pequeno e a busca percorrendo a lista é simples e suficiente.
* Planejamento.despesas: List (ArrayList): mantém a ordem de lançamento e permite trocar uma despesa no mesmo lugar ao editar (indexOf + set). O id único é garantido pelo contador e pelo porteiro do adicionarDespesa.
* Despesa.participantes: List: guarda quem divide a despesa. Não usamos Set para que um participante repetido seja recusado com mensagem, em vez de sumir em silêncio, e para que a ordem informada seja mantida.

## 2. Diagrama da organização MVC

```mermaid
classDiagram
    direction LR

    %% ===== Pacotes e classes (só os nomes, para ficar legível) =====
    namespace app {
        class Main
    }
    namespace visao {
        class JanelaPrincipal
        class PainelParticipantes
        class PainelDespesas
        class PainelResumo
    }
    namespace controle {
        class ControladorParticipantes
        class ControladorDespesas
        class ControladorResumo
        class ControladorArquivo
    }
    namespace persistencia {
        class PersistenciaPlanejamento {
            <<interface>>
        }
        class PersistenciaEmArquivo
    }
    namespace modelo {
        class Planejamento
        class Participante
        class Despesa {
            <<abstract>>
        }
    }

    %% ===== Quem monta o programa =====
    Main ..> JanelaPrincipal : monta
    Main ..> PersistenciaEmArquivo : cria (único lugar)

    %% ===== Visão: a janela mostra os painéis em abas =====
    JanelaPrincipal o-- PainelParticipantes : aba
    JanelaPrincipal o-- PainelDespesas : aba
    JanelaPrincipal o-- PainelResumo : aba

    %% ===== Controle conhece a sua tela... =====
    ControladorParticipantes --> PainelParticipantes : atualiza
    ControladorDespesas --> PainelDespesas : atualiza
    ControladorResumo --> PainelResumo : atualiza
    ControladorArquivo --> JanelaPrincipal : seletor e mensagens

    %% ===== ...e o modelo =====
    ControladorParticipantes --> Planejamento : pede
    ControladorDespesas --> Planejamento : pede
    ControladorResumo --> Planejamento : pede
    ControladorArquivo --> Planejamento : substitui conteúdo
    ControladorArquivo --> PersistenciaPlanejamento : salva e abre

    %% ===== Persistência =====
    PersistenciaPlanejamento <|.. PersistenciaEmArquivo
    PersistenciaPlanejamento ..> Planejamento : grava e lê

    %% ===== Modelo (detalhes no diagrama 1) =====
    Planejamento *-- Participante
    Planejamento *-- Despesa

    %% ===== Limitação conhecida: dois painéis leem objetos do modelo só para exibir =====
    PainelParticipantes ..> Participante : só lê para exibir
    PainelDespesas ..> Despesa : só lê para exibir
```

## Justificativa das relações do MVC
* **O modelo não conhece ninguém acima dele:** nenhuma classe de `modelo` importa `visao`, `controle` ou `persistencia`. Ele só calcula e valida, e por isso funciona igual na tela, nos `TestesManuais` e no Validador.
* **A visão avisa o controle só pela interface `ActionListener` (não desenhada, para o diagrama ficar legível):** os quatro controladores implementam `ActionListener`. O painel recebe o controlador como um `ActionListener` (em `aoClicarNosBotoes`; a janela, em `aoUsarMenuArquivo`) e não sabe qual classe está do outro lado; o controlador decide o que fazer pelo nome do comando do botão. Por isso não há seta da visão para o controle: quem guarda a referência é o controlador.
* **O controle conhece a sua tela e o modelo (associação `-->`):** cada controlador guarda como atributo o painel que atende e o `Planejamento`. Ele lê o que foi digitado, converte (com `FormatoDeMoeda`), pede ao modelo e manda a tela mostrar o resultado ou a mensagem de recusa.
* **`ControladorArquivo --> PersistenciaPlanejamento`:** o controlador conhece só o contrato (a interface). A classe concreta `PersistenciaEmArquivo` é criada só no `Main`, então trocar arquivo por outra forma de gravar muda uma linha só.
* **`JanelaPrincipal o-- Painel...` (agregação):** a janela mostra os painéis em abas, mas não os cria: eles são criados no `Main` e entregues prontos no construtor, por isso agregação e não composição.
* **`Main`:** é o único que conhece todo mundo: cria o modelo, os painéis, os controladores, a janela e a persistência concreta, e liga as peças (incluindo o "lembrete" que atualiza as três abas depois de Novo e Abrir). Para o diagrama não virar um emaranhado, só duas setas dele aparecem.
* **`FormatoDeMoeda` (não desenhada):** os controladores de participantes, despesas e resumo usam essa classe utilitária do pacote `visao` de passagem, sem guardá-la como atributo (dependência), para converter entre texto ("70,00") e centavos (`7000`). Ela fica no pacote `visao` porque é formatação de tela, e não regra de negócio.

### Limitação conhecida
* `PainelParticipantes` e `PainelDespesas` recebem listas de objetos do modelo (`Participante` e `Despesa`) e leem os seus getters para montar as tabelas e a lista de caixas de marcar. Eles **não calculam nem alteram** nada: só exibem. Já o `PainelResumo` (a aba mais recente) segue uma separação mais rígida e recebe do controlador apenas textos prontos, sem importar o modelo. Mantivemos as duas primeiras telas como estavam porque já estavam testadas e congeladas, e a regra do enunciado ("a visão recebe entradas e mostra resultados; o modelo calcula e valida") continua cumprida. Para uma próxima versão, o caminho seria fazer os dois painéis receberem textos prontos, como o `PainelResumo`.

## Decisões

### Planejamento
* Criamos editarDespesa em vez de setters na Despesa: a edição cria uma despesa nova, com o mesmo ID, que substitui a anterior. Assim é possível trocar o tipo (Fixa <-> Por pessoa), o que um setter não faria, e a despesa anterior só é substituída se a nova for válida.
* Não utilizaremos ainda um método para excluir participante pois não é obrigatório (podemos criar ao final, se tivermos tempo suficiente). Se for criado, deve impedir a exclusão enquanto o participante estiver em alguma despesa.
* Criamos uma validação para evitar que haja duplicação de ID nas despesas pois o CRUD opera por ID e precisamos garantir a manipulação da Despesa exata.
* Os totais, o custo do grupo e o excesso não são guardados em atributos: são calculados na hora em que são pedidos. Assim, depois de editar ou remover uma despesa, os valores já saem atualizados (regra 5), sem precisar de código de recálculo.
* O id da despesa é gerado pelo próprio Planejamento (gerarIdDespesa), com um contador que só cresce. Assim quem cria a despesa não precisa inventar um número, e um id nunca se repete, nem depois de excluir uma despesa. Como o contador é atributo do Planejamento, ele é salvo junto no arquivo.
* Lembrete de validação/teste: Uma despesa só entra (ou substitui outra) se todos os participantes dela estiverem cadastrados no planejamento. A conferência fica no modelo, e não só na tela, para que nenhuma entrada (tela ou testes) passe por fora da regra.
* Lembrete de validação/teste: Se uma despesa for recusada depois de o id já ter sido gerado, aquele número é descartado e o próximo pula (ex.: de 3 para 5). Aceitamos esse "pulo" porque os ids continuam únicos e nenhum cálculo depende de eles serem seguidos.

### Participante
* O orçamento é guardado em centavos, como `long`: evita possíveis erros de arredondamento do double
* `Participante` não tem `setCodigo` pois ele é o ID que é definido na criação do participante e nunca muda
* Os atributos são `private` e só podem ser acessados pelos getters e setters

### Despesa
* A despesa é abstrata pois toda despesa é Fixa ou PorPessoa; uma despesa ‘pura’ não saberia calcular quanto cada participante paga, então ela não pode ser criada (não dá para usar new Despesa).
* calcularValorDe() é abstrato pois será tratado com suas regras específicas de cálculo dentro do DespesaFixa ou do DespesaPorPessoa
* calcularTotal() não é abstrato pois ele soma o calcularValorDe de cada participante, e isso é igual para os dois tipos
* valorEmCentavos na Despesa Fixa é o total da conta, na Despesa Por Pessoa o valor de cada um.
* A DespesaFixa calcula o resto da divisão exata entre os participantes e distribui os centavos que sobram, 1 centavo para cada, em ordem crescente de código, até acabarem (regra 3).”

### Persistência
* Usamos uma interface para a persistência: o resto do sistema só conhece salvar e carregar, então podemos trocar o arquivo por outra forma de gravação (ex.: banco de dados) sem mudar o controle nem a tela.
* A ligação com o Planejamento é tracejada (dependência) porque a persistência só usa o Planejamento de passagem, como parâmetro do salvar e retorno do carregar, e não o guarda como atributo. Se guardasse, seria uma associação (linha cheia).
* Gravamos o Planejamento inteiro com um único writeObject, porque a serialização é recursiva e leva junto as informações definidas do planejamento, as despesas e os participantes. Por isso, o Planejamento, o Participante e a Despesa têm o carimbo Serializable. As filhas herdam o carimbo da mãe.
* O salvar e o carregar capturam os erros técnicos de leitura/gravação (IOException, ClassNotFoundException e ClassCastException, ex: arquivo inexistente/vazio/cortado/de texto/com outro tipo de objeto) e os embrulham numa exceção nossa, a PersistenciaException, com uma mensagem clara e o erro original guardado como causa. Quem usar a persistência (Main e/ou Front) trata um único tipo de erro, separado dos erros de regra de negócio (ValidacaoException).
* O arquivo `planejamento.dat` foi adicionado ao `.gitignore` pois pode mudar de acordo com o uso da execução, não é código do projeto e, em uso real, guardaria dados pessoais dos participantes.
* Todas as classes gravadas têm serialVersionUID = 1L fixo: sem ele, o Java calcula um número a partir do código, e qualquer mudança na classe impediria abrir arquivos salvos antes.
* Ao carregar, o planejamento novo é lido para uma variável à parte e só depois substitui o que está em uso. Se o arquivo for inválido, a exceção acontece antes da troca e o planejamento em uso continua igual.

### ValidacaoException
* Criamos uma exceção própria para separar os erros de regra de negócio (dados inválidos digitados pelo usuário) dos erros técnicos do Java (ex.: falha ao gravar o arquivo), permitindo tratar cada um de forma diferente. 
* As validações ficam antes dos this., no construtor e nos setters: assim nenhum dado inválido entra no objeto, nem na criação nem na atualização, e, se a validação recusar, os valores anteriores continuam iguais.
* Lembrete de validação/teste: Recusar sem alterar o estado, em toda operação que muda algo, todas as conferências vêm antes da primeira linha que altera dados. Nos construtores e setters, essa linha é o primeiro this.; no Planejamento, é o add (adicionar), o remove (remover) ou o set (editar). Se qualquer conferência recusar, nada foi mudado.
* Lembrete de validação/teste: As mensagens dizem o que aconteceu e com qual valor (ex.: "Já existe um participante com o código 1."), para que a pessoa saiba exatamente o que corrigir.