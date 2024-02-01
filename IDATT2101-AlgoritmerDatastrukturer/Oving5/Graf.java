import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Graf {
    int N, K;
    Node[] noder;
    //Filen ligger i mappen med kode filene
    String path = "./L7g2";

    public void nyGraf(){
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            StringTokenizer st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            noder = new Node[N];
            for(int i = 0; i<N; i++) {
                noder[i] = new Node();
                //Indeks
                noder[i].index = i;
            }
            K = Integer.parseInt(st.nextToken());
            for(int i = 0; i<K;i++){
                st = new StringTokenizer(br.readLine());
                int fra = Integer.parseInt(st.nextToken());
                int til = Integer.parseInt(st.nextToken());
                Kant k = new Kant(noder[til], noder[fra].kant1);
                noder[fra].kant1 = k;
            }
            br.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Lager en omvendt graf/transposert graf, ved å bytte om fra og til indekser fra filen.
    public void omvendtGraf(){
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            StringTokenizer st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            noder = new Node[N];
            for(int i = 0; i<N; i++) {
                noder[i] = new Node();
                //Indeks
                noder[i].index = i;
            }
            K = Integer.parseInt(st.nextToken());
            for(int i = 0; i<K;i++){
                st = new StringTokenizer(br.readLine());
                int til = Integer.parseInt(st.nextToken());
                int fra = Integer.parseInt(st.nextToken());
                Kant k = new Kant(noder[til], noder[fra].kant1);
                noder[fra].kant1 = k;
            }
            br.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Lager omvendt graf, gjennom å snu alle grafer ved hver node
    public void transposeGraf(){
        Node[] temp = new Node[N];
        for(int  i= 0; i<N;i++){
            temp[i] = new Node();
            //Indeks
            temp[i].index = i;
        }
        for(int i = 0; i<N; i++){
            Kant tempKant = noder[i].kant1;
            while(tempKant != null){
                int til = i;
                int fra = tempKant.til.index;
                Kant k = new Kant(temp[til],temp[fra].kant1);
                temp[fra].kant1 = k;
                tempKant = tempKant.neste;
            }
        }
        noder = temp;
    }
}
