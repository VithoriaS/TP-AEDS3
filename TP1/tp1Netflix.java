import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class tp1Netflix {
    
    public static void main(String[] args) throws IOException {

        BufferedReader bf = new BufferedReader(new FileReader("netflixCsv.csv"));

        String s1 = bf.readLine();

            while (s1 != null) {
                
                 
            
            int x = 1;
            String s2 = "";
            while (s1.length() > x) {
                s2 = s2 + s1.charAt(x);
                x++;
            }
            System.out.println(s2);
            
            FileWriter fw = new FileWriter("teste2.csv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(s2);
            pw.flush();
            pw.close();
            s1 = bf.readLine();

            }

            bf.close();
    }


}