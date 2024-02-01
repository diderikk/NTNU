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
            //Cost is equal to the distance + expected time
            //Checks both left and right child nodes to find the smallest cost value
            //Traverses through the tree to find alle possible changes from parameter.
            if(h<len && node[h].getCost()<node[m].getCost())m = h;
            if(node[m].getCost()<node[i].getCost()){
                bytt(node,i,m);
                fiks_heap(m);
            }
        }
    }

    public void endrePrioritet(int indeks){
        int f;
        while(indeks>0 && node[indeks].getCost()<node[f=over(indeks)].getCost()){
            bytt(node,indeks,f);
            indeks = f;
        }
    }

    private void bytt(Node[] n,int i, int m){
        //Swaps indexes
        n[i].indeks = m;
        n[m].indeks = i;
        Node temp = n[i];
        n[i] = n[m];
        n[m] = temp;
        
    }

    public Node hent_min(){
        //Gets the root node
        Node min = node[0];
        //Swaps first and last references in the heap array
        bytt(node,0,--len);
        //Sorts from the root
        fiks_heap(0);
        return min;
    }

}