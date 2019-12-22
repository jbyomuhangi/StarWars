package starwars.entities.actors;

import java.util.ArrayList;
import java.util.List;

import edu.monash.fit2099.simulator.space.Direction;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWActor;
import starwars.SWEntityInterface;
import starwars.SWLocation;
import starwars.SWWorld;
import starwars.Team;
import starwars.actions.Enter;
import starwars.actions.Move;
import starwars.entities.actors.behaviors.Patrol;

/**
 * A sandcrawler vehicle.
 * A sandcrawler moves in the same way as Ben Kenobi, but only moves every second turn. If a sandcrawler finds a droid
 * in its location, the droid is taken inside the sandcrawler.
 *
 * A sandcrawler has a door that can be entered by any actor with force ability that is in the same location as the
 * sandcrawler. When the actor enters the sandcrawler, the actor moves to the interior of the sandcrawler, which is a
 * grid of at least four locations.
 *
 * All the droids taken by the sandcrawler must be in one of these interior locations.
 *
 * One of the interior locations must have a door that can be user by any actor with force ability that is in that
 * location. Exiting the door results in the actor being returned to the location in which the sandcrawler is located.
 * @author omar0003
 *
 */
public class Sandcrawler extends SWActor {
	
	private Patrol path;
	private int count = 1;
	private static ArrayList<Droid> picked = new ArrayList<>();
	private static SWLocation myLoc;
	/**
	 * Constructor for the <code>Sandcrawler</code> class
	 * <p>
	 * The constructor initializes the <code>path</code> of the sandcrawler.
	 * It also sets the short and long descriptions and adds the enter affordance, to allow entities to enter.
	 * <p>
	 *
	 * @param team that the Sandcrawler belongs to.
	 * @param hitpoints initial hitpoints of this <code>Sandcrawler</code> to start with
	 * @param m message renderer for this <code>Sandcrawler</code> to display messages
	 * @param world the <code>World</code> to which <code>Sandcrawler</code> belongs to
	 * @param moves used to initialise the path that the Sandcrawler follows
	 */
	public Sandcrawler(Team team, int hitpoints, MessageRenderer m, SWWorld world, Direction [] moves) {
		super(team, hitpoints, m, world);
		this.setShortDescription("Sandcrawler");
        this.setLongDescription("A vehicle called a sandcrawler, crawling with Jawas.");
        this.path = new Patrol(moves);
        this.addAffordance(new Enter(this, messageRenderer));
	}

	@Override
	public void act() {
		if (count % 2 == 0) {
			Direction newdirection = path.getNext();
			say(this.getShortDescription() + " moves " + newdirection);
			Move myMove = new Move(newdirection, messageRenderer, (SWWorld) world);

			scheduler.schedule(myMove, this, 1);
		}
		count += 1;
		myLoc = ((SWWorld)this.world).getEntityManager().whereIs(this);
		List<SWEntityInterface> contents = ((SWWorld)this.world).getEntityManager().contents(myLoc);	//get the contents of the location
		if (contents != null) {
			for (SWEntityInterface entity : contents) {
				if (entity instanceof Droid) {
					picked.add((Droid)entity);
					SWWorld.getEntitymanager().remove(entity);
					((Droid) entity).setWorld(null);
				}
			}
		}
	}
	public static SWLocation getMyLoc(){return myLoc;}
	public static ArrayList<Droid> getDroids(){return picked;}
	public static void setDroids(ArrayList<Droid> newList){picked = newList;}
}
