/*
 * Copyright (C) 2017 team-cachebox.de
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
package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import de.longri.cachebox3.gui.skin.styles.CompassViewStyle;
import de.longri.cachebox3.gui.skin.styles.LanguageStyle;
import de.longri.cachebox3.gui.skin.styles.LogListItemStyle;
import de.longri.cachebox3.gui.skin.styles.LogTypesStyle;
import de.longri.cachebox3.translation.Language;

/**
 * Created by Longri on 12.01.2017.
 */
public class SavableSvgSkin extends SvgSkin {

    public SavableSvgSkin(String name) {
        super(name);
    }

    public SavableSvgSkin(boolean forceCreateNewAtlas, String name, StorageType storageType, FileHandle skinFolder) {
        super(null,forceCreateNewAtlas, name, storageType, skinFolder);
    }


    /**
     * Store all resources in the specified skin JSON file.
     */
    public boolean save(FileHandle skinFile) {


        // Sort items
        Array<Class> items = new Array<Class>();


        //items for cachebox 3.0 skin
        items.add(com.badlogic.gdx.scenes.scene2d.ui.ScaledSvg.class);
        items.add(de.longri.cachebox3.utils.SkinColor.class);
        items.add(de.longri.cachebox3.gui.skin.styles.ColorDrawableStyle.class);
        items.add(com.badlogic.gdx.graphics.g2d.BitmapFont.class);
        items.add(com.badlogic.gdx.scenes.scene2d.ui.SvgNinePatchDrawable.class);
        items.add(de.longri.cachebox3.gui.skin.styles.FrameAnimationStyle.class);
        items.add(com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle.class);
        items.add(com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle.class);
        items.add(de.longri.cachebox3.gui.widgets.ButtonBar.ButtonBarStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.GestureButtonStyle.class);
        items.add(com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle.class);
        items.add(com.kotcrab.vis.ui.widget.VisTextButton.VisTextButtonStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.ButtonDialogStyle.class);
        items.add(com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle.class);
        items.add(de.longri.cachebox3.gui.views.listview.ListView.ListViewStyle.class);
        items.add(de.longri.cachebox3.gui.menu.Menu.MenuStyle.class);
        items.add(de.longri.cachebox3.gui.menu.MenuItem.MenuItemStyle.class);
        items.add(de.longri.cachebox3.gui.help.HelpWindow.HelpWindowStyle.class);
        items.add(de.longri.cachebox3.gui.help.GestureHelp.GestureHelpStyle.class);
        items.add(de.longri.cachebox3.gui.ActivityBase.ActivityBaseStyle.class);
        items.add(de.longri.cachebox3.gui.activities.Settings_Activity.SettingsActivityStyle.class);
        items.add(com.kotcrab.vis.ui.widget.VisTextField.VisTextFieldStyle.class);
        items.add(de.longri.cachebox3.gui.activities.SelectDB_Activity.SelectDbStyle.class);
        items.add(com.kotcrab.vis.ui.widget.VisCheckBox.VisCheckBoxStyle.class);
        items.add(de.longri.cachebox3.gui.widgets.Slider.SliderStyle.class);
        items.add(de.longri.cachebox3.gui.widgets.QuickButtonList.QuickButtonListStyle.class);
        items.add(de.longri.cachebox3.gui.widgets.MapStateButton.MapStateButtonStyle.class);
        items.add(de.longri.cachebox3.gui.widgets.ZoomButton.ZoomButtonStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.MapWayPointItemStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.ScaledSize.class);
        items.add(de.longri.cachebox3.gui.skin.styles.IconsStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.MenuIconStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.MapArrowStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.FileChooserStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.MapCenterCrossStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.CompassStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.MapInfoPanelStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.DirectLineRendererStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.ApiButtonStyle.class);
        items.add(ProgressBar.ProgressBarStyle.class);
        items.add(com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.SelectBoxStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.EditTextStyle.class);
        items.add(SplitPane.SplitPaneStyle.class);
        items.add(CompassViewStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.AttributesStyle.class);
        items.add(LanguageStyle.class);
        items.add(LogTypesStyle.class);
        items.add(LogListItemStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.CacheTypeStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.CacheListItemStyle.class);
        items.add(de.longri.cachebox3.gui.skin.styles.WayPointListItemStyle.class);


        SvgSkinUtil.saveSkin(this, items, skinFile);

        return true;
    }

    public SavableSvgSkin clone(String newName) {
        SavableSvgSkin newSkin = new SavableSvgSkin(newName);
        newSkin.getIcon = getIcon;
        newSkin.getMenuIcon = getMenuIcon;
        newSkin.resources = resources;
        newSkin.atlas = atlas;

        return newSkin;
    }
}
