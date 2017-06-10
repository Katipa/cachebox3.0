/*
 * Copyright (C) 2016 team-cachebox.de
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
package de.longri.cachebox3.gui.stages.initial_tasks;

/**
 * Created by Longri on 02.08.16.
 */
public abstract class AbstractInitTask {

    public interface WorkCallback {
        void taskNameChange(String text);
    }

    public final String name;
    public final int percent;

    public AbstractInitTask(String name, int percent) {
        this.name = name;
        this.percent = percent;
    }

    public abstract void runnable(WorkCallback callback);

}

