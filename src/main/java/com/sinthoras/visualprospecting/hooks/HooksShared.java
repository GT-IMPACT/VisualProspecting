package com.sinthoras.visualprospecting.hooks;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

public class HooksShared {

    // preInit "Run before anything else. Read your config, create blocks, items,
    // etc, and register them with the GameRegistry."
    public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {}

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes."
    public void fmlLifeCycleEvent(FMLInitializationEvent event) {}

    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void fmlLifeCycleEvent(FMLPostInitializationEvent event) {}

    public void fmlLifeCycleEvent(FMLServerAboutToStartEvent event) {}

    // register server commands in this event handler
    public void fmlLifeCycleEvent(FMLServerStartingEvent event) {}

    public void fmlLifeCycleEvent(FMLServerStartedEvent event) {}

    public void fmlLifeCycleEvent(FMLServerStoppingEvent event) {}

    public void fmlLifeCycleEvent(FMLServerStoppedEvent event) {}
}
