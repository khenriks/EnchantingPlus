/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.odin.eplus.common.inventory;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ContainerEnchanting extends Container {

    public IInventory tableInventory = new SlotEnchanting(this, "enchanting", false, 2);

    private World worldObj;

    private int xPos;
    private int yPos;
    private int zPos;

    private Map enchantments;
    private Map disenchantments;

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

    public Map getEnchantments() {
        return enchantments;
    }

    public Map getDisenchantments() {
        return disenchantments;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory) {
        checkItems();
        super.onCraftMatrixChanged(par1IInventory);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void checkItems() {
        ItemStack itemStack;
        this.enchantments = null;
        this.disenchantments = null;

        for (int i = 0; i < this.tableInventory.getSizeInventory(); i++) {

            itemStack = this.tableInventory.getStackInSlot(i);
            if (itemStack != null) {
                this.enchantments = EnchantmentHelper.mapEnchantmentData(30, itemStack);
                this.disenchantments = EnchantmentHelper.getEnchantments(itemStack);
            }
        }
    }
}
