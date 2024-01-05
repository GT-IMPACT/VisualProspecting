package com.sinthoras.visualprospecting;

import static com.sinthoras.visualprospecting.Utils.isJourneyMapInstalled;
import com.sinthoras.visualprospecting.integration.journeymap.JourneyMapState;
import com.sinthoras.visualprospecting.integration.journeymap.buttons.LayerButton;
import com.sinthoras.visualprospecting.integration.journeymap.render.LayerRenderer;
import com.sinthoras.visualprospecting.integration.model.MapState;
import com.sinthoras.visualprospecting.integration.model.buttons.ButtonManager;
import com.sinthoras.visualprospecting.integration.model.layers.LayerManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
public class VisualProspecting_API {

    @SideOnly(Side.CLIENT)
    public static class LogicalClient {

        // Register the logical button
        public static void registerCustomButtonManager(ButtonManager customManager) {
            MapState.instance.buttons.add(customManager);
        }

        // Register the logical layer
        public static void registerCustomLayer(LayerManager customLayer) {
            MapState.instance.layers.add(customLayer);
        }

        // Register visualization for logical button in JourneyMap
        public static void registerJourneyMapButton(LayerButton customButton) {
            if (isJourneyMapInstalled()) {
                JourneyMapState.instance.buttons.add(customButton);
            }
        }

        // Add the JourneyMap renderer for a layer
        public static void registerJourneyMapRenderer(LayerRenderer customRenderer) {
            if (isJourneyMapInstalled()) {
                JourneyMapState.instance.renderers.add(customRenderer);
            }
        }

        public static void openJourneyLayerMapAt(LayerManager layer, int blockX, int blockZ) {
            if (isJourneyMapInstalled()) {
                layer.activateLayer();
                JourneyMapState.instance.openJourneyMapAt(blockX, blockZ);
            }
        }

        public static void openJourneyLayerMapAt(LayerManager layer, int blockX, int blockZ, int zoom) {
            if (isJourneyMapInstalled()) {
                layer.activateLayer();
                JourneyMapState.instance.openJourneyMapAt(blockX, blockZ, zoom);
            }
        }
    }
}
