package elevator;
import java.util.*;

public class Elevator implements Runnable{
    private int floor;
    private Direction direction;
    private Queue<Person> upStops;
    private Queue<Person> downStops;
    private int capacity;
    private int count;
    private int speed;
    private int id;
    private int maxfloor;
    private boolean[] elevatoron;
    private int targetfloor;
    private Floor[] floors;
    
    public Comparator<Person> upcom = new Comparator<Person>() {
    	public int compare(Person a, Person b) {
    		return a.floor-b.floor;
    	}
    };
    public Comparator<Person> downcom = new Comparator<Person>() {
    	public int compare(Person a, Person b) {
    		return b.floor-a.floor;
    	}
    };
    public Elevator(int id, int capacity, boolean[] elevatoron, Floor[] floors, int maxfloor) {
    	this.id = id;
    	this.floor = 0;
    	this.capacity = capacity;
    	this.count = 0;
    	this.speed = 10;
    	this.targetfloor = 0;
    	this.elevatoron = elevatoron;
    	this.direction = Direction.UP;
    	this.maxfloor = maxfloor;
    }
	public synchronized boolean onoff() {
    	return elevatoron[this.id];
    }
    public void setDirection() throws InterruptedException {
    	if(floor == targetfloor||downStops.isEmpty()&&upStops.isEmpty())
	    	if(direction ==  Direction.UP) {
	    		addStop(floor);
	    		if(upStops.isEmpty()) {
	    			direction =  Direction.DOWN;
	    			addStop(floor);
	    		}
	    	}else {	
	    		addStop(floor);
	    		if(downStops.isEmpty()) {
	    			direction =  Direction.UP;
	    			addStop(floor);
	    		}
	    	}
    }
    public void moveToNext() throws InterruptedException {
    	if(direction ==  Direction.UP) {
    		Thread.sleep(speed);
    		floor = upStops.peek().floor;
    	}else {
    		Thread.sleep(speed);
    		floor = downStops.peek().floor;
    	}
    }
    public State getState() { return null; }
    
    public Direction getDirection() {
            return direction;
    }
    private void getout() {
    	if(direction ==  Direction.UP) {
    		while(!upStops.isEmpty() && upStops.peek().floor==floor) {
    			upStops.poll();
    			count--;
    		}
    	}else {
    		while(!downStops.isEmpty() && downStops.peek().floor==floor) {
    			downStops.poll();
    			count--;
    		}
    	}
    }
    private void getin() {
    	if(direction ==  Direction.UP) {
    		while(count<capacity) {
    			Person a = floors[floor].getup();
    			if(a==null)return;
    			if(a.floor>targetfloor) {
    				targetfloor = a.floor;
    			}
    			upStops.offer(a);
    			count++;
    		}
    	}else {
    		while(count<capacity) {
    			Person a = floors[floor].getdown();
    			if(a==null)return;
    			if(a.floor<targetfloor) {
    				targetfloor = a.floor;
    			}
    			downStops.offer(a);
    			count++;
    		}

    	}
    }
    public void open() throws InterruptedException {
    	getout();
    	getin();
    	close();
    }
    public void close() throws InterruptedException {
    }
    
    public void addStop(int floor) {
    	Person a = null;
    	if(direction ==  Direction.UP) {
    		int i = maxfloor;
    		while(floor<i && a== null) {
    			a = floors[i].getdown();
    			i--;
    			if(a!=null) {
    				downStops.offer(a);
    			}
    		}
    	}else {
    		int i = 0;
    		while(floor>i && a== null) {
    			a = floors[i].getup();
    			i++;
    			if(a!=null) {
    				upStops.offer(a);
    			}
    		}    		
    	}
    }

	public void run() {
		while(onoff()) {
			try {
				setDirection();
				moveToNext();
				open();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
