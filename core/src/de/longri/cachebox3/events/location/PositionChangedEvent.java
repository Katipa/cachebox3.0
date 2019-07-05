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
package de.longri.cachebox3.events.location;

import de.longri.cachebox3.events.AbstractEvent;
import de.longri.cachebox3.locator.Coordinate;

/**
 * Created by Longri on 23.03.2017.
 */
public class PositionChangedEvent extends AbstractEvent<Coordinate> {
    public final Coordinate pos;
    public final boolean gpsProvided;

    public PositionChangedEvent(Coordinate pos, boolean gpsProvided) {
        super(Coordinate.class);
        this.pos = pos;
        this.gpsProvided = gpsProvided;
    }

    public PositionChangedEvent(Coordinate pos, boolean gpsProvided, short id) {
        super(Coordinate.class, id);
        this.pos = pos;
        this.gpsProvided = gpsProvided;
    }

    @Override
    public Class getListenerClass() {
        return PositionChangedListener.class;
    }
}