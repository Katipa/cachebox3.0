/*
 * Copyright (C) 2017 - 2018 team-cachebox.de
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
package de.longri.cachebox3.types;

import com.badlogic.gdx.utils.Array;
import de.longri.cachebox3.gui.utils.CharSequenceArray;
import de.longri.cachebox3.sqlite.Database;
import de.longri.cachebox3.sqlite.dao.DaoFactory;
import de.longri.gdx.sqlite.GdxSqliteCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Longri on 17.10.2017.
 */
public class ImmutableCache extends AbstractCache {

    // ########################################################
    // Boolean Handling
    // one Boolean use up to 4 Bytes
    // Boolean data type represents one bit of information, but its "size" isn't something that's precisely defined. (Oracle Docs)
    //
    // so we use one Short for Store all Boolean and Use a BitMask
    // ########################################################

    // Masks
    public final static short MASK_HAS_HINT = 1 << 0;
    public final static short MASK_CORECTED_COORDS = 1 << 1; //   2
    public final static short MASK_ARCHIVED = 1 << 2;        //   4
    public final static short MASK_AVAILABLE = 1 << 3;       //   8
    public final static short MASK_FAVORITE = 1 << 4;        //  16
    public final static short MASK_FOUND = 1 << 5;           //  32
    public final static short MASK_IS_LIVE = 1 << 6;         //  64
    public final static short MASK_SOLVER1CHANGED = 1 << 7;  // 128
    public final static short MASK_HAS_USER_DATA = 1 << 8;   // 256
    public final static short MASK_LISTING_CHANGED = 1 << 9; // 512
    private Array<AbstractWaypoint> waypoints;


    public ImmutableCache(double latitude, double longitude) {
        super(latitude, longitude);
        this.id = 0;
        short sizeOrigin = 0;
        this.size = CacheSizes.parseInt(sizeOrigin);
        this.difficulty = 0f;
        this.terrain = 0f;
        short typeOrigin = 0;
        this.type = CacheTypes.get(typeOrigin);
        this.rating = 0;
        this.numTravelbugs = 0;
        this.gcCode = "";
        this.name = "";
        this.placedBy = "";
        this.owner = "";
        this.gcId = "";
        this.booleanStore = 0;
        this.favPoints = 0;
    }

    public ImmutableCache(AbstractCache cache) {
        super(cache.getLatitude(), cache.getLongitude());
        this.name = cache.getName() == null ? null : cache.getName().toString();
        this.gcCode = cache.getGcCode() == null ? null : cache.getGcCode().toString();
        this.placedBy = cache.getPlacedBy() == null ? null : cache.getPlacedBy().toString();
        this.owner = cache.getOwner() == null ? null : cache.getOwner().toString();
        this.gcId = cache.getGcId() == null ? null : cache.getGcId().toString();
        this.rating = (short) (cache.getRating() * 2);
        this.favPoints = cache.getFavoritePoints();
        this.id = cache.getId();
        this.type = cache.getType();
        this.size = cache.getSize();
        this.difficulty = cache.getDifficulty();
        this.terrain = cache.getTerrain();
        this.waypoints = cache.getWaypoints();
        this.numTravelbugs = (short) cache.getNumTravelbugs();

        short bitStore = 0;
        bitStore = setMaskValue(MASK_HAS_HINT, cache.hasHint(), bitStore);
        bitStore = setMaskValue(MASK_ARCHIVED, cache.isArchived(), bitStore);
        bitStore = setMaskValue(MASK_AVAILABLE, cache.isAvailable(), bitStore);
        bitStore = setMaskValue(MASK_FAVORITE, cache.isFavorite(), bitStore);
        bitStore = setMaskValue(MASK_FOUND, cache.isFound(), bitStore);
        bitStore = setMaskValue(MASK_HAS_USER_DATA, cache.isHasUserData(), bitStore);
        bitStore = setMaskValue(MASK_LISTING_CHANGED, cache.isListingChanged(), bitStore);
        bitStore = setMaskValue(MASK_CORECTED_COORDS, cache.hasCorrectedCoordinates(), bitStore);
        this.booleanStore = bitStore;
    }


