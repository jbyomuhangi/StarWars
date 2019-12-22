package starwars;

import java.util.List;

import edu.monash.fit2099.simulator.matter.EntityManager;
import edu.monash.fit2099.simulator.space.Direction;
import edu.monash.fit2099.simulator.space.Location;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.entities.SandcrawlerDoor;

/**
 * Class representing the interior of the Sandcrawler.
 *
 * @author omar003
 */
public class SandcrawlerWorld extends SWWorld {

	private static SWGrid myGrid2;
	private int height = 5;
	private int width = 5;
	private static final EntityManager<SWEntityInterface, SWLocation> entityManager = new EntityManager<SWEntityInterface, SWLocation>();
	/**
	 * Constructor of <code>SandcrawlerWorld</code>. This will initialize the <code>SWLocationMaker</code>
	 * and the grid.
	 */
	public SandcrawlerWorld(int dimention) {
		super(dimention);
		SWLocation.SWLocationMaker factory = SWLocation.getMaker();
		myGrid2 = new SWGrid(factory, 5);
		space = myGrid2;
	}

	public void initializeWorld(MessageRenderer iface, SWActor a) {
		SWLocation loc;
		for (int row=0; row < height; row++) {
			for (int col=0; col < width; col++) {
				loc = myGrid2.getLocationByCoordinates(col, row);
				loc.setLongDescription("SandcrawlerWorld (" + col + ", " + row + ")");
				loc.setShortDescription("SandcrawlerWorld (" + col + ", " + row + ")");
				loc.setSymbol('.');				
			}
		}
		
		loc = myGrid2.getLocationByCoordinates(0,0);
		getEntityManager().setLocation(a, loc);
		a.resetMoveCommands(loc);

		//a door 
		loc = myGrid2.getLocationByCoordinates(0, 0);
		SandcrawlerDoor door = new SandcrawlerDoor(iface, this);
		getEntityManager().setLocation(door, loc);
	}
	
	public boolean canMove(SWActor a, Direction whichDirection) {
		SWLocation where = (SWLocation)getEntityManager().whereIs(a); // requires a cast for no reason I can discern
		return where.hasExit(whichDirection);
	}
	
	/**
	 * Accessor for the grid.
	 * 
	 * @author ram
	 * @return the grid
	 */
	public static SWGrid getGrid() {
		return myGrid2;
	}

	/**
	 * Move an actor in a direction.
	 * 
	 * @author ram
	 * @param a the actor to move
	 * @param whichDirection the direction in which to move the actor
	 */
	public void moveEntity(SWActor a, Direction whichDirection) {
		
		//get the neighboring location in whichDirection
		Location loc = getEntityManager().whereIs(a).getNeighbour(whichDirection);
		
		// Base class unavoidably stores superclass references, so do a checked downcast here
		if (loc instanceof SWLocation)
			//perform the move action by setting the new location to the the neighboring location
			getEntityManager().setLocation(a, (SWLocation) getEntityManager().whereIs(a).getNeighbour(whichDirection));
	}

	/**
	 * Returns the <code>Location</code> of a <code>SWEntity</code> in this grid, null if not found.
	 * Wrapper for <code>entityManager.whereIs()</code>.
	 * 
	 * @author 	ram
	 * @param 	e the entity to find
	 * @return 	the <code>Location</code> of that entity, or null if it's not in this grid
	 */
	public Location find(SWEntityInterface e) {
		return getEntityManager().whereIs(e); //cast and return a SWLocation?
	}

	/**
	 * This is only here for compliance with the abstract base class's interface and is not supposed to be
	 * called.
	 */

	@SuppressWarnings("unchecked")
	public EntityManager<SWEntityInterface, SWLocation> getEntityManager() {
		return SWWorld.getEntitymanager();
	}

	/**
	 * Returns the <code>EntityManager</code> which keeps track of the <code>SWEntities</code> and
	 * <code>SWLocations</code> in <code>SWWorld</code>.
	 * 
	 * @return 	the <code>EntityManager</code> of this <code>SWWorld</code>
	 * @see 	{@link #entityManager}
	 */
	public static EntityManager<SWEntityInterface, SWLocation> getEntitymanager() {
		return entityManager;
	}
}
