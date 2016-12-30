/*
 * Copyright 2012 osmdroid authors: Nicolas Gramlich, Theodore Hong, Fred Eisele
 * 
 * Copyright 2013 Hannes Janetzek
 * Copyright 2016 devemux86
 * Copyright 2016 Stephan Leuschner 
 * Copyright 2016 Pedinel
 *
 * This file is part of the OpenScienceMap project (http://www.opensciencemap.org).
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.longri.cachebox3.gui.map.layer.cluster;

import de.longri.cachebox3.locator.Coordinate;
import de.longri.cachebox3.locator.geocluster.GeoCluster;
import de.longri.cachebox3.utils.lists.CB_List;
import org.oscim.core.Box;
import org.oscim.core.GeoPoint;
import org.oscim.core.Point;
import org.oscim.event.Gesture;
import org.oscim.event.GestureListener;
import org.oscim.event.MotionEvent;
import org.oscim.map.Map;
import org.oscim.map.Viewport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemizedClusterLayer<Item extends GeoCluster> extends ClusterLayer<Item>
        implements GestureListener {

    static final Logger log = LoggerFactory.getLogger(ItemizedClusterLayer.class);

    protected final CB_List<Item> mItemList;
    protected final Point mTmpPoint = new Point();
    protected OnItemGestureListener<Item> mOnItemGestureListener;
    protected int mDrawnItemsLimit = Integer.MAX_VALUE;

    public ItemizedClusterLayer(Map map, ClusterSymbol defaultCluster) {
        this(map, new CB_List<Item>(), defaultCluster, null);
    }

    public ItemizedClusterLayer(Map map, CB_List<Item> list,
                                ClusterSymbol defaultCluster,
                                OnItemGestureListener<Item> listener) {

        super(map, defaultCluster);

        mItemList = list;
        mOnItemGestureListener = listener;
        populate();
    }

    public ItemizedClusterLayer(Map map, ClusterRendererFactory ClusterRendererFactory) {
        this(map, new CB_List<Item>(), ClusterRendererFactory, null);
    }

    public ItemizedClusterLayer(Map map, CB_List<Item> list,
                                ClusterRendererFactory ClusterRendererFactory,
                                OnItemGestureListener<Item> listener) {

        super(map, ClusterRendererFactory);

        mItemList = list;
        mOnItemGestureListener = listener;
        populate();
    }

    public void setOnItemGestureListener(OnItemGestureListener<Item> listener) {
        mOnItemGestureListener = listener;
    }

    @Override
    protected Item createItem(int index) {
        return mItemList.get(index);
    }

    @Override
    public int size() {
        return Math.min(mItemList.size(), mDrawnItemsLimit);
    }

    public boolean addItem(Item item) {
        final int result = mItemList.add(item);
        populate();
        return result>=0;
    }

    public void addItem(int location, Item item) {
        mItemList.add(location, item);
    }

    public boolean addItems(CB_List<Item> items) {
         mItemList.addAll(items);
        populate();
        return true;
    }

    public CB_List<Item> getItemList() {
        return mItemList;
    }

    public void removeAllItems() {
        removeAllItems(true);
    }

    public void removeAllItems(boolean withPopulate) {
        mItemList.clear();
        if (withPopulate) {
            populate();
        }
    }

    public boolean removeItem(Item item) {
        final Item result = mItemList.remove(item);
        populate();
        return result!=null;
    }

    public Item removeItem(int position) {
        final Item result = mItemList.remove(position);
        populate();
        return result;
    }

    /**
     * Each of these methods performs a item sensitive check. If the item is
     * located its corresponding method is called. The result of the call is
     * returned. Helper methods are provided so that child classes may more
     * easily override behavior without resorting to overriding the
     * ItemGestureListener methods.
     */
    //    @Override
    //    public boolean onTap(MotionEvent event, MapPosition pos) {
    //        return activateSelectedItems(event, mActiveItemSingleTap);
    //    }
    protected boolean onSingleTapUpHelper(int index, Item item) {
        return mOnItemGestureListener.onItemSingleTapUp(index, item);
    }

    private final ActiveItem mActiveItemSingleTap = new ActiveItem() {
        @Override
        public boolean run(int index) {
            final ItemizedClusterLayer<Item> that = ItemizedClusterLayer.this;
            if (mOnItemGestureListener == null) {
                return false;
            }
            return onSingleTapUpHelper(index, that.mItemList.get(index));
        }
    };

    protected boolean onLongPressHelper(int index, Item item) {
        return this.mOnItemGestureListener.onItemLongPress(index, item);
    }

    private final ActiveItem mActiveItemLongPress = new ActiveItem() {
        @Override
        public boolean run(final int index) {
            final ItemizedClusterLayer<Item> that = ItemizedClusterLayer.this;
            if (that.mOnItemGestureListener == null) {
                return false;
            }
            return onLongPressHelper(index, that.mItemList.get(index));
        }
    };

    /**
     * When a content sensitive action is performed the content item needs to be
     * identified. This method does that and then performs the assigned task on
     * that item.
     *
     * @return true if event is handled false otherwise
     */
    protected boolean activateSelectedItems(MotionEvent event, ActiveItem task) {
        int size = mItemList.size();
        if (size == 0)
            return false;

        int eventX = (int) event.getX() - mMap.getWidth() / 2;
        int eventY = (int) event.getY() - mMap.getHeight() / 2;
        Viewport mapPosition = mMap.viewport();

        Box box = mapPosition.getBBox(null, 128);
        box.map2mercator();
        box.scale(1E6);

        int nearest = -1;
        int inside = -1;
        double insideY = -Double.MAX_VALUE;

        /* squared dist: 50*50 pixel ~ 2mm on 400dpi */
        double dist = 2500;

        for (int i = 0; i < size; i++) {
            Item item = mItemList.get(i);

            if (!box.contains((int) (item.longitude * 1000000.0D),
                    (int) (item.latitude * 1000000.0D)))
                continue;

            mapPosition.toScreenPoint(new GeoPoint(item.latitude, item.longitude), mTmpPoint);

            float dx = (float) (mTmpPoint.x - eventX);
            float dy = (float) (mTmpPoint.y - eventY);

            ClusterSymbol it = item.clustersymbol;
            if (it == null)
                it = mClusterRenderer.mDefaultCluster;

            if (it.isInside(dx, dy)) {
                if (mTmpPoint.y > insideY) {
                    insideY = mTmpPoint.y;
                    inside = i;
                }
            }
            if (inside >= 0)
                continue;

            double d = dx * dx + dy * dy;
            if (d > dist)
                continue;

            dist = d;
            nearest = i;
        }

        if (inside >= 0)
            nearest = inside;

        if (nearest >= 0 && task.run(nearest)) {
            mClusterRenderer.update();
            mMap.render();
            return true;
        }
        return false;
    }

    /**
     * When the item is touched one of these methods may be invoked depending on
     * the type of touch. Each of them returns true if the event was completely
     * handled.
     */
    public static interface OnItemGestureListener<T> {
        public boolean onItemSingleTapUp(int index, T item);

        public boolean onItemLongPress(int index, T item);
    }

    public static interface ActiveItem {
        public boolean run(int aIndex);
    }

    @Override
    public boolean onGesture(Gesture g, MotionEvent e) {
        if (g instanceof Gesture.Tap)
            return activateSelectedItems(e, mActiveItemSingleTap);

        if (g instanceof Gesture.LongPress)
            return activateSelectedItems(e, mActiveItemLongPress);

        return false;
    }
}
