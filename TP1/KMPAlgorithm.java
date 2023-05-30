import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class KMPAlgorithm {
    public int comparacoes = 0;
     void kmp(String s1) throws IOException
    {
      
        System.out.println(s1);
    
        byte[] pattern = s1.getBytes();
        int[] lps = computeLPSArray(pattern);
        File file = new File("teste.db"); 
        byte[] text = readFileToByteArray(file);

        int patternIndex = 0;
        int textIndex = 0;
        while (textIndex < text.length) {
            if (pattern[patternIndex] == text[textIndex]) {
                comparacoes++;
                patternIndex++;
                textIndex++;
            }
            if (patternIndex == pattern.length) {
                System.out.println("Pattern found at index " + (textIndex - patternIndex));
                patternIndex = lps[patternIndex - 1];
            } else if (textIndex < text.length && pattern[patternIndex] != text[textIndex]) {
                if (patternIndex != 0)
                    patternIndex = lps[patternIndex - 1];
                else
                    textIndex++;
            }
        }
 

    
        }
       
    

    private static int[] computeLPSArray(byte[] pattern) {
        int[] lps = new int[pattern.length];
        int length = 0;
        int i = 1;

        while (i < pattern.length) {
            if (pattern[i] == pattern[length]) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    private static byte[] readFileToByteArray(File file) {
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;

        try {
            fis = new FileInputStream(file);
            baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    
}
