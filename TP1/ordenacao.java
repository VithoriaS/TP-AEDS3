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
        Scanner sc = new Scanner(System.in);
        System.out.println("Qual Valor De Y");
        y = sc.nextInt();
        ordencaoMemoriaPrimaria();
        int length = 0;
        int x = 0;
        while (y < length) {

            System.out.println("\nOpcoes ");
            System.out.println(" 1 - Comum");
            System.out.println(" 2 - BlocosTamanhoVariavel");
            System.out.println(" 3 - SelecaoPorSubstituicao");
            System.out.println("Entrar com uma opcao:");
            x = sc.nextInt();

            switch (x) {
                case 1:
                    intercalacaoComum();
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
        }
    }

    public void ordencaoMemoriaPrimaria() throws IOException {

        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        RandomAccessFile arq1 = new RandomAccessFile("temp1.db", "rw");
        RandomAccessFile arq2 = new RandomAccessFile("temp2.db", "rw");
        RandomAccessFile arq3 = new RandomAccessFile("temp3.db", "rw");
        RandomAccessFile arq4 = new RandomAccessFile("temp4.db", "rw");
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
        arq3.close();
        arq4.close();

    }

    public void intercalacaoComum() throws IOException {

        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        RandomAccessFile arq1 = new RandomAccessFile("temp1.db", "rw");
        RandomAccessFile arq2 = new RandomAccessFile("temp2.db", "rw");
        RandomAccessFile arq3 = new RandomAccessFile("temp3.db", "rw");
        RandomAccessFile arq4 = new RandomAccessFile("temp4.db", "rw");

        int len = 0;
        len = arq.readInt();

        // boolean par = len1 % 2 == 0;
        int bloco = 0;
        int k = 0;
        byte ba[];
        int j = 0;
        int p = 0;
        Netflix paraOWhile = new Netflix();
        Netflix[] Array1 = new Netflix[0];
        Netflix[] Array2 = new Netflix[0];
        int numeroLoco = 0;
        numeroLoco = len / y;
        if (len % y > 0) {
            numeroLoco++;
        }
        System.out.println(numeroLoco);
        
        while (numeroLoco > bloco) {

            j = 0;
            p = 0;
            if (len - k < y) {
                Array1 = new Netflix[len - k];
                Array2 = new Netflix[0];

            } else {
                Array1 = new Netflix[y];

                if (len - k < y) {
                    Array2 = new Netflix[len - k];
                } else {
                    Array2 = new Netflix[y];

                }
            }

            for (int i = 0; i < Array1.length; i++) {
                char c = arq1.readChar();
                int tamanho = arq1.readInt();

                if (c != '*') {

                    ba = new byte[tamanho];
                    arq1.read(ba);
                    Netflix net_temp = new Netflix();
                    net_temp.fromByteArray(ba);
                    Array1[i] = net_temp;
                } else {
                    arq1.skipBytes(tamanho);
                }

                k++;
            }

            for (int i = 0; i < Array2.length; i++) {
                char c = arq2.readChar();
                int tamanho1 = arq2.readInt();

                if (c != '*') {

                    ba = new byte[tamanho1];
                    arq2.read(ba);
                    Netflix net_temp = new Netflix();
                    net_temp.fromByteArray(ba);
                    Array2[i] = net_temp;
                } else {
                    arq2.skipBytes(tamanho1);
                }

                k++;
            }

            if (Array2.length == 0) {
                if (bloco % 2 == 0) {
                    for (int i = 0; i < Array1.length; i++) {
                        byte[] ba2;
                        ba2 = Array1[i].toByteArray();
                        arq3.writeChar(' ');
                        arq3.writeInt(ba2.length);
                        arq3.write(ba2);
                    }
                } else {
                    for (int i = 0; i < Array1.length; i++) {
                        byte[] ba2;
                        ba2 = Array1[i].toByteArray();
                        arq4.writeChar(' ');
                        arq4.writeInt(ba2.length);
                        arq4.write(ba2);
                    }
                }
                bloco++;
            } else {

                if (bloco % 2 == 0) {

                    while (j != Array1.length && p != Array2.length) {
                        if (Array1[j].getName().compareTo(Array2[p].getName()) > 0) {
                            byte[] ba2;
                            ba2 = Array1[j].toByteArray();
                            arq3.writeChar(' ');
                            arq3.writeInt(ba2.length);
                            arq3.write(ba2);
                            j++;
                        } else {
                            byte[] ba2;
                            ba2 = Array2[p].toByteArray();
                            arq3.writeChar(' ');
                            arq3.writeInt(ba2.length);
                            arq3.write(ba2);
                            p++;
                        }
                    }

                } else {

                    while (j != Array1.length && p != Array2.length) {
                        if (Array1[j].getName().compareTo(Array2[p].getName()) > 0) {
                            byte[] ba2;
                            ba2 = Array1[j].toByteArray();
                            arq4.writeChar(' ');
                            arq4.writeInt(ba2.length);
                            arq4.write(ba2);
                            j++;
                        } else {
                            byte[] ba2;
                            ba2 = Array2[p].toByteArray();
                            arq4.writeChar(' ');
                            arq4.writeInt(ba2.length);
                            arq4.write(ba2);
                            p++;
                        }

                    }

                }
                // caso algum arquivo for maior que o outro irá terminar de escrever
                if (bloco % 2 == 0) {
                    while (j != Array1.length) {
                        byte[] ba2;
                        ba2 = Array1[j].toByteArray();
                        arq3.writeChar(' ');
                        arq3.writeInt(ba2.length);
                        arq3.write(ba2);
                        j++;
                    }

                    while (p != Array2.length) {
                        byte[] ba2;
                        ba2 = Array2[p].toByteArray();
                        arq3.writeChar(' ');
                        arq3.writeInt(ba2.length);
                        arq3.write(ba2);
                        p++;
                    }
                } else {
                    while (j != Array1.length) {
                        byte[] ba2;
                        ba2 = Array1[j].toByteArray();
                        arq4.writeChar(' ');
                        arq4.writeInt(ba2.length);
                        arq4.write(ba2);
                        j++;
                    }

                    while (p != Array2.length) {
                        byte[] ba2;
                        ba2 = Array2[p].toByteArray();
                        arq4.writeChar(' ');
                        arq4.writeInt(ba2.length);
                        arq4.write(ba2);
                        p++;
                    }
                }

            }
            bloco++;

            System.out.println(bloco);
        }

        y = y + y;
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
