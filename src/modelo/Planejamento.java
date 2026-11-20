package modelo;

// Importa as classes necessárias para o Planejamento
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Planejamento implements Serializable {

    // versão do formato salvo (muda só se a classe mudar de forma incompatível)
    private static final long serialVersionUID = 1L;

    // Atributos
    private List<Participante> participantes = new ArrayList<>();
    private List<Despesa> despesas = new ArrayList<>();
    private int ultimoIdDespesa = 0; // último id de despesa entregue (0 = nenhum ainda)
    private boolean marcadoParaRevisao = false; // registro de que o planejamento precisa ser revisto

    // CRUD Participante: Create (guarda o participante na lista)
    public void adicionarParticipante(Participante participante) throws ValidacaoException {

        for (Participante p : participantes) {
            if (p.getCodigo() == participante.getCodigo()) {
                throw new ValidacaoException("Já existe um participante com o código " + participante.getCodigo() + ".");
            }
        }
        participantes.add(participante);
    }

    // CRUD Participante: Read (procura pelo código; devolve null se não achar)
    public Participante buscarParticipante(int codigo) { // Não tem throws pois não há validação (Ou existe usuário com o código buscado ou não existe)

        for (Participante p : participantes) {
            if (p.getCodigo() == codigo) {
                return p;
            }
        }
        return null; // Não achou participante com o código informado
    }

    // CRUD Participante: Update (altera nome e orçamento juntos; recusa se o código não existir)
    public void alterarParticipante(int codigo, String novoNome, long novoOrcamentoEmCentavos) throws ValidacaoException {
        Participante encontrado = buscarParticipante(codigo); // reaproveita a busca
        if (encontrado == null) {
            throw new ValidacaoException("Não existe participante com o código " + codigo + ".");
        }
        encontrado.alterar(novoNome, novoOrcamentoEmCentavos); // o participante confere tudo antes de mudar
    }

    // Getters
    public List<Participante> getParticipantes() { return participantes; }
    public List<Despesa> getDespesas() { return despesas; }

    // Gera um id novo para despesa: soma 1 ao último entregue e devolve
    public int gerarIdDespesa() {
        ultimoIdDespesa++;
        return ultimoIdDespesa;
    }

    // CRUD Despesa: Create (guarda a despesa na lista)
    public void adicionarDespesa(Despesa despesa) throws ValidacaoException {

        for (Despesa d : despesas) {
            if (d.getId() == despesa.getId()) {
                throw new ValidacaoException("Já existe uma despesa com o id " + despesa.getId() + ".");
            }
        }
        conferirParticipantesCadastrados(despesa);
        despesas.add(despesa);
    }

    // CRUD Despesa: Read (buscar pelo id; devolve null se não achar)
    public Despesa buscarDespesa(int id) { // Não tem throws pois não há validação (Ou existe despesa com o id buscado ou não existe)

        for (Despesa d : despesas) {
            if (d.getId() == id) {
                return d;
            }
        }
        return null; // Não achou despesa com o código informado
    }

    // CRUD Despesa: Delete (remove pelo id; recusa se a despesa não existir)
    public void removerDespesa(int id) throws ValidacaoException {
        Despesa encontrada = buscarDespesa(id); // reaproveita a busca
        if (encontrada == null) {
            throw new ValidacaoException("Não existe despesa com o id " + id + ".");
        }
        despesas.remove(encontrada); // Remove a despesa
    }

    // CRUD Despesa: Update (troca a despesa antiga pela nova, mantendo o mesmo id)
    public void editarDespesa(int id, Despesa novaDespesa) throws ValidacaoException {
        Despesa antiga = buscarDespesa(id);
        if (antiga == null) {
            throw new ValidacaoException("Não existe despesa com o id " + id + ".");
        }
        if (novaDespesa.getId() != id) {
            throw new ValidacaoException("A despesa editada precisa manter o id " + id + ".");
        }

        conferirParticipantesCadastrados(novaDespesa);

        int posicao = despesas.indexOf(antiga);  // em que posição da fila está a antiga
        despesas.set(posicao, novaDespesa);      // coloca a nova no mesmo lugar
    }

    // Calcular o total de um participante
    public long calcularTotalDe(Participante participante) {
        long total = 0;                                       // 1. Começa com a conta zerada
        for (Despesa d : despesas) {                          // 2. Para cada despesa (d)
            total = total + d.calcularValorDe(participante);  //    é somado ao total o valor que ele paga (Perceba o polimorfismo)
        }
        return total;                                         // 3. Devolve o valor total do participante
    }

    // Calcular o total do grupo
    public long calcularCustoTotal() {
        long total = 0;                            // 1. Começa com a conta zerada
        for (Despesa d : despesas) {               // 2. Para cada despesa (d)
            total = total + d.calcularTotal();     //    é somado ao total o valor que ela gera (Perceba o polimorfismo)
        }
        return total;                              // 3. Devolve o valor total das despesas
    }

    // Calcular o excedente de orçamento de participante
    public long calcularExcessoDe(Participante participante) {
        long excesso = calcularTotalDe(participante) - participante.getOrcamentoEmCentavos(); // 1. Começa subtraíndo o valor do orçamento do total gasto
        if (excesso > 0) {                                                                    // 2. Se passou do orçamento, devolve quanto passou;
            return excesso;                                                                   //
        }
        return 0;                                                                             // 3. Não passou: excesso zero
    }

    // Está dentro do orçamento quando não há excesso (gastar exatamente o orçamento conta como dentro)
    public boolean estaDentroDoOrcamento(Participante participante) {
        return calcularExcessoDe(participante) == 0;
    }

    // Troca todo o conteúdo deste planejamento pelo de outro (usado ao abrir um arquivo).
    // Este objeto continua o mesmo: quem já o segura (os controladores) passa a ver os dados novos.
    public void substituirConteudoPor(Planejamento outro) {
        participantes.clear();                        // 1. esvazia (abrir duas vezes não duplica)
        despesas.clear();
        participantes.addAll(outro.participantes);    // 2. copia o que veio do arquivo
        despesas.addAll(outro.despesas);
        ultimoIdDespesa = outro.ultimoIdDespesa;      // 3. ids novos continuam de onde o arquivo parou
        marcadoParaRevisao = outro.marcadoParaRevisao;
    }

    // Verdadeiro se pelo menos um participante passou do orçamento
    public boolean existeParticipanteAcimaDoOrcamento() {
        for (Participante p : participantes) {
            if (!estaDentroDoOrcamento(p)) {
                return true;
            }
        }
        return false;
    }

    // Revisão: registra o planejamento para revisão (só faz sentido se alguém excede)
    public void marcarParaRevisao() throws ValidacaoException {
        if (!existeParticipanteAcimaDoOrcamento()) {
            throw new ValidacaoException("Ninguém está acima do orçamento: não há o que revisar.");
        }
        marcadoParaRevisao = true;
    }

    // Revisão: retira o registro (por exemplo, depois que a equipe revisou)
    public void desmarcarRevisao() {
        marcadoParaRevisao = false;
    }

    public boolean isMarcadoParaRevisao() { return marcadoParaRevisao; }

    // Ajudante: recusa a despesa se algum participante dela não estiver cadastrado neste planejamento
    private void conferirParticipantesCadastrados(Despesa despesa) throws ValidacaoException {
        for (Participante p : despesa.getParticipantes()) {
            if (!participantes.contains(p)) {
                throw new ValidacaoException("O participante " + p.getNome() + " (código " + p.getCodigo() + ") não está cadastrado no planejamento.");
            }
        }
    }

}
