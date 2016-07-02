package nl.hetisniels.rsmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nl.hetisniels.rsmod.RSMod;
import nl.hetisniels.rsmod.item.ItemBlockBase;
import nl.hetisniels.rsmod.tile.TileBase;

public abstract class BlockBase extends Block {
    private final String name;
    private ItemBlockBase itemBlock;

    public BlockBase(String name) {
        super(Material.ROCK);

        this.name = name;

        setRegistryName(RSMod.ID, name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String getUnlocalizedName() {
        return "block." + RSMod.ID + ":" + name;
    }

    public ItemBlockBase getAsItem() {
        if (this.itemBlock == null)
            this.itemBlock = new ItemBlockBase(this);

        return this.itemBlock;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileBase) {
            TileBase tileBase = (TileBase) tileEntity;

            tileBase.dropItems(world, pos);
        }

        super.breakBlock(world, pos, state);
    }
}
