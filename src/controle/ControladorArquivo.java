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
 * Controlador do menu Arquivo: começa um planejamento novo, salva e abre.
 * Conhece só o contrato (PersistenciaPlanejamento), nunca a classe de arquivo:
 * quem decide qual persistência usar é o Main.
 */
public class ControladorArquivo implements ActionListener {

    // Atributos: o modelo, o contrato de persistência, a janela e o que fazer quando o conteúdo muda
    private final Planejamento planejamento;
    private final PersistenciaPlanejamento persistencia;
    private final JanelaPrincipal janela;
    private final Runnable atualizarTodasAsAbas; // "lembrete" entregue pelo Main (usado após Novo e Abrir)

    public ControladorArquivo(Planejamento planejamento, PersistenciaPlanejamento persistencia,
                              JanelaPrincipal janela, Runnable atualizarTodasAsAbas) {
        this.planejamento = planejamento;
        this.persistencia = persistencia;
        this.janela = janela;
        this.atualizarTodasAsAbas = atualizarTodasAsAbas;
        janela.aoUsarMenuArquivo(this); // passa a "ouvir" o menu
    }

    // Chamado pelo Swing a cada clique em Arquivo > Novo planejamento, Abrir ou Salvar
    @Override
    public void actionPerformed(ActionEvent evento) {
        String comando = evento.getActionCommand();
        if (JanelaPrincipal.NOVO.equals(comando)) {
            novo();
        } else if (JanelaPrincipal.SALVAR.equals(comando)) {
            salvar();
        } else if (JanelaPrincipal.ABRIR.equals(comando)) {
            abrir();
        }
    }

    private void novo() {
        if (!janela.confirmar("Começar um planejamento novo? O que não foi salvo será perdido.")) {
            return; // respondeu Não (ou fechou a pergunta): nada muda
        }
        planejamento.substituirConteudoPor(new Planejamento()); // o planejamento em uso fica vazio
        atualizarTodasAsAbas.run();
        janela.mostrarStatus("Novo planejamento (vazio).");
    }

    private void salvar() {
        String caminho = janela.escolherArquivoParaSalvar();
        if (caminho == null) {
            return; // cancelou: nada acontece
        }
        try {
            persistencia.salvar(planejamento, caminho);                                   // se falhar, pula para o catch...
            janela.mostrarStatus("Planejamento salvo em " + new File(caminho).getName()); // ...e o "salvo" nunca aparece
        } catch (PersistenciaException e) {
            janela.mostrarErro("Não foi possível salvar", e.getMessage());
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
            atualizarTodasAsAbas.run();                              // todas as abas mostram o conteúdo novo
            janela.mostrarStatus("Planejamento aberto: " + new File(caminho).getName());
        } catch (PersistenciaException e) {
            janela.mostrarErro("Não foi possível abrir", e.getMessage()); // tela e modelo continuam como estavam
        }
    }
}