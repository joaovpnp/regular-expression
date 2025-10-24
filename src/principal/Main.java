package principal;

import automato.AFD;
import automato.AutomatoFinito;
import automato.mealy.MaquinaMealy;
import automato.builders.BuildAFD;
import automato.mealy.Palavras;
import exceptions.ExpressaoIncorretaException;
import exceptions.OperadorInvalidoException;
import exceptions.SemEstadoFinalException;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static Scanner input = new Scanner(System.in);

    public static void aceitacaoPalavras(AutomatoFinito automato, String er) {

        if (automato == null) {
            System.out.println("\n[Defina uma expressão regular]\n");
            return;
        }

        String palavra;
        do {

            System.out.println("\n[Digite a palavra. Caso queira sair, digite \\]\n");
            System.out.print("> ");
            palavra = input.nextLine();

            if (palavra.equals("\\")) break;

            if (automato.computarPalavra(palavra))
                System.out.println("\nA palavra foi aceita. [ER = %s]".formatted(er));
            else
                System.out.println("\nA palavra foi rejeitada. [ER = %s]".formatted(er));

        } while (true);

    }

    public static String definirExpressaoRegular() {

        System.out.println("\n[Digite a Expressão]\t{Obs.: Palavra vazia é denotada pelo símbolo $}\n");
        System.out.print("> ");
        return input.nextLine();
    }

    public static AFD definirAutomato(String er) {
        try {
            AFD afd = BuildAFD.criar(er);
            return afd;
        } catch (ExpressaoIncorretaException | SemEstadoFinalException | OperadorInvalidoException e) {
            System.out.println();
            System.err.println(e.getMessage());
            System.out.println();
            return null;
        }
    }

    public static void criarMealy(AFD afd) {

        if (afd == null) return;

        MaquinaMealy mealy = new MaquinaMealy(afd);
        boolean escolhida = false;
        int opc = 1;

        do {

            System.out.println("\n[Escolha a palavra de entrada]\n");
            int cont = 1;
            for (Palavras p : Palavras.values()) {
                System.out.println("\t" + cont + " - " + p);
                cont++;
            }
            System.out.print("\n> ");
            opc = Integer.parseInt(input.nextLine());

            if (opc < 1 || opc > Palavras.values().length) {
                System.out.println("\n[Opção inválida]\n");

            } else {
                escolhida = true;
            }

        } while (!escolhida);

        try {
            mealy.gerarPPM(Palavras.values()[opc - 1]);
        } catch (IOException e) {
            System.err.println("[Erro ao gerar o arquivo da Máquina de Mealy]");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.err.println("[Processo de criação do PPM foi interrompido]");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        String menu = """
                \nMenu
                
                \t1 - Definir Expressão Regular
                \t2 - Verificar Aceitação de Palavras
                \t3 - Testar Máquina de Mealy (Específica p/ o Trabalho)
                \t0 - Sair""";

        AFD automato = null;
        String er = null;
        int opc;

        do {

            System.out.println(menu);
            System.out.print("\n> ");
            opc = Integer.parseInt(input.nextLine());

            switch (opc) {
                case 0:
                    break;
                case 1:
                    er = definirExpressaoRegular();
                    automato = definirAutomato(er);
                    break;
                case 2:
                    aceitacaoPalavras(automato, er);
                    break;
                case 3:
                    er = definirExpressaoRegular();
                    automato = definirAutomato(er);
                    criarMealy(automato);
                    automato = null;
                    break;
                default:
                    System.out.println("\n[Opção inválida]\n");
                    break;
            }

        } while (opc != 0);

        input.close();
    }
}
