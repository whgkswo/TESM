package net.whgkswo.tesm.sounds.musics;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.sounds.SoundHelper;
import net.whgkswo.tesm.sounds.musics.soundEvents.MusicEvent;
import net.whgkswo.tesm.sounds.musics.soundEvents.MusicKey;

import java.util.*;

import static net.whgkswo.tesm.general.GlobalVariables.player;
import static net.whgkswo.tesm.general.GlobalVariables.world;

public class MusicPlayer {
    public static final HashMap<MusicKey, List<MusicEvent>> MUSICS = MusicEvent.getMusicsOfTamriel();
    private Map<MusicKey, PlayedMusicPool> playedMusicMap = new HashMap<>();
    private int tickCounter;
    private int tickGoal = 100;
    private MusicEvent currentPlaying;
    private boolean isFirstTrackOfCycle;

    public void reset(){
        tickCounter = 0;
        tickGoal = 100;
        currentPlaying = null;
    }

    public void onClientTick(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> this.updateTick());
    }
    private void updateTick(){
        if(MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().isPaused()){
            return;
        }
        tickCounter++;
        if(currentPlaying == null && tickCounter == tickGoal){
            TimePeriod timePeriod = TimePeriod.getPeriod(MinecraftClient.getInstance().world.getTimeOfDay());
            if(timePeriod == TimePeriod.EXCEPTION){
                tickCounter = 0;
                tickGoal = 100;
                return;
            }
            playMusic();
        }
        if(GlobalVariables.player != null){
            GlobalVariables.player.sendMessage(Text.literal(tickCounter + "/" + tickGoal + " - " + currentPlaying));
        }
    }
    private int selectTrackNumber(MusicKey musicKey, int maxTracks){
        // NPE를 방지하기 위한 코드
        if(!playedMusicMap.containsKey(musicKey)){
            playedMusicMap.put(musicKey, new PlayedMusicPool());
        }
        PlayedMusicPool playedMusicPool = playedMusicMap.get(musicKey);
        Set<Integer> playedMusicSet = playedMusicPool.getPlayedMusicSet();

        // 이미 재생된 음악들을 제외하고 가용한 곡들 목록 구성
        List<Integer> availableMusicPool = new ArrayList<>();
        // 첫 곡이라면 마지막 사이클의 마지막 곡도 제외해야 함
        if(isFirstTrackOfCycle && playedMusicPool.getLastIndexOfLatestCycle() != -1){
            playedMusicSet.add(playedMusicPool.getLastIndexOfLatestCycle());
        }
        for(int i = 0; i< maxTracks; i++){
            if(!playedMusicSet.contains(i)){
                availableMusicPool.add(i);
            }
        }
        int trackNumber = new Random().nextInt(availableMusicPool.size());
        // 첫 곡때만 제외했던 지난 사이클의 마지막 트랙을 두 번째부터 다시 재생 가능하게 변경
        if(isFirstTrackOfCycle){
            playedMusicSet.remove(playedMusicPool.getLastIndexOfLatestCycle());
            isFirstTrackOfCycle = false;
        }

        return availableMusicPool.get(trackNumber);
    }
    private void playMusic(){
        Region region = Region.getRegion();
        TimePeriod timePeriod = TimePeriod.getPeriod(world.getTimeOfDay());

        List<MusicEvent> musicList = MUSICS.get(new MusicKey(region,timePeriod));
        if(musicList.isEmpty()){
            return;
        }
        // 재생할 트랙 번호 생성
        int trackNumber = selectTrackNumber(new MusicKey(region, timePeriod), musicList.size());

        MusicEvent music = musicList.get(trackNumber);
        /*player.sendMessage(Text.literal(String.format("%d/%d번 트랙 %s 재생", trackNumber+1,musicList.size(),music.getSoundEvent().getId())));*/

        SoundHelper.playSound(music.getSoundEvent(), SoundCategory.RECORDS);
        currentPlaying = music;
        tickCounter = 0;
        tickGoal = music.getLength();
        MusicKey musicKey = new MusicKey(music.getRegion(),music.getTimePeriod());

        PlayedMusicPool playedMusicPool = playedMusicMap.get(musicKey);
        Set<Integer> playedMusicSet = playedMusicPool.getPlayedMusicSet();

        playedMusicSet.add(trackNumber);
        // 한 사이클 돌았으면 초기화
        if(playedMusicSet.size() == MUSICS.get(musicKey).size()){
            playedMusicSet.clear();
            isFirstTrackOfCycle = true;
            playedMusicSet.add(trackNumber);
            playedMusicPool.setLastIndexOfLatestCycle(trackNumber);
        }
    }
}