package starwars.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.*;

/**
 * <code>SWAction</code> that lets Ben train Luke.
 *
 * This affordance is attached to Ben
 *
 * @author omar0003@monash.edu (omar)
 */
public class Train extends SWAffordance{
    /**
     * Constructor for the <code>Train</code> Class. Will initialize the message renderer, the target and
     * set the priority of this <code>Action</code> to 1 (lowest priority is 0).
     *
     * @param theTarget Ben
     * @param m the message renderer to display messages
     */
    public Train(SWEntityInterface theTarget, MessageRenderer m) {
        super(theTarget, m);
        priority = 1;}
    /**
     * This method checks if luke and ben are in the same location
     *
     * @author 	omar
     * @param 	actor luke's <code>SWActor</code> instance
     * @param   theTarget ben
     * @pre 	this method should only be called if both <code>SWActor</code>s are alive.
     * @return true if they are in the same location, false otherwise.
     */
    public boolean sameLocation(SWActor actor, SWEntityInterface theTarget){
        SWLocation luke = SWWorld.getEntitymanager().whereIs(actor);
        SWLocation ben = SWWorld.getEntitymanager().whereIs(theTarget);
        return luke==ben;
    }
    /**
     * Returns if or not this <code>Train</code> can be performed by the <code>SWActor a</code>.
     * <p>
     * This method returns true if and only if <code>a</code> has not been trained by ben before.
     *
     * @author 	omar
     * @param 	a the <code>SWActor</code> being queried
     * @return 	true if the <code>SWActor</code> is can take this item, false otherwise
     */
    @Override
    public boolean canDo(SWActor a) { return a.getForce()< 60; }
    /**
     * Perform the <code>Train</code> command on Luke.
     * This method checks whether Luke and Ben are in the same location by calling sameLocation for each actor.
     * It then checks if luke has been trained before by Ben, if so do nothing.
     * Otherwise, it increases Luke's force level.
     *
     * @author 	omar
     * @param   luke luke's <code>SWActor</code> instance
     * @pre 	this method should only be called if Ben and Luke are alive
     * @post	if Luke and Ben are in the same location, Luke's force level is set to 60.
     */
    public void act(SWActor luke) {
        if (target instanceof SWEntityInterface) {
            if (sameLocation(luke, this.getTarget())) {
                if (canDo(luke)) {
                    luke.setForce(60);
                }

            }
        }
    }
    /**
     * A String describing what this action will do, suitable for display in a user interface
     *
     * @author ram
     * @return String comprising "train with " and the short description of the target of this <code>Train</code>
     */
    @Override
    public String getDescription() {
        return "train with " + target.getShortDescription();
    }

}
