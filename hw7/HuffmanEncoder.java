import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : inputSymbols) {
            if (!map.containsKey(c)) {
                map.put(c, 1);
            } else {
                map.put(c, map.get(c) + 1);
            }
        }
        return map;
    }
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException();
        }
        char[] input = FileUtils.readFile(args[0]);
        int nSymbols = input.length;
        Map<Character, Integer> freqTable = buildFrequencyTable(input);
        BinaryTrie bTrie = new BinaryTrie(freqTable);

        ObjectWriter writer = new ObjectWriter(args[0] + ".huf");
        writer.writeObject(bTrie);
        writer.writeObject(nSymbols);
        Map<Character, BitSequence> lut = bTrie.buildLookupTable();
        List<BitSequence> bitSeqList = new ArrayList<>();

        for (char c : input) {
            bitSeqList.add(lut.get(c));
        }

        writer.writeObject(BitSequence.assemble(bitSeqList));

    }
}
