package net.whgkswo.tesm.sounds.musics.soundEvents;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.sounds.musics.Region;
import net.whgkswo.tesm.calendar.TimePeriod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public enum MusicEvent {
    ASSALTO_04_FINAL(Region.CYRODIIL, TimePeriod.MORNING,"assalto_04_final",3500),
    KING_AND_COUNTRY(Region.CYRODIIL, TimePeriod.MORNING,"king_and_country",4920),
    THESE_VERDANT_FIELDS(Region.CYRODIIL, TimePeriod.MORNING,"these_verdant_fields",6600),
    THROUGH_THE_VALLEY(Region.CYRODIIL, TimePeriod.MORNING,"through_the_valley",5220),
    BLOODLINES(Region.CYRODIIL, TimePeriod.DAY,"bloodlines",6700),
    FATED_LANDS(Region.CYRODIIL, TimePeriod.DAY,"fated_lands",4540),
    GLORY_OF_CYRODIIL(Region.CYRODIIL, TimePeriod.DAY,"glory_of_cyrodiil",3000),
    HEARTLANDS_SIGH(Region.CYRODIIL, TimePeriod.DAY,"heartlands_sigh",6160),
    KYNARETHS_WISH(Region.CYRODIIL, TimePeriod.DAY,"kynareths_wish",6240),
    LOST_GLORY(Region.CYRODIIL, TimePeriod.DAY,"lost_glory",5800),
    MAKER_OF_WORLDS(Region.CYRODIIL, TimePeriod.DAY,"maker_of_worlds",7100),
    ODE_TO_A_DYNASTY(Region.CYRODIIL, TimePeriod.DAY,"ode_to_a_dynasty",5720),
    OF_STORMS_AND_HALF_LIGHT(Region.CYRODIIL, TimePeriod.DAY,"of_storms_and_half_light",6780),
    THE_SUNLIGHT_WANDERER(Region.CYRODIIL, TimePeriod.DAY,"the_sunlight_wanderer",3080),
    WIDOWS_LAMENT(Region.CYRODIIL, TimePeriod.DAY,"widows_lament",6240),
    BALLAD_OF_DUSK(Region.CYRODIIL, TimePeriod.EVENING,"ballad_of_dusk",8320),
    BRUMAS_FROZEN_SORROW(Region.CYRODIIL, TimePeriod.EVENING,"brumas_frozen_sorrow",2880),
    E3_TEASER_MUSIC(Region.CYRODIIL, TimePeriod.EVENING,"e3_teaser_music",2160),
    THE_NIBENESE_TRAILS(Region.CYRODIIL, TimePeriod.EVENING,"the_nibenese_trails",3160),
    REMEMBERING_THE_LAST_GREAT_DYNASTY(Region.CYRODIIL, TimePeriod.EVENING,"remembering_the_last_great_dynasty",3020),
    REVERENCE(Region.CYRODIIL, TimePeriod.EVENING,"reverence",2280),
    RISING_HOPE_DURING_SUNDOWN(Region.CYRODIIL, TimePeriod.EVENING,"rising_hope_during_sundown",2620),
    TIMES_OF_CHANGE_FOR_THE_RESTLESS(Region.CYRODIIL, TimePeriod.EVENING,"times_of_change_for_the_restless",2860),
    VIGILANCE(Region.CYRODIIL, TimePeriod.EVENING,"vigilance",2180),
    ARCANA_MAGICAE(Region.CYRODIIL, TimePeriod.NIGHT,"arcana_magicae",4680),
    AURIELS_ASCENSION(Region.CYRODIIL, TimePeriod.NIGHT,"auriels_ascension",3740),
    LORKHANS_FOOTSTEPS(Region.CYRODIIL, TimePeriod.NIGHT,"lorkhans_footsteps",3620),
    MOONS_AND_SCARS(Region.CYRODIIL, TimePeriod.NIGHT,"moon_and_scars",5840),
    NOX_ATRA(Region.CYRODIIL, TimePeriod.NIGHT,"nox_atra",5080),
    RESTLESS_SECRETS(Region.CYRODIIL, TimePeriod.NIGHT,"restless_secrets",2780),
    SHAPELESS(Region.CYRODIIL, TimePeriod.NIGHT,"shapeless",2900),
    THE_SLEEPING_SKY(Region.CYRODIIL, TimePeriod.NIGHT,"the_sleeping_sky",4960),
    SO_BLIND_AND_IN_TERROR(Region.CYRODIIL, TimePeriod.NIGHT,"so_blind_and_in_terror",4480),
    WILDER_HEARTS(Region.CYRODIIL, TimePeriod.NIGHT,"wilder_hearts",3860)
    ;
    private Region region;
    private TimePeriod timePeriod;
    private SoundEvent soundEvent;
    private int length;

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }

    public Region getRegion() {
        return region;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public int getLength() {
        return length;
    }
    MusicEvent(Region region, TimePeriod timePeriod, String id, int length){
        this.region = region;
        this.timePeriod = timePeriod;
        this.soundEvent = SoundEvent.of(Identifier.of(TESMMod.MODID, id));
        this.length = length;
    }
    public static SoundEvent getMusic(TimePeriod timePeriod, int number){
        return Arrays.stream(MusicEvent.values())
                .filter(musicEvent -> musicEvent.getTimePeriod() == timePeriod)
                .collect(Collectors.toList())
                .get(number)
                .getSoundEvent();
    }
    public static List<SoundEvent> getMusics(TimePeriod timePeriod){
        return Arrays.stream(MusicEvent.values())
                .filter(musicEvent -> musicEvent.getTimePeriod() == timePeriod)
                .map(musicEvent -> musicEvent.getSoundEvent())
                .collect(Collectors.toList())
                ;
    }

    public static HashMap<TimePeriod,List<SoundEvent>> getMusicsOfProvince(Region region){
        HashMap<TimePeriod,List<SoundEvent>> musics = new HashMap<>();
        musics.put(TimePeriod.MORNING, getMusics(TimePeriod.MORNING));
        musics.put(TimePeriod.DAY, getMusics(TimePeriod.DAY));
        musics.put(TimePeriod.EVENING, getMusics(TimePeriod.EVENING));
        musics.put(TimePeriod.NIGHT, getMusics(TimePeriod.NIGHT));
        return musics;
    }
    public static List<MusicEvent> getMusics(MusicKey musicKey){
        return Arrays.stream(MusicEvent.values())
                .filter(musicEvent ->
                    musicEvent.getRegion() == musicKey.getRegion()
                            && musicEvent.getTimePeriod() == musicKey.getTimePeriod()
                )
                .collect(Collectors.toList());
    }

    public static HashMap<MusicKey, List<MusicEvent>> getMusicsOfTamriel(){
        HashMap<MusicKey, List<MusicEvent>> musics = new HashMap<>();
        for(Region region : Region.values()){
            for(TimePeriod timePeriod : TimePeriod.getAllPeriods()){
                MusicKey musicKey = new MusicKey(region,timePeriod);
                musics.put(musicKey, getMusics(musicKey));
            }
        }
        return musics;
    }

}
