package com.odin.eplus.common.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotEnchant extends Slot {

    final ContainerEnchanting container;

    public SlotEnchant(ContainerEnchanting containerEnchanting, IInventory tableInventory, int id, int x, int y) {
	super(tableInventory, id, x, y);
	this.container = containerEnchanting;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack) {
	return true;
    }
}
