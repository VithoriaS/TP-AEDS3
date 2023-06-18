import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class viginere {
   

     private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static byte[] encrypt(byte[] plaintext, byte[] key) {
        byte[] ciphertext = new byte[plaintext.length];

        int keyIndex = 0;
        for (int i = 0; i < plaintext.length; i++) {
            byte plainByte = plaintext[i];
            if (Character.isLetter(plainByte)) {
                boolean isUpperCase = Character.isUpperCase(plainByte);
                byte keyByte = key[keyIndex];
                int plainIndex = ALPHABET.indexOf(Character.toUpperCase(plainByte));
                int keyCharIndex = ALPHABET.indexOf(Character.toUpperCase((char) keyByte));
                int cipherIndex = (plainIndex + keyCharIndex) % ALPHABET.length();
                char cipherChar = ALPHABET.charAt(cipherIndex);

                if (!isUpperCase) {
                    cipherChar = Character.toLowerCase(cipherChar);
                }

                ciphertext[i] = (byte) cipherChar;

                keyIndex = (keyIndex + 1) % key.length;
            } else {
                ciphertext[i] = plainByte;
            }
        }

        return ciphertext;
    }

    public static byte[] decrypt(byte[] ciphertext, byte[] key) {
        byte[] plaintext = new byte[ciphertext.length];

        int keyIndex = 0;
        for (int i = 0; i < ciphertext.length; i++) {
            byte cipherByte = ciphertext[i];
            if (Character.isLetter(cipherByte)) {
                boolean isUpperCase = Character.isUpperCase(cipherByte);
                byte keyByte = key[keyIndex];
                int cipherIndex = ALPHABET.indexOf(Character.toUpperCase(cipherByte));
                int keyCharIndex = ALPHABET.indexOf(Character.toUpperCase((char) keyByte));
                int plainIndex = (cipherIndex - keyCharIndex + ALPHABET.length()) % ALPHABET.length();
                char plainChar = ALPHABET.charAt(plainIndex);

                if (!isUpperCase) {
                    plainChar = Character.toLowerCase(plainChar);
                }

                plaintext[i] = (byte) plainChar;

                keyIndex = (keyIndex + 1) % key.length;
            } else {
                plaintext[i] = cipherByte;
            }
        }

        return plaintext;
    }

    

        public static byte[] readFileToByteArray(String string) {
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;

        try {
            fis = new FileInputStream(string);
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


