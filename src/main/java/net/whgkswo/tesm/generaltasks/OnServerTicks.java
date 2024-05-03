package net.whgkswo.tesm.generaltasks;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.brigadier.ParseResults;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.GameRuleCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.whgkswo.tesm.musics.TESMusicsMain;
import net.whgkswo.tesm.pathfinding.PathFinder;
import net.whgkswo.tesm.tags.BiomeTags;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OnServerTicks {

    public static int runTimeCounter = 0;
    public static boolean timeFlowOn = true;
    static int addTimeCounter = 0;
    static String parseCommand = "";
    static String currentBiomeID = "";
    static String previousBiomeID = "";
    static String currentRegionTag = "";
    static String previousRegionTag = "";
    static final List<TagKey<Biome>> REGION_TAGS = Arrays.asList(BiomeTags.CYR_EXTERIORS,BiomeTags.MW_EXTERIORS,BiomeTags.SKY_EXTERIORS,BiomeTags.TEST_TAG);

    static float playerYaw;
    public static float northCompassPos;
    public static float westCompassPos;
    public static float southCompassPos;
    public static float eastCompassPos;

    public static boolean northCompassOn;
    public static boolean westCompassOn;
    public static boolean southCompassOn;
    public static boolean eastCompassOn;

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 플레이어 현재 지역 검사 메소드 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    private static String getCurrentRegion(World world, BlockPos playerPos){

        for (TagKey<Biome> regionTag: OnServerTicks.REGION_TAGS){
            if(world.getBiome(playerPos).isIn(regionTag)){  //바이옴이 어떤 태그에 속하는지 검사
                return regionTag.id().getPath();
            }
        }
        return "Unidentified";
    }
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 플레이어 방향과 동서남북 차이 검사 메소드 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    public static void getCompassPos(PlayerEntity player){

        playerYaw = player.getYaw();

        if(playerYaw>=135){
            northCompassPos = 180-playerYaw;
            northCompassOn = true;
        } else if (playerYaw<=-135) {
            northCompassPos = (180+playerYaw)*-1;
            northCompassOn = true;
        }else{
            northCompassOn = false;
        }

        if(45<=playerYaw && playerYaw<=135){
            westCompassPos = 90-playerYaw;
            westCompassOn = true;
        }else{
            westCompassOn = false;
        }

        if(-45<=playerYaw && playerYaw<=45){
            southCompassPos = -playerYaw;
            southCompassOn = true;
        }else {
            southCompassOn = false;
        }

        if(-135<=playerYaw && playerYaw<=-45){
            eastCompassPos = -90-playerYaw;
            eastCompassOn = true;
        }else {
            eastCompassOn = false;
        }
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ OnServerTicks 본체 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    public static void onServerTick() {

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            World world = server.getOverworld();
            server.getPlayerManager().getPlayerList().forEach(player -> {
                ServerCommandSource source = player.getServer().getCommandSource();
                CommandManager commandManager = player.getServer().getCommandManager();
                ParseResults<ServerCommandSource> parseResults = commandManager.getDispatcher().parse(parseCommand,source);

                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 플레이어가 월드에 접속할 때 실행 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ *runTimeCounter == 0일 때
                if(runTimeCounter == 0){

                    player.sendMessage(Text.literal("환영합니다!"),false);

                    server.getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK).set(false,server);
                    server.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false,server);

                }
                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 플레이어 좌표 및 바이옴 정보 갱신 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

                BlockPos playerPos = player.getBlockPos();

                Optional<RegistryKey<Biome>> biomeRegistry = world.getBiome(playerPos).getKey();
                RegistryKey<Biome> key = biomeRegistry.get();
                Identifier biomeID = key.getValue();
                currentBiomeID=biomeID.toString();

                //player.sendMessage(Text.literal(playerPos.getX()+","+playerPos.getY()+","+playerPos.getZ()+" "+ currentBiomeID));


                if(runTimeCounter==0){
                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 바이옴 ID 및 지역 정보 초기화 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

                    previousBiomeID = currentBiomeID;
                    previousRegionTag = getCurrentRegion(world,playerPos); //previousRegionTag 초기화
                    player.sendMessage(Text.literal("현재 지역: "+previousRegionTag));

                }else{ //runTimeCounter!=0일 때 (초기화 시점 후 모든 틱마다)
                    if(!Objects.equals(previousBiomeID,currentBiomeID)){ // 바이옴 변경이 일어날 때

                        player.sendMessage(Text.literal("바이옴 변경 감지 ("+previousBiomeID+" -> "+currentBiomeID+"), "+currentRegionTag));
                        previousBiomeID = currentBiomeID;

                        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 지역 정보 갱신 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

                        currentRegionTag = getCurrentRegion(world,playerPos); // currentRegionTag 갱신

                        if(!Objects.equals(previousRegionTag,currentRegionTag)){

                            player.sendMessage(Text.literal("지역 이동 감지 ("+previousRegionTag+" -> "+currentRegionTag+")"));
                            previousRegionTag = currentRegionTag;
                        }
                    }
                }
                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 내부 메소드 호출 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                getCompassPos(player);

                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 외부 메소드 호출 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                TESMusicsMain.tesMusicsMain(player,world,commandManager,source);


                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 타이머 및 인게임 시간 갱신 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

                runTimeCounter++;

                if(timeFlowOn){
                    addTimeCounter++;
                }

                if(addTimeCounter >=3){
                    addTimeCounter = 0;
                    parseCommand = "time add 1";
                    commandManager.execute(parseResults, parseCommand);
                }

            });
        });
    }
}
