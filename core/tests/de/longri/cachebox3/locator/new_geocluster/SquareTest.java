package de.longri.cachebox3.locator.new_geocluster;

import de.longri.cachebox3.locator.geocluster.ClusteredList;
import de.longri.cachebox3.locator.geocluster.GeoCluster;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.oscim.core.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Longri on 25.12.16.
 */
public class SquareTest {

    static final int arraySize = 100;//5;
    static final double distance = 10;
    static final double tolerance = 0.1;


//    static final int arraySize = 1000;
//    static final double distance = 10;
//    static final double tolerance = 0.1;

    static GeoPoint[][] points;
    static List<GeoPoint> allPoints = new ArrayList<GeoPoint>();


    @BeforeAll
    public static void init() {
        points = new GeoPoint[arraySize][arraySize];
        GeoPoint leftTop = new GeoPoint(60.0, 13.0);

        GeoPoint pX, pY;
        pX = leftTop;
        pY = leftTop;

        for (int x = 0; x < arraySize; x++) {
            for (int y = 0; y < arraySize; y++) {
                allPoints.add(pX);
                points[x][y] = new GeoPoint(pX.latitudeE6, pX.longitudeE6);
                GeoPoint projectedPoint = pX.destinationPoint(distance, 90);
                assertThat("distance", pX.sphericalDistance(projectedPoint), closeTo(distance, tolerance));
                pX = projectedPoint;
            }
            pX = pY = pY.destinationPoint(distance, 180);
        }
    }

    @Test
    public void chkInit() {
        for (int x = 0; x < arraySize - 1; x++) {
            for (int y = 0; y < arraySize - 1; y++) {
                GeoPoint a = points[x][y];
                GeoPoint b = points[x + 1][y];
                assertThat("wrong distance at x/y :" + x + "/" + y,
                        a.sphericalDistance(b), closeTo(distance, tolerance));
                GeoPoint c = points[x][y + 1];
                assertThat("wrong distance at x/y :" + x + "/" + y,
                        a.sphericalDistance(c), closeTo(distance, tolerance));
            }
        }
        assertThat("all List length", allPoints.size(), equalTo(arraySize * arraySize));
    }

    @Test
    public void clusteringNoReduce() {

        ClusteredList allCluster = new ClusteredList();

        for (GeoPoint point : allPoints) {
            allCluster.add(new GeoCluster(point));
        }

        assertThat("cluster size", allCluster.size(), equalTo(allPoints.size()));


        allCluster.getClustertByDistancs(distance - 2);
        assertThat("cluster size", allCluster.size(), equalTo(allPoints.size()));


        allCluster.getClustertByDistancs(distance * 2);
        assertThat("cluster size", allCluster.size(), equalTo((int) (allPoints.size() * 0.26)));

        allCluster.getClustertByDistancs(distance * 4);
        assertThat("cluster size", allCluster.size(), equalTo((int) (allPoints.size() * 0.26)));


    }

}