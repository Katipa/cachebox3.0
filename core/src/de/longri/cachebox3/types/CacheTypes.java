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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import com.kotcrab.vis.ui.VisUI;
import de.longri.cachebox3.gui.interfaces.SelectBoxItem;
import de.longri.cachebox3.gui.skin.styles.CacheTypeStyle;

import java.util.ArrayList;

public enum CacheTypes implements SelectBoxItem {
    Traditional(true), // = 0,
    Multi(true), // = 1,
    Mystery(true), // = 2,
    Camera(true), // = 3,
    Earth(true), // = 4,
    Event(true), // = 5,
    MegaEvent(true), // = 6,
    CITO(true), // = 7,
    Virtual(true), // = 8,
    Letterbox(true), // = 9,
    Wherigo(true), // = 10,
    ReferencePoint(false), // = 11,
    Wikipedia(false), // = 12,
    Undefined(true), // = 13,
    MultiStage(false), // = 14,
    MultiQuestion(false), // = 15,
    Trailhead(false), // = 16,
    ParkingArea(false), // = 17,
    Final(false), // = 18, !!! 18 used in CacheListDAO
    Cache(true), // = 19,
    MyParking(true), // = 20
    Giga(true), // 22
    LabCache(true);

    private boolean isCache;

    CacheTypes(boolean isCache) {
        this.isCache = isCache;
    }

    public final static CacheTypes[] caches() {
        ArrayList<CacheTypes> result = new ArrayList<CacheTypes>();
        for (CacheTypes c : CacheTypes.values()) {
            if (c.isCache) {
                result.add(c);
            }
        }
        return result.toArray(new CacheTypes[result.size()]);
    }

    /**
     * @param string
     * @return
     */
    public static CacheTypes parseString(String string) {

        if (string.toLowerCase().contains("virtual cache")) {
            return Virtual;
        } else if (string.equalsIgnoreCase("Cache In Trash Out Event")) {
            return CITO;
        } else {
            // remove trailing " cache" or " hybrid" fragments
            if (string.contains(" "))
                string = string.substring(0, string.indexOf(" "));
            // remove trailing "-cache" fragments
            if (string.contains("-"))
                string = string.substring(0, string.indexOf("-"));

            // Replace some opencaching.de / geotoad cache types
            if (string.toLowerCase().contains("unknown"))
                return Mystery;
            if (string.toLowerCase().contains("multicache"))
                return Multi;
            if (string.toLowerCase().contains("whereigo"))
                return Wherigo; // note the additional "e"
            if (string.toLowerCase().contains("other"))
                return Mystery;
            if (string.toLowerCase().contains("earthcache"))
                return Earth;
            if (string.toLowerCase().contains("webcam"))
                return Camera;
            if (string.toLowerCase().contains("question"))
                return MultiQuestion;
            if (string.toLowerCase().contains("reference"))
                return ReferencePoint;
            if (string.toLowerCase().contains("referenzpunkt"))
                return ReferencePoint;
            if (string.toLowerCase().contains("parking"))
                return ParkingArea;
            if (string.toLowerCase().contains("stages"))
                return MultiStage;
            if (string.toLowerCase().contains("mega"))
                return MegaEvent;
            if (string.toLowerCase().contains("virtual"))
                return MultiQuestion; // Import Virtual Stage as Question of a Multi
            if (string.toLowerCase().contains("physical"))
                return MultiStage; // Import Physical Stage as a Multi Stage
            if (string.length() == 0)
                return Undefined;
        }

        try

        {
            return valueOf(string);
        } catch (

                Exception ex)

        {
            CacheTypes cacheType = Undefined;
            Boolean blnCacheTypeFound = false;
            for (CacheTypes ct : CacheTypes.values()) {
                if (ct.toString().toLowerCase().equals(string.toLowerCase())) {
                    cacheType = ct;
                    blnCacheTypeFound = true;
                    break;
                }
            }
            if (!blnCacheTypeFound) {
                System.out.println("Handle cache type: " + string);
            }
            return cacheType;
        }

    }

    public CacheWidget getCacheWidget(CacheTypeStyle style) {
        return new CacheWidget(this, style);
    }

    public String getName() {
        return this.name();
    }


    static CacheTypeStyle cacheListTypeStyle;

    @Override
    public Drawable getDrawable() {
        // for select Box interface, use 'cacheList' style
        if (cacheListTypeStyle == null) cacheListTypeStyle = VisUI.getSkin().get("cacheList", CacheTypeStyle.class);
        return getDrawable(cacheListTypeStyle);
    }

