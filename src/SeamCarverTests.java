import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.princeton.cs.algs4.Picture;

public class SeamCarverTests {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testEnergy() {
        String filename = "/run/media/bert/280AC22E0AF59495/coursera/algorithms/2/assignments/2SeamCarving/seamCarving/3x4.png";        
        
        double[][] ea = { { 1000, 1000, 1000 }, { 1000, 228.53, 1000 },
                { 1000, 228.09, 1000 }, { 1000, 1000, 1000 } };

        Picture p = new Picture(filename);
        SeamCarver sc = new SeamCarver(p);
        
        assertEquals(3,sc.width());
        assertEquals(4,sc.height());

        for (int x = 0; x < sc.width(); x++) {
            for (int y = 0; y < sc.height(); y++) {
                double actual = sc.energy(x, y);
                double expected = ea[x][y];

                assertEquals(actual,expected,0.001);
            }
        }

    }

}
