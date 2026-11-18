package persistencia;

/**
 * Exceção do domínio para falhas ao salvar ou carregar o planejamento.
 * Guarda uma mensagem clara (para o usuário) e a causa técnica original (para quem for investigar).
 */
public class PersistenciaException extends Exception {

    private static final long serialVersionUID = 1L;

    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa); // repassa a mensagem e a causa para a mãe (Exception)
    }
}