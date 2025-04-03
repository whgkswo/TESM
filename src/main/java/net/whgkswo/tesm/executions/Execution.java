package net.whgkswo.tesm.executions;

public class Execution {
    private int countdown;
    private Runnable runnable;

    public Execution(int countdown, Runnable runnable){
        this.countdown = countdown;
        this.runnable = runnable;
    }

    public int getCountdown(){
        return countdown;
    }

    public Runnable getRunnable(){
        return runnable;
    }

    public void count(){
        countdown--;
    }
}
