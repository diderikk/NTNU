class DobbeltLenke{
    int antallElementer = 0;
    Node hode, hale;


    public void settInnFremst(int element){
        hode = new Node(element, hode, null);
        if(hale == null){
            hale = hode;
        }
        else hode.neste.forrige = hode;
        antallElementer++;
    }

    public void settInnBakerst(int element){
        Node ny = new Node(element,null,hale);
        if(hale != null) hale.neste = ny;
        else hode = ny;
        hale = ny;
        antallElementer++;
    }

    //Lager dobbelt lenket liste av en string med tall
    //Vil throw exception hvis en char ikke er et siffer
    public void lagLenkeAvString(String str){
        
        for(int i = 0; i<str.length();i++){
            int temp = Integer.parseInt(String.valueOf(str.charAt(i)));
            this.settInnBakerst(temp);
        }
    }

}