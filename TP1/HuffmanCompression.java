// Huffman Coding in Java

import java.util.PriorityQueue;

import javax.lang.model.util.ElementScanner14;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



class HuffmanNode implements Comparable<HuffmanNode> {
    byte data;
    int frequency;
    HuffmanNode left, right;

    public HuffmanNode(byte data, int frequency) {
        this.data = data;
        this.frequency = frequency;
    }

    public HuffmanNode(byte data, int frequency, HuffmanNode left, HuffmanNode right) {
        this.data = data;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(HuffmanNode node) {
        return this.frequency - node.frequency;
    }

}

public class HuffmanCompression {
    private Map<Byte, String> byteToCode;
    private HuffmanNode root;

    public HuffmanCompression(Map<Byte, Integer> byteFrequency) {
        byteToCode = new HashMap<>();
        buildHuffmanTree(byteFrequency);
    }

    public HuffmanCompression() {
        
    }

    private void buildHuffmanTree(Map<Byte, Integer> byteFrequency) {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();

        for (Map.Entry<Byte, Integer> entry : byteFrequency.entrySet()) {
            byte b = entry.getKey();
            int frequency = entry.getValue();
            pq.add(new HuffmanNode(b, frequency));
        }

        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode parent = new HuffmanNode((byte) 0, left.frequency + right.frequency, left, right);
            pq.add(parent);
        }

        if (!pq.isEmpty()) {
            root = pq.poll();
            generateCodes(root, "");
        }
    }

    private void generateCodes(HuffmanNode node, String code) {
        if (node == null)
            return;

        if (node.left == null && node.right == null) {
            byteToCode.put(node.data, code);
            return;
        }

        generateCodes(node.left, code + "0");
        generateCodes(node.right, code + "1");
    }

    public byte[] compress(byte[] data) {
        StringBuilder compressedData = new StringBuilder();

        for (byte b : data) {
            compressedData.append(byteToCode.get(b));
        }

        int numBytes = (compressedData.length() + 7) / 8;
        byte[] compressedBytes = new byte[numBytes];

        int currentIndex = 0;
        for (int i = 0; i < compressedData.length(); i += 8) {
            String binaryString = compressedData.substring(i, Math.min(i + 8, compressedData.length()));
            byte compressedByte = (byte) Integer.parseInt(binaryString, 2);
            compressedBytes[currentIndex] = compressedByte;
            currentIndex++;
        }

        return compressedBytes;
    }

    

    static public void teste() throws IOException {

        Path filePath = Paths.get("teste.db");
        byte[] fileBytes = Files.readAllBytes(filePath);
    
        Map<Byte, Integer> byteFrequency = new HashMap<>();
        for (byte b : fileBytes) {
          byteFrequency.put(b, byteFrequency.getOrDefault(b, 0) + 1);
        }
    
        List<Byte> sortedBytes = new ArrayList<>(byteFrequency.keySet());
        sortedBytes.sort(Comparator.comparingInt(byteFrequency::get));
        for (Byte b : sortedBytes) {
          System.out.println(b + ": " + byteFrequency.get(b));
        }
        
      }
    
