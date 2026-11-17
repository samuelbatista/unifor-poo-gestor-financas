# Diagrama de classes v1

## Observações
* Utilizamos o Mermaid para que os diagramas de classe possam ser vistos em tempo real e versionados junto com o projeto
* Linhas com `-` indicam que é `private`
* Linhas com `+` indicam que é `public`

## Diagrama 

```mermaid
classDiagram
    
    %% ===== Classes =====
    class Planejamento {
        %% Atributos
        - participantes: List~Participante~
        - despesas: List~Despesa~
        
        %% Método para o CRUD de Participante
        + adicionarParticipante(participante: Participante) void
        + buscarParticipante(codigo: int) Participante
        %% Update será feito pelos setters do Participante
        %% Delete (Pendente / Não obrigatório)
        
        %% Getters: Traz os valores
        + getParticipantes() List~Participante~
        + getDespesas() List~Despesa~
        
        %% Método para o CRUD de Despesa
        + adicionarDespesa(despesa: Despesa) void
        + buscarDespesa(id: int) Despesa
        + editarDespesa(id: int, novaDespesa: Despesa) void
        + removerDespesa(id: int) void
        
        %% Cálculos (Resumo)
        + calcularTotalDe(participante: Participante) long
        + calcularCustoTotal() long
        + calcularExcessoDe(participante: Participante) long
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
        %% Método para calcular o valor da despesa fixa por participante
        + calcularValorDe(participante: Participante) long
    }

    class DespesaPorPessoa {
        %% Método para calcular o valor da despesa (não fixa) por participante
        + calcularValorDe(participante: Participante) long
    }
    
    class PersistenciaPlanejamento {
        <<interface>>
        + salvar(planejamento: Planejamento) void
        + carregar() Planejamento
    }
    
    class PersistenciaEmArquivo {
        - caminhoDoArquivo: String
        + salvar(planejamento: Planejamento) void
        + carregar() Planejamento
    }

    %% ===== Heranças =====
    Despesa <|-- DespesaFixa
    Despesa <|-- DespesaPorPessoa
    Exception <|-- ValidacaoException

    %% ===== Implementações =====
    PersistenciaPlanejamento <|.. PersistenciaEmArquivo

    %% ===== Associações =====
    Planejamento "1" --> "0..*" Participante : cadastra
    Planejamento "1" --> "0..*" Despesa : registra
    Despesa "0..*" --> "1..*" Participante : divide entre

    %% ===== Dependências =====
    PersistenciaPlanejamento ..> Planejamento : salva e carrega
    Participante ..> ValidacaoException : lança
```

## Decisões

### Planejamento
* Criamos editarDespesa em vez de setters na Despesa: a edição cria uma despesa nova, com o mesmo ID, que substitui a anterior. Assim é possível trocar o tipo (Fixa <-> Por pessoa), o que um setter não faria, e a despesa anterior só é substituída se a nova for válida.
* Não utilizaremos ainda um método para excluir participante pois não é obrigatório (podemos criar ao final, se tivermos tempo suficiente). Se for criado, deve impedir a exclusão enquanto o participante estiver em alguma despesa.

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

### ValidacaoException
* Criamos uma exceção própria para separar os erros de regra de negócio (dados inválidos digitados pelo usuário) dos erros técnicos do Java (ex.: falha ao gravar o arquivo), permitindo tratar cada um de forma diferente. 
* As validações ficam antes dos this., no construtor e nos setters: assim nenhum dado inválido entra no objeto, nem na criação nem na atualização, e, se a validação recusar, os valores anteriores continuam iguais.