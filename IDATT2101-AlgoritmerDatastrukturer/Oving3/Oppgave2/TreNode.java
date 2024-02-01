public class TreNode{
    String element;
    TreNode forelder;
    TreNode venstre;
    TreNode høyre;


    public TreNode(String element, TreNode forelder, TreNode venstre, TreNode høyre){
        this.element = element;
        this.forelder = forelder;
        this.venstre = venstre;
        this.høyre = høyre;
    }


}