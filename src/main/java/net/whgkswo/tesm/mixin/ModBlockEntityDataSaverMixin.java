package net.whgkswo.tesm.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.nbt.IBlockEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public class ModBlockEntityDataSaverMixin implements IBlockEntityDataSaver {
    // 커스텀 데이터를 저장할 필드
    private NbtCompound persistentData;

    // 인터페이스 메소드 구현
    @Override
    public NbtCompound getPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }
        return persistentData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, RegistryWrapper.WrapperLookup registries, CallbackInfo ci) {
        if (persistentData != null && !persistentData.isEmpty()) {
            nbt.put(TESMMod.MODID, persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, RegistryWrapper.WrapperLookup registries, CallbackInfo ci) {
        if (nbt.contains(TESMMod.MODID, 10)) { // 10은 NbtCompound 타입
            persistentData = nbt.getCompound(TESMMod.MODID);
        }
    }
}
