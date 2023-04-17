import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.text.Position;

public class arvore {
    private int Ordem;

    private int  maxElementos;          // Variável igual a Ordem - 1 para facilitar a clareza do código
    private int  maxFilhos;             // Variável igual a Ordem para facilitar a clareza do código
    private RandomAccessFile arquivo;   // Arquivo em que a árvore será armazenada
    private String nomeArquivo;
    
    // Variáveis usadas nas funções recursivas (já que não é possível passar valores por referência)
    private int  chaveAux;
    private long     dadoAux;
    private long    nodeAux;
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
        long node;
        node = arquivo.readLong();

        // O processo de inclusão permite que os valores passados como referência
        // sejam substituídos por outros valores, para permitir a divisão de páginas
        // e crescimento da árvore. Assim, são usados os valores globais chaveAux 
        // e dadoAux. Quando há uma divisão, a chave e o valor promovidos são armazenados
        // nessas variáveis.
        chaveAux = ID;
        dadoAux = pos;
        
        // Se houver crescimento, então será criada uma página extra e será mantido um
        // ponteiro para essa página. Os valores também são globais.
        nodeAux = -1;
        cresceu = false;
                
        // Chamada recursiva para a inserção da chave e do valor
        // A chave e o valor não são passados como parâmetros, porque são globais
        boolean inserido = create1(node);
        
