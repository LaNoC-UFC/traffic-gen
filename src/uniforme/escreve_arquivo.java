
package uniforme;
/*
* @(#)Escreve arquivo
*
* @George Harinson Martins Castro
* THOR - LESC - UFC - Universidade Federal do Cear�
* 
*/
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
//import AtlasPackage.*;

public class escreve_arquivo{

	private FileOutputStream file,fileOmnet,filePetri;
	private DataOutputStream dataOutput;
    private DataOutputStream dataOutputOmnet;
    private DataOutputStream dataOutputPetri;
    
    private Map<String,Integer> numberOfPacks = new HashMap<String,Integer>();

	private Conversao conversao;
	private double rate, frequency;
	private int packetSize,numberPackets,FlitSize;
	private int dimX, dimY, flitWidth, flitClockCycles;
	private double Rates[] = {10,20,30,40};
	private Integer [] nPackets;
	private Integer [] nFlits;
	int priorityO;


	private String core, address,target,distTime,dado;
	private int addressX,addressY,targetX,targetY;
        //int auxprintomnet = 0;
      

        Vector <String> alvo = new Vector();
        Vector <String> dstOmnet = new Vector(); //Vetor com destinos para OMNeT
        Vector <String> dstPetri = new Vector(); //Vetor com destinos para Rede Petri
       
        int a = 0;
        int b = 0;
	
	private String scenarioPath;
	private Dados Petri;
	private DistSpatial_THOR distSpatial;
	private String sACG;
	
	private Integer totalPackets,totalFlits=0;
	private Vector VRate,VTarget;
	private int sequenceNumberH=0,sequenceNumberL=1;
	
	
	
	/**
	* M�todo construtor da classe escreve arquivo
	*/
	public escreve_arquivo(int dimX,int dimY,int flitWidth,int flitClockCycles,int FlitSize, int numberPackets, int packetSize,double frequency,int targetX,int targetY,String dado){
		//public Distribuicao(int flitWidth,int flitClockCycles,double Frequency,int PacketSize,int NumberPackets)
		//Neste m�todo construtor eu vou colocar os par�metros
		//(N de pacotes, tamanho do pacote, Distribui��o, TARGET = random, Frequencia, Vetor de taxas)
		this.dimX=dimX;
		this.dimY=dimY;
                this.dado=dado;
		this.flitWidth= flitWidth;
		this.flitClockCycles=flitClockCycles;
		this.FlitSize =FlitSize;
		conversao = new Conversao();
		distSpatial = new DistSpatial_THOR(dimX,dimY,flitWidth);
		Petri = new Dados(numberPackets,packetSize,dado);
                nPackets=new Integer[dimX*dimY];
                for(int i =0;i<dimX*dimY;i++)
                nPackets[i]=0;
                
		nFlits=new Integer[dimX*dimY];
                for(int i =0;i<dimX*dimY;i++)
                nFlits[i]=0;
                
                this.numberPackets=numberPackets;
		this.packetSize=packetSize;
               // this.target=target;
                this.frequency=frequency;
                //private double Rates[] = {10,20,30,40};//OK
                this.rate=rate;
                priorityO=0;

               // for(int i=0;i<rates.length;i++){
               // this.scenarioPath=caminho;    //+"\\F"+((int)rates[i]);
              //  }

		this.targetX=targetX;
		this.targetY=targetY;
 	}//

//Método gera destinos estáticos para todas as cargas oferecidas

        public void gera_destinos(String Target, int dimY,int dimX,int numberPackets, int targetX, int targetY ){


            for(int y=0;y<dimY;y++){
		for(int x=0;x<dimX;x++){
                    for(int i=0;i<numberPackets;i++){

                        alvo.add(distSpatial.defineTarget(Target,x,y,targetX,targetY));

                   // target = distSpatial.defineTarget(target2,sourceX,sourceY,targetX,targetY); //GENERATE ATLAS
                    }
                }
            }

            /*for(int i=0;i<alvo.size();i++){

                System.out.println("DESTINO: " + alvo.get(i));

            }*/

            //System.out.println("TAMANHO ALVO: " + alvo.size());

        }//Fim método

