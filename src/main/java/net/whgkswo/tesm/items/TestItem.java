package net.whgkswo.tesm.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;

public class TestItem extends Item{
    public TestItem(Item.Settings settings) {
        super(settings);
    }

    // TODO: 포팅
    /*@Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand){
        playerEntity.playSound(SoundEvents.BLOCK_WOOL_BREAK,1.0F,1.0F);
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext){
        tooltip.add(Text.translatable("테스트 재료입니다.").formatted(Formatting.DARK_RED));
    }*/
}