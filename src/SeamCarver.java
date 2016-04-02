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
        if (seam == null) {
            throw new java.lang.NullPointerException();
        }

    }

    // remove vertical seam from
    // current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new java.lang.NullPointerException();
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