/*
 * Copyright (C) 2016 - 2017 team-cachebox.de
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
package de.longri.cachebox3.gui.stages.initial_tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.ScaledSvg;
import com.badlogic.gdx.scenes.scene2d.ui.StoreSvg;
import com.badlogic.gdx.scenes.scene2d.ui.SvgSkin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.VisUI;
import de.longri.cachebox3.CB;
import de.longri.cachebox3.PlatformConnector;
import de.longri.cachebox3.gui.skin.styles.AttributesStyle;
import de.longri.cachebox3.settings.Settings;
import de.longri.cachebox3.types.Attributes;
import de.longri.cachebox3.utils.DevicesSizes;
import de.longri.cachebox3.utils.SizeF;
import org.oscim.backend.canvas.Bitmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Longri on 02.08.16.
 */
public final class SkinLoaderTask extends AbstractInitTask {
    private final Logger log = LoggerFactory.getLogger(SkinLoaderTask.class);
    private static final String INTERNAL_SKIN_DEFAULT_NAME = "internalDefault";

    public SkinLoaderTask(String name, int percent) {
        super(name, percent);
    }

    @Override
    public void runnable(final WorkCallback callback) {

        //initial sizes
        DevicesSizes ui = new DevicesSizes();
        ui.Window = new SizeF(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ui.Density = CB.getScalefactor();
        ui.isLandscape = false;


        String skinName = null;
        SvgSkin.StorageType storageType = null;
        FileHandle skinFileHandle = null;


        //Get selected skin name and check if available
        if (Settings.nightMode.getValue()) {
            if (!Settings.nightSkinName.isDefault()) {
                // check if skin exist into skin folder
                FileHandle skinFolder = Gdx.files.absolute(Settings.SkinFolder.getValue());
                if (skinFolder.exists()) {
                    FileHandle skin = skinFolder.child(Settings.nightSkinName.getValue());
                    if (skin.exists()) {
                        skinName = Settings.nightSkinName.getValue();
                        storageType = SvgSkin.StorageType.LOCAL;
                        skinFileHandle = skin;
                    }
                }
            }

            if (skinName == null) {
                // use default internal night skin
                skinName = Settings.nightSkinName.getDefaultValue();
                storageType = SvgSkin.StorageType.INTERNAL;
                skinFileHandle = Gdx.files.internal("skins/night");
            }
        } else {
            if (!Settings.daySkinName.isDefault()) {
                // check if skin exist into skin folder
                FileHandle skinFolder = Gdx.files.absolute(Settings.SkinFolder.getValue());
                if (skinFolder.exists()) {
                    FileHandle skin = skinFolder.child(Settings.daySkinName.getValue());
                    if (skin.exists()) {
                        skinName = Settings.daySkinName.getValue();
                        storageType = SvgSkin.StorageType.LOCAL;
                        skinFileHandle = skin;
                    }
                }
            }

            if (skinName == null) {
                // use default internal day skin
                skinName = Settings.daySkinName.getDefaultValue();
                storageType = SvgSkin.StorageType.INTERNAL;
                skinFileHandle = Gdx.files.internal("skins/day");
            }
        }

        final AtomicBoolean wait = new AtomicBoolean(true);

        final String finalSkinName = skinName;
        final SvgSkin.StorageType finalType = storageType;
        final FileHandle finalSkinFileHandle = skinFileHandle;
        callback.taskNameChange("load Skin");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CB.setActSkin(new SvgSkin(callback, false, finalSkinName, finalType, finalSkinFileHandle));
                CB.backgroundColor = CB.getColor("background");
                wait.set(false);
            }
        });
        thread.start();

        while (wait.get()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
        }


        //after skin loading store attribute icons for HTML description view
        //copy attributes*.png to data folder
        //this png files are used on description view, as Html image

        callback.taskNameChange("generate attributes images");

        //TODO store temp on generated skin for skin changes
        FileHandle attFileHandle = Gdx.files.absolute(CB.WorkPath + "/data/Attributes");
        attFileHandle.mkdirs();


        SvgSkin skin = (SvgSkin) VisUI.getSkin();
        AttributesStyle style = VisUI.getSkin().get("CompassView", AttributesStyle.class);

        Attributes[] values = Attributes.values();
        for (int i = 0, n = values.length; i < n; i++) {
            Attributes value = values[i];
            value.setNegative();
            storeAttributePng(callback, skin, style, attFileHandle, value);
            value.setPositive();
            storeAttributePng(callback, skin, style, attFileHandle, value);
        }

    }


    private void storeAttributePng(final WorkCallback callback, SvgSkin skin, AttributesStyle style, FileHandle attFileHandle, Attributes value) {

        String imageName = value.getImageName() + ".png";
        FileHandle storeFile = attFileHandle.child(imageName);
        if (storeFile.exists()) return;
        callback.taskNameChange("generate attribute image: " + imageName);
        TextureRegionDrawable drawable = (TextureRegionDrawable) value.getDrawable(style);
        if (drawable == null) return;
        ScaledSvg scaledSvg = skin.get(drawable.getName(), ScaledSvg.class);
        Bitmap bitmap = null;
        try {
            FileHandle svgFile = skin.skinFolder.child(scaledSvg.path);
            bitmap = PlatformConnector.getSvg(scaledSvg.getRegisterName(), svgFile.read(), PlatformConnector.SvgScaleType.DPI_SCALED, scaledSvg.scale);
        } catch (IOException e) {
            log.error("", e);
        }
        StoreSvg storeSvg = (StoreSvg) bitmap;
        storeSvg.store(storeFile);
        log.debug("store {} png", bitmap);
    }

    private void loadInternaleDefaultSkin() {


    }

}
