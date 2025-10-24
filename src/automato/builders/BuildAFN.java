package automato.builders;

import automato.AFE;
import automato.AFN;
import automato.Estado;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BuildAFN extends AutomatoBuilder {

    public static AFN criar(AFE afe) {

        Set<Estado> novosEstados = new HashSet<>(afe.getEstados());
        Set<Estado> estadosNaoFinais = novosEstados.stream().filter(Estado::notIsFinal).collect(Collectors.toSet());

        estadosNaoFinais.forEach(e -> {
            Set<Estado> fechoVazio = afe.computacaoVazia(e);

            for (Estado fin : afe.getEstadosFinais())
                if (fechoVazio.contains(fin)) {
                    Estado.tornarFinal(e);
                    break;
                }
        });

        AFN afn = new AFN(afe.getEstadoInicial(), novosEstados.stream().filter(Estado::isFinal).collect(Collectors.toSet()), novosEstados, afe.getAlfabeto());

        for (Estado e : afn.getEstados())
            for (char c : afn.getAlfabeto())
                afn.addTransicao(e, c, afe.programaEstendida(e, String.valueOf(c)));

        return afn;
    }
}
