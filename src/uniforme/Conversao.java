
package uniforme;
//package AtlasPackage;

/*
* @(#)Conversao.java  1.0  30/03/2001
*
* Aline Vieira de Mello
* Pontif�cia Universidade Cat�lica do Rio Grande do Sul - Faculdade de Inform�tica
*/

/**
* <i>Convers�o</i> converte valores do tipo HEXADECIMAL, DECIMAL e BINARIO.
*/
public class Conversao{
	/**
	* Verifica se a string informada eh um numero em hexadecimal.
	*/
	public boolean isHexadecimal(String valorhex)
	{
		String codigo[],cod;
		int tam;

		tam=(valorhex.length());
		codigo=new String[tam];

		//Separa bit por bit para a convers�o.
		for(int indice=0;indice<tam;indice++)
			codigo[indice]=(valorhex.substring(indice,indice+1));

		//verifica bit a bit.
		for(int i=0,j=tam-1;i<tam;i++,j--)
		{
			cod=codigo[i];

			if (cod.equalsIgnoreCase("0")) {}
			else if(cod.equalsIgnoreCase("1")) {}
			else if(cod.equalsIgnoreCase("2")) {}
			else if(cod.equalsIgnoreCase("3")) {}
			else if(cod.equalsIgnoreCase("4")) {}
			else if(cod.equalsIgnoreCase("5")) {}
			else if(cod.equalsIgnoreCase("6")) {}
			else if(cod.equalsIgnoreCase("7")) {}
			else if(cod.equalsIgnoreCase("8")) {}
			else if(cod.equalsIgnoreCase("9")) {}
			else if(cod.equalsIgnoreCase("A")) {}
			else if(cod.equalsIgnoreCase("B")) {}
			else if(cod.equalsIgnoreCase("C")) {}
			else if(cod.equalsIgnoreCase("D")) {}
			else if(cod.equalsIgnoreCase("E")) {}
			else if(cod.equalsIgnoreCase("F")) {}
			else
				return false;
		}
		return true;
	}

	/**
	* Converte um valor em HEXADECIMAL para DECIMAL.
	*/
	public int hexa_decimal(String valorhex)
	{
		String codigo[],cod;
		int dec,decimal,i,j,tam,indice;

		tam=(valorhex.length());
		codigo=new String[tam];

		//Separa bit por bit para a convers�o.
		for(indice=0;indice<tam;indice++)
			codigo[indice]=(valorhex.substring(indice,indice+1));

		//Converte bit a bit para DECIMAL.
		decimal=0;
		for(i=0,j=tam-1;i<tam;i++,j--)
		{
			cod=codigo[i];

			if(cod.equalsIgnoreCase("0"))
					dec=0;
			else if(cod.equalsIgnoreCase("1"))
					dec=1;
			else if(cod.equalsIgnoreCase("2"))
					dec=2;
			else if(cod.equalsIgnoreCase("3"))
					dec=3;
			else if(cod.equalsIgnoreCase("4"))
					dec=4;
			else if(cod.equalsIgnoreCase("5"))
					dec=5;
			else if(cod.equalsIgnoreCase("6"))
					dec=6;
			else if(cod.equalsIgnoreCase("7"))
					dec=7;
			else if(cod.equalsIgnoreCase("8"))
					dec=8;
			else if(cod.equalsIgnoreCase("9"))
					dec=9;
			else if(cod.equalsIgnoreCase("A"))
					dec=10;
			else if(cod.equalsIgnoreCase("B"))
					dec=11;
			else if(cod.equalsIgnoreCase("C"))
					dec=12;
			else if(cod.equalsIgnoreCase("D"))
					dec=13;
			else if(cod.equalsIgnoreCase("E"))
					dec=14;
			else
					dec=15;

			dec=dec*(exp(16,j));
			decimal=decimal+dec;
		}
		return decimal;
	}


	/**
	* Verifica se a string infiormada por parametro ehnum valor em binario.
	*/
	public boolean isBinario(String valor)
	{
		String codigo[],cod;
		int tam;

		tam=(valor.length());
		codigo=new String[tam];

		//Separa bit por bit para a convers�o.
		for(int indice=0;indice<tam;indice++)
			codigo[indice]=(valor.substring(indice,indice+1));

		//Verifica bit a bit se eh binario.
		for(int i=0,j=tam-1;i<tam;i++,j--)
		{
			cod=codigo[i];

			if (cod.equalsIgnoreCase("0")) {}
			else if (cod.equalsIgnoreCase("1")) {}
			else
				return false;
		}
		return true;
	}

