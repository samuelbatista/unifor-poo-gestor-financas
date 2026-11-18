package modelo;

// Importa as classes necessárias para a Despesa
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * A Despesa será uma classe abstrata que servirá de base para as Classes Filhas (DespesaFixa e DespesaPorPessoa)
 */
public abstract class Despesa implements Serializable {

    // versão do formato salvo (muda só se a classe mudar de forma incompatível)
    private static final long serialVersionUID = 1L;

    // Atributos
    private int id;
    private String descricao;
    private long valorEmCentavos;
    private List<Participante> participantes;

    // Construtor (chamado pelas classes filhas através do super)
    public Despesa(int id, String descricao, long valorEmCentavos, List<Participante> participantes) throws ValidacaoException {

        // Validações para garantir que uma despesa será criada com um valor maior que zero e com pelo menos 1 participante
        if (descricao == null || descricao.isBlank()) {
            throw new ValidacaoException("A despesa precisa ter uma descrição.");
        }
        if (valorEmCentavos <= 0) {
            throw new ValidacaoException("O valor da despesa precisa ser maior que zero (recebido: " + valorEmCentavos + ").");
        }
        if (participantes == null || participantes.isEmpty()) {
            throw new ValidacaoException("A despesa precisa ter pelo menos um participante.");
        }

        // Verifica se há participantes repetidos na despesa
        List<Participante> vistos = new ArrayList<>();
        for (Participante p : participantes) {
            if (vistos.contains(p)) {
                throw new ValidacaoException("O participante " + p.getNome() + " aparece duas vezes na mesma despesa.");
            }
            vistos.add(p);
        }

        this.id = id;
        this.descricao = descricao;
        this.valorEmCentavos = valorEmCentavos;
        this.participantes = participantes;
    }

    // Getters
    public int getId()                           { return id; }
    public String getDescricao()                 { return descricao; }
    public long getValorEmCentavos()             { return valorEmCentavos; }
    public List<Participante> getParticipantes() { return participantes; }

    // Cada tipo de despesa (filha) implementa a sua regra de cálculo
    public abstract long calcularValorDe(Participante participante);

    // Soma quanto cada participante paga nesta despesa
    public long calcularTotal() {
        long total = 0;                            // 1. Começa com a conta zerada
        for (Participante p : participantes) {     // 2. Para cada participante (p)
            total = total + calcularValorDe(p);    //    é somado ao total o valor que ele paga (Perceba o polimorfismo)
        }
        return total;                              // 3. Devolve o valor total da despesa
    }
}