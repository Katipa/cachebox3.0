/*
 * Copyright (C) 2016-2017 team-cachebox.de
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

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import de.longri.cachebox3.gui.Window;
import de.longri.cachebox3.gui.actions.AbstractAction;
import de.longri.cachebox3.gui.actions.show_vies.Abstract_Action_ShowView;
import de.longri.cachebox3.gui.help.GestureHelp;
import de.longri.cachebox3.gui.menu.Menu;
import de.longri.cachebox3.gui.menu.MenuItem;
import de.longri.cachebox3.gui.menu.OnItemClickListener;
import de.longri.cachebox3.gui.skin.styles.GestureButtonStyle;
import de.longri.cachebox3.settings.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by Longri on 24.07.16.
 */
public class GestureButton extends Button {

    final static Logger log = LoggerFactory.getLogger(GestureButton.class);
    final static float MIN_GESTURE_VELOCITY = 100;


    private static int idCounter = 0;
//    protected static Drawable menuDrawable;
//    protected static Drawable menuDrawableFiltered;

    private GestureButtonStyle style;
    private final ArrayList<ActionButton> buttonActions;
    private final int ID;
    private Abstract_Action_ShowView aktActionView;
    private boolean hasContextMenu;
    private GestureHelp gestureHelper;
    private Drawable gestureRightIcon, gestureUpIcon, gestureLeftIcon, gestureDownIcon;

    public ArrayList<ActionButton> getButtonActions() {
        return buttonActions;
    }

    public void setHasContextMenu(boolean hasContextMenu) {
        this.hasContextMenu = hasContextMenu;
    }


    public GestureButton(String styleName) {
        style = VisUI.getSkin().get(styleName, GestureButtonStyle.class);
        style.checked = style.select;
        this.setStyle(style);
        this.ID = idCounter++;
        buttonActions = new ArrayList<ActionButton>();

        //remove all Listeners
        Array<EventListener> listeners = this.getListeners();
        for (EventListener listener : listeners) {
            this.removeListener(listener);
        }
        this.addListener(gestureListener);
        this.pack();
    }

    public boolean equals(Object other) {
        if (other instanceof GestureButton) {
            return ((GestureButton) other).ID == ID;
        }
        return false;
    }

    public void addAction(ActionButton action) {
        buttonActions.add(action);

        //check if this a gesture
        if (action.getGestureDirection() != ActionButton.GestureDirection.None) {
            switch (action.getGestureDirection()) {
                case Right:
                    gestureRightIcon = action.getIcon();
                    break;
                case Up:
                    gestureUpIcon = action.getIcon();
                    break;
                case Left:
                    gestureLeftIcon = action.getIcon();
                    break;
                case Down:
                    gestureDownIcon = action.getIcon();
                    break;
            }
        }

    }

    public void executeDefaultAction() {
        for (ActionButton action : buttonActions) {
            if (action.isDefaultAction()) {
                action.getAction().execute();
                return;
            }
        }

        //if no default button so take the first or do nothing if no buttonAction set
        if (!buttonActions.isEmpty()) {
            ActionButton action = buttonActions.get(0);
            if (action != null) action.getAction().execute();
        }
    }

    public void executeAction(ActionButton.GestureDirection direction) {
        for (ActionButton action : buttonActions) {
            if (action.getGestureDirection() == direction) {
                action.getAction().execute();
                return;
            }
        }
    }


