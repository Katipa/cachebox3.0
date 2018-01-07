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
package de.longri.cachebox3.sqlite.dao;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import de.longri.cachebox3.TestUtils;
import de.longri.cachebox3.sqlite.Database;
import de.longri.cachebox3.types.AbstractWaypoint;
import de.longri.cachebox3.types.CacheTypes;
import de.longri.cachebox3.types.ImmutableWaypoint;
import de.longri.cachebox3.types.MutableWaypoint;
import de.longri.gdx.sqlite.GdxSqliteCursor;
import de.longri.gdx.sqlite.SQLiteGdxException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Longri on 21.10.2017.
 */
class Waypoint3DAOTest {

    static FileHandle testDbFileHandle;
    static FileHandle copyDbFileHandle;
    static Database cb3Database;

    @BeforeAll
    static void beforeAll() throws SQLiteGdxException {

        TestUtils.initialGdx();

        // copy testDb
        testDbFileHandle = TestUtils.getResourceFileHandle("testsResources/Database/testACB2.db3");
        copyDbFileHandle = testDbFileHandle.parent().child("testWaypointDAO.db3");
        if (copyDbFileHandle.exists()) {
            // delete first
            assertThat("TestDB must be deleted for cleanup", copyDbFileHandle.delete());
        }
        testDbFileHandle.copyTo(copyDbFileHandle);
        assertThat("TestDB must exist", copyDbFileHandle.exists());

        // open DataBase
        cb3Database = new Database(Database.DatabaseType.CacheBox3);
        cb3Database.startUp(copyDbFileHandle);

    }

    @AfterAll
    static void cleanUpRecources() {
        cb3Database.close();
        assertThat("TestDB must be deleted after cleanup", copyDbFileHandle.delete());
    }


    @Test
    void getWaypointsFromCacheID() {

        Waypoint3DAO dao = new Waypoint3DAO();
        Array<AbstractWaypoint> waypoints = dao.getWaypointsFromCacheID(cb3Database, null, true);
        assertThat("TestDB must have 282 Waypoints but has:" + waypoints.size, waypoints.size == 282);

        waypoints = dao.getWaypointsFromCacheID(cb3Database, 20919627218633543L, true);
        assertThat("TestDB must have 6 Waypoints but has:" + waypoints.size, waypoints.size == 6);

        AbstractWaypoint wp = waypoints.get(1);
        assertThat("Waypoint.GcCode must be 'S23EJRJ' but was: '" + wp.getGcCode() + "'", wp.getGcCode().equals("S23EJRJ"));
        assertThat("Waypoint.Latitude must be '52.507768' but was: '" + TestUtils.roundDoubleCoordinate(wp.getLatitude()) + "'", TestUtils.roundDoubleCoordinate(wp.getLatitude()) == 52.507768);
        assertThat("Waypoint.Longitude must be '13.465333' but was: '" + TestUtils.roundDoubleCoordinate(wp.getLongitude()) + "'", TestUtils.roundDoubleCoordinate(wp.getLongitude()) == 13.465333);
        assertThat("Waypoint.Type must be 'Question to Answer' but was: '" + wp.getType() + "'", wp.getType() == CacheTypes.MultiQuestion);
        assertThat("Waypoint.Start must be 'false' but was: '" + wp.isStart() + "'", !wp.isStart());
        assertThat("Waypoint.SyncExcluded must be 'false' but was: '" + wp.isSyncExcluded() + "'", !wp.isSyncExcluded());
        assertThat("Waypoint.UserWaypoint must be 'false' but was: '" + wp.isUserWaypoint() + "'", !wp.isUserWaypoint());
        assertThat("Waypoint.Title must be 'Stage 2' but was: '" + wp.getTitle() + "'", wp.getTitle().equals("Stage 2"));

        assertThat("Waypoint.Description must be 'Wohin geht der Mann im Hauseingang Nr. 9? Der erste Buchstabe wird gesucht' but was: '" + wp.getDescription(cb3Database) + "'", wp.getDescription(cb3Database).equals("Wohin geht der Mann im Hauseingang Nr. 9? Der erste Buchstabe wird gesucht"));
        assertThat("Waypoint.Clue must be '' but was: '" + wp.getClue(cb3Database) + "'", wp.getClue(cb3Database).equals(""));

    }


