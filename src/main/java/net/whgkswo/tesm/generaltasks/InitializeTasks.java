package net.whgkswo.tesm.generaltasks;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.whgkswo.tesm.musics.TESMusicsMain;

public class InitializeTasks {

    /*이곳에서 수행되지 못한 작업들은 OnServerTick에서 runTimeCounter=0일 때 이루어지고 있음.
    변수들은 게임을 종료하면 초기화되지만, 메인화면으로 나갔다 들어오는 경우에는 초기화되지 않기 때문에 이곳에서 직접 초기화해야 함*/
    public static void initializeTasks() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            TESMusicsMain.musicTimer = 0;
            OnServerTicks.runTimeCounter = 0;
            TESMusicsMain.trackLength = 100;
            TESMusicsMain.musicPlaying = false;
            TESMusicsMain.currentMusicString = "음악 준비 중";
        });
    }
}
