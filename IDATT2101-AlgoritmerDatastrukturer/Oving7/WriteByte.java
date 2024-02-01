import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteByte {
    private boolean isLast = false;
    private BitString last;
    private DataOutputStream dos;
    private byte[] output;
    private int count = 0;

    public WriteByte(int[] ft, int inputLength) throws IOException {
        dos = new DataOutputStream(new FileOutputStream("compressedHuffman"));
        //Går kjappere å skrive ut en stor block istedenfor enkel bytes
        output = new byte[inputLength];
        //Skriver ut frekvenstabellen
        for(int integer: ft){
            dos.writeInt(integer);
        }
    }

    public void writeByteToFile(BitString bs) throws IOException {
        BitString bitString = new BitString(bs);
        //Sjekker om det er noe igjen fra forrige runde og konkatterer den forrige med den vi har hentet
        if(isLast){
            bitString = BitString.concat(last, bitString);
        }
        //Om bitstringen er mindre enn 8, er den ikke stor nok for 1 byte
        if(bitString.getBitLength() <8){
            isLast = true;
            last = bitString;
            return;
        }
        int length = bitString.getBitLength();
        int amount = 8;
        //Hvor mange bytes som kan skrives ut
        int div = length/amount;
        //Hvor den resterende mengden
        int rest = length%amount;
        for(int i = 0; i<div; i++){
            //Henter bytene
            byte b = (byte)(bitString.getValue() >> (rest+(div-1-i)*8));
            //dos.writeByte(b);
            output[count++] = b;
        }
        //Hvis det er resternde bits, blir de lagt til for å brukes neste runde
        if(rest != 0){
            isLast = true;
            //Maskerer ut den siste delen
            int y = (0b11111111 >> (8-rest));
            long b = (byte) ((byte)bitString.getValue() & y);
            last = new BitString(b, rest);
        }
        else{ 
            isLast = false;
            last = null;
        }
        
    }

    public void writeLastByte() throws IOException {
        //Hvis den resternde bits, fyller vi den opp til en byte ved å legge til 0-ere
        if(isLast){
            int rest = 8 - last.getBitLength();
            long temp = 0 << rest;
            BitString b = BitString.concat(last, new BitString(temp,rest));
            byte by = (byte)b.getValue();
            //dos.writeByte(b);
            output[count++] = by;
        }
        //Lager en ny tabell som passer output
        byte[] temp = new byte[count];
        for(int i = 0; i<count; i++){
            temp[i] = output[i];
        }
        //Hvis det er resternde bytes, blir mengden skrevet ut, så vi vet når vi decompresser
        //hvor mange bits som skal skrives ut på den siste byten
        if(isLast) dos.writeByte((byte)last.getBitLength());
        else dos.writeByte((byte)0);
        dos.write(temp);
        dos.close();
    }

    public boolean getIsLast(){
        return isLast;
    }
    
}
