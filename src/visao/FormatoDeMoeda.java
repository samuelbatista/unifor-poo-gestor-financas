package visao;

// Imports
import java.math.BigDecimal;

/**
 * Tradutor: Converte o texto em reais ("70,00") para centavos (7000) e vice-versa.
 */
public final class FormatoDeMoeda {

    // Ninguém precisa criar um FormatoDeMoeda: todos os métodos são static
    private FormatoDeMoeda() { }

    // "70,00" -> 7000. Aceita "70", "70,5", "1.234,56" e negativos ("-5,00" -> -500)
    public static long paraCentavos(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new NumberFormatException("Digite um valor em reais, por exemplo 70,00.");
        }
        String limpo = texto.trim().replace(".", "").replace(",", "."); // "1.234,56" -> "1234.56"
        try {
            return new BigDecimal(limpo).movePointRight(2).longValueExact(); // 1234.56 -> 123456
        } catch (NumberFormatException | ArithmeticException e) {
            throw new NumberFormatException("Valor inválido: \"" + texto.trim()
                    + "\". Digite em reais, com no máximo 2 casas, por exemplo 70,00.");
        }
    }

    // 7000 -> "70,00" (para preencher o campo do formulário)
    public static String paraTexto(long centavos) {
        String sinal = centavos < 0 ? "-" : "";
        long positivo = Math.abs(centavos);
        return String.format("%s%d,%02d", sinal, positivo / 100, positivo % 100);
    }

    // 7000 -> "R$ 70,00" (para mostrar na tabela)
    public static String paraReais(long centavos) {
        return "R$ " + paraTexto(centavos);
    }
}