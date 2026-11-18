package modelo;

// Importa a classe do Java para fazer as listagens
import java.util.List;

/**
 * A DespesaPorPessoa utilizará a Despesa como base para tratar as Despesas Por Pessoa
 * Despesas Por Pessoa são aquelas que tem um valor padrão para cada pessoa participante da despesa
 */
public class DespesaPorPessoa extends Despesa {

    // versão do formato salvo (muda só se a classe mudar de forma incompatível)
    private static final long serialVersionUID = 1L;

    public DespesaPorPessoa(int id, String descricao, long valorEmCentavos, List<Participante> participantes) throws ValidacaoException {
        super(id, descricao, valorEmCentavos, participantes); // repassa para a mãe (Despesa), que valida e guarda
    }

    @Override // Serve para substituir o método da Classe mãe
    public long calcularValorDe(Participante participante) {
        if (!getParticipantes().contains(participante)) {
            return 0; // quem não participa paga zero
        }
        return getValorEmCentavos(); // cada um paga o quanto já informado na despesa por pessoa
    }

}
