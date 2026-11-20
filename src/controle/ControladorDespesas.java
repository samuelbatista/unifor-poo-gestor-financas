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
 * Controlador da aba Despesas: inclui, edita e exclui despesas pelo modelo
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
        painel.aoSelecionarLinha(e -> {  // e também a seleção da tabela
            if (!e.getValueIsAdjusting()) {
                mostrarDespesaSelecionada();
            }
        });
        recarregarTela();               // mostra o que já existe
    }

    // Lê o modelo e manda o painel mostrar participantes, despesas e totais
    public void recarregarTela() {
        painel.mostrarParticipantes(planejamento.getParticipantes());
        painel.mostrarDespesas(planejamento.getDespesas());
        painel.mostrarTotais(montarTextoDosTotais());
    }

    // Chamado pelo Swing a cada clique em Incluir, Salvar alterações, Excluir selecionada ou Limpar
    @Override
    public void actionPerformed(ActionEvent evento) {
        String comando = evento.getActionCommand();
        if (PainelDespesas.INCLUIR.equals(comando)) {
            incluir();
        } else if (PainelDespesas.EXCLUIR.equals(comando)) {
            excluir();
        } else if (PainelDespesas.SALVAR.equals(comando)) {
            salvar();
        } else if (PainelDespesas.LIMPAR.equals(comando)) {
            painel.limparFormulario();
        }
    }

    private void incluir() {
        try {
            Despesa nova = montarDespesaDoFormulario(planejamento.gerarIdDespesa());
            planejamento.adicionarDespesa(nova);   // quem decide é o modelo
            painel.limparFormulario();
            recarregarTela();
        } catch (ValidacaoException | NumberFormatException e) {
            painel.mostrarMensagem(e.getMessage()); // recusou: nada mais muda
        }
    }

    private void salvar() {
        int id = painel.getIdSelecionado();
        if (id < 0) {
            painel.mostrarMensagem("Selecione na tabela a despesa que quer alterar.");
            return;
        }
        try {
            Despesa nova = montarDespesaDoFormulario(id); // mesmo id; o tipo pode ter mudado
            planejamento.editarDespesa(id, nova);         // o modelo confere tudo e troca no mesmo lugar
            painel.limparFormulario();
            recarregarTela();
        } catch (ValidacaoException | NumberFormatException e) {
            painel.mostrarMensagem(e.getMessage());       // recusou: tabela, totais e formulário como estavam
        }
    }

    // Lê o formulário e monta a despesa do tipo escolhido (usado por Incluir e por Salvar alterações)
    private Despesa montarDespesaDoFormulario(int id) throws ValidacaoException {
        String descricao = painel.getDescricaoDigitada().trim();
        long valor = FormatoDeMoeda.paraCentavos(painel.getValorDigitado());
        List<Participante> escolhidos = new ArrayList<>();
        for (int codigo : painel.getCodigosMarcados()) {
            escolhidos.add(planejamento.buscarParticipante(codigo));
        }
        if (PainelDespesas.POR_PESSOA.equals(painel.getTipoEscolhido())) {
            return new DespesaPorPessoa(id, descricao, valor, escolhidos);
        }
        return new DespesaFixa(id, descricao, valor, escolhidos);
    }

    // Quando uma linha é clicada: busca a despesa no modelo e manda o painel preencher o formulário
    private void mostrarDespesaSelecionada() {
        int id = painel.getIdSelecionado();
        if (id < 0) {
            return; // nada selecionado
        }
        Despesa despesa = planejamento.buscarDespesa(id);
        List<Integer> codigos = new ArrayList<>();
        for (Participante p : despesa.getParticipantes()) {
            codigos.add(p.getCodigo());
        }
        String tipo = (despesa instanceof DespesaPorPessoa) ? PainelDespesas.POR_PESSOA : PainelDespesas.FIXA;
        painel.preencherFormulario(id, despesa.getDescricao(),
                FormatoDeMoeda.paraTexto(despesa.getValorEmCentavos()), tipo, codigos);
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
            painel.limparFormulario();
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