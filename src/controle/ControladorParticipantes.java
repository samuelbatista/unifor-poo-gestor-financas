package controle;

// Imports
import modelo.Planejamento;
import visao.PainelParticipantes;

/**
 * Controlador da aba Participantes: Faz o intermédio entre Visão e Modelo
 */
public class ControladorParticipantes {

    // Atributos: o modelo (a "cozinha") e o painel (a "mesa") que este controlador atende
    private Planejamento planejamento;
    private PainelParticipantes painel;

    public ControladorParticipantes(Planejamento planejamento, PainelParticipantes painel) {
        this.planejamento = planejamento;
        this.painel = painel;
        // TO-DO: ligar os botões do painel aos métodos do planejamento
    }
}