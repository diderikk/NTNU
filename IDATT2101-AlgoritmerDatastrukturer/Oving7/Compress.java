import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Compress {
    public static void main(String[] args) {
        try{
            //Komprimerer med Lempel-Ziv
            DataInputStream dis1 = new DataInputStream(new FileInputStream("diverse.txt"));
            byte[] bytes = dis1.readAllBytes();
            dis1.close();
            LZ LZ = new LZ(bytes);
            LZ.compress();

            DataInputStream dis = new DataInputStream(new FileInputStream("compressedLZ"));
            bytes = dis.readAllBytes();
            dis.close();
            Huffman h = new Huffman(bytes);
            h.compress(bytes);

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
