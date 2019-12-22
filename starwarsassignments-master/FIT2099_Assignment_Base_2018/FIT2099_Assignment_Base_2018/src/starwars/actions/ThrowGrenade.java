package starwars.actions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.monash.fit2099.gridworld.Grid.CompassBearing;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.SWGrid;
import starwars.SWLocation;
import starwars.SWWorld;
import starwars.entities.Grenade;
import starwars.entities.Reservoir;

/**
 * this class is the "throw grenade" action
 * @author JOEL
 *
 */
public class ThrowGrenade extends SWAffordance {
	/**world the action is being done in */
	private SWWorld world;
	
	/**the grenade that is being thrown  */
	private SWEntityInterface current;
	
	/**hash map that contains areas that have already been affected by the grenade (areas one step away from the landing point) 	 */
	private HashMap<SWLocation, Integer> destruction = new HashMap<SWLocation, Integer>();
	
	/**hash map that contains areas 2 steps from the source that have been discovered	 */
	private HashMap<SWLocation, Integer> discovered = new HashMap<SWLocation, Integer>();

	/**
	 * constructor for the "throw" action  
	 * 
	 * @param theTarget the grenade being thrown
	 * @param m message renderer
	 * @param grenadaeWorld the world the action is happening in 
	 */
	public ThrowGrenade(SWEntityInterface theTarget, MessageRenderer m, SWWorld grenadaeWorld) {
		super(theTarget, m);
		priority = 1;
		world = grenadaeWorld;
		current = theTarget;
	}
	
	
	@Override
	public boolean canDo(SWActor a) {
		return a.getItemCarried()!=null;
	}
	
	/**
	 * method that damages all entities in a given location
	 *  
	 * @param contents list of all entities in a location
	 * @param damagePoints amount of damage each item takes 
	 */
	private void act_aux(List<SWEntityInterface> contents, int damagePoints) {
		if (contents != null) {	//location must contain entities
			for (SWEntityInterface entity : contents) {
				if(entity != current) { //grenade object is not included
					if(entity == ((Grenade)current).getOwner()) { //owner in unharmed
						System.out.println(entity.getShortDescription() + " is unharmed by the grenade");
					}
					else {
						if (entity instanceof Grenade == false){
							entity.takeDamage(damagePoints); //all other elements take damage
							System.out.println(entity.getLongDescription() + " has been damaged by a grenade new hitpoints: 7" +
									"" +
									"" + entity.getHitpoints());

						}
						if(entity instanceof Reservoir) {
							((Reservoir)entity).setDescription();	//reset the description of the reservoir if necessary 
						}
					}
				}
				
			}
		}
		
	}
	

	@Override
	public void act(SWActor a) {
		int row;
		int column;
		Scanner user = new Scanner(System.in);
		System.out.println("Enter row and column one after the other: ");
		row = user.nextInt();
		column = user.nextInt();
		while (row > world.height() || column > world.width() || row < 0 || column < 0) { //coordinates entered must be valid
			System.out.println("Row or column entered out of range, try again");
			row = user.nextInt();
			column = user.nextInt();
		}
		SWGrid myGrid = world.getGrid();
		SWLocation loc = myGrid.getLocationByCoordinates(column, row);
		this.world.getEntityManager().setLocation(current, loc);	//reset the location of the grenade
		
		List<SWEntityInterface> contents = this.world.getEntityManager().contents(loc);	//get the contents of the location
		act_aux(contents, 20);	// entities in current location getting damaged
		destruction.put(loc, 1);	// indicates location where it landed is done
		for (CompassBearing d: CompassBearing.values()) {
			SWLocation firstNeighbour = (SWLocation) loc.getNeighbour(d); //get a neighbouring location 
			contents = SWWorld.getEntitymanager().contents(firstNeighbour); //get contents of a neighbouring location 
			act_aux(contents, 10);	// entities in current location getting damaged
			destruction.put(firstNeighbour, 1);	//indicates location has been affected 
		}
		
		for(Map.Entry<SWLocation, Integer> entry: destruction.entrySet()) {
			SWLocation start = entry.getKey();
			if(start != null) {
				for (CompassBearing d: CompassBearing.values()) {
					SWLocation secondNeighbour = (SWLocation) start.getNeighbour(d); //get the location 2 steps away
					if(destruction.containsKey(secondNeighbour) == false && discovered.containsKey(secondNeighbour) == false) {
						contents = SWWorld.getEntitymanager().contents(secondNeighbour);	//get contents if it hasn't already been effected by the grenade
						act_aux(contents, 5);	// entities in current location getting damaged
						discovered.put(secondNeighbour, 1);	//indicates location was effected 
					}
				}
			}
		}
		SWWorld.getEntitymanager().remove(current);
		a.setItemCarried(null);
	}

	@Override
	public String getDescription() {
		return "throw " + target.getShortDescription();
	}

}
