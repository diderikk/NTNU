public class Ssk {
    public static void main(String[] args) {
        Graf graf = new Graf();
        graf.nyGraf();
        DFS dfs = new DFS(graf);
        Stakk rekkefolge = dfs.runDFS();
        int count = dfs.runDFSomvendt(rekkefolge);
        System.out.printf("\nAntall komponenter: %d\n",count);
    }
}
