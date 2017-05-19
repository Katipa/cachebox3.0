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
package de.longri.cachebox3.gui.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import de.longri.cachebox3.settings.Config;

/**
 * Created by Longri on 19.05.2017.
 */
public abstract class ClickLongClickListener extends ActorGestureListener {

    public ClickLongClickListener() {
        super();
        this.getGestureDetector().setLongPressSeconds((Config.LongClicktime.getValue() / 1000f));
    }

    public abstract void clicked(InputEvent event, float x, float y);

    public abstract boolean longClicked(Actor actor, float x, float y);


    public boolean longPress(Actor actor, float x, float y) {
        return longClicked(actor, x, y);
    }

    public void tap(InputEvent event, float x, float y, int count, int button) {
        clicked(event, x, y);
    }
}
