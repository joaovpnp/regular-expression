package automato;

import java.util.*;

public class AFE extends AutomatoFinito {

    public AFE(Estado inicial, Set<Estado> finais, Set<Estado> estados, Set<Character> alfabeto) {
        super(inicial, finais, estados, alfabeto);
    }

    public Set<Estado> computacaoVazia(Estado estado) {

        Set<Estado> fecho = new HashSet<>();
        Stack<Estado> pilha = new Stack<>();

        pilha.push(estado);
        fecho.add(estado);

        while (!pilha.isEmpty()) {
            Estado estadoAtual = pilha.pop();

            for (Estado destino : transicionar(estadoAtual, PALAVRA_VAZIA))
                if (fecho.add(destino))
                    pilha.push(destino);

        }

        return fecho;
    }

    public Set<Estado> computacaoVazia(Set<Estado> estados) {
        Set<Estado> conjunto = new HashSet<>();
        for (Estado e : estados)
            conjunto.addAll(computacaoVazia(e));

        return conjunto;
    }

    @Override
    public Set<Estado> programaEstendida(Estado inicial, String palavra) {
        return programaEstendida(Set.of(inicial), palavra, palavra.length()-1);
    }

    @Override
    public Set<Estado> programaEstendida(Set<Estado> inicial, String palavra) {
        return programaEstendida(inicial, palavra, palavra.length()-1);
    }

    @Override
    protected Set<Estado> programaEstendida(Set<Estado> estados, String palavra, int i) {
        if (i < 0) return computacaoVazia(estados);
        return computacaoVazia(transicionar(programaEstendida(estados, palavra, i-1), palavra.charAt(i)));
    }

    @Override
    public boolean computarPalavra(String palavra) {

        Set<Estado> conjuntoFinal = programaEstendida(Set.of(estadoInicial), palavra, palavra.length()-1);
        if (conjuntoFinal == null || conjuntoFinal.isEmpty()) return false;

        return conjuntoFinal.stream().anyMatch(Estado::isFinal);
    }
}