    @Test
    void exceptionThrowing() {
        //ImmutableWaypoint class must throw a Exception with set properties

        AbstractWaypoint wp = new ImmutableWaypoint(should_Latitude, should_Longitude);
        boolean hasThrowed = false;
        try {
            wp.setCacheId(should_cacheId);
        } catch (Exception e) {
            hasThrowed = true;
        } finally {
            assertThat("Set CacheID must throw a RuntimeException", hasThrowed);
        }

        hasThrowed = false;
        try {
            wp.setLatLon(should_Latitude, should_Longitude);
        } catch (Exception e) {
            hasThrowed = true;
        } finally {
            assertThat("Set LatLon must throw a RuntimeException", hasThrowed);
        }


        hasThrowed = false;
        try {
            wp.setGcCode(should_GcCode);
        } catch (Exception e) {
            hasThrowed = true;
        } finally {
            assertThat("Set GcCode must throw a RuntimeException", hasThrowed);
        }

        hasThrowed = false;
        try {
            wp.setTitle(should_Title);
        } catch (Exception e) {
            hasThrowed = true;
        } finally {
            assertThat("Set Title must throw a RuntimeException", hasThrowed);
        }

        hasThrowed = false;
        try {
            wp.setType(should_Type);
        } catch (Exception e) {
            hasThrowed = true;
        } finally {
            assertThat("Set Type must throw a RuntimeException", hasThrowed);
        }

        hasThrowed = false;
        try {
            wp.setStart(should_isStart);
        } catch (Exception e) {
            hasThrowed = true;
        } finally {
            assertThat("Set Start must throw a RuntimeException", hasThrowed);
        }

        hasThrowed = false;
        try {
            wp.setSyncExcluded(should_syncExclude);
        } catch (Exception e) {
            hasThrowed = true;
        } finally {
            assertThat("Set SyncExclude must throw a RuntimeException", hasThrowed);
        }

        hasThrowed = false;
        try {
            wp.setUserWaypoint(should_userWaypoint);
        } catch (Exception e) {
            hasThrowed = true;
        } finally {
            assertThat("Set UserWaypoint must throw a RuntimeException", hasThrowed);
        }

        hasThrowed = false;
        try {
            wp.setUserWaypoint(should_userWaypoint);
        } catch (Exception e) {
            hasThrowed = true;
        } finally {
            assertThat("Set UserWaypoint must throw a RuntimeException", hasThrowed);
        }
    }


    private final double should_Latitude = 53.123;
    private final double should_Longitude = 13.456;
    private final long should_cacheId = 1234567890L;
    private final String should_GcCode = "GCCCCCX";
    private final String should_Title = "Waypoint-Title";
    private final CacheTypes should_Type = CacheTypes.MultiQuestion;
    private final boolean should_isStart = true;
    private final boolean should_syncExclude = true;
    private final boolean should_userWaypoint = true;
    private final String should_Description = " Waypoint description";
    private final String should_Clue = " Waypoint clue";

    private final double should2_Latitude = 53.456;
    private final double should2_Longitude = 13.789;
    private final String should2_Title = "Waypoint-Title Updated";
    private final CacheTypes should2_Type = CacheTypes.CITO;
    private final boolean should2_isStart = false;
    private final boolean should2_syncExclude = false;
    private final boolean should2_userWaypoint = false;
    private final String should2_Description = " Waypoint updated description";
    private final String should2_Clue = " Waypoint updated clue";


