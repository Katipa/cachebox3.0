/*
 * Copyright (C) 2016-2017 team-cachebox.de
 *
 * Licensed under the : GNU General Public License (GPL);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/gpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.longri.cachebox3.gui.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.GetName;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.VisUI;
import de.longri.cachebox3.CB;
import de.longri.cachebox3.CacheboxMain;
import de.longri.cachebox3.gui.CacheboxMapAdapter;
import de.longri.cachebox3.gui.map.MapMode;
import de.longri.cachebox3.gui.map.MapState;
import de.longri.cachebox3.gui.map.MapViewPositionChangedHandler;
import de.longri.cachebox3.gui.map.baseMap.AbstractManagedMapLayer;
import de.longri.cachebox3.gui.map.baseMap.BaseMapManager;
import de.longri.cachebox3.gui.map.baseMap.OSciMap;
import de.longri.cachebox3.gui.map.layer.*;
import de.longri.cachebox3.gui.skin.styles.MapArrowStyle;
import de.longri.cachebox3.gui.skin.styles.MapWayPointItemStyle;
import de.longri.cachebox3.gui.stages.StageManager;
import de.longri.cachebox3.gui.widgets.MapCompass;
import de.longri.cachebox3.gui.widgets.MapStateButton;
import de.longri.cachebox3.gui.widgets.ZoomButton;
import de.longri.cachebox3.locator.Coordinate;
import de.longri.cachebox3.locator.Locator;
import de.longri.cachebox3.settings.Config;
import de.longri.cachebox3.settings.Settings;
import de.longri.cachebox3.settings.Settings_Map;
import de.longri.cachebox3.utils.DEBUG_SB;
import org.oscim.backend.CanvasAdapter;
import org.oscim.backend.Platform;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.core.MapPosition;
import org.oscim.core.Tile;
import org.oscim.event.Event;
import org.oscim.gdx.GestureHandlerImpl;
import org.oscim.gdx.MotionHandler;
import org.oscim.layers.GroupLayer;
import org.oscim.layers.Layer;
import org.oscim.layers.TileGridLayer;
import org.oscim.map.Layers;
import org.oscim.map.Map;
import org.oscim.renderer.BitmapRenderer;
import org.oscim.renderer.GLViewport;
import org.oscim.renderer.MapRenderer;
import org.oscim.renderer.atlas.TextureAtlas;
import org.oscim.renderer.atlas.TextureRegion;
import org.oscim.renderer.bucket.TextItem;
import org.oscim.renderer.bucket.TextureBucket;
import org.oscim.renderer.bucket.TextureItem;
import org.oscim.scalebar.*;
import org.oscim.utils.FastMath;
import org.oscim.utils.TextureAtlasUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;


/**
 * The MapView has transparent background. The Map render runs at CacheboxMain.
 * This View has only the controls for the Map!
 * Created by Longri on 24.07.16.
 */
public class MapView extends AbstractView {
    final static Logger log = LoggerFactory.getLogger(MapView.class);

    private InputMultiplexer mapInputHandler;
    private CacheboxMapAdapter map;
    private final CacheboxMain main;
    private MapScaleBarLayer mapScaleBarLayer;
    private float myBearing;
    private final MapStateButton mapStateButton;
    private final MapCompass mapOrientationButton;
    private final ZoomButton zoomButton;
    private WaypointLayer wayPointLayer;
    private DirectLineLayer directLineLayer;
    private final LinkedHashMap<Object, TextureRegion> textureRegionMap;
    private final MapState lastMapState = new MapState();

    LocationAccuracyLayer myLocationAccuracy;

    private LocationLayer myLocationLayer;

    MapViewPositionChangedHandler positionChangedHandler;


