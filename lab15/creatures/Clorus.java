package creatures;
import huglife.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class Clorus extends Creature {

    private static final double MOVE_ENERGY = 0.03;
    private static final double STAY_ENERGY_GAIN = 0.01;
    private static final int GREEN = 0;
    private static final int RED = 34;
    private static final int BLUE = 231;
    private static final double REPLICATE_THRESHOLD = 1.0;
    public Clorus(double e) {
        super("clorus");
        energy = e;
    }
    public Clorus() {
        this(1);
    }


    @Override
    public void move() {
        energy -= MOVE_ENERGY;
    }

    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    @Override
    public Creature replicate() {
        energy *= 0.5;
        return new Clorus(energy);
    }

    @Override
    public void stay() {
        energy += STAY_ENERGY_GAIN;
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");
        if (empties.size() == 0) {
            return new Action(Action.ActionType.STAY);
        } else if (plips.size() > 0) {
            Direction attackDir = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, attackDir);
        } else if (energy > REPLICATE_THRESHOLD) {
            Direction repDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, repDir);
        } else {
            Direction moveDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.MOVE, moveDir);
        }
    }

    @Override
    public Color color() {
        return color(RED, GREEN, BLUE);
    }
}
