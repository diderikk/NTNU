public class DFS {
    private Graf graf;
    private Stakk rekkefolge;

    public DFS(Graf graf) {
        this.graf = graf;
        this.rekkefolge = new Stakk(graf.N);
    }

    public void dfsInit() {
        for (int i = graf.N - 1; i >= 0; i--) {
            graf.noder[i].d = new Forgj();
        }
        Forgj.nullTid();
    }

    public void dfSok(Node n, boolean print, boolean addRekkefolge) {
        Forgj nd = n.d;
        //Når en node først er funnt
        nd.funnet_tid = Forgj.lesTid();
        //Kjører gjennom alle kanter til første node
        for (Kant k = n.kant1; k != null; k = k.neste) {
            Forgj md = k.til.d;
            if (md.funnet_tid == 0) {
                md.forgj = n;
                //Printer ut et nivå fra n, dersom den ikke er oppdaget fra før
                if (print)
                    System.out.print(k.til.index + " ");
                //Kjører rekursivt, gjør at vi får DBS.
                dfSok(k.til, print, addRekkefolge);
            }
        }
        nd.ferdig_tid = Forgj.lesTid();
        //Legges til bakkerst i rekkefølge samtidig som når den får sin slutt tid.
        //Mulig fordi den tiden bare øker. 
        if (addRekkefolge)
            rekkefolge.push(n.index);
    }

    //Første DFS gjennomkjøring
    public Stakk runDFS() {
        dfsInit();
        //Kjører gjennom hver node etter indeks
        for (Node node : graf.noder) {
            if (node.d.funnet_tid == 0)
            //Skal ikke printe, men skal legges til i rekkefølgen
                dfSok(node, false, true);
        }
        return rekkefolge;
    }

    //Gjennomkjøring av den omvendte grafen
    public int runDFSomvendt(Stakk rekkefolge) {
        //Omvendte grafen blir objektets graf.
        graf.transposeGraf();
        // graf.omvendtGraf();
        dfsInit();
        //Teller antall komponenter
        int count = 0;
        if (graf.N < 100) {
            System.out.println("Komponenter: ");
            for (int i = 0; i < graf.N; i++) {
                //Henter første indeks i rekkefolgen
                int num = rekkefolge.pop();
                //For hver nye node som ikke er funnet, får vi en ny komponent
                if (graf.noder[num].d.funnet_tid == 0) {
                    //Første indeks blir ikke printet i DF søket
                    System.out.print("Komponent " + (count+1) + ":  " + graf.noder[num].index + " ");
                    dfSok(graf.noder[num], true, false);
                    System.out.println();
                    count++;
                }
                
            }
        }
        else{
            for(int i = 0; i<graf.N; i++){
                int num = rekkefolge.pop();
                if(graf.noder[num].d.funnet_tid == 0){
                    dfSok(graf.noder[num], false, false);
                    count++;
                }
            }
        }
        return count;
    }
}
