public class Tre {
    private TreNode rot;

    public Tre(){
        rot = null;
    }

    public void settInn(String ord){
        if(rot == null){
            rot = new TreNode(ord, null, null, null);
            return;
        }
        TreNode n = rot;
        TreNode forrige = null;
        String nøkkel = null;
        while(n != null){
            forrige = n;
            nøkkel = n.element;
            //Bruker compareTo for å sammenligne ordene
            if(ord.compareTo(nøkkel) < 0) n = n.venstre;
            //Alle ord som er lik eller større enn foreldrenoden vil bli sent mot høyre
            else n = n.høyre;
        }

        //Kommet til noden hvor ordet skal plasseres
        if(ord.compareTo(nøkkel) < 0) forrige.venstre = new TreNode(ord, forrige, null, null);
        else forrige.høyre = new TreNode(ord, forrige, null, null);
    }


    //Printer grafikken for treet
    public void printGrafikk(int antallOrd){
        //Bruker nivåordentaversering for å få hvert nivå på linje
        Kø kø = new Kø(antallOrd);
        kø.leggIKø(rot);
        //Som oppgitt som tips i oppgaven starter man med 64 plasser
        int plass = 64;
        //Teller hvor mange gjennomganger av while løkken
        int count = 1;
        //Bruker opp mot count for å se når vi kan et nivå dypere
        int forNyLinje = 2;
        //Sjekker om køen er tom eller om vi er kommet til nivå 5.
        //Kunne brukt treets høyde + 1 istedenfor. forNyLinje < Math.pow(2,høyde+1)
        while(!kø.tom() && forNyLinje < 32){
            TreNode denne = kø.nesteIKØ();
            if(denne != null){
                //Printer ut plassen/2 - ordet lengde / 2, for å gjøre ord plasseringen mer midtstilt.
                //Kan føre til at man plassen på hvert ord blir større
                if(denne.element.length()/2 < plass/2) System.out.print(" ".repeat((plass/2)-(denne.element.length()/2)));
                //Dersom ordets lengde er for stort
                else System.out.print(" ");
                System.out.print(denne.element);
                if(denne.element.length()/2 < plass/2) System.out.print(" ".repeat((plass/2)-(denne.element.length()/2)));
                else System.out.print(" ");

                kø.leggIKø(denne.venstre);
                kø.leggIKø(denne.høyre);
            }
            else{
                //Gjøres for å holde strukturen i treet, sett plass uansett om det ikke er barnenoder. 
                System.out.print(" ".repeat(plass));
                //Legger til null for å beholde strukturen i treet. Setter av plass selv om ikke null har barnenoder. 
                //Kunne ført til uendelig while løkke, derfor må man stoppe etter en bestemt høyde. 
                kø.leggIKø(null);
                kø.leggIKø(null);
            }
            count++;
            //Sjekker om man skal bytte linje
            if(forNyLinje/count == 1 && forNyLinje%count == 0){
                System.out.println();
                forNyLinje *= 2;
                plass /= 2;
            }
        }
    }
}
