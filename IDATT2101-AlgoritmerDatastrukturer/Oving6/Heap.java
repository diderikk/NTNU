public class Heap{
    int len;
    Node[] node;

    public Heap(Node[] node){
        this.node = node;
        len = node.length;
    }

    private int venstre(int i){return (i<<1)+1;}
    private int over(int i){return (i-1)>>1;}


    public void fiks_heap(int i){
        int m = venstre(i);
        if(m < len){
            int h = m+1;
            if(h<len && node[h].d.dist<node[m].d.dist)m = h;
            if(node[m].d.dist<node[i].d.dist){
                bytt(node,i,m);
                fiks_heap(m);
            }
        }
    }

    public void endrePrioritet(int indeks){
        //Endrer ikke prioritet, gjøres i forkort metoden
        int f;
        //Sjekker at indeks ikke er roten og at input node dist er mindre en dist til node over/foreldre noden.
        while(indeks>0 && node[indeks].d.dist<node[f=over(indeks)].d.dist){
            bytt(node,indeks,f);
            indeks = f;
        }
    }

    private void bytt(Node[] n,int i, int m){
        //Noder inneholder en currindeks, som er den midlertidige indeksen i heapen/tabellen.
        n[i].currindeks = m;
        n[m].currindeks = i;
        Node temp = n[i];
        n[i] = n[m];
        n[m] = temp;
        
    }

    public Node hent_min(){
        //Henter roten
        Node min = node[0];
        //Bytter den med siste blad node
        bytt(node,0,--len);
        //Fikser heap etter rot er byttet ut
        fiks_heap(0);
        return min;
    }

}