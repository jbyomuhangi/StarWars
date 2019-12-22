package starwars.entities;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.Capability;
import starwars.SWActor;
import starwars.SWEntity;
import starwars.SWWorld;
import starwars.actions.Take;
import starwars.actions.ThrowGrenade;

/**
 * this class creates <code>Grenade</code> objects
 * @author JOEL
 *
 */
public class Grenade extends SWEntity {
	/** the  world the grenade belongs to */
	private SWWorld myWorld;
	
	/**the <code>SWActor</code> that owns the grenade	 */
	private SWActor owner; 
	
	/**
	 * constructor for the grenade object, initializes everything
	 * @param m message renderer 
	 * @param myWorld the world the grenade is in
	 */
	public Grenade(MessageRenderer m, SWWorld myWorld) {
		super(m);
		this.shortDescription = "A grenade";
		this.longDescription = "A powerful grenade ";
		this.addAffordance(new Take(this, m));
		this.capabilities.add(Capability.WEAPON);
		this.myWorld = myWorld;
	}

	public String getSymbol() {
		return "G"; 
	}
	
	/**
	 * method to set the owner of a grenade 
	 * @param owner SWActor that picked up the grenade
	 */
	public void setOwner(SWActor owner) {
		this.owner = owner;  
	}
	
	/**
	 * method to get the owner of a grenade 
	 * @return SWActor that owns the grenade 
	 */
	public SWActor getOwner() {
		return owner;
	}
	
	public SWWorld getWorld() {
		return this.myWorld;
	}
}
