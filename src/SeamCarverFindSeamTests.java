import static org.junit.Assert.*;

import org.junit.Test;

import edu.princeton.cs.algs4.Picture;

public class SeamCarverFindSeamTests {

    @Test
    public void test() {

        String[] filenames = {
                "/run/media/bert/280AC22E0AF59495/coursera/algorithms/2/assignments/2SeamCarving/seamCarving/3x4.png",
                "/run/media/bert/280AC22E0AF59495/coursera/algorithms/2/assignments/2SeamCarving/seamCarving/12x10.png" };

        int seams[][] = { { 0, 1, 1, 0 }, { 6, 7, 7, 6, 6, 7, 7, 7, 8, 7 } };

        int length = seams.length;

        for (int i = 0; i < length; i++) {
            String filename = filenames[i];
            Picture p = new Picture(filename);
            SeamCarver sc = new SeamCarver(p);
            int actual[] = sc.findVerticalSeam();
            int expected[] = seams[i];
            TestEqual(expected, actual);
        }

    }

    private void TestEqual(int[] expected, int[] actual) {
        assertEquals(expected.length, actual.length);
        
        for (int i=0; i<expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

}
