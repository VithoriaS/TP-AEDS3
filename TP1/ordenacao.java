import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class ordenacao {
    int y = 0;
    

    public void LoopOrdenacao() throws IOException {
        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        int len = 0;
        len = arq.readInt();
        
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
        int i = 0;
        switch (x) {
            case 1:
                while (y < len) {

                    if (i % 2 == 0) {
                        intercalacaoComum2("temp1.db", "temp2.db", "temp3.db", "temp4.db");
                    } else {
                        intercalacaoComum2("temp3.db", "temp4.db", "temp1.db", "temp2.db");
                    }

                    i++;
                }

                if (i % 2 == 0) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    RandomAccessFile arq1 = new RandomAccessFile("temp1.db", "rw");
                    // Ler os dados do arquivo de origem e escrevê-los no arquivo de destino.
                    arq.setLength(0);
                    while ((bytesRead = arq1.read(buffer)) != -1) {
                        arq.write(buffer, 0, bytesRead);
                    }
                    arq1.close();
                    arq.close();
                    apagarRegistroOrd("temp1.db");
                    apagarRegistroOrd("temp2.db");
                    apagarRegistroOrd("temp3.db");
                    apagarRegistroOrd("temp4.db");
                }
                else{
                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    RandomAccessFile arq2 = new RandomAccessFile("temp3.db", "rw");
                    // Ler os dados do arquivo de origem e escrevê-los no arquivo de destino.
                    arq.setLength(0);
                    while ((bytesRead = arq2.read(buffer)) != -1) {
                        arq.write(buffer, 0, bytesRead);
                    }
                    arq2.close();
                    arq.close();
                    apagarRegistroOrd("temp1.db");
                    apagarRegistroOrd("temp2.db");
                    apagarRegistroOrd("temp3.db");
                    apagarRegistroOrd("temp4.db");
                }

                break;
            case 2:

            while (y < len) {

                if (i % 2 == 0) {
                    intercalacaoBlocosTamanhoVariavel("temp1.db", "temp2.db", "temp3.db", "temp4.db");
                } else {
                    intercalacaoBlocosTamanhoVariavel("temp3.db", "temp4.db", "temp1.db", "temp2.db");
                }

                i++;
            };
                break;
            case 3:
                intercalacaoSelecaoPorSubstituicao();
                break;
            default:
                break;
        }
        
    }

    /*
     * Pré intercalacao, fazendo a intercalacao apenas de um arquivo.
     */
    public void ordencaoMemoriaPrimaria() throws IOException {

        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        RandomAccessFile arq1 = new RandomAccessFile("temp1.db", "rw");
        RandomAccessFile arq2 = new RandomAccessFile("temp2.db", "rw");

        byte ba[];
        long pos;
        int len = 0;
        len = arq.readInt();
        arq1.writeInt(-1);
        arq2.writeInt(-1);
      
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
            int salvacao = 0;
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
                    salvacao++;
                }

                k++;
            }

            paraOWhile = Netarray[Netarray.length - 1 - (salvacao)];
            ordernarLista(Netarray, 0, (Netarray.length - 1) - (salvacao));
            
            // colocando todos os 4 dados
            for (int i = 0; i < Netarray.length; i++) {
                byte[] ba2;
                if (Netarray[i] == null) {
                    i++;
                }
                ba2 = Netarray[i].toByteArray();
                if (j % 2 == 0) {
                    arq1.writeChar(' ');
                    arq1.writeInt(ba2.length);
                    arq1.write(ba2);
                    
                } else {
                    arq2.writeChar(' ');
                    arq2.writeInt(ba2.length);
                    arq2.write(ba2);
                    
                }
            }

            // BLOCO
            j++;

        }
     
        arq.close();
        arq1.close();
        arq2.close();

    }

    public void intercalacaoComum2(String R1, String R2, String W1, String W2) throws IOException {

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
        arq3.writeInt(-1);
        arq4.writeInt(-1);
        arq1.skipBytes(4);
        arq2.skipBytes(4);

        int len = 0;
        len = arq.readInt();

        int bloco = 0;
   
        int j = 0;
        int p = 0;
        long pos1 = 0;
        long pos2 = 0;
        
      
        int numeroLoco = 0;
        numeroLoco = len / y;
        if (len % y > 0) {
            numeroLoco++;
        }
        
        //System.out.println(y);
        
         while (numeroLoco > bloco) {

            j = 0;
            p = 0;
            
            if (bloco % 2 == 0) {

                while (j != y && p != y &&  arq2.getFilePointer() != arq2.length() && arq1.getFilePointer() != arq1.length()) {

                    pos1 = arq1.getFilePointer();
                    Netflix net_temp1 = readOrd(arq1);
                    pos2 = arq2.getFilePointer();
                    Netflix net_temp2 = readOrd(arq2);

                    if (net_temp1.getName().compareTo(net_temp2.getName()) < 0) {
                  
                        creatOrd(arq3, net_temp1);
                        arq2.seek(pos2);
                        j++;
                        
                    } else {
                        creatOrd(arq3, net_temp2);
                        arq1.seek(pos1);
                        p++;
                       
                    }

                }

            } else {

                while (j != y && p != y && arq2.getFilePointer() != arq2.length() && arq1.getFilePointer() != arq1.length()) {
                   
                    pos1 = arq1.getFilePointer();
                    Netflix net_temp1 = readOrd(arq1);
                    pos2 = arq2.getFilePointer();
                    Netflix net_temp2 = readOrd(arq2);

                    if (net_temp1.getName().compareTo(net_temp2.getName()) < 0) {
                        
                        
                        creatOrd(arq4, net_temp1);
                        
                        arq2.seek(pos2);
                       
                        j++;
                    } else {
                       
                        creatOrd(arq4, net_temp2);
                        arq1.seek(pos1);
                        
                       
                        p++;
                    }

                }

            }
            // caso algum arquivo for maior que o outro irá terminar de escrever
            if (bloco % 2 == 0) {
                while (j != y  && arq1.getFilePointer() != arq1.length()) {
                
                    Netflix net_temp1 = readOrd(arq1);
                    creatOrd(arq3, net_temp1);
                    j++;
                  

                }
                
                while (p != y &&  arq2.getFilePointer() != arq2.length()) {
                  
                    Netflix net_temp1 = readOrd(arq2);
        
                    creatOrd(arq3, net_temp1);
                    p++;
                   
                }
            } else {
                while (j != y && arq1.getFilePointer() != arq1.length()) {
                   
                    Netflix net_temp1 = readOrd(arq1);
                    creatOrd(arq4, net_temp1);
                    j++;
                   

                }

                while (p != y  && arq2.getFilePointer() != arq2.length()) {
                   
                    Netflix net_temp1 = readOrd(arq2);
                    creatOrd(arq4, net_temp1);
                    p++;
                    
                }
            }

            bloco++;

            //System.out.println(bloco);
        }

        y = y + y;
       
        arq.close();
        arq1.close();
        arq2.close();
        arq3.close();
        arq4.close();
    }

    public void intercalacaoBlocosTamanhoVariavel(String R1, String R2, String W1, String W2) throws IOException {

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
        arq3.writeInt(-1);
        arq4.writeInt(-1);
        arq1.skipBytes(4);
        arq2.skipBytes(4);

        int len = 0;
        len = arq.readInt();

        int bloco = 0;
   
        int j = 0;
        int p = 0;
        long pos1 = 0;
        long pos2 = 0;
        
      
        int numeroLoco = 0;
        numeroLoco = len / y;
        if (len % y > 0) {
            numeroLoco++;
        }
        
        System.out.println(y);
        
         while (numeroLoco > bloco) {

            j = 0;
            p = 0;
            
            if (bloco % 2 == 0) {
                Netflix net_temp1 = new Netflix();
                Netflix net_temp2 = new Netflix();
                boolean x1 = false;
                boolean x2 = false;
                while (j != y && p != y &&  arq2.getFilePointer() != arq2.length() && arq1.getFilePointer() != arq1.length()) {

                    pos1 = arq1.getFilePointer();
                    Netflix net_temp3 = readOrd(arq1);
                    pos2 = arq2.getFilePointer();
                    Netflix net_temp4 = readOrd(arq2);


                    if (net_temp1.getName().compareTo(net_temp3.getName()) > 0) {
                        x1 = false;
                    }
                    else{
                        x1 = true;
                    }



                    if (net_temp1.getName().compareTo(net_temp2.getName()) < 0 && x1 == true) {
                  
                        creatOrd(arq3, net_temp3);
                        net_temp1 = net_temp3;
                        arq2.seek(pos2);
                        j++;
                        
                    } else if (net_temp1.getName().compareTo(net_temp2.getName()) > 0  && x2 == true) {
                        creatOrd(arq3, net_temp4);
                        net_temp2 = net_temp4;
                        arq1.seek(pos1);
                        p++;
                       
                    }



                }

            } else {
                Netflix net_temp1 = new Netflix();
                Netflix net_temp2 = new Netflix();
                while (j != y && p != y && arq2.getFilePointer() != arq2.length() && arq1.getFilePointer() != arq1.length()) {
                   
                    pos1 = arq1.getFilePointer();
                     net_temp1 = readOrd(arq1);
                    pos2 = arq2.getFilePointer();
                     net_temp2 = readOrd(arq2);

                    if (net_temp1.getName().compareTo(net_temp2.getName()) < 0) {
                        
                        
                        creatOrd(arq4, net_temp1);
                        
                        arq2.seek(pos2);
                       
                        j++;
                    } else {
                       
                        creatOrd(arq4, net_temp2);
                        arq1.seek(pos1);
                        
                       
                        p++;
                    }

                }

            }
            // caso algum arquivo for maior que o outro irá terminar de escrever
            if (bloco % 2 == 0) {
                while (j != y  && arq1.getFilePointer() != arq1.length()) {
                
                    Netflix net_temp1 = readOrd(arq1);
                    creatOrd(arq3, net_temp1);
                    j++;
                  

                }
                
                while (p != y &&  arq2.getFilePointer() != arq2.length()) {
                  
                    Netflix net_temp1 = readOrd(arq2);
        
                    creatOrd(arq3, net_temp1);
                    p++;
                   
                }
            } else {
                while (j != y && arq1.getFilePointer() != arq1.length()) {
                   
                    Netflix net_temp1 = readOrd(arq1);
                    creatOrd(arq4, net_temp1);
                    j++;
                   

                }

                while (p != y  && arq2.getFilePointer() != arq2.length()) {
                   
                    Netflix net_temp1 = readOrd(arq2);
                    creatOrd(arq4, net_temp1);
                    p++;
                    
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

    public void intercalacaoSelecaoPorSubstituicao() {
        y = y + y;
    }

    /**
     * Recebe um array de objetos Netflix e dois índices "x" e "y" para serem trocados.
       @return Retorna o array com os objetos trocados.
     */
    public Netflix[] swap(Netflix netArr[], int x, int y) {
        Netflix temp = netArr[x];
        netArr[x] = netArr[y];
        netArr[y] = temp;
        return netArr;
    }

    /*
     * Ordenacao Quick Sort
     */
    void ordernarLista(Netflix netArr[], int esq, int dir) {
        if (esq < dir) {
            int pi = particao(netArr, esq, dir);

            ordernarLista(netArr, esq, pi - 1);
            ordernarLista(netArr, pi + 1, dir);
        }
    }
    /*
        * Faz a reparticao do Quick Sort
        */
    int particao(Netflix netArr[], int esq, int dir) {
        Netflix pivo = netArr[dir];
        if (pivo == null) {
            pivo = netArr[dir + 1];
        }

        int i = (esq - 1);

        for (int j = esq; j < dir; j++) {

            if (netArr[j] == null) {
                j++;
            }

            if (pivo.getName().compareTo(netArr[j].getName()) > 0) {

                i++;
                netArr = swap(netArr, i, j);
            }

        }

        netArr = swap(netArr, dir, i + 1);

        return (i + 1);
    }

    /**
     * Função que cria um novo registro no arquivo "teste.db" com os dados do objeto Netflix fornecido,
     * @param arq o arquivo "teste.db" aberto para escrita
     * @param net_temp1 o objeto Netflix contendo os dados a serem inseridos no arquivo
    */
    public static void creatOrd(RandomAccessFile arq, Netflix net_temp1) throws IOException {
        byte[] ba3;
        ba3 = net_temp1.toByteArray();
        arq.writeChar(' ');
        arq.writeInt(ba3.length);
        arq.write(ba3);
        long posTemp = arq.getFilePointer();
        arq.seek(0);
        arq.writeInt(net_temp1.getId());
        arq.seek(posTemp);

    }

    /*
     * A função "readOrd" lê um registro do arquivo "teste.db" na 
     * ordem em que foi inserido e o retorna como um objeto da classe "Netflix".
     */
    public static Netflix readOrd(RandomAccessFile arq1 ) throws IOException {

        byte[] ba;
        char c = arq1.readChar();
        int tamanho = arq1.readInt();
        ba = new byte[tamanho];
        arq1.read(ba);
        Netflix net_temp1 = new Netflix();
        net_temp1.fromByteArray(ba);

        return net_temp1;

    }

    public static void apagarRegistroOrd(String FilePath)
    {
        
            try {
                // Crie uma instância do objeto File com o caminho e nome do arquivo
                File arquivo = new File(FilePath);
    
                // Verifique se o arquivo existe antes de tentar excluir
                if (arquivo.exists()) {
                    // Crie uma instância do objeto RandomAccessFile
                    RandomAccessFile raf = new RandomAccessFile(arquivo, "rw");
    
                    // ... faça algo com o arquivo ...
    
                    // Feche o objeto RandomAccessFile
                    raf.close();
    
                    // Exclua o arquivo usando o método delete()
                    if (arquivo.delete()) {
                        System.out.println("Arquivo excluído com sucesso!");
                    } else {
                        System.out.println("Não foi possível excluir o arquivo.");
                    }
                } else {
                    System.out.println("O arquivo não existe.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        
    }


}