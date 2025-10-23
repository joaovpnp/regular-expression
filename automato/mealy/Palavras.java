package automato.mealy;

import leitura.LerArquivo;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public enum Palavras {

    W4(4),
    W8(8),
    W16(16),
    W32(32),
    W64(64),
    W128(128),
    W256(256),
    W512(512),
    W1024(1024);

    private final int ordemGrid;
    private final List<String> coordenadas;

    Palavras(int ordem) {
        ordemGrid = ordem;

        try {
            String palavra = LerArquivo.lerPalavraMealy(ordem);
            coordenadas = Arrays.stream(palavra.split("\\.")).toList();

        } catch (FileNotFoundException e) {
            System.err.println("[Não foi possível encontrar o arquivo para a palavra w%d]".formatted(ordem));
            throw new RuntimeException(e);
        }
    }

    public int getOrdemGrid() {
        return ordemGrid;
    }

    public List<String> getCoordenadas() {
        return coordenadas.stream().toList();
    }

}
