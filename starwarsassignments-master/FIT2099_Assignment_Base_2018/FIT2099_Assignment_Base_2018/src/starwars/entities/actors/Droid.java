package starwars.entities.actors;


import java.util.ArrayList;
import java.util.Random;
import edu.monash.fit2099.gridworld.Grid.CompassBearing;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.*;
import starwars.actions.Move;

/**
 * <code>Droid</code> class will make droids that can be owned by actors.
 * @author JOEL
 *
 */
public class Droid extends SWActor {
	/** the <code> owner </code> of the droid*/
	private SWActor owner;
	
	/** lets us know if a random direction has been chosen*/
	private boolean chosen = false;
	
	/** tells us the random <code> chosenDirection </code> that the droid will move in*/
	private CompassBearing chosenDirection = null;
	
	//the force ability of droids must always be 0
	private final int FORCEABILITY = 0; 
	
	
	/**
	 * Constructor for the <code>Droid</code> class
	 * <p>
	 * The constructor initializes the <code>owner</code> of the droid.
	 * <p>
	 * 
	 * @param team that the droid belongs to.
	 * @param hitpoints initial hitpoints of this <code>Droid</code> to start with
	 * @param m message renderer for this <code>Droid</code> to display messages
	 * @param world the <code>World</code> to which <code>Droid</code> belongs to
	 * @param droidOwner the <code>SWActor</code> that owns the <code>Droid</code>
	 */
	public Droid(Team team, int hitpoints, MessageRenderer m, SWWorld world, SWActor droidOwner) {
		super(team, hitpoints, m, world);
		owner = droidOwner; 
	}
	
	/**
	 * This method gets a Droid force ability
	 * 
	 * @return FORCEABILITY which is always 0
	 */
	public int getForce() {
		return FORCEABILITY;
	}
	
	/**
	 * This method will set the ownership of a <code>Droid</code>
	 * @param newOwner
	 */
	public void setOwner(SWActor newOwner) {
		owner = newOwner;
	}
	
	/**
	 * This method will retrieve the owner of a <code>Droid</code>
	 * @return <code>SWActor</code> that owns the droid, returns <code>null</code> if not owned.
	 */
	public SWActor getOwner() {
		return owner;
	}

	@Override
	public void act() {
		SWLocation droidLoc;
		SWLocation ownerLoc;
		// initialize locations of a droid and its owner to variables
		try {
			droidLoc = ((SWWorld) this.world).getEntityManager().whereIs(this);
		}
		catch (NullPointerException e){
			droidLoc = ((SandcrawlerWorld) this.world).getEntitymanager().whereIs(this);
		}
		try {
			ownerLoc = ((SWWorld)this.world).getEntityManager().whereIs(getOwner());
		}
		catch (NullPointerException e){
			ownerLoc = ((SandcrawlerWorld)this.world).getEntitymanager().whereIs(getOwner());
		}

		// we check to see if the droid is in a badland
		if(droidLoc != null) {
			if (droidLoc.getSymbol() == 'b') {
				this.takeDamage(3);
			}
		}
		// check if the droid has an owner and if it still has health 
		if(getOwner() != null && this.getHitpoints() > 0 ) {	
			// if droid is with owner, do nothing 
			if (droidLoc == ownerLoc) {}
			
			else {
				ArrayList<CompassBearing> selection = new ArrayList<CompassBearing>(); // list to store possible directions to move
				boolean done = false; // indicates if the owner was found in a neighboring location 
				
				for (CompassBearing d: CompassBearing.values()) {
					// if the owner is in a neighboring location, move there
					if (droidLoc.getNeighbour(d) == ownerLoc) {
						Move newMove = new Move(d,messageRenderer,(SWWorld) this.world);
						newMove.act(this);
						done = true;
					}
					
					// add possible directions to the array in case the owner is not found 
					if (droidLoc.getNeighbour(d) != null) {
						selection.add(d);
					}	
				}

				// if the owner wasn't found
				if (done == false) {
					// if no direction has been chosen yet, chose one at random from the array 
					if (chosen == false) {
						chosenDirection = selectDirection(selection);
						chosen = true;
					}
					
					Move newMove = new Move(chosenDirection,messageRenderer,(SWWorld) this.world);
					// move in the chosen direction until you can't any more 
					if(selection.contains(chosenDirection) == true) {
						newMove.act(this);
					}
					else {
						// set chosen to false, indicating you can't move in that direction any more
						chosen = false;
					}	
				}
			}
		}
	}
	
	/**
	 * This method will randomly select a direction for the <code>Droid</code> to move in
	 *  
	 * @param options an array containing valid possible directions to go in 
	 * @return the randomly chosen direction 
	 */
	private CompassBearing selectDirection(ArrayList <CompassBearing> options) {
		CompassBearing randomDirection;
		Random a = new Random();
		int number;
		number = a.nextInt(options.size()-1);
		randomDirection = options.get(number);
		return randomDirection;
		
		
	}
}
