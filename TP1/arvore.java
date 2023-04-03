import java.io.RandomAccessFile;

public class arvore {
    private int Orderm = 8;

    private int  maxElementos;          // Variável igual a ordem - 1 para facilitar a clareza do código
    private int  maxFilhos;             // Variável igual a ordem para facilitar a clareza do código
    private RandomAccessFile arquivo;   // Arquivo em que a árvore será armazenada
    private String nomeArquivo;
    
    // Variáveis usadas nas funções recursivas (já que não é possível passar valores por referência)
    private String  chaveAux;
    private int     dadoAux;
    private long    paginaAux;
    private boolean cresceu;
    private boolean diminuiu;


    public void create()
    {

    }

    public void uptate(int IdUpdate)
    {

    }

    public boolean delete(int IdDelet)
    {


    return false;
    }

    public Netflix read(int IdRead)
    {
        return null;

    }


    public void LerRegistroEfazerArvore()
    {

    }
    
}
