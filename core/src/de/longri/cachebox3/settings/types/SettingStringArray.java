/* 
 * Copyright (C) 2011-2017 team-cachebox.de
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
package de.longri.cachebox3.settings.types;

public class SettingStringArray extends SettingString {

    private String possibleValues[];

    public SettingStringArray(String name, SettingCategory category, SettingMode modus, String defaultValue, SettingStoreType StoreType, SettingUsage usage, String possibleValues[]) {
        super(name, category, modus, defaultValue, StoreType, usage);
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.possibleValues = possibleValues;
    }

    public String[] possibleValues() {
        return possibleValues;
    }

    public int getIndexOfValue() {
        for (int i = 0; i < possibleValues.length; i++) {
            if (possibleValues[i].equals(value))
                return i;
        }
        return -1;
    }

    public String getValueFromIndex(int index) {
        return possibleValues[index];
    }
}
