#include <iostream>
#include "time.h"
#include <random>
#include <unordered_map>
using namespace std;

//Første hashfunksjon, restdivisjons regning med bits for å øke hastighet
//Fungerer bare med toer-potenser, m er en toer-potens.
inline int h1(int nokkel, int m)
{
    return (nokkel & (m - 1));
}

//Andre hashfunksjon, brukes som hopp for neste posisjon
inline int h2(int nokkel, int m)
{
    //Ikke restdivisjon, men gir tall mellom 1 og m;
    //Gir langt flere kollisjoner
    return (nokkel & (m - 2)) + 1;
}

void setup(int randTabell[], int tabell2[], int m)
{
    //Setter opp tabellen med tilfeldige tall
    srand((unsigned)time(0));
    for (int i = 0; i < 8000000; i++)
    {
        //Tall mellom 18000000 og 1
        randTabell[i] = rand() % (100000000 - 1) + 1;
    }
    //Setter alle verdier i hashtabellen lik 0. Kan ikke få 0 som et tilfeldig tall
    for (int i = 0; i < m; i++)
    {
        tabell2[i] = 0;
    }
}

int main()
{
    //Antall tilfeldige tall
    unsigned int n = 8000000;
    //2^23, tall i hashtabellen
    unsigned int m = 8388608;
    int *hashTabell = new int[m];
    int *randTabell = new int[n];
    setup(randTabell, hashTabell, m);
    unsigned int antKollisjoner = 0;

    struct timespec start, stopp;

    clock_gettime(CLOCK_THREAD_CPUTIME_ID, &start);
    for (int i = 0; i < n; i++)
    {
        unsigned int val = randTabell[i];
        //Første posisjon
        unsigned int pos = h1(val, m);
        //Sjekker om første posisjon er 0 eller ikke
        if (hashTabell[pos] != 0)
        {
            int h2Val = h2(val, m);
            while (hashTabell[pos] != 0)
            {
                //probe()
                pos = (pos + h2Val) & (m - 1);
                antKollisjoner++;
            }
        }
        hashTabell[pos] = randTabell[i];
    }
    clock_gettime(CLOCK_THREAD_CPUTIME_ID, &stopp);

    cout << "Antall kollisjoner: " << antKollisjoner << endl;
    cout << "Kollisjoner per tall: " << (float)antKollisjoner / n << endl;
    cout << "Lastfaktor: " << (float)n / m << endl;
    ;

    int sec = stopp.tv_sec - start.tv_sec;
    long int nsec = stopp.tv_nsec - start.tv_nsec;
    if (nsec < 0)
    {
        sec -= 1;
        nsec += 1000000000;
    }
    if (sec)
        printf("%i.%03li sekunder\n", sec, nsec / 1000000); //sekunder, og 3 desimaler
    else if (nsec > 50000000)
        printf("%li ms\n", nsec / 1000000); //ms, hvis > 50ms
    else if (nsec > 50000)
        printf("%li µs\n", nsec / 1000); // µs, hvis > 50µs
    else
        printf("%li ns\n", nsec); //ns


    //C++ sin hashtabell
    unordered_map<int, int> hashTabell2;

    clock_gettime(CLOCK_THREAD_CPUTIME_ID, &start);
    hashTabell2.reserve(n);
    for (int i = 0; i < n; i++)
    {
        //Index og verdi blir de samme tilfeldige tallene som min hashtabell bruker.
        hashTabell2.insert({randTabell[i], randTabell[i]});
    }
    clock_gettime(CLOCK_THREAD_CPUTIME_ID, &stopp);

    cout << endl
         << "Innebygd hashtabell: ";

    sec = stopp.tv_sec - start.tv_sec;
    nsec = stopp.tv_nsec - start.tv_nsec;
    if (nsec < 0)
    {
        sec -= 1;
        nsec += 1000000000;
    }
    if (sec)
        printf("%i.%03li sekunder\n", sec, nsec / 1000000); //sekunder, og 3 desimaler
    else if (nsec > 50000000)
        printf("%li ms\n", nsec / 1000000); //ms, hvis > 50ms
    else if (nsec > 50000)
        printf("%li µs\n", nsec / 1000); // µs, hvis > 50µs
    else
        printf("%li ns\n", nsec); //ns

    return 0;
}