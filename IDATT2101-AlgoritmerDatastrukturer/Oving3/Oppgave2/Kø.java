public class Kø {
    private TreNode[] arr;
    private int start = 0;
    private int slutt = 0;
    private int antall = 0;

    public Kø(int størrelse){
        arr = new TreNode[størrelse];
    }

    public boolean tom(){
        return antall == 0;
    }

    public boolean full(){
        return antall == arr.length;
    }

    public void leggIKø(TreNode node){
        if(full()) return;
        arr[slutt] = node;
        slutt = (slutt+1)%arr.length;
        antall++;
    }

    public TreNode nesteIKØ(){
        if(!tom()){
            TreNode node = arr[start];
            start = (start+1)%arr.length;
            antall--;
            return node;
        }
        else return null;
    }
}
