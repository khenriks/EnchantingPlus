package com.odin.eplus.client.gui;

import com.odin.eplus.common.inventory.ContainerEnchanting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class GuiEnchanting extends GuiContainer {

    public GuiEnchanting(InventoryPlayer inventory, World world, int x, int y, int z) {
        super(new ContainerEnchanting(inventory, world, x, y, z));

        this.xSize = 209;
        this.ySize = 238;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        int texture = this.mc.renderEngine.getTexture("/eplus/enchant.png");
        mc.renderEngine.bindTexture(texture);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

}
