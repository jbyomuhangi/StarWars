package starwars.actions;

import edu.monash.fit2099.simulator.time.Scheduler;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.SandcrawlerWorld;
import starwars.entities.actors.Droid;
import starwars.entities.actors.Sandcrawler;
import starwars.swinterfaces.SWGridController;

import java.util.ArrayList;

/**
 * <code>SWAction</code> that lets a <code>SWActor</code> enter a Sandcrawler.
 *
 * @author omar0003
 *
 */
public class Enter extends SWAffordance{
	/**
	 * Constructor for the <code>Enter</code> Class. Will initialize the message renderer, the target and
	 * set the priority of this <code>Action</code> to 1 (lowest priority is 0).
	 *
	 * @param theTarget a <code>SWEntity</code> that is being entered; a sandcrawler
	 * @param m the message renderer to display messages
	 */
	public Enter(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		priority = 1;
	}
	/**
	 * Returns if or not this <code>Enter</code> can be performed by the <code>SWActor a</code>.
	 * <p>
	 * This method returns true if and only if <code>a</code> has a force level higher than 60.
	 *
	 * @author 	omar0003
	 * @param 	a the <code>SWActor</code> being queried
	 * @return 	true if the <code>SWActor</code> can enter the sandcrawler, false otherwise
	 */
	@Override
	public boolean canDo(SWActor a) {
		return a.getForce() >= 60;
	}
	/**
	 * Perform the <code>Enter</code> action by initializing the interior world.
	 * This method should only be called if the <code>SWActor a</code> is alive.
	 *
	 * @author 	omar0003
	 * @param 	a the <code>SWActor</code> that is entering the sandcrawler
	 * @see		{@link starwars.SWActor#isDead()}
	 */
	@Override
	public void act(SWActor a) {
		if (target instanceof SWEntityInterface) {
			if (canDo(a)) {
				SandcrawlerWorld inside = new SandcrawlerWorld(5);
				a.setWorld(inside);
				SWGridController uiController2 = new SWGridController(inside);
				Scheduler theScheduler2 = new Scheduler(1, inside);
				SWActor.setScheduler(theScheduler2);
				inside.initializeWorld(uiController2, a);
				ArrayList<Droid> droidArrayList = Sandcrawler.getDroids();
				for(Droid i:droidArrayList){
					i.setWorld(inside);
					SandcrawlerWorld.getEntitymanager().setLocation(i, inside.getMyGrid().getLocationByCoordinates(0,0));
				}

				while (true) {
					uiController2.render();
					theScheduler2.tick();
				}
			}
		}
	}
	/**
	 * A String describing what this action will do, suitable for display in a user interface
	 *
	 * @author omar0003
	 * @return String comprising "Enter " and the short description of the target of this <code>Enter</code>
	 */
	@Override
	public String getDescription() {
		return "Enter Sandcrawler";
	}

}
