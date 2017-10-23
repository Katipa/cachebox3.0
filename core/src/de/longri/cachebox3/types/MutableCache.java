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
package de.longri.cachebox3.types;

import com.badlogic.gdx.utils.Array;
import de.longri.cachebox3.locator.Coordinate;
import de.longri.cachebox3.sqlite.Database;
import de.longri.cachebox3.utils.MathUtils;

import java.util.Date;

/**
 * Created by Longri on 23.10.2017.
 */
public class MutableCache extends AbstractCache {

    private double latitude, longitude;
    private Array<Attributes> attributes;
    private String name, gcCode, placedBy, owner, gcId;
    private short rating, numTravelbugs;
    private int favPoints;
    private long id;
    private CacheTypes type;
    private CacheSizes size;
    private float difficulty, terrain;

    private boolean hasHint;
    private boolean correctedCoordinates;
    private boolean archived;
    private boolean available;
    private boolean favorite;
    private boolean found;
    private boolean userData;
    private boolean listingChanged;
    private Array<AbstractWaypoint> waypoints;
    private String longDescription;
    private String shortDescription;
    private String hint;
    private Date dateHidden;
    private DLong attributesNegative;
    private DLong attributesPositive;

    public MutableCache(double latitude, double longitude) {
        super(latitude, longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MutableCache(Database database, ImmutableCache cache) {
        super(cache.getLatitude(), cache.getLongitude());
        this.latitude = cache.getLatitude();
        this.longitude = cache.getLongitude();
        this.attributes = cache.getAttributes(database);
        this.name = cache.getName().toString();
        this.gcCode = cache.getGcCode().toString();
        this.placedBy = cache.getPlacedBy().toString();
        this.owner = cache.getOwner().toString();
        this.gcId = cache.getGcId().toString();
        this.rating = (short) (cache.getRating() * 2);
        this.favPoints = cache.getFavoritePoints();
        this.id = cache.getId();
        this.type = cache.getType();
        this.size = cache.getSize();
        this.difficulty = cache.getDifficulty();
        this.terrain = cache.getTerrain();
        this.hasHint = cache.hasHint();
        this.archived = cache.isArchived();
        this.available = cache.isAvailable();
        this.favorite = cache.isFavorite();
        this.found = cache.isFound();
        this.userData = cache.isHasUserData();
        this.listingChanged = cache.isListingChanged();
        this.waypoints = cache.getWaypoints();
        this.correctedCoordinates = cache.hasCorrectedCoordinates();
    }

    @Override
    public double getLatitude() {
        return this.latitude;
    }

    @Override
    public double getLongitude() {
        return this.longitude;
    }

    @Override
    public Array<Attributes> getAttributes(Database database) {
        return this.attributes;
    }

    @Override
    public boolean ImTheOwner() {
        return false;
    }

    @Override
    public boolean CorrectedCoordiantesOrMysterySolved() {
        return this.correctedCoordinates;
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
    public float Distance(MathUtils.CalculationType type, boolean useFinal) {
        return 0;
    }

    @Override
    float Distance(MathUtils.CalculationType type, boolean useFinal, Coordinate fromPos) {
        return 0;
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
    public CharSequence getGcCode() {
        return gcCode;
    }

    @Override
    public void setGcCode(String gcCode) {
        this.gcCode = gcCode;
    }

    @Override
    public CharSequence getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public CharSequence getOwner() {
        return owner;
    }

    @Override
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public CharSequence getGcId() {
        return gcId;
    }

    @Override
    public void setGcId(String gcId) {
        this.gcId = gcId;
    }

    @Override
    public String getHint() {
        return this.hint;
    }

    @Override
    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public long getGPXFilename_ID() {
        return 0;
    }

    @Override
    public void setGPXFilename_ID(long gpxFilenameId) {

    }

    @Override
    public boolean hasHint() {
        return hasHint;
    }

    @Override
    public boolean hasCorrectedCoordinates() {
        return correctedCoordinates;
    }

    @Override
    public void setCorrectedCoordinates(boolean correctedCoordinates) {
        this.correctedCoordinates = correctedCoordinates;
    }

    @Override
    public boolean isArchived() {
        return archived;
    }

    @Override
    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Override
    public boolean isAvailable() {
        return this.available;
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean isFavorite() {
        return this.favorite;
    }

    @Override
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public float getDifficulty() {
        return this.difficulty;
    }

    @Override
    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public float getTerrain() {
        return terrain;
    }

    @Override
    public void setTerrain(float terrain) {
        this.terrain = terrain;
    }

    @Override
    public boolean isFound() {
        return this.found;
    }

    @Override
    public void setFound(boolean found) {
        this.found = found;
    }

    @Override
    public boolean isLive() {
        return false;
    }

    @Override
    public void setLive(boolean isLive) {

    }

    @Override
    public boolean isHasUserData() {
        return this.userData;
    }

    @Override
    public void setHasUserData(boolean hasUserData) {
        this.userData = hasUserData;
    }

    @Override
    public boolean isListingChanged() {
        return this.listingChanged;
    }

    @Override
    public void setListingChanged(boolean listingChanged) {
        this.listingChanged = listingChanged;
    }

    @Override
    public CharSequence getPlacedBy() {
        return placedBy;
    }

    @Override
    public void setPlacedBy(String placedBy) {
        this.placedBy = placedBy;
    }

    @Override
    public Date getDateHidden() {
        return this.dateHidden;
    }

    @Override
    public void setDateHidden(Date date) {
        this.dateHidden = date;
    }

    @Override
    public byte getApiState() {
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
    public String getTmpNote() {
        return null;
    }

    @Override
    public void setTmpNote(String value) {

    }

    @Override
    public int getSolverChecksum() {
        return 0;
    }

    @Override
    public void setSolverChecksum(int value) {

    }

    @Override
    public String getTmpSolver() {
        return null;
    }

    @Override
    public void setTmpSolver(String value) {

    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public void setUrl(String value) {

    }

    @Override
    public String getCountry() {
        return null;
    }

    @Override
    public void setCountry(String value) {

    }

    @Override
    public String getState() {
        return null;
    }

    @Override
    public void setState(String value) {

    }

    public void addAttributeNegative(Attributes attribute) {
        if (attributesNegative == null)
            attributesNegative = new DLong(0, 0);
        attributesNegative.BitOr(Attributes.GetAttributeDlong(attribute));
    }

    public void addAttributePositive(Attributes attribute) {
        if (attributesPositive == null)
            attributesPositive = new DLong(0, 0);
        attributesPositive.BitOr(Attributes.GetAttributeDlong(attribute));
    }

    @Override
    public DLong getAttributesPositive() {
        return attributesPositive;
    }

    @Override
    public DLong getAttributesNegative() {
        return attributesNegative;
    }

    @Override
    public void setAttributesPositive(DLong dLong) {
        attributesPositive = dLong;
    }

    @Override
    public void setAttributesNegative(DLong dLong) {
        attributesNegative = dLong;
    }

    @Override
    public void setLongDescription(String value) {
        this.longDescription = value;
    }

    @Override
    public String getLongDescription() {
        return this.longDescription;
    }

    @Override
    public void setShortDescription(String value) {
        this.shortDescription = value;
    }

    @Override
    public String getShortDescription() {
        return this.shortDescription;
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
        return false;
    }

    @Override
    public boolean isAttributeNegativeSet(Attributes attribute) {
        return false;
    }

    @Override
    public void setFavoritePoints(int value) {
        this.favPoints = value;
    }


    @Override
    public int getFavoritePoints() {
        return this.favPoints;
    }

    @Override
    public Array<AbstractWaypoint> getWaypoints() {
        return this.waypoints;
    }

    @Override
    public void setWaypoints(Array<AbstractWaypoint> waypoints) {
        this.waypoints = waypoints;
    }

    @Override
    public CacheDetail getDetail() {
        return null;
    }

    @Override
    public void setDetail(CacheDetail detail) {

    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public float getRating() {
        return rating / 2;
    }

    @Override
    public void setRating(float rating) {
        this.rating = (short) (rating * 2);
    }

    @Override
    public CacheSizes getSize() {
        return this.size;
    }

    @Override
    public void setSize(CacheSizes size) {
        this.size = size;
    }

    @Override
    public CacheTypes getType() {
        return this.type;
    }

    @Override
    public void setType(CacheTypes type) {
        this.type = type;
    }

    @Override
    public int getNumTravelbugs() {
        return numTravelbugs;
    }

    @Override
    public void setNumTravelbugs(int numTravelbugs) {
        this.numTravelbugs = (short) numTravelbugs;
    }

    @Override
    public float getCachedDistance() {
        return 0;
    }

    @Override
    public void setCachedDistance(float cachedDistance) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void loadDetail() {

    }

    @Override
    public void deleteDetail(Boolean value) {

    }

    @Override
    public boolean isDetailLoaded() {
        return false;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public AbstractCache getMutable(Database database) {
        return this;
    }

    @Override
    public void setAttributes(Array<Attributes> attributes) {
        this.attributes = attributes;
    }

    @Override
    public void setHasHint(boolean hasHint) {
        this.hasHint = hasHint;
    }

    @Override
    public void setLatLon(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public short getBooleanStore() {
        short bitFlags = 0;
        bitFlags = ImmutableCache.setMaskValue(ImmutableCache.MASK_ARCHIVED, archived, bitFlags);
        bitFlags = ImmutableCache.setMaskValue(ImmutableCache.MASK_AVAILABLE, available, bitFlags);
        bitFlags = ImmutableCache.setMaskValue(ImmutableCache.MASK_FOUND, found, bitFlags);
        bitFlags = ImmutableCache.setMaskValue(ImmutableCache.MASK_FAVORITE, favorite, bitFlags);
        bitFlags = ImmutableCache.setMaskValue(ImmutableCache.MASK_HAS_USER_DATA, userData, bitFlags);
        bitFlags = ImmutableCache.setMaskValue(ImmutableCache.MASK_LISTING_CHANGED, listingChanged, bitFlags);
        bitFlags = ImmutableCache.setMaskValue(ImmutableCache.MASK_CORECTED_COORDS, correctedCoordinates, bitFlags);
        bitFlags = ImmutableCache.setMaskValue(ImmutableCache.MASK_HAS_HINT, hasHint, bitFlags);

        return bitFlags;
    }

    public void reset() {

    }
}
