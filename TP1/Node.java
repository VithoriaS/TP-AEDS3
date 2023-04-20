import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Node {
    
    int ordem;          // Número máximo de filhos que uma página pode ter
    int maxElementos;       // Variável igual a ordem - 1 para facilitar a clareza do código
    int maxFilhos;       // Variável igual a ordem para facilitar a clareza do código
    int n;              // Número de elementos presentes na página
    int[] chaves;                // Chaves
    long[] dados;                        // Dados associados às chaves
    long[] filhos;                  // Vetor de ponteiros para os filhos
    protected int      TAMANHO_PAGINA;    
    public Node(int o) {

        // Inicialização dos atributos
        n = 0;
        ordem = o;
        maxFilhos = o;
        maxElementos = o-1;
        chaves = new int[maxElementos];
        dados  = new long[maxElementos];
        filhos = new long[maxFilhos];
       
        
        // Criação de uma página vázia
        for(int i=0; i<maxElementos; i++) {  
            chaves[i] = 0;
            dados[i]  = -1;
            filhos[i] = -1;
        }
        filhos[maxFilhos-1] = -1;

        TAMANHO_PAGINA = 4 + maxElementos*4 + maxElementos*8 + maxFilhos*8  ;
    }

    public byte[] toByteArrayArv() throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(n);
            
        // Escreve todos os elementos
        int i=0;
        while(i<n) {
            dos.writeLong(filhos[i]);
            dos.writeInt(chaves[i]);
            dos.writeLong(dados[i]);
            i++;
        }
        dos.writeLong(filhos[i]);
        while(i<maxElementos){
            dos.writeLong(-1);
            dos.writeInt(0);
            dos.writeLong(-1);
            i++;
        }

        return baos.toByteArray();
    }
    
    public void fromByteArrayArv(byte[] buffer) throws IOException
    {
        ByteArrayInputStream ba = new ByteArrayInputStream(buffer);
        DataInputStream in = new DataInputStream(ba);
        n = in.readInt();
            
        // Lê todos os elementos (reais ou vazios)
        int i=0;
        while(i<maxElementos) {
            filhos[i]  = in.readLong();
            
            chaves[i] = in.readInt();
            dados[i]   = in.readLong(); 
            i++;
        }
        filhos[i] = in.readLong();
    }
    
}