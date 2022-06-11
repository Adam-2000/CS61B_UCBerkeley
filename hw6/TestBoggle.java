import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.List;


public class TestBoggle {
    @Test
    public void testComparator() {
        String[] ss = new String[] {"", "app", "hell", "hello", "apple",
                                    "smacks", "humane", "ascent", "applet"};
        Comparator<String> comp = new Boggle.StringComparator();
        for (int i = 0; i < ss.length - 1; i++) {
            assertTrue(comp.compare(ss[i], ss[i + 1]) < 0);
        }
    }
    @Test
    public void test1() {
        Boggle.dictPath = "words.txt";
        List<String> res1 = Boggle.solve(7, "exampleBoard.txt");
        String[] expected1 = new String[]{"thumbtacks", "thumbtack",
                                          "setbacks", "setback", "ascent", "humane", "smacks"};
        int i = 0;
        System.out.println(res1);
        for (String s : res1) {
            System.out.println(s);
            assertEquals(expected1[i++], s);
        }
    }
    @Test
    public void test2() {
        Boggle.dictPath = "trivial_words.txt";
        List<String> res2 = Boggle.solve(20, "exampleBoard2.txt");
        String[] expected2 = new String[] {"aaaaa", "aaaa"};
        int i = 0;
        System.out.println(res2);
        for (String s : res2) {
            System.out.println(s);
            assertEquals(expected2[i++], s);
        }
    }
    @Test
    public void main() {
        testComparator();
        test1();
        test2();
    }
}
