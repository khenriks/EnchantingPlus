package com.odin.eplus.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;

public class ContainerEnchanting extends Container {

    public IInventory tableInventory = new SlotEnchanting(this, "enchanting", false, 2);

    private World worldObj;

    private int xPos;
    private int yPos;
    private int zPos;

    public ContainerEnchanting(InventoryPlayer inventoryPlayer, World worldObj, int x, int y, int z) {
        this.worldObj = worldObj;
        this.xPos = x;
        this.yPos = y;
        this.zPos = z;

        this.addSlotToContainer(new SlotEnchant(this, this.tableInventory, 0, 11, 31));

        int l;

        for (l = 0; l < 3; ++l) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlotToContainer(new Slot(inventoryPlayer, i1 + l * 9 + 9, 17 + i1 * 18, 147 + l * 18));
            }
        }

        for (l = 0; l < 9; ++l) {
            this.addSlotToContainer(new Slot(inventoryPlayer, l, 17 + l * 18, 205));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }


}
