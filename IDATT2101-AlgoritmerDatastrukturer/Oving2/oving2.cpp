#include <iostream>
#include "time.h"

using namespace std;
//Tar inn to pointere, og bytter verdiene.
void bytt(int *a, int *b)
{
    int temp = *a;
    *a = *b;
    *b = temp;
}

//Sjekker at v og h ligger på riktig plass i forhold til midt-verdien.
int median3sort(int arr[], int v, int h)
{
    int mid = (v + h) / 2;
    if (arr[v] > arr[mid])
        bytt(&arr[v], &arr[mid]);
    if (arr[mid] > arr[h])
    {
        bytt(&arr[h], &arr[mid]);
        if (arr[v] > arr[mid])
            bytt(&arr[v], &arr[mid]);
    }
    return mid;
}

int splitt1(int arr[], int v, int h)
{
    int iv, ih;
    int m = median3sort(arr, v, h);
    int dv = arr[m];
    //Delingsverdi flyttes til en lavere index enn h, siden arr[h] > arr[m]. Gjort i median3sort()
    bytt(&arr[m],&arr[h-1]);

    for (iv = v, ih = h - 1;;)
    {
        //Finner første index med verdi høyere eller lik delingsverdi fra venstre
        while (arr[++iv] < dv)
            ;
        //Finner første index med verdi lavere eller lik delingsverdi fra høyre
        while (arr[--ih] > dv)
            ;
        //Hvis iv >= ih, må alle verdier <= dv være til venste for alle verdier >= dv.
        if (iv >= ih)
            break;
        bytt(&arr[ih],&arr[iv]);
    }
    //Bytter dv til index hvor det første tallet som er større eller lik dv i rekken fra venstre.
    bytt(&arr[iv],&arr[h-1]);
    return iv;
}

void innsettingssort(int arr[], int v, int h){
    for (int j = v+1; j<=h; ++j){
        int bytt = arr[j];
        int i = j-1;

        while(i>=v && arr[i] > bytt){
            arr[i+1] = arr[i];
            i--;
        }
        arr[i+1] = bytt;
    }
}

void quicksort(int arr[], int v, int h)
{
    if (h - v > 2)
    {
        int delepos = splitt1(arr, v, h);
        quicksort(arr, v, delepos - 1);
        quicksort(arr, delepos + 1, h);
    }
    else
        median3sort(arr, v, h);
}

//Bruker innsettningssortering når en liste har blitt splittet til en lengde mindre eller lik 40.
void quicksort_innsettning(int arr[], int v, int h)
{
    if (h - v > 40)
    {
        int delepos = splitt1(arr, v, h);
        quicksort_innsettning(arr, v, delepos - 1);
        quicksort_innsettning(arr, delepos + 1, h);
    }
    else
        innsettingssort(arr,v,h);
}

//Legger til tilfeldige verdier i tabellen fra 1 til n
void random_arr(int arr[], int n)
{
    srand((unsigned) time(0));
    for (int i = 0; i < n; ++i)
    {   
        arr[i] = rand() % n + 1;
    }
}

void random_arr_duplicates(int arr[], int n){
    srand((unsigned) time(0));
    for (int i = 0; i < n; ++i)
    {
        //Mange av de samme, når det bare blir tall mellom 1-100, i en array med størrelse 10-100 millioner
        arr[i] = rand() % 100 + 1;
    }
}

//Tester om et arr[i+1] alltid er større eller lik arr[i]
bool rekkefolgetest(int arr[], int n){
    for(int i = 0; i<n-1 ;i++){
        if(arr[i] > arr[i+1]) return false;
    }
    return true;
}
//Tester om begge tabellene har lik sum
bool sumtest(int arr1[], int n, int arr2[]){
    int sum1 = 0, sum2 = 0;
    for(int i = 0; i<n;i++){
        sum1 += arr1[i];
    }

    for(int i = 0; i<n;i++){
        sum2 += arr2[i];
    }
    cout << sum1 << " " << sum2 << endl;
    return sum1 == sum2;
}

int main(void){
    int n = 100000000;
    int * arr = new int[n];
    random_arr_duplicates(arr,n);
    auto copy1 = arr;
    struct timespec start, stopp;
    //Sortere listen før den tidtakes og sorteres.
    // quicksort_innsettning(arr,0,n-1);
    

    clock_gettime(CLOCK_THREAD_CPUTIME_ID, &start);

    // quicksort(arr,0,n-1);
    quicksort_innsettning(arr,0,n-1);

    clock_gettime(CLOCK_THREAD_CPUTIME_ID, &stopp);

    if(rekkefolgetest(arr,n) && sumtest(copy1,n, arr)){
        int sec = stopp.tv_sec - start.tv_sec;

        long int nsec = stopp.tv_nsec - start.tv_nsec;
        if (nsec < 0) {
            sec -= 1;
            nsec += 1000000000;
        }
        if (sec) printf("%i.%03li sekunder\n", sec, nsec/1000000); //sekunder, og 3 desimaler

        else if (nsec > 50000000) printf("%li ms\n", nsec/1000000); //ms, hvis > 50ms

        else if (nsec > 50000) printf("%li µs\n", nsec/1000); // µs, hvis > 50µs

        else printf("%li ns\n", nsec); //ns
    }
    else{
        cout << "False" << endl;
    }
    

    return 0;
}