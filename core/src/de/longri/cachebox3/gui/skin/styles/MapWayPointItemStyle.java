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

import org.oscim.backend.canvas.Bitmap;

/**
 * Style for map icons.
 * [small] => zoom level < 13
 * [middle] => zoom level 13 - 14
 * [large] => zoom level > 14
 * <p>
 * Created by Longri on 19.01.2017.
 */
public class MapWayPointItemStyle {
    public Bitmap small, middle, large;
}