package net.whgkswo.tesm.data;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.minecraft.text.Text;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.pathfinding.v3.NavMeshDataOfChunk;

import java.io.File;
import java.io.IOException;

import static net.whgkswo.tesm.general.GlobalVariables.player;

public class DataSerializer {
    private static final String BASE_PATH = "config/tesm/navmesh/";
    public static void createJson(NavMeshDataOfChunk data, String fileName){
        ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        try{
            long startTime = System.currentTimeMillis();

            File file = new File(BASE_PATH + fileName);
            // 경로가 없으면 생성
            file.getParentFile().mkdirs();
            objectMapper.writeValue(file, data);
            long endTime = System.currentTimeMillis();
            player.sendMessage(Text.literal("청크 스캔 데이터 저장 완료 (" + (endTime - startTime) + "ms)"));
        }catch(IOException e){
            //아무것도 안함
            player.sendMessage(Text.literal(e.getClass().getSimpleName() + " 발생"));
        }
    }
}
