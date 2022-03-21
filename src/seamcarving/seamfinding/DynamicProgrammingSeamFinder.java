package seamcarving.seamfinding;

import graphs.Edge;
import seamcarving.Picture;
import seamcarving.SeamCarver;
import seamcarving.energy.EnergyFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 * @see SeamCarver
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        double [][] pixelGraph = new double[picture.width()][picture.height()];
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < picture.height(); i++) {
            pixelGraph[0][i] = f.apply(picture, 0, i);
        }
        // Starting from the next-rightmost column...
        for (int x = 1; x < picture.width(); x++) {
            for (int y = 0; y < picture.height(); y++) {
                double min = Double.MAX_VALUE;
                for (int z = y - 1; z <= y + 1; z++) {
                    if (z >= 0 && z < picture.height()) {
                        min = Math.min(pixelGraph[x-1][z], min);
                    }
                }
                pixelGraph[x][y] = min + f.apply(picture, x, y);
            }
        }
        // seamfinding
        int yMin = 0;
        double min = Double.MAX_VALUE;
        // find smallest energy pixel on right edge
        for (int y = 0; y < picture.height(); y++) {
            if (pixelGraph[picture.width()-1][y] < min) {
                min = pixelGraph[picture.width()-1][y];
                yMin = y;
            }
        }
        result.add(yMin);
        // find seam centered around updating yMin
        for (int x = picture.width() - 2; x >= 0; x--) {
            min = Double.MAX_VALUE;
            int start = yMin - 1;
            int end = yMin + 1;
            if (yMin == 0) {
                start = 0;
            } else if (yMin == picture.height() - 1) {
                end = yMin;
            }
            /*for (int z = yMin - 1; z <= yMin + 1; z++) {
                if (z >= 0 && z < picture.height()) {
                    if (pixelGraph[x][z] < min) {
                        // changes min to compare w/ neighbors
                        min = pixelGraph[x][z];
                        yMin = z;
                    }
                }
            }*/
            for (int z = start; z <= end; z++) {
                if (pixelGraph[x][z] < min) {
                    min = pixelGraph[x][z];
                    yMin = z;
                }
            }
            result.add(yMin);
        }
        Collections.reverse(result);
        return result;
    }
}
