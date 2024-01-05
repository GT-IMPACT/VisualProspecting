package com.sinthoras.visualprospecting.mixinplugin;

import static com.sinthoras.visualprospecting.mixinplugin.TargetedMod.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import cpw.mods.fml.relauncher.FMLLaunchHandler;

public enum Mixins {

    // Journeymap mixins
    DisplayVarsAccessorMixin("journeymap.DisplayVarsAccessor", Side.CLIENT, JOURNEYMAP),
    FullscreenAccessorMixin("journeymap.FullscreenAccessor", Side.CLIENT, JOURNEYMAP),
    FullscreenMixin("journeymap.FullscreenMixin", Side.CLIENT, JOURNEYMAP),
    FullscreenActionsMixin("journeymap.FullscreenActionsMixin", Side.CLIENT, JOURNEYMAP),
    MiniMapMixin("journeymap.MiniMapMixin", Side.CLIENT, JOURNEYMAP),
    RenderWaypointBeaconMixin("journeymap.RenderWaypointBeaconMixin", Side.CLIENT, JOURNEYMAP),
    WaypointManagerMixin("journeymap.WaypointManagerMixin", Side.CLIENT, JOURNEYMAP),

    // Vanilla Mixins
    MinecraftServerAccessorMixin("minecraft.MinecraftServerAccessor", Phase.EARLY, Side.BOTH, VANILLA),
    ForgeHooksClientMixin("minecraft.ForgeHooksClientMixin", Phase.EARLY, Side.CLIENT, XAEROMINIMAP, XAEROWORLDMAP);

    public final String mixinClass;
    public final Phase phase;
    private final Side side;
    private final List<TargetedMod> targetedMods;

    Mixins(String mixinClass, TargetedMod... targetedMods) {
        this.mixinClass = mixinClass;
        this.phase = Phase.LATE;
        this.side = Side.BOTH;
        this.targetedMods = Arrays.asList(targetedMods);
    }

    Mixins(String mixinClass, Phase phase, TargetedMod... targetedMods) {
        this.mixinClass = mixinClass;
        this.phase = phase;
        this.side = Side.BOTH;
        this.targetedMods = Arrays.asList(targetedMods);
    }

    Mixins(String mixinClass, Side side, TargetedMod... targetedMods) {
        this.mixinClass = mixinClass;
        this.phase = Phase.LATE;
        this.side = side;
        this.targetedMods = Arrays.asList(targetedMods);
    }

    Mixins(String mixinClass, Phase phase, Side side, TargetedMod... targetedMods) {
        this.mixinClass = mixinClass;
        this.phase = phase;
        this.side = side;
        this.targetedMods = Arrays.asList(targetedMods);
    }

    public boolean shouldLoad(Set<String> loadedCoreMods, Set<String> loadedMods) {
        return shouldLoadSide() && allModsLoaded(targetedMods, loadedCoreMods, loadedMods);
    }

    private boolean shouldLoadSide() {
        return (side == Side.BOTH || (side == Side.SERVER && FMLLaunchHandler.side().isServer())
                || (side == Side.CLIENT && FMLLaunchHandler.side().isClient()));
    }

    private boolean allModsLoaded(List<TargetedMod> targetedMods, Set<String> loadedCoreMods, Set<String> loadedMods) {
        if (targetedMods.isEmpty()) return false;
        for (TargetedMod target : targetedMods) {
            if (target == TargetedMod.VANILLA) continue;
            // Check coremod first
            if (!loadedCoreMods.isEmpty() && target.coreModClass != null
                    && !loadedCoreMods.contains(target.coreModClass)) {
                return false;
            } else if (!loadedMods.isEmpty() && target.modId != null && !loadedMods.contains(target.modId)) {
                return false;
            }
        }
        return true;
    }

    enum Side {
        BOTH,
        CLIENT,
        SERVER
    }

    public enum Phase {
        EARLY,
        LATE
    }

}
