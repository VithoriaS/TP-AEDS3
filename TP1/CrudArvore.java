import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CrudArvore {
    Crud c ;
    arvore ARV ;

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


    public void LerRegistroEfazerArvore( String s1, String s2 ,int NumRegistros) throws IOException
    {

        byte[] ba;

        BufferedReader bf = new BufferedReader(new FileReader(s1));
        RandomAccessFile arq = new RandomAccessFile(s2, "rw");
        arq.writeInt(0);
        arq.close();
        Crud CRU = new Crud();
        
        for (int j = 0; j < NumRegistros; j++) {
            Netflix net =  new Netflix(bf.readLine());
            CRU.create(net);
            createArv2(net);
        }
        bf.close();

      
    
        
            
    }
}
