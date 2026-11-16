package modelo; // Indica que esta classe faz parte do pacote do Model

public class Participante {
    private int codigo;                // Identificador único. Não pode repetir
    private String nome;               // String convencional. Aceita todos os caracateres
    private long orcamentoEmCentavos;  // R$ 70,00 = 7000 (long por aceitar valores maiores que o int)
    // Os 3 atributos ficarão como privados por motivos de segurança

    // Construtor
    public Participante(int codigo, String nome, long orcamentoEmCentavos) {
        this.codigo = codigo;
        this.nome = nome;
        this.orcamentoEmCentavos = orcamentoEmCentavos;
    }

    // Getters
    public int getCodigo()               { return codigo; }
    public String getNome()              { return nome; }
    public long getOrcamentoEmCentavos() { return orcamentoEmCentavos; }
}