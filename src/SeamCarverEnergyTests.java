import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.princeton.cs.algs4.Picture;

public class SeamCarverEnergyTests {

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
        String[] filenames = {
                "/run/media/bert/280AC22E0AF59495/coursera/algorithms/2/assignments/2SeamCarving/seamCarving/3x4.png",
                "/run/media/bert/280AC22E0AF59495/coursera/algorithms/2/assignments/2SeamCarving/seamCarving/12x10.png"};

        double[][][] eas = { { { 1000, 1000, 1000, 1000 },
                { 1000, 228.53, 228.09, 1000 }, { 1000, 1000, 1000, 1000 } }, {
                        { 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
                                1000 },
                        { 1000, 218.03, 302.49, 211.42, 278.44, 299.81, 248.94,
                                294.15, 219.97, 1000 },
                        { 1000, 149.7, 251.81, 343.91, 201.02, 218.19, 325.12,
                                198.64, 253.51, 1000 },
                        { 1000, 244.34, 374.68, 253.05, 251.05, 230.37, 158.91,
                                194.5, 150.86, 1000 },
                        { 1000, 283.24, 267.69, 261.13, 290.57, 229.3, 212.26,
                                184.68, 268.54, 1000 },
                        { 1000, 154.42, 254.96, 249.92, 453.32, 279.72, 213.61,
                                326.5, 259.28, 1000 },
                        { 1000, 356.6, 212.97, 211.04, 184.53, 237.31, 259.53,
                                297.71, 227.65, 1000 },
                        { 1000, 155, 173.89, 289.04, 231.94, 203.21, 114.91,
                                99.89, 311.86, 1000 },
                        { 1000, 218.34, 268.38, 269.88, 265.7, 259.03, 138.97,
                                343.3, 168.54, 1000 },
                        { 1000, 283.59, 198.94, 334.67, 256.8, 230, 309.96,
                                267.94, 217.37, 1000 },
                        { 1000, 127.79, 326.78, 210.63, 189.91, 330.31, 194.41,
                                251.26, 219.49, 1000 },
                        { 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
                                1000 }

                }

        };

        int length = filenames.length;

        for (int i = 0; i < length; i++) {

            String filename = filenames[i];
            double[][] ea = eas[i];

            Picture p = new Picture(filename);
            SeamCarver sc = new SeamCarver(p);

            for (int x = 0; x < sc.width(); x++) {
                for (int y = 0; y < sc.height(); y++) {
                    double actual = new BigDecimal(sc.energy(x, y))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    double expected = ea[x][y];

                    assertEquals(expected,actual, 0.001);
                }
            }

        }

    }

}