    //TODO inital with longPressDuration from settings
    //   use ClickLongClickListener!!!
    ActorGestureListener gestureListener = new ActorGestureListener() {

        @Override
        public void tap(InputEvent event, float x, float y, int count, int button) {
            log.debug("on click");


            // Einfacher Click -> alle Actions durchsuchen, ob die aktActionView darin enthalten ist und diese sichtbar ist
            if ((aktActionView != null) && (aktActionView.hasContextMenu())) {
                for (ActionButton ba : buttonActions) {
                    if (ba.getAction() == aktActionView) {
                        if (aktActionView.isActVisible()) {
                            // Dieses View ist aktuell das Sichtbare
                            // -> ein Click auf den Menü-Button zeigt das Contextmenü
                            // if (aktActionView.ShowContextMenu()) return true;

                            if (aktActionView.hasContextMenu()) {
                                // das View Context Menü mit dem LongKlick Menü zusammen führen!

                                // Menu zusammen stellen!
                                // zuerst das View Context Menu
                                Menu compoundMenu = new Menu("compoundMenu");

                                final OnItemClickListener bothListener[] = new OnItemClickListener[2];
                                final OnItemClickListener bothItemClickListener = new OnItemClickListener() {


                                    @Override
                                    public boolean onItemClick(MenuItem item) {

                                        boolean handeld = false;

                                        if (bothListener[0] != null)
                                            handeld = bothListener[0].onItemClick(item);

                                        if (!handeld && bothListener[1] != null)
                                            handeld = bothListener[1].onItemClick(item);

                                        return handeld;
                                    }
                                };


                                Menu viewContextMenu = aktActionView.getContextMenu();
                                if (viewContextMenu != null) {
                                    compoundMenu.addItems(viewContextMenu.getItems());
                                    bothListener[0] = viewContextMenu.getOnItemClickListeners();

                                    // add divider
                                    compoundMenu.addDivider(-1);
                                }

                                Menu longClickMenu = getLongClickMenu();
                                if (longClickMenu != null) {
                                    compoundMenu.addItems(longClickMenu.getItems());
                                    bothListener[1] = longClickMenu.getOnItemClickListeners();
                                }
                                compoundMenu.setOnItemClickListener(bothItemClickListener);
                                compoundMenu.reorganizeListIndexes();
                                compoundMenu.show();
                                return;
                            }
                        }
                    }
                }
            }

            // execute default action
            boolean actionExecuted = false;
            for (ActionButton ba : buttonActions) {
                if (ba.isDefaultAction()) {
                    AbstractAction action = ba.getAction();
                    if (action != null) {
                        action.execute();
                        if (action instanceof Abstract_Action_ShowView)
                            aktActionView = (Abstract_Action_ShowView) action;
                        actionExecuted = true;
                        break;
                    }
                }
            }

            // if no default action seted, show context-menu from view (like long click)
            if (!actionExecuted) {
                longPress(event.getTarget(), x, y);
            }
        }

        @Override
        public boolean longPress(Actor actor, float x, float y) {
            log.debug("onLongClick");
            // GL_MsgBox.Show("Button " + Me.getName() + " recivet a LongClick Event");
            // Wenn diesem Button mehrere Actions zugeordnet sind dann wird nach einem Lang-Click ein Menü angezeigt aus dem eine dieser
            // Actions gewählt werden kann

            if (buttonActions.size() > 1) {
                getLongClickMenu().show();
            } else if (buttonActions.size() == 1) {
                // nur eine Action dem Button zugeordnet -> diese Action gleich ausführen
                ActionButton ba = buttonActions.get(0);
                AbstractAction action = ba.getAction();
                if (action != null) {
                    action.execute();
                    if (action instanceof Abstract_Action_ShowView)
                        aktActionView = (Abstract_Action_ShowView) action;
                }
            }
            return true;
        }

        public void fling(InputEvent event, float velocityX, float velocityY, int button) {

            float maxVelocity = Math.max(Math.abs(velocityX), Math.abs(velocityY));

            if (maxVelocity < MIN_GESTURE_VELOCITY) {
                // not really a gesture
                return;
            }


            ActionButton.GestureDirection direction = ActionButton.GestureDirection.Up;
            if (Math.abs(velocityX) >= Math.abs(velocityY)) {
                // left or right
                if (velocityX > 0) {
                    direction = ActionButton.GestureDirection.Right;
                } else {
                    direction = ActionButton.GestureDirection.Left;
                }
            } else {
                // up or down
                if (velocityY > 0) {
                    direction = ActionButton.GestureDirection.Up;
                } else {
                    direction = ActionButton.GestureDirection.Down;
                }
            }
            executeAction(direction);
        }
    };


    private Menu getLongClickMenu() {
        Menu cm = new Menu("Name");

        cm.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public boolean onItemClick(MenuItem item) {
                int mId = item.getMenuItemId();

                for (ActionButton ba : buttonActions) {
                    if (ba.getAction().getId() == mId) {
                        final AbstractAction action = ba.getAction();

                        //have the calling action a gesture, then show gesture helper
                        if (Config.showGestureHelp.getValue() && ba.getGestureDirection() != ActionButton.GestureDirection.None) {
                            if (gestureHelper == null) {
                                gestureHelper = new GestureHelp(GestureHelp.getHelpEllipseFromActor(GestureButton.this),
                                        style.up, gestureRightIcon, gestureUpIcon, gestureLeftIcon, gestureDownIcon);
                            }
                            gestureHelper.setWindowCloseListener(new Window.WindowCloseListener() {
                                @Override
                                public void windowClosed() {
                                    gestureHelper.clearWindowCloseListener();
                                    action.execute();
                                    if (action instanceof Abstract_Action_ShowView)
                                        aktActionView = (Abstract_Action_ShowView) action;
                                }
                            });
                            gestureHelper.show(ba.getGestureDirection());
                            return true;
                        } else {
                            // no gesture, call direct
                            action.execute();
                            if (action instanceof Abstract_Action_ShowView)
                                aktActionView = (Abstract_Action_ShowView) action;
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        for (ActionButton ba : buttonActions) {
            AbstractAction action = ba.getAction();
            if (action == null)
                continue;
            MenuItem mi = cm.addItem(action.getId(), action.getName(), action.getNameExtention());
            mi.setEnabled(action.getEnabled());
            mi.setCheckable(action.getIsCheckable());
            mi.setChecked(action.getIsChecked());
            mi.setIcon(action.getIcon());
        }
        return cm;
    }


    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (hasContextMenu && isChecked()) {

            Vector2 stagePos = new Vector2();
            this.localToStageCoordinates(stagePos);

            boolean isFiltered = false; //TODO set filtered!

            if (!isFiltered && style.hasMenu != null)
                style.hasMenu.draw(batch, stagePos.x, stagePos.y, this.getWidth(), this.getHeight());
            if (isFiltered && style.hasFilteredMenu != null)
                style.hasFilteredMenu.draw(batch, stagePos.x, stagePos.y, this.getWidth(), this.getHeight());
        }
    }
}
