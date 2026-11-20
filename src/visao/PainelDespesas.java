package visao;

// Imports
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import modelo.Despesa;
import modelo.DespesaFixa;
import modelo.Participante;

/**
 * Painel da aba Despesas: formulário e lista de participantes em cima, tabela no meio,
 * totais e botões embaixo. Não decide nada: só mostra e entrega ao controlador o que foi escolhido.
 */
public class PainelDespesas extends JPanel {

    // versão do formato salvo (exigida porque JPanel é Serializable)
    private static final long serialVersionUID = 1L;

    // Nomes dos comandos dos botões (o controlador usa para saber qual foi clicado)
    public static final String INCLUIR = "incluir";
    public static final String EXCLUIR = "excluir";
    public static final String SALVAR = "salvar";
    public static final String LIMPAR = "limpar";

    // Textos do aviso de modo (nova despesa ou edição)
    private static final String MODO_INCLUIR = "Nova despesa: preencha e clique em Incluir.";

    // Opções da caixa de tipo
    public static final String FIXA = "Fixa";
    public static final String POR_PESSOA = "Por pessoa";

    // Formulário
    private final JTextField campoDescricao = new JTextField();
    private final JTextField campoValor = new JTextField();
    private final JComboBox<String> comboTipo = new JComboBox<>(new String[] {FIXA, POR_PESSOA});

    // Lista de participantes: uma caixa de marcar por pessoa, ligada ao código dela
    private final JPanel listaParticipantes = new JPanel(new GridLayout(0, 1));
    private final Map<Integer, JCheckBox> caixas = new LinkedHashMap<>();

