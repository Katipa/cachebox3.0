/* 
 * Copyright (C) 2014-2016 team-cachebox.de
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

import com.badlogic.gdx.utils.*;
import de.longri.cachebox3.CB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class FilterProperties {
    final static Logger log = LoggerFactory.getLogger(FilterProperties.class);

    private final static String SEPARATOR = ",";
    private final static String GPXSEPARATOR = "^";

    public int Finds;
    public int NotAvailable;
    public int Archived;
    public int Own;
    public int ContainsTravelbugs;
    public int Favorites;
    public int ListingChanged;
    public int WithManualWaypoint;
    public int HasUserData;

    public float MinDifficulty;
    public float MaxDifficulty;
    public float MinTerrain;
    public float MaxTerrain;
    public float MinContainerSize;
    public float MaxContainerSize;
    public float MinRating;
    public float MaxRating;

    public int hasCorrectedCoordinates;
    public boolean isHistory;

    public boolean[] mCacheTypes;

    public int[] mAttributes;

    public LongArray GPXFilenameIds;

    public String filterName;
    public String filterGcCode;
    public String filterOwner;

    public LongArray Categories;

    /**
     * creates the FilterProperties with default values.
     * For default nothing is filtered!
     */
    public FilterProperties() {
        initCreation();
    }

    /**
     * Copy constructor
     *
     * @param properties
     */
    public FilterProperties(FilterProperties properties) {
        this.set(properties);
    }

    private void initCreation() {
        Finds = 0;
        NotAvailable = 0;
        Archived = 0;
        Own = 0;
        ContainsTravelbugs = 0;
        Favorites = 0;
        ListingChanged = 0;
        WithManualWaypoint = 0;
        HasUserData = 0;

        MinDifficulty = 1;
        MaxDifficulty = 5;
        MinTerrain = 1;
        MaxTerrain = 5;
        MinContainerSize = 0;
        MaxContainerSize = 4;
        MinRating = 0;
        MaxRating = 5;

        this.hasCorrectedCoordinates = 0;
        isHistory = false;
        mCacheTypes = new boolean[CacheTypes.values().length];
        Arrays.fill(mCacheTypes, true);

        mAttributes = new int[Attributes.values().length]; // !!! attention: Attributes 0 not used
        Arrays.fill(mAttributes, 0);

        GPXFilenameIds = new LongArray();
        filterName = "";
        filterGcCode = "";
        filterOwner = "";

        Categories = new LongArray();
    }

    /**
     * creates the FilterProperties from a serialization-String
     * an empty serialization-String filters nothing
     *
     * @param serialization
     */
    public FilterProperties(String serialization) {
        if (serialization.length() == 0) {
            initCreation();
            return;
        }
        try {
            JsonValue json = new JsonReader().parse(serialization);
            try {
                isHistory = json.getBoolean("isHistory");
            } catch (Exception e) {
                isHistory = false;
            }
            String caches = json.getString("caches");
            String[] parts = caches.split(SEPARATOR);
            int cnt = 0;
            Finds = Integer.parseInt(parts[cnt++]);
            NotAvailable = Integer.parseInt(parts[cnt++]);
            Archived = Integer.parseInt(parts[cnt++]);
            Own = Integer.parseInt(parts[cnt++]);
            ContainsTravelbugs = Integer.parseInt(parts[cnt++]);
            Favorites = Integer.parseInt(parts[cnt++]);
            HasUserData = Integer.parseInt(parts[cnt++]);
            ListingChanged = Integer.parseInt(parts[cnt++]);
            WithManualWaypoint = Integer.parseInt(parts[cnt++]);

            MinDifficulty = Float.parseFloat(parts[cnt++]);
            MaxDifficulty = Float.parseFloat(parts[cnt++]);
            MinTerrain = Float.parseFloat(parts[cnt++]);
            MaxTerrain = Float.parseFloat(parts[cnt++]);
            MinContainerSize = Float.parseFloat(parts[cnt++]);
            MaxContainerSize = Float.parseFloat(parts[cnt++]);
            MinRating = Float.parseFloat(parts[cnt++]);
            MaxRating = Float.parseFloat(parts[cnt++]);

            if (parts.length == 17) {
                this.hasCorrectedCoordinates = 0;
            } else {
                hasCorrectedCoordinates = Integer.parseInt(parts[cnt++]);
            }

            mCacheTypes = parseCacheTypes(json.getString("types"));

            String attributes = json.getString("attributes");
            parts = attributes.split(SEPARATOR);
            mAttributes = new int[Attributes.values().length];
            mAttributes[0] = 0; // gibts nicht
            int og = parts.length;
            if (parts.length == mAttributes.length) {
                og = parts.length - 1; // falls doch schon mal mit mehr gespeichert
            }
            for (int i = 0; i < (og); i++)
                mAttributes[i + 1] = Integer.parseInt(parts[i]);
            // aus älteren Versionen
            for (int i = og; i < mAttributes.length - 1; i++)
                mAttributes[i + 1] = 0;

            GPXFilenameIds = new LongArray();
            String gpxfilenames = json.getString("gpxfilenameids");
            parts = gpxfilenames.split(SEPARATOR);
            cnt = 0;
            if (parts.length > cnt) {
                String tempGPX = parts[cnt++];
                String[] partsGPX = tempGPX.split("\\" + GPXSEPARATOR);
                for (int i = 1; i < partsGPX.length; i++) {
                    GPXFilenameIds.add(Long.parseLong(partsGPX[i]));
                }
            }

            filterName = json.getString("filtername");
            filterGcCode = json.getString("filtergc");
            filterOwner = json.getString("filterowner");

            Categories = new LongArray();
            String filtercategories = json.getString("categories");
            if (filtercategories.length() > 0) {
                String[] partsGPX = filtercategories.split("\\" + GPXSEPARATOR);
                for (int i = 1; i < partsGPX.length; i++) {
                    // Log.info(log, "parts[" + i + "]=" + partsGPX[i]);
                    Categories.add(Long.parseLong(partsGPX[i]));
                }
            }
        } catch (Exception e) {
            log.error("Json Version FilterProperties(" + serialization + ")", e);
        }
    }

    private boolean[] parseCacheTypes(String types) {
        String[] parts = types.split(SEPARATOR);
        final boolean[] result = new boolean[CacheTypes.values().length];

        for (int i = 0; i < result.length; i++) {
            result[i] = Boolean.parseBoolean(parts[i]);
        }

        return result;
    }

    /**
     * True, wenn FilterProperties eine Filterung nach name, Gc-Code oder Owner enthält!
     *
     * @return
     */
    public boolean isExtendedFilter() {
        if (filterName.length() > 0)
            return true;

        if (filterGcCode.length() > 0)
            return true;

        if (filterOwner.length() > 0)
            return true;

        return false;
    }

    /**
     * a String to save in the database
     *
     * @return
     */
    @Override
    public String toString() {
        String result = "";

        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter writer = new JsonWriter(stringWriter);
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            json.setWriter(writer);
            json.writeObjectStart();

            // add Cache Types
            String tmp = "";
            for (int i = 0; i < mCacheTypes.length; i++) {
                if (i > 0)
                    tmp += SEPARATOR;
                tmp += String.valueOf(mCacheTypes[i]);
            }
            json.writeValue("types", tmp);

            // add Cache properties
            json.writeValue("caches",
                    String.valueOf(Finds) + SEPARATOR + String.valueOf(NotAvailable) + SEPARATOR + String.valueOf(Archived) + SEPARATOR + String.valueOf(Own) + SEPARATOR + String.valueOf(ContainsTravelbugs) + SEPARATOR + String.valueOf(Favorites)
                            + SEPARATOR + String.valueOf(HasUserData) + SEPARATOR + String.valueOf(ListingChanged) + SEPARATOR + String.valueOf(WithManualWaypoint) + SEPARATOR + String.valueOf(MinDifficulty) + SEPARATOR
                            + String.valueOf(MaxDifficulty) + SEPARATOR + String.valueOf(MinTerrain) + SEPARATOR + String.valueOf(MaxTerrain) + SEPARATOR + String.valueOf(MinContainerSize) + SEPARATOR + String.valueOf(MaxContainerSize) + SEPARATOR
                            + String.valueOf(MinRating) + SEPARATOR + String.valueOf(MaxRating) + SEPARATOR + String.valueOf(this.hasCorrectedCoordinates));

            // Filter GCCode
            json.writeValue("filtergc", filterGcCode);

            // GPX Filenames
            tmp = "";
            for (int i = 0; i <= GPXFilenameIds.size - 1; i++) {
                tmp += GPXSEPARATOR + String.valueOf(GPXFilenameIds.get(i));
            }
            json.writeValue("gpxfilenameids", tmp);

            // add Cache Attributes
            tmp = "";
            for (int i = 1; i < mAttributes.length; i++) {
                if (tmp.length() > 0)
                    tmp += SEPARATOR;
                tmp += String.valueOf(mAttributes[i]);
            }
            json.writeValue("attributes", tmp);

            // Filter name
            json.writeValue("filtername", filterName);

            // History
            json.writeValue("isHistory", isHistory);

            // Categories
            tmp = "";
            for (int i = 0, n = Categories.size; i < n; i++) {
                tmp += GPXSEPARATOR + Categories.items[i];
            }
            json.writeValue("categories", tmp);

            // Filter Owner
            json.writeValue("filterowner", filterOwner);

            json.writeObjectEnd();
            result = stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("JSON toString", e);
        }
        return result;
    }

    /**
     * Gibt den SQL Where String dieses Filters zurück
     *
     * @param userName Config.settings.GcLogin.getValue()
     * @return
     */
    public String getSqlWhere(String userName) {

        if (isHistory) {
            ArrayList<String> orParts = new ArrayList<String>();
            String[] gcCodes = CB.cacheHistory.split(",");
            for (int i = 0; i < gcCodes.length; i++) {
                String gcCode = gcCodes[i];
                if (gcCode.length() > 0) {
                    if (!orParts.contains(gcCode))
                        orParts.add("GcCode = '" + gcCode + "'");
                }
            }
            return join(" or ", orParts);
        } else {
            userName = userName.replace("'", "''");

            ArrayList<String> andParts = new ArrayList<String>();

            short bitstoreMust = 0;
            short bitstoreMustNot = 0;


            //TODO Bitwise equals [BooleanStore & MASK_FOUND != 0] for true
            //TODO Bitwise equals [BooleanStore & MASK_FOUND == 0] for false
            if (Finds == 1)
                bitstoreMust = ImmutableCache.setMaskValue(ImmutableCache.MASK_FOUND, true, bitstoreMust);//andParts.add("Found=1");
            if (Finds == -1)
                bitstoreMustNot = ImmutableCache.setMaskValue(ImmutableCache.MASK_FOUND, true, bitstoreMustNot);//andParts.add("(Found=0 or Found is null)");

            if (Favorites == 1)
                bitstoreMust = ImmutableCache.setMaskValue(ImmutableCache.MASK_FAVORITE, true, bitstoreMust);
            if (Favorites == -1)
                bitstoreMustNot = ImmutableCache.setMaskValue(ImmutableCache.MASK_FAVORITE, true, bitstoreMustNot);

            if (NotAvailable == 1)
                bitstoreMust = ImmutableCache.setMaskValue(ImmutableCache.MASK_AVAILABLE, true, bitstoreMust);
            if (NotAvailable == -1)
                bitstoreMustNot = ImmutableCache.setMaskValue(ImmutableCache.MASK_AVAILABLE, true, bitstoreMustNot);

            if (Archived == 1)
                bitstoreMust = ImmutableCache.setMaskValue(ImmutableCache.MASK_ARCHIVED, true, bitstoreMust);
            if (Archived == -1)
                bitstoreMustNot = ImmutableCache.setMaskValue(ImmutableCache.MASK_ARCHIVED, true, bitstoreMustNot);

            if (ListingChanged == 1)
                bitstoreMust = ImmutableCache.setMaskValue(ImmutableCache.MASK_LISTING_CHANGED, true, bitstoreMust);
            if (ListingChanged == -1)
                bitstoreMustNot = ImmutableCache.setMaskValue(ImmutableCache.MASK_LISTING_CHANGED, true, bitstoreMustNot);

            if (HasUserData == 1)
                bitstoreMust = ImmutableCache.setMaskValue(ImmutableCache.MASK_HAS_USER_DATA, true, bitstoreMust);
            if (HasUserData == -1)
                bitstoreMustNot = ImmutableCache.setMaskValue(ImmutableCache.MASK_HAS_USER_DATA, true, bitstoreMustNot);

            if (bitstoreMust > 0)
                andParts.add("BooleanStore & " + bitstoreMust + "= " + bitstoreMust);
            if (bitstoreMustNot > 0)
                andParts.add("~BooleanStore & " + bitstoreMustNot + "= " + bitstoreMustNot);


            if (Own == 1)
                andParts.add("(Owner='" + userName + "')");
            if (Own == -1)
                andParts.add("(not Owner='" + userName + "')");

            if (ContainsTravelbugs == 1)
                andParts.add("NumTravelbugs > 0");
            if (ContainsTravelbugs == -1)
                andParts.add("NumTravelbugs = 0");


            if (WithManualWaypoint == 1)
                andParts.add(" ID in (select CacheId FROM Waypoint WHERE UserWaypoint = 1)");
            if (WithManualWaypoint == -1)
                andParts.add(" NOT ID in (select CacheId FROM Waypoint WHERE UserWaypoint = 1)");


            andParts.add("Difficulty >= " + String.valueOf(MinDifficulty * 2));
            andParts.add("Difficulty <= " + String.valueOf(MaxDifficulty * 2));
            andParts.add("Terrain >= " + String.valueOf(MinTerrain * 2));
            andParts.add("Terrain <= " + String.valueOf(MaxTerrain * 2));
            andParts.add("Size >= " + String.valueOf(MinContainerSize));
            andParts.add("Size <= " + String.valueOf(MaxContainerSize));
            andParts.add("Rating >= " + String.valueOf(MinRating * 100));
            andParts.add("Rating <= " + String.valueOf(MaxRating * 100));

            FilterInstances.hasCorrectedCoordinates = hasCorrectedCoordinates;

            String csvTypes = "";
            for (int i = 0; i < mCacheTypes.length; i++) {
                if (mCacheTypes[i])
                    csvTypes += String.valueOf(i) + ",";
            }
            if (csvTypes.length() > 0) {
                csvTypes = csvTypes.substring(0, csvTypes.length() - 1);
                andParts.add("Type in (" + csvTypes + ")");
            }

            for (int i = 1; i < mAttributes.length; i++) {
                if (mAttributes[i] != 0) {
                    if (i < 62) {
                        long shift = DLong.UL1 << (i);
                        if (mAttributes[i] == 1)
                            andParts.add("(AttributesPositive & " + shift + ") > 0");
                        else
                            andParts.add("(AttributesNegative &  " + shift + ") > 0");
                    } else {
                        long shift = DLong.UL1 << (i - 61);
                        if (mAttributes[i] == 1)
                            andParts.add("(AttributesPositiveHigh &  " + shift + ") > 0");
                        else
                            andParts.add("(AttributesNegativeHigh & " + shift + ") > 0");
                    }
                }
            }

            if (GPXFilenameIds.size != 0) {
                String s = "";
                for (long id : GPXFilenameIds.items) {
                    s += String.valueOf(id) + ",";
                }
                // s += "-1";
                if (s.length() > 0) {
                    andParts.add("GPXFilename_Id not in (" + s.substring(0, s.length() - 1) + ")");
                }
            }

            if (filterName != "") {
                andParts.add("name like '%" + filterName + "%'");
            }
            if (filterGcCode != "") {
                andParts.add("GcCode like '%" + filterGcCode + "%'");
            }
            if (filterOwner != "") {
                andParts.add("( PlacedBy like '%" + filterOwner + "%' or Owner like '%" + filterOwner + "%' )");
            }

            return join(" and ", andParts);
        }
    }

    public static String join(String separator, ArrayList<String> array) {
        String retString = "";

        int count = 0;
        for (String tmp : array) {
            retString += tmp;
            count++;
            if (count < array.size())
                retString += separator;
        }
        return retString;
    }

    /**
     * Filter miteinander vergleichen wobei Category Einstellungen ignoriert werden sollen
     *
     * @param filter
     * @return
     */
    public boolean equals(FilterProperties filter) {
        if (Finds != filter.Finds)
            return false;
        if (NotAvailable != filter.NotAvailable)
            return false;
        if (Archived != filter.Archived)
            return false;
        if (Own != filter.Own)
            return false;
        if (ContainsTravelbugs != filter.ContainsTravelbugs)
            return false;
        if (Favorites != filter.Favorites)
            return false;
        if (ListingChanged != filter.ListingChanged)
            return false;
        if (WithManualWaypoint != filter.WithManualWaypoint)
            return false;
        if (HasUserData != filter.HasUserData)
            return false;

        if (MinDifficulty != filter.MinDifficulty)
            return false;
        if (MaxDifficulty != filter.MaxDifficulty)
            return false;
        if (MinTerrain != filter.MinTerrain)
            return false;
        if (MaxTerrain != filter.MaxTerrain)
            return false;
        if (MinContainerSize != filter.MinContainerSize)
            return false;
        if (MaxContainerSize != filter.MaxContainerSize)
            return false;
        if (MinRating != filter.MinRating)
            return false;
        if (MaxRating != filter.MaxRating)
            return false;

        if (hasCorrectedCoordinates != filter.hasCorrectedCoordinates)
            return false;

        for (int i = 0; i < mCacheTypes.length; i++) {
            if (filter.mCacheTypes.length <= i)
                break;
            if (filter.mCacheTypes[i] != this.mCacheTypes[i])
                return false; // nicht gleich!!!
        }

        for (int i = 1; i < mAttributes.length; i++) {
            if (filter.mAttributes.length <= i)
                break;
            if (filter.mAttributes[i] != this.mAttributes[i])
                return false; // nicht gleich!!!
        }

        if (GPXFilenameIds.size != filter.GPXFilenameIds.size)
            return false;
        for (int i = 0, n = GPXFilenameIds.size; i < n; i++) {
            if (!filter.GPXFilenameIds.contains(GPXFilenameIds.items[i]))
                return false;
        }

        if (!filterOwner.equals(filter.filterOwner))
            return false;
        if (!filterGcCode.equals(filter.filterGcCode))
            return false;
        if (!filterName.equals(filter.filterName))
            return false;

        if (isHistory != filter.isHistory)
            return false;

        return true;
    }

    /**
     * @param abstractCache
     * @return
     */
    public boolean passed(AbstractCache abstractCache) {
        if (chkFilterBoolean(this.Finds, abstractCache.isFound()))
            return false;
        if (chkFilterBoolean(this.Own, abstractCache.ImTheOwner()))
            return false;
        if (chkFilterBoolean(this.NotAvailable, !abstractCache.isAvailable()))
            return false;
        if (chkFilterBoolean(this.Archived, abstractCache.isArchived()))
            return false;
        if (chkFilterBoolean(this.ContainsTravelbugs, abstractCache.getNumTravelbugs() > 0))
            return false;
        if (chkFilterBoolean(this.Favorites, abstractCache.isFavorite()))
            return false;
        if (chkFilterBoolean(this.ListingChanged, abstractCache.isListingChanged()))
            return false;
        if (chkFilterBoolean(this.HasUserData, abstractCache.isHasUserData()))
            return false;
        if (chkFilterBoolean(this.hasCorrectedCoordinates, abstractCache.hasCorrectedCoordinates()))
            return false;
        // TODO implement => if (chkFilterBoolean(this.WithManualWaypoint, cache.)) return false;
        // TODO ? the other restrictions?
        if (!this.mCacheTypes[abstractCache.getType().ordinal()])
            return false;

        return true;
    }

    /**
     * @param propertyValue
     * @param found
     * @return
     */
    private boolean chkFilterBoolean(int propertyValue, boolean found) {
        // -1= Cache.{attribute} == False
        // 0= Cache.{attribute} == False|True
        // 1= Cache.{attribute} == True

        if (propertyValue != 0) {
            if (propertyValue != (found ? 1 : -1))
                return true;
        }
        return false;
    }

    public FilterProperties copy() {
        return new FilterProperties(this);
    }

    public void set(FilterProperties properties) {
        Finds = properties.Finds;
        NotAvailable = properties.NotAvailable;
        Archived = properties.Archived;
        Own = properties.Own;
        ContainsTravelbugs = properties.ContainsTravelbugs;
        Favorites = properties.Favorites;
        ListingChanged = properties.ListingChanged;
        WithManualWaypoint = properties.WithManualWaypoint;
        HasUserData = properties.HasUserData;

        MinDifficulty = properties.MinDifficulty;
        MaxDifficulty = properties.MaxDifficulty;
        MinTerrain = properties.MinTerrain;
        MaxTerrain = properties.MaxTerrain;
        MinContainerSize = properties.MinContainerSize;
        MaxContainerSize = properties.MaxContainerSize;
        MinRating = properties.MinRating;
        MaxRating = properties.MaxRating;

        this.hasCorrectedCoordinates = properties.hasCorrectedCoordinates;
        isHistory = properties.isHistory;
        mCacheTypes = Arrays.copyOf(properties.mCacheTypes, properties.mCacheTypes.length);

        mAttributes = Arrays.copyOf(properties.mAttributes, properties.mAttributes.length);

        filterName = properties.filterName;
        filterGcCode = properties.filterGcCode;
        filterOwner = properties.filterOwner;

        Categories = new LongArray();
        GPXFilenameIds = new LongArray();
    }
}