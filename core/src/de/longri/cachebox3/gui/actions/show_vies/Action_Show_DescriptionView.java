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
package de.longri.cachebox3.gui.actions.show_vies;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import de.longri.cachebox3.CB;
import de.longri.cachebox3.gui.dialogs.ButtonDialog;
import de.longri.cachebox3.gui.dialogs.MessageBoxButtons;
import de.longri.cachebox3.gui.dialogs.MessageBoxIcon;
import de.longri.cachebox3.gui.events.CacheListChangedEventList;
import de.longri.cachebox3.gui.menu.Menu;
import de.longri.cachebox3.gui.menu.MenuID;
import de.longri.cachebox3.gui.menu.MenuItem;
import de.longri.cachebox3.gui.menu.OnItemClickListener;
import de.longri.cachebox3.gui.views.AbstractView;
import de.longri.cachebox3.gui.views.DescriptionView;
import de.longri.cachebox3.locator.events.newT.EventHandler;
import de.longri.cachebox3.sqlite.Database;
import de.longri.cachebox3.sqlite.dao.CacheDAO;
import de.longri.cachebox3.translation.Translation;

/**
 * Created by Longri on 14.09.2016.
 */
public class Action_Show_DescriptionView extends Abstract_Action_ShowView {
    public Action_Show_DescriptionView() {
        super("Description", MenuID.AID_SHOW_DESCRIPTION);
    }

    @Override
    public boolean hasContextMenu() {
        return true;
    }

    @Override
    public Menu getContextMenu() {
        Menu cm = new Menu("DescriptionViewContextMenu");

        cm.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public boolean onItemClick(MenuItem item) {
                switch (item.getMenuItemId()) {
                    case MenuID.MI_FAVORIT:
                        if (EventHandler.getSelectedCache() == null) {

                            new ButtonDialog("NoCacheSelect", Translation.Get("NoCacheSelect"), Translation.Get("Error"),
                                    MessageBoxButtons.OKCancel, MessageBoxIcon.Error, null).show();
                            return true;
                        }

                        EventHandler.getSelectedCache().setFavorite(!EventHandler.getSelectedCache().isFavorite());
                        CacheDAO dao = new CacheDAO();
                        dao.UpdateDatabase(EventHandler.getSelectedCache());

                        // Update Query
                        Database.Data.Query.GetCacheById(EventHandler.getSelectedCache().Id).setFavorite(EventHandler.getSelectedCache().isFavorite());

                        // Update View
                        //TODO update
//                        if (TabMainView.descriptionView != null)
//                            TabMainView.descriptionView.onShow();

                        CacheListChangedEventList.Call();
                        return true;
                    case MenuID.MI_RELOAD_CACHE:
                        //TODO ReloadSelectedCache();
                        return true;
                }
                return false;
            }
        });

        MenuItem mi;

        boolean isSelected = (EventHandler.getSelectedCache()!=null);

        mi = cm.addItem(MenuID.MI_FAVORIT, "Favorite", CB.getSkin().getMenuIcon.favorit);
        mi.setCheckable(true);
        if (isSelected) {
            mi.setChecked(EventHandler.getSelectedCache().isFavorite());
        } else {
            mi.setEnabled(false);
        }

        boolean selectedCacheIsNoGC = false;

        if (isSelected)
            selectedCacheIsNoGC = !EventHandler.getSelectedCache().getGcCode().startsWith("GC");
        mi = cm.addItem(MenuID.MI_RELOAD_CACHE, "ReloadCacheAPI", CB.getSkin().getIcon.GC_Live);
        if (!isSelected)
            mi.setEnabled(false);
        if (selectedCacheIsNoGC)
            mi.setEnabled(false);

        return cm;
    }

    @Override
    public boolean isActVisible() {
        return CB.viewmanager.getActView() instanceof DescriptionView;
    }

    @Override
    public boolean viewTypeEquals(AbstractView actView) {
        return actView.getClass().getName().equals(DescriptionView.class.getName());
    }

    @Override
    public void execute() {
        if (isActVisible()) return;
        DescriptionView view = new DescriptionView();
        CB.viewmanager.showView(view);
    }

    @Override
    public Drawable getIcon() {
        return CB.getSkin().getMenuIcon.docIcon;
    }
}
