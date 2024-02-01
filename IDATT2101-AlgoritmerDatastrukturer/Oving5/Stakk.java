public class Stakk {
    private int[] tab;
    private int antall = 0; 

    public Stakk(int str){
        tab = new int[str];
    }

    public boolean tom(){
        return antall == 0;
    }

    public boolean full(){
        return antall - tab.length == 0;
    }

    public void push(int n){
        if(!full()) tab[antall++] = n;
    }

    public int pop(){
        if(!tom()) return tab[--antall];
        return -1;
    }
}
