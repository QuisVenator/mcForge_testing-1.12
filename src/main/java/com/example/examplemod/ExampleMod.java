package com.example.examplemod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import com.example.examplemod.init.ModRecipes;
import com.example.examplemod.proxy.CommonProxy;
import com.example.examplemod.util.handlers.GuiHandler;

@Mod(modid = ExampleMod.MOD_ID, name = ExampleMod.NAME, version = ExampleMod.VERSION)
public class ExampleMod
{
    public static final String MOD_ID = "qv_testing";
    public static final String NAME = "QV Test Mod";
    public static final String VERSION = "0.1";
    public static final String ACCEPTED_VERSIONS = "[1.12.2]";
    public static final String CLIENT_PROXY_CLASS = "com.example.examplemod.proxy.ClientProxy";
    public static final String COMMON_PROXY_CLASS = "com.example.examplemod.proxy.CommonProxy";
    
    @Instance
    public static ExampleMod instance;
    
    @SidedProxy(clientSide = ExampleMod.CLIENT_PROXY_CLASS, serverSide = ExampleMod.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @EventHandler
    public static void PreInit(FMLPreInitializationEvent event)
    {

    }

    @EventHandler
    public static void init(FMLInitializationEvent event)
    {
    	ModRecipes.init();
    	GuiHandler.init();
    }

    @EventHandler
    public static void PostInit(FMLPostInitializationEvent event)
    {
    	
    }
}
