import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Scanner;

import javax.lang.model.util.ElementScanner14;

public class Netflix {
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
    //teste
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
        System.out.println("\nID: " + Id +
                "\nType: " + Type +
                "\nNome: " + Name);
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
        // System.out.println("ID: " + Id);
        //dos.writeInt(Type.length());
        // System.out.println("TYPE.length(): " + Type.length());
        // System.out.println("TYPE: " + Type);
        dos.writeUTF(Type);

        //dos.writeInt(Name.length());
        // System.out.println("TYPE.length(): " + Name.length());
        // System.out.println("TYPE: " + Name);
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

      //  dos.writeInt(Data_added.getMes().length());
        // System.out.println("Data_added.getMes().length(): " +
        // Data_added.getMes().length() );
        // System.out.println("Data_added.getMes(): " + Data_added.getMes() );
        // System.out.println("Data_added.getdia(): " + Data_added.getDia() );
        // System.out.println("Data_added.getano(): " + Data_added.getAno() );
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

        // System.out.println("Description.length(): " + Description.length());
        //dos.writeInt(Description.length());
        // System.out.println("Description: " + Description);
        dos.writeUTF(Description);

        return baos.toByteArray();
    }

    public void fromByteArray(byte ba[]) throws IOException {

        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        lapide = dis.readChar();
        Id = dis.readInt();
        // System.out.println(Id);
       // dis.readInt();
        
        Type = dis.readUTF();
        // System.out.println(Type);
        //dis.readInt();
        Name = dis.readUTF();
        // System.out.println(Name);
        int x = dis.readInt();
        Cast = new String[x];
        for (int i = 0; i < x; i++) {
            //dis.readInt();
            Cast[i] = dis.readUTF();
            // System.out.println(Cast[i]);
        }

       // dis.readInt();
        Data_added.setMes(dis.readUTF());
        // System.out.println(Data_added.getMes());
        Data_added.setDia(dis.readInt());
        // System.out.println(Data_added.getDia());
        Data_added.setAno(dis.readInt());
        // System.out.println(Data_added.getAno());

        int teste = dis.readInt();

        Listed_in = new String[teste];
        for (int i = 0; i < teste; i++) {
           // dis.readInt();
            Listed_in[i] = dis.readUTF();
            // System.out.println(Listed_in[i]);
        }

       // dis.readInt();
        Description = dis.readUTF();
        // System.out.println( Description);

    }



    
    public Netflix(String str) {

        String[] Cast = null;
        String[] Listed_in = null;
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
            for (i = i + 1; i > str.length(); i++) {
                aux += str.charAt(i);
            }

        }

        setDescription(aux);
        // System.out.println("\nTESTE8: " + Description);
    }

    static public void LerBaseDeDadosInicial(String s1, String s2) throws IOException
    {
        Netflix[] net = new Netflix[8807];
        byte[] ba;

        BufferedReader bf = new BufferedReader(new FileReader("teste2.csv"));

        for (int j = 0; j < 8807; j++) {
            net[j] = new Netflix(bf.readLine());
        }
        bf.close();

        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        
          arq.writeInt(8807);
          for (int i = 0; i < 8807; i++) {
          ba = net[i].toByteArray();
          arq.writeChar(' ');
          arq.writeInt(ba.length);
          arq.write(ba);
          }

          arq.close();
         
    }

    static public void TelaIncial() throws IOException {

        int x = 0;
        Crud c = new Crud();
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


            System.out.println("Entrar com uma opcao:");
            
            x= sc.nextInt();
            System.out.println(x);

            switch (x) {
                case 0:
                    
                    break;
                case 1:
                    c.preCreate();
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
                    
                    break;
                case 4:

                System.out.println("Qual Id deseja Deletar:");
                    int Num = sc.nextInt();
   
                    Boolean b1 = c.delete(Num);

                    if (b1) {
                        System.out.println("Deletado");
                    } else {
                        System.out.println("n tem esse registro ou ele ja esta deletado");
                    }
                    
                    break;
                case 5:
                    
                    break;
                case 6:
                LerBaseDeDadosInicial("teste2.csv", "teste.db");
                    break;

                default:
                    System.out.println("ERRO: Valor invalido:" + x);
            }

        } while (x != 0);
        sc.close();
    }

    public static void main(String[] args) throws IOException {
      
        TelaIncial();

    }
}