    public static boolean getMaskValue(short mask, short bitFlags) {
        return (bitFlags & mask) == mask;
    }

    public static short setMaskValue(short mask, boolean value, short bitFlags) {
        if (getMaskValue(mask, bitFlags) == value) {
            return bitFlags;
        }

        if (value) {
            bitFlags |= mask;
        } else {
            bitFlags &= ~mask;
        }
        return bitFlags;
    }


    private final static Logger log = LoggerFactory.getLogger(ImmutableCache.class);

    private final CharSequence name, gcCode, placedBy, owner, gcId;
    private short rating, numTravelbugs, booleanStore;
    private final int favPoints;
    private final long id;
    private final CacheTypes type;
    private final CacheSizes size;
    private final float difficulty, terrain;


    public ImmutableCache(GdxSqliteCursor cursor) {
        super(cursor.getDouble(1), cursor.getDouble(2));
        this.id = cursor.getLong(0);
        this.size = CacheSizes.parseInt(cursor.getShort(3));
        this.difficulty = (float) cursor.getShort(4) / 2.0f;
        this.terrain = (float) cursor.getShort(5) / 2.0f;
        this.type = CacheTypes.get(cursor.getShort(6));
        this.rating = (short) (cursor.getShort(7) / 100);
        this.numTravelbugs = cursor.getShort(8);

        this.gcCode = new CharSequenceArray(cursor.getString(9));

        String nameString = cursor.getString(10);
        if (nameString != null) this.name = new CharSequenceArray(nameString.trim());
        else this.name = null;

        String placedByString = cursor.getString(11);
        if (placedByString != null) this.placedBy = new CharSequenceArray(placedByString);
        else this.placedBy = null;

        String ownerString = cursor.getString(12);
        if (ownerString != null) this.owner = new CharSequenceArray(ownerString);
        else this.owner = null;

        String gcIdString = cursor.getString(13);
        if (gcIdString != null) this.gcId = new CharSequenceArray(gcIdString);
        else this.gcId = null;

        this.booleanStore = cursor.getShort(14);
        this.favPoints = cursor.getInt(15);
    }

    public ImmutableCache(Object[] values) {
        super((double) values[1], (double) values[2]);
        this.id = (long) values[0];
        this.size = CacheSizes.parseInt(((Long) values[3]).intValue());
        this.difficulty = (float) ((Long) values[4]) / 2.0f;
        this.terrain = (float) ((Long) values[5]) / 2.0f;
        this.type = CacheTypes.get(((Long) values[6]).intValue());
        this.rating = (short) (((Long) values[7]).shortValue() / 100);
        this.numTravelbugs = ((Long) values[8]).shortValue();

        this.gcCode = new CharSequenceArray((String) values[9]);
        this.name = new CharSequenceArray((String) values[10]);
        this.placedBy = new CharSequenceArray((String) values[11]);
        this.owner = new CharSequenceArray((String) values[12]);
        this.gcId = new CharSequenceArray((String) values[13]);

        this.booleanStore = ((Long) values[14]).shortValue();
        this.favPoints = values[15] == null ? 0 : ((Long) values[15]).intValue();

    }

    //################################################################################
    //# properties retained at the class
    ///###############################################################################

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public CharSequence getName() {
        return name;
    }

    @Override
    public CacheSizes getSize() {
        return size;
    }

    @Override
    public float getDifficulty() {
        return difficulty;
    }

    @Override
    public float getTerrain() {
        return terrain;
    }

    @Override
    public CacheTypes getType() {
        return type;
    }

    @Override
    public CharSequence getGcCode() {
        return gcCode;
    }

    @Override
    public CharSequence getPlacedBy() {
        return placedBy;
    }

