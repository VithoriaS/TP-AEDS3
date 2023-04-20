import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class HashExtensivel {
   
    String           nomeArqDiretorio;
    String           nomeArqCestos;
    RandomAccessFile arqDiretório;
    RandomAccessFile arqCestos;
    int              qtdCesto;
    Diretório        diretório;
    Crud c;
    
    class Cesto {
        byte   profundidadeLocal;   // profundidade local do cesto
        short  quantidade;          // quantidade de pares presentes no cesto
        short  quantidadeMaxima;    // quantidade máxima de pares que o cesto pode conter
        int[]  chaves;              // sequência de chaves armazenadas no cesto
        long[] dados;               // sequência de dados correspondentes às chaves
        short  bytesPorPar;         // size fixo de cada par de chave/dado em bytes
        short  bytesPorCesto;       // size fixo do cesto em bytes


        public Cesto(int qtdmax) throws Exception {
            this(qtdmax, 0);
        }


        public Cesto(int qtdmax, int pl) throws Exception {
            if(qtdmax>8900)
                throw new Exception("Quantidade máxima de 32.767 elementos");
            if(pl>127)
                throw new Exception("Profundidade local máxima de 127 bits");
            profundidadeLocal = (byte)pl;
            quantidade = 0;
            quantidadeMaxima = (short)qtdmax;
            chaves = new int[quantidadeMaxima];
            dados =new long[quantidadeMaxima];
            bytesPorPar = 12;  // int + long
            bytesPorCesto = (short)(bytesPorPar * quantidadeMaxima + 3);
        }

        /**
         * Transforma a estrutura de dados em um array de bytes.
         * @return o array de bytes que representa a estrutura de dados.
         * @throws IOException se ocorrer um erro de I/O durante a conversão.
         */
        public byte[] toByteArray() throws IOException {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            dos.writeByte(profundidadeLocal);
            dos.writeShort(quantidade);
            int i=0;
            while(i<quantidade) {
                dos.writeInt(chaves[i]);
                dos.writeLong(dados[i]);
                i++;
            }
            while(i<quantidadeMaxima) {
                dos.writeInt(0);
                dos.writeLong(0);
                i++;
            }
            return baos.toByteArray();    
        }

        
        /**
         * Carrega os dados da estrutura de dados a partir de um array de bytes.
         * @param ba o array de bytes que representa a estrutura de dados.
         * @throws IOException se ocorrer um erro de I/O durante a conversão.
         */
        
        public void fromByteArray(byte[] ba) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            DataInputStream dis = new DataInputStream(bais);
            profundidadeLocal = dis.readByte();
            quantidade = dis.readShort();
            int i=0;
            while(i<quantidadeMaxima) {
                chaves[i] = dis.readInt();
                dados[i] = dis.readLong();
                i++;
            }

             
        }

        /**
         * Insere um novo par chave-valor ordenado na estrutura de dados.
         * @param c A chave do novo elemento.
         * @param d O endereco associado do novo elemento.
         * @return true se a inserção foi bem-sucedida, false caso contrário.
        */
        public boolean create(int c, long d) {
            if(full())
                return false;
            int i=quantidade-1;
            while(i>=0 && c<chaves[i]) {
                chaves[i+1] = chaves[i];
                dados[i+1] = dados[i];
                i--;
            }
            i++;
            chaves[i] = c;
            dados[i] = d;
            quantidade++;
            return true;
        }

        /**
         * Retorna o valor associado a uma chave na estrutura de dados.
         * @param c A chave do elemento a ser buscado.
         * @return O valor associado à chave buscada, ou -1 se a chave não foi encontrada.
        */
        public long read(int c) {
            if(empty())
                return -1;
            int i=0;
            while(i<quantidade && c>chaves[i])
                i++;
            if(i<quantidade && c==chaves[i])
                return dados[i];
            else
                return -1;        
        }

        /**
         * Atualiza o valor associado a uma chave na estrutura de dados.
         * @param c A chave do elemento a ser atualizado.
         * @param d O novo endereço associado à chave do elemento.
         * @return true se a atualização foi bem-sucedida, false caso contrário.
        */
        public boolean update(int c, long d) {
            if(empty())
                return false;
            int i=0;
            while(i<quantidade && c>chaves[i])
                i++;
            if(i<quantidade && c==chaves[i]) {
                //MUDAR PARA OBEJTO NETFLIX
                dados[i] = d;
                return true;
            }
            else
                return false;        
        }

        /**
         * Remove um elemento da estrutura de dados.
         * @param c A chave do elemento a ser removido.
         * @return true se a remoção foi bem-sucedida, false caso contrário.
        */
        public boolean delete(int c) {
            if(empty())
                return false;
            int i=0;
            while(i<quantidade && c>chaves[i])
                i++;
            if(c==chaves[i]) {
                while(i<quantidade-1) {
                    chaves[i] = chaves[i+1];
                    dados[i] = dados[i+1];
                    i++;
                }
                quantidade--;
                return true;
            }
            else
                return false;        
        }

        /**
         * Verifica se a estrutura de dados está vazia.
         * @return true se a estrutura está vazia, false caso contrário.
         */

        public boolean empty() {
            return quantidade == 0;
        }
        /**
         * Verifica se a estrutura de dados está cheia.
         * @return true se a estrutura está cheia, false caso contrário.
         */
        public boolean full() {
            return quantidade == quantidadeMaxima;
        }

        public String toString() {
            String s = "\nProfundidade Local: "+profundidadeLocal+
                       "\nQuantidade: "+quantidade+
                       "\n| ";
            int i=0;
            while(i<quantidade) {
                s += chaves[i] + ";" + dados[i] + " | ";
                i++;
            }
            while(i<quantidadeMaxima) {
                s += "-;- | ";
                i++;
            }
            return s;
        }

        public int size() {
            return bytesPorCesto;
        }

    }

    class Diretório {

        byte   profundidadeGlobal;
        long[] endereços;
        Crud c;

        public Diretório() {
            profundidadeGlobal = 0;
            endereços = new long[1];
            endereços[0] = 0;
        }


        /**
         * Função para recalcular a profundidade
         */
        public boolean atualizaEndereco(int p, long e) {
            if(p>Math.pow(2,profundidadeGlobal))
                return false;
            endereços[p] = e;
            return true;
        }

        /**
         * Transforma a estrutura de dados em um array de bytes.
         * @return o array de bytes que representa a estrutura de dados.
         * @throws IOException se ocorrer um erro de I/O durante a conversão.
         */
        public byte[] toByteArray() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            dos.writeByte(profundidadeGlobal);
            int quantidade = (int)Math.pow(2,profundidadeGlobal);
            int i=0;
            while(i<quantidade) {
                dos.writeLong(endereços[i]);
                i++;
            }
            return baos.toByteArray();            
        }

        /**
         * Carrega os dados da estrutura de dados a partir de um array de bytes.
         * @param ba o array de bytes que representa a estrutura de dados.
         * @throws IOException se ocorrer um erro de I/O durante a conversão.
         */
        public void fromByteArray(byte[] ba) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            DataInputStream dis = new DataInputStream(bais);
            profundidadeGlobal = dis.readByte();
            int quantidade = (int)Math.pow(2,profundidadeGlobal);
            endereços = new long[quantidade];
            int i=0;
            while(i<quantidade) {
                endereços[i] = dis.readLong();
                i++;
            }
        }

        public String toString() {
            String s = "\nProfundidade global: "+profundidadeGlobal;
            int i=0;
            int quantidade = (int)Math.pow(2,profundidadeGlobal);
            while(i<quantidade) {
                s += "\n" + i + ": " + endereços[i];
                i++;
            }
            return s;
        }

        protected long endereço(int p) {
            if(p>Math.pow(2,profundidadeGlobal))
                return -1;
            return endereços[p];
        }

        /**
        *Duplica a tabela de endereços, aumentando a profundidade global em 1 e atualizando
        *os novos endereços. Retorna true se a duplicação foi bem sucedida, false caso contrário.
        *Se a profundidade global já for 127, não é possível realizar a duplicação.
        *@return true se a duplicação foi bem sucedida, false caso contrário.
        */
        protected boolean duplica() {
            if(profundidadeGlobal==127)
                return false;
            profundidadeGlobal++;
            int q1 = (int)Math.pow(2,profundidadeGlobal-1);
            int q2 = (int)Math.pow(2,profundidadeGlobal);
            long[] novosEnderecos = new long[q2];
            int i=0;
            while(i<q1) {
                novosEnderecos[i]=endereços[i];
                i++;
            }
            while(i<q2) {
                novosEnderecos[i]=endereços[i-q1];
                i++;
            }
            endereços = novosEnderecos;
            return true;
        }

        /**
         * Calcula o valor de hash de uma chave para a tabela de hash principal, usando a profundidade global.
         * @param chave A chave para a qual o valor de hash será calculado.
         * @return O valor de hash para a chave.
        */
        protected int hash(int chave) {
            return chave % (int)Math.pow(2, profundidadeGlobal);
        }

        /**
         * Calcula o valor de hash de uma chave para uma tabela de hash específica, usando a profundidade local.
         * @param chave A chave para a qual o valor de hash será calculado.
         * @param pl A profundidade local da tabela de hash.
         * @return O valor de hash para a chave.
        */
        protected int hash2(int chave, int pl) { // cálculo do hash para uma dada profundidade local
            return chave % (int)Math.pow(2, pl);            
        }

    }
    
    
    /**
     * @param Arq1 é o nome do arquivo que armazena o diretório.
     * @param Arq2 é o nome do arquivo que armazena os cestos.
    */
    public HashExtensivel(int n, String Arq1, String Arq2 ) throws Exception {
        qtdCesto = n;
        nomeArqDiretorio = Arq1;
        nomeArqCestos = Arq2;
        c = new Crud();
        
        
        arqDiretório = new RandomAccessFile(nomeArqDiretorio,"rw");
        arqCestos = new RandomAccessFile(nomeArqCestos,"rw");

        // Se o diretório ou os cestos estiverem vazios, cria um novo diretório e lista de cestos
        if(arqDiretório.length()==0 || arqCestos.length()==0) {

            // Cria um novo diretório, com profundidade de 0 bits (1 único elemento)
            diretório = new Diretório();
            byte[] bd = diretório.toByteArray();
            arqDiretório.write(bd);
            
            // Cria um cesto vazio, já apontado pelo único elemento do diretório
            Cesto c = new Cesto(qtdCesto);
            bd = c.toByteArray();
            arqCestos.seek(0);
            arqCestos.write(bd);
        }
    }

    /**
     * É responsável por criar um novo registro na tabela hash para a instância Netflix 
     * que é obtida por meio do método preCreate() da instância do objeto Crud.
     * @throws Exception
     */
    public void createHash() throws Exception {
        Netflix net = c.preCreate();
        long pos = c.createPos(net);
        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        int id = arq.readInt();
        arq.close();
        create(id, pos);
        
    }

    /**
     * É responsável por criar um novo registro na tabela hash para a instância Netflix fornecida.
     * @param net objetos com os dados a serem inseridos 
     * @throws Exception
     */
    public void createHash(Netflix net) throws Exception {
        
        long pos = c.createPos(net);
        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        int id = arq.readInt();
        arq.close();
        create(id, pos);
        
    }
    
    /**
    *Cria uma entrada no hash table com a chave e o dado fornecidos, de acordo com o algoritmo de hashing
    *e tratamento de colisões por encadeamento utilizado. A função lê o diretório, identifica a hash apropriada,
    *recupera o cesto correspondente e realiza as operações necessárias para criar a entrada.
    *Se a chave já existe no hash table, a função lança uma exceção.
    *@param chave A chave da entrada a ser criada
    *@param dado O dado associado à chave na entrada a ser criada
    *@return true se a entrada foi criada com sucesso, false caso contrário (o cesto correspondente está cheio e não pôde ser duplicado)
    *@throws Exception se a chave já existe no hash table
    */
    public boolean create(int chave, long dado) throws Exception {
        
        //Carrega o diretório
        byte[] bd = new byte[(int)arqDiretório.length()];
        arqDiretório.seek(0);
        arqDiretório.read(bd);
        diretório = new Diretório();
        diretório.fromByteArray(bd);        
        
        // Identifica a hash do diretório,
        int i = diretório.hash(chave);
        
        // Recupera o cesto
        long endereçoCesto = diretório.endereço(i);
        Cesto c = new Cesto(qtdCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(endereçoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);
        
        // Testa se a chave já não existe no cesto
        if(c.read(chave)!= -1)
            throw new Exception("Chave já existe");     

        // Testa se o cesto já não está cheio
        // Se não estiver, create o par de chave e dado
        if(!c.full()) {
            // Insere a chave no cesto e o atualiza 
            c.create(chave, dado);
            arqCestos.seek(endereçoCesto);
            arqCestos.write(c.toByteArray());
            return true;        
        }
        
        // Duplica o diretório
        byte pl = c.profundidadeLocal;
        if(pl>=diretório.profundidadeGlobal)
            diretório.duplica();
        byte pg = diretório.profundidadeGlobal;

        // Cria os novos cestos, com os seus dados no arquivo de cestos
        Cesto c1 = new Cesto(qtdCesto, pl+1);
        arqCestos.seek(endereçoCesto);
        arqCestos.write(c1.toByteArray());

        Cesto c2 = new Cesto(qtdCesto, pl+1);
        long novoEndereço = arqCestos.length();
        arqCestos.seek(novoEndereço);
        arqCestos.write(c2.toByteArray());
        
        // Atualiza os dados no diretório
        int inicio = diretório.hash2(chave, c.profundidadeLocal);
        int deslocamento = (int)Math.pow(2,pl);
        int max = (int)Math.pow(2,pg);
        boolean troca = false;
        for(int j=inicio; j<max; j+=deslocamento) {
            if(troca)
                diretório.atualizaEndereco(j,novoEndereço);
            troca=!troca;
        }
        
        // Atualiza o arquivo do diretório
        bd = diretório.toByteArray();
        arqDiretório.seek(0);
        arqDiretório.write(bd);
        
        // Reinsere as chaves
        for(int j=0; j<c.quantidade; j++) {
            create(c.chaves[j], c.dados[j]);
        }
        create(chave,dado);
        return false;   

    }
    
    /**
     * Lê o dado associado a uma determinada chave.
     * @param chave a chave para a qual o dado deve ser lido.
     * @return o dado associado à chave ou -1 se a chave não existir no arquivo.
     * @throws Exception se ocorrer um erro ao ler o arquivo.
    */
    public long read(int chave) throws Exception {
        
        //Carrega o diretório
        byte[] bd = new byte[(int)arqDiretório.length()];
        arqDiretório.seek(0);
        arqDiretório.read(bd);
        diretório = new Diretório();
        diretório.fromByteArray(bd);        
        
        // Identifica a hash do diretório,
        int i = diretório.hash(chave);
        
        // Recupera o cesto
        long endereçoCesto = diretório.endereço(i);
        Cesto c = new Cesto(qtdCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(endereçoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);
        
        return c.read(chave);
    }

    /**
    Lê o valor da chave especificada na tabela hash e recupera 
    o registro correspondente no arquivo de dados.
    Se o registro existe, imprime as informações na tela.
    Caso contrário, imprime uma mensagem de erro.
    @param chave a chave a ser buscada na tabela hash
    @throws Exception se ocorrer um erro durante a leitura do arquivo
    */
    public void readHash(int chave) throws Exception
    {

        long pos = read(chave);

        if (pos == -1) {
            System.out.println("Chave não existe");
        } else {
            RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
            arq.seek(pos);
            char c = arq.readChar();
        
            int length = arq.readInt();
            byte[] ba = new byte[length];
            arq.read(ba);
            Netflix net_temp = new Netflix();
            net_temp.fromByteArray(ba);
            net_temp.printar();
          

            arq.close();
        }

    }

    
    /**
     * Atualiza o registro com a chave especificada na tabela hash,
     * atualizando o registro no cesto correspondente.
     * Se o registro já existir no cesto, atualiza diretamente nele.
     * Se o registro não existir no cesto, apaga o registro antigo e insere um novo.
     *
     * @param chave a chave do registro a ser atualizado
     * @param pos a posição do registro a ser atualizado no arquivo
     * @throws Exception se ocorrer algum erro durante a atualização
     */
    public void updateHash(int chave, long pos) throws Exception{
        
            RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
            arq.seek(pos);
            char cha = arq.readChar();
            if (cha == '*') {
                System.out.println("Registro não exite!");
            }
            else{
                int length = arq.readInt();
                byte[] ba = new byte[length];
                arq.read(ba);
                Netflix net_temp = new Netflix();
                net_temp.fromByteArray(ba);

                Netflix net = c.preCreate();
                net.Id = net_temp.Id;

                byte[] ba2 = net.toByteArray();

                int length2 = ba2.length;
                if(length > length2){
                    
                    // UPDATE sem colocar um novo
                    arq.seek(pos);
                    arq.readChar();
                    arq.readInt();
                    arq.write(ba2);
                } else {
                    
                    // UPDATE colocando um novo
                    delete(chave);
                    arq.seek(pos);
                    arq.writeChar('*');
                    arq.seek(0);
                    int tamanho = arq.readInt();
                    tamanho = tamanho + 1;
                    arq.seek(0);
                    arq.writeInt(tamanho);
                    long pos2 = arq.length();
                    arq.seek(arq.length());
                    net.Id = tamanho;
                
                    byte[]  ba3 = net.toByteArray();
                    arq.writeChar(' ');
                    arq.writeInt(ba3.length);
                    arq.write(ba3);
                
                    create(net.Id, pos2 );
                }

            }
            arq.close();
            

    }
    
    /**
     * Esta função recebe uma chave como parâmetro e busca essa 
     * chave em uma tabela hash para removê-la, caso ela exista.
     * @param chave inteiro que representa a chave a ser buscada e removida na tabela hash
      * @return true se a chave foi removida com sucesso, false caso contrário.
     * @throws Exception lançada caso ocorra algum erro na leitura ou escrita do arquivo de diretório ou cestos.
     */
    public boolean delete(int chave) throws Exception {
        
        //Carrega o diretório
        byte[] bd = new byte[(int)arqDiretório.length()];
        arqDiretório.seek(0);
        arqDiretório.read(bd);
        diretório = new Diretório();
        diretório.fromByteArray(bd);        
        
        // Identifica a hash do diretório,
        int i = diretório.hash(chave);
        
        // Recupera o cesto
        long endereçoCesto = diretório.endereço(i);
        Cesto c = new Cesto(qtdCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(endereçoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);
        
        // delete a chave
        if(!c.delete(chave))
            return false;
        
        // Atualiza o cesto
        arqCestos.seek(endereçoCesto);
        arqCestos.write(c.toByteArray());
        return true;
    }
    
    /**
    *Imprime o conteúdo do diretório e dos cestos no console.
    *@throws Exception se ocorrer algum erro durante a leitura do arquivo ou na conversão de bytes para objetos.
    */
    public void print() {
        try {
            byte[] bd = new byte[(int)arqDiretório.length()];
            arqDiretório.seek(0);
            arqDiretório.read(bd);
            diretório = new Diretório();
            diretório.fromByteArray(bd);  

            System.out.println(" ");
            System.out.println("------- DIRETÓRI)-------");
            System.out.println(diretório);

            System.out.println(" ");
            System.out.println("-------CESTOS ------- ");
            arqCestos.seek(0);
            while(arqCestos.getFilePointer() != arqCestos.length()) {
                Cesto c = new Cesto(qtdCesto);
                byte[] ba = new byte[c.size()];
                arqCestos.read(ba);
                c.fromByteArray(ba);
                System.out.println(c);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    /**
    *Lê em arquivo CSV contendo informações de registros do tipo Netflix e cria um hash
    *com essas informações. O hash é armazenado em um arquivo binário especificado em s2.
    *@param s1 o caminho do arquivo CSV a ser lido
    *@param s2 o caminho do arquivo binário em que o hash será armazenado
    *@throws Exception se ocorrer um erro de leitura ou gravação de arquivos
    */
    public void LerRegistroHash( String s1, String s2) throws Exception
    {

        byte[] ba;

        BufferedReader bf = new BufferedReader(new FileReader("teste2.csv"));
        RandomAccessFile arq = new RandomAccessFile(s2, "rw");
        arq.writeInt(0);
        arq.close();
     
        for (int j = 0; j < 8807; j++) {
            Netflix net =  new Netflix(bf.readLine());
    
            createHash(net);
        }
        bf.close();
      
    }
}