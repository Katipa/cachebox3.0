/*
 * Copyright (C) 2016 team-cachebox.de
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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import de.longri.cachebox3.CB;
import de.longri.cachebox3.gui.views.listview.ListViewItem;
import de.longri.cachebox3.types.CacheTypes;

/**
 * Created by Longri on 05.09.2016.
 */
public class CacheListItem extends ListViewItem {

    private final CacheListItemStyle style;
    private final CacheTypes type;
    private final CharSequence cacheName;
    private boolean needsLayout = true;


    public CacheListItem(int listIndex, CacheTypes type, CharSequence cacheName) {
        super(listIndex);
        this.style = VisUI.getSkin().get("default", CacheListItemStyle.class);
        this.type = type;
        this.cacheName = cacheName;
    }


    public void layout() {
//        this.setDebug(true, true);
        if (!needsLayout) {
            super.layout();
            return;
        }

        this.clear();

        VisTable iconTable = new VisTable();
        iconTable.add(type.getCacheWidget());

        iconTable.pack();
        iconTable.layout();

        this.add(iconTable).left().padRight(CB.scaledSizes.MARGIN_HALF);


        Label.LabelStyle nameLabelStyle = new Label.LabelStyle();
        nameLabelStyle.font = this.style.nameFont;
        nameLabelStyle.fontColor = this.style.nameFontColor;
        VisLabel nameLabel = new VisLabel(cacheName, nameLabelStyle);
        nameLabel.setWrap(true);
        this.add(nameLabel).top().expandX().fillX();

        super.layout();
        needsLayout = false;
    }


    public static class CacheListItemStyle {
        BitmapFont nameFont;
        Color nameFontColor;
    }

}
