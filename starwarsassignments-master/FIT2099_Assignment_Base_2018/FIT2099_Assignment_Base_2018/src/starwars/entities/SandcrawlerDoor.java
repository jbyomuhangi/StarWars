package starwars.entities;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWEntity;
import starwars.SWWorld;
import starwars.actions.Exit;

/**
 * An entity that allows SWActors to leave a sandcrawler, if they have a high enough force level.
 *
 * @author 	omar0003
 */

public class SandcrawlerDoor extends SWEntity {
    /** the  world the door belongs to */
    private SWWorld myWorld;
    /**
     * constructor for the door object, initializes everything
     * @param m message renderer
     * @param myWorld the world the door is in
     */
    public SandcrawlerDoor(MessageRenderer m, SWWorld myWorld) {
        super(m);
        this.shortDescription = "A door";
        this.longDescription = "A door";
        this.myWorld = myWorld;
        this.addAffordance(new Exit(this, messageRenderer));
    }
    public String getSymbol() {
        return "D";
    }
    public SWWorld getWorld() {return this.myWorld;}
}
