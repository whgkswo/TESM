package net.whgkswo.tesm.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.message.MessageHelper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public abstract class ResourceHelper {
    protected final ResourceManager resourceManager;

    // 클라이언트, 서버가 리소스 매니저가 달라서 구분 필요
    protected ResourceHelper(ResourceManagerProvider provider) {
        this.resourceManager = provider.getResourceManager();
    }

    // 공통 메서드들
    public Resource getResource(String path) {
        Identifier id = Identifier.of(TESMMod.MODID, path);

        Optional<Resource> optionalResource = resourceManager.getResource(id);
        if (optionalResource.isPresent()) {
            return optionalResource.get();
        }else{
            MessageHelper.sendMessage(path + "에 파일이 없습니다.");
            return null;
        }
    }

    public JsonObject getJsonObject(String path) {
        try {
            if(!path.endsWith(".json")) path += ".json";
            Resource resource = getResource(path);
            if (resource != null) {
                Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
                return JsonParser.parseReader(reader).getAsJsonObject();
            }
        } catch (IOException | NullPointerException e) {
            MessageHelper.sendMessage(path + ".json이 없습니다.");
        }
        return new JsonObject();
    }

    public JsonArray getJsonArray(String path) {
        try {
            if(!path.endsWith(".json")) path += ".json";
            Resource resource = getResource(path);
            if (resource != null) {
                Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
                return JsonParser.parseReader(reader).getAsJsonArray();
            }
        } catch (IOException | NullPointerException e) {
            MessageHelper.sendMessage(path + ".json이 없습니다.");
        }
        return new JsonArray();
    }
}
