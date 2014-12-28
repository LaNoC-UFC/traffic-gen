package uniforme;

import java.io.*;
import java.util.*;

public class escreve_arquivo {

	private FileOutputStream file, fileOmnet, filePetri;
	private DataOutputStream dataOutput;
	private DataOutputStream dataOutputOmnet;
	private DataOutputStream dataOutputPetri;

	private Map<String, Integer> numberOfPacks = new HashMap<String, Integer>();
	private int[][] nPcks;

	private Conversao conversao;
	private double rate, frequency;
	private int packetSize, numberPackets, FlitSize;
	private int dimX, dimY, flitWidth, flitClockCycles;
	private double Rates[] = { 10, 20, 30, 40 };
	private Integer[] nPackets;
	private Integer[] nFlits;
	int priorityO;

	private String core, address, target, distTime, dado;
	private int addressX, addressY, targetX, targetY;

	// ArrayList<String> sinks = new ArrayList<String>();
	Vector<String> dstOmnet = new Vector();
	Vector<String> dstPetri = new Vector();

	int a = 0;
	int b = 0;

	private String scenarioPath;
	private Dados Petri;
	private DistSpatial_THOR distSpatial;
	private String sACG;

	private Integer totalPackets, totalFlits = 0;
	private Vector VRate, VTarget;
	private int sequenceNumberH = 0, sequenceNumberL = 1;

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

	public escreve_arquivo(int dimX, int dimY, int flitWidth,
			int flitClockCycles, int FlitSize, int numberPackets,
			int packetSize, double frequency, int targetX, int targetY,
			String dado) {

		nPcks = new int[dimX * dimY][dimX * dimY];

		this.dimX = dimX;
		this.dimY = dimY;
		this.dado = dado;
		this.flitWidth = flitWidth;
		this.flitClockCycles = flitClockCycles;
		this.FlitSize = FlitSize;
		conversao = new Conversao();
		distSpatial = new DistSpatial_THOR(dimX, dimY, flitWidth);
		Petri = new Dados(numberPackets, packetSize, dado);
		nPackets = new Integer[dimX * dimY];

		for (int i = 0; i < dimX * dimY; i++)
			nPackets[i] = 0;

		nFlits = new Integer[dimX * dimY];
		for (int i = 0; i < dimX * dimY; i++)
			nFlits[i] = 0;

		this.numberPackets = numberPackets;
		this.packetSize = packetSize;
		this.frequency = frequency;
		this.rate = rate;
		priorityO = 0;

		this.targetX = targetX;
		this.targetY = targetY;
	}

	public void writeTraffic(ArrayList<String> sinks, String target,
			String Caminho, double rate) {
		File diretory;
		double pkt_tx, flt_tx, localRate;
		nPcks = new int[dimX * dimY][dimX * dimY];

		scenarioPath = Caminho;
		diretory = new File(scenarioPath + File.separator + "In");

		diretory.mkdirs();

		sACG = "";
		for (int y = 0; y < dimY; y++) {
			for (int x = 0; x < dimX; x++) {
				try {
					// Create files for all routers
					file = new FileOutputStream(scenarioPath + File.separator
							+ "In" + File.separator + "in"
							+ formatAddress(x, y) + ".txt");

					dataOutput = new DataOutputStream(file);

					// Format and print packets
					writeLinesPS(sinks, target, x, y, numberPackets, rate,
							targetX, targetY);

					dataOutput.close();

				}

				catch (Exception e) {
					System.out.println("Erro");
				}
			}

		}
	}

	public void writeLinesPS(ArrayList<String> sinks, String target, int x,
			int y, int numberPackets, double Rate, int targetX, int targetY) {

		String linha, payload = "";
		String target2 = target;

		String priority;
		int sourceX, sourceY, iTarget;
		int counter, packetSize1, payloadSize = 0;
		String[] timestampHex;

		int destinoX, destinoY;
		destinoX = targetX;
		destinoY = targetY;

		DistTime distTime = new DistTime(flitWidth, flitClockCycles, frequency,
				packetSize, numberPackets, Rate);

		Vector vet = distTime.uniform();

		try {

			for (int j = 0; j < numberPackets; j++) {
				String aux = null;
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
				// priority = conversao.decimal_hexa(priorityO, (flitWidth /
				// 8));

				sourceX = x;
				sourceY = y;

				target = sinks.get((x * numberPackets) + (y * numberPackets * dimX) + j);

				// Get the number of pcks per flux
				String HashKey = sourceX + "." + sourceY + " " + target;
				int sourceN = sourceX + sourceY * dimX;
				int sinkN;// = Character.getNumericValue(target.charAt(0)) + Character.getNumericValue(target.charAt(1)) * dimX;
				sinkN = conversao.nodoToInteger(target, dimX, FlitSize);

				nPcks[sourceN][sinkN]++;

				if (!numberOfPacks.containsKey(HashKey)) {
					numberOfPacks.put(HashKey, 1);
				} else {
					numberOfPacks.put(HashKey, numberOfPacks.get(HashKey) + 1);
				}

				//iTarget = conversao.nodoToInteger(target,dimX, FlitSize);
				iTarget = sinkN;

				linha = linha.concat(/* priority + */target + " ");
				/***************************************************************************************/
				/*
				 * 2� FLIT = SIZE /********************************************
				 * ******************************************
				 */

				packetSize1 = packetSize;
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

				linha = linha.concat(formatAddress(x, y) + " ");

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
				timestampHex[i] = conversao.decimal_hexa(((int) value % 16), 1)
						+ timestampHex[i];
				value = value / 16;
			}
		}
		return timestampHex;
	}

	private String[] getTimestamp(String value) {
		String[] timestampHex = new String[4];

		value = conversao.setLengthHexa(value, flitWidth);
		// System.out.println("value "+value);
		for (int i = 0, j = flitWidth; i < 4; i++, j = j - (flitWidth / 4)) {
			timestampHex[i] = value.substring(j - (flitWidth / 4), j);
			// System.out.println("value["+i+"] "+value.substring(j-(flitWidth/4),j));
		}
		return timestampHex;
	}

	private String formatAddress(int addX, int addY) {
		String targetXBin, targetYBin, targetBin, zeros, targetHex;
		// Gerando destino na horizontal
		targetXBin = conversao.decimal_binario(addX, (flitWidth / 2));
		// Gerando destino na vertical
		targetYBin = conversao.decimal_binario(addY, (flitWidth / 2));
		targetBin = targetXBin + targetYBin; // concatena targetX e targetY;
		// zeros = conversao.decimal_hexa(0, (flitWidth / 8));
		targetHex = conversao.binario_hexa(targetBin, (flitWidth / 4));
		// targetHex = zeros + targetHex;
		return targetHex;
	}

}
