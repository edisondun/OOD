package elevator;

import java.util.concurrent.*;


public class Floor {
	private int floor;
	private BlockingQueue<Person> up = new LinkedBlockingQueue<Person>();
	private BlockingQueue<Person> down = new LinkedBlockingQueue<Person>();
	public Floor(int floor) {
		this.floor = floor;
	}
	public int getfloor() {
		return this.floor;
	}
	public void pressUp(Person a) {
		up.offer(a);
	}
	public void pressDown(Person b) {
		down.offer(b);
	}
	public synchronized Person getup() {
		if(!up.isEmpty()) {
			return up.poll();
		}
		return null;
	}
	public synchronized Person getdown() {
		if(!down.isEmpty()) {
			return down.poll();
		}
		return null;		
	}
}
