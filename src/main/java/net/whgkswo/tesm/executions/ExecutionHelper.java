package net.whgkswo.tesm.executions;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;

public class ExecutionHelper {
    private static final Set<ScheduledTask<?>> ON_END_SERVER_TICK = new HashSet<>();

    public static void registerExecutioner(){
        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            // ConcurrentModificationException 방지 차 이터레이터 사용
            Iterator<ScheduledTask<?>> iterator = ON_END_SERVER_TICK.iterator();
            while (iterator.hasNext()){
                ScheduledTask<?> scheduledTask = iterator.next();
                scheduledTask.count();
                if(scheduledTask.getCountdown() <= 0){
                    scheduledTask.execute();
                    iterator.remove();
                }
            }
        });
    }

    public static void addToOnServerTick(int countdown, String purpose, Callable<?> callable){
        ON_END_SERVER_TICK.add(new ScheduledTask<>(countdown, purpose, callable));
    }
    public static void addToOnServerTick(ScheduledTask<?> scheduledTask){
        ON_END_SERVER_TICK.add(scheduledTask);
    }
}
