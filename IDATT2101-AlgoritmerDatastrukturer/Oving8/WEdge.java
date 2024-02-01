public class WEdge {
    WEdge next;
    Node to;
    int weight;

    public WEdge(Node n, WEdge neste, int vekt){
        this.to = n;
        this.next = neste;
        this.weight = vekt;
    }
}
