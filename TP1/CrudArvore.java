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
}
