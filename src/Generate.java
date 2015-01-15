
import java.io.*;
import java.util.*;

public class Generate {

	private FileOutputStream file;
	private DataOutputStream dataOutput;
	private Map<String, Integer> numberOfPacks = new HashMap<String, Integer>();
	private int[][] nPcks;

	private int warmupPcks;
	private double frequency;
	private int packetSize, numberPackets;
	private int dimX, dimY, flitWidth, flitClockCycles;
	private Integer[] nPackets;
	private Integer[] nFlits;
	int priorityO;

	int a = 0;
	int b = 0;

	private String scenarioPath;
	private Integer totalFlits = 0;
	private int sequenceNumberH = 0, sequenceNumberL = 1;

	public Generate(int dimX, int dimY, int flitWidth,
			int flitClockCycles, int numberPackets, int warmupPcks,
			int packetSize, double frequency) {

		nPcks = new int[dimX * dimY][dimX * dimY];

		this.warmupPcks= warmupPcks;
		this.dimX = dimX;
		this.dimY = dimY;
		this.flitWidth = flitWidth;
		this.flitClockCycles = flitClockCycles;
		nPackets = new Integer[dimX * dimY];

		for (int i = 0; i < dimX * dimY; i++)
			nPackets[i] = 0;

		nFlits = new Integer[dimX * dimY];
		for (int i = 0; i < dimX * dimY; i++)
			nFlits[i] = 0;

		this.numberPackets = numberPackets;
		this.packetSize = packetSize;
		this.frequency = frequency;
	}

