package uniforme;

import java.util.ArrayList;

public class genSinks 
{
	private DistSpatial_THOR distSpatial;
	
	public genSinks(int dimX, int dimY, int flitWidth)
	{
		this.distSpatial = new DistSpatial_THOR(dimX, dimY, flitWidth);		
	}
	public ArrayList<String> doSinks(String Target, int dimY, int dimX, int numberPackets, int targetX, int targetY) 
	{
		ArrayList<String> sinks = new ArrayList<>();
		
		for (int y = 0; y < dimY; y++) 
			for (int x = 0; x < dimX; x++) 
				for (int i = 0; i < numberPackets; i++) 
					sinks.add(distSpatial.defineTarget(Target, x, y, targetX,targetY));		
		
		return sinks;
	}

}
