package com.gtveinlocator;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public final class ItemLocator extends Item {

    private final LocatorTier tier;

    public ItemLocator(LocatorTier tier) {
        super(new Item.Properties().stacksTo(1));
        this.tier = tier;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && level instanceof net.minecraft.server.level.ServerLevel serverLevel
                && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            net.minecraft.nbt.CompoundTag result = VeinQuery.build(serverLevel, serverPlayer, tier);
            com.gtveinlocator.network.VPNetwork.sendTo(serverPlayer,
                    new com.gtveinlocator.network.LocatorResultPacket(result));
            VeinQuery.print(serverPlayer, result);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip,
                                TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.gtveinlocator.tier", tier.name())
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.gtveinlocator.radius", tier.radiusChunks)
                .withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("tooltip.gtveinlocator.all")
                .withStyle(ChatFormatting.DARK_GRAY));
        if (tier.fluid) {
            tooltip.add(Component.translatable("tooltip.gtveinlocator.fluid")
                    .withStyle(ChatFormatting.AQUA));
        }
    }
}
