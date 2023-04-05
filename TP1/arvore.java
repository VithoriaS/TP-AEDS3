import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.text.Position;

public class arvore {
    private int Ordem;

    private int  maxElementos;          // Variável igual a ordem - 1 para facilitar a clareza do código
    private int  maxFilhos;             // Variável igual a ordem para facilitar a clareza do código
    private RandomAccessFile arquivo;   // Arquivo em que a árvore será armazenada
    private String nomeArquivo;
    
    // Variáveis usadas nas funções recursivas (já que não é possível passar valores por referência)
    private int  chaveAux;
    private long     dadoAux;
    private long    paginaAux;
    private boolean cresceu;
    private boolean diminuiu;

    public arvore(int o, String na) throws IOException {
        
        // Inicializa os atributos da árvore
        Ordem = o;
        maxElementos = o-1;
        maxFilhos = o;
        nomeArquivo = na;
        
        // Abre (ou cria) o arquivo, escrevendo uma raiz empty, se necessário.
        arquivo = new RandomAccessFile(nomeArquivo,"rw");
        if(arquivo.length()<8) 
            arquivo.writeLong(-1);  // raiz empty
    }

    public boolean empty() throws IOException {
        long raiz;
        arquivo.seek(0);
        raiz = arquivo.readLong();
        return raiz == -1;
    }


    public boolean create(int ID, long pos) throws IOException
    {

        // Chave não pode ser empty
        if(ID == 0) {
            System.out.println( "Chave não pode ser vazia" );
            return false;
        }
            
        // Carrega a raiz
        arquivo.seek(0);       
        long pagina;
        pagina = arquivo.readLong();

        // O processo de inclusão permite que os valores passados como referência
        // sejam substituídos por outros valores, para permitir a divisão de páginas
        // e crescimento da árvore. Assim, são usados os valores globais chaveAux 
        // e dadoAux. Quando há uma divisão, a chave e o valor promovidos são armazenados
        // nessas variáveis.
        chaveAux = ID;
        dadoAux = pos;
        
        // Se houver crescimento, então será criada uma página extra e será mantido um
        // ponteiro para essa página. Os valores também são globais.
        paginaAux = -1;
        cresceu = false;
                
        // Chamada recursiva para a inserção da chave e do valor
        // A chave e o valor não são passados como parâmetros, porque são globais
        boolean inserido = create1(pagina);
        
        // Testa a necessidade de criação de uma nova raiz.
        if(cresceu) {
            
            // Cria a nova página que será a raiz. O ponteiro esquerdo da raiz
            // será a raiz antiga e o seu ponteiro direito será para a nova página.
            Node node = new Node(Ordem);
            node.n = 1;
            node.chaves[0] = chaveAux;
            node.dados[0]  = dadoAux;
            node.filhos[0] = pagina;
            node.filhos[1] = paginaAux;
            
            // Acha o espaço em disco. Nesta versão, todas as novas páginas
            // são escrita no fim do arquivo.
            arquivo.seek(arquivo.length());
            long raiz = arquivo.getFilePointer();
            arquivo.write(node.toByteArrayArv());
            arquivo.seek(0);
            arquivo.writeLong(raiz);
        }
        return inserido;        
    }

    public boolean create1(long node)
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



    public void LerRegistroEfazerArvore( String s1, String s2 ,int NumRegistros) throws IOException
    {

        byte[] ba;

        BufferedReader bf = new BufferedReader(new FileReader(s1));
        RandomAccessFile arq = new RandomAccessFile(s2, "rw");
        arq.writeInt(0);
        arq.close();
        Crud CRU = new Crud();
        
        for (int j = 0; j < NumRegistros; j++) {
            Netflix net =  new Netflix(bf.readLine());
            CRU.create(net);
            // create ARVORE
        }
        bf.close();

      
    
        
            
    }
    
}
