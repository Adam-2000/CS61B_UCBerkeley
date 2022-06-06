package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    int period;
    int state;

    public StrangeBitwiseGenerator(int period) {
        state = 0;
        this.period = period;
    }
    @Override
    public double next() {
        state += 1;
        return (state & (state >> 3) & (state >> 8) % period) / (double) period * 2 - 1;
    }
}
