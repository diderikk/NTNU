public class Subtraher{
    



    public static DobbeltLenke finnDifferanse(String a1, String b1){
        DobbeltLenke a = new DobbeltLenke();
        DobbeltLenke b = new DobbeltLenke();
        a.lagLenkeAvString(a1);
        b.lagLenkeAvString(b1);
        Node test1 = a.hode;
        Node test2 = b.hode;

        //For å finne hvilket av tallene som er minst
        //Hvis første tallet er størst, vanlig subtrahering
        //Hvis det andre tallet er størst, tar vi andre tallet - første tallet og endrer fortegn på svaret.
        if(a.antallElementer > b.antallElementer) return diff(a,b);
        else if(a.antallElementer < b.antallElementer){ 
            DobbeltLenke temp = diff(b,a);
            temp.hode.element = -temp.hode.element;
            return temp;
        }
        else{
            while(test1 != null && test2 != null){
                if(test1.element == test2.element){
                    test1 = test1.neste;
                    test2 = test2.neste;
                }
                else if (test1.element > test2.element){
                    return diff(a,b);
                }
                else{
                    DobbeltLenke temp = diff(b,a);
                    temp.hode.element = -temp.hode.element;
                    return temp;
                }
            }
        }
        //Hvis tallene er like, blir svaret null, vanlig subtrahering
        return diff(a,b);
    }

    private static DobbeltLenke  diff(DobbeltLenke a, DobbeltLenke b){
        DobbeltLenke resultat = new DobbeltLenke();

        //Starter bakerst
        Node node1 = a.hale;
        Node node2 = b.hale;
        while (node1 != null && node2 != null) {
            //Finner differanse mellom tallene
            int tempDiff = node1.element - node2.element;
            if(tempDiff < 0){
                //Finner første tall som kan trekkes fra til å gi tallene foran
                if (node1.forrige.element == 0){
                    Node temp = node1.forrige;
                    while(temp != null && temp.element == 0){
                        temp = temp.forrige;
                    }
                    while(temp != null && temp != node1){
                        temp.element -= 1;
                        temp.neste.element += 10;
                        temp = temp.neste;
                    }
                    resultat.settInnFremst(tempDiff+10);
                }
                else{
                    node1.forrige.element -=1;
                    resultat.settInnFremst(tempDiff+10);
                }
            }
            else resultat.settInnFremst(tempDiff);

            node1 = node1.forrige;
            node2 = node2.forrige;
        }

        //Hvis liste a er lengre enn liste b
        while (node1 != null) {
            resultat.settInnFremst(node1.element);
            node1 = node1.forrige;
        }
        //Liste b kan ikke være lengre enn a

        fjernNull(resultat);

        return resultat;
    }

    //Fjerner alle null foran første siffer != 0
    private static void fjernNull(DobbeltLenke resultat){
        Node node = resultat.hode;
        while(node.element == 0 && node != resultat.hale){
            if(node.neste != null) node.neste.forrige = null;
            Node fjernes = node;
            node = node.neste;
            resultat.antallElementer--;
            fjernes.neste = null;
        }
        resultat.hode = node;
    }
}