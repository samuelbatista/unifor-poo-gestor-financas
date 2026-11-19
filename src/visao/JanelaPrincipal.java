package visao;

// Imports
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;

/**
 * Janela principal: três abas e uma linha de status embaixo.
 * Não cria os painéis: recebe-os prontos de quem monta o programa (o Main).
 */
public class JanelaPrincipal extends JFrame {

    // versão do formato salvo (exigida porque JFrame é Serializable)
    private static final long serialVersionUID = 1L;

    // O componente das abas vira atributo, para o aoTrocarDeAba conseguir usá-lo
    private final JTabbedPane abas = new JTabbedPane();

    public JanelaPrincipal(PainelParticipantes painelParticipantes,
                           PainelDespesas painelDespesas,
                           PainelResumo painelResumo) {
        super("Cabe no bolso");                              // título na barra da janela
        setDefaultCloseOperation(EXIT_ON_CLOSE);                  // fechar a janela encerra o programa
        setLayout(new BorderLayout());

        abas.addTab("Participantes", painelParticipantes);
        abas.addTab("Despesas", painelDespesas);
        abas.addTab("Resumo", painelResumo);
        add(abas, BorderLayout.CENTER);                           // as abas ocupam todo o espaço que sobrar

        add(new JLabel(" Pronto."), BorderLayout.SOUTH);     // linha de status embaixo

        setSize(800, 500);                           // tamanho inicial
        setLocationRelativeTo(null);                              // centraliza na tela
    }

    // Repassa o ouvinte ao componente: ele é avisado toda vez que a pessoa troca de aba
    public void aoTrocarDeAba(ChangeListener ouvinte) {
        abas.addChangeListener(ouvinte);
    }
}