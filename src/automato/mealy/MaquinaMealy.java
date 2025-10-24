package automato.mealy;

import automato.AFD;
import automato.Estado;
import leitura.LerArquivo;

import java.util.Map;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class MaquinaMealy {

    private final AFD afd;

    public MaquinaMealy(AFD afd) {
        this.afd = afd;
    }
    
    public void gerarPPM(Palavras palavra) throws IOException, InterruptedException {

        LerArquivo.escrever("../mealy-machine/arquivos/MM.txt", this.toString());

        ProcessBuilder pb = new ProcessBuilder(
                "make", "--no-print-directory", "MM=arquivos/MM.txt", "PALAVRA=arquivos/%s".formatted(palavra.getNomeArquivo())
        );

        pb.directory(new File("../mealy-machine"));
        pb.inheritIO();
        pb.start().waitFor();

        ProcessBuilder clean = new ProcessBuilder("make", "clean");
        clean.directory(new File("../mealy-machine"));
        clean.start().waitFor();

        try {
            Path origem = Paths.get("../mealy-machine/arquivos/fractal.ppm");
            Path destino = Paths.get("src/arquivos/fractal.ppm");

            Files.createDirectories(destino.getParent());
            Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            System.err.println("[Verifique a pasta arquivos em mealy-machine]");
        }
    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();
        String estadoRalo = "qr";

        for (Estado e : afd.getEstados())
            s.append(e.getNome()).append(' ');
        s.append(estadoRalo).append('\n');

        s.append(afd.getEstadoInicial().getNome()).append('\n');
        
        for (Estado e : afd.getEstadosFinais())
            s.append(e.getNome()).append(' ');
        if (afd.getEstadoInicial().notIsFinal())
            s.append(afd.getEstadoInicial().getNome());
        s.append('\n');

        for (char c : afd.getAlfabeto())
            s.append(c).append(' ');
        s.append(". N\n");
        
        s.append("0 1 \\n").append('\n');

        Map<Estado, Map<Character,Set<Estado>>> transicoes = afd.getTransicoes();
        for (Estado origem : transicoes.keySet()) {
            for (char c : afd.getAlfabeto()) {

                if (transicoes.get(origem).containsKey(c)) {
                    
                    Set<Estado> destinos = transicoes.get(origem).get(c);
                    for (Estado destino : destinos)
                        s.append("%s %c %s e\n".formatted(origem.getNome(), c, destino.getNome()));

                } else {
                    s.append("%s %c %s e\n".formatted(origem.getNome(), c, estadoRalo));
                }
            }
        }

        for (Estado e : afd.getEstadosFinais())
            s.append("%s . %s 1\n".formatted(e.getNome(), afd.getEstadoInicial().getNome()));
        
        for (char c : afd.getAlfabeto())
            s.append("qr %c qr e\n".formatted(c));
        s.append("qr . %s 0\n".formatted(afd.getEstadoInicial().getNome()));
        s.append("%s N %s \\n".formatted(afd.getEstadoInicial().getNome(), afd.getEstadoInicial().getNome()));

        return s.toString();
    }
    
}
