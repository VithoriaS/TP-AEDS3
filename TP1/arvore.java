import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.text.Position;

public class arvore {
    private int Ordem;

    private int  maxElementos;          
    private int  maxFilhos;             
    private RandomAccessFile arquivo;   
    private String nomeArquivo;
    
    // Variáveis usadas nas recursioes 
    private int  chaveAux;
    private long     dadoAux;
    private long    nodeAux;
    private boolean cresceu;
    private boolean diminuiu;

    // contrutor, abre arquivo com raiz vazia
    public arvore(int o, String na) throws IOException {    
        
        
        Ordem = o;
        maxElementos = o-1;
        maxFilhos = o;
        nomeArquivo = na;
        
      
        arquivo = new RandomAccessFile(nomeArquivo,"rw");
        if(arquivo.length()<8) 
            arquivo.writeLong(-1);  // raiz empty
    }


    public boolean create(int ID, long pos) throws IOException
    {

        // Chave não pode ser empty
        if(ID == 0) {
            System.out.println( "Chave não pode ser vazia" );
            return false;
        }
            
        // pega a raiz
        arquivo.seek(0);       
        long node;
        node = arquivo.readLong();

       // coloca nas variaveis globais para nao perder o valor na funcao recursiva
        chaveAux = ID;
        dadoAux = pos;
        
        // se crescer cria uma nova pagina
        nodeAux = -1;
        cresceu = false;
                
        // Chamada recursiva para a inserção da chave e do valor
        boolean inserido = create1(node);
        
        // ve se precisa aumentar ou nao a arvore
        if(cresceu) {
            
           
            Node nd = new Node(Ordem);
            nd.n = 1;
            nd.chaves[0] = chaveAux;
            nd.dados[0]  = dadoAux;
            nd.filhos[0] = node;
            nd.filhos[1] = nodeAux;
            
            
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
         // testa se passou para o filho de uma página folha. se sim iniciliaza as variaveis  
       
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
        while(i<pa.n && chaveAux > pa.chaves[i]) { // abacate
            i++;
        } 
        
        // Testa se a chave já existe em uma folha. Se isso acontecer, então 
        // a inclusão é cancelada.
        if(i<pa.n && pa.filhos[0]== -1 && chaveAux == (pa.chaves[i])) {
            cresceu = false;
            return false;
        }
        
        // Continua a busca recursiva por uma nova página. A busca continuará até o
        // filho inexistente de uma página folha ser alcançado.
        boolean inserido;
        if(i==pa.n ||   chaveAux < pa.chaves[i] ) // abacate
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
            for(j=maxElementos-meio; j>0 && chaveAux < np.chaves[j-1]; j--) { // abacate
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


    public long read(int d) throws IOException {
        
        // Recupera a raiz da árvore
        long raiz;
        arquivo.seek(0);
        raiz = arquivo.readLong();
        
        // Executa a busca recursiva
        if(raiz!=-1)
        {
           
            return read1(d,raiz);
        }
        else
        {
            
            return -1;
        }
          
    }

    private long read1(int chave, long pagina) throws IOException {
        
        // Como a busca é recursiva, a descida para um filho inexistente
        // (filho de uma página folha) retorna um valor negativo.
        if(pagina==-1)
            return -1;
        
        // Reconstrói a página passada como referência a partir 
        // do registro lido no arquivo
        arquivo.seek(pagina);
        Node pa = new Node(Ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.fromByteArrayArv(buffer);
 
        // Encontra o ponto em que a chave deve estar na página
        // Primeiro passo - todas as chaves menores que a chave buscada são ignoradas
        int i=0;
        while(i<pa.n && chave > pa.chaves[i] ) { // abacate
            i++;
        }
        
        // Chave encontrada (ou pelo menos o ponto onde ela deveria estar).
        // Segundo passo - testa se a chave é a chave buscada e se está em uma folha
        // Obs.: em uma árvore B+, todas as chaves válidas estão nas folhas
        // Obs.: a comparação exata só será possível se considerarmos a menor string
        //       entre a chave e a string na página
        if(i<pa.n  
                  && chave == (pa.chaves[i])) {
                    
            return pa.dados[i];
        }
        
        // Terceiro passo - ainda não é uma folha, continua a busca recursiva pela árvore
        if(i==pa.n || chave < pa.chaves[i]) // abacate
            return read1(chave, pa.filhos[i]);
        else
            return read1(chave, pa.filhos[i+1]);

    }


 // Remoção elementos na árvore. A remoção é recursiva. A primeira
    // função chama a segunda recursivamente, passando a raiz como referência.
    // Eventualmente, a árvore pode reduzir seu tamanho, por meio da exclusão da raiz.
    public boolean delete(int chave) throws IOException {
                
        // Encontra a raiz da árvore
        arquivo.seek(0);       
        long pagina;                
        pagina = arquivo.readLong();

        // variável global de controle da redução do tamanho da árvore
        diminuiu = false;  
                
        // Chama recursivamente a exclusão de registro (na chave1Aux e no 
        // chave2Aux) passando uma página como referência
        boolean excluido = delete1(chave, pagina);
        
        // Se a exclusão tiver sido possível e a página tiver reduzido seu tamanho,
        // por meio da fusão das duas páginas filhas da raiz, elimina essa raiz
        if(excluido && diminuiu) {
            
            // Lê a raiz
            arquivo.seek(pagina);
            Node pa = new Node(Ordem);
            byte[] buffer = new byte[pa.TAMANHO_PAGINA];
            arquivo.read(buffer);
            pa.fromByteArrayArv(buffer);
            
            // Se a página tiver 0 elementos, apenas atualiza o ponteiro para a raiz,
            // no cabeçalho do arquivo, para o seu primeiro filho.
            if(pa.n == 0) {
                arquivo.seek(0);
                arquivo.writeLong(pa.filhos[0]);  
            }
        }
         
        return excluido;
    }
    

    // Função recursiva de exclusão. A função passa uma página de referência.
    // As exclusões são sempre feitas em folhas e a fusão é propagada para cima.
    private boolean delete1(int chave, long pagina) throws IOException {
        
        // Declaração de variáveis
        boolean excluido=false;
        int diminuido;
        
        // Testa se o registro não foi encontrado na árvore, ao alcançar uma folha
        // inexistente (filho de uma folha real)
        if(pagina==-1) {
            diminuiu=false;
            return false;
        }
        
        // Lê o registro da página no arquivo
        arquivo.seek(pagina);
        Node pa = new Node(Ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.fromByteArrayArv(buffer);

        // Encontra a página em que a chave está presente
        // Nesse primeiro passo, salta todas as chaves menores
        int i=0;
        while(i<pa.n && chave > pa.chaves[i] ) {
            i++;
        }

        // Chaves encontradas em uma folha
        if(i<pa.n && pa.filhos[0]==-1 && chave == pa.chaves[i] ) {

            // Puxa todas os elementos seguintes para uma posição anterior, sobrescrevendo
            // o elemento a ser excluído
            int j;
            for(j=i; j<pa.n-1; j++) {
                pa.chaves[j] = pa.chaves[j+1];
                pa.dados[j] = pa.dados[j+1];
            }
            pa.n--;
            
            // limpa o último elemento
            pa.chaves[pa.n] = -1;
            pa.dados[pa.n] = 0;
            
            // Atualiza o registro da página no arquivo
            arquivo.seek(pagina);
            arquivo.write(pa.toByteArrayArv());
            
            // Se a página contiver menos elementos do que o mínimo necessário,
            // indica a necessidade de fusão de páginas
            diminuiu = pa.n<maxElementos/2;
            return true;
        }

        // Se a chave não tiver sido encontrada (observar o return true logo acima),
        // continua a busca recursiva por uma nova página. A busca continuará até o
        // filho inexistente de uma página folha ser alcançado.
        // A variável diminuído mantem um registro de qual página eventualmente 
        // pode ter ficado com menos elementos do que o mínimo necessário.
        // Essa página será filha da página atual
        if(i==pa.n || chave < pa.chaves[i] ) {
            excluido = delete1(chave, pa.filhos[i]);
            diminuido = i;
        } else {
            excluido = delete1(chave, pa.filhos[i+1]);
            diminuido = i+1;
        }
        
        
        // A partir deste ponto, o código é executado após o retorno das chamadas
        // recursivas do método
        
        // Testa se há necessidade de fusão de páginas
        if(diminuiu) {

            // Carrega a página filho que ficou com menos elementos do 
            // do que o mínimo necessário
            long paginaFilho = pa.filhos[diminuido];
            Node pFilho = new Node(Ordem);
            arquivo.seek(paginaFilho);
            arquivo.read(buffer);
            pFilho.fromByteArrayArv(buffer);
            
            // Cria uma página para o irmão (da direita ou esquerda)
            long paginaIrmao;
            Node pIrmao;
            
            // Tenta a fusão com irmão esquerdo
            if(diminuido>0) {
                
                // Carrega o irmão esquerdo
                paginaIrmao = pa.filhos[diminuido-1];
                pIrmao = new Node(Ordem);
                arquivo.seek(paginaIrmao);
                arquivo.read(buffer);
                pIrmao.fromByteArrayArv(buffer);
                
                // Testa se o irmão pode ceder algum registro
                if(pIrmao.n>maxElementos/2) {
                    
                    // Move todos os elementos do filho aumentando uma posição
                    // à esquerda, gerando espaço para o elemento cedido
                    for(int j=pFilho.n; j>0; j--) {
                        pFilho.chaves[j] = pFilho.chaves[j-1];
                        pFilho.dados[j] = pFilho.dados[j-1];
                        pFilho.filhos[j+1] = pFilho.filhos[j];
                    }
                    pFilho.filhos[1] = pFilho.filhos[0];
                    pFilho.n++;
                    
                    // Se for folha, copia o elemento do irmão, já que o do pai
                    // será extinto ou repetido
                    if(pFilho.filhos[0]==-1) {
                        pFilho.chaves[0] = pIrmao.chaves[pIrmao.n-1];
                        pFilho.dados[0] = pIrmao.dados[pIrmao.n-1];
                    }
                    
                    // Se não for folha, rotaciona os elementos, descendo o elemento do pai
                    else {
                        pFilho.chaves[0] = pa.chaves[diminuido-1];
                        pFilho.dados[0] = pa.dados[diminuido-1];
                    }

                    // Copia o elemento do irmão para o pai (página atual)
                    pa.chaves[diminuido-1] = pIrmao.chaves[pIrmao.n-1];
                    pa.dados[diminuido-1] = pIrmao.dados[pIrmao.n-1];
                        
                    
                    // Reduz o elemento no irmão
                    pFilho.filhos[0] = pIrmao.filhos[pIrmao.n];
                    pIrmao.n--;
                    diminuiu = false;
                }
                
                // Se não puder ceder, faz a fusão dos dois irmãos
                else {

                    // Se a página reduzida não for folha, então o elemento 
                    // do pai deve ser copiado para o irmão
                    if(pFilho.filhos[0] != -1) {
                        pIrmao.chaves[pIrmao.n] = pa.chaves[diminuido-1];
                        pIrmao.dados[pIrmao.n] = pa.dados[diminuido-1];
                        pIrmao.filhos[pIrmao.n+1] = pFilho.filhos[0];
                        pIrmao.n++;
                    }
                    
                    
                    // Copia todos os registros para o irmão da esquerda
                    for(int j=0; j<pFilho.n; j++) {
                        pIrmao.chaves[pIrmao.n] = pFilho.chaves[j];
                        pIrmao.dados[pIrmao.n] = pFilho.dados[j];
                        pIrmao.filhos[pIrmao.n+1] = pFilho.filhos[j+1];
                        pIrmao.n++;
                    }
                    pFilho.n = 0;   // aqui o endereço do filho poderia ser incluido em uma lista encadeada no cabeçalho, indicando os espaços reaproveitáveis
                    
                    // Se as páginas forem folhas, copia o ponteiro para a folha seguinte
              //      if(pIrmao.filhos[0]==-1)
                     //   pIrmao.proxima = pFilho.proxima;
                    
                    // puxa os registros no pai
                    int j;
                    for(j=diminuido-1; j<pa.n-1; j++) {
                        pa.chaves[j] = pa.chaves[j+1];
                        pa.dados[j] = pa.dados[j+1];
                        pa.filhos[j+1] = pa.filhos[j+2];
                    }
                    pa.chaves[j] = -1;
                    pa.dados[j] = -1;
                    pa.filhos[j+1] = -1;
                    pa.n--;
                    diminuiu = pa.n<maxElementos/2;  // testa se o pai também ficou sem o número mínimo de elementos
                }
            }
            
            // Faz a fusão com o irmão direito
            else {
                
                // Carrega o irmão
                paginaIrmao = pa.filhos[diminuido+1];
                pIrmao = new Node(Ordem);
                arquivo.seek(paginaIrmao);
                arquivo.read(buffer);
                pIrmao.fromByteArrayArv(buffer);
                
                // Testa se o irmão pode ceder algum elemento
                if(pIrmao.n>maxElementos/2) {
                    
                    // Se for folha
                    if( pFilho.filhos[0]==-1 ) {
                    
                        //copia o elemento do irmão
                        pFilho.chaves[pFilho.n] = pIrmao.chaves[0];
                        pFilho.dados[pFilho.n] = pIrmao.dados[0];
                        pFilho.filhos[pFilho.n+1] = pIrmao.filhos[0];
                        pFilho.n++;

                        // sobe o próximo elemento do irmão
                        pa.chaves[diminuido] = pIrmao.chaves[1];
                        pa.dados[diminuido] = pIrmao.dados[1];
                        
                    } 
                    
                    // Se não for folha, rotaciona os elementos
                    else {
                        
                        // Copia o elemento do pai, com o ponteiro esquerdo do irmão
                        pFilho.chaves[pFilho.n] = pa.chaves[diminuido];
                        pFilho.dados[pFilho.n] = pa.dados[diminuido];
                        pFilho.filhos[pFilho.n+1] = pIrmao.filhos[0];
                        pFilho.n++;
                        
                        // Sobe o elemento esquerdo do irmão para o pai
                        pa.chaves[diminuido] = pIrmao.chaves[0];
                        pa.dados[diminuido] = pIrmao.dados[0];
                    }
                    
                    // move todos os registros no irmão para a esquerda
                    int j;
                    for(j=0; j<pIrmao.n-1; j++) {
                        pIrmao.chaves[j] = pIrmao.chaves[j+1];
                        pIrmao.dados[j] = pIrmao.dados[j+1];
                        pIrmao.filhos[j] = pIrmao.filhos[j+1];
                    }
                    pIrmao.filhos[j] = pIrmao.filhos[j+1];
                    pIrmao.n--;
                    diminuiu = false;
                }
                
                // Se não puder ceder, faz a fusão dos dois irmãos
                else {

                    // Se a página reduzida não for folha, então o elemento 
                    // do pai deve ser copiado para o irmão
                    if(pFilho.filhos[0] != -1) {
                        pFilho.chaves[pFilho.n] = pa.chaves[diminuido];
                        pFilho.dados[pFilho.n] = pa.dados[diminuido];
                        pFilho.filhos[pFilho.n+1] = pIrmao.filhos[0];
                        pFilho.n++;
                    }
                    
                    // Copia todos os registros do irmão da direita
                    for(int j=0; j<pIrmao.n; j++) {
                        pFilho.chaves[pFilho.n] = pIrmao.chaves[j];
                        pFilho.dados[pFilho.n] = pIrmao.dados[j];
                        pFilho.filhos[pFilho.n+1] = pIrmao.filhos[j+1];
                        pFilho.n++;
                    }
                    pIrmao.n = 0;   // aqui o endereço do irmão poderia ser incluido em uma lista encadeada no cabeçalho, indicando os espaços reaproveitáveis
                    
                    // Se a página for folha, copia o ponteiro para a próxima página
             //       pFilho.proxima = pIrmao.proxima;
                    
                    // puxa os registros no pai
                    for(int j=diminuido; j<pa.n-1; j++) {
                        pa.chaves[j] = pa.chaves[j+1];
                        pa.dados[j] = pa.dados[j+1];
                        pa.filhos[j+1] = pa.filhos[j+2];
                    }
                    pa.n--;
                    diminuiu = pa.n<maxElementos/2;  // testa se o pai também ficou sem o número mínimo de elementos
                }
            }
            
            // Atualiza todos os registros
            arquivo.seek(pagina);
            arquivo.write(pa.toByteArrayArv());
            arquivo.seek(paginaFilho);
            arquivo.write(pFilho.toByteArrayArv());
            arquivo.seek(paginaIrmao);
            arquivo.write(pIrmao.toByteArrayArv());
        }
        return excluido;
    }
    

   



  
    
}
