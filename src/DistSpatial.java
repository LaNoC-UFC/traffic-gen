
public class DistSpatial {

	private int numberOfBits;
	private int dimX;
	private int dimY;
	private int flitWidth;
	private char c[];

	public DistSpatial(int dimX, int dimY, int flitWidth) {
		this.dimX = dimX;
		this.dimY = dimY;
		this.flitWidth = flitWidth;
		numberOfBits = (int) Math.ceil((Math.log((dimX * dimY)) / Math.log(2)));
		c = new char[numberOfBits];
	}

	/**
	 * Define o nodo destino do nodo origem informado por par�metro a partir da
	 */
	public String defineTarget(String target, int sourceX, int sourceY) {
		String t;
		if (target.equalsIgnoreCase("random"))
			t = random(sourceX, sourceY);
		else if (target.equalsIgnoreCase("bitReversal"))
			t = bitReversal(sourceX, sourceY);
		else if (target.equalsIgnoreCase("butterfly"))
			t = butterfly(sourceX, sourceY);
		else if (target.equalsIgnoreCase("complemento"))
			t = complemento(sourceX, sourceY);
		else if (target.equalsIgnoreCase("matrixTranspose"))
			t = matrixTranspose(sourceX, sourceY);
		else if (target.equalsIgnoreCase("perfectShuffle"))
			t = perfectShuffle(sourceX, sourceY);
		else 
		{	//target have the destination of hotspot
			int targetX = Integer.parseInt(target.split("\\.")[0]); 
			int targetY = Integer.parseInt(target.split("\\.")[1]); 
			t = Conversion.formatAddress(targetX, targetY, flitWidth);
		}

		return t;
	}

	/**
	 * Define o nodo destino
	 */
	private String random(int addX, int addY) {
		int targetX, targetY;
		do {
			// Gerando destino na horizontal
			targetX = (int) (Math.random() * dimX);
			// Gerando destino na vertical
			targetY = (int) (Math.random() * dimY);
		} while (addX == targetX && addY == targetY);
		return Conversion.formatAddress(targetX, targetY, flitWidth);
	}


	/**
	 * Define o nodo destino do nodo origem informado por par�metro a partir da
	 * troca do bit mais significativo com o menos at� o centro do n�mero. Por
	 * exemplo, 1010 para 0101 e 0100 para 0010.
	 */
	private String bitReversal(int addX, int addY) {
		String targetXBin, targetYBin, targetBin;
		int targetX, targetY;

		targetXBin = Conversion.zeroLeftPad(Integer.toBinaryString(addX), (numberOfBits / 2));
		targetYBin = Conversion.zeroLeftPad(Integer.toBinaryString(addY), (numberOfBits / 2));
		targetBin = targetXBin + targetYBin; // concatena targetX e targetY;
		// System.out.print("targetBin="+targetBin);
		for (int i = 0; i < numberOfBits; i++) { // inverte os bits
			if (targetBin.charAt(numberOfBits - 1 - i) == '1')
				c[i] = '1';
			else
				c[i] = '0';
		}
		targetBin = new String(c); // converte char em string
		// System.out.println(" Bit reversal targetBin="+targetBin);
		targetXBin = targetBin.substring(0, numberOfBits / 2);
		targetYBin = targetBin.substring(numberOfBits / 2);
		targetX = Integer.parseInt(targetXBin, 2);
		targetY = Integer.parseInt(targetYBin, 2);
		return Conversion.formatAddress(targetX, targetY, flitWidth);
	}

