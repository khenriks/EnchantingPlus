package com.odin.eplus.client.gui;

import com.odin.eplus.common.inventory.ContainerEnchanting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class GuiEnchanting extends GuiContainer {

    class GuiTabs extends Gui {

        EnchantmentData enchantmentData;

        int xPos;
        int yPos;

        public GuiTabs(EnchantmentData enchantmentData, int xPos, int yPos) {
            this.enchantmentData = enchantmentData;
            this.xPos = xPos;
            this.yPos = yPos;
        }
    }

    ArrayList<GuiTabs> arrayListEnchants = new ArrayList<GuiTabs>();
    ArrayList<GuiTabs> arrayListDisnchants = new ArrayList<GuiTabs>();

    Map enchants;
    Map disenchants;

    public GuiEnchanting(InventoryPlayer inventory, World world, int x, int y, int z) {
        super(new ContainerEnchanting(inventory, world, x, y, z));

        this.xSize = 209;
        this.ySize = 238;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        int texture = this.mc.renderEngine.getTexture("/resources/eplus/enchant.png");
        mc.renderEngine.bindTexture(texture);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        updateEnchants(enchants, ((ContainerEnchanting) this.inventorySlots).getEnchantments());
        updateDisenchants(disenchants, ((ContainerEnchanting) this.inventorySlots).getDisenchantments());

        for (GuiTabs tab : arrayListEnchants) {
            this.mc.fontRenderer.drawString(tab.enchantmentData.enchantmentobj.getName(), tab.xPos, tab.yPos, 0xff0000);
        }
        for (GuiTabs tab : arrayListDisnchants) {
            this.mc.fontRenderer.drawString(tab.enchantmentData.enchantmentobj.getName(), tab.xPos, tab.yPos, 0xff0000);
        }
    }

    public void updateEnchants(Map perm, Map temp) {

        if (perm != null && perm.equals(temp)) {
            return;
        }

        if ((temp != null && perm == null) || (perm != null && !perm.equals(temp))) {
            perm = temp;

            if (perm != null) {
                int y = 0;
                for (Object item : perm.values()) {
                    EnchantmentData enchantmentData = (EnchantmentData) item;
                    arrayListEnchants.add(new GuiTabs(enchantmentData, guiLeft + 36, guiTop + 17 + y * 10));
                    y++;
                }
            } else {
                arrayListEnchants.clear();
            }
        }

        if (temp == null) {
            perm = temp;
            arrayListEnchants.clear();
        }
    }

    public void updateDisenchants(Map perm, Map temp) {

        if (perm != null && perm.equals(temp)) {
            return;
        }

        if ((temp != null && perm == null) || (perm != null && !perm.equals(temp))) {
            perm = temp;

            if (perm != null) {
                int y = 0;

                Iterator iterator = perm.keySet().iterator();

                while (iterator.hasNext()) {
                    int id = (Integer) iterator.next();
                    int level = (Integer) perm.get(id);
                    arrayListDisnchants.add(new GuiTabs(new EnchantmentData(id, level), guiLeft + 36, guiTop + 91 + y * 10));
                    y++;
                }
            } else {
                arrayListDisnchants.clear();
            }
        }

        if (temp == null) {
            perm = temp;
            arrayListDisnchants.clear();
        }
    }

}