    @Override
    public CharSequence getOwner() {
        return owner;
    }

    @Override
    public CharSequence getGcId() {
        return gcId;
    }

    @Override
    public boolean isAvailable() {
        return this.getMaskValue(MASK_AVAILABLE);
    }

    @Override
    public boolean isFound() {
        return this.getMaskValue(MASK_FOUND);
    }

    @Override
    public boolean isHasUserData() {
        return this.getMaskValue(MASK_HAS_USER_DATA);
    }

    @Override
    public int getNumTravelbugs() {
        return numTravelbugs;
    }

    @Override
    public boolean isArchived() {
        return this.getMaskValue(MASK_ARCHIVED);
    }

    @Override
    public boolean hasCorrectedCoordinates() {
        return this.getMaskValue(MASK_CORECTED_COORDS);
    }

    @Override
    public boolean isFavorite() {
        return this.getMaskValue(MASK_FAVORITE);
    }

    @Override
    public boolean hasHint() {
        return this.getMaskValue(MASK_HAS_HINT);
    }

    @Override
    public boolean isListingChanged() {
        return this.getMaskValue(MASK_LISTING_CHANGED);
    }


    private boolean getMaskValue(short mask) {
        return getMaskValue(mask, booleanStore);
    }

    @Override
    public Array<AbstractWaypoint> getWaypoints() {
        return this.waypoints;
    }


    @Override
    public String getCountry(Database database) {
        return getStringFromDB(database, "SELECT country FROM CacheInfo WHERE Id=?");
    }

    @Override
    public String getState(Database database) {
        return getStringFromDB(database, "SELECT state FROM CacheInfo WHERE Id=?");
    }


