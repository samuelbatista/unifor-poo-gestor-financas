package visao;

// Imports

import javax.swing.*;
import java.awt.*;

/**
 * Painel da aba Resumo.
 */
public class PainelResumo extends JPanel {

    // versão do formato salvo (exigida porque JPanel é Serializable)
    private static final long serialVersionUID = 1L;

    public PainelResumo() {
        setLayout(new BorderLayout());                                   // divide o painel em 5 regiões
        add(new JLabel("Resumo", JLabel.CENTER), BorderLayout.CENTER); // texto no meio
    }
}