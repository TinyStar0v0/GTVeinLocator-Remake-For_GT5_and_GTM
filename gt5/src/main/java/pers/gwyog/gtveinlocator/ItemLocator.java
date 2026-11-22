package pers.gwyog.gtveinlocator;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import cpw.mods.fml.common.Loader;

public class ItemLocator extends Item {

    private final int tier;
    private final int radiusBlocks;

    public ItemLocator(String name, int tier, int radiusBlocks) {
        this.tier = tier;
        this.radiusBlocks = radiusBlocks;
        setUnlocalizedName(GTVeinLocator.MODID + "." + name);
        setTextureName(GTVeinLocator.MODID + ":" + name);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) {
            return stack;
        }
        List<GT5Scanner.FoundVein> found = GT5Scanner.scan(player, radiusBlocks);
        if (Loader.isModLoaded("visualprospecting")) {
            VPIntegration.publish(player, found, radiusBlocks);
        } else {
            VPIntegration.sendFallback(player, found);
        }
        player.addChatMessage(
            new ChatComponentText(
                "\u00a7a[GT Vein Locator] Tier " + tier
                    + ", radius "
                    + (radiusBlocks / 16)
                    + " chunks, found "
                    + found.size()
                    + " vein(s)."));
        return stack;
    }
}
