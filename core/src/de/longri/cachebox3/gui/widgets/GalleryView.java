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
package de.longri.cachebox3.gui.widgets;

import de.longri.cachebox3.Utils;
import de.longri.cachebox3.gui.widgets.catch_exception_widgets.Catch_Table;
import de.longri.cachebox3.gui.widgets.list_view.DefaultListViewAdapter;
import de.longri.cachebox3.gui.widgets.list_view.GalleryItem;
import de.longri.cachebox3.gui.widgets.list_view.GalleryListView;
import de.longri.cachebox3.types.ImageEntry;
import de.longri.cachebox3.utils.ImageLoader;

/**
 * Created by Longri on 23.04.2019.
 */
public class GalleryView extends Catch_Table {

    private final static int MAX_THUMB_WIDTH = 500;
    private final static int MAX_OVERVIEW_THUMB_WIDTH = 240;

    private final GalleryListView overview;
    private final GalleryListView gallery;
    private final DefaultListViewAdapter overViewAdapter = new DefaultListViewAdapter();
    private final DefaultListViewAdapter galleryAdapter = new DefaultListViewAdapter();


    public GalleryView() {
        overview = new GalleryListView();
        gallery = new GalleryListView();
    }

    public void clearGallery() {
        overViewAdapter.clear();
        galleryAdapter.clear();
    }

    public void addItem(ImageEntry imageEntry, String label) {
        int index = overViewAdapter.size;


        ImageLoader loader = new ImageLoader(true); // image loader with thumb
        loader.setThumbWidth(MAX_THUMB_WIDTH, "");
        loader.setImage(imageEntry.LocalPath);


        GalleryItem item = new GalleryItem(index, loader, label);
//        item.setOnDoubleClickListener(onGalleryItemDoubleClicked);
        galleryAdapter.add(item);

        ImageLoader overviewloader = new ImageLoader(true); // image loader with thumb
        overviewloader.setThumbWidth(MAX_OVERVIEW_THUMB_WIDTH, Utils.THUMB_OVERVIEW);
        overviewloader.setImage(imageEntry.LocalPath);
        GalleryItem overviewItem = new GalleryItem(index, overviewloader);
//        overviewItem.setOnClickListener(onIconClicked);

        overViewAdapter.add(overviewItem);
    }
}
