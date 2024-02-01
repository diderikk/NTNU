#include <iostream>
#include <chrono>
#include <math.h>

using namespace std;

//Task 2.1-1
double pow1(double x,int n){
    if(n == 0){
        return 1;
    }
    else{
        return pow1(x,n-1) * x;
    }
    
}

//Task 2.2-3
double pow2(double x, int n){
    if(n == 0) {
        return 1;
    }
    //Even
    else if (n%2 == 0){
        return pow2(x*x,n/2);
    }
    //Odd
    else{
        return x*pow2(x*x,(n-1)/2);
    }
}


int main(void){
    //Function from task 2.1-1
    auto begin = chrono::high_resolution_clock::now();
    auto ans = pow1(2,10);
    auto end = chrono::high_resolution_clock::now();
    //Number of nanosecoonds
    auto ns = std::chrono::duration_cast<std::chrono::nanoseconds>(end-begin).count();
    cout << "Answer is equal to: " << ans << endl;
    cout << "Time used(ns): " << ns << " nanoseconds" << endl;

    //Function from task 2.2-3
    begin = chrono::high_resolution_clock::now();
    ans = pow2(2, 10);
    end = chrono::high_resolution_clock::now();
    ns = std::chrono::duration_cast<std::chrono::nanoseconds>(end-begin).count();
    cout << "Answer is equal to: " << ans << endl;
    cout << "Time used(ns): " << ns << " nanoseconds" << endl;

    //C++'s power function
    begin = chrono::high_resolution_clock::now();
    ans = pow(2,10);
    end = chrono::high_resolution_clock::now();
    ns = std::chrono::duration_cast<std::chrono::nanoseconds>(end-begin).count();
    cout << "Answer is equal to: " << ans << endl;
    cout << "Time used(ns): " << ns << " nanoseconds" << endl;

    cout << endl;
    return 0;
}