    public MapView(CacheboxMain main) {
        super("MapView");
        this.setTouchable(Touchable.disabled);
        this.main = main;
        this.textureRegionMap = createTextureAtlasRegions();

        mapStateButton = new MapStateButton(new MapStateButton.StateChangedListener() {

            private final Event selfEvent = new Event();

            @Override
            public void stateChanged(MapMode mapMode, MapMode lastMapMode, Event event) {

                if (mapMode == MapMode.CAR) {
                    lastMapState.setMapMode(lastMapMode);
                    log.debug("Activate Carmode with last mapstate:" + lastMapState);
                    float bearing = -Locator.getHeading();
                    positionChangedHandler.setBearing(bearing);
                    mapOrientationButton.setOrientation(-bearing);
                    positionChangedHandler.positionChanged(new Event());
                } else if (lastMapMode == MapMode.CAR) {
                    log.debug("Disable Carmode! Activate last Mode:" + lastMapState);
                    final MapPosition mapPosition = map.getMapPosition();
                    // restore last MapState
                    if (lastMapState.getMapMode() == MapMode.WP) {
                        final Coordinate wpCoord = CB.getSelectedCoord();
                        log.debug("set animation to WP coords");
                        mapPosition.setPosition(wpCoord.latitude, wpCoord.longitude);
                    }
                    mapStateButton.setMapMode(lastMapState.getMapMode(), true, selfEvent);
                    mapOrientationButton.setMode(lastMapState.getMapOrientationMode());
                    mapPosition.setTilt(map.viewport().getMinTilt());
                    float ori = 0;
                    if (lastMapState.getMapOrientationMode() != MapOrientationMode.NORTH) {
                        ori = Locator.getHeading();
                    }

                    mapPosition.setBearing(ori);
                    mapPosition.setZoomLevel(lastMapState.getZoom());

                    if (map.isBlocked()) {
                        map.waitForAnimationEnd(new Runnable() {
                            @Override
                            public void run() {
                                map.animateTo(mapPosition);
                            }
                        });
                    }else{
                        map.animateTo(mapPosition);
                    }
                    map.updateMap(true);
                } else if (mapMode == MapMode.GPS) {
                    log.debug("Activate GPS Mode");
                    positionChangedHandler.positionChanged(new Event());
                } else if (mapMode == MapMode.WP) {
                    log.debug("Activate WP Mode");
                    if (event == selfEvent) {
                        final MapPosition mapPosition = map.getMapPosition();
                        final Coordinate wpCoord = CB.getSelectedCoord();
                        mapPosition.setPosition(wpCoord.latitude, wpCoord.longitude);
                        float ori = 0;
                        if (!mapOrientationButton.isNorthOriented()) {
                            ori = Locator.getHeading();
                        }
                        mapPosition.setBearing(ori);

                        if (map.isBlocked()) {
                            map.waitForAnimationEnd(new Runnable() {
                                @Override
                                public void run() {
                                    map.animateTo(mapPosition);
                                }
                            });
                        }else{
                            map.animateTo(mapPosition);
                        }
                        map.updateMap(true);
                    } else {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                final MapPosition mapPosition = map.getMapPosition();
                                final Coordinate wpCoord = CB.getSelectedCoord();
                                mapPosition.setPosition(wpCoord.latitude, wpCoord.longitude);
                                float ori = 0;
                                if (!mapOrientationButton.isNorthOriented()) {
                                    ori = Locator.getHeading();
                                }
                                mapPosition.setBearing(ori);

                                if (map.isBlocked()) {
                                    map.waitForAnimationEnd(new Runnable() {
                                        @Override
                                        public void run() {
                                            map.animateTo(mapPosition);
                                        }
                                    });
                                }else{
                                    map.animateTo(mapPosition);
                                }
                                map.updateMap(true);
                            }
                        });
                    }
                } else {

                }
                if (event != selfEvent && mapMode != MapMode.CAR && lastMapMode != MapMode.CAR)
                    setMapState(lastMapState);
            }
        });
        this.mapOrientationButton = new MapCompass(mapStateButton.getWidth(), mapStateButton.getHeight());

        map = createMap();

        this.addActor(mapStateButton);
        this.addActor(mapOrientationButton);
        this.setTouchable(Touchable.enabled);

        this.zoomButton = new ZoomButton(new ZoomButton.ValueChangeListener() {
            @Override
            public void valueChanged(int changeValue) {

                MapPosition mapPosition = map.getMapPosition();
                double value = mapPosition.getScale();
                if (changeValue > 0)
                    value = value * 2;
                else
                    value = value * 0.5;

                mapPosition.setScale(value);
                map.animateTo(mapPosition);
                lastMapState.setZoom(FastMath.log2((int) value));
            }
        });
        this.zoomButton.pack();
        this.addActor(zoomButton);
    }


    private void checkInputListener() {
        MapMode mapMode = mapStateButton.getMapMode();
        // remove input handler with map mapMode Car and Lock
        if (mapMode == MapMode.CAR || mapMode == MapMode.LOCK) {
            removeInputListener();
        } else {
            addInputListener();
        }
    }

    public CacheboxMapAdapter createMap() {


        {//set map scale
            float scaleFactor = CB.getScaledFloat(Settings.MapViewDPIFaktor.getValue());
            Tile.SIZE = (int) (400 * scaleFactor);
            CanvasAdapter.dpi = 240 * scaleFactor;
            CanvasAdapter.textScale = scaleFactor;
            CanvasAdapter.scale = scaleFactor;
//            PolygonBucket.enableTexture = CanvasAdapter.platform != Platform.IOS;//fixme if vtm can render polygon texture

            log.debug("Create new map instance with scale factor:" + Float.toString(scaleFactor));
            log.debug("Tile.SIZE:" + Integer.toString(Tile.SIZE));
            log.debug("Canvas.dpi:" + Float.toString(CanvasAdapter.dpi));
        }


        main.drawMap = true;
        map = new CacheboxMapAdapter(mapOrientationButton) {
            @Override
            public void onMapEvent(Event e, final MapPosition mapPosition) {
                super.onMapEvent(e, mapPosition);
                if (e == Map.MOVE_EVENT) {
//                    log.debug("Map.MOVE_EVENT");
                    if (mapStateButton.getMapMode() != MapMode.FREE)
                        mapStateButton.setMapMode(MapMode.FREE, new Event());
                } else if (e == Map.TILT_EVENT) {
//                    log.debug("Map.TILT_EVENT");
                    if (positionChangedHandler != null)
                        positionChangedHandler.tiltChangedFromMap(mapPosition.getTilt());
                } else if (e == Map.ROTATE_EVENT) {
//                    log.debug("Map.ROTATE_EVENT");
                    if (positionChangedHandler != null)
                        positionChangedHandler.rotateChangedFromUser(mapPosition.getBearing());
                }
            }
        };
        main.mMapRenderer = new MapRenderer(map);
        main.mMapRenderer.onSurfaceCreated();

        this.lastMapState.setValues(Settings_Map.lastMapState.getValue());
        this.lastMapState.setValues(Settings_Map.lastMapState.getValue());

        double lastLatitude = Settings_Map.MapInitLatitude.getValue();
        double lastLongitude = Settings_Map.MapInitLongitude.getValue();

        map.setMapPosition(lastLatitude, lastLongitude, 1 << this.lastMapState.getZoom());

        //          grid,labels,buldings,scalebar
        initLayers(true, true, true, true);


        //add position changed handler
        positionChangedHandler = MapViewPositionChangedHandler.getInstance
                (map, myLocationLayer, myLocationAccuracy, mapOrientationButton, mapStateButton);

        return map;
    }

    public static LinkedHashMap<Object, TextureRegion> createTextureAtlasRegions() {
        // create TextureRegions from all Bitmap symbols
        LinkedHashMap<Object, TextureRegion> textureRegionMap = new LinkedHashMap<Object, TextureRegion>();
        ObjectMap<String, MapWayPointItemStyle> list = VisUI.getSkin().getAll(MapWayPointItemStyle.class);
        Array<Bitmap> bitmapList = new Array<Bitmap>();
        for (MapWayPointItemStyle style : list.values()) {
            if (style.small != null) if (!bitmapList.contains(style.small, true)) bitmapList.add(style.small);
            if (style.middle != null) if (!bitmapList.contains(style.middle, true)) bitmapList.add(style.middle);
            if (style.large != null) if (!bitmapList.contains(style.large, true)) bitmapList.add(style.large);
        }

        //add Bitmaps from MapArrowStyle
        MapArrowStyle mapArrowStyle = null;
        try {
            mapArrowStyle = VisUI.getSkin().get("myLocation", MapArrowStyle.class);
        } catch (Exception e) {
        }

        if (mapArrowStyle != null) {
            if (mapArrowStyle.myLocation != null) bitmapList.add(mapArrowStyle.myLocation);
            if (mapArrowStyle.myLocationTransparent != null) bitmapList.add(mapArrowStyle.myLocationTransparent);
            if (mapArrowStyle.myLocationCar != null) bitmapList.add(mapArrowStyle.myLocationCar);
        }


        LinkedHashMap<Object, Bitmap> input = new LinkedHashMap<Object, Bitmap>();
        for (Bitmap bmp : bitmapList) {
            input.put(((GetName) bmp).getName(), bmp);
        }
        ArrayList<TextureAtlas> atlasList = new ArrayList<TextureAtlas>();
        boolean flipped = CanvasAdapter.platform == Platform.IOS;
        System.out.print("create MapTextureAtlas with flipped Y? " + flipped);
        TextureAtlasUtils.createTextureRegions(input, textureRegionMap, atlasList, true,
                flipped);


        if (true) {//Debug write atlas Bitmap to tmp folder
            int count = 0;
            for (TextureAtlas atlas : atlasList) {
                byte[] data = atlas.texture.bitmap.getPngEncodedData();
                Pixmap pixmap = new Pixmap(data, 0, data.length);
                FileHandle file = Gdx.files.absolute(CB.WorkPath + "/user/temp/testAtlas" + count++ + ".png");
                PixmapIO.writePNG(file, pixmap);
                pixmap.dispose();
            }
        }
        return textureRegionMap;
    }

    @Override
    protected void create() {
        // override and don't call super
        // for non creation of default name label
    }

    @Override
    public void onShow() {
        addInputListener();
    }

    @Override
    public void onHide() {
        removeInputListener();
    }


    @Override
    public void dispose() {
        log.debug("Dispose MapView");

        //save last position for next initial
        MapPosition mapPosition = this.map.getMapPosition();
        Settings_Map.lastMapState.setValue(lastMapState.getValues());
        Settings_Map.MapInitLatitude.setValue(mapPosition.getLatitude());
        Settings_Map.MapInitLongitude.setValue(mapPosition.getLongitude());
        Config.AcceptChanges();

        positionChangedHandler.dispose();
        positionChangedHandler = null;

        Layers layers = map.layers();
        for (Layer layer : layers) {
            if (layer instanceof Disposable) {
                ((Disposable) layer).dispose();
            } else if (layer instanceof GroupLayer) {
                for (Layer groupLayer : ((GroupLayer) layer).layers) {
                    if (groupLayer instanceof Disposable) {
                        ((Disposable) groupLayer).dispose();
                    }
                }
            }
        }

        layers.clear();

        wayPointLayer = null;

        mapInputHandler.clear();
        mapInputHandler = null;

        main.drawMap = false;
        map.clearMap();
        map.destroy();
        TextureBucket.pool.clear();
        TextItem.pool.clear();
        TextureItem.disposeTextures();

        main.mMapRenderer = null;
        map = null;

        //dispose actors
        mapOrientationButton.dispose();
        mapStateButton.dispose();

    }

    private void setMapState(MapState state) {
        state.setMapMode(mapStateButton.getMapMode());
        state.setMapOrientationMode(mapOrientationButton.getMode());
        state.setZoom(this.map.getMapPosition().getZoomLevel());
    }

    @Override
    public void sizeChanged() {
        if (map == null) return;
        map.setMapPosAndSize((int) this.getX(), (int) this.getY(), (int) this.getWidth(), (int) this.getHeight());
        map.viewport().setScreenSize((int) this.getWidth(), (int) this.getHeight());
        main.setMapPosAndSize((int) this.getX(), (int) this.getY(), (int) this.getWidth(), (int) this.getHeight());

        // set position of MapScaleBar
        setMapScaleBarOffset(CB.scaledSizes.MARGIN, CB.scaledSizes.MARGIN_HALF);

        mapStateButton.setPosition(getWidth() - (mapStateButton.getWidth() + CB.scaledSizes.MARGIN),
                getHeight() - (mapStateButton.getHeight() + CB.scaledSizes.MARGIN));

        mapOrientationButton.setPosition(CB.scaledSizes.MARGIN,
                getHeight() - (mapOrientationButton.getHeight() + CB.scaledSizes.MARGIN));

        zoomButton.setPosition(getWidth() - (zoomButton.getWidth() + CB.scaledSizes.MARGIN), CB.scaledSizes.MARGIN);
    }


    @Override
    public void positionChanged() {
        main.setMapPosAndSize((int) this.getX(), (int) this.getY(), (int) this.getWidth(), (int) this.getHeight());
    }


    protected void initLayers(boolean tileGrid, boolean labels,
                              boolean buildings, boolean mapScalebar) {

        // load last saved BaseMap
        String baseMapName = Settings_Map.CurrentMapLayer.getValue()[0];
        BaseMapManager.INSTANCE.setMapFolder(Gdx.files.absolute(Settings_Map.MapPackFolder.getValue()));
        AbstractManagedMapLayer baseMap = null;
        for (int i = 0, n = BaseMapManager.INSTANCE.size(); i < n; i++) {
            AbstractManagedMapLayer map = BaseMapManager.INSTANCE.get(i);
            if (baseMapName.equals(map.name)) {
                baseMap = map;
                break;
            }
        }

        if (baseMap == null) {
            baseMap = new OSciMap();
        }

        setBaseMap(baseMap);

        //MyLocationLayer
        myLocationAccuracy = new LocationAccuracyLayer(map);
        myLocationAccuracy.setPosition(52.580400947530364, 13.385594096047232, 100);

        myLocationLayer = new LocationLayer(map, textureRegionMap);
        myLocationLayer.setPosition(52.580400947530364, 13.385594096047232, 0);


        GroupLayer layerGroup = new GroupLayer(map);


        if (tileGrid)
            layerGroup.layers.add(new TileGridLayer(map));

        if (mapScalebar) {
            DefaultMapScaleBar mapScaleBar = new DefaultMapScaleBar(map);
            mapScaleBar.setScaleBarMode(DefaultMapScaleBar.ScaleBarMode.BOTH);
            mapScaleBar.setDistanceUnitAdapter(MetricUnitAdapter.INSTANCE);
            mapScaleBar.setSecondaryDistanceUnitAdapter(ImperialUnitAdapter.INSTANCE);
            mapScaleBar.setScaleBarPosition(MapScaleBar.ScaleBarPosition.BOTTOM_LEFT);

            mapScaleBarLayer = new MapScaleBarLayer(map, mapScaleBar);
            layerGroup.layers.add(mapScaleBarLayer);
            layerGroup.layers.add(myLocationAccuracy);
        }


        wayPointLayer = new WaypointLayer(map, textureRegionMap);
        layerGroup.layers.add(wayPointLayer);
        layerGroup.layers.add(myLocationLayer);

        directLineLayer = new DirectLineLayer(map);
        layerGroup.layers.add(directLineLayer);

        map.layers().add(layerGroup);

    }

    public void setMapScaleBarOffset(float xOffset, float yOffset) {
        if (mapScaleBarLayer == null) return;
        BitmapRenderer renderer = mapScaleBarLayer.getRenderer();
        renderer.setPosition(GLViewport.Position.BOTTOM_LEFT);
        renderer.setOffset(xOffset, yOffset);
    }


    public void setInputListener(boolean on) {
        if (on) {
            checkInputListener();
        } else {
            removeInputListener();
        }
    }


    private void createMapInputHandler() {
        GestureDetector gestureDetector = new GestureDetector(new GestureHandlerImpl(map));
        MotionHandler motionHandler = new MotionHandler(map);
        MapInputHandler inputHandler = new MapInputHandler(map);
        mapInputHandler = new InputMultiplexer();
        mapInputHandler.addProcessor(gestureDetector);
        mapInputHandler.addProcessor(motionHandler);
        mapInputHandler.addProcessor(inputHandler);
    }

    private void addInputListener() {
        if (mapInputHandler == null) createMapInputHandler();
        StageManager.addMapMultiplexer(mapInputHandler);
    }

    private void removeInputListener() {
        StageManager.removeMapMultiplexer(mapInputHandler);
    }

    public boolean getAlignToCompass() {
        return mapOrientationButton.isNorthOriented();
    }

    public void setAlignToCompass(boolean align) {
        mapOrientationButton.setMode(align ? MapOrientationMode.NORTH : MapOrientationMode.COMPASS);
    }

    public void setNewSettings() {
        //TODO
    }

    public void setBaseMap(AbstractManagedMapLayer baseMap) {
        this.map.setNewBaseMap(baseMap);
    }
}
