package starwars.actions;

import edu.monash.fit2099.simulator.time.Scheduler;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.*;
import starwars.entities.actors.Droid;
import starwars.entities.actors.Sandcrawler;
import starwars.swinterfaces.SWGridController;
import starwars.swinterfaces.SWGridTextInterface;
import starwars.userinterfaces.TextInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>SWAction</code> that lets a <code>SWActor</code> exit a Sandcrawler.
 *
 * @author omar0003
 *
 */
public class Exit extends SWAffordance{
    /**
     * Constructor for the <code>Exit</code> Class. Will initialize the message renderer, the target and
     * set the priority of this <code>Action</code> to 1 (lowest priority is 0).
     *
     * @param theTarget a <code>SWEntity</code> that is a door
     * @param m the message renderer to display messages
     */
    public Exit(SWEntityInterface theTarget, MessageRenderer m) {
        super(theTarget, m);
        priority = 1;
    }
    /**
     * Returns if or not this <code>Exit</code> can be performed by the <code>SWActor a</code>.
     * <p>
     * This method returns true if and only if <code>a</code> has a force level higher than 60.
     *
     * @author 	omar0003
     * @param 	a the <code>SWActor</code> being queried
     * @return 	true if the <code>SWActor</code> can exit the sandcrawler, false otherwise
     */
    @Override
    public boolean canDo(SWActor a) {
        return a.getForce() >= 60;
    }
    /**
     * Perform the <code>Exit</code> action by restoring the outside world.
     * This method should only be called if the <code>SWActor a</code> is alive.
     *
     * @author 	omar0003
     * @param 	a the <code>SWActor</code> that is exiting
     * @see		{@link starwars.SWActor#isDead()}
     */
    @Override
    public void act(SWActor a) {
        if (target instanceof SWEntityInterface) {
            if(canDo(a)){
                ArrayList<Droid> droidArrayList = new ArrayList<Droid>();
                SWLocation sandcrawlerLoc = Sandcrawler.getMyLoc();
                SWWorld world = Application.getSWWorld();
                List<SWEntityInterface> contents = SandcrawlerWorld.getEntitymanager().contents(SandcrawlerWorld.getGrid().getLocationByCoordinates(0,0));
                for(SWEntityInterface i: contents){
                    if(i instanceof Droid){
                        droidArrayList.add((Droid)i);
                    }
                }
                Sandcrawler.setDroids(droidArrayList);
                    a.setWorld(world);
                Scheduler theScheduler = Application.getScheduler();
                SWActor.setScheduler(theScheduler);
                SWWorld.getEntitymanager().setLocation(a,sandcrawlerLoc);
                SWGridTextInterface test = new SWGridTextInterface(world.getMyGrid(), false);

                while(true) {
                    test.displayMap();
                    theScheduler.tick();
                }
            }
        }
    }
    /**
     * A String describing what this action will do, suitable for display in a user interface
     *
     * @author omar0003
     * @return String comprising "Exit " and the short description of the target of this <code>Enter</code>
     */
    @Override
    public String getDescription() {
        return "Exit Sandcrawler";
    }

}
