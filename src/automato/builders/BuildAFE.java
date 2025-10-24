package automato.builders;

import automato.AFE;
import automato.AutomatoFinito;
import automato.Estado;
import exceptions.ExpressaoIncorretaException;
import exceptions.OperadorInvalidoException;
import exceptions.SemEstadoFinalException;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class BuildAFE extends AutomatoBuilder {

    public static AFE criar(char r) {

        AFE automato;
        Estado estadoInicial;
        Set<Estado> estados = new HashSet<>();
        Set<Character> alfabeto = new HashSet<>();

        if (r != PALAVRA_VAZIA_ER) {
            estadoInicial = Estado.criar(false);
            estados.add(estadoInicial);
            Estado estadoFinal = Estado.criar(true);
            estados.add(estadoFinal);
            alfabeto.add(r);

            automato = new AFE(estadoInicial, Set.of(estadoFinal), estados, alfabeto);
            automato.addTransicao(estadoInicial, r, estadoFinal);

        } else {
            estadoInicial = Estado.criar(true);
            estados.add(estadoInicial);
            automato = new AFE(estadoInicial, Set.of(estadoInicial), estados, alfabeto);
        }

        return automato;
    }

    public static AFE criar(AFE r) throws SemEstadoFinalException {

        Set<Estado> estadosNovos = new HashSet<>();

        Estado novoEstadoInicial = Estado.criar(false);
        Estado novoEstadoFinal = Estado.criar(true);

        estadosNovos.add(novoEstadoInicial);
        estadosNovos.addAll(r.getEstados());
        estadosNovos.add(novoEstadoFinal);

        AFE afe = new AFE(novoEstadoInicial, Set.of(novoEstadoFinal), estadosNovos, r.getAlfabeto());

        if (r.getEstadosFinais() == null) throw new SemEstadoFinalException();

        for (Estado fin : r.getEstadosFinais())
            Estado.tornarNaoFinal(fin);

        afe.addTransicao(r.getTransicoes());

        afe.addTransicao(novoEstadoInicial, AutomatoFinito.PALAVRA_VAZIA, novoEstadoFinal);
        afe.addTransicao(novoEstadoInicial, AutomatoFinito.PALAVRA_VAZIA, r.getEstadoInicial());

        for (Estado fin : r.getEstadosFinais()) {
            afe.addTransicao(fin, AutomatoFinito.PALAVRA_VAZIA, r.getEstadoInicial());
            afe.addTransicao(fin, AutomatoFinito.PALAVRA_VAZIA, novoEstadoFinal);
        }

        return afe;
    }

    private static AFE uniao(AFE r, AFE s) throws SemEstadoFinalException {

        Estado novoEstadoInicial = Estado.criar(false);
        Estado novoEstadoFinal = Estado.criar(true);

        if (r.getEstadosFinais() == null || s.getEstadosFinais() == null) throw new SemEstadoFinalException();

        for (Estado fin : r.getEstadosFinais())
            Estado.tornarNaoFinal(fin);

        for (Estado fin : s.getEstadosFinais())
            Estado.tornarNaoFinal(fin);

        Set<Estado> novosEstados = new HashSet<>();
        novosEstados.add(novoEstadoInicial);
        novosEstados.addAll(r.getEstados());
        novosEstados.addAll(s.getEstados());
        novosEstados.add(novoEstadoFinal);

        Set<Character> novoAlfabeto = new HashSet<>();
        novoAlfabeto.addAll(r.getAlfabeto());
        novoAlfabeto.addAll(s.getAlfabeto());

        AFE afe = new AFE(novoEstadoInicial, Set.of(novoEstadoFinal), novosEstados, novoAlfabeto);

        afe.addTransicao(r.getTransicoes());
        afe.addTransicao(s.getTransicoes());
        afe.addTransicao(novoEstadoInicial, AutomatoFinito.PALAVRA_VAZIA, r.getEstadoInicial());
        afe.addTransicao(novoEstadoInicial, AutomatoFinito.PALAVRA_VAZIA, s.getEstadoInicial());

        for (Estado fin : r.getEstadosFinais())
            afe.addTransicao(fin, AutomatoFinito.PALAVRA_VAZIA, novoEstadoFinal);

        for (Estado fin : s.getEstadosFinais())
            afe.addTransicao(fin, AutomatoFinito.PALAVRA_VAZIA, novoEstadoFinal);

        return afe;
    }

    private static AFE conc(AFE r, AFE s) throws SemEstadoFinalException {

        if (r.getEstadosFinais() == null || s.getEstadosFinais() == null) throw new SemEstadoFinalException();

        for (Estado fin : r.getEstadosFinais())
            Estado.tornarNaoFinal(fin);

        Set<Estado> novosEstados = new HashSet<>();
        novosEstados.addAll(r.getEstados());
        novosEstados.addAll(s.getEstados());

        Set<Character> novoAlfabeto = new HashSet<>();
        novoAlfabeto.addAll(r.getAlfabeto());
        novoAlfabeto.addAll(s.getAlfabeto());

        AFE afe = new AFE(r.getEstadoInicial(), s.getEstadosFinais(), novosEstados, novoAlfabeto);

        afe.addTransicao(r.getTransicoes());
        afe.addTransicao(s.getTransicoes());

        for (Estado fin : r.getEstadosFinais())
            afe.addTransicao(fin, AutomatoFinito.PALAVRA_VAZIA, s.getEstadoInicial());

        return afe;
    }

    public static AFE criar(AFE r, AFE s, char operacao) throws SemEstadoFinalException, OperadorInvalidoException {
        if (operacao == UNIAO)
            return uniao(r, s);
        else if (operacao == CONC)
            return conc(r, s);
        else
            throw new OperadorInvalidoException();
    }

    public static AFE criar(String er) throws ExpressaoIncorretaException, SemEstadoFinalException, OperadorInvalidoException {

        String erFormatada = FormatadorER.inserirConcatenacaoExplicita(er);

        if (!isERsintaticamenteCorreta(erFormatada))
            throw new ExpressaoIncorretaException();

        String erPosfixa = FormatadorER.infixaParaSufixa(erFormatada);

        Stack<AFE> pilhaAutomatos = new Stack<>();

        for (char c : erPosfixa.toCharArray()) {

            if (c == UNIAO || c == CONC) {
                AFE s = pilhaAutomatos.pop();
                AFE r = pilhaAutomatos.pop();
                pilhaAutomatos.push(criar(r, s, c));

            } else if (c == CONC_SUC) {
                pilhaAutomatos.push(criar(pilhaAutomatos.pop()));

            } else {
                pilhaAutomatos.push(criar(c));
            }
        }

        return pilhaAutomatos.pop();
    }

}
