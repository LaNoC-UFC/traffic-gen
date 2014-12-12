/*
* @(#)DistSpatial.java  1.0  19/05/2005
*
* Aline Vieira de Mello
* GAPH - Grupo de Apoio ao Projeto de Hardware
* Pontif�cia Universidade Cat�lica do Rio Grande do Sul - PPGCC
*/

package uniforme;
import java.awt.*;
import java.io.*;
import javax.swing.*;


public class DistSpatial_THOR{

	private int tam;
	private int dimX;
	private int dimY;
	private int flitWidth;
	private char c[];
	private Conversao conversao;

	public DistSpatial_THOR(int dimX,int dimY,int flitWidth){
		this.dimX = dimX;
		this.dimY = dimY;
		this.flitWidth = flitWidth;

		tam = 0; //tam � o n�mero m�nino de bits capaz de representar os endere�os da rede.
		if((dimX*dimY)==4) tam = 2;
		else if((dimX*dimY)==8) tam = 3;
		else if((dimX*dimY)==16) tam = 4;
		else if((dimX*dimY)==32) tam = 5;
		else if((dimX*dimY)==64) tam = 6;
		else if((dimX*dimY)==128) tam = 7;
		else tam = 8;

		c = new char[tam];
		conversao = new Conversao();
	}

	/**
	* Define o nodo destino do nodo origem informado por par�metro a partir da
	*/
	public String defineTarget(String target,int sourceX,int sourceY,int targetX,int targetY){
		String t;
		if(target.equalsIgnoreCase("random"))
			t=random(sourceX,sourceY);
                else if(target.equalsIgnoreCase("random_2"))
                        t=random_2(sourceX,sourceY);
                  //public String unif(int x, int y,int dimX,int dimY){
		else if(target.equalsIgnoreCase("bitReversal"))
			t=bitReversal(sourceX,sourceY);
		else if(target.equalsIgnoreCase("butterfly"))
			t=butterfly(sourceX,sourceY);
		else if(target.equalsIgnoreCase("complemento"))
			t=complemento(sourceX,sourceY);
		else if(target.equalsIgnoreCase("matrixTranspose"))
			t=matrixTranspose(sourceX,sourceY);
		else if(target.equalsIgnoreCase("perfectShuffle"))
			t=perfectShuffle(sourceX,sourceY);
		else
			//t=formatAddress(sourceX,sourceY);
                        t=formatAddress(targetX,targetY);
                   

		return t;
	}

	/**
	* Define o nodo destino do nodo origem informado por par�metro de forma rand�mica.
	*/
	public String random(int addX,int addY){
		int targetX,targetY;
		do{
			//Gerando destino na horizontal
			targetX=(int)(Math.random()*dimX);
			//Gerando destino na vertical
			targetY=(int)(Math.random()*dimY);
		}while(addX==targetX && addY==targetY);
		return formatAddress(targetX,targetY);
	}

       public String random_2(int addX,int addY){
		int targetX,targetY;
                
		while(true){
			//Gerando destino na horizontal
			targetX=(int)(Math.random()*dimX);
			//Gerando destino na vertical
			targetY=(int)(Math.random()*dimY);
                        if(addX!=targetX && addY!=targetY){
                            break;
                        }

		}
		return formatAddress(targetX,targetY);
	}

        public String unif(int x, int y){
        String aux="";
        int a,b,c;

        a =  (x + (1 + (int)Math.random()*dimX)) % dimX;
        b =  (y + (1 + (int)Math.random()*dimY)) % dimY;

            //aux = String.valueOf(a)+String.valueOf(b);

        return formatAddress(a,b);
    }

        

        public int random_x(int addX){
            int aux=0;






            return aux;
        }

        public int random_y(int addY){
            int aux=0;






            return aux;
        }

        /**
	* Define o nodo destino do nodo origem informado por par�metro a partir da
	* troca do bit mais significativo com o menos at� o centro do n�mero.
	* Por exemplo, 1010 para 0101  e  0100 para 0010.
	*/
	public String bitReversal(int addX,int addY){
		String targetXBin,targetYBin,targetBin;
		int targetX,targetY;

		targetXBin=conversao.decimal_binario(addX,(tam/2));
		targetYBin=conversao.decimal_binario(addY,(tam/2));
		targetBin=targetXBin+targetYBin; //concatena targetX e targetY;
		//System.out.print("targetBin="+targetBin);
		for(int i=0; i<tam; i++){ //inverte os bits
			if(targetBin.charAt(tam-1-i)=='1') c[i] = '1';
			else c[i] = '0';
		}
		targetBin = new String(c); //converte char em string
		//System.out.println(" Bit reversal targetBin="+targetBin);
		targetXBin=targetBin.substring(0,tam/2);
		targetYBin=targetBin.substring(tam/2);
		targetX = conversao.binario_decimal(targetXBin);
		targetY = conversao.binario_decimal(targetYBin);
		return formatAddress(targetX,targetY);
	}

