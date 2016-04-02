import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private Picture picture;

    // create a seam carver object based on
    // the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new java.lang.NullPointerException();
        }
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return null;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        CheckValidIndices(x, y);
        return Double.NaN;
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
        CheckValidRemoveSeam(seam, picture.height(), picture.width());

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

    private void CheckValidIndices(int x, int y) {
        CheckValidIndex(x, picture.width());
        CheckValidIndex(y, picture.height());
    }

    private void CheckValidIndex(int i, int max) {
        if (i < 0 || i >= max) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }
}