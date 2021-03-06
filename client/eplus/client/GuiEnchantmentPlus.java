package eplus.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;
import eplus.common.ContainerEnchanting;
import eplus.common.EnchantingPlus;
import eplus.common.EnchantmentItemData;
import eplus.common.packet.PacketBase;

public class GuiEnchantmentPlus extends GuiContainer {

    public ArrayList<GuiIcon> icons;
    public ArrayList<GuiEnchantmentItem> enchantmentItems;
    public ArrayList<GuiDisenchantmentItem> disenchantmentItems;
    public ArrayList<Enchantment> possibleEnchantments;
    public ArrayList<EnchantmentItemData> possibleDisenchantments;

    public float eScroll;
    public float dScroll;

    public int eIndex;
    public int dIndex;

    public boolean clicked;
    public boolean isEScrolling;
    public boolean isDScrolling;

    public EntityClientPlayerMP player;

    public GuiEnchantmentPlus(EntityPlayer player, World world, int x, int y, int z) {
        super(new ContainerEnchanting(player, world, x, y, z));
        this.player = (EntityClientPlayerMP) player;
        xSize = 209;
        ySize = 238;
        icons = new ArrayList();
        possibleEnchantments = new ArrayList();
        possibleDisenchantments = new ArrayList();
        enchantmentItems = new ArrayList();
        disenchantmentItems = new ArrayList();
    }

    public void drawScreen(int var1, int var2, float var3)
    {
        int var4 = var1 - guiLeft;
        int var5 = var2 - guiTop;
        if (!clicked && Mouse.isButtonDown(0)) {
            if (var4 >= 180 && var4 <= 192) {
                if (var5 >= 16 && var5 <= 88 && enchantmentItems.size() > 4) {
                    isEScrolling = true;
                } else if (var5 >= 90 && var5 <= 144 && disenchantmentItems.size() > 3) {
                    isDScrolling = true;
                }
            }
            for (GuiEnchantmentItem item : enchantmentItems) {
                if (item.isMouseOver(var1, var2) && item.isSlider) {
                    item.sliding = true;
                }
            }
        }
        if (!Mouse.isButtonDown(0)) {
            isEScrolling = false;
            isDScrolling = false;
            for (GuiEnchantmentItem item : enchantmentItems) {
                item.sliding = false;
            }
            selectEnchantments();
        }
        clicked = Mouse.isButtonDown(0);
        if (isEScrolling) {
            eScroll = ((float) (var5 - 6) - 16) / (float) 57;
            if (eScroll > 1) {
                eScroll = 1;
            }
            if (eScroll < 0) {
                eScroll = 0;
            }
            scrollEnchantment(eScroll);
        }
        if (isDScrolling) {
            dScroll = ((float) (var5 - 6) - 90) / (float) 39;
            if (dScroll > 1) {
                dScroll = 1;
            }
            if (dScroll < 0) {
                dScroll = 0;
            }
            scrollDisenchantment(dScroll);
        }
        for (GuiEnchantmentItem item : enchantmentItems) {
            if (item.yPos < guiTop + 16 || item.yPos > guiTop + 87) {
                item.draw = false;
            } else {
                item.draw = true;
            }
            if (item.sliding) {
                item.scroll(var4 - 39);
            }
        }
        for (GuiDisenchantmentItem item : disenchantmentItems) {
            if (item.yPos < guiTop + 90 || item.yPos > guiTop + 143) {
                item.draw = false;
            } else {
                item.draw = true;
            }
        }
        super.drawScreen(var1, var2, var3);
    }

