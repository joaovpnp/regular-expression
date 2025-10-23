package automato;

import java.util.HashMap;
import java.util.Map;

public class Estado {

    private static final Map<String, Integer> contadorNomes = new HashMap<>();

    private final String nome;
    private boolean isFinal;

    private Estado(String nome, boolean isFinal) {
        this.nome = nome;
        this.isFinal = isFinal;
    }

    public static void tornarNaoFinal(Estado e) {
        e.isFinal = false;
    }

    public static void tornarFinal(Estado e) {
        e.isFinal = true;
    }

    public static Estado criar(boolean isFinal) {
        return criar("q", isFinal);
    }

    public static Estado criar(String prefixo, boolean isFinal) {
        int contador = contadorNomes.getOrDefault(prefixo, 0);
        String nomeUnico = prefixo + contador;
        contadorNomes.put(prefixo, contador + 1);
        return new Estado(nomeUnico, isFinal);
    }

    public String getNome() {
        return nome;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public boolean notIsFinal() {
        return !isFinal;
    }

    @Override
    public String toString() {
        return nome + "(%s)".formatted((isFinal) ? "final" : "não final");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Estado)) return false;
        return nome.equals(((Estado)o).nome);
    }

    @Override
    public int hashCode() { return nome.hashCode(); }
}
