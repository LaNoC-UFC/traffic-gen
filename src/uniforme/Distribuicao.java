/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uniforme;

import java.util.Vector;

/**
 *
 * @author George
 */
public class Distribuicao {


        private	String name,distTime;
	private	double channelRate;
	private	double packetTime;
	private	int numberPackets,burstSize;
	private	double minRate,maxRate,incRate,avgRate,dvRate;
	private	double ipRate;




        public Distribuicao(int flitWidth,int flitClockCycles,double Frequency,int PacketSize,int NumberPackets,double Rate){
        //Distribuicao unif  = new Distribuicao(16,1,50.0,16,10);


		channelRate = Frequency * flitWidth;
		packetTime = PacketSize * flitClockCycles;
		numberPackets = NumberPackets;
                ipRate=(Rate/100)*channelRate;

	}


       // Distribuicao unif  = new Distribuicao(16,1,50.0,16,10);
       // unif.setIpRate(10.0);



        public Vector uniform(){

		double idleTime =(double)((channelRate/ipRate - 1) * packetTime);
		double totalTime = idleTime + packetTime;
		double timestamp = 1;
		Vector vet = new Vector();

              /*  System.out.println("CHANNEL RATE: " + channelRate);
                System.out.println("IP_RATE: " + ipRate );
                System.out.println("idleTime: "+idleTime);
                System.out.println("totalTime: "+totalTime);
                System.out.println("packetTime: "+packetTime);
                System.out.println("timestamp: "+timestamp);*/
		for(int j=0;j<numberPackets;j++){
			vet.add(Integer.toHexString((int)timestamp).toUpperCase());
			timestamp = timestamp + totalTime;
                       // System.out.println("timestamp -->  " + timestamp);
		}


		return vet;
        }

    public double getIpRate() {
        return ipRate;
    }

    public void setIpRate(double ipRate) {
        this.ipRate = ipRate;
    }





}
