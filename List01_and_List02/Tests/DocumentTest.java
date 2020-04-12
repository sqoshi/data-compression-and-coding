import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DocumentTest {


    @BeforeAll
    static void createObject() {
        Document doc = new Document("00000000", 2);
        Assertions.assertTrue(doc instanceof Document);
    }

    @Test
    void testGetAfterSymbolList() {
        Document doc = new Document("00000000", 2);
        Assertions.assertNull(doc.getAfterSymbolList());
    }

    @Test
    void testGetFrequency() {
        Document doc = new Document("00000000", 2);
        Assertions.assertEquals(doc.getFrequency(), 4.144716967857676E-6);
    }

    @Test
    void testSetFrequency() {
    }

    @Test
    void testGetC() {
        Document doc = new Document("00000000", 2);
        Assertions.assertEquals(doc.getC(), "00000000");
    }

    @Test
    void testSetC() {
    }

    @Test
    void testGetRepetitions() {
        Document doc = new Document("00000000", 2);
        Assertions.assertEquals(doc.getRepetitions(), 2);
    }

    @Test
    void testSetRepetitions() {
    }

    @Test
    void testToString() {
        Document doc = new Document("00000000", 2);
        Assertions.assertTrue(true);
    }

    @Test
    void testCompareTo() {
        Document doc = new Document("00000000", 2);
        Document doc2 = new Document("00000001", 3);
        Assertions.assertEquals(-1, doc.getC().compareTo(doc2.getC()));

    }
}