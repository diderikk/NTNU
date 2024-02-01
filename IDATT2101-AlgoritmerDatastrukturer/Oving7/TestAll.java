import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class TestAll {
    public static void main(String[] args) {
        Instant start = Instant.now();
           try{
            Instant startZcom = Instant.now();
            DataInputStream dis1 = new DataInputStream(new FileInputStream("diverse.txt"));
            byte[] bytes = dis1.readAllBytes();
            dis1.close();
            LZ LZ = new LZ(bytes);
            LZ.compress();
            Instant endZcom = Instant.now();

            Instant startHuffCom = Instant.now();
            DataInputStream dis = new DataInputStream(new FileInputStream("compressedLZ"));
            bytes = dis.readAllBytes();
            dis.close();
            Huffman h = new Huffman(bytes);
            h.compress(bytes);
            Instant endHuffCom = Instant.now();

            Instant startHuffDe = Instant.now();
            Huffman h1 = new Huffman();
            h1.decompress("compressedHuffman");
            Instant endHuffDe = Instant.now();

            Instant startZDe = Instant.now();
            LZ LZ1 = new LZ();
            LZ1.decompress("decompressedHuffman");
            Instant endZDe = Instant.now();

            long zcom = Duration.between(startZcom,endZcom).toSeconds();
            long huffcom = Duration.between(startHuffCom, endHuffCom).toSeconds();
            long huffde = Duration.between(startHuffDe, endHuffDe).toSeconds();
            long zde = Duration.between(startZDe, endZDe).toSeconds();

            System.out.printf("Lempel Ziv compress: %d\n",zcom);
            System.out.printf("Huffman compress: %d\n",huffcom);
            System.out.printf("Huffman decompress: %d\n",huffde);
            System.out.printf("Lempel Ziv decompress: %d\n",zde);

           }catch(IOException e){
               e.printStackTrace();
           }
           Instant end = Instant.now();

           long timeElapsed = Duration.between(start, end).toSeconds();
           System.out.printf("Total time: %d\n", timeElapsed);


    }
}
