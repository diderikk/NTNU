import java.io.IOException;

public class Decompress {
    public static void main(String[] args) {
        try{
            Huffman h1 = new Huffman();
            h1.decompress("compressedHuffman");

            LZ LZ1 = new LZ();
            LZ1.decompress("decompressedHuffman");
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
