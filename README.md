# VisualProspecting

[![Latest Release](https://img.shields.io/github/v/tag/GT-IMPACT/VisualProspecting?label=Latest&sort=semver)](https://github.com/GT-IMPACT/VisualProspecting/releases/latest)


Add jitpack to your repositories:
```gradle
repositories {
    maven {
        url = "https://jitpack.io"
    }
}
```

Add Visual Prospecting in your dependencies:
```gradle
dependencies {
    compile("com.github.GT-IMPACT:VisualProspecting:$lastVersion")
}
```
### Usage as API

Please keep in mind that chunk coordinates are block coordinates divided by 16! When in doubt you may fall back on:
```java
int chunkX = Utils.coordBlockToChunk(blockX);
```
```java
// blockZ is the lowest block coordinate in a chunk. If you want 
// to iterate over all blocks in that particular chunk you need
// to add [0, ... 15] to it
int blockZ = Utils.coordChunkToBlock(chunkZ);
```

Whenever you detect a new ore vein you need to add custom network payloads and request the information from the logical server yourself. Please do your best to disallow a logical client from querying the complete server database as it would lead to potential abuse. So, please check if the player is allowed to prospect a dimension and location.

If you simply want to notify a logical client from the logical server you may send a [`ProspectingNotification`](https://github.com/GT-IMPACT/VisualProspecting/blob/master/src/main/java/com/sinthoras/visualprospecting/network/ProspectingNotification.java) to the logical client. It will be handled from the client. For example:
```java
final World world;
final int blockX;
final int blockZ;
final int blockRadius;
final EntityPlayerMP entityPlayer;

if(world.isRemote == false) {
    final List<OreVeinPosition> foundOreVeins = VisualProspecting_API.LogicalServer.prospectOreVeinsWithinRadius(world.provider.dimensionId, blockX, blockZ, blockRadius);
    final List<UndergroundFluidPosition> foundUndergroundFluids = VisualProspecting_API.LogicalServer.prospectUndergroundFluidsWithingRadius(world, blockX, blockZ, VP.undergroundFluidChunkProspectingBlockRadius);

    VisualProspecting_API.LogicalServer.sendProspectionResultsToClient(entityPlayer, foundOreVeins, foundUndergroundFluids);
}
```

#### JourneyMap Custom Layer

VisualProspecting provides a light-weight API for custom and interactive layers. This API will keep all maps as optional mod at runtime and not crash you game if it is missing. You may instantiate [`ButtonManager`](https://github.com/GT-IMPACT/VisualProspecting/blob/master/src/main/java/com/sinthoras/visualprospecting/gui/model/buttons/ButtonManager.java) to create your own logical button. Follow it up with an instance of [`LayerButton`](https://github.com/GT-IMPACT/VisualProspecting/blob/master/src/main/java/com/sinthoras/visualprospecting/gui/journeymap/buttons/LayerButton.java) and register both in the [`VisualProspecting_API`](https://github.com/GT-IMPACT/VisualProspecting/blob/master/src/main/java/com/sinthoras/visualprospecting/VisualProspecting_API.java):

```java
ButtonManager buttonManager = new ButtonManager("translation.key", "iconName");
LayerButton layerButton = new LayerButton(buttonManager);

VisualProspecting_API.LogicalClient.registerCustomButtonManager(buttonManager);
VisualProspecting_API.LogicalClient.registerJourneyMapButton(layerButton);
```

If you start the game now, you will see a new button in the menu!

First, you will implement [`ILocationProvider`](https://github.com/GT-IMPACT/VisualProspecting/blob/master/src/main/java/com/sinthoras/visualprospecting/gui/model/locations/ILocationProvider.java). This class is a container and will provide all information required to display your item on screen. It won't do any rendering.

```java
class MyLocation implements ILocationProvider {

    public int getDimensionId() {
        return 0; // overworld
    }
    
    public int getBlockX() {
        return 0;
    }
    
    public int getBlockY() {
        return 65;
    }
    
    public int getBlockZ() {
        return 0;
    }
    
    public String getLabel() {
        return "Hello Minecraft";
    }
}
```

Next up, you'll extend [`LayerManager`](https://github.com/GT-IMPACT/VisualProspecting/blob/master/src/main/java/com/sinthoras/visualprospecting/gui/model/layers/LayerManager.java) and implement the abstract function to generate cached List of your [`ILocationProvider`](https://github.com/GT-IMPACT/VisualProspecting/blob/master/src/main/java/com/sinthoras/visualprospecting/gui/model/locations/ILocationProvider.java) implementation. You should only add whatever items are visible to this list. There are more methods to override, that will assist you with that. Take a look!

```java
class MyLayerManager extends LayerManager {

    public static final MyLayerManager instance = new MyLayerManager();

    public MyLayerManager() {
        super(buttonManager);
    }
    
    protected List<? extends ILocationProvider> generateVisibleElements(int minBlockX, int minBlockZ, int maxBlockX, int maxBlockZ) {
        return Collections.singletonList(new MyLocation);
    }
}
```

You have finished the logical implementation of your custom map layer. Congratulations! Now it is time for the visual integration into the map. This example is provided for JourneyMap, but you might as well take a look at the other possibilities. Since you already implemented the button as a first step, you will need to follow it up with an implementation of `DrawStep`, an interface from JourneyMap. This class will receive an instance of `MyLocation` and perform the actual rendering.

```java
class MyDrawStep implements DrawStep {

    private final MyLocation myLocation;
    
    public MyDrawStep(MyLocation myLocation) {
        this.myLocation = myLocation;
    }
    
    @Override
    public void draw(double draggedPixelX, double draggedPixelY, GridRenderer gridRenderer, float drawScale, double fontScale, double rotation) {
        final double blockSize = Math.pow(2, gridRenderer.getZoom());
        final Point2D.Double blockAsPixel = gridRenderer.getBlockPixelInGrid(myLocation.getBlockX(), myLocation.getBlockZ());
        final Point2D.Double pixel = new Point2D.Double(blockAsPixel.getX() + draggedPixelX, blockAsPixel.getY() + draggedPixelY);
        
        DrawUtil.drawLabel(myLocation.getText(), pixel.getX(), pixel.getY(), DrawUtil.HAlign.Center, DrawUtil.VAlign.Middle, 0, 180, 0x00FFFFFF, 255, fontScale, false, rotation);
    }
}
```

Continue with your own implementation of [`LayerRenderer`](https://github.com/GT-IMPACT/VisualProspecting/blob/master/src/main/java/com/sinthoras/visualprospecting/gui/journeymap/render/LayerRenderer.java). This class will cache all `DrawStep`s and provide it to JourneyMap whenever it is time to render.

```java
class MyLayerRenderer extends LayerRenderer {

    public MyLayerRenderer() {
        // You may skip MyLayerManager and use an existing ButtonManager like "OreVeinLayerManager.instance"
        // Your custom layer will toggle with whatever button you specify here
        super(MyLayerManager.instance);
    }
    
    @Override
    public List<? extends ClickableDrawStep> mapLocationProviderToDrawStep(List<? extends ILocationProvider> visibleElements) {
        final List<MyDrawStep> drawSteps = new ArrayList<>();
        visibleElements.stream()
                .map(element -> (MyLocation) element)
                .forEach(location -> drawSteps.add(new MyDrawStep(location)));
        return drawSteps;
    }

}
```

That's already it! Now you need to register some of these classes. Forge's postInit is a good place for it:

```java
VisualProspecting_API.LogicalClient.registerCustomLayer(MyLayerManager.instance);
VisualProspecting_API.LogicalClient.registerJourneyMapRenderer(new MyLayerRenderer());
```

Now you need to launch Minecraft, teleport to the right sport (`/tp 0 80 0`) and open JourneyMap.

If you want to extend support for your layer in other maps as well you already have half the work done. You just need to add implementations for the layer and element renderer. \
For interactive layers you may take a look at extensions/implementations of [`WaypointProviderManager`](https://github.com/GT-IMPACT/VisualProspecting/blob/master/src/main/java/com/sinthoras/visualprospecting/gui/model/layers/WaypointProviderManager.java) and [`ClickableDrawStep`](https://github.com/GT-IMPACT/VisualProspecting/blob/master/src/main/java/com/sinthoras/visualprospecting/gui/journeymap/drawsteps/ClickableDrawStep.java) to get a head start.
