package net.whgkswo.tesm.general;

import java.util.function.IntConsumer;

public class GeneralUtil {
    public static void simpleRepeat(int repetition, Runnable method){
        for(int i = 0; i< repetition; i++){
            method.run();
        }
    }
    public static void repeatWithIndex(int repetition, IntConsumer method){
        for(int i = 0; i< repetition; i++){
            method.accept(i);
        }
    }
}
