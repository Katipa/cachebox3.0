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
package de.longri.cachebox3.types.test_caches;

import de.longri.cachebox3.sqlite.Database;
import de.longri.cachebox3.types.AbstractCache;
import de.longri.cachebox3.types.Attributes;
import de.longri.cachebox3.types.CacheSizes;
import de.longri.cachebox3.types.CacheTypes;
import de.longri.cachebox3.utils.CharSequenceUtilTest;

import java.util.ArrayList;
import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Longri on 31.03.18.
 */
public abstract class AbstractTestCache {
    double longitude;
    double latitude;
    CacheTypes cacheType;
    String gcCode;
    String name;
    long id;
    boolean available;
    boolean archived;
    String placed_by;
    String owner;
    CacheSizes container;
    ArrayList<Attributes> positiveList = new ArrayList<>();
    ArrayList<Attributes> negativeList = new ArrayList<>();
    String url;
    float difficulty;
    float terrain;
    String country;
    String state;
    String shortDescription;
    String longDescription;
    String hint;

    public void assertCache(AbstractCache other, Database database) {
        assertThat("Cache must not be NULL", other != null);
        assertThat("Latitude must be " + latitude + " but was :" + other.latitude, latitude == other.latitude);
        assertThat("Longitude must be " + longitude + " but was :" + other.longitude, longitude == other.longitude);
        assertThat("Cache type must be " + cacheType + " but was :" + other.getType(), cacheType == other.getType());
        assertThat("GcCode must be " + gcCode + " but was :" + other.getGcCode(), CharSequenceUtilTest.equals(gcCode, other.getGcCode()));
        assertThat("Cache name must be " + name + " but was :" + other.getName(), CharSequenceUtilTest.equals(name, other.getName()));
        assertThat("Cache id must be " + id + " but was :" + other.getId(), id == other.getId());
        assertThat("Cache available must be " + available + " but was :" + other.isAvailable(), available == other.isAvailable());
        assertThat("Cache archived must be " + archived + " but was :" + other.isArchived(), archived == other.isArchived());
        assertThat("Placed by must be " + placed_by + " but was :" + other.getPlacedBy(), CharSequenceUtilTest.equals(placed_by, other.getPlacedBy()));
        assertThat("Owner must be " + owner + " but was :" + other.getOwner(), CharSequenceUtilTest.equals(owner, other.getOwner()));
        assertThat("Container must be " + container + " but was :" + other.getSize(), container == other.getSize());
        assetCacheAttributes(other, database);
        assertThat("Cache Url must be " + url + " but was :" + other.getUrl(database), CharSequenceUtilTest.equals(url, other.getUrl(database)));
        assertThat("Country must be " + country + " but was :" + other.getCountry(database), CharSequenceUtilTest.equals(country, other.getCountry(database)));
        assertThat("State must be " + state + " but was :" + other.getState(database), CharSequenceUtilTest.equals(state, other.getState(database)));
        assertThat("Cache difficulty must be " + difficulty + " but was :" + other.getDifficulty(), difficulty == other.getDifficulty());
        assertThat("Cache terrain must be " + terrain + " but was :" + other.getTerrain(), terrain == other.getTerrain());

        assertEquals(shortDescription, other.getShortDescription(database).replaceAll("\r\n", "\n"), "Short description should be equals");
        assertEquals(longDescription, other.getLongDescription(database).replaceAll("\r\n", "\n"), "Long description should be equals");
        assertEquals(hint, other.getHint(database).toString().replaceAll("\r\n", "\n"), "Hint should be equals");
    }

    private void assetCacheAttributes(AbstractCache abstractCache, Database database) {
        Iterator<Attributes> positiveIterator = positiveList.iterator();
        Iterator<Attributes> negativeIterator = negativeList.iterator();

        abstractCache = abstractCache.getMutable(database);

        while (positiveIterator.hasNext()) {
            Attributes att = positiveIterator.next();
            assertThat("positive Attribute " + att + " wrong", abstractCache.isAttributePositiveSet(att));
        }

        while (negativeIterator.hasNext()) {
            Attributes tmp = negativeIterator.next();
            assertThat(tmp.name() + " negative Attribute wrong", abstractCache.isAttributeNegativeSet((tmp)));
        }

        // f�lle eine Liste mit allen Attributen
        ArrayList<Attributes> attributes = new ArrayList<Attributes>();
        Attributes[] tmp = Attributes.values();
        for (Attributes item : tmp) {
            attributes.add(item);
        }

        // L�sche die vergebenen Atribute aus der Kommplett Liste
        positiveIterator = positiveList.iterator();
        negativeIterator = negativeList.iterator();

        while (positiveIterator.hasNext()) {
            attributes.remove(positiveIterator.next());
        }

        while (negativeIterator.hasNext()) {
            attributes.remove(negativeIterator.next());
        }

        attributes.remove(Attributes.getAttributeEnumByGcComId(64));
        attributes.remove(Attributes.getAttributeEnumByGcComId(65));
        attributes.remove(Attributes.getAttributeEnumByGcComId(66));

        // Teste ob die �brig gebliebenen Atributte auch nicht vergeben wurden.
        Iterator<Attributes> RestInterator = attributes.iterator();

        while (RestInterator.hasNext()) {
            Attributes attr = (Attributes) RestInterator.next();
            assertThat(attr.name() + " Attribute wrong", !abstractCache.isAttributePositiveSet(attr));
            assertThat(attr.name() + " Attribute wrong", !abstractCache.isAttributeNegativeSet(attr));
        }
    }


}
