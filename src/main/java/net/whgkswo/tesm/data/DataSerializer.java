package net.whgkswo.tesm.data;



import com.fasterxml.jackson.databind.ObjectMapper;
import net.minecraft.text.Text;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.pathfinding.v3.NavMeshDataOfChunk;

import java.io.File;
import java.io.IOException;

public class DataSerializer {
    private static final String BASE_PATH = "config/tesm/navmesh/";
    public static void createJson(NavMeshDataOfChunk data, String fileName){
        ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        try{
            /*String json = objectMapper.writeValueAsString(data);
            GlobalVariables.player.sendMessage(Text.literal(json));*/

            File file = new File(BASE_PATH + fileName);
            // 경로가 없으면 생성
            file.getParentFile().mkdirs();
            objectMapper.writeValue(file, data);

        }catch(Exception e){
            //아무것도 안함
            GlobalVariables.player.sendMessage(Text.literal(e.getClass().getSimpleName() + " 발생"));
        }
    }
}
