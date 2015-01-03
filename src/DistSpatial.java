
public class DistSpatial {

	private int tam;
	private int dimX;
	private int dimY;
	private int flitWidth;
	private char c[];

	public DistSpatial(int dimX, int dimY, int flitWidth) {
		this.dimX = dimX;
		this.dimY = dimY;
		this.flitWidth = flitWidth;

		tam = 0; // tam � o n�mero m�nino de bits capaz de representar os
					// endere�os da rede.
		if ((dimX * dimY) == 4)
			tam = 2;
		else if ((dimX * dimY) == 8)
			tam = 3;
		else if ((dimX * dimY) == 16)
			tam = 4;
		else if ((dimX * dimY) == 32)
			tam = 5;
		else if ((dimX * dimY) == 64)
			tam = 6;
		else if ((dimX * dimY) == 128)
			tam = 7;
		else
			tam = 8;

		c = new char[tam];
	}

	/**
	 * Define o nodo destino do nodo origem informado por par�metro a partir da
	 */
	public String defineTarget(String target, int sourceX, int sourceY,
			int targetX, int targetY) {
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
		else // hot-spot
			t = Conversao.formatAddress(targetX, targetY, flitWidth);

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
		return Conversao.formatAddress(targetX, targetY, flitWidth);
	}


	/**
	 * Define o nodo destino do nodo origem informado por par�metro a partir da
	 * troca do bit mais significativo com o menos at� o centro do n�mero. Por
	 * exemplo, 1010 para 0101 e 0100 para 0010.
	 */
	private String bitReversal(int addX, int addY) {
		String targetXBin, targetYBin, targetBin;
		int targetX, targetY;

		targetXBin = Conversao.decimal_binario(addX, (tam / 2));
		targetYBin = Conversao.decimal_binario(addY, (tam / 2));
		targetBin = targetXBin + targetYBin; // concatena targetX e targetY;
		// System.out.print("targetBin="+targetBin);
		for (int i = 0; i < tam; i++) { // inverte os bits
			if (targetBin.charAt(tam - 1 - i) == '1')
				c[i] = '1';
			else
				c[i] = '0';
		}
		targetBin = new String(c); // converte char em string
		// System.out.println(" Bit reversal targetBin="+targetBin);
		targetXBin = targetBin.substring(0, tam / 2);
		targetYBin = targetBin.substring(tam / 2);
		targetX = Conversao.binario_decimal(targetXBin);
		targetY = Conversao.binario_decimal(targetYBin);
		return Conversao.formatAddress(targetX, targetY, flitWidth);
	}

	/**
	 * Define o nodo destino do nodo origem informado por par�metro a partir da
	 * troca do primeiro bit com o �ltimo bit. Por exemplo, 1010 para 0011 e
	 * 1110 para 0111.
	 */
	private String butterfly(int addX, int addY) {
		String targetXBin, targetYBin, targetBin;
		int targetX, targetY;

		targetXBin = Conversao.decimal_binario(addX, (tam / 2));
		targetYBin = Conversao.decimal_binario(addY, (tam / 2));
		targetBin = targetXBin + targetYBin; // concatena targetX e targetY;
		// System.out.print("targetBin="+targetBin);

		for (int i = 0; i < tam; i++) { // copia todo o endereco binario
			c[i] = targetBin.charAt(i);
		}
		c[0] = targetBin.charAt(tam - 1); // grava o ultimo bit na posi��o do
											// primeiro
		c[tam - 1] = targetBin.charAt(0); // grava o primeiro bit na posi��o do
											// ultimo

		targetBin = new String(c); // converte char em string
		// System.out.println(" Butterfly targetBin="+targetBin);
		targetXBin = targetBin.substring(0, tam / 2);
		targetYBin = targetBin.substring(tam / 2);
		targetX = Conversao.binario_decimal(targetXBin);
		targetY = Conversao.binario_decimal(targetYBin);
		return Conversao.formatAddress(targetX, targetY, flitWidth);
	}

	/**
	 * Define o nodo destino do nodo origem informado por par�metro a partir da
	 * invers�o do valor do bit. Por exemplo, 1010 para 0101 e 1110 para 0001.
	 */
	private String complemento(int addX, int addY) {
		String targetXBin, targetYBin, targetBin;
		int targetX, targetY;

		targetXBin = Conversao.decimal_binario(addX, (tam / 2));
		targetYBin = Conversao.decimal_binario(addY, (tam / 2));
		targetBin = targetXBin + targetYBin; // concatena targetX e targetY;
		// System.out.print("targetBin="+targetBin);

		for (int i = 0; i < tam; i++) { // copia todo o endereco binario
			if (targetBin.charAt(i) == '1')
				c[i] = '0';
			else
				c[i] = '1';
		}

		targetBin = new String(c); // converte char em string
		// System.out.println(" Complemento targetBin= "+targetBin);
		targetXBin = targetBin.substring(0, tam / 2);
		targetYBin = targetBin.substring(tam / 2);
		targetX = Conversao.binario_decimal(targetXBin);
		targetY = Conversao.binario_decimal(targetYBin);
		return Conversao.formatAddress(targetX, targetY, flitWidth);
	}

	/**
	 * Define o nodo destino do nodo origem informado por par�metro a partir da
	 * troca do endereco X pelo endereco Y. Por exemplo, 0010 para 1000 e 1110
	 * para 1011.
	 */
	private String matrixTranspose(int addX, int addY) {
		return Conversao.formatAddress(addY, addX, flitWidth);
	}

	/**
	 * Define o nodo destino do nodo origem informado por par�metro a partir do
	 * shift dos bits para a esquerda. Por exemplo, 1010 para 0101 e 1110 para
	 * 1101.
	 */
	private String perfectShuffle(int addX, int addY) {
		String targetXBin, targetYBin, targetBin;
		int targetX, targetY;

		targetXBin = Conversao.decimal_binario(addX, (tam / 2));
		targetYBin = Conversao.decimal_binario(addY, (tam / 2));
		targetBin = targetXBin + targetYBin; // concatena targetX e targetY;
		// System.out.print("targetBin="+targetBin);

		for (int i = 0; i < tam; i++) { // copia todo o endereco binario
			if (i == 0) {
				if (targetBin.charAt(i) == '1')
					c[tam - 1] = '1';
				else
					c[tam - 1] = '0';
			} else {
				if (targetBin.charAt(i) == '1')
					c[i - 1] = '1';
				else
					c[i - 1] = '0';
			}
		}

		targetBin = new String(c); // converte char em string
		// System.out.println(" Complemento targetBin="+targetBin);
		targetXBin = targetBin.substring(0, tam / 2);
		targetYBin = targetBin.substring(tam / 2);
		targetX = Conversao.binario_decimal(targetXBin);
		targetY = Conversao.binario_decimal(targetYBin);
		return Conversao.formatAddress(targetX, targetY, flitWidth);
	}

}