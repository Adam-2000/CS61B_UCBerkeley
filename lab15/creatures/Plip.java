package creatures;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;
import java.awt.Color;
import java.util.Map;
import java.util.List;

/** An implementation of a motile pacifist photosynthesizer.
 *  @author Josh Hug
 */
public class Plip extends Creature {

    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;

    private static final double MOVE_ENERGY = 0.15;
    private static final double STAY_ENERGY_GAIN = 0.2;
    private static final double MAX_ENERGY = 2.0;
    private static final int GREEN_BASE = 63;
    private static final int GREEN_MAX = 255;
    private static final int RED = 99;
    private static final int BLUE = 76;
    private static final double REPLICATE_THRESHOLD = 1.0;
    private static final double FEAR_PROBABILITY = 0.5;
    /** creates plip with energy equal to E. */
    public Plip(double e) {
        super("plip");
        r = RED;
        g = GREEN_BASE;
        b = BLUE;
        energy = e;
    }

    /** creates a plip with energy equal to 1. */
    public Plip() {
        this(1);
    }

    /** Should return a color with red = 99, blue = 76, and green that varies
     *  linearly based on the energy of the Plip. If the plip has zero energy,
     *  it should have a green value of 63. If it has max energy, it should
     *  have a green value of 255. The green value should vary with energy
     *  linearly in between these two extremes. It's not absolutely vital
     *  that you get this exactly correct.
     */
    public Color color() {
        g = GREEN_BASE + (int) ((GREEN_MAX - GREEN_BASE) * (energy / MAX_ENERGY));
        return color(r, g, b);
    }

    /** Do nothing with C, Plips are pacifists. */
    public void attack(Creature c) {
    }

    /** Plips should lose 0.15 units of energy when moving. If you want to
     *  to avoid the magic number warning, you'll need to make a
     *  private static final variable. This is not required for this lab.
     */
    public void move() {
        energy -= MOVE_ENERGY;
    }


    /** Plips gain 0.2 energy when staying due to photosynthesis. */
    public void stay() {
        energy += STAY_ENERGY_GAIN;
        energy = Math.min(energy, MAX_ENERGY);
    }

    /** Plips and their offspring each get 50% of the energy, with none
     *  lost to the process. Now that's efficiency! Returns a baby
     *  Plip.
     */
    public Plip replicate() {
        energy *= 0.5;
        return new Plip(energy);
    }

    /** Plips take exactly the following actions based on NEIGHBORS:
     *  1. If no empty adjacent spaces, STAY.
     *  2. Otherwise, if energy >= 1, REPLICATE.
     *  3. Otherwise, if any Cloruses, MOVE with 50% probability.
     *  4. Otherwise, if nothing else, STAY
     *
     *  Returns an object of type Action. See Action.java for the
     *  scoop on how Actions work. See SampleCreature.chooseAction()
     *  for an example to follow.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        if (empties.size() == 0) {
            return new Action(Action.ActionType.STAY);
        } else if (energy > REPLICATE_THRESHOLD) {
            Direction repDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, repDir);
        } else {
            List<Direction> cloruses = getNeighborsOfType(neighbors, "clorus");
            if (cloruses.size() > 0) {
                if (HugLifeUtils.random() < FEAR_PROBABILITY) {
                    Direction moveDir = HugLifeUtils.randomEntry(empties);
                    return new Action(Action.ActionType.MOVE, moveDir);
                }
            }
        }
        return new Action(Action.ActionType.STAY);
    }

}