    @Test
    void writeToDatabase() {
        //1. write new wp to DB
        //2. update wp
        //3. delete wp

//1. write new wp to DB -------------------------------------------------------------------        
        AbstractWaypoint wp = new MutableWaypoint(0, 0, should_cacheId);

        wp.setLatLon(should_Latitude, should_Longitude);
        wp.setGcCode(should_GcCode);
        wp.setTitle(should_Title);
        wp.setType(should_Type);
        wp.setStart(should_isStart);
        wp.setSyncExcluded(should_syncExclude);
        wp.setUserWaypoint(should_userWaypoint);
        wp.setDescription(should_Description);
        wp.setClue(should_Clue);
        assertWp("MutableWaypoint", wp);

        Waypoint3DAO DAO = new Waypoint3DAO();
        DAO.writeToDatabase(cb3Database, wp);

        Array<AbstractWaypoint> waypoints = DAO.getWaypointsFromCacheID(cb3Database, should_cacheId, true);
        AbstractWaypoint wp2 = waypoints.get(0);
        assertWp("StoredWaypoint", wp2);


//2. update wp -----------------------------------------------------------------------------  

        if (!wp2.isMutable()) {
            wp2 = wp2.getMutable(cb3Database);
        }

        wp2.setLatLon(should2_Latitude, should2_Longitude);
        wp2.setTitle(should2_Title);
        wp2.setType(should2_Type);
        wp2.setStart(should2_isStart);
        wp2.setSyncExcluded(should2_syncExclude);
        wp2.setUserWaypoint(should2_userWaypoint);
        wp2.setDescription(should2_Description);
        wp2.setClue(should2_Clue);
        assertWp2("ChangedWaypoint", wp2);

        DAO.updateDatabase(cb3Database, wp2);

        Array<AbstractWaypoint> waypoints2 = DAO.getWaypointsFromCacheID(cb3Database, should_cacheId, true);
        AbstractWaypoint wp3 = waypoints2.get(0);
        assertWp2("updatedWaypoint", wp3);

//3. delete wp -----------------------------------------------------------------------------

        DAO.delete(cb3Database, wp3);
        Array<AbstractWaypoint> waypoints3 = DAO.getWaypointsFromCacheID(cb3Database, should_cacheId, true);
        assertThat("Waypoint list must be empty", waypoints3.size == 0);

        //check is also deleted from WaypointsText table
        GdxSqliteCursor cursor = cb3Database.rawQuery("SELECT * FROM WaypointsText WHERE GcCode='GCCCCCX'", null);
        cursor.moveToFirst();
        assertThat("Waypoint must also deleted from WaypointsText table", cursor.isAfterLast());

    }

    private void assertWp(String msg, AbstractWaypoint wp) {
        assertThat(msg + " Id must equals", wp.getCacheId() == should_cacheId);
        assertThat(msg + " Latitude must equals", TestUtils.roundDoubleCoordinate(wp.getLatitude()) == should_Latitude);
        assertThat(msg + " Longitude must equals", TestUtils.roundDoubleCoordinate(wp.getLongitude()) == should_Longitude);
        assertThat(msg + " GcCode must equals", wp.getGcCode().equals(should_GcCode));
        assertThat(msg + " Type must equals", wp.getType() == should_Type);
        assertThat(msg + " IsStart must equals", wp.isStart() == should_isStart);
        assertThat(msg + " SyncExclude must equals", wp.isSyncExcluded() == should_syncExclude);
        assertThat(msg + " IsUserWaypoint must equals", wp.isUserWaypoint() == should_userWaypoint);
        assertThat(msg + " Title must equals", wp.getTitle().equals(should_Title));
        assertThat(msg + " Description must equals", wp.getDescription(cb3Database).equals(should_Description));
        assertThat(msg + " Clue must equals", wp.getClue(cb3Database).equals(should_Clue));
    }

    private void assertWp2(String msg, AbstractWaypoint wp) {
        assertThat(msg + " Id must equals", wp.getCacheId() == should_cacheId);
        assertThat(msg + " Latitude must equals", TestUtils.roundDoubleCoordinate(wp.getLatitude()) == should2_Latitude);
        assertThat(msg + " Longitude must equals", TestUtils.roundDoubleCoordinate(wp.getLongitude()) == should2_Longitude);
        assertThat(msg + " GcCode must equals", wp.getGcCode().equals(should_GcCode));
        assertThat(msg + " Type must equals", wp.getType() == should2_Type);
        assertThat(msg + " IsStart must equals", wp.isStart() == should2_isStart);
        assertThat(msg + " SyncExclude must equals", wp.isSyncExcluded() == should2_syncExclude);
        assertThat(msg + " IsUserWaypoint must equals", wp.isUserWaypoint() == should2_userWaypoint);
        assertThat(msg + " Title must equals", wp.getTitle().equals(should2_Title));
        assertThat(msg + " Description must equals", wp.getDescription(cb3Database).equals(should2_Description));
        assertThat(msg + " Clue must equals", wp.getClue(cb3Database).equals(should2_Clue));
    }


    @Test
    void resetStartWaypoint() {
    }


}