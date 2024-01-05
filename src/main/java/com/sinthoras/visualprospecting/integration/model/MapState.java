package com.sinthoras.visualprospecting.integration.model;

import java.util.ArrayList;
import java.util.List;

import com.sinthoras.visualprospecting.integration.model.buttons.ButtonManager;
import com.sinthoras.visualprospecting.integration.model.layers.LayerManager;

public class MapState {

    public static final MapState instance = new MapState();

    public final List<ButtonManager> buttons = new ArrayList<>();
    public final List<LayerManager> layers = new ArrayList<>();

    public MapState() {}
}
