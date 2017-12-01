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
package de.longri.cachebox3.gui.widgets;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.kotcrab.vis.ui.VisUI;
import de.longri.cachebox3.CB;
import de.longri.cachebox3.gui.drawables.geometry.Circle;
import de.longri.cachebox3.gui.drawables.geometry.CircularSegment;
import de.longri.cachebox3.gui.drawables.geometry.GeometryDrawable;
import de.longri.cachebox3.gui.drawables.geometry.Ring;
import de.longri.cachebox3.gui.skin.styles.CircularProgressStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Longri on 28.11.2017.
 */
public class CircularProgressWidget extends Widget {

    Logger log = LoggerFactory.getLogger(CircularProgressWidget.class);


    private enum Drawtype {
        UNKNOWN, PROGRESS, READY
    }

    private final CircularProgressStyle style;
    private final float prefSize;
    private int progressMax = -1, progress = -1;

    private Drawtype drawtype = Drawtype.UNKNOWN;

    public CircularProgressWidget() {
        style = VisUI.getSkin().get("circularProgressStyle", CircularProgressStyle.class);
        this.prefSize = CB.getScaledFloat(style.scaledPreferedRadius) * 2;
        setSize(this.prefSize, this.prefSize);
    }

    public void setProgressMax(int value) {
        progressMax = value;
        checkState();
    }

    public void setProgress(int value) {
        progress = (int) (100.0 / (double) progressMax * (double) value);
        checkState();
    }

    private void checkState() {
        if (progressMax < 0) {
            drawtype = Drawtype.UNKNOWN;
        } else if (progress >= progressMax) {
            drawtype = Drawtype.READY;
        } else {
            drawtype = Drawtype.PROGRESS;
        }
    }

    @Override
    public float getPrefWidth() {
        return this.prefSize;
    }

    @Override
    public float getPrefHeight() {
        return this.prefSize;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        switch (drawtype) {
            case UNKNOWN:
                drawUnknown(batch, parentAlpha);
                break;
            case PROGRESS:
                drawProgress(batch, parentAlpha);
                break;
            case READY:
                drawReady(batch, parentAlpha);
                break;
        }
    }

    private void drawReady(Batch batch, float parentAlpha) {

    }

    private GeometryDrawable progressDrawableBorder, progressDrawable;
    private int lastProgress = -1;
    private CircularSegment circSeg;

    private void drawProgress(Batch batch, float parentAlpha) {

        float radius = getPrefHeight();

        if (progressDrawableBorder == null) {
            Ring ring = new Ring(radius, radius, radius - CB.getScaledFloat(2), radius);
            progressDrawableBorder = new GeometryDrawable(ring, style.borderColor.cpy(), radius, radius);
        }

        if (lastProgress != progress) {
            float startAngle = (360 - 3.60f * progress) + 90;
            if (circSeg == null) {
                circSeg = new CircularSegment(radius, radius, radius, startAngle, 90, true);
                progressDrawable = new GeometryDrawable(circSeg, style.backgroundColor.cpy(), radius, radius);
            } else {
                circSeg.setAngles(startAngle, 90, true);
                progressDrawable.setDirty();
            }
            lastProgress = progress;
        }


        progressDrawableBorder.draw(batch, getX(), getY(), getWidth(), getHeight());
        progressDrawable.draw(batch, getX(), getY(), getWidth(), getHeight());

    }

    private GeometryDrawable unknownDrawable;
    private float[] unknownAplhas = new float[]{0, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f, 1.2f, 1.4f, 1.6f, 1.8f, 2.0f};
    private float[] unknownXpos, unknownYpos;
    private float unknownRadius;
    private float rotate = 20;


    @Override
    public void act(float delta) {
        if (this.drawtype == Drawtype.UNKNOWN) {
            rotate += delta * 2;
            if (rotate >= MathUtils.PI2) {
                rotate = 0;
            }

            // set Alphas
            int n = unknownAplhas.length;
            while (n-- > 0) {
                unknownAplhas[n] += 0.05;
                if (unknownAplhas[n] > 2.0) unknownAplhas[n] = 0;
            }

        }
    }

    private void drawUnknown(Batch batch, float parentAlpha) {

        // drawing has animation so call request rendering
        CB.requestRendering();

        if (unknownDrawable == null) {
            unknownRadius = getPrefHeight() / 8;
            Circle circle = new Circle(unknownRadius, unknownRadius, unknownRadius);
            unknownDrawable = new GeometryDrawable(circle, style.unknownColor.cpy(), unknownRadius, unknownRadius);
        }


        // calculate positions
        if (unknownXpos == null) {
            unknownXpos = new float[unknownAplhas.length];
            unknownYpos = new float[unknownAplhas.length];
        }

        double thetaStep = (MathUtils.PI2 / unknownAplhas.length);
        int index = 0;
        float radius = ((this.getPrefHeight() - unknownRadius) / 2);
        float centerX = getX() + radius;
        float centerY = getY() + radius;

        for (float i = 0; index < (unknownAplhas.length); i += thetaStep) {
            unknownXpos[index] = centerX + radius * MathUtils.cos(i + rotate);
            unknownYpos[index++] = centerY + radius * MathUtils.sin(i + rotate);
        }


        int n = unknownXpos.length;
        while (n-- > 0) {
            unknownDrawable.setColor(style.unknownColor.r, style.unknownColor.g, style.unknownColor.b,
                    style.unknownColor.a * parentAlpha * (unknownAplhas[n] > 1 ? 1 : unknownAplhas[n]));
            unknownDrawable.draw(batch, unknownXpos[n], unknownYpos[n], unknownRadius, unknownRadius);
        }
    }

}
