package com.sinthoras.visualprospecting.integration.journeymap;

import java.util.ArrayList;
import java.util.List;
import com.sinthoras.visualprospecting.integration.journeymap.buttons.LayerButton;
import com.sinthoras.visualprospecting.integration.journeymap.render.LayerRenderer;
import com.sinthoras.visualprospecting.integration.journeymap.waypoints.WaypointManager;
import com.sinthoras.visualprospecting.mixins.late.journeymap.FullscreenAccessor;

import journeymap.client.render.map.GridRenderer;

public class JourneyMapState {

    public static JourneyMapState instance = new JourneyMapState();

    public final List<LayerButton> buttons = new ArrayList<>();
    public final List<LayerRenderer> renderers = new ArrayList<>();
    public final List<WaypointManager> waypointManagers = new ArrayList<>();

    public JourneyMapState() {

    }

    public void openJourneyMapAt(int blockX, int blockZ) {
        final GridRenderer gridRenderer = FullscreenAccessor.getGridRenderer();
        assert gridRenderer != null;
        gridRenderer.center(gridRenderer.getMapType(), blockX, blockZ, gridRenderer.getZoom());
    }

    public void openJourneyMapAt(int blockX, int blockZ, int zoom) {
        final GridRenderer gridRenderer = FullscreenAccessor.getGridRenderer();
        assert gridRenderer != null;
        gridRenderer.center(gridRenderer.getMapType(), blockX, blockZ, zoom);
    }
}
