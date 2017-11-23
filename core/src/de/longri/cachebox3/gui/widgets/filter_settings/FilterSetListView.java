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
package de.longri.cachebox3.gui.widgets.filter_settings;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import de.longri.cachebox3.CB;
import de.longri.cachebox3.gui.activities.EditFilterSettings;
import de.longri.cachebox3.gui.skin.styles.FilterStyle;
import de.longri.cachebox3.gui.views.listview.Adapter;
import de.longri.cachebox3.gui.views.listview.ListView;
import de.longri.cachebox3.gui.views.listview.ListViewItem;
import de.longri.cachebox3.gui.widgets.AdjustableStarWidget;
import de.longri.cachebox3.gui.widgets.CharSequenceButton;
import de.longri.cachebox3.translation.Translation;
import de.longri.cachebox3.types.IntProperty;
import de.longri.cachebox3.types.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Longri on 16.11.2017.
 */
public class FilterSetListView extends Table implements EditFilterSettings.OnShow {

    private final Logger log = LoggerFactory.getLogger(FilterSetListView.class);

    private final ListView setListView;
    private final FilterStyle style;
    private final EditFilterSettings filterSettings;
    private final Array<ListViewItem> listViewItems = new Array<>();
    private final Adapter listViewAdapter;

    public FilterSetListView(EditFilterSettings editFilterSettings, FilterStyle style) {
        this.style = style;
        this.filterSettings = editFilterSettings;

        listViewAdapter = new Adapter() {
            @Override
            public int getCount() {
                return listViewItems.size;
            }

            @Override
            public ListViewItem getView(int index) {
                return listViewItems.get(index);
            }

            @Override
            public void update(ListViewItem view) {

            }

            @Override
            public float getItemSize(int index) {
                ListViewItem item = listViewItems.get(index);
                return item.isVisible() ? item.getHeight() : 0;
            }
        };

        setListView = new ListView(listViewAdapter, true, false);
        setListView.setSelectable(ListView.SelectableType.NONE);


        this.add(setListView).expand().fill();
        setListView.setEmptyString("EmptyList");

        fillList();

    }

    private void fillList() {
        listViewItems.clear();
        addGeneralItems();
        addDTGcVoteItems();
        addCachTypeItems();
        addAttributeItems();
        setListView.setAdapter(listViewAdapter);
    }

