
public class Node {
    boolean leaf;
    int ordem;          // Número máximo de filhos que uma página pode ter
    int maxElementos;       // Variável igual a ordem - 1 para facilitar a clareza do código
    int maxFilhos;       // Variável igual a ordem para facilitar a clareza do código
    int n;              // Número de elementos presentes na página
    int[] chaves;                // Chaves
    int[] dados;                        // Dados associados às chaves
    long proxima;                   // Próxima folha, quando a página for uma folha
    long[] filhos;                  // Vetor de ponteiros para os filhos
 
    public Node(int o) {

        // Inicialização dos atributos
        n = 0;
        ordem = o;
        maxFilhos = o;
        maxElementos = o-1;
        chaves = new int[maxElementos];
        dados  = new int[maxElementos];
        filhos = new long[maxFilhos];
        proxima = -1;
        
        // Criação de uma página vázia
        for(int i=0; i<maxElementos; i++) {  
            chaves[i] = 0;
            dados[i]  = -1;
            filhos[i] = -1;
        }
        filhos[maxFilhos-1] = -1;

    }

    
    
}