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
package de.longri.cachebox3.interfaces;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Longri on 04.10.17
 */
public abstract class ProgressCancelRunnable implements Runnable {

    private final AtomicBoolean isCanceled = new AtomicBoolean(false);
    private float progressValue;
    private String progressMsg;

    public float getProgress() {
        return this.progressValue;
    }

    public String getProgressMsg() {
        return this.progressMsg;
    }

    public abstract void canceled();

    public void cancel() {
        isCanceled.set(true);
        canceled();
    }

    public boolean isCanceled() {
        return isCanceled.get();
    }

    protected void setProgress(float progressValue, String msg) {
        this.progressValue = progressValue;
        this.progressMsg = msg;
    }

}
