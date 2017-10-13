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
package de.longri.cachebox3.gui.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.longri.cachebox3.gui.utils.ClickLongClickListener;
import de.longri.cachebox3.settings.Config;
import de.longri.cachebox3.utils.IChanged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Longri on 03.09.16.
 */
public class NamedStage extends Stage {

    private final static Logger log = LoggerFactory.getLogger(NamedStage.class);
    private static int VIBRATE_TIME_MSEC = Config.VibrateTime.getValue();
    private static boolean VIBRATE = Config.VibrateFeedback.getValue();

    static {
        Config.VibrateTime.addChangedEventListener(new IChanged() {
            @Override
            public void isChanged() {
                VIBRATE_TIME_MSEC = Config.VibrateTime.getValue();
            }
        });

        Config.VibrateFeedback.addChangedEventListener(new IChanged() {
            @Override
            public void isChanged() {
                VIBRATE = Config.VibrateFeedback.getValue();
            }
        });
    }

    private String name;

    public NamedStage(final String name, Viewport viewport, Batch batch) {
        super(viewport, batch);
        this.name = name;

        this.addListener(new ClickLongClickListener() {
            @Override
            public boolean clicked(InputEvent event, float x, float y) {
                log.debug("click on {} Stage", name);
                if (VIBRATE) Gdx.input.vibrate(VIBRATE_TIME_MSEC);
                return true;
            }

            @Override
            public boolean longClicked(Actor actor, float x, float y) {
                log.debug("long click on {} Stage", name);
                if (VIBRATE) Gdx.input.vibrate(VIBRATE_TIME_MSEC);
                return true;
            }
        });
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Stage: " + name;
    }
}
