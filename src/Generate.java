
import java.io.*;
import java.util.*;

public class Generate {

	private FileOutputStream file;
	private DataOutputStream dataOutput;
	private Map<String, Integer> numberOfPacks = new HashMap<String, Integer>();
	private int[][] nPcks;

	private double frequency;
	private int packetSize, numberPackets, FlitSize;
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
			int flitClockCycles, int FlitSize, int numberPackets,
			int packetSize, double frequency) {

		nPcks = new int[dimX * dimY][dimX * dimY];

		this.dimX = dimX;
		this.dimY = dimY;
		this.flitWidth = flitWidth;
		this.flitClockCycles = flitClockCycles;
		this.FlitSize = FlitSize;
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
					writeLinesPS(sinks, x, y, numberPackets, rate);

					dataOutput.close();

				}

				catch (Exception e) {
					System.out.println("Erro");
				}
			}

		}
	}

	public void writeLinesPS(ArrayList<String> sinks, int x,
			int y, int numberPackets, double Rate) {

		String linha, payload = "";

		int sourceX, sourceY, iTarget;
		int counter, payloadSize = 0;
		String[] timestampHex;

		DistTime distTime = new DistTime(flitWidth, flitClockCycles, frequency,
				packetSize, numberPackets, Rate);

		ArrayList<String> vet = distTime.uniform();

		try {

			for (int j = 0; j < numberPackets; j++) {
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

				String target = sinks.get((x * numberPackets) + (y * numberPackets * dimX) + j);

				// Get the number of pcks per flux
				String HashKey = sourceX + "." + sourceY + " " + target;
				int sourceN = sourceX + sourceY * dimX;
				int sinkN;// = Character.getNumericValue(target.charAt(0)) + Character.getNumericValue(target.charAt(1)) * dimX;
				sinkN = Conversao.nodoToInteger(target, dimX, FlitSize);

				nPcks[sourceN][sinkN]++;

				if (!numberOfPacks.containsKey(HashKey)) {
					numberOfPacks.put(HashKey, 1);
				} else {
					numberOfPacks.put(HashKey, numberOfPacks.get(HashKey) + 1);
				}

				//iTarget = Conversao.nodoToInteger(target,dimX, FlitSize);
				iTarget = sinkN;

				linha = linha.concat(/* priority + */target + " ");
				/***************************************************************************************/
				/*
				 * 2� FLIT = SIZE /********************************************
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
				 * 3� FLIT = SOURCE /******************************************
				 * ********************************************
				 */

				linha = linha.concat(Conversao.formatAddress(x, y, flitWidth) + " ");

				/***************************************************************************************/
				/*
				 * 4� - 7� FLITS = TIMESTAMP HEXA /**************************
				 * ************************************************************
				 */
				timestampHex = getTimestamp((String) vet.get(j));
				linha = linha.concat(timestampHex[3] + " " + timestampHex[2]
						+ " " + timestampHex[1] + " " + timestampHex[0] + " ");

				/***************************************************************************************/
				/*
				 * 8� AND 9� FLIT = SEQUENCE NUMBER /************************
				 * *************************************
				 * *************************
				 */
				linha = linha
						.concat(addLineByte(Integer
								.toHexString(sequenceNumberH).toUpperCase(),
								" "));
				linha = linha
						.concat(addLineByte(Integer
								.toHexString(sequenceNumberL).toUpperCase(),
								" "));

				// incrementando o numero de sequencia
				if (sequenceNumberL == (Math.pow(2, flitWidth) - 1)) {
					sequenceNumberH++;
					sequenceNumberL = 0;
				} else
					sequenceNumberL++;
				/***************************************************************************************/
				/*
				 * PAYLOAD
				 * /*****************************************************
				 * *********************************
				 */
				if (j == 0) {
					counter = 8;
					for (int l = counter; l <= payloadSize; l++) {

						if (l == payloadSize) // n�o coloca espa�o se � o
												// �ltimo flit do payload
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

				dataOutput.writeBytes(linha + "\r\n");
			}
		} catch (Exception e) {
			System.out.println("Exception at WriteLinePS");
		}
	}

	private String addLineByte(String data, String separador) {
		if (data != null) {
			int sizeEsperado = flitWidth / 4;
			int sizeData = data.length();
			if (sizeData > sizeEsperado)
				System.out.println("Problema no tamanhos dos flits");
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
		for (int i = 0; i < 4; i++) { // 4 � o n�mero de flits
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

			for (int x = 0; x < (dimX * dimY - 1); x++) {
				for (int y = 0; y < (dimY * dimX - 1); y++) {
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
