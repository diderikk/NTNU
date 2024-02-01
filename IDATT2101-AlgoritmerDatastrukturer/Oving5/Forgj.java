public class Forgj {
    //Bruker ikke distanse
    //Bruker funnet_tid/ferdig_tid, i motsetning til en visited array
    int funnet_tid, ferdig_tid;
    Node forgj;
    static int tid;
    static void nullTid(){tid = 0;}
    static int lesTid(){return ++tid;}
    public Forgj(){
    }

    public String toString(){
        return funnet_tid + " " + ferdig_tid;
    }
}
