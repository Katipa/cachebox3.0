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
package de.longri.cachebox3.file_transfer;

import de.longri.cachebox3.socket.filebrowser.FileBrowserClint;
import javafx.scene.layout.BorderPane;

/**
 * Created by Longri on 01.11.2017.
 */
public class FileBrowserPane extends BorderPane {


    private final FileBrowserClint clint;

    public FileBrowserPane(FileBrowserClint clint) {
        this.clint = clint;
    }
}
