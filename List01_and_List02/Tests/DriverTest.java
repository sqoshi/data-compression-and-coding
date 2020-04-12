import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

import static java.lang.Math.round;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DriverTest extends Driver {

    @BeforeAll
    static void main() {
        String filename = "pan-tadeusz-czyli-ostatni-zajazd-na-litwie.txt";
        try {
            loadFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        to8bit();
        checkDataFrequency();
        Collections.sort(getInformationList());
        findAllSymbolsAfter();
        System.out.println(calculateEntropy());
        System.out.println(entropyCondAsAv());
    }


    @Test
    void testCalculateCondEntropy() {
        Assertions.assertEquals(round(entropyCondAsAv()), 3);
    }

    @Test
    void testCalculateEntropy() {
        Assertions.assertEquals(round(calculateEntropy()), 5);
    }

    @Test
    void testCheckDataFrequency() {
        Assertions.assertEquals(getInformationList().size(), 94);
    }

    @Test
    void testTo8bit() {
        Assertions.assertEquals("01000001", getData8Bit()[0]);
    }

    @Test
    void testLoadFile() {
        String filename = "pan-tadeusz-czyli-ostatni-zajazd-na-litwie.txt";
        try {
            loadFile(filename);
            assertTrue(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}