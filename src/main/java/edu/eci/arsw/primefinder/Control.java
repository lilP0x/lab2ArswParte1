package edu.eci.arsw.primefinder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Control extends Thread {
    private static final int NTHREADS = 3;
    private static final int MAXVALUE = 300000000;
    private static final int TMILISECONDS = 500;

    private final Object monitor = new Object();
    private final PrimeFinderThread[] pft;
    private final BufferedReader reader;

    public Control() {
        this.pft = new PrimeFinderThread[NTHREADS];
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        int NDATA = MAXVALUE / NTHREADS;

        for (int i = 0; i < NTHREADS - 1; i++) {
            pft[i] = new PrimeFinderThread(i * NDATA, (i + 1) * NDATA, monitor);
        }
        pft[NTHREADS - 1] = new PrimeFinderThread((NTHREADS - 1) * NDATA, MAXVALUE + 1, monitor);
    }

    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        for (Thread t : pft) {
            t.start();
        }

        while (true) {
            try {
                Thread.sleep(TMILISECONDS);
                for (PrimeFinderThread thread : pft) {
                    thread.pauseThread();
                }
                int totalPrimos = 0;
                for (PrimeFinderThread thread : pft) {
                    totalPrimos += thread.getPrimes().size();
                }

                System.out.println("Total primos encontrados hasta ahora: " + totalPrimos);
                System.out.println("Presione Enter para continuar...");

               
                reader.readLine();

              
                for (PrimeFinderThread thread : pft) {
                    thread.resumeThread();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
