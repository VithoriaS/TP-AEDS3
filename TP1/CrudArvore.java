import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class CrudArvore {
    Crud c ;
    arvore ARV ;
    Scanner sc = new Scanner(System.in);
    CrudArvore() throws IOException
    {
    c = new Crud();
    ARV = new arvore(8, "indexArv.db");
    }

    public void createArv() throws IOException {
        Netflix net = c.preCreate();
        long pos = c.createPos(net);
        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        int id = arq.readInt();
        arq.close();
        ARV.create(id, pos);
        
    }

    
    public void createArv2(Netflix net) throws IOException {
        
        long pos = c.createPos(net);
        RandomAccessFile arq = new RandomAccessFile("teste.db", "rw");
        int id = arq.readInt();
        arq.close();
        ARV.create(id, pos);
        
    }

    public void readArv() throws IOException
    {
        
        System.out.println("Qual Id deseja procurar:");

        long pos = ARV.read(sc.nextInt());

        if (pos == -1) {
            System.out.println("Nao Tem");
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

    public void LerRegistroEfazerArvore( String s1, String s2 ,int NumRegistros) throws IOException
    {

        byte[] ba;

        BufferedReader bf = new BufferedReader(new FileReader(s1));
        RandomAccessFile arq = new RandomAccessFile(s2, "rw");
        arq.writeInt(0);
        arq.close();
        Crud CRU = new Crud();
      //  arq.writeInt(NumRegistros);
        for (int j = 0; j < NumRegistros; j++) {
            Netflix net =  new Netflix(bf.readLine());
      //      ba = net.toByteArray();
          //  arq.writeChar(' ');
          //  arq.writeInt(ba.length);
         //   arq.write(ba);
           // CRU.create(net);
            createArv2(net);
        }
        bf.close();
      
    }
}
