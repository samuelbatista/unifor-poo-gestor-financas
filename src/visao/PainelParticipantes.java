package visao;

// Imports
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.List;

import modelo.Participante;

/**
 * Painel da aba Participantes: formulário em cima, tabela no meio e botões embaixo.
 * Não decide nada: só mostra e entrega ao controlador o que foi digitado.
 */
public class PainelParticipantes extends JPanel {

    // versão do formato salvo (exigida porque JPanel é Serializable)
    private static final long serialVersionUID = 1L;

    // Nomes dos comandos dos botões (o controlador usa para saber qual foi clicado)
    public static final String CADASTRAR = "cadastrar";
    public static final String ALTERAR = "alterar";
    public static final String LIMPAR = "limpar";

    // Formulário: os campos onde a pessoa digita
    private final JTextField campoCodigo = new JTextField();
    private final JTextField campoNome = new JTextField();
    private final JTextField campoOrcamento = new JTextField();

    // Tabela: "linhas" guarda os dados; "tabela" mostra
    private final DefaultTableModel linhas = new DefaultTableModel(new String[] {"Código", "Nome", "Orçamento"}, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false; // ninguém edita direto na tabela: só pelo formulário
        }
    };
    private final JTable tabela = new JTable(linhas);

    // Botões
    private final JButton botaoCadastrar = new JButton("Cadastrar");
    private final JButton botaoAlterar = new JButton("Alterar");
    private final JButton botaoLimpar = new JButton("Limpar");

    public PainelParticipantes() {
        setLayout(new BorderLayout(8, 8));                          // 8 pixels de folga entre as regiões
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));     // margem em volta do painel

        // Topo: formulário com 3 linhas e 2 colunas (rótulo | campo)
        JPanel formulario = new JPanel(new GridLayout(3, 2, 4, 4));
        formulario.add(new JLabel("Código:"));
        formulario.add(campoCodigo);
        formulario.add(new JLabel("Nome:"));
        formulario.add(campoNome);
        formulario.add(new JLabel("Orçamento (R$):"));
        formulario.add(campoOrcamento);
        add(formulario, BorderLayout.NORTH);

        // Centro: tabela com rolagem; só uma linha selecionada por vez
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Embaixo: os botões, alinhados à direita
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(botaoCadastrar);
        botoes.add(botaoAlterar);
        botoes.add(botaoLimpar);
        add(botoes, BorderLayout.SOUTH);

        // Cada botão carrega o nome do seu comando
        botaoCadastrar.setActionCommand(CADASTRAR);
        botaoAlterar.setActionCommand(ALTERAR);
        botaoLimpar.setActionCommand(LIMPAR);

        // Ao clicar numa linha da tabela, o formulário é preenchido com aquela pessoa
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherComLinhaSelecionada();
            }
        });
    }


    // ===== Leitura: o controlador pergunta o que foi digitado =====
    public String getCodigoDigitado()    { return campoCodigo.getText(); }
    public String getNomeDigitado()      { return campoNome.getText(); }
    public String getOrcamentoDigitado() { return campoOrcamento.getText(); }

    // ===== Ações: o controlador manda o painel mostrar ou limpar =====

    // Liga o "ouvinte" (o controlador) aos três botões
    public void aoClicarNosBotoes(ActionListener ouvinte) {
        botaoCadastrar.addActionListener(ouvinte);
        botaoAlterar.addActionListener(ouvinte);
        botaoLimpar.addActionListener(ouvinte);
    }

    // Mostra uma mensagem numa janelinha (aviso de sucesso ou de recusa)
    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }

    // Esvazia a tabela e escreve de novo uma linha por participante (só lê, não decide nada)
    public void mostrarParticipantes(List<Participante> participantes) {
        linhas.setRowCount(0);
        for (Participante p : participantes) {
            linhas.addRow(new Object[] {
                    p.getCodigo(),
                    p.getNome(),
                    FormatoDeMoeda.paraReais(p.getOrcamentoEmCentavos())
            });
        }
    }

    // Deixa o formulário em branco, pronto para um novo cadastro
    public void limparFormulario() {
        tabela.clearSelection();
        campoCodigo.setText("");
        campoNome.setText("");
        campoOrcamento.setText("");
        campoCodigo.setEditable(true); // volta a permitir digitar o código
    }

    // Copia a linha clicada para o formulário; o código fica travado (código não muda)
    private void preencherComLinhaSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return; // nada selecionado
        }
        campoCodigo.setText(String.valueOf(linhas.getValueAt(linha, 0)));
        campoNome.setText((String) linhas.getValueAt(linha, 1));
        campoOrcamento.setText(((String) linhas.getValueAt(linha, 2)).replace("R$ ", ""));
        campoCodigo.setEditable(false);
    }

}