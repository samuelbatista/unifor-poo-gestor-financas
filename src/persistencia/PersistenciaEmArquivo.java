package persistencia;

// Importa as classes necessárias para a Persistencia em Arquivo
import java.io.IOException;
import modelo.Planejamento;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

// Persistência que grava o Planejamento num arquivo, usando serialização
public class PersistenciaEmArquivo implements PersistenciaPlanejamento {

    // Grava o planejamento inteiro (com participantes e despesas) no arquivo indicado
    @Override
    public void salvar(Planejamento planejamento, String caminho) throws PersistenciaException {
        try (FileOutputStream arquivo = new FileOutputStream(caminho);
             ObjectOutputStream saida = new ObjectOutputStream(arquivo)) {
            saida.writeObject(planejamento);
        } catch (IOException e) {
            throw new PersistenciaException("Não foi possível salvar o planejamento em " + caminho + ".", e);
        }
    }

    // Carrega o planejamento a partir do arquivo indicado
    @Override
    public Planejamento carregar(String caminho) throws PersistenciaException {
        try (FileInputStream arquivo = new FileInputStream(caminho);
             ObjectInputStream entrada = new ObjectInputStream(arquivo)) {
            return (Planejamento) entrada.readObject();
        }
        catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new PersistenciaException("Não foi possível carregar o planejamento de " + caminho + ": arquivo ausente ou inválido.", e);
        }
    }
}