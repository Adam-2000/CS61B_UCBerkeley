public class HuffmanDecoder {
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException();
        }
        ObjectReader reader = new ObjectReader(args[0]);
        BinaryTrie bTrie = (BinaryTrie) reader.readObject();
        int nSymbols = (int) reader.readObject();
        BitSequence bs = (BitSequence) reader.readObject();
        char[] cs = new char[nSymbols];
        for (int i = 0; i < nSymbols; i++) {
            Match m = bTrie.longestPrefixMatch(bs);
            bs = bs.allButFirstNBits(m.getSequence().length());
            cs[i] = m.getSymbol();
        }
        FileUtils.writeCharArray(args[1], cs);
    }
}