	/**
	* Define o nodo destino do nodo origem informado por par�metro a partir da
	* troca do primeiro bit com o �ltimo bit.
	* Por exemplo, 1010 para 0011  e  1110 para 0111.
	*/
	public String butterfly(int addX,int addY){
		String targetXBin,targetYBin,targetBin;
		int targetX,targetY;

		targetXBin=conversao.decimal_binario(addX,(tam/2));
		targetYBin=conversao.decimal_binario(addY,(tam/2));
		targetBin=targetXBin+targetYBin; //concatena targetX e targetY;
		//System.out.print("targetBin="+targetBin);

		for(int i=0; i<tam; i++){ //copia todo o endereco binario
			c[i] = targetBin.charAt(i);
		}
		c[0] = targetBin.charAt(tam-1); //grava o ultimo bit na posi��o do primeiro
		c[tam-1] = targetBin.charAt(0); //grava o primeiro bit na posi��o do ultimo

		targetBin = new String(c); //converte char em string
		//System.out.println(" Butterfly targetBin="+targetBin);
		targetXBin=targetBin.substring(0,tam/2);
		targetYBin=targetBin.substring(tam/2);
		targetX = conversao.binario_decimal(targetXBin);
		targetY = conversao.binario_decimal(targetYBin);
		return formatAddress(targetX,targetY);
	}

	/**
	* Define o nodo destino do nodo origem informado por par�metro a partir da
	* invers�o do valor do bit.
	* Por exemplo, 1010 para 0101  e  1110 para 0001.
	*/
	public String complemento(int addX,int addY){
		String targetXBin,targetYBin,targetBin;
		int targetX,targetY;

		targetXBin=conversao.decimal_binario(addX,(tam/2));
		targetYBin=conversao.decimal_binario(addY,(tam/2));
		targetBin=targetXBin+targetYBin; //concatena targetX e targetY;
		//System.out.print("targetBin="+targetBin);

		for(int i=0; i<tam; i++){ //copia todo o endereco binario
			if(targetBin.charAt(i)=='1') c[i] = '0';
			else c[i] = '1';
		}

		targetBin = new String(c); //converte char em string
		//System.out.println(" Complemento targetBin= "+targetBin);
		targetXBin=targetBin.substring(0,tam/2);
		targetYBin=targetBin.substring(tam/2);
		targetX = conversao.binario_decimal(targetXBin);
		targetY = conversao.binario_decimal(targetYBin);
		return formatAddress(targetX,targetY);
	}

	/**
	* Define o nodo destino do nodo origem informado por par�metro a partir da
	* troca do endereco X pelo endereco Y.
	* Por exemplo, 0010 para 1000  e  1110 para 1011.
	*/
	public String matrixTranspose(int addX,int addY){
		return formatAddress(addY,addX);
	}

	/**
	* Define o nodo destino do nodo origem informado por par�metro a partir do
	* shift dos bits para a esquerda.
	* Por exemplo, 1010 para 0101  e  1110 para 1101.
	*/
	public String perfectShuffle(int addX,int addY){
		String targetXBin,targetYBin,targetBin;
		int targetX,targetY;

		targetXBin=conversao.decimal_binario(addX,(tam/2));
		targetYBin=conversao.decimal_binario(addY,(tam/2));
		targetBin=targetXBin+targetYBin; //concatena targetX e targetY;
		//System.out.print("targetBin="+targetBin);

		for(int i=0; i<tam; i++){ //copia todo o endereco binario
			if(i==0){
				if(targetBin.charAt(i)=='1') c[tam-1] = '1';
				else c[tam-1] = '0';
			}
			else{
				if(targetBin.charAt(i)=='1') c[i-1] = '1';
				else c[i-1] = '0';
			}
		}

		targetBin = new String(c); //converte char em string
		//System.out.println(" Complemento targetBin="+targetBin);
		targetXBin=targetBin.substring(0,tam/2);
		targetYBin=targetBin.substring(tam/2);
		targetX = conversao.binario_decimal(targetXBin);
		targetY = conversao.binario_decimal(targetYBin);
		return formatAddress(targetX,targetY);
	}

	private String formatAddress(int addX,int addY){
		String targetXBin,targetYBin,targetBin,zeros,targetHex;
		//Gerando destino na horizontal
		targetXBin=conversao.decimal_binario(addX,(flitWidth/4));
		//Gerando destino na vertical
		targetYBin=conversao.decimal_binario(addY,(flitWidth/4));
		targetBin=targetXBin+targetYBin; //concatena targetX e targetY;
		targetHex = conversao.binario_hexa(targetBin,(flitWidth/8));
		return targetHex;
	}


	/*public static void main(String s[]){
		DistSpatial g =new DistSpatial(4,4,16);
		System.out.println("Target="+g.bitReversal(1,0));
		System.out.println("Target="+g.bitReversal(0,0));
		System.out.println("Target="+g.bitReversal(1,1));
		System.out.println("Target="+g.bitReversal(2,2));
	}*/
}