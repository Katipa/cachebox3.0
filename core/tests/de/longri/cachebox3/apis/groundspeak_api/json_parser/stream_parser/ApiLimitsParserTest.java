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
package de.longri.cachebox3.apis.groundspeak_api.json_parser.stream_parser;

import de.longri.cachebox3.TestUtils;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by longri on 01.07.17.
 */
public class ApiLimitsParserTest {

    @Test
    public void parseLimitResultStream() throws FileNotFoundException {
        InputStream stream = TestUtils.getResourceRequestStream("core/testsResources/ApiLimitsResult.json");
        ApiLimitParser parser = new ApiLimitParser();

        int result = parser.parseCallsPerMinute(stream);

        assertThat("Result must be 30", result == 30);
    }

}