	/**
	* Converte um valor em BINARIO para DECIMAL.
	*/
	public int binario_decimal(String valor)
	{
		String codigo[],cod;
		int dec,decimal,i,j,tam,indice;

		tam=(valor.length());
		codigo=new String[tam];

		//Separa bit por bit para a convers�o.
		for(indice=0;indice<tam;indice++)
			codigo[indice]=(valor.substring(indice,indice+1));

		//Converte bit a bit para DECIMAL.
		decimal=0;
		for(i=0,j=tam-1;i<tam;i++,j--)
		{
			cod=codigo[i];

			if(cod.equalsIgnoreCase("0"))
					dec=0;
			else
					dec=1;

			dec=dec*(exp(2,j));
			decimal=decimal+dec;
		}
		return decimal;
	}

	/**
	* Fun��o Exponencial.
	*/
	public int exp(int base,int expoente)
	{
		if(expoente==0)
			return 1;
		else
   	{
			int exp=base;
			for(int i=0;i<(expoente-1);i++)
      	exp=exp*base;
			return exp;
   	}
	}

	/**
	* Converte um valor em DECIMAL para HEXADECIMAL.
	*/
	public String decimal_hexa(int valor)
	{
		String hexadecimal,hexa,zero;
	   	int div,resto,tam,i;

	   	//Converte
	   	hexadecimal="";
	   	do
	   	{
	   		resto=(valor%16);
			valor=valor/16;
			if(resto==0)
				hexa="0";
			else if(resto==1)
				hexa="1";
			else if(resto==2)
				hexa="2";
			else if(resto==3)
				hexa="3";
			else if(resto==4)
				hexa="4";
			else if(resto==5)
				hexa="5";
			else if(resto==6)
				hexa="6";
			else if(resto==7)
				hexa="7";
			else if(resto==8)
				hexa="8";
			else if(resto==9)
				hexa="9";
			else if(resto==10)
				hexa="A";
			else if(resto==11)
				hexa="B";
			else if(resto==12)
				hexa="C";
			else if(resto==13)
				hexa="D";
			else if(resto==14)
				hexa="E";
			else
				hexa="F";

			hexadecimal=hexa.concat(hexadecimal);
   	}while(valor!=0);

   	return hexadecimal;
	}


	/**
	* Converte um valor em DECIMAL para HEXADECIMAL retornando nbytes.
	*/
	public String decimal_hexa(int valor,int nbytes)
	{
		String hexadecimal=decimal_hexa(valor);
		//se o hexadecimal possui menos bytes do que nbytes
		//esta � acrescentada de zeros no bytes + significativos
		//ate completar os nbytes de retorno
	  if(hexadecimal.length()<nbytes)
		{
	   	String zero="0";
	   	int tam=nbytes-hexadecimal.length();
	   	for(int i=0;i<tam;i++)
	   		hexadecimal=zero.concat(hexadecimal);
		}
	  //captura os nbytes - significativos
	  hexadecimal=hexadecimal.substring(hexadecimal.length()-nbytes,hexadecimal.length());
		return hexadecimal;
	}

	/**
	* Converte um valor em DECIMAL para BINARIO.
	*/
	public String decimal_binario(int numero)
	{
		String binario,bin,zero;
		int resto,tam,i;

		binario="";
		bin="";

		while(numero!=0)
		{
			resto=(numero%2);
			numero=numero/2;

			if(resto==0)
				bin="0";
			else if(resto==1)
				bin="1";
			binario=bin.concat(binario);
		}

   	return binario;
	}
	
	/**
	* Converte um valor em DECIMAL para BINARIO retornando nbytes.
	*/
	public String decimal_binario(int valor,int nbytes)
	{
		String binario=decimal_binario(valor);
		//se a string binario possui menos bytes do que nbytes
		//esta � acrescentada de zeros no bytes + significativos
		//ate completar os nbytes de retorno
	  if(binario.length()<nbytes)
		{
	   	String zero="0";
	   	int tam=nbytes-binario.length();
	   	for(int i=0;i<tam;i++)
	   		binario=zero.concat(binario);
		}
	  //captura os nbytes - significativos
	  binario=binario.substring(binario.length()-nbytes,binario.length());
		return binario;
	}

