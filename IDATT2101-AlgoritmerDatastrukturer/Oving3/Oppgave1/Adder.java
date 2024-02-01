public class Adder {
    
    public static DobbeltLenke finnSum(String a1, String b1) {
        DobbeltLenke a = new DobbeltLenke();
        DobbeltLenke b = new DobbeltLenke();

        a.lagLenkeAvString(a1);
        b.lagLenkeAvString(b1);

        DobbeltLenke resultat = new DobbeltLenke();

        Node node1 = a.hale;
        Node node2 = b.hale;
        while (node1 != null && node2 != null) {
            int tempSum = node1.element + node2.element;
            if (tempSum > 9) {
                //Setter inn summen - 10 og legger til 1 på neste tall i lenken.
                resultat.settInnFremst(tempSum - 10);
                if (node1.forrige != null)
                    node1.forrige.element += 1;
                else if (node2.forrige != null)
                    node2.forrige.element += 1;
                else
                    resultat.settInnFremst(1);
            } else
                resultat.settInnFremst(tempSum);

            node1 = node1.forrige;
            node2 = node2.forrige;
        }

        //Hvis første tallet(fra command line) er lengre enn det andre tallet
        while (node1 != null) {
            // Kan bli lagt til 1 fra forrige sum
            if (node1.element > 9) {
                resultat.settInnFremst(node1.element - 10);
                if (node1.forrige == null) resultat.settInnFremst(1);
                else node1.forrige.element += 1;
            } else
                resultat.settInnFremst(node1.element);
            node1 = node1.forrige;
        }
        //Hvis andre tallet(fra command line) er lengre enn det første tallet
        while (node2 != null) {
            if (node2.element > 9) {
                resultat.settInnFremst(node2.element - 10);
                if(node1.forrige == null) resultat.settInnFremst(1);
                else node1.forrige.element += 1;
            } else
                resultat.settInnFremst(node2.element);
            node2 = node2.forrige;
        }

        return resultat;
    }

}
