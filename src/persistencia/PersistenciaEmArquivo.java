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

    private String caminhoDoArquivo; // onde o arquivo fica no disco (ex.: "planejamento.dat")

    public PersistenciaEmArquivo(String caminhoDoArquivo) {
        this.caminhoDoArquivo = caminhoDoArquivo;
    }

    // Grava o planejamento inteiro (com participantes e despesas) no arquivo
    @Override
    public void salvar(Planejamento planejamento) throws PersistenciaException {
        try (FileOutputStream arquivo = new FileOutputStream(caminhoDoArquivo);
             ObjectOutputStream saida = new ObjectOutputStream(arquivo)) {
            saida.writeObject(planejamento);
        } catch (IOException e) {
            throw new PersistenciaException("Não foi possível salvar o planejamento em " + caminhoDoArquivo + ".", e);
        }
    }

    // Carrega o planejamento a partir do arquivo
    @Override
    public Planejamento carregar() throws PersistenciaException {
        try (FileInputStream arquivo = new FileInputStream(caminhoDoArquivo);
             ObjectInputStream entrada = new ObjectInputStream(arquivo)) {
            return (Planejamento) entrada.readObject();
        }
        catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new PersistenciaException("Não foi possível carregar o planejamento de " + caminhoDoArquivo + ": arquivo ausente ou inválido.", e);
        }
    }
}