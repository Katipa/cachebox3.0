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

import de.longri.cachebox3.types.*;

import java.text.ParseException;

/**
 * Created by Longri on 10.04.2018.
 */
public class TestCache_GCV272 extends AbstractTestCache {
    @Override
    protected void setValues() {
        this.latitude = 52.564783;
        this.longitude = 13.393233;
        this.cacheType = CacheTypes.Multi;
        this.gcCode = "GCV272";
        this.name = "Wollankstraße (Berlin)";
        this.available = true;
        this.archived = false;
        this.placed_by = "oldfield";
        this.owner = "oldfield";
        this.container = CacheSizes.micro;
        this.url = "http://www.geocaching.com/seek/cache_details.aspx?guid=c3c2f1df-6632-4f5f-b2ba-d3aa19d862ee";
        this.difficulty = 2.5f;
        this.terrain = 1.5f;
        this.country = "Germany";
        this.state = "Berlin";
        this.found = false;
        this.tbCount = 0;
        this.hint = "\n      ";
        this.note = "";
        this.solver = "";
        try {
            this.dateHidden = DATE_PATTERN.parse("2006-03-25T08:00:00Z");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.negativeList.add(Attributes.Takes_less_than_an_hour);
        this.positiveList.add(Attributes.Bicycles);
        this.positiveList.add(Attributes.Available_at_all_times);
        this.positiveList.add(Attributes.Public_transportation);
        this.positiveList.add(Attributes.Wheelchair_accessible);
        this.positiveList.add(Attributes.Needs_maintenance);
        this.positiveList.add(Attributes.Recommended_for_kids);

        this.shortDescription = "&lt;p&gt;Ein Spaziergang führt Euch durch die Geschichte der\n" +
                "Wollankstraße in Berlin, die auch durch die Berliner Mauer stark\n" +
                "geprägt wurde. Die angegebenen Koordinaten markieren den\n" +
                "Ausgangspunkt des Spazierganges, den S-Bahnhof Wollankstraße.&lt;/p&gt;\n" +
                "&lt;!-- Ende Kurzbeschreibung --&gt;";

        this.longDescription = "&lt;p align=\"justify\"&gt;Zunächst folgt eine kleine Beschreibung zur\n" +
                "   Geschichte dieser Straße, das ist jedoch keine\n" +
                "   Pflichtlektüre.&lt;br&gt;\n" +
                "Zum Loggen des Caches genügt die Beantwortung aller sieben Fragen.\n" +
                "Das dürfte nach einem Spaziergang von ca. 45 Minuten und einer\n" +
                "kurzen Denkpause nicht schwer fallen. Ersetzt die fehlenden\n" +
                "Variablen und errechnet mit der unten aufgeführten Formel die\n" +
                "Zielkoordinate.&lt;/p&gt;\n" +
                "&lt;font color=\"red\"&gt;Bitte achtet darauf, dass der Cache immer wieder\n" +
                "genau an dieser Stelle befestigt wird und&lt;br&gt;\n" +
                "dadurch immer mit einem SCHNELLEN und BEQUEMEN Zugriff zu loggen\n" +
                "ist!!!&lt;/font&gt; &lt;!-- Beginn Geschichte --&gt;\n" +
                "&lt;p&gt;&amp;nbsp;&lt;/p&gt;\n" +
                "&lt;p&gt;&lt;b&gt;Geschichte&lt;/b&gt;&lt;/p&gt;\n" +
                "&lt;p align=\"justify\"&gt;Die Berliner Wollankstraße ist eine ganz normale\n" +
                "Straße in Berlin. Wirklich eine ganz normale Straße? Auf den\n" +
                "zweiten Blick ist sie es tatsächlich nicht, denn es gibt viele\n" +
                "Kleinode und Geschichten, die sich dem Betrachter erst auf den\n" +
                "zweiten Blick erschließen.&lt;/p&gt;\n" +
                "&lt;p align=\"justify\"&gt;Die Straße ist seit 1882 nach dem Gutsbesitzer\n" +
                "Adolf Friedrich Wollank (1833-1877) benannt. Vorher trug sie die\n" +
                "Namen Prinzenweg (1703-1877) und Prinzenstraße (1877-1882).&lt;/p&gt;\n" +
                "&lt;p align=\"justify\"&gt;Durch diese Straße führte ab dem Jahre 1895 die\n" +
                "erste elektrische Berliner Straßenbahn von Siemens und Halske vom\n" +
                "Gesundbrunnen bis nach Pankow. Im Jahre 1938 wird im Rahmen\n" +
                "Grenzbereinigung der südliche Teil der Wollankstraße dem Bezirk\n" +
                "Wedding (heute Mitte) zugeschlagen. Dies stieß zu dieser Zeit nicht\n" +
                "auf die Gegenliebe der Bewohner, denn der Bezirk Pankow ist eher\n" +
                "bürgerlich geprägt, während der Wedding ein Arbeiterbezirk ist.&lt;/p&gt;\n" +
                "&lt;p align=\"justify\"&gt;Das villenartige Empfangsgebäude des 1903\n" +
                "errichteten Bahnhofes „Pankow Nordbahn“ befindet sich nicht an der\n" +
                "Wollankstraße, sondern etwa einhundert Meter weiter westlich an der\n" +
                "Nordbahnstraße. Die elektrische S-Bahn hält hier bereits seit dem\n" +
                "5. Juni 1925. Den heutigen Namen „Wollankstraße“ trägt dieser\n" +
                "Bahnhof erst seit dem Jahr 1937.&lt;/p&gt;\n" +
                "&lt;p align=\"justify\"&gt;Die Teilung Berlins nach dem zweiten Weltkrieg\n" +
                "und der Bau der Mauer 1961 prägen die Straße bis zum heutigen Tage.\n" +
                "Da die Bezirksgrenzen maßgeblich für die Aufteilung der Stadt in\n" +
                "Besatzungszonen waren, fiel der nördliche Pankower Teil der\n" +
                "Wollankstraße in die sowjetische Zone, während der Weddinger Teil\n" +
                "der französischen Besatzungszone zugeordnet wurde. Im Weddinger\n" +
                "Teil der Wollankstraße wurde die Straßenbahn 1960 stillgelegt, im\n" +
                "Pankower Teil bereits 1953.&lt;/p&gt;\n" +
                "&lt;p align=\"justify\"&gt;Mit dem Bau der Mauer 1961 wurde die\n" +
                "Straßenunterführung an der die S-Bahn-Brücke von den\n" +
                "DDR-Grenztruppen verbarrikadiert. Bis zur Maueröffnung sollte diese\n" +
                "Durchfahrt hermetisch abgeriegelt bleiben. Es gab in diesem Bereich\n" +
                "keinen Grenzübergang. Wer von Pankow nach Wedding wollte, riskierte\n" +
                "an dieser Stelle sein Leben. Es wird berichtet, dass am 27.01.1962\n" +
                "durch Risse und Absenkungen auf dem S-Bahnsteig ein von Wedding in\n" +
                "Richtung Pankow vorgetriebener Fluchttunnel entdeckt wurde, bevor\n" +
                "er zur Flucht benutzt werden konnte. Obwohl der S-Bahnhof und der\n" +
                "gesamte Bahndamm in vollem Umfang zu Pankow gehört, war er für die\n" +
                "Zeit der Mauer ausschließlich für West-Berliner zugänglich.&lt;/p&gt;\n" +
                "&lt;p align=\"justify\"&gt;In den achtziger Jahren wurde die Wollankstraße\n" +
                "im Wedding zurückgebaut und umgestaltet. Dies geschah auch deshalb,\n" +
                "da diese Straße an der Mauer am S-Bahnhof endete und nicht mehr als\n" +
                "Durchgangsstraße fungierte. Die letzten Überreste der\n" +
                "Straßenbahnschienen in Pankow wurden erst nach dem Jahre 2000\n" +
                "entfernt. Durch die unterschiedlichen Entwicklungen vor allen\n" +
                "während der Zeit der Teilung geben die Straßenhälften ein sehr\n" +
                "unterschiedliches Bild ab.&lt;/p&gt;\n" +
                "&lt;p align=\"justify\"&gt;Von den Grenzanlagen der Berliner Mauer ist\n" +
                "heute nur noch wenig zu erkennen. Nur der Todesstreifen, der\n" +
                "östlich des Bahndammes verlief, ist noch zu erahnen.&lt;/p&gt;\n" +
                "&lt;br&gt;\n" +
                " &lt;!-- Ende Geschichte --&gt;\n" +
                " \n" +
                "&lt;p&gt;&lt;b&gt;&amp;nbsp;&lt;/b&gt;&lt;/p&gt;\n" +
                "&lt;table cellpadding=\"0\" cellspacing=\"0\" width=\"99%\"&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"180\" bgcolor=\"#FFFF99\" style=\n" +
                "\"text-align:left; text-indent:6px; margin:0; border-width:1px; border-color:black; border-top-style:solid; border-right-style:none; border-bottom-style:solid; border-left-style:none;\"&gt;\n" +
                "&lt;p&gt;&lt;b&gt;Station 1&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"1058\" bgcolor=\"#FFFF99\" style=\n" +
                "\"margin:0; border-width:1px; border-color:black; border-top-style:solid; border-right-style:none; border-bottom-style:solid; border-left-style:none;\"&gt;\n" +
                "&lt;p&gt;N 52° 33.887 E 013° 23.594&lt;/p&gt;&lt;/td&gt;&lt;/tr&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"1238\" colspan=\"2\"&gt;Am S-Bahnhof ist ein Schild befestigt.\n" +
                "Welche Höhenangabe üNN ist hier angegeben?&lt;br&gt;\n" +
                "&lt;br&gt;\n" +
                "Lösung: 4A,3B4&lt;br&gt;\n" +
                "&amp;nbsp;&lt;/td&gt;&lt;/tr&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"180\" height=\"16\" bgcolor=\"#FFFF99\" style=\n" +
                "\"text-align:left; text-indent:6px; border-width:1px; border-color:black; border-top-style:solid; border-bottom-style:solid;\"&gt;\n" +
                "&lt;p&gt;&lt;b&gt;Station 2&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"1058\" height=\"16\" bgcolor=\"#FFFF99\" style=\n" +
                "\"border-width:1px; border-color:black; border-top-style:solid; border-bottom-style:solid;\"&gt;\n" +
                "N 52° 33.622 E 013° 23.266&lt;/td&gt;&lt;/tr&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"1238\" colspan=\"2\" height=\"16\"&gt;\n" +
                "&lt;p&gt;In der Wollankstraße befinden sich zwei Friedhöfe. Gehe zum\n" +
                "südlichen Eingang des St. Elisabeth-Friedhofs in der Wollankstraße.\n" +
                "Neben dem sehenswerten Eingangsportal des Friedhofs befindet sich\n" +
                "eine Blumenhandlung. Wann würde dieses Geschäft gegründet?&lt;br&gt;\n" +
                "&lt;br&gt;\n" +
                " Lösung: 1C7D&lt;br&gt;\n" +
                "&amp;nbsp;&lt;/p&gt;&lt;/td&gt;&lt;/tr&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"180\" bgcolor=\"#FFFF99\" height=\"16\" style=\n" +
                "\"border-width:1px; border-color:black; border-top-style:solid; border-bottom-style:solid;\"&gt;\n" +
                "&lt;p&gt;&lt;b&gt;Station 3&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"1058\" bgcolor=\"#FFFF99\" height=\"16\" style=\n" +
                "\"border-width:1px; border-color:black; border-top-style:solid; border-bottom-style:solid;\"&gt;\n" +
                "&lt;p&gt;N 52° 33.724 E 013° 23.382&lt;/p&gt;&lt;/td&gt;&lt;/tr&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"1238\" colspan=\"2\" height=\"16\"&gt;\n" +
                "&lt;p&gt;Die östliche Straßenseite in Mitte ist zu großen Teilen von der\n" +
                "Wohnungsbaugenossenschaft „Vaterländischer Bauverein“ bebaut\n" +
                "worden. Wann wurden die Häuser Wollankstraße 75-83b erbaut?&lt;br&gt;\n" +
                "&lt;br&gt;\n" +
                "Lösung: 1EF6&lt;br&gt;\n" +
                "&amp;nbsp;&lt;/p&gt;&lt;/td&gt;&lt;/tr&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"180\" bgcolor=\"#FFFF99\" height=\"16\" style=\n" +
                "\"border-width:1px; border-color:black; border-top-style:solid; border-bottom-style:solid;\"&gt;\n" +
                "&lt;p&gt;&lt;b&gt;Station 4&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"1058\" bgcolor=\"#FFFF99\" height=\"16\" style=\n" +
                "\"border-width:1px; border-color:black; border-top-style:solid; border-bottom-style:solid;\"&gt;\n" +
                "&lt;p&gt;N 52° 33.795 E 013° 23.458&lt;/p&gt;&lt;/td&gt;&lt;/tr&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"1238\" colspan=\"2\" height=\"16\"&gt;\n" +
                "&lt;p&gt;Etwas weiter nördlich folgt ein Kirchengemeindehaus. Durch den\n" +
                "Bau der Mauer 1961 wurde der Weddinger Teil der\n" +
                "Martin-Luther-Gemeinde von ihrem Gemeindehaus in der Pradelstraße\n" +
                "in Pankow getrennt. Das Haus, vor dem ihr jetzt steht, wurde 1962\n" +
                "als Behelfsbau neu errichtet. Wer hat diesen Neubau und den Bau\n" +
                "einer Kirche (inzwischen abgerissen) gesponsort?&lt;br&gt;\n" +
                "&lt;br&gt;\n" +
                "Lösung:&lt;br&gt;\n" +
                "- US-Präsident J.F.Kennedy (G=9) oder&lt;br&gt;\n" +
                "- Berliner Bürgermeister Willy Brandt (G=7) oder&lt;br&gt;\n" +
                "- schwedische Gräfin Lili Hamilton (G=4)&lt;/p&gt;\n" +
                "&lt;p&gt;Tipp: ganz in der Nähe findet ihr einen kleinen Hinweis...&lt;br&gt;\n" +
                "&amp;nbsp;&lt;/p&gt;&lt;/td&gt;&lt;/tr&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"180\" bgcolor=\"#FFFF99\" height=\"16\" style=\n" +
                "\"border-width:1px; border-color:black; border-top-style:solid; border-bottom-style:solid;\"&gt;\n" +
                "&lt;p&gt;&lt;b&gt;Station 5&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"1058\" bgcolor=\"#FFFF99\" height=\"16\" style=\n" +
                "\"border-width:1px; border-color:black; border-top-style:solid; border-bottom-style:solid;\"&gt;\n" +
                "&lt;p&gt;N 52° 33.882 E 013° 23.616&lt;/p&gt;&lt;/td&gt;&lt;/tr&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"1238\" colspan=\"2\"&gt;\n" +
                "&lt;p&gt;Nun könnt ihr die Straßenseite wechseln und die ehemalige\n" +
                "Sektorengrenze überschreiten, in dem ihr die S-Bahn-Brücken\n" +
                "unterquert. Doch halt! Im Boden ist eine kleine Gedenktafel\n" +
                "eingelassen. Übrigens an der falschen Stelle, sie müsste auf der\n" +
                "südlichen Seite des Bahndammes im Boden versenkt sein! Merkt Euch\n" +
                "bitte die vorletzte Ziffer dieser Inschrift.&lt;br&gt;\n" +
                "&lt;br&gt;\n" +
                "Lösung: H&lt;br&gt;\n" +
                "&amp;nbsp;&lt;/p&gt;&lt;/td&gt;&lt;/tr&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"180\" bgcolor=\"#FFFF99\" style=\n" +
                "\"border-width:1px; border-color:black; border-top-style:solid; border-bottom-style:solid;\"&gt;\n" +
                "&lt;p&gt;&lt;b&gt;Station 6&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"1058\" bgcolor=\"#FFFF99\" height=\"16\" style=\n" +
                "\"border-width:1px; border-color:black; border-top-style:solid; border-bottom-style:solid;\"&gt;\n" +
                "&lt;p&gt;N 52° 33.944 E 013° 23.721&lt;/p&gt;&lt;/td&gt;&lt;/tr&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"1238\" colspan=\"2\"&gt;\n" +
                "&lt;p&gt;Einige Meter weiter befindet sich das Franziskanerkloster. 1921\n" +
                "erwarben die Franziskaner von Breslau hier zwei Grundstücke, um\n" +
                "sich niederzulassen. Die ärmsten der Stadt können sich hier eine\n" +
                "warme Mahlzeit holen, sich waschen oder sich neu einkleiden lassen.\n" +
                "Im Vorgarten befindet sich eine Glocke, die täglich zum Gebet\n" +
                "aufruft. Nenne die erste Ziffer der Hausnummer.&lt;br&gt;\n" +
                "&lt;br&gt;\n" +
                "Lösung: J&lt;br&gt;\n" +
                "&amp;nbsp;&lt;/p&gt;&lt;/td&gt;&lt;/tr&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"180\" bgcolor=\"#FFFF99\" style=\n" +
                "\"border-width:1px; border-color:black; border-top-style:solid; border-bottom-style:solid;\"&gt;\n" +
                "&lt;p&gt;&lt;b&gt;Station 7&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"1058\" bgcolor=\"#FFFF99\" height=\"16\" style=\n" +
                "\"border-width:1px; border-color:black; border-top-style:solid; border-bottom-style:solid;\"&gt;\n" +
                "&lt;p&gt;N 52° 34.110 E 013° 23.979&lt;/p&gt;&lt;/td&gt;&lt;/tr&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"1238\" colspan=\"2\"&gt;\n" +
                "&lt;p&gt;Hier findet Ihr ein sehr altes, wenn nicht sogar das älteste\n" +
                "Gebäude in dieser Straße. Die Anlage wurde um 1860 erbaut und ab\n" +
                "1875 betrieb Carl Hartmann hier ein Geschäft. Um welches Gewerbe\n" +
                "handelte es sich?&lt;br&gt;\n" +
                "&lt;br&gt;\n" +
                "Lösung:&lt;br&gt;\n" +
                "- eine Bank (K=3) oder&lt;br&gt;\n" +
                "- eine Bäckerei (K=2) oder&lt;br&gt;\n" +
                "- ein Museum (K=7)&lt;br&gt;\n" +
                "&amp;nbsp;&lt;/p&gt;&lt;/td&gt;&lt;/tr&gt;&lt;/table&gt;\n" +
                "&lt;p&gt;&lt;!-- Beginn Lösungstabelle --&gt;\n" +
                "&lt;/p&gt;\n" +
                "&lt;table border=\"1\" cellspacing=\"0\" bordercolordark=\"white\"\n" +
                "bordercolorlight=\"black\"&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"45\" height=\"25\" bgcolor=\"#FFFF99\"&gt;\n" +
                "&lt;p align=\"center\"&gt;A&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\" bgcolor=\"#FFFF99\"&gt;\n" +
                "&lt;p align=\"center\"&gt;B&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\" bgcolor=\"#FFFF99\"&gt;\n" +
                "&lt;p align=\"center\"&gt;C&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\" bgcolor=\"#FFFF99\"&gt;\n" +
                "&lt;p align=\"center\"&gt;D&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\" bgcolor=\"#FFFF99\"&gt;\n" +
                "&lt;p align=\"center\"&gt;E&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\" bgcolor=\"#FFFF99\"&gt;\n" +
                "&lt;p align=\"center\"&gt;F&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\" bgcolor=\"#FFFF99\"&gt;\n" +
                "&lt;p align=\"center\"&gt;G&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\" bgcolor=\"#FFFF99\"&gt;\n" +
                "&lt;p align=\"center\"&gt;H&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\" bgcolor=\"#FFFF99\"&gt;\n" +
                "&lt;p align=\"center\"&gt;J&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\" bgcolor=\"#FFFF99\"&gt;\n" +
                "&lt;p align=\"center\"&gt;K&lt;/p&gt;&lt;/td&gt;&lt;/tr&gt;\n" +
                "&lt;tr&gt;\n" +
                "&lt;td width=\"45\" height=\"25\"&gt;\n" +
                "&lt;p align=\"center\"&gt;&lt;b&gt;&amp;nbsp;&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\"&gt;\n" +
                "&lt;p align=\"center\"&gt;&lt;b&gt;&amp;nbsp;&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\"&gt;\n" +
                "&lt;p align=\"center\"&gt;&lt;b&gt;&amp;nbsp;&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\"&gt;\n" +
                "&lt;p align=\"center\"&gt;&lt;b&gt;&amp;nbsp;&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\"&gt;\n" +
                "&lt;p align=\"center\"&gt;&lt;b&gt;&amp;nbsp;&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\"&gt;\n" +
                "&lt;p align=\"center\"&gt;&lt;b&gt;&amp;nbsp;&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\"&gt;\n" +
                "&lt;p align=\"center\"&gt;&lt;b&gt;&amp;nbsp;&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\"&gt;\n" +
                "&lt;p align=\"center\"&gt;&lt;b&gt;&amp;nbsp;&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\"&gt;\n" +
                "&lt;p align=\"center\"&gt;&lt;b&gt;&amp;nbsp;&lt;/b&gt;&lt;/p&gt;&lt;/td&gt;\n" +
                "&lt;td width=\"45\" height=\"25\"&gt;\n" +
                "&lt;p align=\"center\"&gt;&amp;nbsp;&lt;/p&gt;&lt;/td&gt;&lt;/tr&gt;&lt;/table&gt;\n" +
                "&lt;!-- Ende Lösungstabelle --&gt;\n" +
                "&lt;br&gt;\n" +
                "&lt;p&gt;Die Zielkoordinate errechnet sich wie folgt:&lt;/p&gt;\n" +
                "&lt;b&gt;&lt;br&gt;&lt;/b&gt;N 52° 34'(F+H-8)(D+K)(E-9)''&lt;br&gt;\n" +
                "E 13° 23'(C)(G+J+1)(A+B+D)'' &lt;br&gt;\n" +
                "&lt;br&gt;\n" +
                "&lt;br&gt;\n" +
                "&lt;br&gt;\n" +
                "&lt;!-- Beginn Zähler --&gt;\n" +
                " &lt;a rel=\"nofollow\" href=\"http://www.andyhoppe.com/\" target=\"_blank\"\n" +
                "title=\"Counter\"&gt;&lt;img src=\n" +
                "\"http://www.andyhoppe.com/count/?id=1143359603\" border=\"0\"\n" +
                "     alt=\"Counter\"&gt;&lt;/a&gt; &lt;br&gt;\n" +
                " &lt;!-- Ende Zähler --&gt;\n" +
                "&lt;br&gt;\n" +
                "&lt;br&gt;\n" +
                "&lt;br&gt;\n" +
                " \n" +
                "&lt;p&gt;&amp;nbsp;&lt;/p&gt;\n" +
                "&lt;!-- Ende Cachebeschreibung --&gt;&lt;p&gt;Additional Waypoints&lt;/p&gt;AAV272 - Station 1&lt;br /&gt;N 52° 33.888 E 013° 23.594&lt;br /&gt;&lt;br /&gt;ABV272 - Station 2&lt;br /&gt;N 52° 33.622 E 013° 23.266&lt;br /&gt;&lt;br /&gt;ACV272 - Station 3&lt;br /&gt;N 52° 33.724 E 013° 23.382&lt;br /&gt;&lt;br /&gt;ADV272 - Station 4&lt;br /&gt;N 52° 33.795 E 013° 23.458&lt;br /&gt;&lt;br /&gt;AEV272 - Station 5&lt;br /&gt;N 52° 33.882 E 013° 23.616&lt;br /&gt;&lt;br /&gt;AFV272 - Station 6&lt;br /&gt;N 52° 33.944 E 013° 23.721&lt;br /&gt;&lt;br /&gt;AGV272 - Station 7&lt;br /&gt;N 52° 34.110 E 013° 23.979&lt;br /&gt;&lt;br /&gt;";
    }

    @Override
    protected boolean addWaypoints() {
        AbstractWaypoint wp1 = new MutableWaypoint(52.5648, 13.393233, this.id);
        wp1.setGcCode("AAV272");
        wp1.setType(CacheTypes.MultiStage);
        wp1.setTitle("Station 1");
        wp1.setClue("");
        wp1.setUserWaypoint(false);
        wp1.setStart(false);
        this.waypoints.add(wp1);

        AbstractWaypoint wp2 = new MutableWaypoint(52.560367, 13.387767, this.id);
        wp2.setGcCode("ABV272");
        wp2.setType(CacheTypes.MultiStage);
        wp2.setTitle("Station 2");
        wp2.setClue("");
        wp2.setUserWaypoint(false);
        wp2.setStart(false);
        this.waypoints.add(wp2);

        AbstractWaypoint wp3 = new MutableWaypoint(52.562067, 13.3897, this.id);
        wp3.setGcCode("ACV272");
        wp3.setType(CacheTypes.MultiStage);
        wp3.setTitle("Station 3");
        wp3.setClue("");
        wp3.setUserWaypoint(false);
        wp3.setStart(false);
        this.waypoints.add(wp3);

        AbstractWaypoint wp4 = new MutableWaypoint(52.56325, 13.390967, this.id);
        wp4.setGcCode("ADV272");
        wp4.setType(CacheTypes.MultiStage);
        wp4.setTitle("Station 4");
        wp4.setClue("");
        wp4.setUserWaypoint(false);
        wp4.setStart(false);
        this.waypoints.add(wp4);

        AbstractWaypoint wp5 = new MutableWaypoint(52.5647, 13.3936, this.id);
        wp5.setGcCode("AEV272");
        wp5.setType(CacheTypes.MultiStage);
        wp5.setTitle("Station 5");
        wp5.setClue("");
        wp5.setUserWaypoint(false);
        wp5.setStart(false);
        this.waypoints.add(wp5);

        AbstractWaypoint wp6 = new MutableWaypoint(52.565733, 13.39535, this.id);
        wp6.setGcCode("AFV272");
        wp6.setType(CacheTypes.MultiQuestion);
        wp6.setTitle("Station 6");
        wp6.setClue("");
        wp6.setUserWaypoint(false);
        wp6.setStart(false);
        this.waypoints.add(wp6);

        AbstractWaypoint wp7 = new MutableWaypoint(52.5685, 13.39965, this.id);
        wp7.setGcCode("AGV272");
        wp7.setType(CacheTypes.MultiStage);
        wp7.setTitle("Station 7");
        wp7.setClue("");
        wp7.setUserWaypoint(false);
        wp7.setStart(false);
        this.waypoints.add(wp7);

        return true;
    }

    @Override
    protected boolean addLogs() throws ParseException {

        LogEntry logEntry1 = new LogEntry();
        logEntry1.CacheId = this.id;
        logEntry1.Finder = "berlingser";
        logEntry1.Type = LogTypes.found;
        logEntry1.Comment = "Danke für die Geschichtsstunde. Den Hinweis an S4 habe ich nicht gefunden. Sonst klappte alles. Die Stationen waren gut ausgearbeitet und gut zu finden.  TFTC und Gruß vom Haarstrang.";
        logEntry1.Timestamp = DATE_PATTERN.parse("2011-08-17T20:20:15Z");
        logEntry1.Id = 180491712L;
        this.logEntries.add(logEntry1);

        LogEntry logEntry2 = new LogEntry();
        logEntry2.CacheId = this.id;
        logEntry2.Finder = "Egon0815";
        logEntry2.Type = LogTypes.found;
        logEntry2.Comment = "TFTC";
        logEntry2.Timestamp = DATE_PATTERN.parse("2011-08-08T19:00:00Z");
        logEntry2.Id = 178523602L;
        this.logEntries.add(logEntry2);

        LogEntry logEntry3 = new LogEntry();
        logEntry3.CacheId = this.id;
        logEntry3.Finder = "themurkel0815";
        logEntry3.Type = LogTypes.found;
        logEntry3.Comment = "Netter Multi, TFTC";
        logEntry3.Timestamp = DATE_PATTERN.parse("2011-08-08T19:00:00Z");
        logEntry3.Id = 178488480L;
        this.logEntries.add(logEntry3);

        LogEntry logEntry4 = new LogEntry();
        logEntry4.CacheId = this.id;
        logEntry4.Finder = "Logan Silver";
        logEntry4.Type = LogTypes.found;
        logEntry4.Comment = "# 372 # 21:00 #\n" +
                "\n" +
                "Der Letzte Cache der heutigen Runde war auch der stationenreichste. Bei zei Stationen haben sich Baldur unsd ich mächtig dumm angestellt und eine der beiden gar nicht gefunden. Durch logisches Rechnen konnten wir aber die potenziellen Finalgegenden stark eingrenzen und das nach Baldurs Vermutung wahrscheinlichere Final war es letztlich dann auch. Das Finale war dann auch unsere schnellste Station. Baldurs GPS hatte ih  selbst gerade erst eingeholt^^ da warf ich ihm schon die Dose zu. Ein wirklich schöner Multi, der uns wieder einmal die Deutsch-Deutsche Geschichte etwas näher bringen konnte.\n" +
                "\n" +
                "Vielen Dank für die Geschichtsstunde und den Cache.\n" +
                "\n" +
                "Logan Silver\n" +
                "\n" +
                "PS: Nochmal zur Klarstellung: Wir haben noch eine leere Seite im Logbuch entdeckt, aber jetzt ist wirklich nur noch ein einziger Platz frei. Also, die nächsten Besucher sollten sicherheitshalber einen Ersatzlogstreifen dabei haben.";
        logEntry4.Timestamp = DATE_PATTERN.parse("2011-07-16T19:00:00Z");
        logEntry4.Id = 174245656L;
        this.logEntries.add(logEntry4);

        LogEntry logEntry5 = new LogEntry();
        logEntry5.CacheId = this.id;
        logEntry5.Finder = "BaldurMorgan";
        logEntry5.Type = LogTypes.found;
        logEntry5.Comment = "# 644 - 21:00 Uhr\n" +
                "\n" +
                "Dieser feine Multi war der letzte auf meiner heutigen Tour zusammen mit LoganSilver. Die Stationen konnten allesamt gut gefunden werden, auch wenn wir bei der Station mit der Höhenangabe erst eine zeitlang Tomaten auf den Augen hatten. Wer der/die Spender/in des Kirchengebäudes war fanden wir jedoch nicht heraus, ermittelten aber mit etwas logischem Denken 2 mögliche Finalorte. Mein Gefühl sagte mir, dass wir zuerst zu dem Einen fahren sollten. Und nach kurzer Suche bewahrheitete sich meine Vorahnung, als LoganSilver die Dose aus ihrem Versteck zog. Durch die zahlreichen Vorlogs, welche von mehreren notwendigen Anläufen berichteten, hätten wir uns die Suche insgesamt etwas schwieriger vorgestellt, aber es lief doch ganz rund [;)]\n" +
                "\n" +
                "TFTC BaldurMorgan";
        logEntry5.Timestamp = DATE_PATTERN.parse("2011-07-16T19:00:00Z");
        logEntry5.Id = 173276929L;
        this.logEntries.add(logEntry5);

        return true;
    }
}