        public Vector<String> gera_destino_petri(){//Vector<String> alvo
                       
            for(int i=0;i<alvo.size();i++)
            {
            	int x,y,soma;

                x = Integer.parseInt(alvo.get(i).substring(0,1))+1;
                y = Integer.parseInt(alvo.get(i).substring(1,2))+1;
                dstPetri.add(String.valueOf(x)+String.valueOf(y));
                
            }

            return dstPetri;
        }

         public void gera_destino_omnet()
         {            
            for(int i=0;i<alvo.size();i++)
                 dstOmnet.add(Conversao.dstCabaOmnet(alvo.get(i), dimY));       
         }


	/**
	* ESCREVE O TRAFEGO DA REDE DE ACORDO COM OS PAR�METROS MOSTRADOS. Especificados pelo usu�rio.
	*/

	public void writeTraffic(String target,String Caminho,double rate)
	{
		File diretory;
		double pkt_tx, flt_tx, localRate;
		
        scenarioPath=Caminho;
		diretory = new File(scenarioPath+"\\In");

		diretory.mkdirs();
            

		sACG="";
		for(int y=0;y<dimY;y++)
		{
			for(int x=0;x<dimX;x++)
			{
				try
				{
						//Create files for all routers
						file=new FileOutputStream(scenarioPath+"\\In\\in"+formatAddress(x,y)+".txt");

						dataOutput=new DataOutputStream(file);

						//Format and print packets
						writeLinesPS(target,x,y,numberPackets,rate,targetX,targetY);

						dataOutput.close();

				}

                catch(Exception e)
				{
                	System.out.println("Erro");
                }
			}

			}
		}

        public void writeLinesOmnet(int dimY,int x,int y, int numberPackets)
        {
           try
           {

        	   for(int i=0;i<numberPackets;i++)
        	   {
        		   String linha="";
        		   linha = dstOmnet.get((x*numberPackets)+(y*numberPackets*dimX)+i);

        		   dataOutputOmnet.writeBytes(linha+"\r\n");

        	   }

           }

           catch(Exception e)
           {
        	   System.out.println("exeption at write Line to OMNeT");
           }
        }


        /**
         Metodo para escrever os arquivos da REDE de Petri em arquivos txt
         **/
        public void writeLinesRedePetri(int x,int y, int numberPackets)
        {

            char desX,desY;
            String linha="";
            try
            {

            	for(int i=1;i<=numberPackets;i++)
            	{
            		linha = dstPetri.get((x*numberPackets)+(y*numberPackets*dimX)+(i-1));

            		desX = linha.charAt(0);
            		desY = linha.charAt(1);                            

            		dataOutputPetri.writeBytes(Petri.num_flits(x+1,y+1,desX,desY,i)+"\r\n");


            	}

            }

            catch(Exception e)
            {
            	System.out.println("Exception at Write Line to Petri");
            }
        }
 

