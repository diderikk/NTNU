import time
import sys

sys.setrecursionlimit(6000)

def pow1(x, n):
    if n == 0:
        return 1
    else:
        return x*pow1(x,n-1)

def pow2(x,n):
    if n == 0:
        return 1
    elif n%2 == 0:
        return pow2(x*x,n/2)
    else: 
        return x*pow2(x*x,(n-1)/2)

start = time.time()
ans = pow2(1.001,5000)
end = time.time()
print(f'Time taken: {end-start}. Answer: {ans}')