	public void writeTraffic(ArrayList<String> sinks, String target,
			String Caminho, double rate) {
		File diretory;
		nPcks = new int[dimX * dimY][dimX * dimY];

		scenarioPath = Caminho;
		diretory = new File(scenarioPath + File.separator + "In");

		diretory.mkdirs();

		for (int y = 0; y < dimY; y++) {
			for (int x = 0; x < dimX; x++) {
				try {
					// Create files for all routers
					file = new FileOutputStream(scenarioPath + File.separator
							+ "In" + File.separator + "in"
							+ Conversao.formatAddress(x, y, flitWidth) + ".txt");

					dataOutput = new DataOutputStream(file);

					// Format and print packets
					writeLinesPS(sinks, x, y, numberPackets, rate, warmupPcks);
					

					dataOutput.close();

				}

				catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void writeLinesPS(ArrayList<String> sinks, int x,
			int y, int numberPackets, double Rate, int warmUpPcks) {

		String linha;

		int sourceX, sourceY, iTarget;
		int payloadSize = 0;

		//System.out.println("warm up packs: "+warmupPcks);
		int totalNPcks = numberPackets+2*warmupPcks;
		String[] timestampHex;

		DistTime distTime = new DistTime(flitWidth, flitClockCycles, frequency,
				packetSize, totalNPcks, Rate);

		ArrayList<String> vet = distTime.uniform();

		try {

			for (int j = 0; j < totalNPcks; j++) {
				linha = "";
				/***************************************************************************************/
				/*
				 * TIMESTAMP INICIAL
				 * /*******************************************
				 * *******************************************
				 */
				linha = linha.concat((String) vet.get(j) + " ");
				

				/***************************************************************************************/
				/*
				 * 1� FLIT = PRIORITY (HIGH) + TARGET (LOW) /******************
				 * *******************************************
				 * *************************
				 */
				// priority = Conversao.decimal_hexa(priorityO, (flitWidth /
				// 8));

				sourceX = x;
				sourceY = y;
				String target = sinks.get((x * totalNPcks) + (y * totalNPcks * dimX) + j);
				
				if(target.equals(Conversao.formatAddress(x, y, flitWidth)))				
					break;
					
				// Get the number of pcks per flux
				String HashKey = sourceX + "." + sourceY + " " + target;
				int sourceN = sourceX + sourceY * dimX;
				int sinkN;// = Character.getNumericValue(target.charAt(0)) + Character.getNumericValue(target.charAt(1)) * dimX;
				sinkN = Conversao.nodoToInteger(target, dimX, flitWidth);

				if(!(j<warmupPcks || j>=(totalNPcks-warmupPcks)))
					nPcks[sourceN][sinkN]++;

				if(!(j<warmupPcks || j>=(totalNPcks-warmupPcks)))
				{
					if (!numberOfPacks.containsKey(HashKey)) {
						numberOfPacks.put(HashKey, 1);
					} else {
						numberOfPacks.put(HashKey, numberOfPacks.get(HashKey) + 1);
					}
				}

				//iTarget = Conversao.nodoToInteger(target,dimX, FlitSize);
				iTarget = sinkN;

				linha = linha.concat(/* priority + */target + " ");
				/***************************************************************************************/
				/*
				 * 2o FLIT = SIZE /********************************************
				 * ******************************************
				 */

				payloadSize = packetSize - 6; // 6 pq 2 s�o header e 4 s�o o
												// timestampHex da rede inserido
												// pelo fli
				linha = linha.concat(addLineByte(
						Integer.toHexString(payloadSize).toUpperCase(), " "));
				

				
					nPackets[iTarget]++;

				nFlits[iTarget] += packetSize;
				totalFlits += packetSize;

				/***************************************************************************************/
				/*
				 * 3o FLIT = SOURCE /******************************************
				 * ********************************************
				 */

				linha = linha.concat(Conversao.formatAddress(x, y, flitWidth) + " ");
				

				/***************************************************************************************/
				/*
				 * 4o - 7o FLITS = TIMESTAMP HEXA /**************************
				 * ************************************************************
				 */
				timestampHex = getTimestamp((String) vet.get(j));
				linha = linha.concat(timestampHex[3] + " " + timestampHex[2]
						+ " " + timestampHex[1] + " " + timestampHex[0] + " ");
				

				/***************************************************************************************/
				/*
				 * 8o AND 9o FLIT = SEQUENCE NUMBER /************************
				 * *************************************
				 * *************************
				 */
				if(!(j<warmupPcks || j>=totalNPcks-warmupPcks))
				{
					linha = linha.concat(addLineByte(Integer.toHexString(sequenceNumberH).toUpperCase()," "));
					linha = linha.concat(addLineByte(Integer.toHexString(sequenceNumberL).toUpperCase(),""));

				 //incrementando o numero de sequencia
					if (sequenceNumberL >= Math.pow(2, flitWidth)-1) {
						sequenceNumberH++;
						sequenceNumberL = 0;
					} else
						sequenceNumberL++;
				}
				else
				{
					linha = linha.concat(addLineByte(""," "));
					linha = linha.concat(addLineByte("",""));
				}
				
				/***************************************************************************************/
				/*
				 * PAYLOAD
				 * /*****************************************************
				 * *********************************
				 */
				/*
				if (j == 0) {
					counter = 8;
					for (int l = counter; l <= payloadSize; l++) {

						if (l == payloadSize) // nao coloca espaco se eh o
												// ultimo flit do payload
							payload = payload.concat(addLineByte(Integer
									.toHexString(0).toUpperCase(), ""));
						else
							payload = payload.concat(addLineByte(Integer
									.toHexString(counter).toUpperCase(), " "));

						// variando os dados do pacote
						if (counter == Math.pow(2, flitWidth))
							counter = 0;
						else
							counter++;
					}

				}
				linha = linha.concat(payload);
				*/
				dataOutput.writeBytes(linha + "\r\n");
			}
		} catch (IOException e) {
			System.out.println("Exception at WriteLinePS");
		}
	}

	private String addLineByte(String data, String separador) {
		if (data != null) {
			int sizeEsperado = flitWidth / 4;
			int sizeData = data.length();
			if (sizeData > sizeEsperado)
				System.err.println("Problema no tamanho do flit "+data);
			else if (sizeData < sizeEsperado) {
				for (int i = 0; i < (sizeEsperado - sizeData); i++)
					data = "0" + data;
			}
			return (data + separador);
		}
		return separador;
	}

	private String[] getTimestamp(double value) {
		String[] timestampHex = new String[4];
		for (int i = 0; i < 4; i++) { // 4 eh o numero de flits
										// correspondente ao timestampHex
			timestampHex[i] = "";
			for (int l = 0; l < (flitWidth / 4); l++) {
				timestampHex[i] = Conversao.decimal_hexa(((int) value % 16), 1)
						+ timestampHex[i];
				value = value / 16;
			}
		}
		return timestampHex;
	}

	private String[] getTimestamp(String value) {
		String[] timestampHex = new String[4];

		value = Conversao.setLengthHexa(value, flitWidth);
		// System.out.println("value "+value);
		for (int i = 0, j = flitWidth; i < 4; i++, j = j - (flitWidth / 4)) {
			timestampHex[i] = value.substring(j - (flitWidth / 4), j);
			// System.out.println("value["+i+"] "+value.substring(j-(flitWidth/4),j));
		}
		return timestampHex;
	}

	// Print sourceXsourceY sinkXsinkY nOfPcks
	public void printNofPcks(String path) {
		try {
			Formatter output = new Formatter(path + "nPcksMatrix");

			for (int x = 0; x < (dimX * dimY); x++) {
				for (int y = 0; y < (dimY * dimX); y++) {
					output.format("%d \t", nPcks[x][y]);
				}
				output.format("\r\n");
			}

			FileWriter fw = new FileWriter(new File(path + "nPcksHash"));
			for (String key : numberOfPacks.keySet()) {
				fw.write(key + " " + numberOfPacks.get(key) + "\n");
			}

			fw.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Re-initialize numberOfPacks to different OferredLoads
		this.numberOfPacks.clear();

	}

}
