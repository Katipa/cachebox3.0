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
package de.longri.cachebox3.gpx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import de.longri.cachebox3.TestUtils;
import de.longri.cachebox3.sqlite.Database;
import de.longri.cachebox3.types.AbstractCache;
import de.longri.cachebox3.types.test_caches.TEST_CACHES;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Longri on 29.03.2018.
 */
class GpxFileImporterTest {

    static {
        TestUtils.initialGdx();
    }

    @Test
    public void testGpxStreamImport() throws Exception {
        long start = System.currentTimeMillis();

        Database TEST_DB = TestUtils.getTestDB(true);
        FileHandle gpxFile = TestUtils.getResourceFileHandle("testsResources/gpx/GC2T9RW.gpx");
        final AtomicInteger cacheCount = new AtomicInteger();
        final AtomicInteger waypointCount = new AtomicInteger();
        final AtomicInteger logCount = new AtomicInteger();
        final Array<String> mysteryList = new Array<>();
        ImportHandler importHandler = new ImportHandler() {
            @Override
            public void incrementCaches(String mysteryGcCode) {
                cacheCount.incrementAndGet();
                if (mysteryGcCode != null) mysteryList.add(mysteryGcCode);
            }

            @Override
            public void incrementWaypoints() {
                waypointCount.incrementAndGet();
            }

            @Override
            public void incrementLogs() {
                logCount.incrementAndGet();
            }
        };

        new GroundspeakGpxStreamImporter(TEST_DB, importHandler).doImport(gpxFile);
        assertThat("Cache count must be 1", TEST_DB.getCacheCountOnThisDB() == 1);
        AbstractCache cache = TEST_DB.getFromDbByGcCode("GC2T9RW", true);
        TEST_CACHES.GC2T9RW.assertCache(cache, TEST_DB);

        TEST_DB.close();

        assertEquals(cacheCount.get(), 1, "Imported Cache count is wrong");
        assertEquals(waypointCount.get(), 1, "Imported Waypoint count is wrong");
        assertEquals(logCount.get(), 20, "Imported Log count is wrong");
        assertEquals(mysteryList.size, 0, "Imported Mystery count is Wrong");

        long elapseTime = System.currentTimeMillis() - start;
        System.out.println("Gpx Stream import time: " + elapseTime + "ms");
    }

    @Test
    public void testGpxStreamImport_GC52BKF() throws Exception {
        long start = System.currentTimeMillis();

        Database TEST_DB = TestUtils.getTestDB(true);
        FileHandle gpxFile = TestUtils.getResourceFileHandle("testsResources/gpx/GC52BKF.gpx");
        final AtomicInteger cacheCount = new AtomicInteger();
        final AtomicInteger waypointCount = new AtomicInteger();
        final AtomicInteger logCount = new AtomicInteger();
        final Array<String> mysteryList = new Array<>();
        ImportHandler importHandler = new ImportHandler() {
            @Override
            public void incrementCaches(String mysteryGcCode) {
                cacheCount.incrementAndGet();
                if (mysteryGcCode != null) mysteryList.add(mysteryGcCode);
            }

            @Override
            public void incrementWaypoints() {
                waypointCount.incrementAndGet();
            }

            @Override
            public void incrementLogs() {
                logCount.incrementAndGet();
            }
        };

        new GroundspeakGpxStreamImporter(TEST_DB, importHandler).doImport(gpxFile);
        assertThat("Cache count must be 1", TEST_DB.getCacheCountOnThisDB() == 1);
        AbstractCache cache = TEST_DB.getFromDbByGcCode("GC52BKF", true);
        TEST_CACHES.GC52BKF.assertCache(cache, TEST_DB);

        TEST_DB.close();

        assertEquals(cacheCount.get(), 1, "Imported Cache count is wrong");
        assertEquals(waypointCount.get(), 0, "Imported Waypoint count is wrong");
        assertEquals(logCount.get(), 20, "Imported Log count is wrong");
        assertEquals(mysteryList.size, 0, "Imported Mystery count is Wrong");

        long elapseTime = System.currentTimeMillis() - start;
        System.out.println("Gpx Stream import time: " + elapseTime + "ms");
    }

    @Test
    public void testPqStreamImport() throws Exception {
        long start = System.currentTimeMillis();

        Database TEST_DB = TestUtils.getTestDB(true);
        FileHandle gpxFile = TestUtils.getResourceFileHandle("testsResources/gpx/GS_PQ/6004539_HomeZone.gpx");
        FileHandle gpxFile2 = TestUtils.getResourceFileHandle("testsResources/gpx/GS_PQ/6004539_HomeZone-wpts.gpx");
        final AtomicInteger cacheCount = new AtomicInteger();
        final AtomicInteger waypointCount = new AtomicInteger();
        final AtomicInteger logCount = new AtomicInteger();
        final Array<String> mysteryList = new Array<>();
        ImportHandler importHandler = new ImportHandler() {
            @Override
            public void incrementCaches(String mysteryGcCode) {
                cacheCount.incrementAndGet();
                if (mysteryGcCode != null) mysteryList.add(mysteryGcCode);
            }

            @Override
            public void incrementWaypoints() {
                waypointCount.incrementAndGet();
            }

            @Override
            public void incrementLogs() {
                logCount.incrementAndGet();
            }
        };

        GroundspeakGpxStreamImporter importer = new GroundspeakGpxStreamImporter(TEST_DB, importHandler);
        importer.doImport(gpxFile);
        importer.doImport(gpxFile2);


        assertThat("Cache count must be 500", TEST_DB.getCacheCountOnThisDB() == 500);
        AbstractCache cache = TEST_DB.getFromDbByGcCode("GC2TNPV", true);
        TEST_CACHES.GC2TNPV.assertCache(cache, TEST_DB);

        cache = TEST_DB.getFromDbByGcCode("GCV272", true);
        TEST_CACHES.GCV272.assertCache(cache, TEST_DB);


        TEST_DB.close();

        assertEquals(cacheCount.get(), 500, "Imported Cache count is wrong");
        assertEquals(waypointCount.get(), 183, "Imported Waypoint count is wrong");
        assertEquals(logCount.get(), 2534, "Imported Log count is wrong");
        assertEquals(mysteryList.size, 167, "Imported Mystery count is Wrong");

        long elapseTime = System.currentTimeMillis() - start;
        System.out.println("PQ Stream import time: " + elapseTime + "ms");
    }
}