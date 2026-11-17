package app; // Indica que esta classe faz parte do pacote do principal do app (Sem ser dentro do MVC)

import modelo.Participante; // Esta declaração vai importar a classe Participante do pacote do Model
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

        } catch (ValidacaoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}