package com.odin.eplus.common.blocks;

import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {

    public static void init() {

	Block.blocksList[Block.enchantmentTable.blockID] = null;

	Block enchantingTable = new BlockEnchantingTable(Block.enchantmentTable.blockID).setHardness(5.0F).setResistance(2000.0F).setBlockName("enchantmentTable");
	GameRegistry.registerBlock(enchantingTable, "enchantingTable"); //$NON-NLS-1$
    }

}
