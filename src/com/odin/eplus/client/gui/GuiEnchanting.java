package com.odin.eplus.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

import com.odin.eplus.common.inventory.ContainerEnchanting;

public class GuiEnchanting extends GuiContainer {

    public GuiEnchanting(InventoryPlayer inventory, World world, int x, int y, int z) {
	super(new ContainerEnchanting(inventory, world, x, y, z));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
	// TODO Auto-generated method stub

    }

}
