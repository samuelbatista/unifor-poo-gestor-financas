package controle;

// Imports
import modelo.Despesa;
import modelo.DespesaPorPessoa;
import modelo.Participante;
import modelo.Planejamento;
import modelo.ValidacaoException;
import visao.FormatoDeMoeda;
import visao.PainelResumo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador da aba Resumo: pergunta ao modelo os totais e a situação de cada participante
 * e entrega ao painel tudo já em texto. Nenhuma conta é feita aqui nem na tela.
 */
public class ControladorResumo implements ActionListener {

    // Atributos: o modelo e o painel que este controlador atende
    private final Planejamento planejamento;
    private final PainelResumo painel;

    public ControladorResumo(Planejamento planejamento, PainelResumo painel) {
        this.planejamento = planejamento;
        this.painel = painel;
        painel.aoClicarNosBotoes(this); // passa a "ouvir" os botões
        recarregarTela();               // mostra o que já existe
    }

    // Monta o resumo de novo a partir do modelo (chamado ao entrar na aba e após cada botão)
    public void recarregarTela() {
        painel.limpar();

        // 1. Despesas
        for (Despesa d : planejamento.getDespesas()) {
            String tipo = (d instanceof DespesaPorPessoa) ? "Por pessoa" : "Fixa";
            painel.adicionarDespesa(d.getDescricao(), tipo, FormatoDeMoeda.paraReais(d.getValorEmCentavos()));
        }

        // 2. Participantes e situação (quem decide "dentro" ou "excede" é o modelo)
        for (Participante p : planejamento.getParticipantes()) {
            String situacao;
            if (planejamento.estaDentroDoOrcamento(p)) {
                situacao = "Dentro do orçamento";
            } else {
                situacao = "Excede em " + FormatoDeMoeda.paraReais(planejamento.calcularExcessoDe(p));
            }
            painel.adicionarParticipante(p.getNome(),
                    FormatoDeMoeda.paraReais(p.getOrcamentoEmCentavos()),
                    FormatoDeMoeda.paraReais(planejamento.calcularTotalDe(p)),
                    situacao);
        }

        // 3. Custo total e revisão
        painel.mostrarCustoTotal(FormatoDeMoeda.paraReais(planejamento.calcularCustoTotal()));
        painel.mostrarRevisao(planejamento.existeParticipanteAcimaDoOrcamento(),
                planejamento.isMarcadoParaRevisao());
    }

    // Chamado pelo Swing a cada clique em "Marcar para revisão" ou "Retirar marca de revisão"
    @Override
    public void actionPerformed(ActionEvent evento) {
        String comando = evento.getActionCommand();
        if (PainelResumo.MARCAR.equals(comando)) {
            try {
                planejamento.marcarParaRevisao(); // o modelo recusa se ninguém excede
            } catch (ValidacaoException e) {
                painel.mostrarMensagem(e.getMessage());
            }
        } else if (PainelResumo.DESMARCAR.equals(comando)) {
            planejamento.desmarcarRevisao();
        }
        recarregarTela();
    }
}