import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestTrieSet {

    @Test
    public void main() {
        TrieSet trie = new TrieSet();
        trie.put("hello");
        trie.put("apple");
        trie.put("app");
        trie.put("applet");
        trie.put("");
        List<String> list = trie.list();
        System.out.println(list);
//        assertTrue(trie.contains("hello"));
//        assertTrue(trie.contains("apple"));
//        assertTrue(trie.contains("app"));
//        assertTrue(trie.contains("applet"));
//        assertTrue(trie.contains(""));
//        assertFalse(trie.contains("banana"));
//        assertFalse(trie.contains("hell"));
//        assertFalse(trie.contains("apt"));
    }
}
