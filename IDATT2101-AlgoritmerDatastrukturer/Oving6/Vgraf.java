import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Vgraf {
    int N, K;
    Node[] noder;
    String path;
    Heap heap;

    public Vgraf(String path){
        this.path = path;
        ny_vgraf();
        //Lager heap av den nye grafen.
        heap = new Heap(noder);
    }

    public void ny_vgraf(){
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            StringTokenizer st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            noder = new Node[N];
            for(int i = 0; i<N; i++){
                noder[i] = new Node();
                noder[i].indeks = i;
                noder[i].currindeks = i;
            }
            K = Integer.parseInt(st.nextToken());
            for(int i = 0; i<K;i++){
                st = new StringTokenizer(br.readLine());
                int fra = Integer.parseInt(st.nextToken());
                int til = Integer.parseInt(st.nextToken());
                int vekt = Integer.parseInt(st.nextToken());
                Vkant vkant = new Vkant(noder[til], noder[fra].kant1, vekt);
                noder[fra].kant1 = vkant;
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void initforgj(Node s){
        for(int i = 0; i<N; i++){
            noder[i].d = new Forgj();
        }
        s.d.dist = 0;
    }

    private void forkort(Node n, Vkant k){
        Forgj nd = n.d;
        Forgj md = k.til.d;
        //Sjekker om noden kanten peker til kan få mindre distanse. Nodens distanse + vekten til kanten.
        if(md.dist > nd.dist+k.vekt){
            md.dist = nd.dist+k.vekt;
            md.forgj = n;
            //Flytter på den nylig oppdaterte noden.
            heap.endrePrioritet(k.til.currindeks);
        }
    }

    public Node[] dijkstra(Node s){
        //Initialiserer forgjengere
        initforgj(s);
        //Endrer prioritet på start noden, for å få den til toppen av heapen(roten)
        heap.endrePrioritet(s.indeks);
        //Lager en resultat node tabell
        Node[] result = new Node[N];
        for(int i = N; i>1;--i){
            //Henter rot noden av min heapen
            Node n = heap.hent_min();
            //Legger inn noden som ble poped, med den orginale indeksen
            result[n.indeks] = n;
            //Kjører gjennom hver kant for å se om den neste noden kan få endret distanse.
            for(Vkant k = n.kant1; k != null; k = k.neste)forkort(n, k);
        }
        //For lokken kjores gjennom 49 ganger, må legge til siste
        result[noder[0].indeks] = noder[0];
        return result;
    }
}
