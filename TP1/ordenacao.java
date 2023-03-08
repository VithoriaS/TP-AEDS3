import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;

public class ordenacao {
    int y = 0;

    public void LoopOrdenacao() throws IOException {
        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        int len = 0;
        len = arq.readInt();
        arq.close();
        Scanner sc = new Scanner(System.in);
        System.out.println("Qual Valor De Y");
        y = sc.nextInt();
       ordencaoMemoriaPrimaria();
        
        int x = 0;

        System.out.println("\nOpcoes ");
        System.out.println(" 1 - Comum");
        System.out.println(" 2 - BlocosTamanhoVariavel");
        System.out.println(" 3 - SelecaoPorSubstituicao");
        System.out.println("Entrar com uma opcao:");
        x = sc.nextInt();
        int i  = 0;
        switch (x) {
            case 1:
            while (y < len) {

                if (i%2 == 0) {
                    intercalacaoComum2("temp1.db", "temp2.db", "temp3.db", "temp4.db");
                } else {
                    intercalacaoComum2("temp3.db", "temp4.db", "temp1.db", "temp2.db");
                }

                i++;
            }
                
                break;
            case 2:
          
                intercalacaoBlocosTamanhoVariavel();
                break;
            case 3:
                intercalacaoSelecaoPorSubstituicao();
                break;
            default:
                break;
        }
        sc.close();
    }

