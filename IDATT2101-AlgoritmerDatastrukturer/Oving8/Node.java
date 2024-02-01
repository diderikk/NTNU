public class Node {
    WEdge kant1;
    Forgj d;
    int indeks;
    double longitude;
    double latitude;
    boolean thisIsTheEnd = false;
    int type;
    int distance;
    double coslat;
    String name = "";
    

    public Node(double latitude, double longitude){
        this.longitude = longitude;
        this.latitude = latitude;
        this.thisIsTheEnd = false;
        this.type = 0;
        this.distance = 0;
        this.name =  "";
        d = new Forgj();
    }

    public int getCost(){
        return d.timeTaken+distance;
    }

    //Should add a cosLatitude, as it would decrease time usage. 
    public void distance (Node destination) {
        double sin_bredde = Math.sin((latitude-destination.latitude)/2.0);
        double sin_lengde = Math.sin((longitude-destination.longitude)/2.0);
        distance = (int) (35285538.46153846153846153846*Math.asin(Math.sqrt(
        sin_bredde*sin_bredde+coslat*destination.coslat*sin_lengde*sin_lengde)));
    }



    public String toString(){
        // return d.toString() + " " + longitude + " " + latitude;
        // return latitude + "," + longitude+" "+name +"\n";
        return latitude*180/Math.PI + "," + longitude*180/Math.PI+ " "+name+"\n";
    }
}
