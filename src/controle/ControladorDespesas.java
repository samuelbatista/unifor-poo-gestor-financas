package controle;

// Imports
import modelo.Despesa;
import modelo.DespesaFixa;
import modelo.DespesaPorPessoa;
import modelo.Participante;
import modelo.Planejamento;
import modelo.ValidacaoException;
import visao.FormatoDeMoeda;
import visao.PainelDespesas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador da aba Despesas: inclui e exclui despesas pelo modelo
 * e mantém a tabela, a lista de participantes e os totais em dia.
 */
public class ControladorDespesas implements ActionListener {

    // Atributos: o modelo e o painel que este controlador atende
    private final Planejamento planejamento;
    private final PainelDespesas painel;

    public ControladorDespesas(Planejamento planejamento, PainelDespesas painel) {
        this.planejamento = planejamento;
        this.painel = painel;
        painel.aoClicarNosBotoes(this); // passa a "ouvir" os botões
        recarregarTela();                      // mostra o que já existe
    }

    // Lê o modelo e manda o painel mostrar participantes, despesas e totais
    public void recarregarTela() {
        painel.mostrarParticipantes(planejamento.getParticipantes());
        painel.mostrarDespesas(planejamento.getDespesas());
        painel.mostrarTotais(montarTextoDosTotais());
    }

    // Chamado pelo Swing a cada clique em Incluir ou Excluir selecionada
    @Override
    public void actionPerformed(ActionEvent evento) {
        String comando = evento.getActionCommand();
        if (PainelDespesas.INCLUIR.equals(comando)) {
            incluir();
        } else if (PainelDespesas.EXCLUIR.equals(comando)) {
            excluir();
        }
    }

    private void incluir() {
        try {
            // 1. Lê e converte o que foi digitado e marcado
            String descricao = painel.getDescricaoDigitada().trim();
            long valor = FormatoDeMoeda.paraCentavos(painel.getValorDigitado());
            List<Participante> escolhidos = new ArrayList<>();
            for (int codigo : painel.getCodigosMarcados()) {
                escolhidos.add(planejamento.buscarParticipante(codigo));
            }

            // 2. Monta a despesa do tipo escolhido (o cálculo mora em cada classe do modelo)
            int id = planejamento.gerarIdDespesa();
            Despesa nova;
            if (PainelDespesas.POR_PESSOA.equals(painel.getTipoEscolhido())) {
                nova = new DespesaPorPessoa(id, descricao, valor, escolhidos);
            } else {
                nova = new DespesaFixa(id, descricao, valor, escolhidos);
            }

            // 3. Pede ao modelo (quem decide é ele)
            planejamento.adicionarDespesa(nova);

            // 4. Deu certo: limpa o formulário e atualiza tabela e totais
            painel.limparFormulario();
            recarregarTela();

        } catch (ValidacaoException | NumberFormatException e) {
            // Recusou: só mostra o motivo; nada mais muda
            painel.mostrarMensagem(e.getMessage());
        }
    }

    private void excluir() {
        int id = painel.getIdSelecionado();
        if (id < 0) {
            painel.mostrarMensagem("Selecione uma despesa na tabela para excluir.");
            return;
        }
        if (!painel.confirmar("Excluir a despesa de id " + id + "?")) {
            return; // a pessoa desistiu
        }
        try {
            planejamento.removerDespesa(id);
            recarregarTela();
        } catch (ValidacaoException e) {
            painel.mostrarMensagem(e.getMessage());
        }
    }

    // Pede ao modelo o total de cada participante e o custo total, e monta o texto em reais
    private String montarTextoDosTotais() {
        String texto = "";
        for (Participante p : planejamento.getParticipantes()) {
            texto += p.getNome() + ": " + FormatoDeMoeda.paraReais(planejamento.calcularTotalDe(p)) + "    ";
        }
        texto += "\nCusto total: " + FormatoDeMoeda.paraReais(planejamento.calcularCustoTotal());
        return texto;
    }
}