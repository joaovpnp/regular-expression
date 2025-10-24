package automato.mealy;

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

    private final String nomeArquivo;

    Palavras(int ordem) {
        this.nomeArquivo = "w%d.txt".formatted(ordem);
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }
}
