package net.whgkswo.tesm;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.ArrayList;

public class StateSaverAndLoader extends PersistentState {
    public Integer totalDirtBlocksBroken = 0;

    @Override // 세션이 종료될 때 totalDirtBlocksBroken값을 NBT형식으로 변환하여 서버에 저장
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("totalDirtBlocksBroken", totalDirtBlocksBroken);
        return nbt;
    }
    // 세션이 시작될 때, 저장된 NBT에서 totalDirtBlocksBroken 필드를 읽습니다.
    // 새로운 StateSaverAndLoader 객체를 만들고, 그 안에 Nbt태그에서 읽어온 값을 넣습니다.
    public static StateSaverAndLoader createFromNbt(NbtCompound tag) {
        StateSaverAndLoader state = new StateSaverAndLoader();
        state.totalDirtBlocksBroken = tag.getInt("totalDirtBlocksBroken");
        return state;
    }

    // getServerState를 사용하기 위한 준비 작업
    private static Type<StateSaverAndLoader> type = new Type<>(
            StateSaverAndLoader::new, // If there's no 'StateSaverAndLoader' yet create one
            StateSaverAndLoader::createFromNbt, // If there is a 'StateSaverAndLoader' NBT, parse it with 'createFromNbt'
            null // Supposed to be an 'DataFixTypes' enum, but we can just pass null
    );
    // 서버에서 StateSaverAndLoader 인스턴스를 가져오거나(있다면) 생성합니다.(없다면)
    // 이 경우 createFromNbt에서 인스턴스를 만들었으므로, 그걸 가져옵니다. (아닌가? Type에서 가져오는 것 같기도 하고 모르겠음)
    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        // (Note: arbitrary choice to use 'World.OVERWORLD' instead of 'World.END' or 'World.NETHER'.  Any work)
        // 현재 월드의 PersistentStateManager를 가져옵니다. 이것은 세션 간에 데이터를 유지하고 관리하는 역할을 합니다.
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        // The first time the following 'getOrCreate' function is called, it creates a brand new 'StateSaverAndLoader' and
        // stores it inside the 'PersistentStateManager'. The subsequent calls to 'getOrCreate' pass in the saved
        // 'StateSaverAndLoader' NBT on disk to our function 'StateSaverAndLoader::createFromNbt'.
        // 특정 타입과 식별자를 가진 StateSaverAndLoader 객체를 가져오거나 생성합니다.
        StateSaverAndLoader state = persistentStateManager.getOrCreate(type, TESMMod.MODID);

        // If state is not marked dirty, when Minecraft closes, 'writeNbt' won't be called and therefore nothing will be saved.
        // Technically it's 'cleaner' if you only mark state as dirty when there was actually a change, but the vast majority
        // of mod writers are just going to be confused when their data isn't being saved, and so it's best just to 'markDirty' for them.
        // Besides, it's literally just setting a bool to true, and the only time there's a 'cost' is when the file is written to disk when
        // there were no actual change to any of the mods state (INCREDIBLY RARE).
        state.markDirty();

        return state;
    }
}
