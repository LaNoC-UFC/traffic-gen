/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uniforme;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class Main 
{

    public static void main(String[] args) 
    {
    	
    	String caminho="";
    	int dimX=0;
    	int dimY=0;
    	int n_pck=0;
    	int Flits=0;
    	int flitWidth=0;
    	ArrayList<Double> rates = new ArrayList();
    	
    	if(args.length == 6) 
    	{
    		
    		dimX=Integer.parseInt(args[0]); //X dimension
    		dimY=Integer.parseInt(args[1]); //Y dimension
    		n_pck=Integer.parseInt(args[2]); //100 packets per core
    		Flits=Integer.parseInt(args[3]); //#flits
    		flitWidth=Integer.parseInt(args[4]);
    		String[] inputRates = args[5].split(",");
    		
    		for(String rate : inputRates)
    			rates.add(Double.parseDouble(rate));
    	} 
    	else 
    	{
    		dimX=11; //X dimension
    		dimY=11; //Y dimension
    		n_pck=110; //packets per core
    		Flits=17; //#flits
    		//rates.add(20.0);
    		rates.add(80.0);
    		flitWidth=16;
    	}
    	
        int desX=2,desY=2; //for hot spot
        String distrib ="random"; //uniform -> random ; else -> hot spot
        //double rates[] = {80.0};
       
        //caminho = variaveis.chooser();
        caminho = ".";

        escreve_arquivo teste = new escreve_arquivo(dimX,dimY,flitWidth,1,flitWidth,n_pck,Flits,50.0,desX,desY,"msg");

        ArrayList<String> sinks = new ArrayList<String>();
        genSinks genS = new genSinks(dimX,dimY,flitWidth);

        for(Double rate : rates)
        {
        	 sinks = genS.doSinks(distrib,dimX,dimY,n_pck,desX,desY);
             teste.writeTraffic(sinks, distrib,caminho+File.separator+"F"+(rate.intValue()),rate);
             teste.printNofPcks(caminho+File.separator+"F"+(rate.intValue())+File.separator);
        }
        

        

        System.out.println("Done, check selected directory");
        JOptionPane.showMessageDialog(null, "Done, check selected directory", "Traffic Generator  ", JOptionPane.INFORMATION_MESSAGE);

    }

}