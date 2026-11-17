package modelo;

public class ValidacaoException extends Exception {
    public ValidacaoException(String mensagem) {
        super(mensagem); // repassa a mensagem para a classe mãe (Exception)
    }
}
