package modelo; // Indica que esta classe faz parte do pacote do Model

// Importa as classes necessárias para o Participante
import java.io.Serializable;

public class Participante implements Serializable {

    // versão do formato salvo (muda só se a classe mudar de forma incompatível)
    private static final long serialVersionUID = 1L;

    // Atributos
    private int codigo;                // Identificador único. Não pode repetir
    private String nome;               // Texto livre, mas não pode ser vazio
    private long orcamentoEmCentavos;  // R$ 70,00 = 7000 (long por aceitar valores maiores que o int)
    // Os 3 atributos ficarão como privados por motivos de segurança

    // Construtor
    public Participante(int codigo, String nome, long orcamentoEmCentavos) throws ValidacaoException {

        if (nome == null || nome.isBlank()) {
            throw new ValidacaoException("O nome do participante não pode ser vazio.");
        }

        if (orcamentoEmCentavos < 0) {
            throw new ValidacaoException("O orçamento não pode ser negativo (recebido: " + orcamentoEmCentavos + ").");
        }

        this.codigo = codigo;
        this.nome = nome;
        this.orcamentoEmCentavos = orcamentoEmCentavos;
    }

    // Getters
    public int getCodigo()               { return codigo; }
    public String getNome()              { return nome; }
    public long getOrcamentoEmCentavos() { return orcamentoEmCentavos; }

    // Setters (com a mesma validação do construtor)
    public void setNome(String nome) throws ValidacaoException {
        if (nome == null || nome.isBlank()) {
            throw new ValidacaoException("O nome do participante não pode ser vazio.");
        }
        this.nome = nome;
    }

    public void setOrcamentoEmCentavos(long orcamentoEmCentavos) throws ValidacaoException {
        if (orcamentoEmCentavos < 0) {
            throw new ValidacaoException("O orçamento não pode ser negativo (recebido: " + orcamentoEmCentavos + ").");
        }
        this.orcamentoEmCentavos = orcamentoEmCentavos;
    }
}