package nl.hetisniels.rsmod.event;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nl.hetisniels.rsmod.block.IBlockHighlight;

public class drawBlockHighlightEventHandler {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
        try {
            EntityPlayer player = event.getPlayer();

            World world = player.worldObj;
            BlockPos blockPos = event.getTarget().getBlockPos();
            IBlockState state = world.getBlockState(blockPos);
            Block block = state.getBlock();

            if (block instanceof IBlockHighlight)
                event.setCanceled(((IBlockHighlight) block).drawBlockHighlight(world, player, blockPos, block, event.getPartialTicks()));
        } catch (NullPointerException e) {
            // do nothing, thrown because the player looked at an entity
        }
    }
}
