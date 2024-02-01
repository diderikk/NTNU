class Node{
    int element;
    Node neste, forrige;

    public Node(int element, Node neste, Node forrige){
        this.element = element;
        this.neste = neste;
        this.forrige = forrige;
    }

    public int finnElement(){
        return element;
    }

    public Node finnNeste(){
        return neste;
    }

    public Node finnForrige(){
        return forrige;
    }
}