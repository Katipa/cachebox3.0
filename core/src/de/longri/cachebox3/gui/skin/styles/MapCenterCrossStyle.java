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
package de.longri.cachebox3.gui.skin.styles;

import com.badlogic.gdx.graphics.Color;
import org.oscim.backend.canvas.Paint;

/**
 * Created by Longri on 19.03.17.
 */
public class MapCenterCrossStyle {
    public Color color;
    public float width, length;
    public boolean dotAtCenter = true;
    public Paint.Cap cap = Paint.Cap.ROUND;
}