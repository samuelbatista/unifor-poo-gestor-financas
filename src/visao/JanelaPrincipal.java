package visao;

// Imports
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Janela principal: menu Arquivo, três abas e uma linha de status embaixo.
 * Atenção: Não cria os painéis, recebe já prontos de quem monta o programa (o Main).
 */
public class JanelaPrincipal extends JFrame {

    // versão do formato salvo (exigida porque JFrame é Serializable)
    private static final long serialVersionUID = 1L;

    // Nomes dos comandos do menu (o controlador usa para saber qual foi clicado)
    public static final String ABRIR = "abrir";
    public static final String SALVAR = "salvar";

    // O componente das abas vira atributo, para o aoTrocarDeAba conseguir usá-lo
    private final JTabbedPane abas = new JTabbedPane();

    // Menu Arquivo e linha de status
    private final JMenuItem itemAbrir = new JMenuItem("Abrir...");
    private final JMenuItem itemSalvar = new JMenuItem("Salvar...");
    private final JLabel rotuloStatus = new JLabel("Pronto.");

    public JanelaPrincipal(PainelParticipantes painelParticipantes,
                           PainelDespesas painelDespesas,
                           PainelResumo painelResumo) {
        super("Cabe no bolso");                              // título na barra da janela
        setDefaultCloseOperation(EXIT_ON_CLOSE);                  // fechar a janela encerra o programa
        setLayout(new BorderLayout());

        // Menu Arquivo, no alto da janela
        JMenu menuArquivo = new JMenu("Arquivo");
        menuArquivo.add(itemAbrir);
        menuArquivo.add(itemSalvar);
        JMenuBar barraDeMenu = new JMenuBar();
        barraDeMenu.add(menuArquivo);
        setJMenuBar(barraDeMenu);
        itemAbrir.setActionCommand(ABRIR);
        itemSalvar.setActionCommand(SALVAR);

        abas.addTab("Participantes", painelParticipantes);
        abas.addTab("Despesas", painelDespesas);
        abas.addTab("Resumo", painelResumo);
        add(abas, BorderLayout.CENTER);                           // as abas ocupam todo o espaço que sobrar

        rotuloStatus.setBorder(BorderFactory.createEmptyBorder(4, 10, 6, 10)); // margem: não encosta na borda
        add(rotuloStatus, BorderLayout.SOUTH);                    // linha de status embaixo

        setSize(800, 500);                           // tamanho inicial
        setLocationRelativeTo(null);                              // centraliza na tela
    }

    // Repassa o ouvinte ao componente: ele é avisado toda vez que a pessoa troca de aba
    public void aoTrocarDeAba(ChangeListener ouvinte) {
        abas.addChangeListener(ouvinte);
    }

    // Liga o "ouvinte" (o controlador) aos itens do menu Arquivo
    public void aoUsarMenuArquivo(ActionListener ouvinte) {
        itemAbrir.addActionListener(ouvinte);
        itemSalvar.addActionListener(ouvinte);
    }

    public void mostrarStatus(String texto) {
        rotuloStatus.setText(texto);
    }

    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }

    // Abre o seletor para salvar; devolve o caminho escolhido ou null se a pessoa desistiu
    public String escolherArquivoParaSalvar() {
        JFileChooser seletor = new JFileChooser();
        seletor.setDialogTitle("Salvar planejamento");
        seletor.setSelectedFile(new File("planejamento.dat"));   // nome sugerido
        if (seletor.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return null;                                          // cancelou
        }
        File escolhido = seletor.getSelectedFile();
        if (escolhido.exists()) {                                 // o seletor não avisa: perguntamos nós
            int resposta = JOptionPane.showConfirmDialog(this,
                    "O arquivo " + escolhido.getName() + " já existe. Sobrescrever?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (resposta != JOptionPane.YES_OPTION) {
                return null;                                      // não quis sobrescrever
            }
        }
        return escolhido.getAbsolutePath();
    }

    // Abre o seletor para abrir; devolve o caminho escolhido ou null se a pessoa desistiu
    public String escolherArquivoParaAbrir() {
        JFileChooser seletor = new JFileChooser();
        seletor.setDialogTitle("Abrir planejamento");
        if (seletor.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return null;                                          // cancelou
        }
        return seletor.getSelectedFile().getAbsolutePath();
    }
}