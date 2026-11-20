package visao;

// Imports
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

/**
 * Painel da aba Resumo: despesas, situação de cada participante, custo total e revisão.
 * Não conhece o modelo nem faz contas: só mostra os textos que o controlador entrega.
 */
public class PainelResumo extends JPanel {

    // versão do formato salvo (exigida porque JPanel é Serializable)
    private static final long serialVersionUID = 1L;

    // Nomes dos comandos dos botões (o controlador usa para saber qual foi clicado)
    public static final String MARCAR = "marcar";
    public static final String DESMARCAR = "desmarcar";

    // Tabela de despesas: "linhasDespesas" guarda os dados; a JTable mostra
    private final DefaultTableModel linhasDespesas = new DefaultTableModel(new String[] {"Descrição", "Tipo", "Valor"}, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false; // ninguém edita direto na tabela
        }
    };

    // Tabela de participantes: uma linha por pessoa, com a situação diante do orçamento
    private final DefaultTableModel linhasParticipantes = new DefaultTableModel(new String[] {"Participante", "Orçamento", "Total", "Situação"}, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };

    // Rodapé: custo total, situação da revisão e botões
    private final JLabel rotuloCustoTotal = new JLabel();
    private final JLabel rotuloRevisao = new JLabel();
    private final JButton botaoMarcar = new JButton("Marcar para revisão");
    private final JButton botaoDesmarcar = new JButton("Retirar marca de revisão");

    public PainelResumo() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // margem em volta

        // Centro: as duas tabelas, uma em cima da outra
        JScrollPane rolagemDespesas = new JScrollPane(new JTable(linhasDespesas));
        rolagemDespesas.setBorder(BorderFactory.createTitledBorder("Despesas"));
        JScrollPane rolagemParticipantes = new JScrollPane(new JTable(linhasParticipantes));
        rolagemParticipantes.setBorder(BorderFactory.createTitledBorder("Situação de cada participante"));

        JPanel tabelas = new JPanel(new GridLayout(2, 1, 8, 8));
        tabelas.add(rolagemDespesas);
        tabelas.add(rolagemParticipantes);
        add(tabelas, BorderLayout.CENTER);

        // Embaixo: custo total e revisão (à esquerda), botões (à direita)
        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.add(rotuloCustoTotal);
        textos.add(rotuloRevisao);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(botaoMarcar);
        botoes.add(botaoDesmarcar);

        JPanel baixo = new JPanel(new BorderLayout(8, 8));
        baixo.add(textos, BorderLayout.CENTER);
        baixo.add(botoes, BorderLayout.EAST);
        add(baixo, BorderLayout.SOUTH);

        // Cada botão carrega o nome do seu comando
        botaoMarcar.setActionCommand(MARCAR);
        botaoDesmarcar.setActionCommand(DESMARCAR);
    }

    // ===== Ações: o controlador manda o painel mostrar =====

    // Liga o "ouvinte" (o controlador) aos botões
    public void aoClicarNosBotoes(ActionListener ouvinte) {
        botaoMarcar.addActionListener(ouvinte);
        botaoDesmarcar.addActionListener(ouvinte);
    }

    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }

    // Apaga as linhas das duas tabelas (o controlador chama antes de preencher de novo)
    public void limpar() {
        linhasDespesas.setRowCount(0);
        linhasParticipantes.setRowCount(0);
    }

    public void adicionarDespesa(String descricao, String tipo, String valor) {
        linhasDespesas.addRow(new Object[] {descricao, tipo, valor});
    }

    public void adicionarParticipante(String nome, String orcamento, String total, String situacao) {
        linhasParticipantes.addRow(new Object[] {nome, orcamento, total, situacao});
    }

    public void mostrarCustoTotal(String custoTotal) {
        rotuloCustoTotal.setText("Custo total: " + custoTotal);
    }

    // Mostra a situação da revisão; "Retirar marca" só fica ativo quando há marca
    public void mostrarRevisao(boolean alguemExcede, boolean marcado) {
        if (marcado) {
            rotuloRevisao.setText("Planejamento MARCADO PARA REVISÃO.");
            rotuloRevisao.setForeground(Color.RED);
        } else if (alguemExcede) {
            rotuloRevisao.setText("Há participante acima do orçamento.");
            rotuloRevisao.setForeground(Color.RED);
        } else {
            rotuloRevisao.setText("Todos dentro do orçamento.");
            rotuloRevisao.setForeground(Color.DARK_GRAY);
        }
        botaoDesmarcar.setEnabled(marcado);
    }
}