    @Override
    public Date getDateHidden(Database database) {
        String dateString = getStringFromDB(database, "SELECT DateHidden FROM CacheInfo WHERE Id=?");
        if (dateString == null || dateString.isEmpty()) return new Date();

        try {
            return Database.cbDbFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    @Override
    public String getTmpNote(Database database) {
        return getStringFromDB(database, "SELECT Notes FROM CacheText WHERE Id=?");
    }

    @Override
    public String getTmpSolver(Database database) {
        return getStringFromDB(database, "SELECT Solver FROM CacheText WHERE Id=?");
    }


    @Override
    public void setWaypoints(Array<AbstractWaypoint> waypoints) {
        this.waypoints = waypoints;
    }

    //################################################################################
    //# method's that throws exceptions
    ///###############################################################################

    private void throwNotChangeable(String propertyName) {
        throw new RuntimeException("'" + propertyName + "' is not changeable! Use MutableCache.class instead of ImmutableCache.class");
    }

    @Override
    public void setId(long id) {
        throwNotChangeable("Id");
    }

    @Override
    public void setName(String name) {
        throwNotChangeable("Name");
    }

    @Override
    public void setSize(CacheSizes size) {
        throwNotChangeable("Size");
    }

    @Override
    public void setDifficulty(float difficulty) {
        throwNotChangeable("Difficulty");
    }

    @Override
    public void setTerrain(float terrain) {
        throwNotChangeable("Terrain");
    }

    @Override
    public void setType(CacheTypes type) {
        throwNotChangeable("Type");
    }

    @Override
    public void setGcCode(String gcCode) {
        throwNotChangeable("GcCode");
    }

    @Override
    public void setPlacedBy(String value) {
        throwNotChangeable("PlacedBy");
    }

    @Override
    public void setOwner(String owner) {
        throwNotChangeable("Owner");
    }

    @Override
    public void setGcId(String gcId) {
        throwNotChangeable("GcId");
    }

    @Override
    public void setAvailable(boolean available) {
        throwNotChangeable("Available");
    }

    @Override
    public void setHasUserData(boolean hasUserData) {
        throwNotChangeable("HasUserData");
    }

    @Override
    public void setNumTravelbugs(int numTravelbugs) {
        throwNotChangeable("NumTravelbugs");
    }

    @Override
    public void setArchived(boolean archived) {
        throwNotChangeable("Archived");
    }

    @Override
    public void setHasCorrectedCoordinates(boolean correctedCoordinates) {
        throwNotChangeable("hasCorrectedCoordinates");
    }

    @Override
    public void setLatLon(double latitude, double longitude) {
        throwNotChangeable("LatLon");
    }

    @Override
    public void setAttributesPositive(DLong dLong) {
        throwNotChangeable("AttributesPositive");
    }

    @Override
    public void setAttributesNegative(DLong dLong) {
        throwNotChangeable("AttributesNegative");
    }

    @Override
    public void setAttributes(Array<Attributes> attributes) {
        throwNotChangeable("Attributes");
    }

    @Override
    public void setDateHidden(Date date) {
        throwNotChangeable("DateHidden");
    }

    @Override
    public void setRating(float rating) {
        throwNotChangeable("Rating");
    }

    @Override
    public void setFavoritePoints(int value) {
        throwNotChangeable("FavoritePoints");
    }

    @Override
    public void setHasHint(boolean hasHint) {
        throwNotChangeable("HasHint");
    }

    @Override
    public void setShortDescription(Database database, String value) {
        throwNotChangeable("ShortDescription");
    }

    @Override
    public void setLongDescription(Database database, String value) {
        throwNotChangeable("LongDescription");
    }

    @Override
    public void setHint(Database database, String hint) {
        throwNotChangeable("Hint");
    }

    @Override
    public void setListingChanged(boolean listingChanged) {
        throwNotChangeable("ListingChanged");
    }

    @Override
    public void setLatitude(double latitude) {
        throwNotChangeable("Latitude");
    }

    @Override
    public void setLongitude(double longitude) {
        throwNotChangeable("Longitude");
    }

    @Override
    public void setUrl(String value) {
        throwNotChangeable("Url");
    }

    @Override
    public void setState(String value) {
        throwNotChangeable("State");
    }

    @Override
    public void setCountry(String value) {
        throwNotChangeable("Country");
    }


    @Override
    public void setTmpNote(String value) {
        throwNotChangeable("TempNote");
    }

    @Override
    public void setTmpSolver(Database database,String value) {
            throwNotChangeable("TempSolver");
    }

    //################################################################################
    //# properties that not retained at the class but read/write directly from/to DB
    ///###############################################################################


    @Override
    public Array<Attributes> getAttributes(Database database) {
        GdxSqliteCursor cursor = database.rawQuery("SELECT * FROM Attributes WHERE Id=?", new String[]{String.valueOf(this.id)});
        DLong attributesPositive;
        DLong attributesNegative;
        if (cursor != null) {
            cursor.moveToFirst();
            if (!cursor.isNull(1)) {
                attributesPositive = new DLong(cursor.getLong(3), cursor.getLong(1));
                attributesNegative = new DLong(cursor.getLong(4), cursor.getLong(2));
            } else {
                attributesPositive = new DLong(0, 0);
                attributesNegative = new DLong(0, 0);
            }
            cursor.close();
        } else {
            attributesPositive = new DLong(0, 0);
            attributesNegative = new DLong(0, 0);
        }
        return Attributes.getAttributes(attributesPositive, attributesNegative);
    }

    @Override
    public String getLongDescription(Database database) {
        return getStringFromDB(database, "SELECT Description FROM CacheText WHERE Id=?");
    }


    @Override
    public String getShortDescription(Database database) {
        return getStringFromDB(database, "SELECT ShortDescription FROM CacheText WHERE Id=?");
    }

    @Override
    public CharSequence getHint(Database database) {
        return getCharSequenceFromDB(database, "SELECT Hint FROM CacheText WHERE Id=?");
    }

    @Override
    public String getUrl(Database database) {
        return getStringFromDB(database, "SELECT Url FROM CacheText WHERE Id=?");
    }

    private CharSequence getCharSequenceFromDB(Database database, String statement) {
        String[] args = new String[]{Long.toString(this.id)};
        return database.getCharSequence(statement, args);
    }

    private String getStringFromDB(Database database, String statement) {
        String[] args = new String[]{Long.toString(this.id)};
        return database.getString(statement, args);
    }

    @Override
    public void setFound(Database database, boolean found) {
        //write direct to DB
        booleanStore = ImmutableCache.setMaskValue(ImmutableCache.MASK_FOUND, found, booleanStore);
        DaoFactory.CACHE_DAO.writeCacheBooleanStore(database, booleanStore, getId());
    }

    @Override
    public void setFavorite(Database database, boolean favorite) {
        //write direct to DB
        booleanStore = ImmutableCache.setMaskValue(ImmutableCache.MASK_FAVORITE, favorite, booleanStore);
        DaoFactory.CACHE_DAO.writeCacheBooleanStore(database, booleanStore, getId());
    }


    //################################################################################
    //# not tested now
    ///###############################################################################

    @Override
    public boolean ImTheOwner() {
        return false;
    }

    @Override
    public boolean CorrectedCoordiantesOrMysterySolved() {
        return false;
    }

    @Override
    public boolean HasFinalWaypoint() {
        return false;
    }

    @Override
    public AbstractWaypoint GetFinalWaypoint() {
        return null;
    }

    @Override
    public boolean HasStartWaypoint() {
        return false;
    }

    @Override
    public AbstractWaypoint GetStartWaypoint() {
        return null;
    }

    @Override
    public boolean hasSpoiler() {
        return false;
    }

    @Override
    public int compareTo(AbstractCache c2) {
        return 0;
    }

    @Override
    protected AbstractWaypoint findWaypointByGc(String gc) {
        return null;
    }


    @Override
    public long getGPXFilename_ID() {
        return 0;
    }

    @Override
    public void setGPXFilename_ID(long gpxFilenameId) {

    }

    @Override
    public boolean isLive() {
        return false;
    }

    @Override
    public void setLive(boolean isLive) {

    }

    @Override
    public byte getApiState(Database database) {
        GdxSqliteCursor cursor = database.myDB.rawQuery("SELECT ApiStatus FROM CacheInfo WHERE Id='" + this.id + "'");
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor.getByte(0);
        }
        return 0;
    }

    @Override
    public void setApiState(byte value) {

    }

    @Override
    public int getNoteChecksum() {
        return 0;
    }

    @Override
    public void setNoteChecksum(int value) {

    }


    @Override
    public int getSolverChecksum() {
        return 0;
    }

    @Override
    public void setSolverChecksum(int value) {

    }

    @Override
    public void addAttributeNegative(Attributes attribute) {

    }

    @Override
    public void addAttributePositive(Attributes attribute) {

    }

    @Override
    public DLong getAttributesPositive() {
        return null;
    }

    @Override
    public DLong getAttributesNegative() {
        return null;
    }


    @Override
    public void setTourName(String value) {

    }

    @Override
    public String getTourName() {
        return null;
    }

    @Override
    public boolean isAttributePositiveSet(Attributes attribute) {
        throw new RuntimeException("not Stored on ImmutableCache.class");
    }

    @Override
    public boolean isAttributeNegativeSet(Attributes attribute) {
        throw new RuntimeException("not Stored on ImmutableCache.class");
    }

    @Override
    public int getFavoritePoints() {
        return this.favPoints;
    }


    @Override
    public float getRating() {
        return this.rating / 2f;
    }


    @Override
    public void dispose() {

    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public MutableCache getMutable(Database database) {
        return new MutableCache(database, this);
    }

    @Override
    public AbstractCache getImmutable() {
        return this;
    }

    @Override
    public AbstractCache getCopy() {
        return new ImmutableCache(this);
    }


    @Override
    public short getBooleanStore() {
        return this.booleanStore;
    }

}
