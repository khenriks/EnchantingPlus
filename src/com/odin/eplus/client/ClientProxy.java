package com.odin.eplus.client;

import com.odin.eplus.client.gui.GuiEnchanting;
import com.odin.eplus.common.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        // TODO Auto-generated method stub
        return new GuiEnchanting(player.inventory, world, x, y, z);
    }

}
