package net.whgkswo.tesm.musics;

import com.mojang.brigadier.ParseResults;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Random;


public class TESMusicsMain {
    public static int musicTimer;
    public static boolean musicPlaying;
    public static int trackLength = 100;
    static String parseCommand = "";
    public static Identifier currentMusicID = new Identifier("");
    public static String currentMusicString = "음악 준비 중";
    public static SoundEvent soundEvent = SoundEvent.of(currentMusicID);
    static int trackNumber;
    static int randomNumber = 0;

    static Random randomInt = new Random();

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 재생할 트랙 번호 선택 메소드 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    static int selectTrack(int maxTrackNumber, boolean[] musicPlayed, int playCount, int lastPlayedTrack){

        if(playCount==0){
            //이전 사이클에서 마지막으로 재생된 곡이 이번 사이클의 첫 곡으로 연속 재생되는 것을 회피
            while(true){
                randomNumber = randomInt.nextInt(maxTrackNumber-playCount) + 1;
                if(randomNumber!=lastPlayedTrack){
                    break;
                }
            }
        }else{ //두 번째부터는 그대로 무작위
            randomNumber = randomInt.nextInt(maxTrackNumber-playCount) + 1;
        }

        int i = 0;
        trackNumber=0;
        // 이번 사이클에서 이미 재생된 곡은 그냥 넘기고, 아직 재생되지 않은 트랙만 카운트하며 <randomNumber>번째 곡을 탐색
        while(i < randomNumber){
            if (!musicPlayed[trackNumber]){
                i++;
            }
            trackNumber++;
        }
        return trackNumber;
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 사운드 이벤트 생성 메소드 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    public static void inputMusicData(String id, int length){

        currentMusicID = new Identifier("tesm:"+id);
        currentMusicString = currentMusicID.getPath();
        soundEvent = SoundEvent.of(currentMusicID);
        trackLength = length;
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 음악 재생 메소드 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    static void playMusic(World world,PlayerEntity player,SoundEvent soundEvent){
        world.playSound(null, player.getBlockPos(), soundEvent, SoundCategory.RECORDS, 1f, 1f);
        //player.sendMessage(Text.literal( trackNumber+"번 트랙이자 재생되지 않은 " + randomNumber + "번째 트랙인 " + currentMusicString + " 재생"));
    }
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 음악 정지 메소드 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    static void stopMusic(CommandManager commandManager, ParseResults parseResults){
        parseCommand = "stopsound @p record";
        commandManager.execute(parseResults, parseCommand);
        musicTimer = 0;
        musicPlaying = false;
        trackLength = 100;
        currentMusicString = "음악 준비 중";
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 사이클 정보 갱신 메소드 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    static int[] cycleUpdater(int maxTrackNumber, boolean[] musicPlayed, int playCount){

        playCount++;

        // 사이클이 끝났을 경우 초기화하고 새 사이클 시작
        if(playCount >= maxTrackNumber){
            Arrays.fill(musicPlayed,false);
            playCount = 0;
        }else { //사이클이 끝나지 않았다면 이번에 재생한 곡에 표시 (한 사이클 내 중복 재생 피하기)
            musicPlayed[trackNumber-1] = true;
        }
        return new int[] {playCount,trackNumber};
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 음악팩 본체 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    public static void tesMusicsMain(PlayerEntity player, World world, CommandManager commandManager, ServerCommandSource source) {

        ParseResults<ServerCommandSource> parseResults = commandManager.getDispatcher().parse("",source);
        musicTimer++;

        //player.sendMessage(Text.literal("AmbientTimer:"+ musicTimer + "/" + trackLength+" "+ currentMusicString),false);

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 지역별 음악 재생 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

        if(!world.isClient){
            if(!musicPlaying){
                if(musicTimer >= 100) {
                    PlayMusicCYR.playMusicCYR(world,player);
                    musicTimer = 0;
                    musicPlaying = true;
                }
            }else{
                if(musicTimer >= trackLength){
                    stopMusic(commandManager,parseResults);
                }
            }
        }
    }
}