package app; // Indica que esta classe faz parte do pacote do principal do app (Sem ser dentro do MVC)

// Importa outras Classes necessárias para a Main
import java.util.List;
import modelo.Participante;
import modelo.Despesa;
import modelo.DespesaFixa;
import modelo.DespesaPorPessoa;
import modelo.ValidacaoException;
import modelo.Planejamento;
import java.io.IOException;
import persistencia.PersistenciaPlanejamento;
import persistencia.PersistenciaEmArquivo;

public class Main {
    public static void main(String[] args) {
        System.out.println("Olá, Cabe no bolso!");

        try {

            // Cria os 3 participantes (objetos) e guarda cada um numa variável
            Participante ana   = new Participante(1, "Ana", 7000);   // Adiciona a Ana com ID 1 e estipula o orçamento de R$ 70,00
            Participante bruno = new Participante(2, "Bruno", 6000); // Adiciona o Bruno com ID 2 e estipula o orçamento de R$ 60,00
            Participante carla = new Participante(3, "Carla", 7000); // Adiciona a Carla com ID 3 e estipula o orçamento de R$ 70,00

            // Cria o planejamento (o fichário) e guarda os 3 participantes nele
            Planejamento planejamento = new Planejamento();
            planejamento.adicionarParticipante(ana);
            planejamento.adicionarParticipante(bruno);
            planejamento.adicionarParticipante(carla);

            // Cria as 4 despesas do caso de referência.
            Despesa jantar   = new DespesaFixa(planejamento.gerarIdDespesa(), "Jantar", 9000, List.of(ana, bruno, carla));
            Despesa mercado  = new DespesaFixa(planejamento.gerarIdDespesa(), "Mercado", 6000, List.of(ana, bruno, carla));
            Despesa taxi     = new DespesaFixa(planejamento.gerarIdDespesa(), "Táxi", 3000, List.of(ana, carla));
            Despesa ingresso = new DespesaPorPessoa(planejamento.gerarIdDespesa(), "Ingresso", 1000, List.of(ana, bruno, carla));

            // Guarda as 4 despesas no planejamento
            planejamento.adicionarDespesa(jantar);
            planejamento.adicionarDespesa(mercado);
            planejamento.adicionarDespesa(taxi);
            planejamento.adicionarDespesa(ingresso);

            // Resumo: para cada participante do planejamento, mostra gasto, orçamento e excesso
            System.out.println("=== Resumo ===");
            for (Participante p : planejamento.getParticipantes()) {
                System.out.println(p.getNome() + " - gasta: " + planejamento.calcularTotalDe(p)
                        + " - orçamento: " + p.getOrcamentoEmCentavos()
                        + " - excesso: " + planejamento.calcularExcessoDe(p));
            }
            System.out.println("Custo total do grupo: " + planejamento.calcularCustoTotal());

            // Persistência: salva o planejamento no arquivo e carrega de volta
            PersistenciaPlanejamento persistencia = new PersistenciaEmArquivo("planejamento.dat");
            persistencia.salvar(planejamento);
            Planejamento carregado = persistencia.carregar();
            System.out.println("Custo total do planejamento carregado: " + carregado.calcularCustoTotal());

            // ===== Testes de recusa: cada um deve recusar, e o retrato não pode mudar =====
            System.out.println("=== Testes de recusa ===");

            System.out.println("1. Código de participante repetido:");
            imprimirRetrato(planejamento);
            try {
                planejamento.adicionarParticipante(new Participante(1, "Davi", 5000));
                System.out.println("   ERRO: aceitou o que devia recusar");
            } catch (ValidacaoException e) {
                System.out.println("   Recusou: " + e.getMessage());
            }
            imprimirRetrato(planejamento);

            System.out.println("2. Nome só com espaços:");
            imprimirRetrato(planejamento);
            try {
                planejamento.adicionarParticipante(new Participante(4, "    ", 5000));
                System.out.println("   ERRO: aceitou o que devia recusar");
            } catch (ValidacaoException e) {
                System.out.println("   Recusou: " + e.getMessage());
            }
            imprimirRetrato(planejamento);

            System.out.println("3. Orçamento negativo:");
            imprimirRetrato(planejamento);
            try {
                planejamento.adicionarParticipante(new Participante(4, "Davi", -100));
                System.out.println("   ERRO: aceitou o que devia recusar");
            } catch (ValidacaoException e) {
                System.out.println("   Recusou: " + e.getMessage());
            }
            imprimirRetrato(planejamento);

            System.out.println("4. Alterar orçamento para negativo:");
            imprimirRetrato(planejamento);
            try {
                ana.setOrcamentoEmCentavos(-100);
                System.out.println("   ERRO: aceitou o que devia recusar");
            } catch (ValidacaoException e) {
                System.out.println("   Recusou: " + e.getMessage());
            }
            imprimirRetrato(planejamento);

            System.out.println("5. Despesa com descrição vazia:");
            imprimirRetrato(planejamento);
            try {
                planejamento.adicionarDespesa(new DespesaFixa(5, "  ", 1000, List.of(ana)));
                System.out.println("   ERRO: aceitou o que devia recusar");
            } catch (ValidacaoException e) {
                System.out.println("   Recusou: " + e.getMessage());
            }
            imprimirRetrato(planejamento);

            System.out.println("6. Despesa com valor zero:");
            imprimirRetrato(planejamento);
            try {
                planejamento.adicionarDespesa(new DespesaFixa(5, "Lanche", 0, List.of(ana)));
                System.out.println("   ERRO: aceitou o que devia recusar");
            } catch (ValidacaoException e) {
                System.out.println("   Recusou: " + e.getMessage());
            }
            imprimirRetrato(planejamento);

            System.out.println("7. Despesa sem participantes:");
            imprimirRetrato(planejamento);
            try {
                planejamento.adicionarDespesa(new DespesaFixa(5, "Lanche", 1000, List.of()));
                System.out.println("   ERRO: aceitou o que devia recusar");
            } catch (ValidacaoException e) {
                System.out.println("   Recusou: " + e.getMessage());
            }
            imprimirRetrato(planejamento);

            System.out.println("8. Participante repetido na despesa:");
            imprimirRetrato(planejamento);
            try {
                planejamento.adicionarDespesa(new DespesaFixa(5, "Lanche", 1000, List.of(ana, ana)));
                System.out.println("   ERRO: aceitou o que devia recusar");
            } catch (ValidacaoException e) {
                System.out.println("   Recusou: " + e.getMessage());
            }
            imprimirRetrato(planejamento);

            System.out.println("9. Participante não cadastrado:");
            imprimirRetrato(planejamento);
            try {
                planejamento.adicionarDespesa(new DespesaFixa(5, "Lanche", 1000, List.of(new Participante(9, "Davi", 5000))));
                System.out.println("   ERRO: aceitou o que devia recusar");
            } catch (ValidacaoException e) {
                System.out.println("   Recusou: " + e.getMessage());
            }
            imprimirRetrato(planejamento);

            System.out.println("10. Excluir despesa inexistente:");
            imprimirRetrato(planejamento);
            try {
                planejamento.removerDespesa(99);
                System.out.println("   ERRO: aceitou o que devia recusar");
            } catch (ValidacaoException e) {
                System.out.println("   Recusou: " + e.getMessage());
            }
            imprimirRetrato(planejamento);

            System.out.println("11. Editar despesa trocando o id:");
            imprimirRetrato(planejamento);
            try {
                planejamento.editarDespesa(1, new DespesaFixa(7, "Jantar", 9000, List.of(ana, bruno, carla)));
                System.out.println("   ERRO: aceitou o que devia recusar");
            } catch (ValidacaoException e) {
                System.out.println("   Recusou: " + e.getMessage());
            }
            imprimirRetrato(planejamento);

        } catch (ValidacaoException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro técnico ao salvar/carregar: " + e.getMessage());
        }
    }

    // Imprime um "retrato" do planejamento, para comparar antes e depois de uma recusa
    static void imprimirRetrato(Planejamento planejamento) {
        System.out.println("   Retrato: " + planejamento.getParticipantes().size() + " participantes, "
                + planejamento.getDespesas().size() + " despesas, custo total "
                + planejamento.calcularCustoTotal());
    }
}