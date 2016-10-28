
import java.io.*;
import java.util.*;

public class Generate {

    private FileOutputStream file;
    private DataOutputStream dataOutput;
    private Map<String, Integer> numberOfPacks = new HashMap<>();
    private int[][] nPcks;
    private int warmupPcks;
    private int numberOfPackets;
    private int packetSize;
    private int dimX, dimY, flitWidth;
    private Integer[] nPackets;
    private Integer[] nFlits;
    private int sequenceNumberH = 0, sequenceNumberL = 1;

    public Generate(int dimX, int dimY, int flitWidth, int numberPackets, int warmupPcks, int packetSize) {
        nPcks = new int[dimX * dimY][dimX * dimY];
        this.warmupPcks= warmupPcks;
        this.numberOfPackets = numberPackets;
        this.dimX = dimX;
        this.dimY = dimY;
        this.flitWidth = flitWidth;
        this.packetSize = packetSize;

        nPackets = new Integer[dimX * dimY];
        for (int i = 0; i < dimX * dimY; i++)
            nPackets[i] = 0;

        nFlits = new Integer[dimX * dimY];
        for (int i = 0; i < dimX * dimY; i++)
            nFlits[i] = 0;
    }

    public void writeTraffic(ArrayList<String> sinks, String scenarioPath, List<Integer> timeStamps) {
        File diretory = new File(scenarioPath + File.separator + "In");
        diretory.mkdirs();
        for (int y = 0; y < dimY; y++) {
            for (int x = 0; x < dimX; x++) {
                try {
                    // Create files for all routers
                    file = new FileOutputStream(scenarioPath + File.separator
                            + "In" + File.separator + "in"
                            + Conversion.formatAddress(x, y, flitWidth) + ".txt");
                    dataOutput = new DataOutputStream(file);
                    // Format and print packets
                    writeLinesPS(sinks, x, y, timeStamps);
                    dataOutput.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void writeLinesPS(ArrayList<String> sinks, int sourceX, int sourceY, List<Integer> timeStamps) {
        try {
            String line;
            int totalNPcks = numberOfPackets +2*warmupPcks;
            for (int j = 0; j < totalNPcks; j++) {
                line = "";
                /***************************************************************************************/
                /*
                 * TIMESTAMP INICIAL
                 * /*******************************************
                 * *******************************************
                 */
                line = line.concat(Integer.toHexString(timeStamps.get(j)).toUpperCase() + " ");
                /***************************************************************************************/
                /*
                 * 1� FLIT = PRIORITY (HIGH) + TARGET (LOW) /******************
                 * *******************************************
                 * *************************
                 */
                // priority = Conversion.decimal_hexa(priorityO, (flitWidth /
                // 8));
                String target = sinks.get((sourceX * totalNPcks) + (sourceY * totalNPcks * dimX) + j);
                if(target.equals(Conversion.formatAddress(sourceX, sourceY, flitWidth))) {
                    break;
                }
                // Get the number of pcks per flux
                String HashKey = sourceX + "." + sourceY + " " + target;
                int sourceN = sourceX + sourceY * dimX;
                int sinkN = vertexIndex(target, dimX, flitWidth);
                if(!(j<warmupPcks || j>=(totalNPcks-warmupPcks))) {
                    nPcks[sourceN][sinkN]++;
                }
                if(!(j<warmupPcks || j>=(totalNPcks-warmupPcks))) {
                    if (!numberOfPacks.containsKey(HashKey)) {
                        numberOfPacks.put(HashKey, 1);
                    }
                    else {
                        numberOfPacks.put(HashKey, numberOfPacks.get(HashKey) + 1);
                    }
                }
                //iTarget = Conversion.vertexIndex(target,dimX, FlitSize);
                int iTarget = sinkN;
                line = line.concat(/* priority + */target + " ");
                /***************************************************************************************/
                /*
                 * 2o FLIT = SIZE /********************************************
                 * ******************************************
                 */
                int payloadSize = packetSize - 6; // 6 pq 2 s�o header e 4 s�o o
                // timestampHex da rede inserido
                // pelo fli
                line = line.concat(addLineByte(
                        Integer.toHexString(payloadSize).toUpperCase(), " "));
                nPackets[iTarget]++;
                nFlits[iTarget] += packetSize;
                /***************************************************************************************/
                /*
                 * 3o FLIT = SOURCE /******************************************
                 * ********************************************
                 */

                line = line.concat(Conversion.formatAddress(sourceX, sourceY, flitWidth) + " ");
                /***************************************************************************************/
                /*
                 * 4o - 7o FLITS = TIMESTAMP HEXA /**************************
                 * ************************************************************
                 */
                String[] timestampHex = getTimestamp(Integer.toHexString(timeStamps.get(j)).toUpperCase());
                line = line.concat(timestampHex[3] + " " + timestampHex[2]
                        + " " + timestampHex[1] + " " + timestampHex[0] + " ");
                /***************************************************************************************/
                /*
                 * 8o AND 9o FLIT = SEQUENCE NUMBER /************************
                 * *************************************
                 * *************************
                 */
                if(!(j<warmupPcks || j>=totalNPcks-warmupPcks)) {
                    line = line.concat(addLineByte(Integer.toHexString(sequenceNumberH).toUpperCase()," "));
                    line = line.concat(addLineByte(Integer.toHexString(sequenceNumberL).toUpperCase(),""));
                    //incrementando o numero de sequencia
                    if (sequenceNumberL >= Math.pow(2, flitWidth)-1) {
                        sequenceNumberH++;
                        sequenceNumberL = 0;
                    } else
                        sequenceNumberL++;
                }
                else {
                    line = line.concat(addLineByte(""," "));
                    line = line.concat(addLineByte("",""));
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
                line = line.concat(payload);
                */
                dataOutput.writeBytes(line + "\r\n");
            }
        } catch (IOException e) {
            System.out.println("Exception at WriteLinePS");
        }
    }

    private static int vertexIndex(String vertex, int nRotX, int flitSize) {
        int x = vertexAxisX(vertex, flitSize);
        int y = vertexAxisY(vertex, flitSize);
        return (y * nRotX + x);
    }

    private static int vertexAxisX(String vertex, int flitSize) {
        int nodeX = Integer.parseInt(vertex.substring(0, vertex.length() / 2), 16);
        String nX = Conversion.zeroLeftPad(Integer.toBinaryString(nodeX), flitSize / 2);
        return Integer.parseInt(nX,2);
    }

    private static int vertexAxisY(String vertex, int flitSize) {
        int nodeY = Integer.parseInt(vertex.substring(vertex.length() / 2), 16);
        String nY = Conversion.zeroLeftPad(Integer.toBinaryString(nodeY), flitSize / 2);
        return Integer.parseInt(nY,2);
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
                timestampHex[i] = Conversion.zeroLeftPad(Integer.toHexString((int) value % 16), 1)
                        + timestampHex[i];
                value = value / 16;
            }
        }
        return timestampHex;
    }

    private String[] getTimestamp(String value) {
        String[] timestampHex = new String[4];

        value = Conversion.zeroLeftPad(value, flitWidth);
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
            Formatter output = new Formatter(path);

            for (int x = 0; x < (dimX * dimY); x++) {
                for (int y = 0; y < (dimY * dimX); y++) {
                    output.format("%d \t", nPcks[x][y]);
                }
                output.format("\r\n");
            }
            output.close();

            /*
            FileWriter fw = new FileWriter(new File(path + "nPcksHash"));
            for (String key : numberOfPacks.keySet()) {
                fw.write(key + " " + numberOfPacks.get(key) + "\n");
            }
            fw.close();
            */

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Re-initialize numberOfPacks to different OferredLoads
        this.numberOfPacks.clear();

    }

}
