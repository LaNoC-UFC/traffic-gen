
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
	
//	private Project project;
//	private Scenery scenery;

	/***************************************************************************/
	//GEORGE HARINSON
	private Conversao conversao; //OK
	private double rate, frequency; //OK
	private int packetSize,numberPackets,FlitSize;//OK
	private int dimX, dimY, flitWidth, flitClockCycles; //OK
	private double Rates[] = {10,20,30,40};//OK
	private Integer [] nPackets;//OK
	private Integer [] nFlits;//OK
	int priorityO;


	private String core, address,target,distTime,dado;
	private int addressX,addressY,targetX,targetY;
        //int auxprintomnet = 0;
      

        //O cadore que tá dizendo e eu to fazendo

        Vector <String> alvo = new Vector();
        Vector <String> dstOmnet = new Vector(); //Vetor com destinos para OMNeT
        Vector <String> dstPetri = new Vector(); //Vetor com destinos para Rede Petri
       
        int a = 0;
        int b = 0;
        

        //Apagar se não der certo
	
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
                       
            for(int i=0;i<alvo.size();i++){
            int x,y,soma;

                x = Integer.parseInt(alvo.get(i).substring(0,1))+1;
                y = Integer.parseInt(alvo.get(i).substring(1,2))+1;
                dstPetri.add(String.valueOf(x)+String.valueOf(y));
                
            }

            /*for(int i=0;i<alvo.size();i++){

                System.out.println("DESTINO_PETRI: " + dstPetri.get(i));

            }*/

            return dstPetri;
        }

         public void gera_destino_omnet(){//Vector<String> alvo @Rafael
             
            //Vector<String> sources = new Vector();
            
            for(int i=0;i<alvo.size();i++)
                 dstOmnet.add(Conversao.dstCabaOmnet(alvo.get(i), dimY)); //Retorna id para o OMNeT                 
            
            /*for(int i=0;i<alvo.size();i++){

                System.out.println("DESTINO_OMELETE: " + dstOmnet.get(i));

            }*/
         }


	/**
	* ESCREVE O TRAFEGO DA REDE DE ACORDO COM OS PAR�METROS MOSTRADOS. Especificados pelo usu�rio.
	*/

	public void writeTraffic(String target,String Caminho,double rate){
		File diretory,diretory1,diretory2;
		double pkt_tx, flt_tx, localRate;
		
                scenarioPath=Caminho;
		//cria o diret�rio que conter� os arquivos de trafego FLI para NoC
		diretory = new File(scenarioPath+"\\In");
                /*diretory1 = new File(scenarioPath+"\\Omnet");
                diretory2 = new File(scenarioPath+"\\Petri");*/
		diretory.mkdirs();
               /* diretory1.mkdirs();
                diretory2.mkdirs();*/

		//diretory = new File(scenarioPath+"\\Out");
		//diretory.mkdirs();
            

		sACG="";
		for(int y=0;y<dimY;y++){
			for(int x=0;x<dimX;x++){
				try{
					//ip = scenery.getIP(x,y); verificar isso depois

					//file=new FileOutputStream(scenarioPath+"\\In\\in"+(y*dimX+x)+".txt");
                                        file=new FileOutputStream(scenarioPath+"\\In\\in"+formatAddress(x,y)+".txt");
                                        /*fileOmnet=new FileOutputStream(scenarioPath+"\\Omnet\\"+Conversao.dstCabaOmnet(x, y, dimY));
                                        filePetri=new FileOutputStream(scenarioPath+"\\Petri\\flits"+String.valueOf(x+1)+String.valueOf(y+1)+".txt");*/
                                        dataOutput=new DataOutputStream(file);
                                        /*dataOutputOmnet=new DataOutputStream(fileOmnet);
                                        dataOutputPetri=new DataOutputStream(filePetri);*/
                                        //private double Rates[] = {10,20,30,40};//OK
					writeLinesPS(target,x,y,numberPackets,rate,targetX,targetY);//TEm que colocar as vari�veis ainda
                                        //writeLinesOmnet(dimY,x,y,numberPackets);
                                        //writeLinesRedePetri(x+1,y+1,numberPackets); @Rafael comentei
                                        //writeLinesRedePetri(x,y,numberPackets); //@Rafael acrescentei
                                        //writeLinesRedePetri(int x,int y, int numberPackets)



					dataOutput.close();
                                        /*dataOutputOmnet.close();
                                        dataOutputPetri.close();
*/

					}

                                catch(Exception e){System.out.println("Erro");}
				}

			}
		}

		//Fim m�todo que ir� gerar os arquivos de entrada para o trafego UNIFORME 1� a ser gerado.  George Harinson
	/****Metodo que escreve os dados para a rede OMNET
        ***/
        public void writeLinesOmnet(int dimY,int x,int y, int numberPackets){


                    try{

                        for(int i=0;i<numberPackets;i++)
                        {
                        String linha="";

                        linha = dstOmnet.get((x*numberPackets)+(y*numberPackets*dimX)+i);

                        dataOutputOmnet.writeBytes(linha+"\r\n");

                        }

                    }

                    catch(Exception e){System.out.println("escrita da linha SAI AQUI Cath OMNET EXCEÇÂO");}
        }


        /**
         Metodo para escrever os arquivos da REDE de Petri em arquivos txt
         **/
        public void writeLinesRedePetri(int x,int y, int numberPackets){

            char desX,desY;
            String linha="";
                    try{

                        for(int i=1;i<=numberPackets;i++)
                        {
                            linha = dstPetri.get((x*numberPackets)+(y*numberPackets*dimX)+(i-1)); //@Rafael Modificado -1

                            desX = linha.charAt(0);
                            desY = linha.charAt(1);
                            //Petri.num_flits(x,y,desX,desY,i); //@Rafael comentei
                            

                            //dataOutputPetri.writeBytes(linha+"\r\n");
                            dataOutputPetri.writeBytes(Petri.num_flits(x+1,y+1,desX,desY,i)+"\r\n"); //@Rafael acrescentei
                            //dataOutputPetri.writeBytes(Petri.num_flits(x+1,y+1,desX,desY,i)+"\r\n"); @Rafael comentei

                        }

                    }

                    catch(Exception e){System.out.println("escrita da linha SAI AQUI Cath CPNoC EXCEÇÂO");}
        }
 


	/**
	* Escreve as linhas do arquivo quando o chaveamento � por pacotes (Packet Switching)
	*///colocar as vari�veis ainda para funcionar
	public void writeLinesPS(String target, int x, int y ,int numberPackets, double Rate, int targetX, int targetY){

		String linha,payload="";
		String target2 = target;  //Ponto para variação da distribuição espacial --->>> Colocar como parâmetro

		String priority;
		int sourceX,sourceY,iTarget;
		int counter,packetSize1,payloadSize=0;
		String[] timestampHex;
                
		int destinoX,destinoY;
                destinoX=targetX;
                destinoY=targetY;
		
		//gera o tempo em que cada pacote deve entrar na rede
		//DistTime distTime = new DistTime(ip,flitWidth,flitClockCycles);
		//Vector vet = distTime.defineTime();
		//public DistTime(int flitWidth,int flitClockCycles, double Frequency,int PacketSize,int NumberPackets,double Rate){
                //public DistTime(int flitWidth,int flitClockCycles, double Frequency,int PacketSize,int NumberPackets,double Rate){
                //System.out.println("Rate" + Rate);
                DistTime distTime = new DistTime(flitWidth,flitClockCycles,frequency,packetSize,numberPackets,Rate);//ok
                //System.out.println("AQUI");
		Vector vet = distTime.uniform();//ok
                //System.out.println("VETOR " + vet.size());
				


		/*for(int i=0;i<vet.size();i++){
		 System.out.println("VETOR " + vet.get(i));
		}
*/
                

		try{
                        
                       // System.out.println("TESTES THOR");
			for(int j=0;j<numberPackets;j++){
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
				//priority = conversao.decimal_hexa(ip.getPriority(),(flitWidth/8));
				//System.out.println("PRIORITY: "+ priority);
				//sourceX = ip.getAddressX();
				//sourceY = ip.getAddressY();
				//targetX = ip.getTargetX();
				//targetY = ip.getTargetY();				
				
				sourceX=x;
				sourceY=y;
                               // System.out.println("X Y AQUI" + target2 + sourceX + sourceY);
				//System.out.println("TARGET: " + target);
				
				
				
                               //AQUI OHOOOOOOOOOOO
                               // target = distSpatial.defineTarget(target2,sourceX,sourceY,targetX,targetY); //GENERATE ATLAS
                                //target = alvo.get(j+a*numberPackets);
                                target = alvo.get((x*numberPackets)+(y*numberPackets*dimX)+j);

                                  

                                //System.out.println("x: "+ x + " y: " + y + "Pac: " + j);

                               

                                //System.out.println("TARGET: " + target);

				iTarget= conversao.nodoToInteger(target.substring(target.length()-2,target.length()),dimX,FlitSize);

                                //System.out.println("iTARGET: " + iTarget);
                             
				linha=linha.concat(priority + target+" ");
/***************************************************************************************/
/*    2� FLIT = SIZE            
/***************************************************************************************/
                                //System.out.println("LINHA: " + linha);
				packetSize1=packetSize;
				payloadSize = packetSize - 6; //6 pq 2 s�o header e 4 s�o o timestampHex da rede inserido pelo fli
				linha=linha.concat(addLineByte(Integer.toHexString(payloadSize).toUpperCase()," "));
				//linha=linha.concat(" ");
                                //System.out.println("Linha: "+ linha);
                                //System.out.println("VAMOS VER AQUI 111111111111112222222 George");

                                  
                                nPackets[iTarget]++;
                                //System.out.println("nPackets " + nPackets[iTarget]);
				nFlits[iTarget]+=packetSize;
                               // System.out.println("nFlits " + nFlits.length);
				totalFlits+=packetSize;
                               // System.out.println("totalFlits " + totalFlits.SIZE);
                               // System.out.println("Passo aqui AGORA: dfiuwhefw");


/***************************************************************************************/
/*    3� FLIT = SOURCE
/***************************************************************************************/
                                
				linha=linha.concat(formatAddress(x,y)+" ");
                                //System.out.println("Linha formateAddress: " + linha);
/***************************************************************************************/
/*    4� - 7� FLITS = TIMESTAMP HEXA
/***************************************************************************************/
				timestampHex = getTimestamp((String)vet.get(j));
				linha=linha.concat(timestampHex[3]+" "+timestampHex[2]+" "+timestampHex[1]+" "+timestampHex[0]+" ");
                                //System.out.println("VAMOS VER AQUI 2222");
                                //System.out.println(""+linha);
                               
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
				if(j==0){
					//Escreve o tamanho do payload do pacote
					//Se o tamanho do payload n�o preencher a largura do flit, completa com zeros.
					//Ex: flitWidth=8 payloadSize=4 resultado=04
					counter=8; //comeca em 8 pq 1 � source, 2 a 5 � timestampHex e 6 e 7 � sequencia
					for(int l=counter;l<=payloadSize;l++){

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
                                //System.out.println("!!!" + linha);
                                //System.out.println("!!!" + dstOmnet.get(auxprintomnet));
                                //dataOutputOmnet.writeBytes(dstOmnet.get(auxprintomnet) +"\r\n");
                                //auxprintomnet++;
                              
			}

                        //a++;
		}

                catch(Exception e){System.out.println("escrita da linha SAI AQUI Cath EXCEÇÂO");}
	}

	

	private String addLineByte(String data,String separador){
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

	private String[] getTimestamp(double value){
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
