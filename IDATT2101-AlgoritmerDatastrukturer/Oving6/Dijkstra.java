public class Dijkstra {
    public static void main(String[] args) {
        Vgraf vgraf = new Vgraf("./vg5");
        Node[] result = vgraf.dijkstra(vgraf.noder[1]);
        System.out.printf("%-5s %-13s %s%n","Node","Forgjenger","Distanse");
        for(Node node : result){
            node.printNode();
        };
    } 
}
