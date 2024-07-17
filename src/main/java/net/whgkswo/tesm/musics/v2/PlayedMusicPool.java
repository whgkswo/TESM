package net.whgkswo.tesm.musics.v2;

import java.util.HashSet;
import java.util.Set;

public class PlayedMusicPool {
    private Set<Integer> playedMusicSet = new HashSet<>();
    private int lastIndexOfLatestCycle;

    public PlayedMusicPool() {
        lastIndexOfLatestCycle = -1;
    }

    public Set<Integer> getPlayedMusicSet() {
        return playedMusicSet;
    }

    public int getLastIndexOfLatestCycle() {
        return lastIndexOfLatestCycle;
    }

    public void setLastIndexOfLatestCycle(int lastIndexOfLatestCycle) {
        this.lastIndexOfLatestCycle = lastIndexOfLatestCycle;
    }
}
