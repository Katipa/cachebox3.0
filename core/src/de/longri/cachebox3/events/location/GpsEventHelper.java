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

import de.longri.cachebox3.events.EventHandler;
import de.longri.cachebox3.locator.Coordinate;
import de.longri.cachebox3.locator.CoordinateGPS;
import de.longri.cachebox3.utils.LowpassFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Longri on 23.03.2017.
 */
public class GpsEventHelper {

    private static Logger log = LoggerFactory.getLogger(GpsEventHelper.class);

    private GpsState gpsState = GpsState.UNKNOWN;
    LowpassFilter lowpassFilter = new LowpassFilter(10);

    //#################### NEW

    double lastGpsLat, lastGpsLon, lastNetLat, lastNetLon;
    private float lastGpsAccuracy;
    private double lastGpsElevation;
    private double lastHeading;
    private double lastSpeed;

    public void newGpsPos(double latitude, double longitude) {
        // clamp coordinate to handled precision
        latitude = ((int) (latitude * 1E6)) / 1E6;
        longitude = ((int) (longitude * 1E6)) / 1E6;

        if (lastGpsLat != latitude || lastGpsLon != longitude) {
            lastGpsLat = latitude;
            lastGpsLon = longitude;

            //fire pos changed event
            EventHandler.fire(new PositionChangedEvent(new Coordinate(latitude, longitude), true, EventHandler.getId()));
        }

    }

    public void newNetworkPos(double latitude, double longitude) {
        // clamp coordinate to handled precision
        latitude = ((int) (latitude * 1E6)) / 1E6;
        longitude = ((int) (longitude * 1E6)) / 1E6;
    }

    public void newAltitude(double altitude) {

    }

    /**
     * @param bearing as radians
     * @param gps     is from GPS
     */
    public void newBearing(float bearing, boolean gps) {
        if (gps && this.lastSpeed < 5) return; //TODO use setting value
        if (!gps && this.lastSpeed >= 5) return;
        float value = lowpassFilter.add(bearing);
        if (lastHeading != value) {
            this.lastHeading = value;
            //fire event
            EventHandler.fire(new OrientationChangedEvent(value));
        }
    }

    public void newAccuracy(float accuracy) {

    }

    public void newSpeed(double speed) {
        if (lastSpeed != speed) {
            lastSpeed = speed;
            EventHandler.fire(new SpeedChangedEvent((float) speed));
        }
    }

    public void gpsStateChanged(GpsState state) {
        this.gpsState = state;
//        log.debug("Gps state changed to {}", state);
    }

    public CoordinateGPS getLastGpsCoordinate() {
        CoordinateGPS coord = new CoordinateGPS(this.lastGpsLat, this.lastGpsLon);
        coord.setAccuracy(this.lastGpsAccuracy);
        coord.setElevation(this.lastGpsElevation);
        coord.setHeading(this.lastHeading);
        coord.setSpeed(this.lastSpeed);
        return coord;
    }
}
