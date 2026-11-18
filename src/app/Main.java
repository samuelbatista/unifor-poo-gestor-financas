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

        } catch (ValidacaoException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro técnico ao salvar/carregar: " + e.getMessage());
        }
    }
}