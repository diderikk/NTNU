import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Huffman {
    private Tre[] trees;
    private Heap heap;
    int[] frequencyTable;
    WriteByte writeByte;
    DataOutputStream dos;

    //Bruker når man skal compresse
    public Huffman(byte[] bytes) throws IOException {
        //Lager et frekvenstre fra alle bytes fra en fil
        frequencyTable = new int[256];
        trees = finnFrekvens(bytes);
        //Lager en prioriteskø
        heap = new Heap(trees);
        //Filen som har ansvar for fordeling av bytes og å skrive dem ut
        writeByte = new WriteByte(frequencyTable, bytes.length);
    }

    //Brukers om man skal decompresse
    public Huffman() throws FileNotFoundException {
        frequencyTable = new int[256];
        dos = new DataOutputStream(new FileOutputStream("decompressedHuffman"));
    }

    //Bygger treet
    public Tre buildTree() {
        Tre result = null;
        //Kjører gjennom helt til vi har siste tre igjen i heapen
        //Vil da være det riktige treet
        while (heap.len > 1) {
            Tre tre1 = heap.hentMin();
            Tre tre2 = heap.hentMin();
            result = tre1.settSammen(tre2);
            heap.settInn(result);
        }
        return result;
    }

    //Compresser bytes fra en fil
    public void compress(byte[] bytes) throws IOException {
        //Bygger treet
        Tre tree = buildTree();
        //Kjører gjennom hver for å finne bitstring verdien til den byten
        //Bytes som forekommer færre ganger kan ofte få lengre bitstring enn 8, men ellers får vi mindre enn 8
            for(byte b : bytes){
                BitString bitString = new BitString(0,0);
                //Ser etter bitstringen til gitt verdi
                lookUpTree(tree.rot, bitString, b);
            }
            //Skriver ut alle bytesene
            writeByte.writeLastByte();
    }

    //Ser etter git byte, legger til 1 når den går til høyre og 0 om den går til venstre.
    //Fjerner 1 bit etter hver rekursjon
    //Ueffektiv, fordi den stopper ikke før hele treet er gjennomgått
    //Kunne lagt alle bitstringer inn i en array[256], hadde vært raskere fordi vi hadde fått O(1)
    private void lookUpTree(Node n, BitString bitString, byte b) throws IOException {
        if (n != null) {
            //Skriver bitstringer til en byte array
            if (n.d == b)
                writeByte.writeByteToFile(bitString);
            bitString.addOne();
            lookUpTree(n.høyre, bitString, b);
            bitString.remove();
            bitString.addZero();
            lookUpTree(n.venstre, bitString, b);
            bitString.remove();
        }

    }

    //Decompresser en Huffman-compressed fil
    public void decompress(String fileName) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(fileName));
        //Lager en frevenstabel, ved å lese fra fil
        int count = 0;
        for(int i = 0; i<frequencyTable.length;i++){
            frequencyTable[i] = dis.readInt();
            if(frequencyTable[i] >0)count++;
        }
        //Siste byte i compressoren trenger nødvendigvis ikke å være 0, lengden blir skrevet til fil.
        byte lastLength = dis.readByte();
        //Lager treet
        Tre tre[] = new Tre[count];
        count = 0;
        for(int i = 0; i<frequencyTable.length; i++){
            if(frequencyTable[i]>0){
                tre[count++] = new Tre((byte)(i-128), frequencyTable[i]);
            }
        }
        heap = new Heap(tre);
        Tre t = buildTree();
        
        //Leser inn resten av bytesene i filen
        BitString bitString = new BitString(0, 0);
        byte[] bytes = dis.readAllBytes();
        dis.close();
        int l = bytes.length;
        //Om det er en halvferdig byte på slutten, må den behandles anderledes.
        if(lastLength > 0) l--;
        for(int i = 0; i<l;i++){
            //Lager en bitstring av en byte, blir konvertert
            BitString bit = new BitString(bytes[i],8);
            //Konkaterrer den med den forrige bitstring/om det er noe igjen fra forrige bitstring
            bitString = BitString.concat(bitString, bit);
            bitString = decompressPrint(bitString, t);
        }
        if(lastLength > 0){
            byte b = (byte)(bytes[l] >> 8-lastLength);
            BitString bit = new BitString(b,lastLength);
            bitString = BitString.concat(bitString, bit);
            decompressPrint(bitString, t);
        }
        

    }

    //Skriver ut bytes etterhvert som den finner dem i treet
    private BitString decompressPrint(BitString bitString, Tre t) throws IOException {
        Node node = t.rot;
        int count = 0;
        //Sammenligner alle bit, begynner fremst i bitstrigen, og går bakover
        for(long i = 1<<bitString.getBitLength()-1; i != 0; i >>= 1){
            count++;
            if((bitString.getValue() & i) == 0){
                node = node.venstre;
            }
            else{ 
                node = node.høyre;
            }
            //Når venstre eller høyre node==null er vi ved en løvnode
            if(node.venstre == null){
                //Skriver byten ved løvnoden ut til fil.
                dos.writeByte((byte)node.d);
                //Henter ut den resterende bitstringen, ved å fjerne de brukte bitsene
                bitString.getRest(bitString.getBitLength()-count);
                //Setter node til rot igjen og count til 0
                node = t.rot;
                count = 0;
            }
        }
        return bitString;
    }

    //Finner frekvenstabellen og lager trær av alle brukte frekvenser
    private Tre[] finnFrekvens(byte[] bytes){
        int count = 0;
        for(byte b : bytes){
            int index = b+128;
            if(frequencyTable[index] == 0) count++;
            frequencyTable[index]++;
        }
        Tre tre[] = new Tre[count];
        count = 0;
        for(int i = 0; i<frequencyTable.length; i++){
            if(frequencyTable[i]>0){
                tre[count++] = new Tre((byte)(i-128), frequencyTable[i]);
            }
        }
        return tre;
    }
}