        // Testa a necessidade de criação de uma nova raiz.
        if(cresceu) {
            
            // Cria a nova página que será a raiz. O ponteiro esquerdo da raiz
            // será a raiz antiga e o seu ponteiro direito será para a nova página.
            Node nd = new Node(Ordem);
            nd.n = 1;
            nd.chaves[0] = chaveAux;
            nd.dados[0]  = dadoAux;
            nd.filhos[0] = node;
            nd.filhos[1] = nodeAux;
            
            // Acha o espaço em disco. Nesta versão, todas as novas páginas
            // são escrita no fim do arquivo.
            arquivo.seek(arquivo.length());
            long raiz = arquivo.getFilePointer();
            arquivo.write(nd.toByteArrayArv());
            arquivo.seek(0);
            arquivo.writeLong(raiz);
        }
        return inserido;        
    }

    public boolean create1(long node) throws IOException
    {
         // Testa se passou para o filho de uma página folha. Nesse caso, 
        // inicializa as variáveis globais de controle.
        if(node==-1) {
            cresceu = true;
            nodeAux = -1;
            return false;
        }
        
        // Lê a página passada como referência
        arquivo.seek(node);
        Node pa = new Node(Ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.fromByteArrayArv(buffer);
        
        // Busca o próximo ponteiro de descida. Como pode haver repetição
        // da primeira chave, a segunda também é usada como referência.
        // Nesse primeiro passo, todos os pares menores são ultrapassados.
        int i=0;
        while(i<pa.n && chaveAux > pa.chaves[i]) {
            i++;
        } 
        
        // Testa se a chave já existe em uma folha. Se isso acontecer, então 
        // a inclusão é cancelada.
        if(i<pa.n && pa.filhos[0]==-1 && chaveAux == (pa.chaves[i])) {
            cresceu = false;
            return false;
        }
        
        // Continua a busca recursiva por uma nova página. A busca continuará até o
        // filho inexistente de uma página folha ser alcançado.
        boolean inserido;
        if(i==pa.n ||   chaveAux < pa.chaves[i] )
            inserido = create1(pa.filhos[i]);
        else
            inserido = create1(pa.filhos[i+1]);
        
        // A partir deste ponto, as chamadas recursivas já foram encerradas. 
        // Assim, o próximo código só é executado ao retornar das chamadas recursivas.

        // A inclusão já foi resolvida por meio de uma das chamadas recursivas. Nesse
        // caso, apenas retorna para encerrar a recursão.
        // A inclusão pode ter sido resolvida porque a chave já existia (inclusão inválida)
        // ou porque o novo elemento coube em uma página existente.
        if(!cresceu)
            return inserido;
        
        // Se tiver espaço na página, faz a inclusão nela mesmo
        if(pa.n<maxElementos) {

            // Puxa todos elementos para a direita, começando do último
            // para gerar o espaço para o novo elemento
            for(int j=pa.n; j>i; j--) {
                pa.chaves[j] = pa.chaves[j-1];
                pa.dados[j] = pa.dados[j-1];
                pa.filhos[j+1] = pa.filhos[j];
            }
            
            // Insere o novo elemento
            pa.chaves[i] = chaveAux;
            pa.dados[i] = dadoAux;
            pa.filhos[i+1] = nodeAux;
            pa.n++;
            
            // Escreve a página atualizada no arquivo
            arquivo.seek(node);
            arquivo.write(pa.toByteArrayArv());
            
            // Encerra o processo de crescimento e retorna
            cresceu=false;
            return true;
        }
        
        // O elemento não cabe na página. A página deve ser dividida e o elemento
        // do meio deve ser promovido (sem retirar a referência da folha).
        
        // Cria uma nova página
        Node np = new Node(Ordem);
        
        // Copia a metade superior dos elementos para a nova página,
        // considerando que maxElementos pode ser ímpar
        int meio = maxElementos/2;
        for(int j=0; j<(maxElementos-meio); j++) {    
            
            // copia o elemento
            np.chaves[j] = pa.chaves[j+meio];
            np.dados[j] = pa.dados[j+meio];   
            np.filhos[j+1] = pa.filhos[j+meio+1];  
            
            // limpa o espaço liberado
            pa.chaves[j+meio] = -1;
            pa.dados[j+meio] = 0;
            pa.filhos[j+meio+1] = -1;
        }
        np.filhos[0] = pa.filhos[meio];
        np.n = maxElementos-meio;
        pa.n = meio;
        
        // Testa o lado de inserção
        // Caso 1 - Novo registro deve ficar na página da esquerda
        if(i<=meio) {   
            
            // Puxa todos os elementos para a direita
            for(int j=meio; j>0 && j>i; j--) {
                pa.chaves[j] = pa.chaves[j-1];
                pa.dados[j] = pa.dados[j-1];
                pa.filhos[j+1] = pa.filhos[j];
            }
            
            // Insere o novo elemento
            pa.chaves[i] = chaveAux;
            pa.dados[i] = dadoAux;
            pa.filhos[i+1] = nodeAux;
            pa.n++;
            
            // Se a página for folha, seleciona o primeiro elemento da página 
            // da direita para ser promovido, mantendo-o na folha
            if(pa.filhos[0]==-1) {
                chaveAux = np.chaves[0];
                dadoAux = np.dados[0];
            }
            
            // caso contrário, promove o maior elemento da página esquerda
            // removendo-o da página
            else {
                chaveAux = pa.chaves[pa.n-1];
                dadoAux = pa.dados[pa.n-1];
                pa.chaves[pa.n-1] = -1;
                pa.dados[pa.n-1] = 0;
                pa.filhos[pa.n] = -1;
                pa.n--;
            }
        } 
        
        // Caso 2 - Novo registro deve ficar na página da direita
        else {
            int j;
            for(j=maxElementos-meio; j>0 && chaveAux < np.chaves[j-1]; j--) {
                np.chaves[j] = np.chaves[j-1];
                np.dados[j] = np.dados[j-1];
                np.filhos[j+1] = np.filhos[j];
            }
            np.chaves[j] = chaveAux;
            np.dados[j] = dadoAux;
            np.filhos[j+1] = nodeAux;
            np.n++;

            // Seleciona o primeiro elemento da página da direita para ser promovido
            chaveAux = np.chaves[0];
            dadoAux = np.dados[0];
            
            // Se não for folha, remove o elemento promovido da página
            if(pa.filhos[0]!=-1) {
                for(j=0; j<np.n-1; j++) {
                    np.chaves[j] = np.chaves[j+1];
                    np.dados[j] = np.dados[j+1];
                    np.filhos[j] = np.filhos[j+1];
                }
                np.filhos[j] = np.filhos[j+1];
                
                // apaga o último elemento
                np.chaves[j] = -1;
                np.dados[j] = 0;
                np.filhos[j+1] = -1;
                np.n--;
            }

        }
        
        

        // Grava as páginas no arquivos arquivo
        nodeAux = arquivo.length();
        arquivo.seek(nodeAux);
        arquivo.write(np.toByteArrayArv());

        arquivo.seek(node);
        arquivo.write(pa.toByteArrayArv());
        
        return true;
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



  
    
}
