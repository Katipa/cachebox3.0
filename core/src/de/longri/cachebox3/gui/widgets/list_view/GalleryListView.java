/*
 * Copyright (C) 2019 team-cachebox.de
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
package de.longri.cachebox3.gui.widgets.list_view;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import de.longri.cachebox3.CB;
import de.longri.cachebox3.utils.NamedRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.longri.cachebox3.gui.widgets.list_view.ListViewItemLinkedList.search;
import static de.longri.cachebox3.gui.widgets.list_view.ListViewType.VERTICAL;

/**
 * Created by Longri on 23.04.2019.
 */
public class GalleryListView extends ListView {

    private static Logger log = LoggerFactory.getLogger(GalleryListView.class);

    private boolean isMoving = false;

    public GalleryListView() {
        super(ListViewType.HORIZONTAL);
        this.setDebug(true, true);

        scrollPane.addCaptureListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                log.debug("TouchDown");
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                log.debug("TouchUp");
            }
        });
    }


    public void act(float delta) {
        super.act(delta);
        if (scrollPane.isPanning() || scrollPane.isDragging() || scrollPane.isFlinging()) {
            isMoving = true;
        } else {
            if (isMoving) {
                isMoving = false;
                CB.postAsync(new NamedRunnable("GalleryListViewSnapIn") {
                    @Override
                    public void run() {
                        log.debug("SnapIn");
                        snapIn();
                    }
                });
            }
        }
    }


    private void snapIn() {
        // get first visible item and scroll to Center
        ListViewItemInterface firstVisibleItem = getfirstVisibleItem();
        float scrollPos = 0;
        if (firstVisibleItem != null) {
            int index = firstVisibleItem.getListIndex() - 1;
            scrollPos = index < 0 ? 0 : firstVisibleItem.getX();
        }
        this.setScrollPos(scrollPos);
        CB.requestRendering();
        if (firstVisibleItem != null)
            log.debug("Scroll to selected item {} at position {}", firstVisibleItem.getListIndex(), scrollPos);
    }

    private ListViewItemInterface getfirstVisibleItem() {

        float size = getWidth();

        float searchPos = getScrollPos() + (size / 2);


        ListViewItemInterface[] itemArray = this.itemList.itemArray;

        ListViewItemInterface firstItem = search(this.type, itemArray, searchPos, size);
        ListViewItemInterface lastItem = search(this.type, itemArray, searchPos + size, size);

        log.debug("RETURN Item: {}", firstItem);

        return firstItem;
    }


    //    @Override
//    public void setWidth(float newWidth) {
//        super.setWidth(newWidth);
//    }
//
//    @Override
//    public void setHeight(float newHeight) {
//        super.setHeight(newHeight);
//    }
//
//    public void setSize(float width, float height) {
//        super.setSize(width, height);
//    }
//
//    /**
//     * Adds the specified size to the current size.
//     */
//    public void sizeBy(float size) {
//        super.sizeBy(size);
//    }
//
//    /**
//     * Adds the specified size to the current size.
//     */
//    public void sizeBy(float width, float height) {
//        super.sizeBy(width, height);
//    }
//
//    /**
//     * Set bounds the x, y, width, and height.
//     */
//    public void setBounds(float x, float y, float width, float height) {
//        super.setBounds(x, y, width, height);
//    }
//
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        super.draw(batch, parentAlpha);
//    }
//
//    @Override
//    public float getPrefWidth(){
//        return 100;
//    }
//
//
    @Override
    public float getPrefHeight() {
        return CB.getScaledFloat(75);
    }
}
