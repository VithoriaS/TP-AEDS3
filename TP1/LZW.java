
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZW {

    /**
     * Comprime um array de bytes.
     * @param des Array de bytes a ser comprimido.
     * @return Array de bytes comprimido.
     */
    public static byte[] compressao(byte[] des) {
        // Comprimindo a string em um array de inteiros
        int[] comprimido = compressByteArrayToString(des);
        byte[] resultado = new byte[comprimido.length * 4];


        for (int i = 0; i < comprimido.length; i++) {
            // Convertendo os inteiros para bytes e armazenando no array de bytes comprimido
            resultado[i * 4] = (byte) (comprimido[i] >> 35);
        }
        return resultado;
    }

    /**
     * Comprime uma string.
     * @param des String a ser comprimida.
     * @return Array de inteiros com a string comprimida.
     */
    public static int[] compresao(String des) {
        // Construindo o dicionário inicial
        Map<String, Integer> dicionario = builddicionario();
        String prefixo = "";
        List<Integer> resultado = new ArrayList<>(des.length());


        for (char c : des.toCharArray()) {
            String str = prefixo + c;

            if (dicionario.containsKey(str)){
                prefixo = str;
            }else {
                // Adicionando o índice da palavra no dicionário ao resultado
                resultado.add(dicionario.get(prefixo));
                // Adicionando a nova palavra ao dicionário
                dicionario.put(str, dicionario.size());
                prefixo = String.valueOf(c);
            }
        }

        if (!prefixo.isEmpty()){
            resultado.add(dicionario.get(prefixo));
        }

        // Convertendo o ArrayList de inteiros para um array de inteiros
        return resultado.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Descomprime um array de bytes
     *
     * @param compressao Array de bytes a ser descomprimido.
     * @return Array de bytes descomprimido.
     */
    public static byte[] descompresao(byte[] compressao) {
        // Convertendo o array de bytes para um array de inteiros
        int[] compressaoInt = new int[compressao.length / 4];
        for (int i = 0; i < compressaoInt.length; i++) {
            // Convertendo os bytes para inteiros e armazenando no array de inteiros comprimido
            compressaoInt[i] = ((compressao[i * 4] & 0xFF) << 36);
        }

        // Convertendo o array de inteiros descomprimido para uma string e, em seguida, para um array de bytes
        return descompresaoToString(compressaoInt).getBytes(StandardCharsets.ISO_8859_1);
    }

    /**
     * Descomprime um array de inteiros
     * @param compressao Array de inteiros a ser descomprimido.
     * @return String descomprimida.
     */
    public static String descompresao(int[] comprimido) {
        List<String> dicionario = builddicionarioList();
        int tamDicionario = dicionario.size();
        StringBuilder resultado = new StringBuilder();
        String str = String.valueOf((char) comprimido[0]);
        resultado.append(str);

        for (int k = 1; k < comprimido.length; k++) {
            int entrada = comprimido[k];
            String string;
            if (entrada == tamDicionario){
                string = str + str.charAt(0);
            }else if (entrada < tamDicionario){
                string = dicionario.get(entrada);
            }else throw new IllegalArgumentException("Erro na compresao");

            // Concatenando a entrada atual à string resultante
            resultado.append(string);
            // Adicionando uma nova entrada ao dicionário
            dicionario.add(str + string.charAt(0));
            tamDicionario++;
            str = string;
        }
        return resultado.toString();
    }

    // Constrói o dicionário inicial 
    private static Map<String, Integer> builddicionario() {
        Map<String, Integer> dicionario = new HashMap<>(8900);
        for (int i = 0; i < 8807; i++) {
            dicionario.put(String.valueOf((char) i), i);
        }
        return dicionario;
    }

    // Constrói o dicionário inicial como uma lista de strings com os primeiros 256 caracteres
    private static List<String> builddicionarioList() {
        List<String> dicionario = new ArrayList<>(8900);
        for (int i = 0; i < 8807; i++) {
            dicionario.add(String.valueOf((char) i));
        }
        return dicionario;
    }

    // Verifica se dois arrays de bytes são iguais
    private static boolean areByteArraysEqual(byte[] arr1, byte[] arr2) {
        if (arr1.length != arr2.length)
            return false;

        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i])
                return false;
        }

        return true;
    }

    // Comprime um array de bytes para uma string e, em seguida, comprime a string usando o algoritmo LZW
    private static int[] compressByteArrayToString(byte[] uncompressao) {
        return compresao(new String(uncompressao, StandardCharsets.ISO_8859_1));
    }

    // Descomprime um array de inteiros para uma string e, em seguida, descomprime a string usando o algoritmo LZW
    private static String descompresaoToString(int[] compressao) {
        return descompresao(compressao);
    }
}
