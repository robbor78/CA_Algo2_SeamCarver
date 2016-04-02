import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private static double ENERGY_BORDER = 1000;
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
                picture.set(x, y, colors[XY2L(x, y)]);
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
        CheckValidIndices(x, y);
        return ea[XY2L(x, y)];
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
        CheckValidRemoveSeam(seam, width, height);

        removeSeam(seam, false);

        height--;
        dim = width * height;

        rebuildEnergyArray(seam, false);

    }

    // remove vertical seam from
    // current picture
    public void removeVerticalSeam(int[] seam) {
        CheckValidRemoveSeam(seam, height, width);

        removeSeam(seam, true);

        width--;
        dim = width * height;

        rebuildEnergyArray(seam, true);

    }

    private void removeSeam(int[] seam, boolean isVertical) {
        double nea[] = new double[dim - 1];
        Color ncolors[] = new Color[dim - 1];
        int lLast = 0;
        int length = seam.length;
        for (int x = 0; x < length; x++) {
            int y = seam[x];
            int lNow = isVertical ? XY2L(y, x) : XY2L(x, y);

            for (int l = lLast; l < lNow; l++) {
                nea[l] = ea[l];
                ncolors[l] = colors[l];
            }
            lLast = lNow;

        }

    }

    private void rebuildEnergyArray(int[] seam, boolean isVertical) {
        int length = seam.length;
        for (int i = 0; i < length; i++) {
            int j = seam[i];

            int x = isVertical ? j : i;
            int y = isVertical ? j : i;

            if (y == height) {
                ea[XY2L(x, y - 1)] = ENERGY_BORDER;
            } else {
                buildEnergyEntry(x, y);
                buildEnergyEntry(x, y - 1);
                buildEnergyEntry(x - 1, y);
                buildEnergyEntry(x + 1, y);
            }
        }
    }

    private void CheckValidRemoveSeam(int[] seam, int expectedSeamLength,
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

    private void CheckValidIndices(int x, int y) {
        CheckValidIndex(x, width);
        CheckValidIndex(y, height);
    }

    private void CheckValidIndex(int i, int max) {
        if (i < 0 || i >= max) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    private void buildColorArray(Picture picture) {
        colors = new Color[dim];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                colors[XY2L(x, y)] = picture.get(x, y);
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
        double val;
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
            val = ENERGY_BORDER;
        } else {
            int xgrad = grad(colors[XY2L(x - 1, y)], colors[XY2L(x + 1, y)]);
            int ygrad = grad(colors[XY2L(x, y - 1)], colors[XY2L(x, y + 1)]);
            val = Math.sqrt(xgrad + ygrad);
        }
        ea[XY2L(x, y)] = val;
    }

    private int grad(Color c1, Color c2) {
        int dr = c1.getRed() - c2.getRed();
        int dg = c1.getGreen() - c2.getGreen();
        int db = c1.getBlue() - c2.getBlue();
        return dr * dr + dg * dg + db * db;
    }

    private int[] findSeam(int width, int height, boolean isTranspose) {
        int edgeTo[][] = new int[width][];
        double distTo[][] = new double[width][];

        for (int x = 0; x < width; x++) {
            edgeTo[x] = new int[height];
            edgeTo[x][0] = 0;

            distTo[x] = new double[2];
            distTo[x][0] = ENERGY_BORDER;
            distTo[x][1] = Double.POSITIVE_INFINITY;
        }

        MinPair mp = new MinPair();
        for (int y = 1; y < height; y++) {
            mp.x = 0;
            mp.dist = Double.POSITIVE_INFINITY;
            for (int x = 0; x < width; x++) {

                relax(distTo, edgeTo, width, isTranspose, x - 1, x, y, mp);
                relax(distTo, edgeTo, width, isTranspose, x, x, y, mp);
                relax(distTo, edgeTo, width, isTranspose, x + 1, x, y, mp);

            }

            resetDistTo(distTo, width, y);
        }

        int result[] = new int[height];

        int x = mp.x;
        for (int y = height - 1; y >= 0; y--) {
            int tmpX = edgeTo[x][y];
            result[y] = x;
            x = tmpX;
        }

        return result;
    }

    private void resetDistTo(double[][] distTo, int width, int y) {
        int prevY = (y - 1) % 2;
        for (int x = 0; x < width; x++) {
            distTo[x][prevY] = Double.POSITIVE_INFINITY;
        }
    }

    private void relax(double distTo[][], int edgeTo[][], int width,
            boolean isTranspose, int prevX, int x, int y, MinPair mp) {

        if (prevX < 0 || prevX >= width) {
            return;
        }

        int prevY = (y - 1) % 2;
        int distToY = y % 2;

        double energy = isTranspose ? ea[XY2L(y, x)] : ea[XY2L(x, y)];

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

    private int XY2L(int x, int y) {
        return x + y * width;
    }

    private XY L2XY(int l) {
        XY xy = new XY();

        xy.x = l % width;
        xy.y = (int) ((double) l / (double) (width));

        return xy;
    }

    private class MinPair {
        private int x;
        private double dist;
    }

    private class XY {
        private int x;
        private int y;
    }

}