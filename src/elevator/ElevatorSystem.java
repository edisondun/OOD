package elevator;
import java.util.*;

public class ElevatorSystem {

    // N elevators
    private boolean[] elevatoron = new boolean[4];
    private Thread myThreads[] = new Thread[4];
    int maxfloor = 30;
    // M floors, N elevators, each one has a control panel.
    private Floor[] floors = new Floor[maxfloor];

    private static final ElevatorSystem instance = new ElevatorSystem();
    private ElevatorSystem() {
    	for(int i =0;i<30;i++) {
    		floors[i] = new Floor(i);
    	}
    	for (int i = 0; i < 4; i++) {
    		elevatoron[i] = true;
    		myThreads[i] = new Thread(new Elevator(i,10,elevatoron,floors,maxfloor));
    		myThreads[i].start();
    	}
    }
    public static ElevatorSystem getInstance() {
    	return instance;
    }    
    public synchronized void turnoff(int id) {
    	elevatoron[id] = false;
    }
    public void request(int floor, Person person) {
    	if(person.floor > floor) {
    		floors[floor].pressUp(person);
    	}else {
    		floors[floor].pressDown(person);
    	}
    }
    
    public void update(Observable elevator, Object state) {
    }
    

}
