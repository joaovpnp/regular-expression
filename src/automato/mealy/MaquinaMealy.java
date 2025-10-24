package automato.mealy;

import automato.AFD;

public class MaquinaMealy {

    private final AFD afd;

    public MaquinaMealy(AFD afd) {
        this.afd = afd;
    }
    
    public String gerarPPM(Palavras palavra) {

        int ordem = palavra.getOrdemGrid();

        StringBuilder saida = new StringBuilder();
        saida.append("P1\n");
        saida.append("%d %d\n".formatted(ordem, ordem));

        int cont = 1;
        for (String coordenada : palavra.getCoordenadas()) {
            if (afd.computarPalavra(coordenada))
                saida.append(1);
            else
                saida.append(0);

            if (cont == ordem) {
                saida.append('\n');
                cont = 1;
            } else {
                cont++;
            }
        }

        return saida.toString();
    }
    
}
