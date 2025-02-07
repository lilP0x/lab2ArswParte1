package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread {
    private final int a, b;
    private final List<Integer> primes;
    private final Object monitor; 
    private boolean isPaused; 

    public PrimeFinderThread(int a, int b, Object monitor) {
        this.a = a;
        this.b = b;
        this.primes = new LinkedList<>();
        this.monitor = monitor;
        this.isPaused = false;
    }

    @Override
    public void run() {
        for (int i = a; i < b; i++) {
            synchronized (monitor) {
                while (isPaused) {  
                    try {
                        monitor.wait(); 
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (isPrime(i)) {
                primes.add(i);
                //System.out.println(i);
            }
        }
    }

    public void pauseThread() {
        isPaused = true; 
    }

    public void resumeThread() {
        synchronized (monitor) {
            isPaused = false; 
            monitor.notifyAll(); 
        }
    }

    public List<Integer> getPrimes() {
        return primes;
    }
    
    private boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
