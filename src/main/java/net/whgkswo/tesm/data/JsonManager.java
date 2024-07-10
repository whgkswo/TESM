package net.whgkswo.tesm.data;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.whgkswo.tesm.data.dto.ChunkPosDto;
import net.whgkswo.tesm.pathfinding.v3.ScanDataOfChunk;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static net.whgkswo.tesm.general.GlobalVariables.player;
import static net.whgkswo.tesm.general.GlobalVariables.world;

public class JsonManager {
    private static final String BASE_PATH = "config/tesm/scandata/";
    public static void createJson(ScanDataOfChunk data, String filePath){
        ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        try{
            File file = new File(BASE_PATH + filePath);
            // 경로가 없으면 생성
            file.getParentFile().mkdirs();
            objectMapper.writeValue(file, data);
        }catch(IOException e){
            //아무것도 안함
            player.sendMessage(Text.literal(e.getClass().getSimpleName() + " 발생"));
        }
    }
    public static<T> void createJson(T data, String filePath){
        ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        try{
            File file = new File(BASE_PATH + filePath);
            // 경로가 없으면 생성
            file.getParentFile().mkdirs();
            objectMapper.writeValue(file, data);
        }catch(IOException e){
            //아무것도 안함
            player.sendMessage(Text.literal(e.getClass().getSimpleName() + " 발생"));
        }
    }
    public static ScanDataOfChunk readJson(String filePath){
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(BlockPos.class, new BlockPosKeyDeserializer());
        objectMapper.registerModule(module);
        File file = new File(BASE_PATH + filePath);

        try{
            return objectMapper.readValue(file, ScanDataOfChunk.class);
        }catch(IOException e){
            //아무것도 안함
            player.sendMessage(Text.literal(e.getClass().getSimpleName() + " 발생"));
            e.printStackTrace();
            return null;
        }
    }
    public static HashSet<ChunkPos> readJsonToSet(String filePath){
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(BASE_PATH + filePath);
        try{
            HashSet<ChunkPosDto> chunkPosDtos = objectMapper.readValue(file, new TypeReference<>() {
            });
            return chunkPosDtos.stream()
                    .map(chunkPosDto -> world.getChunk(chunkPosDto.getX(), chunkPosDto.getZ()).getPos())
                    .collect(Collectors.toCollection(HashSet::new));
        }catch(IOException e){
            //아무것도 안함
            player.sendMessage(Text.literal(e.getClass().getSimpleName() + " 발생"));
            e.printStackTrace();
            return null;
        }
    }
    public static void deleteJson(String filePath){
        File file = new File(BASE_PATH + filePath);
        if(file.exists()){
            file.delete();
        }
    }
    public static boolean isChunkScanDataExist(ChunkPos chunkPos){
        String region = "r." + chunkPos.getRegionX() + "." + chunkPos.getRegionZ();
        String filePath = "/" + chunkPos.x + "." + chunkPos.z + ".json";
        File file = new File("config/tesm/scandata/" + region + filePath);
        return file.exists();
    }
}
