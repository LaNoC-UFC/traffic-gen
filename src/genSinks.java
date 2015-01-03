
import java.util.ArrayList;

public class genSinks {
	private DistSpatial distSpatial;
	private int dimX, dimY;

	public genSinks(int dimX, int dimY, int flitWidth) {
		this.distSpatial = new DistSpatial(dimX, dimY, flitWidth);
		this.dimX = dimX;
		this.dimY = dimY;
	}

	public ArrayList<String> doSinks(String distribution, int numberPackets, int targetX, int targetY) {
		ArrayList<String> sinks = new ArrayList<>();

		for (int y = 0; y < dimY; y++)
			for (int x = 0; x < dimX; x++)
				for (int i = 0; i < numberPackets; i++)
					sinks.add(distSpatial.defineTarget(distribution, x, y,
							targetX, targetY));

		return sinks;
	}

}
