package net.whgkswo.tesm.data;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.minecraft.text.Text;
import net.whgkswo.tesm.pathfinding.v3.ScanDataOfChunk;

import java.io.File;
import java.io.IOException;

import static net.whgkswo.tesm.general.GlobalVariables.player;

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
    public static ScanDataOfChunk readJson(String filePath){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.readValue(BASE_PATH + filePath + "", ScanDataOfChunk.class);
        }catch(IOException e){
            //아무것도 안함
            player.sendMessage(Text.literal(e.getClass().getSimpleName() + " 발생"));
            return null;
        }
    }
}
