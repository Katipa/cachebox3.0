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
package de.longri.cachebox3.desktop;

import ch.fhnw.imvs.gpssimulator.components.LocationPanel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import de.longri.cachebox3.CB;
import de.longri.cachebox3.CacheboxMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Longri on 03.07.19.
 */
public class DesktopMouseMain extends DesktopMain {

    private final static Logger log = LoggerFactory.getLogger(DesktopMouseMain.class);

    public DesktopMouseMain() {
        main = this;
    }

    @Override
    public void resize(int width, int height) {
        CB.stageManager.viewport.update(width, height, true);
        CB.stageManager.viewport.getCamera().viewportHeight = height;
        CB.stageManager.viewport.getCamera().viewportWidth = width;

        // tell children about the resize

//        CB.stageManager.resize(width, height);
//        for (Actor actor : CB.stageManager.mainStage.getActors()) {
//            log.debug("Actor {}:{}", actor instanceof Layout, actor.toString());
//        }


//                stageList
//
//        toaststage
    }
}
