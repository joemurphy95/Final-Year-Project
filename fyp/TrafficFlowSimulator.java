package fyp;

/*
 * Joseph Murphy, 2019
 */
public class TrafficFlowSimulator {

	int l, numCars, interval;
	double prob;
	private static void printErrorExit(String message) {
		System.err.println(message);
		System.exit(1);
	}
	 
	public void TrafficFlowSimulator(int l, int numCars, double prob, int interval){
		this.l = l;
		this.numCars = numCars; 
		this.prob = prob;
		this.interval = interval;
		 
		Street freeway = new Street(l);
		freeway.insertCars(numCars);
		freeway.simulateTraffic(prob, interval);
		 
	}
}