	/**
	* Converte um valor em HEXADECIMAL para BINARIO.
	*/
	public String hexa_binario(String numero)
	{
		int decimal=hexa_decimal(numero);
		return decimal_binario(decimal);
	}

	/**
	* Converte um valor em BINARIO para HEXADECIMAL.
	*/
	public String binario_hexa(String numero)
	{
		int decimal=binario_decimal(numero);
		return decimal_hexa(decimal);
	}

	/**
	* Converte um valor em HEXADECIMAL para BINARIO.
	*/
	public String hexa_binario(String numero,int nbytes)
	{
		int decimal=hexa_decimal(numero);
		return decimal_binario(decimal,nbytes);
	}

	/**
	* Converte um valor em BINARIO para HEXADECIMAL.
	*/
	public String binario_hexa(String numero,int nbytes)
	{
		int decimal=binario_decimal(numero);
		return decimal_hexa(decimal,nbytes);
	}

	/**
	* Retorna o valor da subtri��o bit a bit dos valores em hexadecimal informados por par�metro.
	*/
	public String subBin(String valorHex1,String valorHex2)
	{
		valorHex2=complemento2(valorHex2);
		return somaBin(valorHex1,valorHex2);
	}

	/**
	* Retorna o valor da soma bit a bit dos valores em hexadecimal informados por par�metro.
	*/
	public String somaBin(String valorHex1,String valorHex2)
	{
		String valorBin1,valorBin2,resultadoBin,resultadoHex,carry;
		valorBin1=hexa_binario(valorHex1);
		valorBin2=hexa_binario(valorHex2);
		resultadoBin="";
		carry="0";
		for(int i=16;i>0;i--)
		{
			if(valorBin1.substring(i-1,i).equals("1"))
			{
				if(valorBin2.substring(i-1,i).equals("1"))
				{
					if(carry.equals("1")) // 1 1 1 -> 1 1
					{
						resultadoBin="1".concat(resultadoBin);
						carry="1";
					}
					else // 1 1 0 -> 0 1
					{
						resultadoBin="0".concat(resultadoBin);
						carry="1";
					}
				}
				else
				{
					if(carry.equals("1")) // 1 0 1 -> 0 1
					{
						resultadoBin="0".concat(resultadoBin);
						carry="1";
					}
					else // 1 0 0 -> 1 0
					{
						resultadoBin="1".concat(resultadoBin);
						carry="0";
					}
				}
			}
			else
			{
				if(valorBin2.substring(i-1,i).equals("1"))
				{
					if(carry.equals("1")) // 0 1 1 -> 0 1
					{
						resultadoBin="0".concat(resultadoBin);
						carry="1";
					}
					else // 0 1 0 -> 1 0
					{
						resultadoBin="1".concat(resultadoBin);
						carry="0";
					}
				}
				else
				{
					if(carry.equals("1")) // 0 0 1 -> 1 0
					{
						resultadoBin="1".concat(resultadoBin);
						carry="0";
					}
					else // 0 0 0 -> 0 0
					{
						resultadoBin="0".concat(resultadoBin);
						carry="0";
					}
				}
			}
		}
		resultadoHex = binario_hexa(resultadoBin);
		return resultadoHex;
	}

	/**
	* Retorna o complemento de dois do valor em hexadecimal informado por par�metro.
	*/
	public String complemento2(String valorHex)
	{
		String valorBin,resultadoBin,resultadoHex;
		resultadoBin="";
		valorBin=hexa_binario(valorHex);
		for (int i=0;i<16;i++)
		{
			if(valorBin.substring(i,i+1).equals("1"))
				resultadoBin=resultadoBin.concat("0");
			else
				resultadoBin=resultadoBin.concat("1");
		}
		resultadoHex=binario_hexa(resultadoBin);
		resultadoHex=somaBin(resultadoHex,"0001"); //adiciona 1
		return resultadoHex;
	}

