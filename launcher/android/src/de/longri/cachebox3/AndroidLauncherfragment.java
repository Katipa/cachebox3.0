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
package de.longri.cachebox3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import org.oscim.android.gl.AndroidGL;
import org.oscim.backend.GLAdapter;
import org.oscim.gdx.GdxAssets;

/**
 * Created by Longri on 26.04.2017.
 */
public class AndroidLauncherfragment extends AndroidFragmentApplication {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //initialize platform bitmap factory
        org.oscim.android.canvas.AndroidGraphics.init();

        GdxAssets.init("");
        GLAdapter.init(new AndroidGL());

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.stencil = 8;
        config.numSamples = 2;
        new SharedLibraryLoader().load("vtm-jni");

        return initializeForView(new CacheboxMain(), config);
    }

}
