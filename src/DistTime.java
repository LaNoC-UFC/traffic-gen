import java.util.*;

public class DistTime {

	private int numberOfPackets;
	private double totalTime;

	public DistTime(int flitClockCycles, int packetSize, int numOfPackets, double rate) {
		double packetTime = packetSize * flitClockCycles;
		double idleTime = Math.rint((100 / rate - 1) * packetTime);
		totalTime = idleTime + packetTime;
		numberOfPackets = numOfPackets;
	}

	public List<Integer> uniform() {
		List<Integer> timeStamps = new ArrayList<>();
		double timeStamp = 1;
		for (int j = 0; j < numberOfPackets; j++) {
			timeStamps.add((int) timeStamp);
			timeStamp += totalTime;
		}

		return timeStamps;
	}
}