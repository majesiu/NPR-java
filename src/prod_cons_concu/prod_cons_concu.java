/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package prod_cons_concu;

/**
 *
 * @author inf117203
 */

import static java.lang.Thread.sleep;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.locks.*;

class Buffer {
    private int[] contents = new int[10];
    private int available = 0;
    final Lock lock = new ReentrantLock();
    final Condition notFull  = lock.newCondition();
    final Condition notEmpty = lock.newCondition();
    
    public void put(int val) throws InterruptedException{
        lock.lock();
        while(available > 9){
            notFull.await();
        }
        contents[available] = val;
        System.out.println("Otrzymano ["+available+"]="+val);
        available++;
        notEmpty.signal();
    }
    
    public int get() throws InterruptedException{
        lock.lock();
        try{
            while(available < 1){
                notEmpty.await();
            }
            available--;
            System.out.println("Wyslano ["+available+"]="+contents[available]);
            int ret = contents[available];
            contents[available] = -1;
            notFull.signal();
            return ret;
        } finally {
            lock.unlock();
        }
        
    }
}

class Producer extends Thread {
    private Buffer buff;
    Random generator = new Random();
    int val;
    private int id;
    public Producer(Buffer b, int id) {
        buff = b;
        this.id = id;
    }
    public void run() {
        while(isAlive()){
            val = generator.nextInt(25);
            System.out.println("Prod nr "+id+" wysłano wartość do bufora " + val);
            try {
                buff.put(val);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

class Consumer extends Thread {
    private Buffer buff;
    private int id;
    public Consumer(Buffer b, int id) {
        buff = b;
        this.id = id;
    }
    public void run() {
        while(isAlive()){
            try {
                System.out.println("Konsument nr "+id+" Otrzymal wartosc "+buff.get());
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

public class prod_cons_concu {
    public static void main(String[] args) {
        Buffer b = new Buffer();
        Producer p1 = new Producer(b,1);
        Producer p2 = new Producer(b,2);
        Producer p3 = new Producer(b,3);
        Consumer c1 = new Consumer(b,1);
        Consumer c2 = new Consumer(b,2);
        Consumer c3 = new Consumer(b,3);
        Consumer c4 = new Consumer(b,4);
        p1.start();
        c1.start();
        p2.start();
        c2.start();
        p3.start();
        c3.start();
        c4.start();
    }
}