    public Drawable getDrawable(CacheTypeStyle style) {
        Drawable drawable;

        switch (this) {
            case Traditional:
                drawable = style.traditional;
                break;
            case Multi:
                drawable = style.multi;
                break;
            case Mystery:
                drawable = style.mystery;
                break;
            case Camera:
                drawable = style.camera;
                break;
            case Earth:
                drawable = style.earth;
                break;
            case Event:
                drawable = style.event;
                break;
            case MegaEvent:
                drawable = style.megaEvent;
                break;
            case CITO:
                drawable = style.cito;
                break;
            case Virtual:
                drawable = style.virtual;
                break;
            case Letterbox:
                drawable = style.letterbox;
                break;
            case Wherigo:
                drawable = style.wherigo;
                break;
            case ReferencePoint:
                drawable = style.referencePoint;
                break;
            case Wikipedia:
                drawable = style.wikipedia;
                break;
            case Undefined:
                drawable = style.undefined;
                break;
            case MultiStage:
                drawable = style.multiStage;
                break;
            case MultiQuestion:
                drawable = style.multiQuestion;
                break;
            case Trailhead:
                drawable = style.trailhead;
                break;
            case ParkingArea:
                drawable = style.parkingArea;
                break;
            case Final:
                drawable = style.Final;
                break;
            case Cache:
                drawable = style.cache;
                break;
            case MyParking:
                drawable = style.myParking;
                break;
            case Giga:
                drawable = style.giga;
                break;
            default:
                drawable = null;
        }
        return drawable;
    }

    public static class CacheWidget extends Widget {
        private final Drawable drawable;
        private boolean needsLayout = true;

        public CacheWidget(CacheTypes cacheType, CacheTypeStyle style) {
            drawable = cacheType.getDrawable(style);
        }

        private float x, y, imageWidth, imageHeight, scaleX, scaleY;

        @Override
        public void layout() {
            if (!needsLayout) {
                super.layout();
                return;
            }

            x = getX();
            y = getY();
            imageWidth = getWidth();
            imageHeight = getHeight();
            scaleX = getScaleX();
            scaleY = getScaleY();

            super.layout();
            needsLayout = true;
        }

        /**
         * Called when the actor's size has been changed.
         */
        @Override
        protected void sizeChanged() {
            super.sizeChanged();
            needsLayout = true;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            validate();
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

            x = getX();
            y = getY();

            if (drawable instanceof TransformDrawable) {
                float rotation = getRotation();
                if (scaleX != 1 || scaleY != 1 || rotation != 0) {
                    ((TransformDrawable) drawable).draw(batch, x, y, getOriginX(), getOriginY(),
                            imageWidth, imageHeight, scaleX, scaleY, rotation);
                    return;
                }
            }
            if (drawable != null) drawable.draw(batch, x, y, imageWidth * scaleX, imageHeight * scaleY);
        }

        @Override
        public float getPrefWidth() {
            if (drawable != null) return drawable.getMinWidth();
            return 0;
        }

        @Override
        public float getPrefHeight() {
            if (drawable != null) return drawable.getMinHeight();
            return 0;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case CITO:
                return "Cache In Trash Out Event";
            case Cache:
                break;
            case Camera:
                break;
            case Earth:
                return "Earthcache";
            case Event:
                return "Event Cache";
            case Final:
                return "Final Location";
            case Giga:
                break;
            case Letterbox:
                break;
            case MegaEvent:
                break;
            case Multi:
                return "Multi-cache";
            case MultiQuestion:
                return "Question to Answer";
            case MultiStage:
                return "Stages of a Multicache";
            case MyParking:
                break;
            case Mystery:
                return "Unknown Cache";
            case ParkingArea:
                return "Parking Area";
            case ReferencePoint:
                return "Reference Point";
            case Traditional:
                return "Traditional Cache";
            case Trailhead:
                break;
            case Undefined:
                break;
            case Virtual:
                break;
            case Wherigo:
                break;
            case Wikipedia:
                break;
            default:
                break;

        }

        return super.toString();
    }

    public  String toShortString() {
        switch (this) {
            case CITO:
                return "X";
            case Cache:
                return "C";
            case Camera:
                return "W";
            case Earth:
                return "E";
            case Event:
                return "X";
            case Giga:
                return "X";
            case Letterbox:
                return "L";
            case MegaEvent:
                return "X";
            case Multi:
                return "M";
            case Mystery:
                return "U";
            case Traditional:
                return "T";
            case Virtual:
                return "V";
            case Wherigo:
                return "G";
            case Wikipedia:
                return "?";
            default:
                break;

        }
        return " ";
    }

}