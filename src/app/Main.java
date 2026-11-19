package app;

// Imports
import controle.ControladorDespesas;
import controle.ControladorParticipantes;
import modelo.Planejamento;
import visao.JanelaPrincipal;
import visao.PainelDespesas;
import visao.PainelParticipantes;
import visao.PainelResumo;

import javax.swing.SwingUtilities;

// Porta de entrada do programa:
// Monta modelo, painéis, controladores e janela (nessa ordem).
// Os testes do modelo (sem tela) ficam em TestesManuais.
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Model
            Planejamento planejamento = new Planejamento();

            // 2. View
            PainelParticipantes painelParticipantes = new PainelParticipantes();
            PainelDespesas painelDespesas = new PainelDespesas();
            PainelResumo painelResumo = new PainelResumo();

            // 3. Controller
            ControladorParticipantes controladorParticipantes = new ControladorParticipantes(planejamento, painelParticipantes);
            ControladorDespesas controladorDespesas = new ControladorDespesas(planejamento, painelDespesas);

            // 4. View: Renderizar tela Principal
            JanelaPrincipal janela = new JanelaPrincipal(painelParticipantes, painelDespesas, painelResumo);
            janela.aoTrocarDeAba(e -> controladorDespesas.recarregarTela()); // ao entrar na aba, a tela se atualiza
            janela.setVisible(true);
        });
    }
}