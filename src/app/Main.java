package app; // Indica que esta classe faz parte do pacote do principal do app (Sem ser dentro do MVC)

// Importa outras Classes necessárias para a Main
import java.util.List;
import modelo.Participante;
import modelo.Despesa;
import modelo.DespesaFixa;
import modelo.DespesaPorPessoa;
import modelo.ValidacaoException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Olá, Cabe no bolso!");

        try {

            // Cria os 3 participantes (objetos) e guarda cada um numa variável
            Participante ana   = new Participante(1, "Ana", 7000);   // Adiciona a Ana com ID 1 e estipula o orçamento de R$ 70,00
            Participante bruno = new Participante(2, "Bruno", 6000); // Adiciona o Bruno com ID 2 e estipula o orçamento de R$ 60,00
            Participante carla = new Participante(3, "Carla", 7000); // Adiciona a Carla com ID 3 e estipula o orçamento de R$ 70,00

            // Imprime as informações de objeto de cada variável
            System.out.println(ana.getCodigo() + " - " + ana.getNome() + " - orçamento: " + ana.getOrcamentoEmCentavos() + " centavos");
            System.out.println(bruno.getCodigo() + " - " + bruno.getNome() + " - orçamento: " + bruno.getOrcamentoEmCentavos() + " centavos");
            System.out.println(carla.getCodigo() + " - " + carla.getNome() + " - orçamento: " + carla.getOrcamentoEmCentavos() + " centavos");

            // Despesa 1 do caso de referência: R$ 90 fixos entre os 3
            Despesa jantar = new DespesaFixa(1, "Jantar", 9000, List.of(ana, bruno, carla));
            System.out.println(jantar.getDescricao() + " - Ana paga: " + jantar.calcularValorDe(ana) + " - total: " + jantar.calcularTotal());

            Despesa mercado = new DespesaFixa(2, "Mercado", 6000, List.of(ana, bruno, carla));
            Despesa taxi    = new DespesaFixa(3, "Táxi", 3000, List.of(ana, carla));
            System.out.println(taxi.getDescricao() + " - Ana paga: " + taxi.calcularValorDe(ana) + " - Bruno paga: " + taxi.calcularValorDe(bruno));

            Despesa ingresso = new DespesaPorPessoa(4, "Ingresso", 1000, List.of(ana, bruno, carla));
            System.out.println(ingresso.getDescricao() + " - Bruno paga: " + ingresso.calcularValorDe(bruno) + " - total: " + ingresso.calcularTotal());

            // Total da Ana somando as 4 despesas (na Aula 5 o Planejamento fará isso sozinho)
            long totalAna = jantar.calcularValorDe(ana) + mercado.calcularValorDe(ana) + taxi.calcularValorDe(ana) + ingresso.calcularValorDe(ana);
            System.out.println("Total da Ana: " + totalAna + " - orçamento: " + ana.getOrcamentoEmCentavos());

        } catch (ValidacaoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}