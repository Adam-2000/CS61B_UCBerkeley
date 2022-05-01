public class Palindrome {

    public Deque<Character> wordToDeque(String word) {
        Deque<Character> retDeq = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            retDeq.addLast(word.charAt(i));
        }
        return retDeq;
    }

    public boolean isPalindrome(String word) {
        return isPalindrome(wordToDeque(word));
    }

    private boolean isPalindrome(Deque q) {
        if (q.size() == 0 || q.size() == 1) {
            return true;
        }
        if (q.removeFirst() == q.removeLast()) {
            return isPalindrome(q);
        }
        return false;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        return isPalindrome(wordToDeque(word), cc);
    }

    private boolean isPalindrome(Deque<Character> q, CharacterComparator cc) {
        if (q.size() == 0 || q.size() == 1) {
            return true;
        }
        if (cc.equalChars(q.removeFirst(), q.removeLast())) {
            return isPalindrome(q, cc);
        }
        return false;
    }
}
