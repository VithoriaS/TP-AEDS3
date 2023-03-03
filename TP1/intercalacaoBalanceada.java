import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class intercalacaoBalanceada {

    public void ordencao() throws IOException {

        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        RandomAccessFile arq1 = new RandomAccessFile("temp1.db", "rw");
        RandomAccessFile arq2 = new RandomAccessFile("temp2.db", "rw");
        RandomAccessFile arq3 = new RandomAccessFile("temp3.db", "rw");
        RandomAccessFile arq4 = new RandomAccessFile("temp4.db", "rw");

        byte ba[];
        long pos;
        int len = 0;
        len = arq.readInt();

        int x = 0;
        int tamanho, tamanho1 = 0;
        boolean par = len % 2 == 0;
        /*
        if (par) {
            tamanho = len / 2;

            for (int i = 0; i < tamanho; i++) {

            }
            
        } else {
            tamanho = len / 2;
            tamanho1 = tamanho + 1;
        }

        String pos1, pos2, pos3, pos4;
         */
        // ---------------------------------------- ordenacao em memoria principal ----------------------------------------
        int  y = 4; // quantos arquivos por vez 
        
        int k =0;
        while (len < k) {
            Netflix[] Netarray = new Netflix[y];

            for (int i = 0; i < Netarray.length; i++) {
                char c = arq.readChar();
                int length = arq.readInt();

                if (c != '*') {
                    
                    ba = new byte[length];
                    arq.read(ba);
                    Netflix net_temp = new Netflix();
                    net_temp.fromByteArray(ba);
                    Netarray[i] = net_temp;
                }
                else{
                    arq.skipBytes(length);
                }

                k++;
            }
            ordernarLista(Netarray, 0, Netarray.length -1);
            
            for (int i = 0; i < Netarray.length; i++) {
                byte[] ba2;
                ba2 = Netarray[i].toByteArray();
                if (k % 2 == 0) {
                    
                    arq1.write(ba2);
                }
                else{
                    arq2.write(ba2);
                }
            }
        }

    }

    public Netflix[] swap(Netflix netArr[], int x, int y) {
        Netflix temp = netArr[x];
        netArr[x] = netArr[y];
        netArr[y] = temp;
        return netArr;
    }

    void ordernarLista(Netflix netArr[], int esq, int dir) {
        if (esq < dir) {
            int pi = particao(netArr, esq, dir);

            ordernarLista(netArr, esq, pi - 1);
            ordernarLista(netArr, pi + 1, dir);
        }
    }

    int particao(Netflix netArr[], int esq, int dir) {
        Netflix pivo = netArr[dir];
        int i = (esq - 1);

        for (int j = esq; j < dir; j++) {
            if (pivo.getName().compareTo(netArr[j].getName()) > 0) {

                i++;
                netArr = swap(netArr, i, j);
            }

        }

        netArr = swap(netArr, dir, i + 1);

        return (i + 1);
    }

}
