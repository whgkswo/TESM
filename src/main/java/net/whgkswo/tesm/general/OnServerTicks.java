package net.whgkswo.tesm.general;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.whgkswo.tesm.calendar.Time;
import net.whgkswo.tesm.data.JsonManager;
import net.whgkswo.tesm.tags.BiomeTags;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.whgkswo.tesm.general.GlobalVariables.*;

public class OnServerTicks {

    public static int runTimeCounter = 0;
    public static final int INITIALIZE_POINT = 0;
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
            if(player != null){
                /*player.sendMessage(Text.literal("Tick updated"));*/
                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 플레이어가 월드에 접속할 때 실행 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ *runTimeCounter == 0일 때
                if(runTimeCounter == INITIALIZE_POINT){

                    world = server.getOverworld().toServerWorld();
                    //player = world.getPlayers().get(0);

                    commandSource = server.getCommandSource();

                    commandManager = server.getCommandManager();
                    parseResults = commandManager.getDispatcher().parse(parseCommand,commandSource);

                    player.sendMessage(Text.literal("환영합니다!"),false);

                    server.getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK).set(false,server);
                    server.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false,server);
                    server.getGameRules().get(GameRules.RANDOM_TICK_SPEED).set(0, server);

                    updatedChunkSet = JsonManager.readJsonToSet("/updatedChunkSet.json");
                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 바이옴 ID 및 지역 정보 초기화 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

                    previousBiomeID = currentBiomeID;
                    previousRegionTag = getCurrentRegion(world,player.getBlockPos()); //previousRegionTag 초기화
                    player.sendMessage(Text.literal("현재 지역: "+previousRegionTag));

                } else if (runTimeCounter > INITIALIZE_POINT) {
                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 플레이어 좌표 및 바이옴 정보 갱신 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

                    BlockPos playerPos = player.getBlockPos();

                    Optional<RegistryKey<Biome>> biomeRegistry = world.getBiome(playerPos).getKey();
                    RegistryKey<Biome> key = biomeRegistry.get();
                    Identifier biomeID = key.getValue();
                    currentBiomeID=biomeID.toString();

                    //player.sendMessage(Text.literal(playerPos.getX()+","+playerPos.getY()+","+playerPos.getZ()+" "+ currentBiomeID));


                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 내부 메소드 호출 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                    getCompassPos(player);

                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 외부 메소드 호출 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                    /*TESMusicsMain.tesMusicsMain(player,world,commandManager,commandSource);*/
                    //runTimeCounter!=0일 때 (초기화 시점 후 모든 틱마다)
                    if(!Objects.equals(previousBiomeID,currentBiomeID)){ // 바이옴 변경이 일어날 때

                        /*player.sendMessage(Text.literal("바이옴 변경 감지 ("+previousBiomeID+" -> "+currentBiomeID+"), "+currentRegionTag));*/
                        previousBiomeID = currentBiomeID;

                        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 지역 정보 갱신 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

                        currentRegionTag = getCurrentRegion(world,playerPos); // currentRegionTag 갱신

                        if(!Objects.equals(previousRegionTag,currentRegionTag)){

                            player.sendMessage(Text.literal("지역 이동 감지 ("+previousRegionTag+" -> "+currentRegionTag+")"));
                            previousRegionTag = currentRegionTag;
                        }
                    }
                }
                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 타이머 및 인게임 시간 갱신 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

                runTimeCounter++;

                if(timeFlowOn){
                    addTimeCounter++;
                }

                if(addTimeCounter >=3){
                    addTimeCounter = 0;
                    world.setTimeOfDay(world.getTimeOfDay() + 1);
                }
                int previousHour = currentTime.getHour();
                currentTime = Time.getTime();
                if(previousHour == 23 && currentTime.getHour() == 0){
                    currentDate.addDate();
                }
            }
        });
    }
}
