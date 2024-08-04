package net.whgkswo.tesm.general;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.whgkswo.tesm.sounds.musics.MusicPlayer;

import static net.whgkswo.tesm.general.GlobalVariablesClient.*;

public class ClientEvents {
    public static void onGameStart(){
        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            if(musicPlayer == null){
                GlobalVariablesClient.musicPlayer = new MusicPlayer();
                musicPlayer.onClientTick();
            }else{
                musicPlayer.reset();
            }
        }));
    }
    public static void getArrowState(){
        ClientTickEvents.END_CLIENT_TICK.register(client ->{
            arrowCounter++;
            if (arrowCounter>=15){
                arrowState = !arrowState;
                arrowCounter = 0;
            }
        });
    }
}