    public void initGui()
    {
        super.initGui();
        GuiIcon.startingX = guiLeft;
        GuiIcon.startingY = guiTop;
        GuiEnchantmentItem.startingX = guiLeft;
        GuiEnchantmentItem.startingY = guiTop;
        GuiDisenchantmentItem.startingX = guiLeft;
        GuiDisenchantmentItem.startingY = guiTop;
        icons.clear();
        icons.add(new GuiIcon("Enchant", 0, 11, 77).setButton().setInfo("This action allows you to selectivly add Enchantments to a item."));
        icons.add(new GuiIcon("Disenchant", 1, 11, 94).setButton().setInfo("This action allows you to selectivly remove Enchantments to a item."));
        icons.add(new GuiIcon("Bookshelves", 6, 11, 8).setInfo("This shows the number of Bookshelves around the Enchantment Table."));
        if (EnchantingPlus.allowRepair)
            icons.add(new GuiIcon("Repair", 2, 11, 111).setButton().setInfo("This action allows you to repair a damaged enchanted item."));
        if (EnchantingPlus.allowTransfer)
            icons.add(new GuiIcon("Transfer", 3, 11, 128).setButton().setInfo("This action allows you to transfer enchantments from 1 item to another."));
        enchantmentItems.clear();

        checkItems();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int i = mc.renderEngine.getTexture("/eplus/enchant.png");
        mc.renderEngine.bindTexture(i);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        this.drawTexturedModalRect(guiLeft + 180, guiTop + 16 + (int) (57 * eScroll), 0 + enchantmentItems.size() > 4 ? 0 : 12, 238, 12, 15);
        this.drawTexturedModalRect(guiLeft + 180, guiTop + 90 + (int) (39 * dScroll), 0 + disenchantmentItems.size() > 3 ? 0 : 12, 238, 12, 15);
        for (GuiEnchantmentItem item : enchantmentItems) {
            item.draw(mc, var2, var3);
        }
        for (GuiDisenchantmentItem item : disenchantmentItems) {
            item.draw(mc, var2, var3);
        }
        for (GuiIcon icon : icons) {
            icon.draw(mc, var2, var3);
            if (icon.isMouseOver(var2, var3)) {
                drawIconString(icon);
            }
        }
        // System.out.println((var2 - guiLeft)+"."+(var3 - guiTop));
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        for (GuiIcon icon : icons) {
            if (icon.isMouseOver(var2, var3)) {
                drawGradientRect(guiLeft - 100, guiTop, guiLeft - 4, guiTop + ySize, -2130706433, -2130706433);
                mc.fontRenderer.drawSplitString(getInfo(icon), guiLeft - 96, guiTop + 4, 92, 0x444444);
            }
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    @Override
    protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4)
    {
        super.handleMouseClick(par1Slot, par2, par3, par4);

        if (par1Slot != null) {
            checkItems();
        }
    }

    public boolean setStack(ItemStack var2)
    {
        if (!getContainer().canSetStack(var2)) {
            return false;
        }
        if (inventorySlots.getSlot(0).getStack() == null) {
            inventorySlots.getSlot(0).putStack(var2);
            return true;
        } else if (inventorySlots.getSlot(1).getStack() == null) {
            inventorySlots.getSlot(1).putStack(var2);
            return true;
        } else {
            return false;
        }
    }

    public void checkItems()
    {
        getContainer().checkItems(this);
    }

    public void selectEnchantments()
    {
        ArrayList var1 = new ArrayList();
        boolean var2[] = new boolean[enchantmentItems.size()];

        for (int i = 0; i < var2.length; i++) {
            var2[i] = true;
        }

        for (int j = 0; j < enchantmentItems.size(); j++) {
            GuiEnchantmentItem var3 = (GuiEnchantmentItem) enchantmentItems.get(j);

            if (var3.level <= 0) {
                continue;
            }

            for (int l = 0; l < enchantmentItems.size(); l++) {
                boolean flag1 = var3.type.canApplyTogether(((GuiEnchantmentItem) enchantmentItems.get(l)).type);
                boolean flag2 = ((GuiEnchantmentItem) enchantmentItems.get(l)).type.canApplyTogether(var3.type);

                if (l != j && (!flag1 || !flag2)) {
                    var2[l] = false;
                }
            }

            var1.add(new EnchantmentData(var3.type, var3.level));
        }

        for (int k = 0; k < var2.length; k++) {
            ((GuiEnchantmentItem) enchantmentItems.get(k)).enabled = var2[k];
        }
        boolean var3 = false;
        boolean var4 = false;
        for (GuiEnchantmentItem item : enchantmentItems) {
            if (item.level > 0) {
                var3 = true;
            }
        }
        for (GuiDisenchantmentItem item : disenchantmentItems) {
            if (item.level > 0) {
                var4 = true;
            }
        }
        getIcon("Enchant").enabled = var3 && canPurchase(getEnchantmentCost());
        getIcon("Disenchant").enabled = var4 && getDisenchantmentCost() > 0;
    }

    public GuiIcon getIcon(String s)
    {
        for (GuiIcon icon : icons) {
            if (icon.id.equals(s)) {
                return icon;
            }
        }
        return null;
    }

    public void drawIconString(GuiIcon var1)
    {
        if (var1.id.equals("Enchant")) {
            mc.fontRenderer.drawString(String.valueOf(getEnchantmentCost()), var1.xPos + 16 + 1, var1.yPos + 4, canPurchase(getEnchantmentCost()) ? 0xff0000 : 0x800000);
        }
        if (var1.id.equals("Disenchant")) {
            mc.fontRenderer.drawString(String.valueOf(getDisenchantmentCost()), var1.xPos + 16 + 1, var1.yPos + 4, 0x00ff00);
        }
        if (var1.id.equals("Bookshelves")) {
            mc.fontRenderer.drawString(String.valueOf(getContainer().bookshelves), var1.xPos + 16 + 1, var1.yPos + 4, 0x00ff00);
        }
        if (var1.id.equals("Repair")) {
            mc.fontRenderer.drawString(String.valueOf(getRepairCost()), var1.xPos + 16 + 1, var1.yPos + 4, canPurchase(getRepairCost()) ? 0xff0000 : 0x800000);
        }
        if (var1.id.equals("Transfer")) {
            mc.fontRenderer.drawString(String.valueOf(getTransferCost()), var1.xPos + 16 + 1, var1.yPos + 4, canPurchase(getTransferCost()) ? 0xff0000 : 0x800000);
        }
    }

    public boolean canPurchase(int var1)
    {
        if (mc.playerController.isInCreativeMode()) {
            return true;
        } else {
            return mc.thePlayer.experienceLevel >= var1 && var1 > 0;
        }
    }

    public ArrayList<EnchantmentItemData> readItem(ItemStack var1)
    {
        if (var1 == null) {
            return new ArrayList();
        }
        if (!var1.isItemEnchanted()) {
            return new ArrayList();
        }
        ArrayList<EnchantmentItemData> list = new ArrayList();
        for (int var2 = 0; var2 < var1.getEnchantmentTagList().tagCount(); var2++) {
            EnchantmentItemData var3;
            NBTTagCompound var4 = (NBTTagCompound) var1.getEnchantmentTagList().tagAt(var2);
            EnchantmentData var5 = new EnchantmentData(Enchantment.enchantmentsList[var4.getShort("id")], var4.getShort("lvl"));
            if (var4.hasKey("bs")) {

                if (var4.getTag("bs").getId() == 2) {
                    var3 = new EnchantmentItemData(var5, (int) var4.getShort("bs"));
                } else if (var4.getTag("bs").getId() == 1) {
                    var3 = new EnchantmentItemData(var5, (int) var4.getByte("bs"));
                } else {
                    var3 = null;
                }
            } else {
                var3 = new EnchantmentItemData(var5, getContainer().bookshelves);
            }
            list.add(var3);
        }
        return list;
    }

    public ContainerEnchanting getContainer()
    {
        return (ContainerEnchanting) inventorySlots;
    }

    public void mouseClicked(int var1, int var2, int var3)
    {
        super.mouseClicked(var1, var2, var3);
        if (var3 == 0) {
            for (GuiEnchantmentItem item : enchantmentItems) {
                if (item.isMouseOver(var1, var2) && !item.isSlider) {
                    item.press();
                }
            }
            for (GuiDisenchantmentItem item : disenchantmentItems) {
                if (item.isMouseOver(var1, var2)) {
                    item.press();
                }
            }
            for (GuiIcon icon : icons) {
                if (icon.isMouseOver(var1, var2) && icon.canClick()) {
                    pressIcon(icon);
                }
            }
        }
    }

    public void handleMouseInput()
    {
        super.handleMouseInput();
        int var1 = Mouse.getDWheel();
        int var2 = (Mouse.getEventX() * this.width / this.mc.displayWidth) - guiLeft;
        int var3 = (this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1) - guiTop;
        if (var1 != 0) {
            if (isMouseOverSection(true, var2, var3)) {
                if (var1 > 0) {
                    if (eIndex > 0) {
                        eIndex--;
                        for (GuiEnchantmentItem item : enchantmentItems) {
                            item.yPos += 18;
                        }
                    }
                }
                if (var1 < 0) {
                    if (enchantmentItems.size() - eIndex > 4) {
                        eIndex++;
                        for (GuiEnchantmentItem item : enchantmentItems) {
                            item.yPos -= 18;
                        }
                    }
                }
                eScroll = ((float) eIndex / (enchantmentItems.size() - 4));
                if (eScroll > 1) {
                    eScroll = 1;
                }
                if (eScroll < 0) {
                    eScroll = 0;
                }
            } else if (isMouseOverSection(false, var2, var3)) {
                if (var1 > 0) {
                    if (dIndex > 0) {
                        dIndex--;
                        for (GuiDisenchantmentItem item : disenchantmentItems) {
                            item.yPos += 18;
                        }
                    }
                }
                if (var1 < 0) {
                    if (possibleDisenchantments.size() - dIndex > 3) {
                        dIndex++;
                        for (GuiDisenchantmentItem item : disenchantmentItems) {
                            item.yPos -= 18;
                        }
                    }
                }
                dScroll = ((float) dIndex / (disenchantmentItems.size() - 3));
                if (dScroll > 1) {
                    dScroll = 1;
                }
                if (dScroll < 0) {
                    dScroll = 0;
                }
            }
        }
    }

    public void onGuiClosed()
    {
    }

    public boolean isMouseOverSection(boolean var1, int var2, int var3)
    {
        if (var1) {
            if (enchantmentItems.size() > 4) {
                return var2 >= 35 && var2 <= 191 && var3 >= 16 && var3 <= 87;
            } else {
                return false;
            }
        } else {
            if (disenchantmentItems.size() > 3) {
                return var2 >= 35 && var2 <= 191 && var3 >= 90 && var3 <= 143;
            } else {
                return false;
            }
        }
    }

    public String getInfo(GuiIcon var1)
    {
        if (var1 == null) {
            return "";
        }
        String var2 = "";
        var2 += var1.id;
        var2 += "\n";
        if (var1.isButton) {
            var2 += "Can use: " + var1.getTranslatedEnabled();
            var2 += "\n";
        }
        var2 += var1.info;
        return var2;
    }

    public void pressIcon(GuiIcon var1)
    {
        if (var1.id.equals("Enchant")) {
            ArrayList<EnchantmentData> var2 = new ArrayList();
            for (GuiEnchantmentItem var3 : enchantmentItems) {
                if (var3.level > 0) {
                    var2.add(new EnchantmentData(var3.type, var3.level));
                }
            }
            if (var2 != null || var2.isEmpty()) {
                try {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    DataOutputStream data = new DataOutputStream(bytes);

                    data.write(getEnchantmentCost());
                    data.write(var2.size());

                    for (int i = 0; i < var2.size(); i++) {
                        data.write(var2.get(i).enchantmentobj.effectId);
                        data.write(var2.get(i).enchantmentLevel);
                    }

                    Packet250CustomPayload packet = PacketBase.createPacket(1, bytes.toByteArray());

                    PacketDispatcher.sendPacketToServer(packet);
                    bytes.close();
                    data.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        if (var1.id.equals("Disenchant")) {
            ArrayList<EnchantmentData> var2 = new ArrayList();
            for (GuiDisenchantmentItem var3 : disenchantmentItems) {
                if (var3.level > 0) {
                    var2.add(new EnchantmentData(var3.type, var3.level));
                }
            }
            if (var2 != null || var2.isEmpty()) {
                try {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    DataOutputStream data = new DataOutputStream(bytes);

                    data.write(getDisenchantmentCost());
                    data.write(var2.size());

                    for (int i = 0; i < var2.size(); i++) {
                        data.write(var2.get(i).enchantmentobj.effectId);
                        data.write(var2.get(i).enchantmentLevel);
                    }

                    Packet250CustomPayload packet = PacketBase.createPacket(2, bytes.toByteArray());

                    PacketDispatcher.sendPacketToServer(packet);
                    bytes.close();
                    data.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (var1.id.equals("Repair") && EnchantingPlus.allowRepair) {
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                DataOutputStream data = new DataOutputStream(bytes);

                data.writeInt(getRepairCost());

                Packet250CustomPayload packet = PacketBase.createPacket(3, bytes.toByteArray());

                PacketDispatcher.sendPacketToServer(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (var1.id.equals("Transfer") && EnchantingPlus.allowTransfer) {
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                DataOutputStream data = new DataOutputStream(bytes);

                data.writeInt(getTransferCost());

                Packet250CustomPayload packet = PacketBase.createPacket(4, bytes.toByteArray());

                PacketDispatcher.sendPacketToServer(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sync();
        checkItems();
    }

    public void sync()
    {
    }

    public void scrollEnchantment(float var1)
    {
        int var2 = enchantmentItems.size() - 4;
        int var3 = (int) Math.floor((double) (var1 * (float) var2));
        for (GuiEnchantmentItem item : enchantmentItems) {
            if (var3 > eIndex) {
                item.yPos -= 18 * (var3 - eIndex);
            } else if (var3 < eIndex) {
                item.yPos += 18 * (eIndex - var3);
            }
        }
        eIndex = var3;
    }

    public void scrollDisenchantment(float var1)
    {
        int var2 = disenchantmentItems.size() - 3;
        int var3 = (int) Math.floor((double) (var1 * (float) var2));
        for (GuiDisenchantmentItem item : disenchantmentItems) {
            if (var3 > dIndex) {
                item.yPos -= 18 * (var3 - dIndex);
            } else if (var3 < dIndex) {
                item.yPos += 18 * (dIndex - var3);
            }
        }
        dIndex = var3;
    }

    public int getEnchantmentCost()
    {
        ItemStack var2 = inventorySlots.getSlot(0).getStack();
        if (var2 == null) {
            return 0;
        }

        int var3 = 0;
        ArrayList<EnchantmentData> var1 = new ArrayList();
        for (GuiEnchantmentItem var4 : enchantmentItems) {
            if (var4.level > 0) {
                var1.add(new EnchantmentData(var4.type, var4.level));
            }
        }
        if (var1.size() == 0) {
            return 0;
        }
        for (EnchantmentData var4 : var1) {
            int var5 = getBaseCost(inventorySlots.getSlot(0).getStack(), var4);
            double var6 = (double) getContainer().bookshelves / 64D;
            var3 += var5 - (int) ((double) var5 * var6);
        }
        double var4 = (double) var2.getItem().getItemEnchantability() / 96D;
        var3 -= (int) ((double) var3 * var4);
        return Math.max(1, var3);
    }

    public int getDisenchantmentCost()
    {
        ItemStack var1 = inventorySlots.getSlot(0).getStack();
        if (var1 == null) {
            return 0;
        }
        int var2 = 0;
        ArrayList<EnchantmentData> var3 = new ArrayList();
        int aint[] = new int[Enchantment.enchantmentsList.length];
        for (GuiDisenchantmentItem var4 : disenchantmentItems) {
            if (var4.level > 0) {
                var3.add(new EnchantmentData(var4.type, var4.level));
                aint[var4.type.effectId] = var4.shelves;
            }
        }
        if (var3.size() == 0) {
            return 0;
        }
        for (EnchantmentData var4 : var3) {
            int var5 = getBaseCost(inventorySlots.getSlot(0).getStack(), var4);
            double var6 = (double) aint[var4.enchantmentobj.effectId] / 64D;
            var2 += var5 - (int) ((double) var5 * var6);
        }
        double var4 = (double) var1.getItem().getItemEnchantability() / 96D;
        var2 -= (int) ((double) var2 * var4);
        return Math.max(1, var2 - (int) ((double) var2 * .1D));
    }

    public int getRepairCost()
    {
        ItemStack var1 = inventorySlots.getSlot(0).getStack();
        if (var1 == null || !var1.isItemDamaged()) {
            return 0;
        }
        int var2 = 0;
        ArrayList<EnchantmentData> var3 = new ArrayList();
        int aint[] = new int[Enchantment.enchantmentsList.length];
        for (EnchantmentItemData var4 : readItem(var1)) {
            var3.add(var4);
            aint[var4.enchantmentobj.effectId] = var4.shelves;
        }
        if (var3.size() == 0) {
            return 1;
        }
        for (EnchantmentData var4 : var3) {
            int var5 = getBaseCost(inventorySlots.getSlot(0).getStack(), var4);
            double var6 = (double) aint[var4.enchantmentobj.effectId] / 64D;
            var2 += var5 - (int) ((double) var5 * var6);
        }
        double var4 = (double) var1.getItem().getItemEnchantability() / 96D;
        var2 -= (int) ((double) var2 * var4);
        var2 = (int) ((double) var2 * .3D);
        double var5 = (double) var1.getItemDamage() / (double) var1.getMaxDamage();
        var2 += (int) ((double) var2 * var5);
        return Math.max(1, var2);
    }

    public int getTransferCost()
    {
        ItemStack var1 = inventorySlots.getSlot(1).getStack();
        if (var1 == null) {
            return 0;
        }
        int var2 = 0;
        ArrayList<EnchantmentData> var3 = new ArrayList();
        int aint[] = new int[Enchantment.enchantmentsList.length];
        for (EnchantmentItemData var4 : readItem(var1)) {
            var3.add(var4);
            aint[var4.enchantmentobj.effectId] = var4.shelves;
        }
        if (var3.size() == 0) {
            return 0;
        }
        for (EnchantmentData var4 : var3) {
            int var5 = getBaseCost(inventorySlots.getSlot(0).getStack(), var4);
            double var6 = (double) aint[var4.enchantmentobj.effectId] / 64D;
            var2 += var5 - (int) ((double) var5 * var6);
        }
        double var4 = (double) var1.getItem().getItemEnchantability() / 96D;
        var2 -= (int) ((double) var2 * var4);
        return Math.max(1, (int) ((double) var2 * .1D));
    }

    public int getBaseCost(ItemStack var1, EnchantmentData var2)
    {
        if (var1 == null) {
            return 0;
        }
        int var3 = 0;
        int var4 = (var2.enchantmentobj.getMinEnchantability(var2.enchantmentLevel) + var2.enchantmentobj.getMaxEnchantability(var2.enchantmentLevel)) / 2;
        var3 = (int) ((float) var3 + (float) var4 * ((float) var2.enchantmentLevel / (float) (var2.enchantmentobj.getMaxLevel() + 3)));
        return Math.max(1, var3);
    }
}