    private void addGeneralItems() {

        final AtomicBoolean sectionVisible = new AtomicBoolean(false);

        final IntPropertyListView available = new IntPropertyListView(listViewItems.size + 1,
                filterSettings.filterProperties.NotAvailable, style.Available, Translation.get("disabled"));
        final IntPropertyListView archived = new IntPropertyListView(listViewItems.size + 2,
                filterSettings.filterProperties.Archived, style.PrepareToArchive, Translation.get("archived"));
        final IntPropertyListView finds = new IntPropertyListView(listViewItems.size + 3,
                filterSettings.filterProperties.Finds, style.finds, Translation.get("myfinds"));
        final IntPropertyListView own = new IntPropertyListView(listViewItems.size + 4,
                filterSettings.filterProperties.Own, style.own, Translation.get("myowncaches"));
        final IntPropertyListView withTb = new IntPropertyListView(listViewItems.size + 5,
                filterSettings.filterProperties.ContainsTravelbugs, style.TB, Translation.get("withtrackables"));
        final IntPropertyListView favorites = new IntPropertyListView(listViewItems.size + 6,
                filterSettings.filterProperties.Favorites, style.Favorites, Translation.get("Favorites"));
        final IntPropertyListView hasUserData = new IntPropertyListView(listViewItems.size + 7,
                filterSettings.filterProperties.HasUserData, style.HasUserData, Translation.get("hasuserdata"));
        final IntPropertyListView listingChanged = new IntPropertyListView(listViewItems.size + 8,
                filterSettings.filterProperties.ListingChanged, style.ListingChanged, Translation.get("ListingChanged"));
        final IntPropertyListView manualwaypoint = new IntPropertyListView(listViewItems.size + 9,
                filterSettings.filterProperties.WithManualWaypoint, style.ManualWaypoint, Translation.get("manualwaypoint"));
        final IntPropertyListView corrected = new IntPropertyListView(listViewItems.size + 10,
                filterSettings.filterProperties.hasCorrectedCoordinates, style.CoorectedCoord, Translation.get("hasCorrectedCoordinates"));


        ClickListener listener = new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                boolean visible = !sectionVisible.get();
                sectionVisible.set(visible);
                available.setVisible(visible);
                archived.setVisible(visible);
                finds.setVisible(visible);
                own.setVisible(visible);
                withTb.setVisible(visible);
                favorites.setVisible(visible);
                hasUserData.setVisible(visible);
                listingChanged.setVisible(visible);
                manualwaypoint.setVisible(visible);
                corrected.setVisible(visible);

                setListView.invalidate();
                setListView.layout(true);
            }
        };

        listViewItems.add(new ButtonListViewItem(listViewItems.size, Translation.get("General"), listener));
        listViewItems.add(available);
        listViewItems.add(archived);
        listViewItems.add(finds);
        listViewItems.add(own);
        listViewItems.add(withTb);
        listViewItems.add(favorites);
        listViewItems.add(hasUserData);
        listViewItems.add(listingChanged);
        listViewItems.add(manualwaypoint);
        listViewItems.add(corrected);

    }

    private void addDTGcVoteItems() {

        final AtomicBoolean sectionVisible = new AtomicBoolean(false);

        final AdjustableStarListViewItem minDificulty = new AdjustableStarListViewItem(listViewItems.size + 1,
                filterSettings.filterProperties.MinDifficulty, Translation.get("minDifficulty"));
        final AdjustableStarListViewItem maxDificulty = new AdjustableStarListViewItem(listViewItems.size + 1,
                filterSettings.filterProperties.MaxDifficulty, Translation.get("maxDifficulty"));
        final AdjustableStarListViewItem minTerrain = new AdjustableStarListViewItem(listViewItems.size + 1,
                filterSettings.filterProperties.MinTerrain, Translation.get("minTerrain"));
        final AdjustableStarListViewItem maxTerrain = new AdjustableStarListViewItem(listViewItems.size + 1,
                filterSettings.filterProperties.MaxTerrain, Translation.get("maxTerrain"));
        final AdjustableStarListViewItem minContainerSize = new AdjustableStarListViewItem(listViewItems.size + 1,
                filterSettings.filterProperties.MinContainerSize, Translation.get("minContainerSize"));
        final AdjustableStarListViewItem maxContainerSize = new AdjustableStarListViewItem(listViewItems.size + 1,
                filterSettings.filterProperties.MaxContainerSize, Translation.get("maxContainerSize"));
        final AdjustableStarListViewItem minRating = new AdjustableStarListViewItem(listViewItems.size + 1,
                filterSettings.filterProperties.MinRating, Translation.get("minRating"));
        final AdjustableStarListViewItem maxRating = new AdjustableStarListViewItem(listViewItems.size + 1,
                filterSettings.filterProperties.MaxRating, Translation.get("maxRating"));


        ClickListener listener = new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                boolean visible = !sectionVisible.get();
                sectionVisible.set(visible);
                minDificulty.setVisible(visible);
                maxDificulty.setVisible(visible);
                minTerrain.setVisible(visible);
                maxTerrain.setVisible(visible);
                minContainerSize.setVisible(visible);
                maxContainerSize.setVisible(visible);
                minRating.setVisible(visible);
                maxRating.setVisible(visible);

                setListView.invalidate();
                setListView.layout(true);
            }
        };

        listViewItems.add(new ButtonListViewItem(listViewItems.size, "D / T" + String.format("%n") + "GC-Vote", listener));
        listViewItems.add(minDificulty);
        listViewItems.add(maxDificulty);
        listViewItems.add(minTerrain);
        listViewItems.add(maxTerrain);
        listViewItems.add(minContainerSize);
        listViewItems.add(maxContainerSize);
        listViewItems.add(minRating);
        listViewItems.add(maxRating);
    }

    private void addCachTypeItems() {
        ClickListener listener = new ClickListener() {

        };

        listViewItems.add(new ButtonListViewItem(listViewItems.size, Translation.get("CacheTypes"), listener));
    }

    private void addAttributeItems() {
        ClickListener listener = new ClickListener() {

        };

        listViewItems.add(new ButtonListViewItem(listViewItems.size, Translation.get("Attributes"), listener));
    }

    @Override
    public void onShow() {
        fillList();
    }

    class ButtonListViewItem extends ListViewItem {

        public ButtonListViewItem(int listIndex, CharSequence text, ClickListener clickListener) {
            super(listIndex);
            CharSequenceButton btn = new CharSequenceButton(text);
            btn.getLabel().setWrap(true);
            this.addListener(clickListener);
            this.add(btn).expand().fill();
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            //clear Background
            this.setBackground((Drawable) null);
            super.draw(batch, parentAlpha);
        }

        @Override
        public void dispose() {

        }
    }

    class IntPropertyListView extends ListViewItem {

        final IntProperty property;
        final Image checkImage;


        public IntPropertyListView(int listIndex, final IntProperty property, Drawable icon, final CharSequence name) {
            super(listIndex);

            this.property = property;

            this.setVisible(false);

            //Left icon
            final Image iconImage = new Image(icon, Scaling.none);
            this.add(iconImage).center().padRight(CB.scaledSizes.MARGIN_HALF);

            //Center name text
            final Label label = new VisLabel(name);
            label.setWrap(true);
            this.add(label).expandX().fillX().padTop(CB.scaledSizes.MARGIN).padBottom(CB.scaledSizes.MARGIN);

            //Right checkBox
            checkImage = new Image(style.CheckNo);
            this.add(checkImage).width(checkImage.getWidth()).pad(CB.scaledSizes.MARGIN / 2);

            this.setCheckImage();

            this.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    int value = property.get();

                    if (value == -1)
                        value = 0;
                    else if (value == 0)
                        value = 1;
                    else if (value == 1)
                        value = -1;
                    property.set(value);
                }
            });

            property.setChangeListener(new Property.PropertyChangedListener() {
                @Override
                public void propertyChanged() {
                    log.debug("Property {} changed to {}", name, property.get());

                    //property changed, so set name to "?"
                    filterSettings.filterProperties.setName("?");
                    setCheckImage();
                }
            });
        }

        private void setCheckImage() {
            CB.postOnMainThread(new Runnable() {
                @Override
                public void run() {
                    switch (property.get()) {
                        case -1:
                            checkImage.setDrawable(style.CheckOff);
                            break;
                        case 0:
                            checkImage.setDrawable(style.CheckNo);
                            break;
                        case 1:
                            checkImage.setDrawable(style.Check);
                            break;
                        default:
                            throw new RuntimeException("Unknown filter property state");
                    }
                }
            });
        }

        @Override
        public void dispose() {

        }
    }

    class AdjustableStarListViewItem extends ListViewItem {

        final IntProperty property;
        final AdjustableStarWidget adjustableWidget;

        public AdjustableStarListViewItem(int listIndex, final IntProperty property, final CharSequence name) {
            super(listIndex);

            this.property = property;
            this.setVisible(false);

            this.adjustableWidget = new AdjustableStarWidget(name, property);
            this.add(this.adjustableWidget).expandX().fillX().padTop(CB.scaledSizes.MARGIN).padBottom(CB.scaledSizes.MARGIN);

            property.setChangeListener(new Property.PropertyChangedListener() {
                @Override
                public void propertyChanged() {
                    log.debug("Property {} changed to {}", name, property.get());

                    //property changed, so set name to "?"
                    filterSettings.filterProperties.setName("?");
                }
            });


            // ListViewItem catch the ClickEvent from Button/Label
            // So we reroute the event to the Button!
            this.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (event.getTarget() instanceof VisTextButton
                            || event.getTarget().getParent() instanceof VisTextButton) {

                        Array<EventListener> listeners = event.getTarget() instanceof VisTextButton ?
                                event.getTarget().getListeners() : event.getTarget().getParent().getListeners();
                        int n = listeners.size;
                        while (n-- > 0) {
                            EventListener listener = listeners.get(n);
                            if (listener instanceof ClickListener) {
                                ((ClickListener) listener).clicked(event, x, y);
                            }
                        }
                    }
                }
            });
        }


        @Override
        public void dispose() {

        }
    }

}
