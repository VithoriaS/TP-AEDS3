import java.io.IOException;
import java.io.RandomAccessFile;

public class arvore {
    private int Ordem;

    private int maxElementos;
    private int maxFilhos;
    private RandomAccessFile arquivo;
    private String nomeArquivo;

    // Variáveis usadas nas recursioes
    private int chaveAux;
    private long dadoAux;
    private long nodeAux;
    private boolean cresceu;
    private boolean diminuiu;

    // contrutor, abre arquivo com raiz vazia
    public arvore(int o, String na) throws IOException {

        Ordem = o;
        maxElementos = o - 1;
        maxFilhos = o;
        nomeArquivo = na;

        arquivo = new RandomAccessFile(nomeArquivo, "rw");
        if (arquivo.length() < 8)
            arquivo.writeLong(-1); // raiz empty
    }

    public boolean create(int ID, long pos) throws IOException {

        // Chave não pode ser empty
        if (ID == 0) {
            System.out.println("Chave não pode ser vazia");
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
        if (cresceu) {

            Node nd = new Node(Ordem);
            nd.n = 1;
            nd.chaves[0] = chaveAux;
            nd.dados[0] = dadoAux;
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

    // Funcao que cria o novo elemento na arvore, identifica onde ele deve ser
    // colocado e manda pra funcao anterior
    // as informacoes se precisar criar um novo node
    // retorna um boolean falando se deu certo ou nao
    public boolean create1(long node) throws IOException {
        // testa se passou para o filho de uma página folha. se sim iniciliaza as
        // variaveis

        if (node == -1) {
            cresceu = true;
            nodeAux = -1;
            return false;
        }

        // Lê a página passada
        arquivo.seek(node);
        Node nd = new Node(Ordem);
        byte[] ba = new byte[nd.TAMANHO_PAGINA];
        arquivo.read(ba);
        nd.fromByteArrayArv(ba);

        // Busca o ponteiro de decida

        int i = 0;
        while (i < nd.n && chaveAux > nd.chaves[i]) {
            i++;
        }

        // Continua a busca recursiva por uma nova página
        boolean inserido;
        if (i == nd.n || chaveAux < nd.chaves[i])
            inserido = create1(nd.filhos[i]);
        else
            inserido = create1(nd.filhos[i + 1]);

        // ve se precisar criar uma nova pagina
        // ou se cabe numa anterior

        if (!cresceu)
            return inserido;

        if (nd.n < maxElementos) {

            for (int j = nd.n; j > i; j--) {
                nd.chaves[j] = nd.chaves[j - 1];
                nd.dados[j] = nd.dados[j - 1];
                nd.filhos[j + 1] = nd.filhos[j];
            }

            nd.chaves[i] = chaveAux;
            nd.dados[i] = dadoAux;
            nd.filhos[i + 1] = nodeAux;
            nd.n++;

            arquivo.seek(node);
            arquivo.write(nd.toByteArrayArv());

            cresceu = false;
            return true;
        }

        // Cria uma nova página
        Node np = new Node(Ordem);

        int meio = maxElementos / 2;
        for (int j = 0; j < (maxElementos - meio); j++) {

            np.chaves[j] = nd.chaves[j + meio];
            np.dados[j] = nd.dados[j + meio];
            np.filhos[j + 1] = nd.filhos[j + meio + 1];

            nd.chaves[j + meio] = -1;
            nd.dados[j + meio] = 0;
            nd.filhos[j + meio + 1] = -1;
        }
        np.filhos[0] = nd.filhos[meio];
        np.n = maxElementos - meio;
        nd.n = meio;

        if (i <= meio) {

            for (int j = meio; j > 0 && j > i; j--) {
                nd.chaves[j] = nd.chaves[j - 1];
                nd.dados[j] = nd.dados[j - 1];
                nd.filhos[j + 1] = nd.filhos[j];
            }

            nd.chaves[i] = chaveAux;
            nd.dados[i] = dadoAux;
            nd.filhos[i + 1] = nodeAux;
            nd.n++;

            if (nd.filhos[0] == -1) {
                chaveAux = np.chaves[0];
                dadoAux = np.dados[0];
            }

            else {
                chaveAux = nd.chaves[nd.n - 1];
                dadoAux = nd.dados[nd.n - 1];
                nd.chaves[nd.n - 1] = -1;
                nd.dados[nd.n - 1] = 0;
                nd.filhos[nd.n] = -1;
                nd.n--;
            }
        }

        else {
            int j;
            for (j = maxElementos - meio; j > 0 && chaveAux < np.chaves[j - 1]; j--) { // abacate
                np.chaves[j] = np.chaves[j - 1];
                np.dados[j] = np.dados[j - 1];
                np.filhos[j + 1] = np.filhos[j];
            }
            np.chaves[j] = chaveAux;
            np.dados[j] = dadoAux;
            np.filhos[j + 1] = nodeAux;
            np.n++;

            chaveAux = np.chaves[0];
            dadoAux = np.dados[0];

            if (nd.filhos[0] != -1) {
                for (j = 0; j < np.n - 1; j++) {
                    np.chaves[j] = np.chaves[j + 1];
                    np.dados[j] = np.dados[j + 1];
                    np.filhos[j] = np.filhos[j + 1];
                }
                np.filhos[j] = np.filhos[j + 1];

                np.chaves[j] = -1;
                np.dados[j] = 0;
                np.filhos[j + 1] = -1;
                np.n--;
            }

        }

        // Grava as páginas no arquivos arquivo
        nodeAux = arquivo.length();
        arquivo.seek(nodeAux);
        arquivo.write(np.toByteArrayArv());

        arquivo.seek(node);
        arquivo.write(nd.toByteArrayArv());

        return true;
    }

    public long read(int d) throws IOException {

        // Recupera a raiz da árvore
        long raiz;
        arquivo.seek(0);
        raiz = arquivo.readLong();

        // Executa a busca recursiva
        if (raiz != -1) {

            return read1(d, raiz);
        } else {

            return -1;
        }

    }

    // procura a chave resusivamente, pela arvore
    // vai decendo dependendo de ser maior ou menor o elemento
    private long read1(int chave, long pagina) throws IOException {

        if (pagina == -1)
            return -1;

        // Reconstrói a página no objeto

        arquivo.seek(pagina);
        Node nd = new Node(Ordem);
        byte[] ba = new byte[nd.TAMANHO_PAGINA];
        arquivo.read(ba);
        nd.fromByteArrayArv(ba);

        // Encontra onde a pagina deve estar

        int i = 0;
        while (i < nd.n && chave > nd.chaves[i]) {
            i++;
        }

        if (i < nd.n && chave == (nd.chaves[i])) {

            return nd.dados[i];
        }

        // continua a busca recursiva pela árvore se nao for folha
        if (i == nd.n || chave < nd.chaves[i])
            return read1(chave, nd.filhos[i]);
        else
            return read1(chave, nd.filhos[i + 1]);

    }

   
  // chama a segunda funcao recursivamente passando a raiz
    public boolean delete(int chave) throws IOException {

        arquivo.seek(0);
        long pagina;
        pagina = arquivo.readLong();

        diminuiu = false;

        boolean excluido = delete1(chave, pagina);

        if (excluido && diminuiu) {

            arquivo.seek(pagina);
            Node nd = new Node(Ordem);
            byte[] ba = new byte[nd.TAMANHO_PAGINA];
            arquivo.read(ba);
            nd.fromByteArrayArv(ba);

            if (nd.n == 0) {
                arquivo.seek(0);
                arquivo.writeLong(nd.filhos[0]);
            }
        }

        return excluido;
    }

    // deleta o elemento desejado trabalhando a excessoes
    // dependendo da situacao 
    private boolean delete1(int chave, long pagina) throws IOException {

        boolean excluido = false;
        int diminuido;

        if (pagina == -1) {
            diminuiu = false;
            return false;
        }

        arquivo.seek(pagina);
        Node nd = new Node(Ordem);
        byte[] ba = new byte[nd.TAMANHO_PAGINA];
        arquivo.read(ba);
        nd.fromByteArrayArv(ba);

        int i = 0;
        while (i < nd.n && chave > nd.chaves[i]) {
            i++;
        }

        if (i < nd.n && nd.filhos[0] == -1 && chave == nd.chaves[i]) {

            int j;
            for (j = i; j < nd.n - 1; j++) {
                nd.chaves[j] = nd.chaves[j + 1];
                nd.dados[j] = nd.dados[j + 1];
            }
            nd.n--;

            nd.chaves[nd.n] = -1;
            nd.dados[nd.n] = 0;

            arquivo.seek(pagina);
            arquivo.write(nd.toByteArrayArv());

            diminuiu = nd.n < maxElementos / 2;
            return true;
        }

        if (i == nd.n || chave < nd.chaves[i]) {
            excluido = delete1(chave, nd.filhos[i]);
            diminuido = i;
        } else {
            excluido = delete1(chave, nd.filhos[i + 1]);
            diminuido = i + 1;
        }

        if (diminuiu) {

            long paginaFilho = nd.filhos[diminuido];
            Node pFilho = new Node(Ordem);
            arquivo.seek(paginaFilho);
            arquivo.read(ba);
            pFilho.fromByteArrayArv(ba);

            long paginaIrmao;
            Node pIrmao;

            if (diminuido > 0) {

                paginaIrmao = nd.filhos[diminuido - 1];
                pIrmao = new Node(Ordem);
                arquivo.seek(paginaIrmao);
                arquivo.read(ba);
                pIrmao.fromByteArrayArv(ba);

                if (pIrmao.n > maxElementos / 2) {

                    for (int j = pFilho.n; j > 0; j--) {
                        pFilho.chaves[j] = pFilho.chaves[j - 1];
                        pFilho.dados[j] = pFilho.dados[j - 1];
                        pFilho.filhos[j + 1] = pFilho.filhos[j];
                    }
                    pFilho.filhos[1] = pFilho.filhos[0];
                    pFilho.n++;

                    if (pFilho.filhos[0] == -1) {
                        pFilho.chaves[0] = pIrmao.chaves[pIrmao.n - 1];
                        pFilho.dados[0] = pIrmao.dados[pIrmao.n - 1];
                    }

                    else {
                        pFilho.chaves[0] = nd.chaves[diminuido - 1];
                        pFilho.dados[0] = nd.dados[diminuido - 1];
                    }

                    nd.chaves[diminuido - 1] = pIrmao.chaves[pIrmao.n - 1];
                    nd.dados[diminuido - 1] = pIrmao.dados[pIrmao.n - 1];

                    pFilho.filhos[0] = pIrmao.filhos[pIrmao.n];
                    pIrmao.n--;
                    diminuiu = false;
                }

                else {

                    if (pFilho.filhos[0] != -1) {
                        pIrmao.chaves[pIrmao.n] = nd.chaves[diminuido - 1];
                        pIrmao.dados[pIrmao.n] = nd.dados[diminuido - 1];
                        pIrmao.filhos[pIrmao.n + 1] = pFilho.filhos[0];
                        pIrmao.n++;
                    }

                    for (int j = 0; j < pFilho.n; j++) {
                        pIrmao.chaves[pIrmao.n] = pFilho.chaves[j];
                        pIrmao.dados[pIrmao.n] = pFilho.dados[j];
                        pIrmao.filhos[pIrmao.n + 1] = pFilho.filhos[j + 1];
                        pIrmao.n++;
                    }
                    pFilho.n = 0;
                    int j;
                    for (j = diminuido - 1; j < nd.n - 1; j++) {
                        nd.chaves[j] = nd.chaves[j + 1];
                        nd.dados[j] = nd.dados[j + 1];
                        nd.filhos[j + 1] = nd.filhos[j + 2];
                    }
                    nd.chaves[j] = -1;
                    nd.dados[j] = -1;
                    nd.filhos[j + 1] = -1;
                    nd.n--;
                    diminuiu = nd.n < maxElementos / 2;
                }
            }

            else {

                paginaIrmao = nd.filhos[diminuido + 1];
                pIrmao = new Node(Ordem);
                arquivo.seek(paginaIrmao);
                arquivo.read(ba);
                pIrmao.fromByteArrayArv(ba);

                if (pIrmao.n > maxElementos / 2) {

                    if (pFilho.filhos[0] == -1) {

                        pFilho.chaves[pFilho.n] = pIrmao.chaves[0];
                        pFilho.dados[pFilho.n] = pIrmao.dados[0];
                        pFilho.filhos[pFilho.n + 1] = pIrmao.filhos[0];
                        pFilho.n++;

                        nd.chaves[diminuido] = pIrmao.chaves[1];
                        nd.dados[diminuido] = pIrmao.dados[1];

                    }

                    else {

                        pFilho.chaves[pFilho.n] = nd.chaves[diminuido];
                        pFilho.dados[pFilho.n] = nd.dados[diminuido];
                        pFilho.filhos[pFilho.n + 1] = pIrmao.filhos[0];
                        pFilho.n++;

                        nd.chaves[diminuido] = pIrmao.chaves[0];
                        nd.dados[diminuido] = pIrmao.dados[0];
                    }

                    int j;
                    for (j = 0; j < pIrmao.n - 1; j++) {
                        pIrmao.chaves[j] = pIrmao.chaves[j + 1];
                        pIrmao.dados[j] = pIrmao.dados[j + 1];
                        pIrmao.filhos[j] = pIrmao.filhos[j + 1];
                    }
                    pIrmao.filhos[j] = pIrmao.filhos[j + 1];
                    pIrmao.n--;
                    diminuiu = false;
                }

                else {

                    if (pFilho.filhos[0] != -1) {
                        pFilho.chaves[pFilho.n] = nd.chaves[diminuido];
                        pFilho.dados[pFilho.n] = nd.dados[diminuido];
                        pFilho.filhos[pFilho.n + 1] = pIrmao.filhos[0];
                        pFilho.n++;
                    }

                    for (int j = 0; j < pIrmao.n; j++) {
                        pFilho.chaves[pFilho.n] = pIrmao.chaves[j];
                        pFilho.dados[pFilho.n] = pIrmao.dados[j];
                        pFilho.filhos[pFilho.n + 1] = pIrmao.filhos[j + 1];
                        pFilho.n++;
                    }
                    pIrmao.n = 0;
                    for (int j = diminuido; j < nd.n - 1; j++) {
                        nd.chaves[j] = nd.chaves[j + 1];
                        nd.dados[j] = nd.dados[j + 1];
                        nd.filhos[j + 1] = nd.filhos[j + 2];
                    }
                    nd.n--;
                    diminuiu = nd.n < maxElementos / 2;
                }
            }

            arquivo.seek(pagina);
            arquivo.write(nd.toByteArrayArv());
            arquivo.seek(paginaFilho);
            arquivo.write(pFilho.toByteArrayArv());
            arquivo.seek(paginaIrmao);
            arquivo.write(pIrmao.toByteArrayArv());
        }
        return excluido;
    }

}
