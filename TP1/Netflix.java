import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class Netflix {
    public static String db_path = "teste.db";
    public static String tree_path = "aeds3/src/main/java/tmp/btree.idx";
    public static String hash_path = "aeds3/src/main/java/tmp/hash.idx";
    public static String listaTitle_path = "aeds3/src/main/java/tmp/listaTitlePath.idx";
    public static String listaDirectors_path = "aeds3/src/main/java/tmp/listaDirectorsPath.idx";
    char lapide;
    int Id = 0;
    String Type = "";
    String Name = "";
    String[] Cast = null;
    Data Data_added = new Data();
    int min = 0;
    int seasons = 0;
    String[] Listed_in = null;
    String Description = "";

    // teste
    public Netflix(int id, String type, String name, String[] cast, Data data_added, int min, int seasons,
            String[] listed_in, String description) {
        Id = id;
        Type = type;
        Name = name;
        Cast = cast;
        Data_added = data_added;
        this.min = min;
        this.seasons = seasons;
        Listed_in = listed_in;
        Description = description;
    }

    public Netflix() {
    }

    public String toString() {
        return "\nID:" + Id
                + "\nType:" + Type
                + "\nName:" + Name
                + "\nCast:" + Arrays.toString(Cast)
                + this.getData_added().toString()
                + "\nListed_in:" + Arrays.toString(Listed_in) +
                "\nDescription:" + Description;
    }

    public void printar() {
        System.out.println("ID:" + Id);
        System.out.println("Type:" + Type);
        System.out.println("NOME:" + Name);

        if (Cast == null) {

        } else {
            for (int i = 0; i < Cast.length; i++) {
                System.out.println("\ncast[" + i + "]: " + Cast[i]);

            }
        }

        System.out.println("\nMes: " + Data_added.getMes() +
                "\nDia :" + Data_added.getDia() +
                "\nAno: " + Data_added.getAno() +
                "\nseasons: " + seasons +
                "\nMin: " + min);
        if (Cast == null) {

        } else {
            for (int i = 0; i < Listed_in.length; i++) {
                System.out.println("\nListed_in[" + i + "]: " + Listed_in[i]);

            }
        }

        System.out.println("\nDescription: " + Description);

    }

    public static void apagarRegistro(String FilePath) {

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

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSeasons() {
        return seasons;
    }

    public void setSeasons(int seasons) {
        this.seasons = seasons;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String[] getCast() {
        return Cast;
    }

    public void setCast(String[] cast) {
        Cast = cast;
    }

    public Data getData_added() {
        return Data_added;
    }

    public void setData_added(Data data_added) {
        Data_added = data_added;
    }

    public String[] getListed_in() {
        return Listed_in;
    }

    public void setListed_in(String[] listed_in) {
        Listed_in = listed_in;
    }

    public void tamanhoListed_in(int length) {
        Listed_in = new String[length];
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public byte[] toByteArray() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeChar(' ');
        dos.writeInt(Id);
        // tipo
        dos.writeUTF(Type);
        // name
        dos.writeUTF(Name);

        if (Cast == null) {
            dos.writeInt(0);
        } else {
            dos.writeInt(Cast.length);
            // System.out.println("Cast.length: " + Cast.length);
            for (int i = 0; i < Cast.length; i++) {
                // dos.writeInt(Cast[i].length());
                // System.out.println("Cast[i].length(): " + Cast[i].length());
                // System.out.println("Cast[i]: " + Cast[i]);
                dos.writeUTF(Cast[i]);
            }
        }

        dos.writeUTF(Data_added.getMes());
        dos.writeInt(Data_added.getDia());
        dos.writeInt(Data_added.getAno());

        if (Listed_in == null) {
            dos.writeInt(0);
        } else {
            dos.writeInt(Listed_in.length);
            // System.out.println("Listed_in.length: " + Listed_in.length);
            for (int i = 0; i < Listed_in.length; i++) {
                // dos.writeInt(Listed_in[i].length());
                // System.out.println("Listed_in[i].length(): " + Listed_in[i].length());
                // System.out.println("Listed_in[i]: " + Listed_in[i]);
                dos.writeUTF(Listed_in[i]);
            }
        }

        dos.writeUTF(Description);

        return baos.toByteArray();
    }

    public void fromByteArray(byte ba[]) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        lapide = dis.readChar();
        Id = dis.readInt();
        Type = dis.readUTF();
        Name = dis.readUTF();

        int length = dis.readInt();
        Cast = new String[length];
        for (int i = 0; i < length; i++) {
            Cast[i] = dis.readUTF();
        }

        // dis.readInt();
        Data_added.setMes(dis.readUTF());
        // System.out.println(Data_added.getMes());
        Data_added.setDia(dis.readInt());
        // System.out.println(Data_added.getDia());
        Data_added.setAno(dis.readInt());
        // System.out.println(Data_added.getAno());

        length = dis.readInt();
        Listed_in = new String[length];
        for (int i = 0; i < length; i++) {
            Listed_in[i] = dis.readUTF();
        }

        Description = dis.readUTF();
    }

    public Netflix(String str) {

        String[] Cast = new String[0];
        String[] Listed_in = new String[0];
        String aux = "";
        int i = 0;

        // -----------------------------Id----------------------------------
        for (i = 0; str.charAt(i) != ','; i++) {
            aux += str.charAt(i);
        }
        setId(Integer.parseInt(aux));
        // System.out.println("\nTESTE1: " + Id);
        // -----------------------------Type----------------------------------
        aux = "";
        for (i++; str.charAt(i) != ','; i++) {
            aux += str.charAt(i);
        }
        setType(aux);
        // System.out.println("\nTESTE2: " + Type);
        // Scanner scanner = new Scanner(System.in);
        // -------------------------------Name---------------------------------

        aux = "";

        if (str.charAt(i + 1) == '\"') {

            for (i = i + 2; str.charAt(i) != '\"'; i++) {
                aux += str.charAt(i);
            }

            if (str.charAt(i + 1) == '\"') {

                for (; str.charAt(i + 1) != ','; i++) {
                    aux += str.charAt(i);
                }

                // System.out.println("\nTESTE31: " + aux);
                // scanner.nextInt();
            }

            i++;
        } else {
            for (i++; str.charAt(i) != ','; i++) {
                aux += str.charAt(i);

            }
        }
        setName(aux);
        // System.out.println("\nTESTE3: " + Name);

        // -------------------------------Cast--------------------------------
        aux = "";
        int cont = 1;
        int k = i;

        // contar quantos atores existem para criar o tamanho do array de string
        if (str.charAt(k + 1) == '\"') {
            for (k = k + 2; (str.charAt(k) != '\"') || (str.charAt(k + 1) == '\"' && str.charAt(k) == '\"')
                    || (str.charAt(k - 1) == '\"' && str.charAt(k) == '\"'); k++) {
                if (str.charAt(k) == ',') {
                    cont++;
                }
            }
        }

        if (str.charAt(i + 1) == '\"') {
            Cast = new String[cont];
        } else if (str.charAt(i + 1) != ',') {
            Cast = new String[1];
        }

        cont = 0;
        if (str.charAt(i + 1) == '\"') {
            for (i = i + 2; (str.charAt(i) != '\"') || (str.charAt(i + 1) == '\"' && str.charAt(i) == '\"')
                    || (str.charAt(i - 1) == '\"' && str.charAt(i) == '\"'); i++) {
                if (str.charAt(i) == ',') {
                    Cast[cont] = aux;
                    cont++;
                    aux = "";
                    i++;
                } else {
                    aux += str.charAt(i);
                }
            }
            Cast[cont] = aux;
            // pode da erro

        } else if (str.charAt(i + 1) != ',') {

            for (i++; str.charAt(i) != ','; i++) {
                aux += str.charAt(i);
            }
            Cast[0] = aux;
            i--;
        }

        setCast(Cast);

        if (Cast == null) {
            // System.out.println("\nTESTE4: NULL");
        } else {
            for (int j = 0; j < Cast.length; j++) {
                // System.out.println("\nTESTE4: " + Cast[j]);
            }
        }

        i++;
        // ---------------------Data---------------------------
        aux = "";
        if (str.charAt(i + 1) == ',') {
            getData_added().setDia(0);
            getData_added().setMes("");
            getData_added().setAno(0);
        } else {
            for (i = i + 2; str.charAt(i) != '\"'; i++) {

                if (str.charAt(i) == ',') {
                    getData_added().setDia(Integer.parseInt(aux));
                    i++;
                    aux = "";
                } else if (str.charAt(i) == ' ') {
                    getData_added().setMes(aux);
                    aux = "";
                } else if (str.charAt(i) != ' ') {

                    aux += str.charAt(i);
                }

            }

            getData_added().setAno(Integer.parseInt(aux));
        }
        aux = "";
        i = i + 2;
        // System.out.println("\nTESTE5: " + Data_added.getMes() + " " +
        // Data_added.getDia() + " " + Data_added.getAno());
        // --------------------Duration-------------------------
        if (getType().charAt(0) == 'T') {
            if (str.charAt(i) == ',') {
                setSeasons(0);

            } else {
                for (; str.charAt(i) != ' '; i++) {
                    aux += str.charAt(i);
                }
                setSeasons(Integer.parseInt(aux));
            }
        } else {
            if (str.charAt(i) == ',') {
                setMin(0);

            } else {
                for (; str.charAt(i) != ' '; i++) {
                    aux += str.charAt(i);
                }
                setMin(Integer.parseInt(aux));
            }
        }

        // System.out.println("\nTESTE6: " + seasons + " " + min);
        // anda ate achar a virgula
        for (; str.charAt(i) != ','; i++)
            ;
        aux = "";

        // --------------------Listed_in-------------------------

        aux = "";
        cont = 1;
        k = i;

        // contar quantos atores existem para criar o tamanho do array de string
        if (str.charAt(k + 1) == '\"') {
            for (k = k + 2; str.charAt(k) != '\"'; k++) {
                if (str.charAt(k) == ',') {
                    cont++;
                }
            }
        }

        if (str.charAt(i + 1) == '\"') {
            Listed_in = new String[cont];
        } else if (str.charAt(i + 1) != ',') {
            Listed_in = new String[1];
        }

        cont = 0;

        if (str.charAt(i + 1) == '\"') {
            for (i = i + 2; str.charAt(i) != '\"'; i++) {
                if (str.charAt(i) == ',') {
                    Listed_in[cont] = aux;
                    cont++;
                    aux = "";
                    i++;
                } else {
                    aux += str.charAt(i);
                }
            }
            Listed_in[cont] = aux;
            i++;
        } else if (str.charAt(i + 1) != ',') {

            for (i++; str.charAt(i) != ','; i++) {

                aux += str.charAt(i);
            }

            Listed_in[0] = aux;

        }

        setListed_in(Listed_in);
        for (int j = 0; j < Listed_in.length; j++) {
            // System.out.println("\nTESTE7: " + Listed_in[j]);
        }
        // --------------------------------Description-----------------------------
        aux = "";
        if (str.charAt(i + 1) == '\"') {

            for (i = i + 2; str.charAt(i) != '\"'; i++) {
                aux += str.charAt(i);
            }
        } else {

            for (i = i + 1; i < str.length(); i++) {

                aux = aux + str.charAt(i);
            }

        }

        setDescription(aux);
        // System.out.println("\nTESTE8: " + Description);
    }

    static public void LerBaseDeDadosInicial(String s1, String s2, int NumRegistros) throws IOException {
        Netflix[] net = new Netflix[NumRegistros];
        byte[] ba;

        BufferedReader bf = new BufferedReader(new FileReader(s1));

        for (int j = 0; j < NumRegistros; j++) {
            net[j] = new Netflix(bf.readLine());
        }
        bf.close();

        RandomAccessFile arq = new RandomAccessFile(s2, "rw");

        arq.writeInt(NumRegistros);
        for (int i = 0; i < NumRegistros; i++) {
            ba = net[i].toByteArray();
            arq.writeChar(' ');
            arq.writeInt(ba.length);
            arq.write(ba);
        }

        arq.close();

    }

    private static void LZWDes() throws IOException {
        RandomAccessFile entrada = new RandomAccessFile("teste.db", "r");
        byte[] input_data = new byte[(int) entrada.length()];
        entrada.readFully(input_data);
        RandomAccessFile saida = new RandomAccessFile("saidaFinal.db", "rw");
        byte[] decompress = LZW.descompresao(input_data);
        saida.write(decompress);

    }

    private static long LZWCom() throws IOException {
        long tam;
        RandomAccessFile entrada = new RandomAccessFile("teste.db", "r");
        byte[] input_data = new byte[(int) entrada.length()];
        entrada.readFully(input_data);
        RandomAccessFile saida = new RandomAccessFile("saida.db", "rw");
        saida.write(LZW.compressao(input_data));
        tam = saida.length();

        return tam;
    }

    static public void TelaArvore() throws IOException {

        int x = 0;
        CrudArvore crudA = new CrudArvore();
        long pos;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("\nOpcoes ");
            System.out.println(" 0 - parar");
            System.out.println(" 1 - Create");
            System.out.println(" 2 - Read");
            System.out.println(" 3 - Update");
            System.out.println(" 4 - Delete");
            System.out.println(" 5 - Ler base de dados incial !CUIDADO! (So use se nao tiver arquivo)");

            System.out.println("Entrar com uma opcao:");

            x = sc.nextInt();
            System.out.println(x);

            switch (x) {
                case 0:

                    break;
                case 1:
                    crudA.createArv();
                    break;
                case 2:
                    crudA.readArv();
                    break;
                case 3:
                    crudA.updateArv();
                    break;
                case 4:
                    crudA.deleteARV();
                    break;
                case 5:

                    crudA.LerRegistroEfazerArvore("teste2.csv", "teste.db", 8807);

                    break;
                case 6:

                    break;

                default:
                    System.out.println("ERRO: Valor invalido:" + x);
            }

        } while (x != 0);
        sc.close();
    }

    static public void TelaArquiHash() throws Exception {
        int y = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("Quantidade de elementos por cesto:");
        int numCesto = sc.nextInt();
        HashExtensivel h = new HashExtensivel(numCesto, "BaseHash/Index.db", "BaseHash/Cesto.db");
        h.LerRegistroHash("teste2.csv", "teste.db");
        do {

            System.out.println("\nOpcoes ");
            System.out.println(" 0 - parar");
            System.out.println(" 1 - Creat");
            System.out.println(" 2 - Uptade");
            System.out.println(" 3 - Read");
            System.out.println(" 4 - Delete");
            y = sc.nextInt();

            switch (y) {
                case 1:
                    h.createHash();
                    break;
                case 2:
                    System.out.println("Digite a chave (ID) que voce quer mudar: ");
                    int chave1 = sc.nextInt();
                    long endereco = h.read(chave1);
                    h.updateHash(chave1, endereco);
                    break;

                case 3:
                    System.out.println("Digite a chave (ID) que voce quer Ler: ");
                    int chave = sc.nextInt();
                    h.readHash(chave);
                    break;

                case 4:
                    System.out.println("Digite a chave (ID) que voce quer deletar: ");
                    int chave2 = sc.nextInt();
                    if (h.delete(chave2)) {
                        System.out.println("Elemente deletado com Sucesso!");
                    } else {
                        System.out.println("Elemento não deletado");
                    }
                    break;
            }
        } while (y != 0);
    }

    static public void TelaArquiSeq() throws IOException {

        int x = 0;
        Crud c = new Crud();
        ordenacao ord = new ordenacao();
        long pos;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("\nOpcoes ");
            System.out.println(" 0 - para");
            System.out.println(" 1 - Create");
            System.out.println(" 2 - Read");
            System.out.println(" 3 - Update");
            System.out.println(" 4 - Delete");
            System.out.println(" 5 - Ordernar");
            System.out.println(" 6 - Ler base de dados incial !CUIDADO! (So use se nao tiver arquivo)");
            System.out.println(" 7 - Deletar Registro");
            System.out.println(" 8 - Ler tudo ");

            System.out.println("Entrar com uma opcao:");

            x = sc.nextInt();
            System.out.println(x);

            switch (x) {
                case 0:

                    break;
                case 1:
                    c.create();
                    break;
                case 2:
                    System.out.println("Qual Id deseja procurar:");

                    Netflix temp = c.read(sc.nextInt());

                    if (temp == null) {
                        System.out.println("Nao Tem");
                    } else {
                        temp.printar();
                    }

                    break;
                case 3:
                    System.out.println("Qual Id deseja Update:");
                    int Num = sc.nextInt();
                    c.uptate(Num);
                    break;
                case 4:
                    System.out.println("Qual Id deseja Deletar:");
                    int Num2 = sc.nextInt();

                    Boolean b1 = c.delete(Num2);

                    if (b1) {
                        System.out.println("Deletado");
                    } else {
                        System.out.println("n tem esse registro ou ele ja esta deletado");
                    }

                    break;
                case 5:
                    ord.LoopOrdenacao();
                    break;

                case 6:
                    LerBaseDeDadosInicial("teste2.csv", "teste.db", 8807);
                    break;

                case 7:
                    System.out.println("Qual Arquivo deseja Deletar:");
                    String s1 = sc.next();
                    apagarRegistro(s1);
                    break;

                case 8:
                    c.readAll();
                    break;

                default:
                    System.out.println("ERRO: Valor invalido:" + x);
            }

        } while (x != 0);
        sc.close();
    }

    static public void TelaCasamentoDepadrao() throws IOException {
        KMPAlgorithm kmp = new KMPAlgorithm();
        int x = 0;

        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("\nOpcoes ");
            System.out.println(" 0 - para");
            System.out.println(" 1 - KMP");
            System.out.println(" 2 - ALGvic");
            System.out.println(" 3 - Comparacao Algoritomos");

            System.out.println("Entrar com uma opcao:");

            x = sc.nextInt(); // ta dando lixo aqui vics
            System.out.println(x);

            switch (x) {
                case 0:

                    break;
                case 1:

                    Scanner sc1 = new Scanner(System.in);
                    System.out.println("Qual Padrão deseja buscar (Apenas Strings):");
                    String s1 = sc1.nextLine();

                    kmp.kmp(s1);
                    sc1.close();

                    break;
                case 2:
                    Scanner sc2 = new Scanner(System.in);
                    System.out.println("Qual Padrão deseja buscar (Apenas Strings):");
                    String s2 = sc.nextLine();
                    // vics alg
                    sc2.close();
                    break;
                case 3:
                    Scanner sc3 = new Scanner(System.in);
                    System.out.println("Qual Padrão deseja buscar (Apenas Strings):");
                    String s3 = sc.nextLine();
                    long tempoKmp, tempoVic, start = 0;
                    start = System.currentTimeMillis();
                    kmp.kmp(s3);
                    tempoKmp = System.currentTimeMillis();
                    tempoKmp = tempoKmp - start;

                    start = System.currentTimeMillis();
                    // vics alg
                    tempoVic = System.currentTimeMillis();
                    tempoVic = tempoVic - start;

                    sc3.close();

                    int comparacaoKmp = kmp.comparacoes;
                    // compara ai vics

                    break;

                default:
                    System.out.println("ERRO: Valor invalido:" + x);
            }

        } while (x != 0);
        sc.close();
    }

    static public void TelaInicial() throws Exception {

        int x = 0;
        int y = 0;
        int numCesto = 0;
        Crud c = new Crud();
        ordenacao ord = new ordenacao();
        long pos;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("\nOpcoes ");
            System.out.println(" 0 - parar");
            System.out.println(" 1 - ArqSequencial");
            System.out.println(" 2 - Arvore");
            System.out.println(" 3 - Hash");
            System.out.println(" 4 - Huffman");
            System.out.println(" 5 - LZW");
            System.out.println(" 6 - LZW e Huffman juntos (Compressão)");
            System.out.println(" 7 - LZW e Huffman juntos (Descompressão)");
            System.out.println(" 8 - KMP e vicsALg");
            System.out.println(" 9 - Criptografia");

            System.out.println("Entrar com uma opcao:");

            x = sc.nextInt();
            System.out.println(x);

            switch (x) {
                case 1:
                    TelaArquiSeq();
                    break;
                case 2:
                    TelaArvore();
                    break;
                case 3:
                    TelaArquiHash();
                    break;
                case 4:
                    TelaCompressao();
                    break;
                case 5:
                    TelaCompressaoLZW();
                    break;
                case 6:
                    compresao();
                    x = 0;
                    break;
                case 7:
                    descompresao();
                    x = 0;
                    break;
                case 8:
                    TelaCasamentoDepadrao();
                    break;
                case 9:
                    TelaCripitografia();
                    break;
                default:
                    System.out.println("ERRO: Valor invalido:" + x);
            }

        } while (x != 0);
        sc.close();
    }

    static public void HuffCompres() throws IOException {
        Scanner sc = new Scanner(System.in);
        HuffmanCompression huff = new HuffmanCompression();
        System.out.println("Qual arquivo deseja compactar");
        String s1 = sc.next();
        huff.comprimir1(s1);

        sc.close();

    }

    static public void HuffDecompres() {
        Scanner sc = new Scanner(System.in);
        HuffmanCompression huff = new HuffmanCompression();
        System.out.println("Qual versão deseja descompactar");
        int k = sc.nextInt();
        String aux1 = "testeHuffman";
        aux1 = aux1 + String.valueOf(k);
        aux1 = aux1 + ".db";
        huff.retrieveDataFromFile(aux1, k);
        sc.close();
    }

    static public void TelaCompressao() throws Exception {
        Scanner sc = new Scanner(System.in);
        int x = 0;
        HuffmanCompression huff = new HuffmanCompression();
        do {
            System.out.println("\nOpcoes ");
            System.out.println(" 0 - para");
            System.out.println(" 1 - Compactar");
            System.out.println(" 2 - Descompactar");

            System.out.println("Entrar com uma opcao:");

            x = sc.nextInt();
            System.out.println(x);

            switch (x) {
                case 1:
                    System.out.println("Qual arquivo deseja compactar");
                    String s1 = sc.next();
                    huff.comprimir1(s1);
                    break;
                case 2:
                    System.out.println("Qual versão deseja descompactar");
                    int k = sc.nextInt();
                    String aux1 = "testeHuffman";
                    aux1 = aux1 + String.valueOf(k);
                    aux1 = aux1 + ".db";
                    huff.retrieveDataFromFile(aux1, k);
                    break;

                default:
                    System.out.println("ERRO: Valor invalido:" + x);
            }

        } while (x != 0);

    }

    private static void compresao() throws IOException {
        // huff
        long tempoHuffman, tempoLZW, start = System.currentTimeMillis();
        HuffCompres();
        RandomAccessFile raf1 = new RandomAccessFile("teste.db", "rw");
        long tamanho1 = raf1.length();

        raf1.close();

        RandomAccessFile raf2 = new RandomAccessFile("testeHuffman1.db", "rw");
        long tamanho2 = raf2.length();

        raf2.close();

        float tamanhoHuffman = (float) tamanho2 / (float) tamanho1;

        tempoHuffman = System.currentTimeMillis() - start;

        // lzw
        LZWCom();
        RandomAccessFile raf3 = new RandomAccessFile("teste.db", "rw");
        long tamanho3 = raf3.length();
        raf3.close();

        RandomAccessFile raf4 = new RandomAccessFile("saida.db", "rw");
        long tamanho4 = raf4.length();
        raf4.close();

        float tamanhoLZW = (float) tamanho4 / (float) tamanho3;

        tempoLZW = System.currentTimeMillis() - tempoHuffman;

        // comparacao tempo
        System.out.println("Tempo de compressão do Huffman: " + tempoHuffman + "ms");
        System.out.println("Tempo de compressão do LZW: " + tempoLZW + "ms");

        if (tempoHuffman > tempoLZW) {
            System.out.println("O LZW foi mais rápido");
        } else {
            System.out.println("O Huffman foi mais rápido");
        }

        // comparacao tamanho
        System.out.println("Porcentagem de tamanho Huffman: " + tamanhoHuffman + "%");
        System.out.println("Porcentagem de tamanho LZW: " + tamanhoLZW + "%");

        if (tamanhoHuffman < tamanhoLZW) {
            System.out.println("O Huffman foi mais eficiente");
        } else {
            System.out.println("O LWZ foi mais eficiente");
        }

    }

    private static void descompresao() throws IOException {
        // huff
        long tempoHuffman, tempoLZW, start = System.currentTimeMillis();
        HuffDecompres();
        RandomAccessFile raf1 = new RandomAccessFile("testeHuffman1.db", "rw");
        long tamanho1 = raf1.length();
        raf1.close();

        RandomAccessFile raf2 = new RandomAccessFile("teste.db", "rw");
        long tamanho2 = raf2.length();
        raf2.close();

        float tamanhoHuffman = (float) tamanho2 / (float) tamanho1;

        tempoHuffman = System.currentTimeMillis() - start;

        // lzw
        LZWDes();
        RandomAccessFile raf3 = new RandomAccessFile("saida.db", "rw");
        long tamanho3 = raf3.length();
        raf3.close();

        RandomAccessFile raf4 = new RandomAccessFile("teste.db", "rw");
        long tamanho4 = raf4.length();
        raf4.close();

        float tamanhoLZW = (float) tamanho4 / (float) tamanho3;

        tempoLZW = System.currentTimeMillis() - tempoHuffman;

        // comparacao tempo
        System.out.println("Tempo de descompressão do Huffman: " + tempoHuffman + "ms");
        System.out.println("Tempo de descompressão do LZW: " + tempoLZW + "ms");

        if (tempoHuffman > tempoLZW) {
            System.out.println("O LZW foi mais rápido");
        } else {
            System.out.println("O Huffman foi mais rápido");
        }

        // comparacao tamanho
        System.out.println("Porcentagem de tamanho Huffman: " + tamanhoHuffman + "%");
        System.out.println("Porcentagem de tamanho LZW: " + tamanhoLZW + "%");

        if (tamanhoHuffman < tamanhoLZW) {
            System.out.println("O Huffman foi mais eficiente");
        } else {
            System.out.println("O LWZ foi mais eficiente");
        }
    }

    static public void TelaCompressaoLZW() throws Exception {
        Scanner sc = new Scanner(System.in);
        int x = 0;
        /// HuffmanCompression huff = new HuffmanCompression();
        do {
            System.out.println("\nOpcoes ");
            System.out.println(" 0 - para");
            System.out.println(" 1 - Compactar");
            System.out.println(" 2 - Descompactar");

            System.out.println("Entrar com uma opcao:");

            x = sc.nextInt();
            System.out.println(x);

            switch (x) {
                case 1:
                    LZWCom();
                    break;
                case 2:
                    LZWDes();

                    break;

                default:
                    System.out.println("ERRO: Valor invalido:" + x);
            }

        } while (x != 0);

    }

    static public void TelaCripitografia() throws IOException {
        Scanner sc = new Scanner(System.in);
        int x = 0;
        viginere vig = new viginere();
        /// HuffmanCompression huff = new HuffmanCompression();
        do {
            System.out.println("\nOpcoes ");
            System.out.println(" 0 - para");
            System.out.println(" 1 - vegenere criptografar");
            System.out.println(" 2 - vegenere decriptografar");

            System.out.println("Entrar com uma opcao:");

            x = sc.nextInt();
            System.out.println(x);

            switch (x) {
                case 1:

                    byte[] encryptedText = vig.readFileToByteArray("teste.db");
                    byte[] key = "KEY".getBytes();
                    byte[] emcryptedText = viginere.encrypt(encryptedText, key);
                     FileOutputStream fos = new FileOutputStream("criptografado.db");
                         fos.write(emcryptedText );
                     fos.close();
                    break;
                case 2:

                   byte[] decripitado = vig.readFileToByteArray("criptografado.db");
                    byte[] chave = "KEY".getBytes();

                    byte[] decryptedText = viginere.decrypt(decripitado, chave);
                     FileOutputStream fos1 = new FileOutputStream("teste.db");
                         fos1.write(decryptedText);
                     fos1.close();
                    break;

                default:
                    System.out.println("ERRO: Valor invalido:" + x);
            }

        } while (x != 0);

    }

    public static void main(String[] args) throws Exception {

        TelaInicial();

    }

    public void setCast(String readUTF) {
    }
}
