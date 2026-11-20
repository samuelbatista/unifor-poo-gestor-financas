package persistencia;

// Importa as classes necessárias para a interface
import modelo.Planejamento;

// Contrato: toda forma de persistência precisa saber salvar e carregar um Planejamento
// O caminho vem a cada chamada (é o usuário quem escolhe o arquivo na tela)
public interface PersistenciaPlanejamento {
    void salvar(Planejamento planejamento, String caminho) throws PersistenciaException;
    Planejamento carregar(String caminho) throws PersistenciaException;
}