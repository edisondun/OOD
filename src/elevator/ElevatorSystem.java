package elevator;
import java.util.*;

public class ElevatorSystem {

    // 4 elevators
    private boolean[] elevatoron = new boolean[4];
    private Thread myThreads[] = new Thread[4];
    
    //30 floors
    private Floor[] floors = new Floor[30];

    private static final ElevatorSystem instance = new ElevatorSystem();
    private ElevatorSystem() {
    	for(int i =0;i<30;i++) {
    		floors[i] = new Floor(i);
    	}
    	for (int i = 0; i < 4; i++) {
    		elevatoron[i] = true;
    		myThreads[i] = new Thread(new Elevator(i,10,elevatoron,floors));
    		myThreads[i].start();
    	}
    }
    //singleton
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
