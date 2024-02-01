import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class WGraph {
    
    public WGraph(String path){
    
    }

    public static Node[] ny_vgraf_dijkstra(){
        int N, K;
        Node[] noder = null;
        try{
            BufferedReader br = new BufferedReader(new FileReader("noder.txt"));
            StringTokenizer st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            noder = new Node[N];
            for(int i = 0; i<N; i++){
                st = new StringTokenizer(br.readLine());
                st.nextToken();
                double lat = Double.parseDouble(st.nextToken())*Math.PI/180;
                double longi = Double.parseDouble(st.nextToken())*Math.PI/180;
                noder[i] = new Node(lat,longi);
                noder[i].coslat = Math.cos(lat);
                noder[i].indeks = i;
            }
            br.close();
            br = new BufferedReader(new FileReader("kanter.txt"));
            st = new StringTokenizer(br.readLine());
            K = Integer.parseInt(st.nextToken());
            for(int i = 0; i<K;i++){
                st = new StringTokenizer(br.readLine());
                int fra = Integer.parseInt(st.nextToken());
                int til = Integer.parseInt(st.nextToken());
                int vekt = Integer.parseInt(st.nextToken());
                WEdge vkant = new WEdge(noder[til], noder[fra].kant1, vekt);
                noder[fra].kant1 = vkant;
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return noder;
    }


    public static Node[] ny_vgraf_dijkstra_typer(){
        int N, K;
        Node[] noder = null;
        try{
            BufferedReader br = new BufferedReader(new FileReader("noder.txt"));
            StringTokenizer st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            noder = new Node[N];
            for(int i = 0; i<N; i++){
                st = new StringTokenizer(br.readLine());
                st.nextToken();
                double lat = Double.parseDouble(st.nextToken())*Math.PI/180;
                double longi = Double.parseDouble(st.nextToken())*Math.PI/180;
                Node n = new Node(lat,longi);
                n.indeks = i;
                noder[i] = n;
            }
            br.close();
            br = new BufferedReader(new FileReader("kanter.txt"));
            st = new StringTokenizer(br.readLine());
            K = Integer.parseInt(st.nextToken());
            for(int i = 0; i<K;i++){
                st = new StringTokenizer(br.readLine());
                int fra = Integer.parseInt(st.nextToken());
                int til = Integer.parseInt(st.nextToken());
                int vekt = Integer.parseInt(st.nextToken());
                WEdge vkant = new WEdge(noder[til], noder[fra].kant1, vekt);
                noder[fra].kant1 = vkant;
            }
            br.close();
            br = new BufferedReader(new FileReader("interessepkt.txt"));
            st = new StringTokenizer(br.readLine());
            int I = Integer.parseInt(st.nextToken());
            for(int i = 0; i<I; i++){
                st = new StringTokenizer(br.readLine());
                int index = Integer.parseInt(st.nextToken());
                int type = Integer.parseInt(st.nextToken());
                noder[index].type = type;
                while(st.hasMoreTokens())noder[index].name += st.nextToken() + " ";
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return noder;
    }
        
}