    public void ordencaoMemoriaPrimaria() throws IOException {

        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        RandomAccessFile arq1 = new RandomAccessFile("temp1.db", "rw");
        RandomAccessFile arq2 = new RandomAccessFile("temp2.db", "rw");
     
        int tamanhoArq1 = 0, tamanhoArq2 = 0;
        byte ba[];
        long pos;
        int len = 0;
        len = arq.readInt();

        int x = 0;
        int tamanho, tamanho1 = 0;
        boolean par = len % 2 == 0;
        /*
         * if (par) {
         * tamanho = len / 2;
         * 
         * for (int i = 0; i < tamanho; i++) {
         * 
         * }
         * 
         * } else {
         * tamanho = len / 2;
         * tamanho1 = tamanho + 1;
         * }
         * 
         * String pos1, pos2, pos3, pos4;
         */
        // ---------------------------------------- ordenacao em memoria principal
        // ----------------------------------------
        // quantos arquivos por vez
        int k = 0;
        int j = 0;
        Netflix paraOWhile = new Netflix();
        Netflix[] Netarray = new Netflix[0];

        while (paraOWhile.getId() != len && k < len) {

            if (len - k < y) {
                Netarray = new Netflix[len - k];
            } else {
                Netarray = new Netflix[y];
            }

            for (int i = 0; i < Netarray.length; i++) {
                char c = arq.readChar();
                int length = arq.readInt();

                if (c != '*') {

                    ba = new byte[length];
                    arq.read(ba);
                    Netflix net_temp = new Netflix();
                    net_temp.fromByteArray(ba);
                    Netarray[i] = net_temp;
                } else {
                    arq.skipBytes(length);
                }

                k++;
            }

            paraOWhile = Netarray[Netarray.length - 1];
            ordernarLista(Netarray, 0, Netarray.length - 1);

            // colocando todos os 4 dados
            for (int i = 0; i < Netarray.length; i++) {
                byte[] ba2;
                ba2 = Netarray[i].toByteArray();
                if (j % 2 == 0) {
                    arq1.writeChar(' ');
                    arq1.writeInt(ba2.length);
                    arq1.write(ba2);
                    tamanhoArq1++;
                } else {
                    arq2.writeChar(' ');
                    arq2.writeInt(ba2.length);
                    arq2.write(ba2);
                    tamanhoArq2++;
                }
            }

            // BLOCO
            j++;

        }
        arq.close();
        arq1.close();
        arq2.close();
   

    }

  
    public void intercalacaoComum2(String R1, String R2, String W1 ,String W2  ) throws IOException {

        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        RandomAccessFile arq1 = new RandomAccessFile(R1, "rw");
        RandomAccessFile arq2 = new RandomAccessFile(R2, "rw");
        RandomAccessFile arq5 = new RandomAccessFile(W1, "rw");
        RandomAccessFile arq6 = new RandomAccessFile(W2, "rw");
        arq5.setLength(0);
        arq6.setLength(0);
        arq5.close();
        arq6.close();
        RandomAccessFile arq3 = new RandomAccessFile(W1, "rw");
        RandomAccessFile arq4 = new RandomAccessFile(W2, "rw");

        int len = 0;
        len = arq.readInt();

        int bloco = 0;
        int k = 0;
        byte ba[];
        byte ba2[];
        int j = 0;
        int p = 0;
        long pos1 = 0;
        long pos2 = 0;

        int numeroLoco = 0;
        numeroLoco = len / y;
        if (len % y > 0) {
            numeroLoco++;
        }
        int limite1 = 0;
        int limite2 = 0;
        System.out.println(y);
        while (numeroLoco > bloco) {

            j = 0;
            p = 0;
            if (len - k < y) {
                limite1 = len - k;
                limite2 = 0;

            } else {
                limite1 = y;

                if ((len - k ) -y < y) {

                    limite2 = len - k;
                    if (len < y*2) {
                        limite2 = len - y;
                    }
                   
                   
                } else {
                    limite2 = y;
                }
            }

            if (bloco % 2 == 0) {

                while (j != limite1 && p != limite2) {

                    pos1 = arq1.getFilePointer();
                    char c = arq1.readChar();
                    int tamanho = arq1.readInt();

                    ba = new byte[tamanho];
                    arq1.read(ba);
                    Netflix net_temp1 = new Netflix();
                    net_temp1.fromByteArray(ba);



                    pos2 =  arq2.getFilePointer();
                    try {
                        char c2 = arq2.readChar();
                    } catch (Exception e) {
                        System.out.println("Teste");
                    }
                    int tamanho2 = arq2.readInt();
                    ba2 = new byte[tamanho2];
                    arq2.read(ba2);

                    Netflix net_temp2 = new Netflix();
                    net_temp2.fromByteArray(ba2);
                    

                    if (net_temp1.getName().compareTo(net_temp2.getName()) > 0) {
                        byte[] ba3;
                        ba3 = net_temp1.toByteArray();
                        arq3.writeChar(' ');
                        arq3.writeInt(ba3.length);
                        arq3.write(ba3);
                        arq2.seek(pos2);
                        j++;
                    } else {
                        byte[] ba3;
                        ba3 = net_temp2.toByteArray();
                        arq3.writeChar(' ');
                        arq3.writeInt(ba3.length);
                        arq3.write(ba3);
                        arq1.seek(pos1);
                        p++;
                    }
                    k++;
                }

            } else {

                while (j != limite1 && p != limite2) {
                    pos1 =  arq1.getFilePointer();
                    char c = arq1.readChar();
                    int tamanho = arq1.readInt();
                    ba = new byte[tamanho];
                    arq1.read(ba);
                    Netflix net_temp1 = new Netflix();
                    net_temp1.fromByteArray(ba);


                    pos2 =  arq2.getFilePointer();
                    char c2 = arq2.readChar();
                    int tamanho2 = arq2.readInt();

                    ba2 = new byte[tamanho2];
                    arq2.read(ba2);
                    Netflix net_temp2 = new Netflix();
                    net_temp2.fromByteArray(ba2);

                    if (net_temp1.getName().compareTo(net_temp2.getName()) > 0) {
                        byte[] ba3;
                        ba3 = net_temp1.toByteArray();
                        arq4.writeChar(' ');
                        arq4.writeInt(ba3.length);
                        arq4.write(ba3);
                        arq2.seek(pos2);
                        j++;
                    } else {
                        byte[] ba3;
                        ba3 = net_temp2.toByteArray();
                        arq4.writeChar(' ');
                        arq4.writeInt(ba3.length);
                        arq4.write(ba3);
                        arq1.seek(pos1);
                        p++;
                    }
                    k++;
                }

            }
            // caso algum arquivo for maior que o outro irá terminar de escrever
            if (bloco % 2 == 0) {
                while (j != limite1) {
                    char c = arq1.readChar();
                    int tamanho = arq1.readInt();
                    ba = new byte[tamanho];
                    arq1.read(ba);
                    arq3.writeChar(' ');
                    arq3.writeInt(ba.length);
                    arq3.write(ba);
                    
                    j++;
                    k++;

                }
               
                while (p != limite2) {
                    char c = arq2.readChar();
                    int tamanho = arq2.readInt();
                    ba = new byte[tamanho];
                    arq2.read(ba);
                    arq3.writeChar(' ');
                    arq3.writeInt(ba.length);
                    arq3.write(ba);
                    
                    p++;
                    k++;

                }
            } else {
                while (j != limite1) {
                    char c = arq1.readChar();
                    int tamanho = arq1.readInt();
                    ba = new byte[tamanho];
                    arq1.read(ba);
                    arq4.writeChar(' ');
                    arq4.writeInt(ba.length);
                    arq4.write(ba);
                    j++;
                    k++;

                }

                while (p != limite2) {
                    char c = arq2.readChar();
                    int tamanho = arq2.readInt();
                    ba = new byte[tamanho];
                    arq2.read(ba);
                    arq4.writeChar(' ');
                    arq4.writeInt(ba.length);
                    arq4.write(ba);
                    p++;
                    k++;

                }
            }

            bloco++;

            System.out.println(bloco);
        }

        y = y + y;
        arq.close();
        arq1.close();
        arq2.close();
        arq3.close();
        arq4.close();
    }

    public void intercalacaoBlocosTamanhoVariavel() {
        y = y + y;
    }

    public void intercalacaoSelecaoPorSubstituicao() {
        y = y + y;
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
