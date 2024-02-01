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
  
int splitt2(int* arr, int low, int high, int* lp) 
{ 
    //Bytter om pivot punktene, dersom den som skal være mindre, er det minste pivot punktet
    if (arr[low] > arr[high]) 
        bytt(&arr[low], &arr[high]); 

    int j = low+1; 
    int g = high-1, k = low+1, p = arr[low], q = arr[high]; 
    while (k <= g) { 
        //Finner verdier som er lavere enn det minste pivot punktet
        if (arr[k] < p) { 
            bytt(&arr[k], &arr[j]); 
            j++; 
        } 
        //Finner verdier større enn det største pivot punktet. Finner også korrekt plass å plassere den.
        else if (arr[k] >= q) { 
            while (arr[g] > q && k < g) 
                g--; 
            bytt(&arr[k], &arr[g]); 
            g--; 
            if (arr[k] < p) { 
                bytt(&arr[k], &arr[j]); 
                j++; 
            } 
        } 
        k++; 
    } 
    j--; 
    g++; 

    bytt(&arr[low], &arr[j]); 
    bytt(&arr[high], &arr[g]); 
  

    *lp = j;

  
    return g; 
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

void dualpivotquicksort(int* arr, int low, int high) 
{ 
    if (low < high) { 
        // lp means left pivot, and rp means right pivot. 
        int lp, rp; 
        rp = splitt2(arr, low, high, &lp); 
        dualpivotquicksort(arr, low, lp - 1); 
        dualpivotquicksort(arr, lp + 1, rp - 1); 
        dualpivotquicksort(arr, rp + 1, high); 
    } 
} 

//Innsettning sorterings metoden når lengden på en splitt er mindre eller lik 50
void dualpivotquicksort_innsettning(int* arr, int low, int high) 
{ 
    if (high - low > 50) { 
        // lp means left pivot, and rp means right pivot. 
        int lp, rp; 
        rp = splitt2(arr, low, high, &lp); 
        dualpivotquicksort_innsettning(arr, low, lp - 1); 
        dualpivotquicksort_innsettning(arr, lp + 1, rp - 1); 
        dualpivotquicksort_innsettning(arr, rp + 1, high); 
    }
    else{
        innsettingssort(arr,low,high);
    } 
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
    //Legger til tilfeldige verdier fra 1-n
    random_arr_duplicates(arr,n);
    auto copy1 = arr;
    struct timespec start, stopp;
    //Sortere listen før den tidtakes og sorteres.
    // dualpivotquicksort_innsettning(arr,0,n-1);

    //Timer starter
    clock_gettime(CLOCK_THREAD_CPUTIME_ID, &start);

    // dualpivotquicksort_innsettning(arr,0,n-1);
    dualpivotquicksort(arr,0,n-1);

    //Timer stopper
    clock_gettime(CLOCK_THREAD_CPUTIME_ID, &stopp);

    //Rekkefølge test og sumtest
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
        cout << "Did not pass test" << endl;
    }
    

    return 0;
}