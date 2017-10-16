/* 
 * Copyright (C) 2014-2017 team-cachebox.de
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
package de.longri.cachebox3.locator.events;

import de.longri.cachebox3.CB;
import de.longri.cachebox3.gui.stages.ViewManager;
import de.longri.cachebox3.events.EventHandler;
import de.longri.cachebox3.settings.Config;
import de.longri.cachebox3.sqlite.Database;
import de.longri.cachebox3.types.AbstractCache;
import de.longri.cachebox3.types.CacheTypes;
import de.longri.cachebox3.types.CacheWithWP;
import de.longri.cachebox3.utils.MathUtils;
import de.longri.cachebox3.utils.SoundCache;
import org.oscim.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Empfängt alle Positions Änderungen und sortiert Liste oder spielt Sounds ab.
 *
 * @author Longri
 */
public class GlobalLocationReceiver implements GPS_FallBackEvent {
    final static Logger log = LoggerFactory.getLogger(GlobalLocationReceiver.class);
    public final static boolean DEBUG_POSITION = true;

    public final static String GPS_PROVIDER = "gps";
    public final static String NETWORK_PROVIDER = "network";

    private boolean initialResortAfterFirstFixCompleted = false;
    private boolean initialFixSoundCompleted = false;
    private static boolean approachSoundCompleted = false;

    public GlobalLocationReceiver() {


        GPS_FallBackEventList.Add(this);
        try {
            SoundCache.loadSounds();
        } catch (Exception e) {
            log.error("Load sound", e);
            e.printStackTrace();
        }
    }

    private static boolean PlaySounds = false;

    public void positionChanged(Event event) {

        PlaySounds = !Config.GlobalVolume.getValue().Mute;

        if (newLocationThread != null) {
            if (newLocationThread.getState() != Thread.State.TERMINATED)
                return;
            else
                newLocationThread = null;
        }

        if (newLocationThread == null)
            newLocationThread = new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        if (PlaySounds && !approachSoundCompleted) {
                            if (EventHandler.getSelectedCache() != null) {
                                float distance = EventHandler.getSelectedCache().Distance(MathUtils.CalculationType.FAST, false);
                                if (EventHandler.getSelectedWaypoint() != null) {
                                    distance = EventHandler.getSelectedWaypoint().distance();
                                }

                                if (!approachSoundCompleted && (distance < Config.SoundApproachDistance.getValue())) {
                                    SoundCache.play(SoundCache.Sounds.Approach);
                                    approachSoundCompleted = true;

                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("Global.PlaySound(Approach.ogg)", e);
                        e.printStackTrace();
                    }

                    try {
                        if (!initialResortAfterFirstFixCompleted) {
                            if (EventHandler.getSelectedCache() == null) {
                                CacheWithWP ret = Database.Data.Query.Resort(EventHandler.getSelectedCoord(), new CacheWithWP(EventHandler.getSelectedCache(), EventHandler.getSelectedWaypoint()));

                                if (ret != null && ret.getCache() != null) {
//                                        EventHandler.setSelectedWaypoint(ret.getCache(), ret.getWaypoint(), false);
                                    CB.setNearestCache(ret.getCache());
                                    ret.dispose();
                                }
                            }
                            initialResortAfterFirstFixCompleted = true;
                        }
                    } catch (Exception e) {
                        log.error("if (!initialResortAfterFirstFixCompleted && CB.LastValidPosition.Valid)", e);
                        e.printStackTrace();
                    }

                    try {
                        // schau die 50 nächsten Caches durch, wenn einer davon näher ist
                        // als der aktuell nächste -> umsortieren und raus
                        // only when showing Map or cacheList
                        if (!Database.Data.Query.ResortAtWork) {
                            if (CB.getAutoResort()) {
                                if ((CB.NearestCache() == null)) {
                                    CB.setNearestCache(EventHandler.getSelectedCache());
                                }
                                int z = 0;
                                if (!(CB.NearestCache() == null)) {
                                    boolean resort = false;
                                    if (CB.NearestCache().isFound()) {
                                        resort = true;
                                    } else {
                                        if (EventHandler.getSelectedCache() != CB.NearestCache()) {
//                                            EventHandler.setSelectedWaypoint(CB.NearestCache(), null, false);
                                        }
                                        float nearestDistance = CB.NearestCache().Distance(MathUtils.CalculationType.FAST, true);

                                        for (int i = 0, n = Database.Data.Query.size; i < n; i++) {
                                            AbstractCache abstractCache = Database.Data.Query.get(i);
                                            z++;
                                            if (z >= 50) {
                                                return;
                                            }
                                            if (abstractCache.isArchived())
                                                continue;
                                            if (!abstractCache.isAvailable())
                                                continue;
                                            if (abstractCache.isFound())
                                                continue;
                                            if (abstractCache.ImTheOwner())
                                                continue;
                                            if (abstractCache.getType() == CacheTypes.Mystery)
                                                if (!abstractCache.CorrectedCoordiantesOrMysterySolved())
                                                    continue;
                                            if (abstractCache.Distance(MathUtils.CalculationType.FAST, true) < nearestDistance) {
                                                resort = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (resort || z == 0) {
                                        CacheWithWP ret = Database.Data.Query.Resort(EventHandler.getSelectedCoord(), new CacheWithWP(EventHandler.getSelectedCache(), EventHandler.getSelectedWaypoint()));

//                                        EventHandler.setSelectedWaypoint(ret.getCache(), ret.getWaypoint(), false);
                                        CB.setNearestCache(ret.getCache());
                                        ret.dispose();

                                        SoundCache.play(SoundCache.Sounds.AutoResortSound);
                                        return;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("Resort", e);
                        e.printStackTrace();
                    }

                }
            });

        newLocationThread.start();

    }

    Thread newLocationThread;


    public static void resetApproach() {

        // set approach sound if the distance low

        if (EventHandler.getSelectedCache() != null) {
            float distance = EventHandler.getSelectedCache().Distance(MathUtils.CalculationType.FAST, false);
            boolean value = distance < Config.SoundApproachDistance.getValue();
            approachSoundCompleted = value;
            CB.switchToCompassCompleted = value;
        } else {
            approachSoundCompleted = true;
            CB.switchToCompassCompleted = true;
        }

    }


    @Override
    public void Fix() {
        PlaySounds = !Config.GlobalVolume.getValue().Mute;

//        try {
//
//            if (!initialFixSoundCompleted && Locator.isGPSprovided() && GPS.getFixedSats() > 3) {
//
//                log.debug("Play Fix");
//                if (PlaySounds)
//                    SoundCache.play(SoundCache.Sounds.GPS_fix);
//                initialFixSoundCompleted = true;
//                loseSoundCompleated = false;
//
//            }
//        } catch (Exception e) {
//            log.error("Global.PlaySound(GPS_Fix.ogg)", e);
//            e.printStackTrace();
//        }

    }

    boolean loseSoundCompleated = false;

    @Override
    public void FallBackToNetworkProvider() {
        PlaySounds = !Config.GlobalVolume.getValue().Mute;

        if (initialFixSoundCompleted && !loseSoundCompleated) {

            if (PlaySounds)
                SoundCache.play(SoundCache.Sounds.GPS_lose);

            loseSoundCompleated = true;
            initialFixSoundCompleted = false;
        }
        CB.viewmanager.toast("Network-Position", ViewManager.ToastLength.LONG);
    }

}
