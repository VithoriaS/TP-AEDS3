import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ProcessBuilder.Redirect.Type;
import java.util.Scanner;

public class Crud {

    long[] posicoes;

    public void setPosicoes(long[] posicoes) {
        this.posicoes = posicoes;
    }

    public long[] getPosicoes() {
        return posicoes;
    }

    public void preCreate() throws IOException
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



        Netflix net = new Netflix(0, Type, Name, Cast,
         Data_added, min, seasons, Listed_in, Description);
         
        create(net);
    }

    public void create(Netflix net) throws IOException {
        
        int len = 0;
        char c;
        int x = 0;
        byte[] ba;

        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        len = arq.readInt();
        len = len + 1;
        net.Id = len;

        net.printar();

        arq.seek(0);
        arq.write(len);
        arq.seek(arq.length());
        ba = net.toByteArray();
        arq.writeChar(' ');
        arq.writeInt(ba.length);
        arq.write(ba);

        arq.close();
    }

    public long Achar(int IdLegal) throws IOException {
        long pos = 0;
        char c;
        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        int len = arq.readInt();
        int x = 0;
        byte[] ba;

         while (x < len) {
            c = arq.readChar();
            if (c != '*') {
                int length = arq.readInt();
                
                ba = new byte[length];
                arq.read(ba);
                
                Netflix net_temp = new Netflix();
                net_temp.fromByteArray(ba);
             
                if (IdLegal == net_temp.Id) {
                    pos = arq.getFilePointer();
                    arq.close();
                    return pos;
                }
                
            }
            else
            {
                int length = arq.readInt();
                arq.skipBytes(length);
            }
           
            x++;
        }
        arq.close();
        return -1;

    
    }

    public Netflix  read(int IdLegal) throws IOException {
        int len = 0;
        char c;
        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        len = arq.readInt();
        int x = 0;
        byte[] ba;
        
        // len = total de ID's
        // while ( id != len) {
        // if ( lapide != '*') {
        // pega obj ????
        // se obj.id = IdLegal
        // return obj e termina
        // }
        // } return null

        while (x < len) {
            c = arq.readChar();
            if (c != '*') {
                int length = arq.readInt();
                
                ba = new byte[length];
                arq.read(ba);
                
                Netflix net_temp = new Netflix();
                net_temp.fromByteArray(ba);
             
                if (IdLegal == net_temp.Id) {
                    arq.close();
                    return net_temp;
                }
                
            }
            else
            {
                int length = arq.readInt();
                arq.skipBytes(length);
            }
           
            x++;
        }
        arq.close();
        return null;

    
    }


    public boolean uptate (int Id) throws IOException{

        long result = Achar(Id);
    
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

        
       // Netflix net = new Netflix(0, Type, Name, Cast,
        //Data_added, min, seasons, Listed_in, Description);
         
        //create(net);

        return false;
    }

    public boolean delete(int IdLegal) throws IOException
    {
        int len = 0;
        char c;
        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        len = arq.readInt();
        int x = 0;
        byte[] ba;
        long pos= 0;
        
        while (x < len) {
            pos =  arq.getFilePointer();
           // System.out.println(pos);
            c = arq.readChar();
          //  System.out.println(c);

            if (c != '*') {
                int length = arq.readInt();
                ba = new byte[length];
                arq.read(ba);
                Netflix net_temp = new Netflix();

                net_temp.fromByteArray(ba);
                if (IdLegal == net_temp.Id) {
                    System.out.println("teste");
                    arq.seek(pos);
                    arq.writeChar('*');
                    
                    
                    return true;
                   
                }
                
            }

            x++;
        }
        
        System.out.println("teste1");
        return false;
    }
}
