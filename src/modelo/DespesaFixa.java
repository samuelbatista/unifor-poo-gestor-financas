package modelo;

// Importa a classe do Java para fazer as listagens
import java.util.List;

/**
 * A DespesaFixa utilizará a Despesa como base para tratar as Despesas Fixas
 * Despesas Fixas são aquelas que tem um valor total que é dividido entre os participantes da despesa
 */
public class DespesaFixa extends Despesa {

    public DespesaFixa(int id, String descricao, long valorEmCentavos, List<Participante> participantes) throws ValidacaoException {
        super(id, descricao, valorEmCentavos, participantes); // repassa para a mãe (Despesa), que valida e guarda
    }

    @Override // Serve para substituir o método da Classe mãe
    public long calcularValorDe(Participante participante) {
        if (!getParticipantes().contains(participante)) {
            return 0; // quem não participa paga zero
        }
        long base  = getValorEmCentavos() / getParticipantes().size(); // quanto cada um paga, no mínimo (Dividindo igualmente)
        long resto = getValorEmCentavos() % getParticipantes().size(); // centavos que sobraram

        int posicao = 0; // quantos participantes desta despesa têm código menor que o dele
        for (Participante p : getParticipantes()) {
            if (p.getCodigo() < participante.getCodigo()) {
                posicao++;
            }
        }

        if (posicao < resto) {
            return base + 1; // está no começo da fila: recebe 1 centavo da sobra
        }
        return base;
    }
}