public class Tre {
    Node rot;

    public Tre(){
        rot = null;
    }

    public Tre(byte b, int count){
        this.rot = new Node(b,count);
    }

    //Setter sammen to trær ved å lage et nytt tre
    //og barne nodene blir rotene til de treene vi ville sette sammen
    public Tre settSammen(Tre tre){
        int sum = tre.rot.count + rot.count;
        Tre nyTre = new Tre();
        nyTre.rot = new Node(sum);
        if(rot.count <= tre.rot.count){
            nyTre.rot.venstre = rot;
            nyTre.rot.høyre = tre.rot;
        }
        else{
            nyTre.rot.venstre = tre.rot;
            nyTre.rot.høyre = rot;
        }
        return nyTre;
    }
    

    
}
