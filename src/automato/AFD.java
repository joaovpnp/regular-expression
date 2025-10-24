package automato;

import java.util.Set;

public class AFD extends AutomatoFinito {

    public AFD(Estado inicial, Set<Estado> finais, Set<Estado> estados, Set<Character> alfabeto) {
        super(inicial, finais, estados, alfabeto);
    }

    @Override
    public Set<Estado> programaEstendida(Estado inicial, String palavra) {
        return programaEstendida(Set.of(inicial), palavra, 0);
    }

    @Override
    public Set<Estado> programaEstendida(Set<Estado> inicial, String palavra) {
        return programaEstendida(inicial, palavra, 0);
    }

    @Override
    protected Set<Estado> programaEstendida(Set<Estado> estados, String palavra, int i) {
        if (i == palavra.length()) return estados;
        return programaEstendida(transicionar(estados, palavra.charAt(i)), palavra, i+1);
    }

    @Override
    public boolean computarPalavra(String palavra) {

        Set<Estado> conjuntoFinal = programaEstendida(Set.of(estadoInicial), palavra, 0);
        if (conjuntoFinal == null || conjuntoFinal.isEmpty()) return false;

        return conjuntoFinal.stream().anyMatch(Estado::isFinal);
    }
}
