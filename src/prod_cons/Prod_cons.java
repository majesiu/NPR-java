/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prod_cons;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author inf117203
 */
class Buffer {
   private int[] contents = new int[10];
   private int available = 0;
   
   public void put(int val){
       while(available > 9){
       }
           contents[available] = val;
           available++;
   }
   
   public int get(){
       while(available < 1){
           
       }
       available--;
       int ret = contents[available];
       contents[available] = -1;
       return ret;
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
            buff.put(val);
            try {
                sleep(500);
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
        System.out.println("Konsument nr "+id+" Otrzymal wartosc "+buff.get());
        try {
                sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
    }
}


public class Prod_cons {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      Buffer b = new Buffer();
      Producer p1 = new Producer(b,1);
      Producer p2 = new Producer(b,2);
      Producer p3 = new Producer(b,3);
      Consumer c1 = new Consumer(b,1);
      Consumer c2 = new Consumer(b,2);
      Consumer c3 = new Consumer(b,3);
      p1.start(); 
      c1.start();
      p2.start(); 
      c2.start();
      p3.start(); 
      c3.start();
    }
    
}
