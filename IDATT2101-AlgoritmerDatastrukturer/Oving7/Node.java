public class Node{
    //Default verdi, passer ikke i en byte
    int d = -200;
    int count;
    Node venstre;
    Node høyre;


    public Node(byte b, int count){
        this.d = (int)b;
        this.count = count;
    }

    public Node(int count){
        this.count = count;
    }

}