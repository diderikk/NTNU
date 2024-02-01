public class Node {
    Vkant kant1;
    Forgj d;
    //Indeksen i orginal heap/tabell
    int indeks;
    //Indeksen i midlertidig tabell/heap under heapendring.
    int currindeks;

    //Printer ut nodene i format
    public void printNode(){
        if(d.dist == Forgj.uendelig){
            System.out.printf("%d %10s %10s \n",indeks,"","nåes ikke");
        }
        else if (d.dist == 0)System.out.printf("%d %10s %10s \n",indeks,"start",d.dist);
        else System.out.printf("%d %10d %10d \n",indeks,d.forgj.indeks,d.dist);
    }
}
