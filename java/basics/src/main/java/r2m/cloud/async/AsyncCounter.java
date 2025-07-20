package r2m.cloud.async;

public class AsyncCounter {

    private int counter = 0;

    public synchronized void incrementSyncMethod(){
        counter++;
    }

    public void incrementSyncBlock(){
        synchronized (this){
            counter++;
        }
    }

    public int getCounter(){
        return counter;
    }
}
