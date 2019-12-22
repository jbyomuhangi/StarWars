package starwars.actions;


import edu.monash.fit2099.simulator.matter.Affordance;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWAction;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.entities.Grenade;

/**
 * <code>SWAction</code> that lets a <code>SWActor</code> drop an object.
 * @author JOEL
 *
 */
public class Leave extends SWAffordance {
	
	/**
	 * Constructor for the <code>Take</code> Class. Will initialize the message renderer, the target and 
	 * set the priority of this <code>Action</code> to 1 (lowest priority is 0).
	 * 
	 * @param theTarget a <code>SWEntity</code> that is being taken
	 * @param m the message renderer to display messages
	 */
	public Leave(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		priority = 1;
	}
	
	/**
	 * Returns if or not this <code>Leave</code> can be performed by the <code>SWActor a</code>.
	 * <p>
	 * This method returns true if and only if <code>a</code> is carrying an item.
	 *  
	 * @param 	a, the <code>SWActor</code> being queried
	 * @return 	true if the <code>SWActor</code> can leave this item, false otherwise
	 */
	@Override
	public boolean canDo(SWActor a) {
		return a.getItemCarried()!=null;
	}
	
	
	
	/**
	 * Perform the <code>Leave</code> action by setting the item carried by the <code>SWActor</code> to <code>null</code> (
	 * This method should only be called if the <code>SWActor a</code> is alive.
	 * 
	 * @param 	a the <code>SWActor</code> that is leaving the target
	 */
	@Override
	public void act(SWActor a) {
		if (target instanceof SWEntityInterface) {
			SWEntityInterface theItem = (SWEntityInterface) target;
			a.setItemCarried(null);
			
			// add the item back on the map
			SWAction.getEntitymanager().setLocation(theItem, SWAction.getEntitymanager().whereIs(a));//add the target to the entity manager since it's not held by the SWActor
			target.removeAffordance(this); // remove leave affordance
			target.addAffordance(new Take(theItem, messageRenderer)); //add the take affordance
			
			if (target instanceof Grenade) {
				Affordance[] x = target.getAffordances();
				for (int i = 0; i< x.length; i++) {
					if (x[i] instanceof ThrowGrenade) {
						target.removeAffordance(x[i]);
					}
				}
				((Grenade) target).setOwner(null);
			}
		}
	}
	
	
	/**
	 * A String describing what this action will do, suitable for display in a user interface
	 * 
	 * @return String comprising "leave " and the short description of the target of this <code>Leave</code>
	 */
	@Override
	public String getDescription() {
		return "leave " + target.getShortDescription();
	}



}
