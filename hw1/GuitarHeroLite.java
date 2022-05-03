import java.util.Map;

/** A client that uses the synthesizer package to replicate a plucked guitar string sound */
public class GuitarHeroLite {
    private static final double CONCERT_A = 440.0;
    private static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        // synthesizer.GuitarString stringA = new synthesizer.GuitarString(CONCERT_A);
        // synthesizer.GuitarString stringC = new synthesizer.GuitarString(CONCERT_C);
        synthesizer.GuitarString[] strings = new synthesizer.GuitarString[10];
        Map<Character, Integer> keyMap = Map.of('q', 0, '2', 1, 'w', 2, 'e', 3, '4', 4,
                                        'r', 5, '5', 6, 't', 7, '6', 8, 'y', 9);
        for (int i = 0; i < 10; i++) {
            strings[i] = new synthesizer.GuitarString(CONCERT_A * Math.pow(2, (i - 24.0) / 12.0));
        }
        while (true) {
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (keyMap.containsKey(key)) {
                    strings[keyMap.get(key)].pluck();
                }
            }

        /* compute the superposition of samples */
            double sample = 0;
            for (synthesizer.GuitarString s : strings) {
                sample += s.sample();
            }

        /* play the sample on standard audio */
            StdAudio.play(sample);

        /* advance the simulation of each guitar string by one step */
            for (synthesizer.GuitarString s : strings) {
                s.tic();
            }
        }
    }
}

