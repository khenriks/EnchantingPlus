package com.odin.eplus.common.blocks;

import com.odin.eplus.EnchantingPlus;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockEnchantingTable extends BlockEnchantmentTable {

    protected BlockEnchantingTable(int id) {
        super(id);
    }

    @Override
    public boolean onBlockActivated(World worldObj, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (worldObj.isRemote) {
            return true;
        } else {
            par5EntityPlayer.openGui(EnchantingPlus.instance, 1, worldObj, x, y, z);
            return true;
        }
    }
}
