package nl.hetisniels.rsmod.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockHighlight {
	boolean drawBlockHighlight(World world, EntityPlayer player, BlockPos blockPos, Block block, float partialTicks);
}
