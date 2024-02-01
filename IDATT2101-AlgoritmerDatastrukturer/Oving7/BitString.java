

public class BitString {
    private long bitString;
    private int length;

    public BitString(long b, int length){
        this.bitString = b;
        this.length = length;
    }

    public BitString(byte b, int length){
        this.length = length;
        this.bitString = convertByte(b, length);
    }

    public BitString(BitString bs){
        this.bitString = bs.getValue();
        this.length = bs.getBitLength();
    }

    public void setLength(int length){
        this.length = length;
    }

    //Konverterer en byte til en long
    //Dersom du har en negativ long, får du mange 1-ere foran bytens bitstring
    //Da får man problemer med concat metoden
    public long convertByte(byte b, int length){
        long temp = 0;
        for(long i = 1<<length-1; i != 0; i >>= 1){
            if((b & i) == 0){
                temp = (temp << 1);
            }
            else temp = ((temp << 1) | 1);
        }
        return temp;
    }

    public void setBitString(long bitString){
        this.bitString = bitString;
    }
    public long getValue(){
        return bitString;
    }

    public int getBitLength(){
        return length;
    }

    //Legger til 1 i bitstringen
    public void addOne(){
        bitString = (bitString << 1) | 1;
        length++;
    }

    //Legger til 0
    public void addZero(){
        bitString = (bitString << 1);
        length++;
    }

    //Fjerner bit lengst til venstre
    public void remove(){
        bitString = bitString >> 1;
        length--;
    }

    
    //Henter ut rest fra en bit
    //De $rest mengden fra venstre
    public void getRest(int rest){
        long temp = ~(0 << rest);
        bitString = (bitString & temp);
        length = rest;
    }

    //Legger sammen to bitstringer, ved å lage en ny bitstring
    //Legger sammen lengdene og legger bitstring2 oppå bitstring1 venstreforskyvet bitstring2.lengde
    public static BitString concat(BitString bitString1, BitString bitString2){
        BitString ny = new BitString(0,0);
        ny.setLength(bitString1.getBitLength()+bitString2.getBitLength());
        long temp = bitString2.getValue() | (bitString1.getValue() << bitString2.getBitLength());
        ny.setBitString(temp);
        return ny;
    }

    //Henter String verdien av bitstrings verdi
    public String toString(){
        if(length == 0) return "";
        String s = "";
        for(long tempBit = 1 << (length-1); tempBit != 0; tempBit >>= 1){
            s += ( (bitString & tempBit) == 0) ? "0" : "1";
        }
        return s;
    }
}
