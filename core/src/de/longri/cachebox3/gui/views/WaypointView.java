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
package de.longri.cachebox3.gui.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import de.longri.cachebox3.CB;
import de.longri.cachebox3.callbacks.GenericCallBack;
import de.longri.cachebox3.events.EventHandler;
import de.longri.cachebox3.events.SelectedCacheChangedEvent;
import de.longri.cachebox3.events.SelectedWayPointChangedEvent;
import de.longri.cachebox3.gui.Window;
import de.longri.cachebox3.gui.activities.EditWaypoint;
import de.longri.cachebox3.gui.activities.ProjectionCoordinate;
import de.longri.cachebox3.gui.dialogs.ButtonDialog;
import de.longri.cachebox3.gui.dialogs.MessageBoxButtons;
import de.longri.cachebox3.gui.dialogs.MessageBoxIcon;
import de.longri.cachebox3.gui.dialogs.OnMsgBoxClickListener;
import de.longri.cachebox3.gui.menu.Menu;
import de.longri.cachebox3.gui.menu.MenuID;
import de.longri.cachebox3.gui.menu.MenuItem;
import de.longri.cachebox3.gui.menu.OnItemClickListener;
import de.longri.cachebox3.gui.utils.ClickLongClickListener;
import de.longri.cachebox3.gui.views.listview.Adapter;
import de.longri.cachebox3.gui.views.listview.ListView;
import de.longri.cachebox3.gui.views.listview.ListViewItem;
import de.longri.cachebox3.locator.Coordinate;
import de.longri.cachebox3.sqlite.Database;
import de.longri.cachebox3.sqlite.dao.DaoFactory;
import de.longri.cachebox3.translation.Translation;
import de.longri.cachebox3.types.AbstractCache;
import de.longri.cachebox3.types.AbstractWaypoint;
import de.longri.cachebox3.types.CacheTypes;
import de.longri.cachebox3.types.MutableWaypoint;
import de.longri.cachebox3.utils.MathUtils;
import de.longri.cachebox3.utils.UnitFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Longri on 14.09.2016.
 */
public class WaypointView extends AbstractView {

    private static final Logger log = LoggerFactory.getLogger(WaypointView.class);
    private AbstractCache actAbstractCache;
    private AbstractWaypoint actWaypoint;
    private ListView listView;

    public WaypointView() {
        super("WaypointView");
        actAbstractCache = EventHandler.getSelectedCache();
    }

    @Override
    public void onShow() {
        Gdx.graphics.requestRendering();
        log.debug("onShow");
    }

