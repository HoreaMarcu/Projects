import GraphicalUserInterface.View;

import javax.swing.*;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static javax.swing.JFrame.*;

public class Main implements Runnable {
    static CountDownLatch latch = new CountDownLatch(100);
    int sum = 0;
    static int steps2=0;
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        int[] a = new int [102];
        int aux;
        int steps = 0;
        Random rand = new Random();
        for(int i = 0; i < 100; i++){
            a[i] = rand.nextInt(100);
            steps ++;
        }
        for(int i = 0; i < 100; i++){
            for(int j = i + 1; j < 100; j++){
                if(a[i] > a[j]){
                    aux = a[i];
                    a[i] = a[j];
                    a[j] = aux;
                    steps += 3;
                }
            }
        }
        long end = System.currentTimeMillis();
        long total = end - start;
        float finalTime = (float)total / 1000;
        System.out.println("Static array sorted in " + steps + " steps and " + finalTime + " seconds");

        steps = 0;
        start = System.currentTimeMillis();
        Main m = new Main();
        steps++;
        steps2++;
        for(int i = 0; i < 100; i++){
            Thread t = new Thread(m, Integer.toString(i));
            t.start();
            steps+=2;
            steps2+=2;
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Number of steps for creating 100 threads: " + steps);
        end = System.currentTimeMillis();
        total = end - start;
        finalTime = (float)total/1000;
        System.out.println("Number of steps for adding 100 numbers using 100 threads: " + steps2);
        System.out.println("This process of thread migration was executed in: " + finalTime + " seconds.");

        JFrame frame = new View("Measurements");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.pack();
        frame.setVisible(true);

    }

    @Override
    public synchronized void run() {
        System.out.println("Thread number " + currentThread().threadId());

        sum += currentThread().threadId();
        steps2++;
        try {
            sleep(1);
            latch.countDown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
