package net.whgkswo.tesm.musics.soundEvents;

import net.whgkswo.tesm.musics.Region;
import net.whgkswo.tesm.musics.TimePeriod;

import java.util.Objects;

public class MusicKey {
    private Region region;
    private TimePeriod timePeriod;

    public MusicKey(Region region, TimePeriod timePeriod) {
        this.region = region;
        this.timePeriod = timePeriod;
    }

    public Region getRegion() {
        return region;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicKey musicKey = (MusicKey) o;
        return region == musicKey.region && timePeriod == musicKey.timePeriod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, timePeriod);
    }
}
