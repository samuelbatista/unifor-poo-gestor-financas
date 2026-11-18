package persistencia;

// Importa as classes necessárias para a interface
import java.io.IOException;
import modelo.Planejamento;

// Contrato: toda forma de persistência precisa saber salvar e carregar um Planejamento
public interface PersistenciaPlanejamento {
    void salvar(Planejamento planejamento) throws IOException;
    Planejamento carregar() throws IOException, ClassNotFoundException;
}