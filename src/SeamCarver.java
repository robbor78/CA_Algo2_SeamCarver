import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private static final double ENERGY_BORDER = 1000;
    private int width, height, dim;
    private double[] ea;
    private Color[] colors;

    // create a seam carver object based on
    // the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new java.lang.NullPointerException();
        }

        width = picture.width();
        height = picture.height();
        dim = width * height;

        buildColorArray(picture);
        buildEnergyArray();

    }

    // current picture
    public Picture picture() {
        Picture picture = new Picture(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                picture.set(x, y, colors[xy2l(x, y)]);
            }
        }

        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        checkValidIndices(x, y);
        return ea[xy2l(x, y)];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return findSeam(height, width, true);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findSeam(width, height, false);
    }

    // remove horizontal seam from
    // current picture
    public void removeHorizontalSeam(int[] seam) {
        checkValidRemoveSeam(seam, width, height);

        removeSeamHorizontal(seam);

        rebuildEnergyArray(false, seam);

    }

    // remove vertical seam from
    // current picture
    public void removeVerticalSeam(int[] seam) {
        checkValidRemoveSeam(seam, height, width);

        removeSeamVertical(seam);

        rebuildEnergyArray(true, seam);

    }

    private void checkValidRemoveSeam(int[] seam, int expectedSeamLength,
            int max) {
        if (seam == null) {
            throw new java.lang.NullPointerException();
        }

        if (max <= 1) {
            throw new java.lang.IllegalArgumentException();
        }

        int actualSeamLength = seam.length;
        if (actualSeamLength != expectedSeamLength) {
            throw new java.lang.IllegalArgumentException();
        }

        for (int i = 0; i < actualSeamLength; i++) {
            int val = seam[i];
            if (val < 0 || val >= max) {
                throw new java.lang.IllegalArgumentException();
            }

            if (i > 0) {
                if (Math.abs(val - seam[i - 1]) > 1) {
                    throw new java.lang.IllegalArgumentException();
                }
            }
        }

    }

    private void checkValidIndices(int x, int y) {
        checkValidIndex(x, width);
        checkValidIndex(y, height);
    }

    private void checkValidIndex(int i, int max) {
        if (i < 0 || i >= max) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    private void buildColorArray(Picture picture) {
        colors = new Color[dim];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                colors[xy2l(x, y)] = picture.get(x, y);
            }
        }
    }

    private void buildEnergyArray() {
        ea = new double[dim];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                buildEnergyEntry(x, y);
            }
        }
    }

    private void buildEnergyEntry(int x, int y) {

        if (x < 0 || y < 0 || x >= width || y >= height) {
            return;
        }

        double val;
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
            val = ENERGY_BORDER;
        } else {
            int xgrad = grad(colors[xy2l(x - 1, y)], colors[xy2l(x + 1, y)]);
            int ygrad = grad(colors[xy2l(x, y - 1)], colors[xy2l(x, y + 1)]);
            val = Math.sqrt(xgrad + ygrad);
        }
        ea[xy2l(x, y)] = val;
    }

    private void rebuildEnergyArray(boolean isVertical, int[] seam) {
        int length = seam.length;
        for (int i = 0; i < length; i++) {
            int j = seam[i];

            int x = isVertical ? j : i;
            int y = isVertical ? i : j;

            buildEnergyEntry(x, y);
            buildEnergyEntry(x, y - 1);
            buildEnergyEntry(x, y + 1);
            buildEnergyEntry(x - 1, y);
            buildEnergyEntry(x + 1, y);

        }
    }

    private int grad(Color c1, Color c2) {
        int dr = c1.getRed() - c2.getRed();
        int dg = c1.getGreen() - c2.getGreen();
        int db = c1.getBlue() - c2.getBlue();
        return dr * dr + dg * dg + db * db;
    }

    private int[] findSeam(int w, int h, boolean isTranspose) {
        int[][] edgeTo = new int[w][];
        double[][] distTo = new double[w][];

        for (int x = 0; x < w; x++) {
            edgeTo[x] = new int[h];
            edgeTo[x][0] = 0;

            distTo[x] = new double[2];
            distTo[x][0] = ENERGY_BORDER;
            distTo[x][1] = Double.POSITIVE_INFINITY;
        }

        MinPair mp = new MinPair();
        for (int y = 1; y < h; y++) {
            mp.x = 0;
            mp.dist = Double.POSITIVE_INFINITY;
            for (int x = 0; x < w; x++) {

                relax(distTo, edgeTo, w, isTranspose, x - 1, x, y, mp);
                relax(distTo, edgeTo, w, isTranspose, x, x, y, mp);
                relax(distTo, edgeTo, w, isTranspose, x + 1, x, y, mp);

            }

            resetDistTo(distTo, w, y);
        }

        int[] result = new int[h];

        int x = mp.x;
        for (int y = h - 1; y >= 0; y--) {
            int tmpX = edgeTo[x][y];
            result[y] = x;
            x = tmpX;
        }

        return result;
    }

    private void resetDistTo(double[][] distTo, int w, int y) {
        int prevY = (y - 1) % 2;
        for (int x = 0; x < w; x++) {
            distTo[x][prevY] = Double.POSITIVE_INFINITY;
        }
    }

    private void relax(double[][] distTo, int[][] edgeTo, int w,
            boolean isTranspose, int prevX, int x, int y, MinPair mp) {

        if (prevX < 0 || prevX >= w) {
            return;
        }

        int prevY = (y - 1) % 2;
        int distToY = y % 2;

        double energy = isTranspose ? ea[xy2l(y, x)] : ea[xy2l(x, y)];

        double tmp = distTo[prevX][prevY] + energy;
        if (tmp < distTo[x][distToY]) {
            edgeTo[x][y] = prevX;
            distTo[x][distToY] = tmp;

            if (tmp < mp.dist) {
                mp.x = x;
                mp.dist = tmp;
            }
        }
    }

    private void removeSeamVertical(int[] seam) {

        int nwidth = width - 1;
        int nheight = height;
        int ndim = nwidth * nheight;

        double[] nea = new double[ndim];
        Color[] ncolors = new Color[ndim];

        int lLast = 0;
        int lNew = 0;

        int length = seam.length;
        for (int y = 0; y < length; y++) {
            int x = seam[y];

            int lNow = xy2l(x, y);

            for (int l = lLast; l < lNow; l++) {
                nea[lNew] = ea[l];
                ncolors[lNew] = colors[l];
                lNew++;
            }
            lLast = lNow + 1;

        }

        for (int l = lLast; l < dim; l++) {
            nea[lNew] = ea[l];
            ncolors[lNew] = colors[l];
            lNew++;
        }

        for (int x = 0; x < dim; x++) {
            colors[x] = null;
        }

        ea = null;
        colors = null;

        ea = nea;
        nea = null;

        colors = ncolors;
        ncolors = null;

        width = nwidth;
        height = nheight;
        dim = ndim;

    }

    private void removeSeamHorizontal(int[] seam) {

        int nwidth = width;
        int nheight = height - 1;
        int ndim = nwidth * nheight;

        double[] nea = new double[ndim];
        Color[] ncolors = new Color[ndim];

        int length = seam.length;
        for (int x = 0; x < length; x++) {
            int y = seam[x];

            int yNew = 0;
            for (int yOrig = 0; yOrig < height; yOrig++) {

                if (yOrig == y) {
                    continue;
                }

                int lOrig = xy2l(x, yOrig);
                int lNew = xy2l(x, yNew);

                nea[lNew] = ea[lOrig];
                ncolors[lNew] = colors[lOrig];

                yNew++;
            }
        }

        for (int x = 0; x < dim; x++) {
            colors[x] = null;
        }

        ea = null;
        colors = null;

        ea = nea;
        nea = null;
        colors = ncolors;
        ncolors = null;

        width = nwidth;
        height = nheight;
        dim = ndim;

    }

    private int xy2l(int x, int y) {
        return x + y * width;
    }

    // private XY L2XY(int l) {
    // XY xy = new XY();
    //
    // xy.x = l % width;
    // xy.y = (int) ((double) l / (double) (width));
    //
    // return xy;
    // }

    private class MinPair {
        private int x;
        private double dist;
    }

    // private class XY {
    // private int x;
    // private int y;
    // }

}