	/**
	* Retorna o valor em hexadecimal informado por par�metro com o tamanho informado
	* em size.
	*/
	public String setLengthHexa(String hexadecimal,int size){
		//se o hexadecimal possui menos bytes do que size
		//esta � acrescentada de zeros no bytes + significativos
		//ate completar os nbytes de retorno
	  if(hexadecimal.length()<size)
		{
	   	String zero="0";
	   	int tam=size-hexadecimal.length();
	   	for(int i=0;i<tam;i++)
	   		hexadecimal=zero.concat(hexadecimal);
		}
	  //captura os nbytes - significativos
	  hexadecimal=hexadecimal.substring(hexadecimal.length()-size,hexadecimal.length());
		return hexadecimal;
	}

	/**
	* captura uma porta em fun��o do inteiro informado por par�metro.
	*/
	public String getPort(int port){
		if(port==0) return "EAST";
		if(port==1) return "WEST";
		if(port==2) return "NORTH";
		if(port==3) return "SOUTH";
		if(port==4) return "LOCAL";
		return "EAST";
	}

	/**
	* converte endere�o no formato integer para nodo (XY)
	*/
	public String integerToNodo(int nodo,int nRotX,int flitSize){
		int x = nodo % nRotX;
		int y = nodo / nRotX;

		//Gerando destino na horizontal
		String nodoXBin=decimal_binario(x,(flitSize/4));
		//Gerando destino na vertical
		String nodoYBin=decimal_binario(y,(flitSize/4));
		 //concatena nodoX e nodoY
		String nodoBin=nodoXBin+nodoYBin;
		return binario_hexa(nodoBin,(flitSize/8));
	}

	/**
	* captura o valor do endere�o X no nodo (XY)
	*/
	public int getX(String nodo,int flitSize){
		String nodoBin = hexa_binario(nodo,flitSize/2); //o nodo corresponde a metade de um flit
		String nodoX = nodoBin.substring(0,nodoBin.length()/2);
		return binario_decimal(nodoX);
	}

	/**
	* captura o valor do endere�o Y no nodo (XY)
	*/
	public int getY(String nodo,int flitSize){
		String nodoBin = hexa_binario(nodo,flitSize/2); //o nodo corresponde a metade de um flit
		String nodoY = nodoBin.substring(nodoBin.length()/2);
		return binario_decimal(nodoY);
	}

	/**
	* converte endere�o no formato nodo (XY) para integer
	*/
	public int nodoToInteger(String nodo,int nRotX,int flitSize){
		int x = getX(nodo,flitSize);
		int y = getY(nodo,flitSize);
		return (y * nRotX + x);
	}

	/**
	* converte endere�o para o formato apresentado no arquivo de sa�da
	*/
	public String formatNodo(String nodo, int flitSize){
		String xHex = nodo.substring(0,nodo.length()/2);
		String yHex = nodo.substring(nodo.length()/2);
		String xBin = hexa_binario(xHex,flitSize/4);
		String yBin = hexa_binario(yHex,flitSize/4);
		String nodoBin = xBin + yBin;
		String nodoHex = binario_hexa(nodoBin,flitSize/8);
		return nodoHex;
	}
        
        static String dstCabaOmnet (String dst,int dimY){ //retorna id para o OMNeT. Necessita da dimensão da rede e de uma String 
                                                          //que representa o destino para o CABA
            int x,y,id;
            
            x = Integer.parseInt(dst.substring(0,1));
            y = Math.abs(Integer.parseInt(dst.substring(1,2))-(dimY-1));
            id = y*dimY + x; //id para destino no OMNeT
            return(String.valueOf(id));
            
        }

        static String dstCabaOmnet (int x,int y,int dimY){ //retorna id para o OMNeT. Necessita da dimensão da rede e de uma String
                                                          //que representa o destino para o CABA
            int id;


            int y1 = Math.abs(y-(dimY-1));
            id = y1*dimY + x; //id para destino no OMNeT
            return(String.valueOf(id));

        }



	/*public static void main(String s[]){
		Conversao c = new Conversao();
		System.out.println("2 X="+c.getX("2",8));
		System.out.println("2 Y="+c.getY("2",8));
		System.out.println("2 integer="+c.nodoToInteger("2",4,8));
		System.out.println("02 format="+c.formatNodo("02",8));
	}*/
}