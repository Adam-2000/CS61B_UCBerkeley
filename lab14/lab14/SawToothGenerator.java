package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {

    int period;
    int state;

    public SawToothGenerator(int period) {
        state = 0;
        this.period = period;
    }
    @Override
    public double next() {
        return (++state % period) / (double) period * 2 - 1;
    }
}
