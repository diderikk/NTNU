public class Langetall {
    public static void main(String[] args) {
        String n1 = args[0];
        String operator = args[1];
        String n2 = args[2];
        DobbeltLenke resultat;

        if(operator.equals("+")){
            resultat = Adder.finnSum(n1, n2);
        }
        else{
            resultat = Subtraher.finnDifferanse(n1, n2);
        }
        //Fører til at plasseringen av første tall blir forskjell ved store og små tall
        //System.out.printf("%23s %n %s %20s %n",n1,operator,n2);
        Node node = resultat.hode;
        String str = "";
        while(node != null){
            str += node.element;
            node = node.neste;
        }
        System.out.printf("%2s %20s %n","=",str);
    }  
}
