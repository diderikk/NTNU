import java.util.PriorityQueue;

public class ShortPath {
    Node[] noder;
    Heap heap;
    int N;
    PriorityQueue<Node> qs;

    public ShortPath(Node[] n){
        this.noder = n;
        heap = new Heap(noder);
        N = n.length;
    }
    
    private void initforgj(Node start, Node end){
        start.d.timeTaken = 0;
        end.thisIsTheEnd = true;
    }

    private void shorten(Node n, WEdge k){
        Forgj nd = n.d;
        Forgj md = k.to.d;
        if(md.timeTaken > nd.timeTaken+k.weight){
            md.timeTaken = nd.timeTaken+k.weight;
            md.forgj = n;
            qs.remove(k.to);
            qs.add(k.to);
            // heap.endrePrioritet(k.to.indeks);
        }
    }

    private void shortenType(Node n, WEdge k){
        Forgj nd = n.d;
        Forgj md = k.to.d;
        if(md.timeTaken > nd.timeTaken+k.weight){
            md.timeTaken = nd.timeTaken+k.weight;
            md.forgj = n;
            heap.endrePrioritet(k.to.indeks);
        }
    }

    private void shortenAS(Node n, WEdge k, Node destination){
        Forgj nd = n.d;
        Forgj md = k.to.d;
        if(md.timeTaken > nd.timeTaken+k.weight){
            md.timeTaken = nd.timeTaken+k.weight;
            md.forgj = n;
            //Only difference from dijkstra shorten method, is adding a distance to the node if not calculated before.
            if(k.to.distance == 0){
                k.to.distance(destination);
            }
            // heap.endrePrioritet(k.to.indeks);
            qs.remove(k.to);
            qs.add(k.to);
        }
    }

    public Node dijkstraNode(Node start, Node end){
        initforgj(start, end);
        // heap.endrePrioritet(start.indeks);
        qs = new PriorityQueue<>((a,b)->a.getCost() - b.getCost());
        qs.add(start);
        int count = 0;
        while(heap.len > 1){
            Node n = qs.poll();
            // Node n = heap.hent_min();
            count ++;
            if(n.thisIsTheEnd){
                System.out.printf("Nodes poped from heap: %d\n",count);
                return n;
            }
            for(WEdge k = n.kant1; k != null; k = k.next)shorten(n, k);
        }
        return null;
    }

    public Node[] dijkstraTypeNoder(Node start, int type){
        start.d.timeTaken = 0;
        heap.endrePrioritet(start.indeks);
        Node[] petrolStations = new Node[10];
        int count = 0;
        while(count < 10){
            Node n = heap.hent_min();
            if((n.type == type || n.type == 6)){
                petrolStations[count++] = n;
            }
            for(WEdge k = n.kant1; k != null; k = k.next)shortenType(n, k);
        }
        return petrolStations;
    }

    public Node AStar(Node start, Node end){
        initforgj(start, end);
        // heap.endrePrioritet(start.indeks);
        qs = new PriorityQueue<>((a,b)->a.getCost() - b.getCost());
        qs.add(start);
        int count = 0;
        while(heap.len > 1){
            // Node n = heap.hent_min();
            Node n = qs.poll();
            count++;
            if(n.thisIsTheEnd){
                System.out.printf("Nodes poped from heap: %d\n",count);
                return n;
            }
            for(WEdge k = n.kant1; k != null; k = k.next)shortenAS(n, k, end);
        }
        
        return null;
    }



     
}
