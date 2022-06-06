package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    int period;
    int state;
    double accRatio;

    public AcceleratingSawToothGenerator(int period, double accRatio) {
        state = 0;
        this.period = period;
        this.accRatio = accRatio;
    }
    @Override
    public double next() {
        if (++state == period) {
            state = 0;
            period = (int) Math.floor(period * accRatio);
        }
        return (state % period) / (double) period * 2 - 1;
    }
}
