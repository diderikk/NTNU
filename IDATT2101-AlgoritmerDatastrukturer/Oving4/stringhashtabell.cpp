#include <iostream>
#include <string>
#include <fstream>
#include <cmath>
using namespace std;
//Anbefalt verdi for A under multiplikasjons hashingen
const float A = (sqrt(5)-1)/2;

//Kollisjon skal behandles med lenkede lister
struct Node{
    string element;
    Node * neste;
};

Node * nyNode(string navn, Node * n){
    Node * ny;
    ny->element = navn;
    ny->neste = n;
    return ny;
}
//Bruker enkellenke fordi man ikke trenger linking bakover
struct EnkelLenke{
    Node * hode = NULL;
};
//Setter inn fremst for å få innsetning lik Theta(1), med mindre man hadde hatt variabel for hale
void settInnFremst(EnkelLenke & l, string navn){
    Node * ny = new Node();
    ny->element = navn;
    ny->neste = l.hode;
    l.hode = ny;
}

/*Legger sammen unicode verdien til hver bokstav i navnet multiplisert med 1,2,3,4... 
  for å få ulike verdier for navn med samme bokstaver, men ulik rekkefølge. Kunne brukt 7^i.
  Dividerer med størrelsen av hashtabellen.
*/
int hashFunksjonDiv(string str, int m){
    long long index = 0;
    
    for(int i = 0; i<str.length(); i++){
        if(str[i] == ' ' || str[i] == ',') continue;
        index += (int)str[i]*(i+1);
    }
    return index % m;
}

//Legger sammen på samme måte. 
int hashFunksjonMult(string str, int m){
    int index = 0;
    for(int i = 0; i<str.length(); i++){
        if(str[i] == ' ' || str[i] == ',') continue;
        index += (int)str[i]*(i+1);
    }
    return floor(m*(index*A - floor(index*A)));
}

int main(){
    string navn;
    //m = 93, gir best minst kollisjoner i forhold til lastfaktor
    //m = 86, gir best lastfaktor, dette er derimot ikke er primtall
    int m = 93;
    int antKollisjoner = 0;
    int antElementer = 0;
    int plasserTatt = 0;
    EnkelLenke hashTabell[m];
    //Opner filen med navn "navn.txt" som ligger i mappen.
    ifstream file ("navn.txt");
    if(file.is_open()){
        
        while( getline(file,navn)){
            //Restdivisjon ga minst kollisjoner i forhold til størrelse av tabell
            int hashVerdi = hashFunksjonDiv(navn,m);
            //Sjekker om hode ikke er initialisert.
            if(hashTabell[hashVerdi].hode == nullptr){
                //Setter inn i tabellen fremst.
                settInnFremst(hashTabell[hashVerdi],navn);
            }
            else{
                //Allerede et hode i lenken, blir derfor en kollisjon.
                //Siden den plassers fremst, blir det bare en kollisjon
                antKollisjoner++;
                cout << navn << " kolliderte med: " << hashTabell[hashVerdi].hode->element << endl;
                settInnFremst(hashTabell[hashVerdi],navn);
            }
        }
        cout << endl;
    }
    file.close();
    //Kjører gjennom hele hashtabellen
    for(int i = 0; i<m; i++){
        if(hashTabell[i].hode != nullptr){
            //Antall plasser tatt, n, er antallet plasser av m som er initialisert.
            plasserTatt++;
            //Antall elementer er antall navn i listen, skal være like 86.
            antElementer++;
            //For å legge sammen elementer i de lenkede listene.
            Node * node = hashTabell[i].hode->neste;
            while(node != nullptr){
                antElementer++;
                node = node->neste;
            }
        }
    }
    
    cout << "Antall elementer i hashtabell: " << antElementer << endl;
    cout << "Størrelse hashtabell: " << m << endl;
    cout << "Antall kollisjoner: " << antKollisjoner << endl;
    cout << "Kollisjoner per person: " << (float)antKollisjoner/antElementer << endl;
    //(plasserTatt/m) er lastfaktor, selv om (antall elementer / m) egentlig er lastfaktor
    cout << "Lastfaktor: " << (float)plasserTatt/m << endl;


    while(1){
        string navn;
        cout << "Skriv inn navn som du vil slå opp(\"exit\" avslutter): " << endl;
        getline(cin,navn);
        if(navn == "exit") break;
        //For å finne index, må bruke samme hashFunksjon som når de ble plassert inn i hashtabellen
        int index = hashFunksjonDiv(navn,m);
        Node * node = hashTabell[index].hode;
        while(node != nullptr && node->element != navn){
            cout << "KOLLISJON: " << node->element << endl;
            node = node->neste;
        }
        if(node == nullptr) cout << "Navn er ikke registrert" << endl;
        else cout << node->element << " eksisterer i hashtabellen ved index: " << index << endl;
    }
    return 0;
}