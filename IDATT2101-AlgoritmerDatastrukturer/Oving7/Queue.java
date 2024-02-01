public class Queue {
    private byte[] buffer;
    private int start = 0;
    private int end = 0;
    private int size = 0;
    private int last = -1;


    public Queue(int size){
        buffer = new byte[size];
    }

    //Returner byte verdien ved en gitt indeks
    public byte get(int index){
        if(index >= buffer.length){
            int i = index%buffer.length;
            return buffer[i];
        }
        //Hvis index er mindre enn 0, kan skje fordi queuen er sirkulær. 
        else if(index < 0){
            int i = buffer.length+(index%buffer.length);
            if(i == buffer.length) i = 0;
            return buffer[i];
        }
        return buffer[index];
    }

    public boolean empty(){
        return size == 0;
    }

    public boolean full(){
        return size == buffer.length;
    }
    //Hvis quenen er full, popes den fremste verdien.
    public boolean addToQueue(byte b){
        if(full()){
            pop();
        }
        buffer[end] = b;
        end = (end+1)%buffer.length;
        size++;
        return true;
    }

    //Sikkert dårlig metode, men klarte ikke å lage en metode som sjekker hele queuen
    //Returnerer indexen til verdien i queuen
    //Fant det vanskelig å lage en metode som sjekket en sirkulær data struktur
    public int contains(byte b, int pos){
        if(empty() || pos == -1)return -1;
        int i = pos;
        while(i != start && i != -1){
            if(buffer[i] == b) return i;
            i--;
            if(i < 0 && start != 0) i = buffer.length-1;
        }
        if(buffer[start] == b && last != start){
            last = start;
            return start;
        }
        return -1;
    }

    public byte pop(){
        if(!empty()){
            byte b = buffer[start];
            start = (start+1)%buffer.length;
            size--;
            return b;
        }
        return -128;
    }

    public int getSize(){
        return size;
    }

    public int getEnd(){
        return end;
    }
}
