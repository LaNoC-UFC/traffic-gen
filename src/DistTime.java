import java.util.*;

public class DistTime {

	private double channelRate;
	private double packetTime;
	private int numberOfPackets;
	private double ipRate;

	public DistTime(int flitWidth, int flitClockCycles, double frequency, int packetSize, int numOfPackets, double rate) {
		channelRate = frequency * flitWidth;
		packetTime = packetSize * flitClockCycles;
		numberOfPackets = numOfPackets;
		ipRate = (rate / 100) * channelRate;
	}

	public ArrayList<String> uniform() {
		double idleTime = ((channelRate / ipRate - 1) * packetTime);
		double totalTime = Math.rint(idleTime) + packetTime;
		double timestamp = 1;
		ArrayList<String> vet = new ArrayList<String>();
		for (int j = 0; j < numberOfPackets; j++) {
			vet.add(Integer.toHexString((int) timestamp).toUpperCase());
			timestamp = timestamp + totalTime;
		}
		return vet;
	}
}