package net.whgkswo.tesm.musics.v2.soundEvents;

import net.whgkswo.tesm.musics.v2.Region;
import net.whgkswo.tesm.musics.v2.TimePeriod;

import java.util.ArrayList;
import java.util.List;
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
