import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DijkstraType {
    public static void main(String[] args) throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter("Coordinates.csv"));
        Node[] noder = WGraph.ny_vgraf_dijkstra_typer();
        ShortPath sw = new ShortPath(noder);

        //Petrolstations around Trondheim lufthavn:
        Node[] node = sw.dijkstraTypeNoder(noder[6198111],2);

        //Charging stations around Røros Hotell:
        // Node[] node = sw.dijkstraTypeNoder(noder[1117256],4);


        for(Node n : node){
            bf.write(n.toString());
        }
        bf.close();   
    }
}
