package automato.builders;

import automato.AFD;
import automato.AFE;
import automato.AFN;
import automato.Estado;
import exceptions.ExpressaoIncorretaException;
import exceptions.OperadorInvalidoException;
import exceptions.SemEstadoFinalException;

import java.util.*;
import java.util.stream.Collectors;

public class BuildAFD extends AutomatoBuilder {

    public static AFD criar(AFE afe) {
        return criar(BuildAFN.criar(afe));
    }

    public static AFD criar(AFN afn) {

        Map<Set<Estado>, Estado> geradoresParaEstado = new HashMap<>();
        Map<Estado, Set<Estado>> estadoParaGeradores = new HashMap<>();

        Queue<Estado> pendencias = new LinkedList<>();

        Map<Estado, Map<Character, Set<Estado>>> transicoes = new HashMap<>();

        Estado novoEstadoInicial = Estado.criar(afn.getEstadoInicial().isFinal());

        geradoresParaEstado.put(Set.of(afn.getEstadoInicial()), novoEstadoInicial);
        estadoParaGeradores.put(novoEstadoInicial, Set.of(afn.getEstadoInicial()));

        pendencias.add(novoEstadoInicial);

        while (!pendencias.isEmpty()) {

            Estado origem = pendencias.poll();

            for (char c : afn.getAlfabeto()) {

                Set<Estado> estadosAtingidos = afn.programaEstendida(estadoParaGeradores.get(origem), String.valueOf(c));

                if (!estadosAtingidos.isEmpty()) {

                    Estado destino;

                    if (geradoresParaEstado.containsKey(estadosAtingidos)) {
                        destino = geradoresParaEstado.get(estadosAtingidos);
                    } else {
                        destino = Estado.criar(estadosAtingidos.stream().anyMatch(Estado::isFinal));

                        geradoresParaEstado.put(estadosAtingidos, destino);
                        estadoParaGeradores.put(destino, estadosAtingidos);

                        pendencias.add(destino);
                    }

                    if (!transicoes.containsKey(origem)) {
                        transicoes.put(origem, new HashMap<>());
                    }
                    transicoes.get(origem).put(c, Set.of(destino));
                }
            }
        }

        Set<Estado> novosEstados = new HashSet<>(geradoresParaEstado.values());

        AFD afd = new AFD(novoEstadoInicial, novosEstados.stream().filter(Estado::isFinal).collect(Collectors.toSet()), novosEstados, afn.getAlfabeto());
        afd.addTransicao(transicoes);

        return afd;
    }

    public static AFD criar(String er) throws ExpressaoIncorretaException, SemEstadoFinalException, OperadorInvalidoException {
        return criar(BuildAFE.criar(er));
    }
}
