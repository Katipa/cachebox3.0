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
package de.longri.cachebox3.translation.word;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.sql.SQLiteGdxException;
import de.longri.cachebox3.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Longri on 26.10.2017.
 */
class WordStoreTest {

    @BeforeAll
    static void setUp() throws SQLiteGdxException {
        TestUtils.initialGdx();
    }


    @Test
    void add() {

        String t1 = "Test string with double used words like Test or string";
        String t2 = "Test string that contains non double words";
        String t3 = "Test string that contains non double words!";

        WordStore store = new WordStore();

        StringSequence m1 = store.add(t1);
        assertThat("MutableString must equals  '" + t1 + "' : '" + m1 + "'", equals(t1, m1));
        assertThat("toString() must equals  '" + t1 + "' : '" + m1 + "'", t1.equals(m1.toString()));

        assertThat("Store length must be 33, but was:" + store.storage.size, store.storage.size == 33);

        StringSequence m2 = store.add(t2);
        assertThat("MutableString must equals  '" + t2 + "' : '" + m2 + "'", equals(t2, m2));
        assertThat("toString() must equals  '" + t2 + "' : '" + m2 + "'", t2.equals(m2.toString()));
        assertThat("Store length must be 48, but was:" + store.storage.size, store.storage.size == 48);

        StringSequence m3 = store.add(t3);
        assertThat("MutableString must equals  '" + t3 + "' : '" + m3 + "'", equals(t3, m3));
        assertThat("toString() must equals  '" + t3 + "' : '" + m3 + "'", t3.equals(m3.toString()));
        assertThat("Store length must be 54, but was:" + store.storage.size, store.storage.size == 54);

    }


    @Test
    void addTranslationFile() throws FileNotFoundException {
        FileHandle fileHandle = TestUtils.getResourceFileHandle("testsResources/strings.ini");
        WordStore store = new WordStore();

        String fileString = fileHandle.readString();
        String[] lines = fileString.split("\r\n");

        String[] clearedLines = new String[lines.length];

        for (int i = 0, n = lines.length; i < n; i++) {
            String line = lines[i];
            if (line == null || line.isEmpty()) continue;
            int pos = line.indexOf("=");
            if (pos < 0) continue;
            clearedLines[i] = line.substring(pos + 1);
        }

        // store lines
        StringSequence[] sequences = new StringSequence[lines.length];
        for (int i = 0, n = lines.length; i < n; i++) {
            String line = clearedLines[i];
            if (line == null || line.isEmpty()) continue;
            sequences[i] = store.add(line);
        }

        // test
        for (int i = 0, n = lines.length; i < n; i++) {
            String line = clearedLines[i];
            StringSequence sequence = sequences[i];
            if (line == null || line.isEmpty()) continue;
            assertThat("MutableString must equals  '" + line + "' : '" + sequence + "'", equals(line, sequence));
            assertThat("toString() must equals  '" + line + "' : '" + sequence + "'", line.equals(sequence.toString()));
        }


        assertThat("Store length must shorter then 26000, but was:" + store.storage.size, store.storage.size < 20000);


    }


    //##################################################################
    //# Helper
    //##################################################################

    private boolean equals(CharSequence s1, CharSequence s2) {
        if (s1.length() != s2.length()) return false;
        int n = s1.length();
        while (n-- > 0) {
            if (s1.charAt(n) != s2.charAt(n)) return false;
        }
        return true;
    }

}