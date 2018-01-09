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
package de.longri.cachebox3.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;
import de.longri.cachebox3.CB;
import de.longri.cachebox3.gui.stages.NamedStage;
import de.longri.cachebox3.gui.stages.StageManager;
import de.longri.cachebox3.utils.Showable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by Longri on 14.08.16.
 */
public class Window extends Table implements Disposable {

    private final static Logger log = LoggerFactory.getLogger(Window.class);

    static private final Vector2 tmpPosition = new Vector2();
    static private final Vector2 tmpSize = new Vector2();

    private Drawable stageBackground;

    @Override
    public void dispose() {

    }


    public interface WindowCloseListener {
        void windowClosed();
    }

    private WindowCloseListener windowCloseListener;

    public void setWindowCloseListener(WindowCloseListener listener) {
        this.windowCloseListener = listener;
    }

    public void clearWindowCloseListener() {
        this.windowCloseListener = null;
    }


    public Window(String name) {
        super();
        this.setName(name);
    }

    public void setStageBackground(Drawable drawable) {
        this.stageBackground = drawable;
    }

    protected Drawable getStageBackground() {
        return this.stageBackground;
    }

    protected NamedStage showingStage;

    public void show() {
        clearActions();
        pack();

        showingStage = StageManager.showOnNewStage(this);
        addAction(sequence(Actions.alpha(0), Actions.fadeIn(CB.WINDOW_FADE_TIME, Interpolation.fade)));

        if (this instanceof Showable) {
            ((Showable) this).onShow();
        }

        log.debug("Show Window: {}", this.getName());
    }

    public void hide() {
        clearActions();
        StageManager.removeAllWithActStage(showingStage);

        if (this.windowCloseListener != null) {
            this.windowCloseListener.windowClosed();
        }

        if (this instanceof Showable) {
            ((Showable) this).onHide();
        }

        dispose();
        log.debug("Hide Window: {}", this.getName());
        CB.requestRendering();
    }

    public void draw(Batch batch, float parentAlpha) {
        if (stageBackground != null) drawStageBackground(batch, parentAlpha);
        drawBackground(batch, parentAlpha, getX(), getY());
        super.drawChildren(batch, parentAlpha);
    }

    private void drawStageBackground(Batch batch, float parentAlpha) {
        Stage stage = getStage();
        if (stage.getKeyboardFocus() == null) stage.setKeyboardFocus(this);

        stageToLocalCoordinates(tmpPosition.set(0, 0));
        stageToLocalCoordinates(tmpSize.set(stage.getWidth(), stage.getHeight()));
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        stageBackground.draw(batch, getX() + tmpPosition.x, getY() + tmpPosition.y, getX() + tmpSize.x,
                getY() + tmpSize.y);
    }
}
