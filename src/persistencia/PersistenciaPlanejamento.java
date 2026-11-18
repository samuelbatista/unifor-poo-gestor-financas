package persistencia;

// Importa as classes necessárias para a interface
import modelo.Planejamento;

// Contrato: toda forma de persistência precisa saber salvar e carregar um Planejamento
public interface PersistenciaPlanejamento {
    void salvar(Planejamento planejamento) throws PersistenciaException;
    Planejamento carregar() throws PersistenciaException;
}