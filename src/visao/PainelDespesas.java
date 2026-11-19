package visao;

// Imports

import javax.swing.*;
import java.awt.*;

/**
 * Painel da aba Despesas.
 */
public class PainelDespesas extends JPanel {

    // versão do formato salvo (exigida porque JPanel é Serializable)
    private static final long serialVersionUID = 1L;

    public PainelDespesas() {
        setLayout(new BorderLayout());                                   // divide o painel em 5 regiões
        add(new JLabel("Despesas", JLabel.CENTER), BorderLayout.CENTER); // texto no meio
    }
}