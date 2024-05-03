package net.whgkswo.tesm.musics;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import static net.whgkswo.tesm.musics.TESMusicsMain.*;


public class PlayMusicCYR {

    static final int MAXTRACKNUMBER_MORNINGCYR = 4;
    static final int MAXTRACKNUMBER_DAYCYR = 11;
    static final int MAXTRACKNUMBER_EVENINGCYR = 9;
    static final int MAXTRACKNUMBER_NIGHTCYR = 10;

    static boolean[] morningCYR_Played = new boolean[MAXTRACKNUMBER_MORNINGCYR];
    static boolean[] dayCYR_Played = new boolean[MAXTRACKNUMBER_DAYCYR];
    static boolean[] eveningCYR_Played = new boolean[MAXTRACKNUMBER_EVENINGCYR];
    static boolean[] nightCYR_Played = new boolean[MAXTRACKNUMBER_NIGHTCYR];

    static int morningCYR_PlayCount = 0;
    static int dayCYR_PlayCount = 0;
    static int eveningCYR_PlayCount = 0;
    static int nightCYR_PlayCount = 0;

    static int morningCYR_LastPlayedTrack = 0;
    static int dayCYR_LastPlayedTrack = 0;
    static int eveningCYR_LastPlayedTrack = 0;
    static int nightCYR_LastPlayedTrack = 0;

    public static void playMusicCYR(World world, PlayerEntity player){

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 아침 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

        if(world.getTimeOfDay()%24000 > 22500
                || world.getTimeOfDay()%24000 <= 1500){

            switch (selectTrack(MAXTRACKNUMBER_MORNINGCYR, morningCYR_Played, morningCYR_PlayCount, morningCYR_LastPlayedTrack)){
                case 1:
                    inputMusicData("assalto_04_final",3500);
                    break;
                case 2:
                    inputMusicData("king_and_country",4920);
                    break;
                case 3:
                    inputMusicData("these_verdant_fields",6600);
                    break;
                case 4:
                    inputMusicData("through_the_valley",5220);
                    break;
            }
            playMusic(world,player,soundEvent);
            int[] cycleUpdater_Return = cycleUpdater(MAXTRACKNUMBER_MORNINGCYR,morningCYR_Played,morningCYR_PlayCount);
            morningCYR_PlayCount = cycleUpdater_Return[0];
            morningCYR_LastPlayedTrack = cycleUpdater_Return[1];
        }
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 낮 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

        else if (world.getTimeOfDay()%24000 > 2000
                && world.getTimeOfDay()%24000 <= 10000) {

            switch (selectTrack(MAXTRACKNUMBER_DAYCYR, dayCYR_Played, dayCYR_PlayCount, dayCYR_LastPlayedTrack)){
                case 1:
                    inputMusicData("bloodlines",6700);
                    break;
                case 2:
                    inputMusicData("fated_lands",4540);
                    break;
                case 3:
                    inputMusicData("glory_of_cyrodiil",3000);
                    break;
                case 4:
                    inputMusicData("heartlands_sigh",6160);
                    break;
                case 5:
                    inputMusicData("kynareths_wish",6240);
                    break;
                case 6:
                    inputMusicData("lost_glory",5800);
                    break;
                case 7:
                    inputMusicData("maker_of_worlds",7100);
                    break;
                case 8:
                    inputMusicData("ode_to_a_dynasty",5720);
                    break;
                case 9:
                    inputMusicData("of_storms_and_half_light",6780);
                    break;
                case 10:
                    inputMusicData("the_sunlight_wanderer",3080);
                    break;
                case 11:
                    inputMusicData("widows_lament",6240);
                    break;
            }
            playMusic(world,player,soundEvent);
            int[] cycleUpdater_Return = cycleUpdater(MAXTRACKNUMBER_DAYCYR,dayCYR_Played,dayCYR_PlayCount);
            dayCYR_PlayCount = cycleUpdater_Return[0];
            dayCYR_LastPlayedTrack = cycleUpdater_Return[1];
        }
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 저녁 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

        else if (world.getTimeOfDay()%24000 > 11000
                && world.getTimeOfDay()%24000 <= 13000) {

            switch (selectTrack(MAXTRACKNUMBER_EVENINGCYR, eveningCYR_Played, eveningCYR_PlayCount, eveningCYR_LastPlayedTrack)){
                case 1:
                    inputMusicData("ballad_of_dusk",8320);
                    break;
                case 2:
                    inputMusicData("brumas_frozen_sorrow",2880);
                    break;
                case 3:
                    inputMusicData("e3_teaser_music",2160);
                    break;
                case 4:
                    inputMusicData("the_nibenese_trails",3160);
                    break;
                case 5:
                    inputMusicData("remembering_the_last_great_dynasty",3020);
                    break;
                case 6:
                    inputMusicData("reverence",2280);
                    break;
                case 7:
                    inputMusicData("rising_hope_during_sundown",2620);
                    break;
                case 8:
                    inputMusicData("times_of_change_for_the_restless",2860);
                    break;
                case 9:
                    inputMusicData("vigilance",2180);
                    break;
            }
            playMusic(world,player,soundEvent);
            int[] cycleUpdater_Return = cycleUpdater(MAXTRACKNUMBER_EVENINGCYR,eveningCYR_Played,eveningCYR_PlayCount);
            eveningCYR_PlayCount = cycleUpdater_Return[0];
            eveningCYR_LastPlayedTrack = cycleUpdater_Return[1];
        }
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 밤 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

        else if (world.getTimeOfDay()%24000 > 13500
                && world.getTimeOfDay()%24000 <= 21500) {

            switch (selectTrack(MAXTRACKNUMBER_NIGHTCYR, nightCYR_Played, nightCYR_PlayCount, nightCYR_LastPlayedTrack)){
                case 1:
                    inputMusicData("arcana_magicae",4680);
                    break;
                case 2:
                    inputMusicData("auriels_ascension",3740);
                    break;
                case 3:
                    inputMusicData("lorkhans_footsteps",3620);
                    break;
                case 4:
                    inputMusicData("moon_and_scars",5840);
                    break;
                case 5:
                    inputMusicData("nox_atra",5080);
                    break;
                case 6:
                    inputMusicData("restless_secrets",2780);
                    break;
                case 7:
                    inputMusicData("shapeless",2900);
                    break;
                case 8:
                    inputMusicData("the_sleeping_sky",4960);
                    break;
                case 9:
                    inputMusicData("so_blind_and_in_terror",4480);
                    break;
                case 10:
                    inputMusicData("wilder_hearts",3860);
                    break;
            }
            playMusic(world,player,soundEvent);
            int[] cycleUpdater_Return = cycleUpdater(MAXTRACKNUMBER_NIGHTCYR,nightCYR_Played,nightCYR_PlayCount);
            nightCYR_PlayCount = cycleUpdater_Return[0];
            nightCYR_LastPlayedTrack = cycleUpdater_Return[1];
        }
        else {
            musicTimer = 0;
            musicPlaying = false;
            trackLength = 100;
            currentMusicString = "틈새 시간대";
        }
    }
}
