package visao;

// Imports
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Painel da aba Participantes.
 */
public class PainelParticipantes extends JPanel {

    // versão do formato salvo (exigida porque JPanel é Serializable)
    private static final long serialVersionUID = 1L;

    public PainelParticipantes() {
        setLayout(new BorderLayout());                                   // divide o painel em 5 regiões
        add(new JLabel("Participantes", JLabel.CENTER), BorderLayout.CENTER); // texto no meio
    }
}