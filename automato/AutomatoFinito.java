package automato;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AutomatoFinito {

    public static final char PALAVRA_VAZIA = '&';

    protected Set<Estado> estados;
    protected Set<Character> alfabeto;
    protected Map<Estado, Map<Character, Set<Estado>>> transicoes;
    protected Estado estadoInicial;
    protected Set<Estado> estadosFinais;

    protected AutomatoFinito(Estado estadoInicial, Set<Estado> estadosFinais, Set<Estado> estados, Set<Character> alfabeto) {
        this.estadoInicial = estadoInicial;
        this.estadosFinais = estadosFinais;
        this.estados = estados;
        this.alfabeto = alfabeto;
        transicoes = new HashMap<>();
    }

    public void addTransicao(Map<Estado, Map<Character, Set<Estado>>> transicoes) {
        this.transicoes.putAll(transicoes);
    }

    public void addTransicao(Estado origem, Character c, Set<Estado> destino) {
        if (!transicoes.containsKey(origem))
            transicoes.put(origem, new HashMap<>());

        Map<Character, Set<Estado>> destinos = transicoes.get(origem);

        if (!destinos.containsKey(c))
            destinos.put(c, new HashSet<>(destino));
        else
            destinos.get(c).addAll(destino);
    }

    public void addTransicao(Estado origem, Character c, Estado destino) {
        addTransicao(origem, c, Set.of(destino));
    }

    protected Set<Estado> transicionar(Estado estado, char simbolo) {
        if (estado == null) return Set.of();

        Map<Character, Set<Estado>> mapaSimbolos = transicoes.get(estado);
        if (mapaSimbolos == null) return Set.of();

        return mapaSimbolos.getOrDefault(simbolo, Set.of());
    }

    protected Set<Estado> transicionar(Set<Estado> estados, char simbolo) {
        Set<Estado> conjuntoFinal = new HashSet<>();
        for (Estado e : estados)
            conjuntoFinal.addAll(transicionar(e, simbolo));
        return conjuntoFinal;
    }

    public Set<Estado> getEstados() {
        return estados;
    }

    public Set<Character> getAlfabeto() {
        return alfabeto;
    }

    public Map<Estado, Map<Character, Set<Estado>>> getTransicoes() {
        return transicoes;
    }

    public Estado getEstadoInicial() {
        return estadoInicial;
    }

    public Set<Estado> getEstadosFinais() {
        return estadosFinais;
    }

    @Override
    public String toString() {
        return """
                \nInicial: %s
                Finais: %s
                Transições: %s
                """.formatted(estadoInicial, estadosFinais, transicoes);
    }

    public abstract Set<Estado> programaEstendida(Set<Estado> inicial, String palavra);
    public abstract Set<Estado> programaEstendida(Estado inicial, String palavra);
    protected abstract Set<Estado> programaEstendida(Set<Estado> estados, String palavra, int i);
    public abstract boolean computarPalavra(String palavra);
}
