package visao;

// Imports
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

/**
 * Janela principal: três abas e uma linha de status embaixo.
 * Não cria os painéis: recebe-os prontos de quem monta o programa (o Main).
 */
public class JanelaPrincipal extends JFrame {

    // versão do formato salvo (exigida porque JFrame é Serializable)
    private static final long serialVersionUID = 1L;

    public JanelaPrincipal(PainelParticipantes painelParticipantes,
                           PainelDespesas painelDespesas,
                           PainelResumo painelResumo) {
        super("Cabe no bolso");                              // título na barra da janela
        setDefaultCloseOperation(EXIT_ON_CLOSE);                  // fechar a janela encerra o programa
        setLayout(new BorderLayout());

        JTabbedPane abas = new JTabbedPane();                     // o "fichário"
        abas.addTab("Participantes", painelParticipantes);
        abas.addTab("Despesas", painelDespesas);
        abas.addTab("Resumo", painelResumo);
        add(abas, BorderLayout.CENTER);                           // as abas ocupam todo o espaço que sobrar

        add(new JLabel(" Pronto."), BorderLayout.SOUTH);     // linha de status embaixo

        setSize(800, 500);                           // tamanho inicial
        setLocationRelativeTo(null);                              // centraliza na tela
    }
}