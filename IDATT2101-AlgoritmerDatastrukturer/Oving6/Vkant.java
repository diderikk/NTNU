public class Vkant {
    Vkant neste;
    Node til;
    int vekt;

    public Vkant(Node n, Vkant neste, int vekt){
        this.til = n;
        this.neste = neste;
        this.vekt = vekt;
    }
}
