import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class LZ {
    byte[] bytes;
    //Buffer på 16 kB
    final int BUFFER_SIZE = 16*1024;
    Queue buffer = new Queue(BUFFER_SIZE);
    // byte[] buffer = new byte[BUFFER_SIZE];
    // int position = 0;

    public LZ(byte[] bytes){
        this.bytes = bytes;
    }

    public LZ(){

    }


    public void compress() throws IOException{
        DataOutputStream dos = new DataOutputStream(new FileOutputStream("compressedLZ"));
        int last = 0;
        int position = 0;
        for(int i = 0; i<bytes.length; i++){
            //Sjekker om den er i bufferen
            int posBuff = checkInBuffer(bytes[i], buffer.getEnd()-1);
            //Ikke i bufferen posBuff = -1
            if(posBuff == -1){
                buffer.addToQueue(bytes[i]);
                position++;
            }
            else{
                //Prøver å bygge order
                //Foreløpige max verdier
                int max = buildWord(posBuff, i);
                int maxIndex = posBuff;
                //Fortsetter bakover i bufferen for å sjekke etter større order som kan referes
                while(posBuff != -1){
                    posBuff = checkInBuffer(bytes[i], posBuff-1);
                    if(posBuff == -1)break;
                    int temp = buildWord(posBuff, i);
                    if(temp > max){
                        max = temp;
                        maxIndex = posBuff;
                    }
                }
                //Bruker 6 short-verdier for å skrive bakover referanse = 6 bytes
                //max må derfor være større en 6 bytes(3*(short_size)=6)
                if(max > 6){
                    //Skriver ut mengden framover med ukomprierte data -- bruker short fordi byte ble for lite når jeg prøvde på en av filene -- teste igjen
                    dos.writeShort((short)(position-last));

                    // for(int j = last; j<position;j++) dos.writeByte((byte)bytes[j]);
                    //Skriver ut alle ukomprimerte bytes i intervallet mellom start og bakover referansen
                    byte[] temp = new byte[position-last];
                    int count = 0;
                    for(int j = last; j<position;j++) temp[count++] = (byte)bytes[j];
                    dos.write(temp);

                    //Skriver ut bakover referansen --  bruker short fordi da kan man se lenger bakover -- 32 kB buffer size
                    dos.writeShort((short)(-(i-maxIndex)));
                    //Skriver ut hvor mange bytes man skal hente fra bakover referansen
                    dos.writeShort((short)max);
                    
                    for(int j = 0; j<max; j++){ 
                        buffer.addToQueue(bytes[i++]);
                        position++;
                    }
                    
                    last = i;
                    i--;
                }
                else{ 
                    buffer.addToQueue(bytes[i]);
                    position++;
                }
            }
        }
        //Skriver ut mengden bytes ukomprimerte data før slutt
        dos.writeShort((short)(position-last));
        //Skriver ut siste del ukomprimerte data.
        for(int i = last; i<position; i++) dos.writeByte(bytes[i]);
        dos.close();
    }

    public void decompress(String path) throws IOException{
        DataOutputStream dos = new DataOutputStream(new FileOutputStream("decompressedFile"));
        DataInputStream dis = new DataInputStream(new FileInputStream(path));
        //Mengden ukomprimnert data i starten av filen.
        short start = dis.readShort();
        bytes = new byte[start];
        //Første del av ukomprimert data leses inn. 
        dis.readFully(bytes);
        dos.write(bytes);
        for(int j = 0; j<start; j++){
            // dos.writeByte(bytes[j]);
            // System.out.print((char)bytes[j]);
            buffer.addToQueue(bytes[j]);
        }
        while(dis.available() > 0){
            //Leser inn bakover referansen
            short back = dis.readShort();
            short copyAmount = dis.readShort();

            //Henter slutt verdien til bufferen
            int end = buffer.getEnd();
            bytes = new byte[copyAmount];
            int i = 0;
            //Legger til bakover referansens verdier fra bufferen. Ser tilbake i bufferen for hente bytesene.
            //Kan bli negativ siden queuen/bufferen er sirkulær: end kan være 3, og start 4, intervallet: [4,3]%BUFFER_SIZE
            for(int tempIndex = end+back; tempIndex<(end+back)+copyAmount; tempIndex++){
                byte index = buffer.get(tempIndex);
                // dos.writeByte(index);
                bytes[i++] = index;
                // System.out.print((char)index);
                buffer.addToQueue(index);
            }
            dos.write(bytes);

            //Mengden ukomprimert data som bare trengs å skrives ut. 
            start = dis.readShort();
            bytes = new byte[start];
            dis.readFully(bytes);
            for(int j = 0; j<start; j++){
                // dos.writeByte(bytes[j]);
                // System.out.print((char)bytes[j]);
                buffer.addToQueue(bytes[j]);
            }
            dos.write(bytes);
        }
        dis.close();
        dos.close();
    }

    //Sjekker bufferen etter git bytt og er etter gitt posisjon
    private int checkInBuffer(byte b, int pos){
        return buffer.contains(b, pos);
    }

    //Sjekker hvor mange bytes er like etterhverandre fra en gitt posisjon i
    //både bufferen og nåværende posisjon
    private int buildWord(int posBuff, int posByte){
        byte byte1 = bytes[posByte];
        byte buff1 = buffer.get(posBuff);
        int count = 0;
        while(buff1 == byte1 && posByte != bytes.length-1){
            count++;
            byte1 = bytes[++posByte];
            buff1 = buffer.get(++posBuff);
        }
        return count;
    
    }
}
