package com.odin.eplus.common.inventory;

import net.minecraft.inventory.InventoryBasic;

public class SlotEnchanting extends InventoryBasic {

    final ContainerEnchanting container;

    public SlotEnchanting(ContainerEnchanting container, String invName, boolean customName, int slotsCount) {
        super(invName, customName, slotsCount);
        this.container = container;
    }

    @Override
    public void onInventoryChanged() {
        container.onCraftMatrixChanged(this);
    }
}
