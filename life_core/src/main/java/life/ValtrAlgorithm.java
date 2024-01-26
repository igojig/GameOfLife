package life;


/**
 * MIT License
 * <p>
 * Copyright (c) 2017 Sander Verdonschot
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

//import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;



public class ValtrAlgorithm {

    //    private static final Random RAND = new Random();
    private static final ThreadLocalRandom RAND = ThreadLocalRandom.current();

    public static List<Coordinate> generateRandomConvexPolygon(int vertex_count){
        return generateRandomConvexPolygon(vertex_count, 10);
    }

    public static List<Coordinate> generateRandomConvexPolygon(int vertex_count, int value_range) {
        // Generate two lists of random X and Y coordinates
        List<Integer> xPool = new ArrayList<>(vertex_count);
        List<Integer> yPool = new ArrayList<>(vertex_count);

        for (int i = 0; i < vertex_count; i++) {
            xPool.add(RAND.nextInt(value_range));
            yPool.add(RAND.nextInt(value_range));
        }

        // Sort them
        Collections.sort(xPool);
        Collections.sort(yPool);

        // Isolate the extreme points
        Integer minX = xPool.get(0);
        Integer maxX = xPool.get(vertex_count - 1);
        Integer minY = yPool.get(0);
        Integer maxY = yPool.get(vertex_count - 1);

        // Divide the interior points into two chains & Extract the vector components
        List<Integer> xVec = new ArrayList<>(vertex_count);
        List<Integer> yVec = new ArrayList<>(vertex_count);

        int lastTop = minX, lastBot = minX;

        for (int i = 1; i < vertex_count - 1; i++) {
            int x = xPool.get(i);

            if (RAND.nextBoolean()) {
                xVec.add(x - lastTop);
                lastTop = x;
            } else {
                xVec.add(lastBot - x);
                lastBot = x;
            }
        }

        xVec.add(maxX - lastTop);
        xVec.add(lastBot - maxX);

        int lastLeft = minY, lastRight = minY;

        for (int i = 1; i < vertex_count - 1; i++) {
            int y = yPool.get(i);

            if (RAND.nextBoolean()) {
                yVec.add(y - lastLeft);
                lastLeft = y;
            } else {
                yVec.add(lastRight - y);
                lastRight = y;
            }
        }

        yVec.add(maxY - lastLeft);
        yVec.add(lastRight - maxY);

        // Randomly pair up the X- and Y-components
        Collections.shuffle(yVec);

        // Combine the paired up components into vectors
        List<Coordinate> vec = new ArrayList<>(vertex_count);

        for (int i = 0; i < vertex_count; i++) {
            vec.add(new Coordinate(xVec.get(i), yVec.get(i)));
        }

        // Sort the vectors by angle
        vec.sort(Comparator.comparingDouble(v -> Math.atan2(v.y, v.x)));

        // Lay them end-to-end
        int x = 0, y = 0;
        int minPolygonX = 0;
        int minPolygonY = 0;
        List<Coordinate> points = new ArrayList<>(vertex_count);

        for (int i = 0; i < vertex_count; i++) {
            points.add(new Coordinate(x, y));

            x += vec.get(i).x;
            y += vec.get(i).y;

            minPolygonX = Math.min(minPolygonX, x);
            minPolygonY = Math.min(minPolygonY, y);
        }

        // Move the polygon to the original min and max coordinates
        int xShift = minX - minPolygonX;
        int yShift = minY - minPolygonY;

        for (int i = 0; i < vertex_count; i++) {
            Coordinate p = points.get(i);
            points.set(i, new Coordinate(p.x + xShift, p.y + yShift));
        }

        return points;
    }

    static boolean isInsideByEvenOddRule(Coordinate coordinate, List<Coordinate> polygon){
        return isInsideByEvenOddRule(coordinate.x, coordinate.y, polygon);
    }

    // java implementation of https://en.wikipedia.org/wiki/Even–odd_rule
    static boolean isInsideByEvenOddRule(int x, int y,  List<Coordinate> polygon){
        boolean result = false;
        int j = polygon.size() - 1;
        for (int i = 0; i < polygon.size(); i++) {
            if ((polygon.get(i).y > y) != (polygon.get(j).y > y) &&
                    (x < polygon.get(i).x + (polygon.get(j).x - polygon.get(i).x) *
                            (y - polygon.get(i).y) / (polygon.get(j).y - polygon.get(i).y))) {
                result = !result;
            }
            j = i;
        }
        return result;
    }
}



