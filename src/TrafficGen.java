
import java.io.File;
import java.util.ArrayList;

public class TrafficGen {

	public static void main(String[] args) {

		String caminho = ".";
		int dimX = 4; // X dimension
		int dimY = 4; // Y dimension
		int n_pck = 10; // packets per core
		int nFlits = 2; // #flits
		int flitWidth = 16;
		double freq = 50.0;
		int flitClockCycles = 1; // on-off
		ArrayList<Double> rates = new ArrayList<Double>();

		if (args.length == 6) {

			dimX = Integer.parseInt(args[0]); // X dimension
			dimY = Integer.parseInt(args[1]); // Y dimension
			n_pck = Integer.parseInt(args[2]); // # packets per core
			nFlits = Integer.parseInt(args[3]); // # flits
			flitWidth = Integer.parseInt(args[4]); // flit size
			String[] inputRates = args[5].split(",");

			for (String rate : inputRates)
				rates.add(Double.parseDouble(rate));
		} else {
			rates.add(50.0);
		}

		int desX = dimX/2, desY = dimY/2; // for hot spot
		 String distrib = "random"; // uniform -> random ; else -> hot spot
		// String distrib = "bitReversal";
		// String distrib = "butterfly";
		// String distrib = "complemento";
		// String distrib = "matrixTranspose";
		// String distrib = "perfectShuffle";

		Generate gen = new Generate(dimX, dimY, flitWidth, flitClockCycles,flitWidth, n_pck, nFlits, freq);

		genSinks genS = new genSinks(dimX, dimY, flitWidth);

		for (Double rate : rates) {
			ArrayList<String> sinks = genS.doSinks(distrib, n_pck, desX, desY);
			gen.writeTraffic(sinks, distrib, caminho + File.separator + "F"
					+ (rate.intValue()), rate);
			gen.printNofPcks(caminho + File.separator + "F"
					+ (rate.intValue()) + File.separator);
		}

		System.out.println("Done, check selected directory");

	}

}