/*
 * Copyright (C) 2018 team-cachebox.de
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
package de.longri.cachebox3.locator.manager;

/**
 * Created by Longri on 06.03.18.
 */
public class Android_LocationHandler extends LocationHandler {
    @Override
    public LocationManager getNewLocationManager() {
        return new Android_LocationManager(false);
    }

    @Override
    public LocationManager getBackgroundLocationManager() {
        return new Android_LocationManager(true);
    }
}