      public void storeDataInFile(byte[] compressedData, Map<Byte, Integer> byteFrequency, String filename) {
       

        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(filename))) {
            // Escreve a quantidade de entradas da byteFrequency no arquivo
            output.writeInt(byteFrequency.size());

            // Escreve a byteFrequency no arquivo
            for (Map.Entry<Byte, Integer> entry : byteFrequency.entrySet()) {
                output.writeByte(entry.getKey());
                output.writeInt(entry.getValue());
            }

            // Escreve a compressedData no arquivo
            output.write(compressedData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    public byte[] decompress(byte[] compressedData, Map<Byte, Integer> byteFrequency) {
        buildHuffmanTree(byteFrequency);

        StringBuilder bitString = new StringBuilder();
        for (byte b : compressedData) {
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            bitString.append(binaryString);
        }

        List<Byte> decompressedBytes = new ArrayList<>();
        HuffmanNode currentNode = root;
        for (int i = 0; i < bitString.length(); i++) {
            char bit = bitString.charAt(i);
            if (bit == '0') {
                currentNode = currentNode.left;
            } else if (bit == '1') {
                currentNode = currentNode.right;
            }

            if (currentNode.left == null && currentNode.right == null) {
                decompressedBytes.add(currentNode.data);
                currentNode = root;
            }
        }

        byte[] decompressedData = new byte[decompressedBytes.size()];
        for (int i = 0; i < decompressedBytes.size(); i++) {
            decompressedData[i] = decompressedBytes.get(i);
        }

        return decompressedData;
    }

    public void retrieveDataFromFile(String filename, int k) {
        try (DataInputStream input = new DataInputStream(new FileInputStream(filename))) {
            // Lê a quantidade de entradas da byteFrequency do arquivo
            int numEntries = input.readInt();

            // Lê a byteFrequency do arquivo
            Map<Byte, Integer> byteFrequency = new HashMap<>();
            for (int i = 0; i < numEntries; i++) {
                byte key = input.readByte();
                int value = input.readInt();
                byteFrequency.put(key, value);
            }

            // Lê a compressedData do arquivo
            byte[] compressedData = new byte[input.available()];
            input.readFully(compressedData);

          
            //  descomprimir os dados
            HuffmanCompression huffman = new HuffmanCompression(byteFrequency);
            byte[] decompressedData = huffman.decompress(compressedData, byteFrequency);
            if (k == 1) {
                writeDecompressedDataToFile(decompressedData, "teste.db");
            }
            else{
                k  = k -1;
                
                writeDecompressedDataToFile(decompressedData, "testeHuffman" + String.valueOf(k) + ".db");
            }
            
           // System.out.println("Dados descomprimidos: " + Arrays.toString(decompressedData));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public void writeDecompressedDataToFile(byte[] decompressedData, String filename) {
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(filename))) {
            output.write(decompressedData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void comprimir1(String fileName) throws IOException
    {
        long tempoInicial = System.currentTimeMillis();
        
       
   


        Path filePath = Paths.get(fileName);
        byte[] fileBytes = Files.readAllBytes(filePath);
    
        Map<Byte, Integer> byteFrequency = new HashMap<>();
        for (byte b : fileBytes) {
          byteFrequency.put(b, byteFrequency.getOrDefault(b, 0) + 1);
        }

        List<Byte> sortedBytes = new ArrayList<>(byteFrequency.keySet());
        sortedBytes.sort(Comparator.comparingInt(byteFrequency::get));

        
       

        // Compressão
        HuffmanCompression compressor = new HuffmanCompression(byteFrequency);
        // this.byteToCode = compressor.byteToCode;
       // this.root = compressor.root;

        byte[] compressedData = compressor.compress(fileBytes);
        

        String s2 = "";
        String s3 = "";
        if(fileName.contains("Huffman"))
        {
         int i = fileName.length() -1;
         while(fileName.charAt(i) != 'n')
         {
             if (fileName.charAt(i) == '.' ||fileName.charAt(i) == 'd' || fileName.charAt(i) == 'b') {
                
             }
             else {
                s3 = s3 + fileName.charAt(i);
             }
             i--;
             
         }
         for (int j = 0; j < i; j++) {
             s2 = s2 + fileName.charAt(j) ;
         }
         s2 = s2 + 'n';
         int aux = Integer.parseInt(s3);
         aux++;
         s2 = s2 + String.valueOf(aux);
         s2 = s2 + ".db";
         fileName = s2;
        }
        else{
            fileName = "testeHuffman1.db";
        }

        compressor.storeDataInFile(compressedData, byteFrequency, fileName);

   

    }

      public static void main(String[] args) throws IOException {
        
        // Suponha que você tenha obtido o resultado da frequência de ocorrência dos bytes


       // System.out.println("Dados comprimidos: " + Arrays.toString(compressedData));

        // Descompressão

    //    compressor.retrieveDataFromFile("testeHuffman1.db");
       // System.out.println("Dados descomprimidos: " + new String(decompressedData));
    }

}