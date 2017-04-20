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
package de.longri.cachebox3.gui.animations.map;

import de.longri.cachebox3.TestUtils;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Longri on 28.03.2017.
 */
class DoubleAnimatorTest {

    static {
        TestUtils.initialGdx();
    }

    @Test
    void update() {
        DoubleAnimator animator = new DoubleAnimator(); // linear
        animator.start(10, 0, 1, 0.001);

        for (int i = 0, n = 10; i < n; i++) {
            assertTrue(animator.update(1), "Must update");
            assertEquals(((double) i + 1) / 10.0, animator.getAct(), 0.00001, "Value must " + (i + 1) / 10);
        }

        assertTrue(!animator.update(1), "Must not update is finish");
    }
}