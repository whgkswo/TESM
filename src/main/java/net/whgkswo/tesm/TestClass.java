package net.whgkswo.tesm;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class TestClass {
    public static final Identifier DIRT_BROKEN = new Identifier(TESMMod.MODID, "dirt_broken");
    public static void testClass(){
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
            if (state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.DIRT) {
                StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(world.getServer());
                // Increment the amount of dirt blocks that have been broken
                serverState.totalDirtBlocksBroken += 1;

                // Send a packet to the client
                MinecraftServer server = world.getServer();

                PacketByteBuf data = PacketByteBufs.create();
                data.writeInt(serverState.totalDirtBlocksBroken);

                ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player.getUuid());
                server.execute(() -> {
                    ServerPlayNetworking.send(playerEntity, DIRT_BROKEN, data);
                });
            }
        });
    }

    public static void main(String[] args) {
        /*HashMap<BlockPos,Integer> map = new HashMap<>();
        map.put(new BlockPos(1,2,3), 1);
        System.out.println(map.get(new BlockPos(1,2,3)));*/

        BlockPos posA = new BlockPos(1,2,3);
        BlockPos posB = new BlockPos(1,2,3);
        System.out.println(posB.up(0).getY());

    }
}
