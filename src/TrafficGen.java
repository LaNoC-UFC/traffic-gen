
import java.io.File;
import java.util.ArrayList;

public class TrafficGen {

	public static void main(String[] args) {

		String caminho = ".";
		int dimX = 11; // X dimension
		int dimY = 11; // Y dimension
		int n_pck = 100; // packets per core
		double percentToWarmUp = 0.1;
		int nFlits = 15; // #flits
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
			percentToWarmUp=0.1;
			String[] inputRates = args[5].split(",");

			for (String rate : inputRates)
				rates.add(Double.parseDouble(rate));
		} else {
			rates.add(1.0);
			rates.add(2.0);
			rates.add(3.0);
			rates.add(4.0);
			rates.add(5.0);
			rates.add(6.0);
			rates.add(7.0);
			rates.add(8.0);
			rates.add(9.0);
			rates.add(10.0);
			rates.add(11.0);
			rates.add(12.0);
			rates.add(13.0);
			rates.add(14.0);
			rates.add(15.0);
		}

		int warmupPcks =(int)Math.ceil((double)n_pck*percentToWarmUp);
		int totalPcks = n_pck+2*warmupPcks;
		int desX = dimX/2, desY = dimY/2; // for hot spot
		 String distrib = "random"; // uniform -> random ; else -> hot spot
		// String distrib = "bitReversal";
		// String distrib = "butterfly";
		// String distrib = "complemento";
		// String distrib = "matrixTranspose";
		// String distrib = "perfectShuffle";
		if(nFlits < 13) {
			System.err.println("Pacote muito pequeno. Assumindo 13 flits...");
			nFlits = 13;
		}
		if(Math.pow(2, flitWidth) < nFlits-2) {
			flitWidth = (int) Math.ceil(Math.log(nFlits-2)/Math.log(2)) + 1;
			if(flitWidth%4 != 0) 
				flitWidth = (flitWidth/4+1)*4;
			System.err.println("Flit pequeno para pacotes de "+nFlits+ " flits. Assumindo "+flitWidth+" bits.");
		}
		if(Math.pow(2, flitWidth/2) < dimX || Math.pow(2, flitWidth/2) < dimY) {
			int dim = (dimX > dimY) ? dimX : dimY;
			flitWidth = (int) (2.0*Math.ceil(Math.log(dim)/Math.log(2)));
			if(flitWidth%4 != 0) 
				flitWidth = (flitWidth/4+1)*4;
			System.err.println("Flit pequeno para rede "+dimX+"x"+dimY+ ". Assumindo "+flitWidth+" bits.");
		}
		if(Math.pow(2, 2*flitWidth) < n_pck*dimX*dimY) {
			flitWidth = (int) (Math.ceil(Math.log(n_pck*dimX*dimY)/Math.log(2))/2.0) + 1;
			if(flitWidth%4 != 0) 
				flitWidth = (flitWidth/4+1)*4;
			System.err.println("Flit pequeno para "+n_pck+" pacotes. Assumindo "+flitWidth+" bits.");
		}

		Generate gen = new Generate(dimX, dimY, flitWidth, flitClockCycles, n_pck, warmupPcks, nFlits, freq);

		genSinks genS = new genSinks(dimX, dimY, flitWidth);

		for (Double rate : rates) {
			System.out.println("Generating the "+rate+"% rate.");
			String path = caminho + File.separator + "F" + String.format("%03d", rate.intValue());
			ArrayList<String> sinks = genS.doSinks(distrib, totalPcks, desX, desY);
			gen.writeTraffic(sinks, distrib, path, rate);
			gen.printNofPcks(path + File.separator);
		}

		System.out.println("Done, check selected directory");

	}

}