public class OffByN implements CharacterComparator {

    private int n;
    public OffByN(int N) {
        n = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        if(x > y) {
            return x - y == n;
        } else {
            return y - x == n;
        }
        // return x - y == n || x - y == -n;
    }
}
