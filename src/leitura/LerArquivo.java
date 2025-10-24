package leitura;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class LerArquivo {

    public static List<String> lerPalavras(String caminho) throws FileNotFoundException {

        List<String> palavras = new LinkedList<>();
        Scanner file = new Scanner(new File(caminho));

        while (file.hasNextLine())
            palavras.add(file.nextLine());

        file.close();
        return palavras;
    }

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
