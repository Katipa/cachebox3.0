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
package de.longri.cachebox3.gui.activities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import de.longri.cachebox3.CB;
import de.longri.cachebox3.apis.groundspeak_api.GroundspeakAPI;
import de.longri.cachebox3.apis.groundspeak_api.json_parser.stream_parser.CheckCacheStateParser;
import de.longri.cachebox3.events.*;
import de.longri.cachebox3.gui.*;
import de.longri.cachebox3.gui.Window;
import de.longri.cachebox3.gui.dialogs.ButtonDialog;
import de.longri.cachebox3.gui.dialogs.MessageBoxButtons;
import de.longri.cachebox3.gui.dialogs.MessageBoxIcon;
import de.longri.cachebox3.gui.dialogs.OnMsgBoxClickListener;
import de.longri.cachebox3.gui.drawables.ColorDrawable;
import de.longri.cachebox3.gui.events.CacheListChangedEventList;
import de.longri.cachebox3.gui.stages.ViewManager;
import de.longri.cachebox3.settings.Config;
import de.longri.cachebox3.sqlite.Database;
import de.longri.cachebox3.sqlite.dao.CacheDAO;
import de.longri.cachebox3.translation.Translation;
import de.longri.cachebox3.types.Cache;
import de.longri.cachebox3.utils.ICancel;
import org.oscim.backend.CanvasAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by Longri on 28.06.2017.
 */
public class CheckStateActivity extends ActivityBase {

    private static final Logger log = LoggerFactory.getLogger(CheckStateActivity.class);

    private final int blockSize = 108; // The API leaves only a maximum of 110 per request!
    private final VisTextButton bCancel;
    private final VisLabel lblTitle;
    private final Image gsLogo;
    private boolean importRuns = false;
    private boolean needLayout = true;
    private final Image workAnimation;
    private final VisProgressBar progressBar;
    private final AtomicBoolean canceld = new AtomicBoolean(false);

    public CheckStateActivity() {
        super("CheckStateActivity");
        bCancel = new VisTextButton(Translation.Get("cancel"));
        gsLogo = new Image(CB.getSkin().getIcon.GC_Live);
        lblTitle = new VisLabel(Translation.Get("chkApiState"));
        Label.LabelStyle style = new Label.LabelStyle(lblTitle.getStyle());
        style.fontColor.set(Color.WHITE);
        lblTitle.setStyle(style);
        Drawable animationDrawable = VisUI.getSkin().getDrawable("download-animation");
        workAnimation = new Image(animationDrawable);
        progressBar = new VisProgressBar(0, 100, 1, false, "default");

        createOkCancelBtn();
        setWorkAnimationVisible(false);

        this.setStageBackground(new ColorDrawable(VisUI.getSkin().getColor("dialog_background")));
//        this.setDebug(true, true);
    }

    @Override
    public void layout() {
        if (!needLayout) {
            super.layout();
            return;
        }

        SnapshotArray<Actor> actors = this.getChildren();
        for (Actor actor : actors)
            this.removeActor(actor);

        this.setFillParent(true);
        this.defaults().pad(CB.scaledSizes.MARGIN);

        this.add(lblTitle).colspan(3).center();
        this.add(gsLogo).colspan(2).center();
        this.row().padTop(new Value.Fixed(CB.scaledSizes.MARGINx2 * 2));


        this.add(workAnimation).colspan(5).center();
        this.row();
        this.add();
        this.add(progressBar).colspan(3).center().expandX().fillX();
        this.row();
        Table nestedTable2 = new Table();
        nestedTable2.defaults().pad(CB.scaledSizes.MARGIN).bottom();
        nestedTable2.add(bCancel).bottom();
        this.add(nestedTable2).colspan(5);

        super.layout();
        needLayout = false;
    }


    private void setWorkAnimationVisible(boolean visible) {
        workAnimation.setVisible(visible);
        progressBar.setVisible(visible);
    }