    // Tabela: "linhas" guarda os dados; "tabela" mostra
    private final DefaultTableModel linhas = new DefaultTableModel(new String[] {"Id", "Descrição", "Tipo", "Valor"}, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false; // ninguém edita direto na tabela
        }
    };
    private final JTable tabela = new JTable(linhas);

    // Totais e botões
    private final JTextArea areaTotais = new JTextArea(2, 40);
    private final JButton botaoIncluir = new JButton("Incluir");
    private final JButton botaoExcluir = new JButton("Excluir selecionada");
    private final JButton botaoSalvar = new JButton("Salvar alterações");
    private final JButton botaoLimpar = new JButton("Limpar");

    // Aviso que diz em qual modo a tela está
    private final JLabel rotuloModo = new JLabel(MODO_INCLUIR);

    public PainelDespesas() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Topo, à esquerda: formulário com 3 linhas e 2 colunas (rótulo | campo)
        JPanel formulario = new JPanel(new GridLayout(3, 2, 4, 4));
        formulario.add(new JLabel("Descrição:"));
        formulario.add(campoDescricao);
        formulario.add(new JLabel("Valor (R$):"));
        formulario.add(campoValor);
        formulario.add(new JLabel("Tipo:"));
        formulario.add(comboTipo);

        // Topo, à direita: participantes com rolagem
        JScrollPane rolagemParticipantes = new JScrollPane(listaParticipantes);
        rolagemParticipantes.setBorder(BorderFactory.createTitledBorder("Participantes desta despesa"));
        rolagemParticipantes.setPreferredSize(new Dimension(220, 110));

        JPanel topo = new JPanel(new BorderLayout(8, 8));
        topo.add(rotuloModo, BorderLayout.NORTH);
        topo.add(formulario, BorderLayout.CENTER);
        topo.add(rolagemParticipantes, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        // Centro: tabela com rolagem; só uma linha selecionada por vez
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Embaixo: totais (só leitura) e botões
        areaTotais.setEditable(false);
        areaTotais.setLineWrap(true);
        areaTotais.setWrapStyleWord(true);
        areaTotais.setBorder(BorderFactory.createTitledBorder("Totais"));

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(botaoIncluir);
        botoes.add(botaoSalvar);
        botoes.add(botaoExcluir);
        botoes.add(botaoLimpar);

        JPanel baixo = new JPanel(new BorderLayout(8, 8));
        baixo.add(areaTotais, BorderLayout.CENTER);
        baixo.add(botoes, BorderLayout.SOUTH);
        add(baixo, BorderLayout.SOUTH);

        // Cada botão carrega o nome do seu comando
        botaoIncluir.setActionCommand(INCLUIR);
        botaoExcluir.setActionCommand(EXCLUIR);
        botaoSalvar.setActionCommand(SALVAR);
        botaoLimpar.setActionCommand(LIMPAR);

        // "Salvar alterações" só fica ativo quando há uma linha selecionada
        botaoSalvar.setEnabled(false);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                botaoSalvar.setEnabled(tabela.getSelectedRow() >= 0);
            }
        });
    }

    // ===== Leitura: o controlador pergunta o que foi digitado ou escolhido =====
    public String getDescricaoDigitada() { return campoDescricao.getText(); }
    public String getValorDigitado()     { return campoValor.getText(); }
    public String getTipoEscolhido()     { return (String) comboTipo.getSelectedItem(); }

    // Códigos dos participantes marcados, na ordem da lista
    public List<Integer> getCodigosMarcados() {
        List<Integer> marcados = new ArrayList<>();
        for (Map.Entry<Integer, JCheckBox> par : caixas.entrySet()) {
            if (par.getValue().isSelected()) {
                marcados.add(par.getKey());
            }
        }
        return marcados;
    }

    // Id da despesa da linha selecionada; -1 se nenhuma estiver selecionada
    public int getIdSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return -1;
        }
        return (Integer) linhas.getValueAt(linha, 0);
    }

    // ===== Ações: o controlador manda o painel mostrar ou limpar =====

    // Liga o "ouvinte" (o controlador) aos botões
    public void aoClicarNosBotoes(ActionListener ouvinte) {
        botaoIncluir.addActionListener(ouvinte);
        botaoExcluir.addActionListener(ouvinte);
        botaoSalvar.addActionListener(ouvinte);
        botaoLimpar.addActionListener(ouvinte);
    }

    // Liga um "ouvinte" à seleção da tabela (o controlador quer saber quando uma linha é clicada)
    public void aoSelecionarLinha(ListSelectionListener ouvinte) {
        tabela.getSelectionModel().addListSelectionListener(ouvinte);
    }

    // Preenche o formulário com os dados de uma despesa e avisa que a tela está em modo de edição
    public void preencherFormulario(int id, String descricao, String valor, String tipo, List<Integer> codigosMarcados) {
        campoDescricao.setText(descricao);
        campoValor.setText(valor);
        comboTipo.setSelectedItem(tipo);
        for (Map.Entry<Integer, JCheckBox> par : caixas.entrySet()) {
            par.getValue().setSelected(codigosMarcados.contains(par.getKey()));
        }
        rotuloModo.setText("Editando a despesa de id " + id + ": altere e clique em Salvar alterações (ou Limpar para voltar).");
    }

    // Mostra uma mensagem numa janelinha
    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }

    // Pergunta "sim ou não"; devolve true se a pessoa clicar em Sim
    public boolean confirmar(String pergunta) {
        int resposta = JOptionPane.showConfirmDialog(this, pergunta, "Confirmar", JOptionPane.YES_NO_OPTION);
        return resposta == JOptionPane.YES_OPTION;
    }

    // Refaz a lista de caixas de marcar; quem já estava marcado continua marcado
    public void mostrarParticipantes(List<Participante> participantes) {
        List<Integer> marcadosAntes = getCodigosMarcados();
        listaParticipantes.removeAll();
        caixas.clear();
        for (Participante p : participantes) {
            JCheckBox caixa = new JCheckBox(p.getNome());
            caixa.setSelected(marcadosAntes.contains(p.getCodigo()));
            caixas.put(p.getCodigo(), caixa);
            listaParticipantes.add(caixa);
        }
        listaParticipantes.revalidate(); // recalcula o leiaute
        listaParticipantes.repaint();    // redesenha
    }

    // Esvazia a tabela e escreve de novo uma linha por despesa (só lê, não decide nada)
    public void mostrarDespesas(List<Despesa> despesas) {
        linhas.setRowCount(0);
        for (Despesa d : despesas) {
            String tipo = (d instanceof DespesaFixa) ? FIXA : POR_PESSOA;
            linhas.addRow(new Object[] {
                    d.getId(),
                    d.getDescricao(),
                    tipo,
                    FormatoDeMoeda.paraReais(d.getValorEmCentavos())
            });
        }
    }

    // Mostra o texto dos totais, já pronto (quem calcula é o modelo; quem monta o texto é o controlador)
    public void mostrarTotais(String texto) {
        areaTotais.setText(texto);
    }

    // Deixa o formulário em branco, pronto para uma nova despesa
    public void limparFormulario() {
        tabela.clearSelection();
        campoDescricao.setText("");
        campoValor.setText("");
        comboTipo.setSelectedIndex(0);
        for (JCheckBox caixa : caixas.values()) {
            caixa.setSelected(false);
        }
        rotuloModo.setText(MODO_INCLUIR);
    }
}