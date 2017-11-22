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

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;
import de.longri.cachebox3.gui.activities.EditFilterSettings;

/**
 * Created by Longri on 16.11.2017.
 */
public class TextFilterView extends Table implements EditFilterSettings.OnShow {

    public TextFilterView() {
        VisLabel label = new VisLabel("TextFilterView");
        this.add(label).expand().fill();
    }

    @Override
    public void onShow() {

    }
}
