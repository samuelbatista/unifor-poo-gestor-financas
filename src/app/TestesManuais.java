package app; // Indica que esta classe faz parte do pacote do principal do app (Sem ser dentro do MVC)

// Importa outras Classes necessárias para a TestesManuais
import modelo.*;
import persistencia.PersistenciaEmArquivo;
import persistencia.PersistenciaException;
import persistencia.PersistenciaPlanejamento;

import java.util.List;

public class TestesManuais {
    public static void main(String[] args) {
        System.out.println("[=== Testes manuais do modelo ===]");

        try {

            Planejamento planejamento = montarCasoDeReferencia();
            imprimirResumo(planejamento);
            testarRecusas(planejamento);
            testarArquivo(planejamento);

        } catch (ValidacaoException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (PersistenciaException e) {
            System.out.println("Erro técnico ao salvar/carregar: " + e.getMessage());
        }
    }

    // Monta o caso de referência do enunciado: 3 participantes e 4 despesas
    static Planejamento montarCasoDeReferencia() throws ValidacaoException {
        Planejamento planejamento = new Planejamento();
        planejamento.adicionarParticipante(new Participante(1, "Ana", 7000));
        planejamento.adicionarParticipante(new Participante(2, "Bruno", 6000));
        planejamento.adicionarParticipante(new Participante(3, "Carla", 7000));

        Participante ana   = planejamento.buscarParticipante(1);
        Participante bruno = planejamento.buscarParticipante(2);
        Participante carla = planejamento.buscarParticipante(3);

        planejamento.adicionarDespesa(new DespesaFixa(planejamento.gerarIdDespesa(), "Jantar", 9000, List.of(ana, bruno, carla)));
        planejamento.adicionarDespesa(new DespesaFixa(planejamento.gerarIdDespesa(), "Mercado", 6000, List.of(ana, bruno, carla)));
        planejamento.adicionarDespesa(new DespesaFixa(planejamento.gerarIdDespesa(), "Táxi", 3000, List.of(ana, carla)));
        planejamento.adicionarDespesa(new DespesaPorPessoa(planejamento.gerarIdDespesa(), "Ingresso", 1000, List.of(ana, bruno, carla)));

        return planejamento;
    }

    // Mostra, para cada participante, quanto gasta, o orçamento e o excesso, e o custo total do grupo
    static void imprimirResumo(Planejamento planejamento) {
        System.out.println("=== Resumo ===");
        for (Participante p : planejamento.getParticipantes()) {
            System.out.println(p.getNome() + " - gasta: " + planejamento.calcularTotalDe(p)
                    + " - orçamento: " + p.getOrcamentoEmCentavos()
                    + " - excesso: " + planejamento.calcularExcessoDe(p));
        }
        System.out.println("Custo total do grupo: " + planejamento.calcularCustoTotal());
    }

    // Testes 1 a 11: cada entrada inválida deve ser recusada, e o retrato não pode mudar
    static void testarRecusas(Planejamento planejamento) {
        Participante ana   = planejamento.buscarParticipante(1);
        Participante bruno = planejamento.buscarParticipante(2);
        Participante carla = planejamento.buscarParticipante(3);
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
    }

    // Salva e carrega o planejamento; testes 12 e 13: carregar de novo não duplica, e arquivo inválido é recusado
    static void testarArquivo(Planejamento planejamento) throws PersistenciaException {

        // Persistência: salva o planejamento no arquivo e carrega de volta
        PersistenciaPlanejamento persistencia = new PersistenciaEmArquivo("planejamento.dat");
        persistencia.salvar(planejamento);
        Planejamento carregado = persistencia.carregar();
        System.out.println("Custo total do planejamento carregado: " + carregado.calcularCustoTotal());

        // ===== Testes de arquivo =====
        System.out.println("=== Testes de arquivo ===");

        System.out.println("12. Carregar o mesmo arquivo de novo (nada pode dobrar/duplicar):");
        Planejamento deNovo = persistencia.carregar();
        imprimirRetrato(deNovo);

        System.out.println("13. Carregar um arquivo de texto (docs/uml.md):");
        imprimirRetrato(carregado);
        try {
            carregado = new PersistenciaEmArquivo("docs/uml.md").carregar();
            System.out.println("   ERRO: aceitou o que devia recusar");
        } catch (PersistenciaException e) {
            System.out.println("   Recusou: " + e.getMessage());
        }
        imprimirRetrato(carregado);
    }

    // Imprime um "retrato" do planejamento, para comparar antes e depois de uma recusa
    static void imprimirRetrato(Planejamento planejamento) {
        System.out.println("   Retrato: " + planejamento.getParticipantes().size() + " participantes, "
                + planejamento.getDespesas().size() + " despesas, custo total "
                + planejamento.calcularCustoTotal());
    }
}