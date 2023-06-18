import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class viginere {
   

     private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

      // Método para criptografar um array de bytes usando o algoritmo de Vigenère
    public static byte[] encrypt(byte[] plaintext, byte[] key) {
        byte[] ciphertext = new byte[plaintext.length];

        int keyIndex = 0;
        for (int i = 0; i < plaintext.length; i++) {
            byte plainByte = plaintext[i];
            if (Character.isLetter(plainByte)) { // Verifica se o byte é uma letra
                boolean isUpperCase = Character.isUpperCase(plainByte); // Verifica se a letra é maiúscula

                byte keyByte = key[keyIndex];
                int plainIndex = ALPHABET.indexOf(Character.toUpperCase(plainByte)); // Obtém o índice da letra no alfabeto
                int keyCharIndex = ALPHABET.indexOf(Character.toUpperCase((char) keyByte)); // Obtém o índice da letra da chave no alfabeto
                int cipherIndex = (plainIndex + keyCharIndex) % ALPHABET.length(); // Calcula o índice da letra cifrada
                char cipherChar = ALPHABET.charAt(cipherIndex); // Obtém a letra cifrada

                if (!isUpperCase) {
                    cipherChar = Character.toLowerCase(cipherChar); // Mantém a letra em minúsculo se a original era minúscula
                }

                ciphertext[i] = (byte) cipherChar; // Armazena a letra cifrada no array de bytes

                keyIndex = (keyIndex + 1) % key.length; // Atualiza o índice da chave para o próximo caractere
            } else {
                ciphertext[i] = plainByte; // Se não for uma letra, mantém o byte original
            }
        }

        return ciphertext;
    }

    // Método para descriptografar um array de bytes cifrado usando o algoritmo de Vigenère
    public static byte[] decrypt(byte[] ciphertext, byte[] key) {
        byte[] plaintext = new byte[ciphertext.length];

        int keyIndex = 0;
        for (int i = 0; i < ciphertext.length; i++) {
            byte cipherByte = ciphertext[i];
            if (Character.isLetter(cipherByte)) { // Verifica se o byte é uma letra
                boolean isUpperCase = Character.isUpperCase(cipherByte); // Verifica se a letra é maiúscula

                byte keyByte = key[keyIndex];
                int cipherIndex = ALPHABET.indexOf(Character.toUpperCase(cipherByte)); // Obtém o índice da letra cifrada no alfabeto
                int keyCharIndex = ALPHABET.indexOf(Character.toUpperCase((char) keyByte)); // Obtém o índice da letra da chave no alfabeto
                int plainIndex = (cipherIndex - keyCharIndex + ALPHABET.length()) % ALPHABET.length(); // Calcula o índice da letra decifrada
                char plainChar = ALPHABET.charAt(plainIndex); // Obtém a letra decifrada

                if (!isUpperCase) {
                    plainChar = Character.toLowerCase(plainChar); // Mantém a letra em minúsculo se a original era minúscula
                }

                plaintext[i] = (byte) plainChar; // Armazena a letra decifrada no array de bytes

                keyIndex = (keyIndex + 1) % key.length; // Atualiza o índice da chave para o próximo caractere
            } else {
                plaintext[i] = cipherByte; // Se não for uma letra, mantém o byte original
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


