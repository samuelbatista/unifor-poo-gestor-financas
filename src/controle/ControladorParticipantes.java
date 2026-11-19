package controle;

// Imports
import modelo.Participante;
import modelo.Planejamento;
import modelo.ValidacaoException;
import visao.FormatoDeMoeda;
import visao.PainelParticipantes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador da aba Participantes: ouve os botões do painel, lê o que foi digitado,
 * chama o modelo e manda o painel mostrar o resultado ou a mensagem de recusa.
 */
public class ControladorParticipantes implements ActionListener {

    // Atributos: o modelo (a "cozinha") e o painel (a "mesa") que este controlador atende
    private final Planejamento planejamento;
    private final PainelParticipantes painel;

    public ControladorParticipantes(Planejamento planejamento, PainelParticipantes painel) {
        this.planejamento = planejamento;
        this.painel = painel;
        painel.aoClicarNosBotoes(this);                              // passa a "ouvir" os botões
        painel.mostrarParticipantes(planejamento.getParticipantes()); // mostra o que já existe
    }

    // Chamado pelo Swing a cada clique em Cadastrar, Alterar ou Limpar
    @Override
    public void actionPerformed(ActionEvent evento) {
        String comando = evento.getActionCommand();

        if (PainelParticipantes.LIMPAR.equals(comando)) {
            painel.limparFormulario();
            return;
        }

        try {
            // 1. Lê e converte o que foi digitado
            int codigo = lerCodigo();
            String nome = painel.getNomeDigitado().trim();
            long orcamento = FormatoDeMoeda.paraCentavos(painel.getOrcamentoDigitado());

            // 2. Pede ao modelo (quem decide é ele)
            if (PainelParticipantes.CADASTRAR.equals(comando)) {
                planejamento.adicionarParticipante(new Participante(codigo, nome, orcamento));
                painel.mostrarMensagem("Participante cadastrado.");
            } else if (PainelParticipantes.ALTERAR.equals(comando)) {
                planejamento.alterarParticipante(codigo, nome, orcamento);
                painel.mostrarMensagem("Participante alterado.");
            }

            // 3. Deu certo: atualiza a tabela e limpa o formulário
            painel.mostrarParticipantes(planejamento.getParticipantes());
            painel.limparFormulario();

        } catch (ValidacaoException | NumberFormatException e) {
            // Recusou: só mostra o motivo; a tabela e o formulário ficam como estavam
            painel.mostrarMensagem(e.getMessage());
        }
    }

    // Converte o código digitado em número; texto inválido vira mensagem clara
    private int lerCodigo() {
        try {
            return Integer.parseInt(painel.getCodigoDigitado().trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Código inválido: digite um número inteiro, por exemplo 1.");
        }
    }
}