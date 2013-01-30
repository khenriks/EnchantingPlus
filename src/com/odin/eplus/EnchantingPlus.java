package com.odin.eplus;

import com.odin.eplus.common.CommonProxy;
import com.odin.eplus.common.blocks.ModBlocks;
import com.odin.eplus.common.handlers.PacketHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = "eplus", name = "Enchanting Plus", version = "0.1")
@NetworkMod(channels = {"eplus"}, clientSideRequired = true, serverSideRequired = true, packetHandler = PacketHandler.class)
public class EnchantingPlus {

    @Instance(value = "eplus")
    public static EnchantingPlus instance;

    @SidedProxy(clientSide = "com.odin.eplus.client.ClientProxy", serverSide = "com.odin.eplus.common.CommonProxy")
    public static CommonProxy proxy;

    @Init
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.instance().registerGuiHandler(instance, proxy);
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event) {

    }

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {

        ModBlocks.init();
    }

}