    private void createOkCancelBtn() {
        bCancel.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (importRuns) {
                    canceld.set(true);
                }
                finish();
            }
        });
    }

    @Override
    public void onShow() {
        if (!workAnimation.isVisible()) importNow();
    }

    private void importNow() {

        setWorkAnimationVisible(true);
        progressBar.setAnimateDuration(0);
        final ImportProgressChangedListener progressListener = new ImportProgressChangedListener() {
            @Override
            public void progressChanged(final ImportProgressChangedEvent event) {
                CB.postOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setValue(event.progress.progress);
                    }
                });
            }
        };
        EventHandler.add(progressListener);
        importRuns = true;

        CB.postAsync(new Runnable() {
            @Override
            public void run() {

                int result = 0;
                final Array<Cache> chkList = new Array<>();

                synchronized (Database.Data.Query) {
                    if (Database.Data.Query == null || Database.Data.Query.size == 0)
                        return;
                    for (int i = 0, n = Database.Data.Query.size; i < n; i++) {
                        chkList.add(Database.Data.Query.get(i));
                    }

                }
                final AtomicInteger progressIncrement = new AtomicInteger(0);

                // in Blöcke Teilen
                int start = 0;
                int stop = blockSize;
                Array<Cache> addedReturnList = new Array<>();
                Array<Cache> chkList100;

                ApiCallLimitListener limitListener = new ApiCallLimitListener() {
                    @Override
                    public void waitForCall(ApiCallLimitEvent event) {
                        int sec = (int) (event.getWaitTime() / 1000);
                        CB.viewmanager.toast( Translation.Get("ApiLimit"
                                , Integer.toString(Config.apiCallLimit.getValue()), Integer.toString(sec))
                                , ViewManager.ToastLength.LONG);
                    }
                };

                EventHandler.add(limitListener);

                do {
                    chkList100 = new Array<>();
                    if (chkList == null || chkList.size == 0) {
                        break;
                    }

                    Iterator<Cache> Iterator2 = chkList.iterator();

                    int index = 0;
                    do {
                        if (index >= start && index <= stop) {
                            chkList100.add(Iterator2.next());
                        } else {
                            Iterator2.next();
                        }
                        index++;
                    } while (Iterator2.hasNext());

                    result = GroundspeakAPI.getGeocacheStatus(chkList100, new ICancel() {
                        @Override
                        public boolean cancel() {
                            return canceld.get();
                        }
                    }, new CheckCacheStateParser.ProgressIncrement() {
                        @Override
                        public void increment() {
                            // send Progress Change Msg
                            ImportProgressChangedEvent.ImportProgress progress = new ImportProgressChangedEvent.ImportProgress();
                            progress.progress = (int) (100f / ((float) chkList.size / (float) progressIncrement.incrementAndGet()));
                            EventHandler.fire(new ImportProgressChangedEvent(progress));
                        }
                    });
                    if (result == -1)
                        break;// API Error
                    addedReturnList.addAll(chkList100);
                    start += blockSize + 1;
                    stop += blockSize + 1;

                } while (chkList100.size == blockSize + 1);

                //Write changes to DB
                final AtomicInteger changedCount = new AtomicInteger(0);
                Database.Data.beginTransaction();
                Iterator<Cache> iterator = addedReturnList.iterator();
                CacheDAO dao = new CacheDAO();
                do {
                    Cache writeTmp = iterator.next();
                    if (dao.UpdateDatabaseCacheState(writeTmp))
                        changedCount.incrementAndGet();
                } while (iterator.hasNext());

                Database.Data.setTransactionSuccessful();
                Database.Data.endTransaction();

                //state check complete, close activity
                EventHandler.remove(limitListener);
                finish();

                CB.postOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        //Give feedback and say what updated!
                        CacheListChangedEventList.Call();
                        String title = Translation.Get("chkState");
                        String msg = Translation.Get("CachesUpdatet") + " " + changedCount.get() + "/" + Database.Data.Query.size;
                        Window dialog = new ButtonDialog("chkState", msg, title, MessageBoxButtons.OK, MessageBoxIcon.None, new OnMsgBoxClickListener() {
                            @Override
                            public boolean onClick(int which, Object data) {
                                if (which == ButtonDialog.BUTTON_POSITIVE) {
                                    hide();
                                }
                                return true;
                            }
                        });
                        dialog.show();
                    }
                });
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

