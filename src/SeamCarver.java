import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private static double ENERGY_BORDER = 1000;
    private int width, height;
    private Picture picture;
    private double[][] energyArr;

    // create a seam carver object based on
    // the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new java.lang.NullPointerException();
        }

        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();

        buildEnergyArray();
    }

    // current picture
    public Picture picture() {
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
        return energyArr[x][y];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return null;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return null;
    }

    // remove horizontal seam from
    // current picture
    public void removeHorizontalSeam(int[] seam) {
        CheckValidRemoveSeam(seam, picture.width(), picture.height());

        // Throw a java.lang.IllegalArgumentException if
        // removeVerticalSeam() or removeHorizontalSeam()
        // is called with an array of the wrong length or
        // if the array is not a valid seam
        // (i.e., either an entry is outside its prescribed range
        // or two adjacent entries differ by more than 1).

    }

    // remove vertical seam from
    // current picture
    public void removeVerticalSeam(int[] seam) {
        CheckValidRemoveSeam(seam, height, width);

        // Throw a java.lang.IllegalArgumentException if
        // removeVerticalSeam() or removeHorizontalSeam()
        // is called with an array of the wrong length or
        // if the array is not a valid seam
        // (i.e., either an entry is outside its prescribed range
        // or two adjacent entries differ by more than 1).
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

    public static void main(String[] args) {
        testEnergy();
    }

    private static void testEnergy() {
        System.out.println("testEnergy");

        String filename = "/run/media/bert/280AC22E0AF59495/coursera/algorithms/2/assignments/2SeamCarving/seamCarving/3x4.png";

        double[][] ea = { { 1000, 1000, 1000 }, { 1000, 228.53, 1000 },
                { 1000, 228.09, 1000 }, { 1000, 1000, 1000 } };

        Picture p = new Picture(filename);
        SeamCarver sc = new SeamCarver(p);

        for (int x = 0; x < sc.width(); x++) {
            for (int y = 0; y < sc.height(); y++) {
                double actual = sc.energy(x, y);
                double expected = ea[x][y];

                assert Math.abs(actual - expected) < 0.001;
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

    private void buildEnergyArray() {
        energyArr = new double[width][];
        for (int x = 0; x < width; x++) {
            energyArr[x] = new double[height];
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
            int xgrad = grad(picture.get(x - 1, y), picture.get(x + 1, y));
            int ygrad = grad(picture.get(x, y - 1), picture.get(x, y + 1));
            val = Math.sqrt(xgrad + ygrad);
        }
        energyArr[x][y] = val;
    }

    private int grad(Color c1, Color c2) {
        int dr = c1.getRed() - c2.getRed();
        int dg = c1.getGreen() - c2.getGreen();
        int db = c1.getBlue() - c2.getBlue();
        return dr * dr + dg * dg + db * db;
    }

}