package modelo;

public class ValidacaoException extends Exception {

    // versão do formato salvo (exigida porque Exception é Serializable)
    private static final long serialVersionUID = 1L;

    public ValidacaoException(String mensagem) {
        super(mensagem); // repassa a mensagem para a classe mãe (Exception)
    }
}