    @Override
    public void layout() {
        log.debug("Layout");
        super.layout();
        if (listView == null) addNewListView();
        log.debug("Finish Layout");
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Gdx.graphics.requestRendering();
            }
        });
    }

    /**
     * Called when the actor's size has been changed.
     */
    protected void sizeChanged() {
        if (listView != null) {
            listView.setSize(this.getWidth(), this.getHeight());
        }
    }

    @Override
    public boolean removeListener(EventListener listener) {
        return super.removeListener(listener);
    }

    private void addNewListView() {

        log.debug("Start Thread add new listView");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                WaypointView.this.clear();
                Adapter listViewAdapter = new Adapter() {
                    @Override
                    public int getCount() {
                        if (actAbstractCache == null) return 0;
                        return actAbstractCache.getWaypoints().size + 1;
                    }

                    @Override
                    public ListViewItem getView(int index) {
                        if (index == 0) {
                            return CacheListItem.getListItem(index, actAbstractCache);
                        } else {
                            final WayPointListItem item = WayPointListItem.getListItem(index, actAbstractCache.getWaypoints().get(index - 1));
                            return item;
                        }
                    }

                    @Override
                    public void update(final ListViewItem view) {
                        // set listener on Update, because Item is remove all listener with Layout
                        view.addListener(clickLongClickListener);


                        //get index from item
                        int idx = view.getListIndex();

                        Coordinate myPosition = EventHandler.getMyPosition();
                        if (myPosition == null)
                            return; // can't update without an position

                        float heading = de.longri.cachebox3.events.EventHandler.getHeading();

                        // get coordinate from Cache or from Waypoint
                        Coordinate targetCoord = idx == 0 ? actAbstractCache : actAbstractCache.getWaypoints().get(idx - 1);

                        //calculate distance and bearing
                        float result[] = new float[4];
                        MathUtils.computeDistanceAndBearing(MathUtils.CalculationType.FAST,
                                myPosition.getLatitude(), myPosition.getLongitude(),
                                targetCoord.getLatitude(), targetCoord.getLongitude(), result);


                        //update item
                        boolean changed;
                        if (idx == 0) {
                            changed = ((CacheListItem) view).update(-(result[2] - heading), UnitFormatter.distanceString(result[0], true));
                        } else {
                            changed = ((WayPointListItem) view).update(-(result[2] - heading), UnitFormatter.distanceString(result[0], true));
                        }
                        if (changed) {
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    Gdx.graphics.requestRendering();
                                }
                            });
                        }
                    }

                    @Override
                    public float getItemSize(int position) {
                        return 0;
                    }
                };

                if (WaypointView.this.listView != null) {
                    disposeListView();
                }

                WaypointView.this.listView = new ListView(listViewAdapter, false, true);
                synchronized (WaypointView.this.listView) {
                    listView.setBounds(0, 0, WaypointView.this.getWidth(), WaypointView.this.getHeight());
                    addActor(listView);
                    listView.setCullingArea(new Rectangle(0, 0, WaypointView.this.getWidth(), WaypointView.this.getHeight()));
                    listView.setSelectable(ListView.SelectableType.SINGLE);
                    CB.requestRendering();
                }

                // add selection changed event listener
                listView.addSelectionChangedEventListner(new ListView.SelectionChangedEvent() {
                    @Override
                    public void selectionChanged() {

                        if (listView.getSelectedItem() instanceof WayPointListItem) {
                            WayPointListItem selectedItem = (WayPointListItem) listView.getSelectedItem();
                            int index = selectedItem.getListIndex() - 1;
                            AbstractWaypoint wp = actAbstractCache.getWaypoints().get(index);

                            log.debug("Waypoint selection changed to: " + wp.toString());
                            //set selected Waypoint global
                            EventHandler.fire(new SelectedWayPointChangedEvent(wp));
                            actWaypoint = wp;

                        } else {
                            CacheListItem selectedItem = (CacheListItem) listView.getSelectedItem();
                            int selectedItemListIndex = selectedItem.getListIndex();

                            AbstractCache cache = Database.Data.Query.get(selectedItemListIndex);
                            log.debug("Cache selection changed to: " + cache.toString());
                            //set selected Cache global
                            EventHandler.fire(new SelectedCacheChangedEvent(cache));
                            actWaypoint = null;
                        }
                    }
                });

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        AbstractWaypoint wp = EventHandler.getSelectedWaypoint();
                        if (wp == null) {
                            //select Cache
                            listView.setSelection(0);
                            listView.setSelectedItemVisible(false);
                        } else {
                            int index = 0;
                            for (ListViewItem item : listView.items()) {
                                if (index == 0) {
                                    index++;
                                    continue;
                                }
                                WayPointListItem wayPointListItem = (WayPointListItem) item;
                                if (wayPointListItem.getWaypointGcCode().equals(wp.getGcCode())) {
                                    listView.setSelection(index);
                                    listView.setSelectedItemVisible(false);
                                    break;
                                }
                                index++;
                            }
                        }
                    }
                });
                log.debug("Finish Thread add new listView");
                CB.requestRendering();
            }
        });
        thread.start();
        CB.requestRendering();
    }

    private final ClickLongClickListener clickLongClickListener = new ClickLongClickListener() {
        @Override
        public boolean clicked(InputEvent event, float x, float y) {
            return false;
        }

        @Override
        public boolean longClicked(Actor actor, float x, float y) {

            int listIndex = ((ListViewItem) actor).getListIndex();

            if (listIndex > 0) {
                actWaypoint = actAbstractCache.getWaypoints().get(listIndex - 1);
                if (WaypointView.this.listView != null)
                    WaypointView.this.listView.setSelection(listIndex);
            }
            final Menu contextMenu = getContextMenu();
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    contextMenu.show();
                }
            });
            return true;
        }
    };

    private void disposeListView() {
        final ListView disposeListView = this.listView;
        Thread disposeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                disposeListView.dispose();
            }
        });
        disposeThread.start();
    }

    @Override
    public void dispose() {
        this.actAbstractCache = null;
        this.actWaypoint = null;
        disposeListView();
        this.listView = null;
    }

    private void addProjection() {
        ProjectionCoordinate projActivity = new ProjectionCoordinate(actWaypoint == null ? actAbstractCache : actWaypoint) {
            @Override
            public void callBack(Coordinate newCoord) {
                addWp(newCoord, false);
            }
        };
        projActivity.show();
    }

    private void addMeasure() {

    }

    private void deleteWP() {
        //name, msg, title, buttons, icon, OnMsgBoxClickListener
        Window dialog = new ButtonDialog("delete Waypoint",
                Translation.get("?DelWP") + "\n[" + actWaypoint.getTitle() + "]\n",
                Translation.get("!DelWP"), MessageBoxButtons.YesNo, MessageBoxIcon.Question,
                new OnMsgBoxClickListener() {
                    @Override
                    public boolean onClick(int which, Object data) {
                        if (which == ButtonDialog.BUTTON_POSITIVE) {
                            log.debug("Delete Waypoint");
                            // Yes button clicked
                            DaoFactory.WAYPOINT_DAO.delete(Database.Data, actWaypoint);
                            actAbstractCache.getWaypoints().removeValue(actWaypoint, false);
                            listView.setSelection(0);// select Cache
                        }
                        return true;
                    }
                });
        dialog.show();
    }

    private void editWP(boolean onlyShow) {
        showEditWpDialog(actWaypoint, true, onlyShow);
    }

    public void addWp() {
        addWp(EventHandler.getSelectedCoord(), true);
    }

    private void addWp(Coordinate coordinate, boolean showCoords) {
        String newGcCode = "";
        try {
            newGcCode = Database.createFreeGcCode(EventHandler.getSelectedCache().getGcCode().toString());
        } catch (Exception e) {
            return;
        }
        if (coordinate == null)
            coordinate = EventHandler.getMyPosition();
        if ((coordinate == null) || (!coordinate.isValid()))
            coordinate = EventHandler.getSelectedCache();
        AbstractWaypoint newWP = new MutableWaypoint(newGcCode, CacheTypes.ReferencePoint
                , coordinate.getLatitude(), coordinate.getLongitude(), EventHandler.getSelectedCache().getId(), "", newGcCode);
        newWP.setUserWaypoint(true);
        showEditWpDialog(newWP, showCoords, false);
    }

    private void showEditWpDialog(final AbstractWaypoint newWP, final boolean showCoords, final boolean onlyShow) {
        CB.postOnMainThread(new Runnable() {
            @Override
            public void run() {
                AbstractWaypoint mutable = newWP.getMutable(Database.Data);
                EditWaypoint editWaypoint = new EditWaypoint(mutable, showCoords, onlyShow, new GenericCallBack<AbstractWaypoint>() {
                    @Override
                    public void callBack(AbstractWaypoint value) {
                        if (value != null) {
                            boolean update = false;
                            if (actAbstractCache.getWaypoints().contains(value, false)) {
                                int index = actAbstractCache.getWaypoints().indexOf(value, false);
                                actAbstractCache.getWaypoints().set(index, value);
                                update = true;
                            } else {
                                actAbstractCache.getWaypoints().add(value);
                            }

                            addNewListView();
                            EventHandler.fire(new SelectedWayPointChangedEvent(value));
                            if (value.isStart()) {
                                //It must be ensured here that this waypoint is the only one of these Cache,
                                //which is defined as starting point !!!
                                DaoFactory.WAYPOINT_DAO.resetStartWaypoint(EventHandler.getSelectedCache(), value);
                            }
                            if (update) {
                                DaoFactory.WAYPOINT_DAO.updateDatabase(Database.Data, value);
                            } else {
                                DaoFactory.WAYPOINT_DAO.writeToDatabase(Database.Data, value);
                            }
                            CB.requestRendering();
                        }
                    }
                });
                editWaypoint.show();
            }
        });
    }


    //################### Context menu implementation ####################################
    @Override
    public boolean hasContextMenu() {
        return true;
    }

    @Override
    public Menu getContextMenu() {
        Menu cm = new Menu("CacheListContextMenu");

        cm.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public boolean onItemClick(MenuItem item) {
                switch (item.getMenuItemId()) {
                    case MenuID.MI_ADD:
                        addWp();
                        return true;
                    case MenuID.MI_WP_SHOW:
                        editWP(false);
                        return true;
                    case MenuID.MI_EDIT:
                        editWP(true);
                        return true;
                    case MenuID.MI_DELETE:
                        deleteWP();
                        return true;
                    case MenuID.MI_PROJECTION:
                        addProjection();
                        return true;
                    case MenuID.MI_FROM_GPS:
                        addMeasure();
                        return true;

                }
                return false;
            }
        });

        if (actWaypoint != null)
            cm.addItem(MenuID.MI_WP_SHOW, "show",CB.getSkin().getMenuIcon.showWp);
        if (actWaypoint != null)
            cm.addItem(MenuID.MI_EDIT, "edit",CB.getSkin().getMenuIcon.editWp);
        cm.addItem(MenuID.MI_ADD, "AddWaypoint", CB.getSkin().getMenuIcon.addWp);
        if ((actWaypoint != null) && (actWaypoint.isUserWaypoint()))
            cm.addItem(MenuID.MI_DELETE, "delete",CB.getSkin().getMenuIcon.delWp);

        //ISSUE (#128 Add Waypoint projection)
        if (actWaypoint != null || actAbstractCache != null)
            cm.addItem(MenuID.MI_PROJECTION, "Projection",CB.getSkin().getMenuIcon.projectWp);

        //ISSUE (#129 add measure WP from GPS)
         cm.addItem(MenuID.MI_FROM_GPS, "FromGps",CB.getSkin().getMenuIcon.mesureWp);

        return cm;
    }
}
