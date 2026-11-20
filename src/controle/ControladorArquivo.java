package controle;

// Imports
import modelo.Planejamento;
import persistencia.PersistenciaException;
import persistencia.PersistenciaPlanejamento;
import visao.JanelaPrincipal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Controlador do menu Arquivo: salva e abre o planejamento.
 * Conhece só o contrato (PersistenciaPlanejamento), nunca a classe de arquivo:
 * quem decide qual persistência usar é o Main.
 */
public class ControladorArquivo implements ActionListener {

    // Atributos: o modelo, o contrato de persistência, a janela e o que fazer depois de abrir
    private final Planejamento planejamento;
    private final PersistenciaPlanejamento persistencia;
    private final JanelaPrincipal janela;
    private final Runnable aoAbrir; // "lembrete" entregue pelo Main: atualizar todas as abas

    public ControladorArquivo(Planejamento planejamento, PersistenciaPlanejamento persistencia,
                              JanelaPrincipal janela, Runnable aoAbrir) {
        this.planejamento = planejamento;
        this.persistencia = persistencia;
        this.janela = janela;
        this.aoAbrir = aoAbrir;
        janela.aoUsarMenuArquivo(this); // passa a "ouvir" o menu
    }

    // Chamado pelo Swing a cada clique em Arquivo > Abrir ou Arquivo > Salvar
    @Override
    public void actionPerformed(ActionEvent evento) {
        String comando = evento.getActionCommand();
        if (JanelaPrincipal.SALVAR.equals(comando)) {
            salvar();
        } else if (JanelaPrincipal.ABRIR.equals(comando)) {
            abrir();
        }
    }

    private void salvar() {
        String caminho = janela.escolherArquivoParaSalvar();
        if (caminho == null) {
            return; // cancelou: nada acontece
        }
        try {
            persistencia.salvar(planejamento, caminho);
            janela.mostrarStatus("Planejamento salvo em " + new File(caminho).getName());
        } catch (PersistenciaException e) {
            janela.mostrarMensagem(e.getMessage());
        }
    }

    private void abrir() {
        String caminho = janela.escolherArquivoParaAbrir();
        if (caminho == null) {
            return; // cancelou: nada acontece
        }
        try {
            Planejamento carregado = persistencia.carregar(caminho); // se falhar, para aqui
            planejamento.substituirConteudoPor(carregado);           // e o planejamento em uso fica intacto
            aoAbrir.run();                                           // todas as abas mostram o conteúdo novo
            janela.mostrarStatus("Planejamento aberto: " + new File(caminho).getName());
        } catch (PersistenciaException e) {
            janela.mostrarMensagem(e.getMessage());
        }
    }
}