public class Søketre {
    public static void main(String[] args) {
        Tre tre = new Tre();
        for(String ord : args){
            tre.settInn(ord);
        }

        tre.printGrafikk(16);
    }
}