	public void writeLinesPS(String target, int x, int y ,int numberPackets, double Rate, int targetX, int targetY)
	{

		String linha,payload="";
		String target2 = target;  //Ponto para variação da distribuição espacial --->>> Colocar como parâmetro

		String priority;
		int sourceX,sourceY,iTarget;
		int counter,packetSize1,payloadSize=0;
		String[] timestampHex;
                
		int destinoX,destinoY;
        destinoX=targetX;
        destinoY=targetY;

        DistTime distTime = new DistTime(flitWidth,flitClockCycles,frequency,packetSize,numberPackets,Rate);//ok

		Vector vet = distTime.uniform();
           

		try{
                        
			for(int j=0;j<numberPackets;j++)
			{
                String aux= null;
				linha="";
/***************************************************************************************/
/*    TIMESTAMP INICIAL
/***************************************************************************************/
				linha=linha.concat((String)vet.get(j)+" ");
                                

/***************************************************************************************/
/*    1� FLIT = PRIORITY (HIGH) +  TARGET (LOW)
/***************************************************************************************/
				priority = conversao.decimal_hexa(priorityO,(flitWidth/8));				
				
				sourceX=x;
				sourceY=y;
				
				//Do modification with SourceX, SourceY, TargetX, TargetY and number of lines.
  
                target = alvo.get((x*numberPackets)+(y*numberPackets*dimX)+j);

				iTarget= conversao.nodoToInteger(target.substring(target.length()-2,target.length()),dimX,FlitSize);
                             
				linha=linha.concat(priority + target+" ");
/***************************************************************************************/
/*    2� FLIT = SIZE            
/***************************************************************************************/

				packetSize1=packetSize;
				payloadSize = packetSize - 6; //6 pq 2 s�o header e 4 s�o o timestampHex da rede inserido pelo fli
				linha=linha.concat(addLineByte(Integer.toHexString(payloadSize).toUpperCase()," "));
                  
                nPackets[iTarget]++;

				nFlits[iTarget]+=packetSize;
				totalFlits+=packetSize;



/***************************************************************************************/
/*    3� FLIT = SOURCE
/***************************************************************************************/
                                
				linha=linha.concat(formatAddress(x,y)+" ");

/***************************************************************************************/
/*    4� - 7� FLITS = TIMESTAMP HEXA
/***************************************************************************************/
				timestampHex = getTimestamp((String)vet.get(j));
				linha=linha.concat(timestampHex[3]+" "+timestampHex[2]+" "+timestampHex[1]+" "+timestampHex[0]+" ");

                               
/***************************************************************************************/
/*    8� AND 9� FLIT = SEQUENCE NUMBER
/***************************************************************************************/
				linha=linha.concat(addLineByte(Integer.toHexString(sequenceNumberH).toUpperCase()," "));
				linha=linha.concat(addLineByte(Integer.toHexString(sequenceNumberL).toUpperCase()," "));
                                
                                
                                
				//incrementando o numero de sequencia
				if(sequenceNumberL==(Math.pow(2,flitWidth)-1)){
					sequenceNumberH++;
					sequenceNumberL=0;
				}
				else
					sequenceNumberL++;
/***************************************************************************************/
/*    PAYLOAD
/***************************************************************************************/
				if(j==0)
				{
					counter=8; 
					for(int l=counter;l<=payloadSize;l++)
					{

						if(l==payloadSize) //n�o coloca espa�o se � o �ltimo flit do payload
							payload=payload.concat(addLineByte(Integer.toHexString(0).toUpperCase(),""));
						else
							payload=payload.concat(addLineByte(Integer.toHexString(counter).toUpperCase()," "));

						//variando os dados do pacote
						if(counter==Math.pow(2,flitWidth)) counter=0;
						else counter++;
					}

				}
				linha=linha.concat(payload);

				dataOutput.writeBytes(linha+"\r\n");                              
			}                     
		}
                catch(Exception e){System.out.println("escrita da linha SAI AQUI Cath EXCEÇÂO");}
	}

	

	private String addLineByte(String data,String separador)
	{
		if(data!=null){
			int sizeEsperado=flitWidth/4;
			int sizeData=data.length();
			if(sizeData>sizeEsperado)
				System.out.println("Problema no tamanhos dos flits");
			else if(sizeData<sizeEsperado){
				for(int i=0;i<(sizeEsperado-sizeData);i++)
					data="0"+data;
			}
			return (data+separador);
		}
		return separador;
	}

	private String[] getTimestamp(double value)
	{
		String[] timestampHex=new String[4];
		for(int i=0;i<4;i++){ //4 � o n�mero de flits correspondente ao timestampHex
			timestampHex[i]="";
			for(int l=0;l<(flitWidth/4);l++){
				timestampHex[i] = conversao.decimal_hexa(((int) value % 16),1) + timestampHex[i];
				value = value / 16;
			}
		}
		return timestampHex;
	}

	private String[] getTimestamp(String value){
		String[] timestampHex=new String[4];

		value = conversao.setLengthHexa(value,flitWidth);
		//System.out.println("value "+value);
		for(int i=0,j=flitWidth;i<4;i++,j=j-(flitWidth/4)){
			timestampHex[i] = value.substring(j-(flitWidth/4),j);
			//System.out.println("value["+i+"] "+value.substring(j-(flitWidth/4),j));
		}
		return timestampHex;
	}


	private String formatAddress(int addX,int addY){
		String targetXBin,targetYBin,targetBin,zeros,targetHex;
		//Gerando destino na horizontal
		targetXBin=conversao.decimal_binario(addX,(flitWidth/4));
		//Gerando destino na vertical
		targetYBin=conversao.decimal_binario(addY,(flitWidth/4));
		targetBin=targetXBin+targetYBin; //concatena targetX e targetY;
		zeros = conversao.decimal_hexa(0,(flitWidth/8));
		targetHex = conversao.binario_hexa(targetBin,(flitWidth/8));
		targetHex = zeros + targetHex;
		return targetHex;
	}


}
