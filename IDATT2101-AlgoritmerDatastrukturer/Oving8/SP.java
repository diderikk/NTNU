import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SP {
    public static void main(String[] args) throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter("Coordinates.csv"));
        Node[] noder = WGraph.ny_vgraf_dijkstra();
        ShortPath sw = new ShortPath(noder);

        long start = System.nanoTime();

        //Trondheim - Helsinki:
        // Node node = sw.AStar(noder[2399829], noder[1221382]);
        // Node node = sw.dijkstraNode(noder[2399829], noder[1221382]);


        //Kårvåg - Gjemnes:
        // Node node = sw.dijkstraNode(noder[6013683], noder[6225195]);
        Node node = sw.AStar(noder[6013683], noder[6225195]);

        long end = System.nanoTime();

        System.out.printf("Hours expected driving: %f\n",(float)node.d.timeTaken/360000);
        System.out.printf("Minutes expected driving: %f\n",(float)node.d.timeTaken/6000);
        System.out.printf("Seconds expected driving: %f\n",(float)node.d.timeTaken/100);

        //Does'nt count the last
        int count = 1;
        //Will write the route backwards, in web map it does not make a difference.
        //Could use an array, and reverse it
        while(node.d.forgj != null){
            count ++;
            bf.write(node.toString());
            node = node.d.forgj;
        }
        bf.close();
        System.out.printf("Nodes to destination: %d\n",count);
        long timeElapsed = (end-start)/1000000;
        System.out.printf("Time elapsed: %d milliseconds\n",timeElapsed);
    }
}
