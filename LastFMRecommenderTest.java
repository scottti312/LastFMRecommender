import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class LastFMRecommenderTest {

    private LastFMRecommender recommender;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        recommender = new LastFMRecommender();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        recommender = null;
        System.setOut(originalOut);
    }

    @Test
    public void testListFriends1() throws IOException {
        recommender.listFriends(5);
        assertEquals("[228, 725, 831, 1271, 1310, 1481, 1785]", outContent.toString());
    }

    @Test
    public void testListFriends2() throws IOException {
        recommender.listFriends(2);
        assertEquals("[275, 428, 515, 761, 831, 909, 1209, 1210, 1230, 1327, 1585, 1625, 1869]", outContent.toString());
    }

    @Test
    public void testCommonFriends1() throws IOException {
        recommender.commonFriends(129, 179);
        assertEquals("[911, 527, 1300, 1820, 1453, 816, 437, 310, 1356, 732, 1503, 609, 1895, 1771, 1773, 2037, 503, 1150]", outContent.toString());
    }

    @Test
    public void testCommonFriends2() throws IOException {
        recommender.commonFriends(129, 169);
        assertEquals("[]", outContent.toString());
    }

    @Test
    public void testListArtists1() throws IOException {
        recommender.listArtists(2, 4);
        assertEquals("[51, 53, 64, 65, 70, 72, 77]", outContent.toString());
    }

    @Test
    public void testListArtists2() throws IOException {
        recommender.listArtists(2, 3);
        assertEquals("[]", outContent.toString());
    }

    @Test
    public void testListTop10() throws IOException {
        recommender.listTop10();
        assertEquals("Artist: Britney Spears, Weight: 2393140\n" +
                "Artist: Depeche Mode, Weight: 1301308\n" +
                "Artist: Lady Gaga, Weight: 1291387\n" +
                "Artist: Christina Aguilera, Weight: 1058405\n" +
                "Artist: Paramore, Weight: 963449\n" +
                "Artist: Madonna, Weight: 921198\n" +
                "Artist: Rihanna, Weight: 905423\n" +
                "Artist: Shakira, Weight: 688529\n" +
                "Artist: The Beatles, Weight: 662116\n" +
                "Artist: Katy Perry, Weight: 532545\n", outContent.toString());
    }

    @Test
    public void testRecommend10_1() throws IOException {
        recommender.recommend10(2);
        assertEquals("Artist: Duran Duran, Weight: 207695\n" +
                "Artist: Depeche Mode, Weight: 42140\n" +
                "Artist: Madonna, Weight: 39505\n" +
                "Artist: Panic! At the Disco, Weight: 39369\n" +
                "Artist: Rammstein, Weight: 36956\n" +
                "Artist: U2, Weight: 26447\n" +
                "Artist: The Cure, Weight: 20776\n" +
                "Artist: Pet Shop Boys, Weight: 20596\n" +
                "Artist: Simple Minds, Weight: 14638\n" +
                "Artist: Kylie Minogue, Weight: 14089\n", outContent.toString());
    }

    @Test
    public void testRecommend10_2() throws IOException {
        recommender.recommend10(4);
        assertEquals("Artist: Depeche Mode, Weight: 153862\n" +
                "Artist: Marc Almond, Weight: 28945\n" +
                "Artist: And One, Weight: 20702\n" +
                "Artist: Dave Gahan, Weight: 12433\n" +
                "Artist: Soft Cell, Weight: 12350\n" +
                "Artist: Pet Shop Boys, Weight: 6313\n" +
                "Artist: Coldplay, Weight: 6052\n" +
                "Artist: Erasure, Weight: 6037\n" +
                "Artist: Nitzer Ebb, Weight: 5941\n" +
                "Artist: Paul Anka, Weight: 5399\n", outContent.toString());
    }
}

