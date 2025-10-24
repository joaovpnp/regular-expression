package leitura;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class LerArquivo {

    public static String lerPalavraMealy(int ordem) throws FileNotFoundException {
        Scanner file = new Scanner(new File("src/arquivos/palavrasMealy/w" + ordem + ".txt"));
        String palavra = file.nextLine();
        file.close();
        return palavra;
    }

    public static void escrever(String caminho, String conteudo) throws IOException {
        Files.writeString(Paths.get(caminho), conteudo);
    }
}