	/**
	 * Define o nodo destino do nodo origem informado por par�metro a partir da
	 * troca do primeiro bit com o �ltimo bit. Por exemplo, 1010 para 0011 e
	 * 1110 para 0111.
	 */
	private String butterfly(int addX, int addY) {
		String targetXBin, targetYBin, targetBin;
		int targetX, targetY;

		targetXBin = Conversion.zeroLeftPad(Integer.toBinaryString(addX), (numberOfBits / 2));
		targetYBin = Conversion.zeroLeftPad(Integer.toBinaryString(addY), (numberOfBits / 2));
		targetBin = targetXBin + targetYBin; // concatena targetX e targetY;
		// System.out.print("targetBin="+targetBin);

		for (int i = 0; i < numberOfBits; i++) { // copia todo o endereco binario
			c[i] = targetBin.charAt(i);
		}
		c[0] = targetBin.charAt(numberOfBits - 1); // grava o ultimo bit na posi��o do
											// primeiro
		c[numberOfBits - 1] = targetBin.charAt(0); // grava o primeiro bit na posi��o do
											// ultimo

		targetBin = new String(c); // converte char em string
		// System.out.println(" Butterfly targetBin="+targetBin);
		targetXBin = targetBin.substring(0, numberOfBits / 2);
		targetYBin = targetBin.substring(numberOfBits / 2);
		targetX = Integer.parseInt(targetXBin, 2);
		targetY = Integer.parseInt(targetYBin, 2);
		return Conversion.formatAddress(targetX, targetY, flitWidth);
	}

	/**
	 * Define o nodo destino do nodo origem informado por par�metro a partir da
	 * invers�o do valor do bit. Por exemplo, 1010 para 0101 e 1110 para 0001.
	 */
	private String complemento(int addX, int addY) {
		String targetXBin, targetYBin, targetBin;
		int targetX, targetY;

		targetXBin = Conversion.zeroLeftPad(Integer.toBinaryString(addX), (numberOfBits / 2));
		targetYBin = Conversion.zeroLeftPad(Integer.toBinaryString(addY), (numberOfBits / 2));
		targetBin = targetXBin + targetYBin; // concatena targetX e targetY;
		// System.out.print("targetBin="+targetBin);

		for (int i = 0; i < numberOfBits; i++) { // copia todo o endereco binario
			if (targetBin.charAt(i) == '1')
				c[i] = '0';
			else
				c[i] = '1';
		}

		targetBin = new String(c); // converte char em string
		// System.out.println(" Complemento targetBin= "+targetBin);
		targetXBin = targetBin.substring(0, numberOfBits / 2);
		targetYBin = targetBin.substring(numberOfBits / 2);
		targetX = Integer.parseInt(targetXBin, 2);
		targetY = Integer.parseInt(targetYBin, 2);
		return Conversion.formatAddress(targetX, targetY, flitWidth);
	}

	/**
	 * Define o nodo destino do nodo origem informado por par�metro a partir da
	 * troca do endereco X pelo endereco Y. Por exemplo, 0010 para 1000 e 1110
	 * para 1011.
	 */
	private String matrixTranspose(int addX, int addY) {
		return Conversion.formatAddress(addY, addX, flitWidth);
	}

	/**
	 * Define o nodo destino do nodo origem informado por par�metro a partir do
	 * shift dos bits para a esquerda. Por exemplo, 1010 para 0101 e 1110 para
	 * 1101.
	 */
	private String perfectShuffle(int addX, int addY) {
		String targetXBin, targetYBin, targetBin;
		int targetX, targetY;

		targetXBin = Conversion.zeroLeftPad(Integer.toBinaryString(addX), (numberOfBits / 2));
		targetYBin = Conversion.zeroLeftPad(Integer.toBinaryString(addY), (numberOfBits / 2));
		targetBin = targetXBin + targetYBin; // concatena targetX e targetY;
		// System.out.print("targetBin="+targetBin);

		for (int i = 0; i < numberOfBits; i++) { // copia todo o endereco binario
			if (i == 0) {
				if (targetBin.charAt(i) == '1')
					c[numberOfBits - 1] = '1';
				else
					c[numberOfBits - 1] = '0';
			} else {
				if (targetBin.charAt(i) == '1')
					c[i - 1] = '1';
				else
					c[i - 1] = '0';
			}
		}

		targetBin = new String(c); // converte char em string
		// System.out.println(" Complemento targetBin="+targetBin);
		targetXBin = targetBin.substring(0, numberOfBits / 2);
		targetYBin = targetBin.substring(numberOfBits / 2);
		targetX = Integer.parseInt(targetXBin, 2);
		targetY = Integer.parseInt(targetYBin, 2);
		return Conversion.formatAddress(targetX, targetY, flitWidth);
	}

}