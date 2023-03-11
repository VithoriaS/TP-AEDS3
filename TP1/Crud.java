import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Crud {

    long[] posicoes;

    public void setPosicoes(long[] posicoes) {
        this.posicoes = posicoes;
    }

    public long[] getPosicoes() {
        return posicoes;
    }

    /**
     * Essa função chamada olicita ao usuário as informações 
     * necessárias para criar um novo objeto Netflix e retorna o objeto preenchido.
     * @return retorna o objeto preenchido pelo usurio.
     */
    public Netflix preCreate() throws IOException
    {
        Scanner sc= new Scanner(System.in);
        
        String Type = "";
        String Name = "";
        String[] Cast = null;
        Data Data_added = new Data();
        int min = 0;
        int seasons = 0;
        String[] Listed_in = null;
        String Description = "";
        int Num_cast = 0;
        int Num_Listed_in = 0;

        System.out.println("Insira as informacaoes a seguir: \nType: ");
        Type = sc.nextLine();
        System.out.println("\nName: ");
        Name = sc.nextLine();
        System.out.println("\nQuantos atores: ");
        Num_cast = sc.nextInt();
        Cast = new String[Num_cast];
        sc.nextLine();
        for (int i = 0; i <Num_cast; i++) {
            System.out.println("\nCast[" + i + "] :");
            
            Cast[i] = sc.nextLine();
        }
        System.out.println("\nMes: ");
        Data_added.setMes(sc.nextLine());
        System.out.println("\nDia: ");
        Data_added.setDia(sc.nextInt());
        System.out.println("\nAno: ");
        Data_added.setAno(sc.nextInt());
        if (Type.charAt(0) == 'T') {
            System.out.println("\nSeasons: ");
            seasons = sc.nextInt();
        }
        else{
            System.out.println("\nMin: ");
            min = sc.nextInt();
        }

        System.out.println("\nQuantas categorias: ");
        Num_Listed_in = sc.nextInt();
        Listed_in = new String[Num_Listed_in];
        sc.nextLine();
        for (int i = 0; i <Num_Listed_in; i++) {
            System.out.println("\nNum_Listed_in[" + i + "] :");
            Listed_in[i] = sc.nextLine();
        }
        System.out.println("\nDescrition: ");
        Description = sc.nextLine();


        //colocando os dados na classe
        Netflix net = new Netflix(0, Type, Name, Cast,
         Data_added, min, seasons, Listed_in, Description);
         
        return net;
    }

    public void create() throws IOException {
        
        //chamando o pre create
        Netflix net = preCreate();
        int len = 0;
        byte[] ba;

        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        len = arq.readInt();
       
        len = len + 1;
        
        net.Id = len;

        net.printar();

        arq.seek(0);
        arq.writeInt(len);
        arq.seek(arq.length());
        ba = net.toByteArray();
        arq.writeChar(' ');
        arq.writeInt(ba.length);
        arq.write(ba);

        arq.close();
    }

    /**
    Método para criar um novo registro no arquivo de dados.
    Recebe como parâmetro um objeto da classe Netflix a ser adicionado.
    Abre o arquivo "teste.db" em modo de escrita e leitura.
    Posiciona o ponteiro do arquivo no final, onde serão adicionados novos registros.
    Converte o objeto em um array de bytes e grava no arquivo, junto com um caractere '*' e a
    indicação do tamanho do registro em bytes.
    @param net objeto da classe Netflix a ser adicionado ao arquivo.
    @throws IOException em caso de erro na leitura ou escrita do arquivo.
    */
    public void create(Netflix net) throws IOException {
        
       
        int len = 0;
        byte[] ba;

        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        len = arq.readInt();
       
        len = len + 1;
        
        net.Id = len;

        net.printar();

        arq.seek(0);
        arq.writeInt(len);
        arq.seek(arq.length());
        ba = net.toByteArray();
        arq.writeChar(' ');
        arq.writeInt(ba.length);
        arq.write(ba);

        arq.close();
    }

    
    /**
     * Método para ler um registro dentro de um arquivo binario.
      @param recebe como parâmetro um ID do tipo inteiro que representa a chave primária do objeto a ser buscado.
     */
    public Netflix  read(int IdLegal) throws IOException {
        int len = 0;
        char c = 'a';
        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        len = arq.readInt();
        int x = 0;
        byte[] ba;

        Netflix net_temp = new Netflix();
        while (net_temp.Id != len) { 
            
            try {
                c = arq.readChar();
            } catch (Exception e) {
                System.out.println("TryCathc" + x + "TESTE"+  c + "TESTE");
            }

  
            
            int length = arq.readInt();
            
            if (c != '*') {
                
                ba = new byte[length];
                arq.read(ba);
                net_temp.fromByteArray(ba);
             
                if (IdLegal == net_temp.Id) {
                    arq.close();
                    return net_temp;
                }
                
            }
            else
            {
                arq.skipBytes(length);
            }
           
            
        }
        arq.close();
        return null;

    
    }

    /**
    Função que atualiza um registro no arquivo "teste.db" com o ID fornecido.
    A função retorna um valor booleano indicando se a atualização foi bem sucedida ou não.
    @param Id o ID do registro a ser atualizado.
    @return true se a atualização foi bem sucedida, false caso contrário.
    @throws IOException se houver um erro de entrada ou saída ao trabalhar com o arquivo.
    */

    public boolean uptate (int Id) throws IOException{

        int len = 0;
        char c;
        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        len = arq.readInt();
        int x = 0;
        byte[] ba;
        long pos = 0;
        byte[] ba2;

        Netflix net_temp = new Netflix();
        while (net_temp.Id != len) {
            pos =  arq.getFilePointer();
             c = arq.readChar();
             int length = arq.readInt();

             if (c != '*') {
               

                ba = new byte[length];
                arq.read(ba);
                net_temp.fromByteArray(ba);
                if (Id == net_temp.Id) {
                    Netflix net = preCreate();
                    net.Id = Id;
                    ba2= net.toByteArray();
                    // verifica se o novo registro cabe no espaço do registro antigo
                    if (  ba2.length <= length) {
                        arq.seek(pos);
                        arq.readChar();
                        arq.readInt();
                        arq.write(ba2);
                        System.out.println("Deu certo1");
                        break;
                        
                    }
                    else{
                        // se não cabe, exclui o registro antigo e cria um novo
                        delete(Id);
                        create(net);
                        System.out.println("Deu errado2");
                        break;
                    }
                }

            }
            else{
               // pula o registro excluído marcado com "*"
                arq.skipBytes(length);
            }
            
    }
        return false;
    }

    /**
     Método com objetivo buscar o registro do objeto Netflix no arquivo "teste.db" 
      que corresponde ao IdLegal passado como parâmetro e marcá-lo com um asterisco 
     (caractere '*') para indicar que foi deletado. 
      @param IdLegal
      @return true se foi deletado, false caso contrário.
      @throws IOException
     */
    public boolean delete(int IdLegal) throws IOException
    {
        int len = 0;
        char c;
        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        len = arq.readInt();
        int x = 0;
        byte[] ba;
        long pos= 0;
        
        Netflix net_temp = new Netflix();
        while (net_temp.Id != len) {
            pos =  arq.getFilePointer();
           // System.out.println(pos);
            c = arq.readChar();
          //  System.out.println(c);

            if (c != '*') {
                int length = arq.readInt();
                ba = new byte[length];
                arq.read(ba);
                

                net_temp.fromByteArray(ba);
                if (IdLegal == net_temp.Id) {
                    
                    arq.seek(pos);
                    arq.writeChar('*');
                    
                    arq.close();
                    return true;
                   
                }
                
            }
            else{
                int tamanho = arq.readInt();
                arq.skipBytes(tamanho);
            }

            x++;
        }
        
        System.out.println("teste1");
        arq.close();
        return false;
    }
}
