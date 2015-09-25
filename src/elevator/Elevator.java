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
    public Elevator(int id, int capacity, boolean[] elevatoron, Floor[] floors) {
    	this.id = id;
    	this.floor = 0;
    	this.capacity = capacity;
    	this.count = 0;
    	this.speed = 10;
    	this.targetfloor = 0;
    	this.elevatoron = elevatoron;
    	this.direction = Direction.UP;
    }
	public synchronized boolean onoff() {
    	return elevatoron[this.id];
    }
    public void setDirection() throws InterruptedException {
    	if(floor == targetfloor)
	    	if(direction ==  Direction.UP) {
	    		direction =  Direction.DOWN;
	    		open();
	    		if(downStops.isEmpty()) {
	    			 addStop(floor);
	    		}
	    	}else {
	    		direction =  Direction.UP;
	    		open();
	    		if(upStops.isEmpty()) {
	    			 addStop(floor);
	    		}
	    	}
    }
    public void moveToNext() throws InterruptedException {
    	if(direction ==  Direction.UP) {
    		Thread.sleep(speed);
    		floor = upStops.peek().floor;
    		open();
    	}else {
    		Thread.sleep(speed);
    		floor = downStops.peek().floor;
    		open();
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
    	int i =1;
    	while(floor+i<30 || floor-i>0) {
    		int nextfloor = 0;
    		if(floor+i<30) {
    			nextfloor = floor+i;
    		}else {
    			nextfloor =  floor-i;
    		}
			Person temp = floors[nextfloor].getup();
			if (temp!=null) {
				upStops.offer(temp);
				direction = Direction.UP;
				break;
			}
			temp = floors[nextfloor].getdown();
			if (temp!=null) {
				downStops.offer(temp);
				direction = Direction.DOWN;
				break;
			}
    		i++;
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
