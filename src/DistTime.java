import javax.swing.*;
import java.io.*;
import java.util.*;

public class DistTime {

	private String name, distTime;
	private double channelRate;
	private double packetTime;
	private int numberPackets, burstSize;
	private double minRate, maxRate, incRate, avgRate, dvRate;
	private double ipRate;
	private double ipRateOn;

	public DistTime(int flitWidth, int flitClockCycles, double Frequency,
			int PacketSize, int NumberPackets, double Rate) {

		channelRate = Frequency * flitWidth;
		packetTime = PacketSize * flitClockCycles;
		numberPackets = NumberPackets;
		ipRate = (Rate / 100) * channelRate;
		// ipRate = Rate;

	}

	public DistTime(int flitWidth, int flitClockCycles, String distTime,
			double frequency, int packetSize, int numberPackets, int burstSize,
			double avgRate, double minRate, double maxRate, double dvRate,
			double incRate) {
		name = "Standard";
		this.distTime = distTime;
		channelRate = frequency * flitWidth;
		packetTime = packetSize * flitClockCycles;
		this.numberPackets = numberPackets;
		this.burstSize = burstSize;
		this.avgRate = avgRate;
		this.minRate = minRate;
		this.maxRate = maxRate;
		this.dvRate = dvRate;
		this.incRate = incRate;
	}

	public ArrayList<String> defineTime() {
		if (distTime.equalsIgnoreCase("UNIFORM"))
			return uniform();
		else if (distTime.equalsIgnoreCase("NORMAL"))
			return normal();
		else
			return pareto();
	}

	public ArrayList<String> uniform() {

		double idleTime = (double) ((channelRate / ipRate - 1) * packetTime);
		double totalTime = Math.rint(idleTime) + packetTime;
		double timestamp = 1;
		ArrayList<String> vet = new ArrayList<String>();

		for (int j = 0; j < numberPackets; j++) {
			vet.add(Integer.toHexString((int) timestamp).toUpperCase());
			timestamp = timestamp + totalTime;
		}
		return vet;
	}

	public double on(double r, int fator) {
		double alfa_on = 1.9;
		return Math.pow((1 - r), (-1 / alfa_on)) * fator;
	}

	public double off(double r, int fator) {
		double alfa_off = 1.25;
		return Math.pow((1 - r), (-1 / alfa_off)) * fator;
	}

	public ArrayList<String> pareto() {
		double periodo_on = 0, periodo_off = 0;
		double idleTime = (double) ((channelRate / ipRateOn - 1) * packetTime);
		double totalTime = idleTime + packetTime;
		double timestamp = 1, timestampBurst = 0;
		int totalPackets = 0, fator;
		int pronto = 0;
		ArrayList<String> vet;
		double r = 0;

		fator = (int) ((((numberPackets * packetTime) / burstSize) * (channelRate / ipRateOn)) / 1.5);
		// JOptionPane.showMessageDialog(null,"fator= "+fator,"T�TULO",-1);

		do {
			vet = new ArrayList<String>();
			totalPackets = 0;

			timestamp = Math.random() * packetTime;
			timestampBurst = timestamp;

			for (int i = 0; i < burstSize; i++) {

				do {
					r = Math.random();
				} while (r > 0.9);

				periodo_on = on(r, fator);
				periodo_off = off(r, fator);
				int npacks = (int) ((periodo_on / packetTime) * (ipRateOn / channelRate));
				totalPackets = totalPackets + npacks;

				for (int k = 0; k < npacks; k++) {
					vet.add(Integer.toHexString((int) timestamp).toUpperCase());
					timestamp = timestamp + totalTime;
				}
				timestampBurst = timestampBurst + periodo_on + periodo_off;

				timestamp = timestampBurst;
			}
			if (totalPackets < numberPackets) {
				vet.clear();
				pronto = 0;
				burstSize++;
			} else
				pronto = 1;
		} while (pronto == 0);

		return vet;
	}

	private double calculaNormal(double avgRate, double dvRate, double x) {

		double a = 0;
		double b = 0;
		double c = 0;

		a = dvRate * Math.sqrt(2 * Math.PI);
		b = -Math.pow((x - avgRate), 2);
		c = 2 * Math.pow(dvRate, 2);
		return ((1 / a) * Math.pow(2.71828, (b / c)));
	}

	public ArrayList<String> normal() {
		int numPackets = 0, numIntervalos = 0, inseridos = 0, maior = 0, celula_maior = 0, falta, timestamp = 0;
		double perc, percAcum = 0, idleTime, rateIp;
		Ponto auxPonto;

		ArrayList<Ponto> vAux = new ArrayList<Ponto>();
		ArrayList<String> out = new ArrayList<String>();
		String nameFile = "normal" + name;

		numIntervalos = (int) ((maxRate - minRate) / incRate);

		for (double j = minRate; j <= (minRate + (incRate * numIntervalos)); j = j
				+ incRate) {

			perc = calculaNormal(avgRate, dvRate, j) * 10;
			numPackets = (int) (numberPackets * perc);
			idleTime = (((channelRate / (float) j) - 1) * packetTime);
			inseridos = inseridos + numPackets;
			percAcum = percAcum + perc;
			rateIp = channelRate / ((idleTime / (packetTime)) + 1);

			vAux.add(new Ponto(numPackets, (int) idleTime, rateIp));
		}

		falta = numberPackets - inseridos;
		// System.out.println("falta="+falta);

		for (int i = 0; i < (numIntervalos + 1); i++) {
			auxPonto = (Ponto) vAux.get(i);
			if (auxPonto.getNumPackets() >= maior) {
				maior = auxPonto.getNumPackets();
				celula_maior = i;
			}
		}

		auxPonto = (Ponto) vAux.get(celula_maior);
		auxPonto.setNumPackets(auxPonto.getNumPackets() + falta);

		// Escreve o arquivo com os dados para serem plotados pelo gnuplot
		writeGNUDat(nameFile, vAux);

		for (int a_inserir = numberPackets; a_inserir > 0;) {
			int i = (int) (Math.random() * (numIntervalos + 1));
			auxPonto = (Ponto) vAux.get(i);

			if (auxPonto.getNumPackets() > 0) {
				a_inserir = a_inserir - 1;
				if (a_inserir == (numberPackets - 1)) {
					out.add(Integer.toHexString(0).toUpperCase());
				} else {
					timestamp = timestamp + auxPonto.getIdleTime()
							+ (int) packetTime;
					out.add(Integer.toHexString(timestamp).toUpperCase());
				}
				auxPonto.setNumPackets(auxPonto.getNumPackets() - 1);
			}
		}

		// escreve o arquivo para gerar o gr�fico no gnuplot
		writeGNUTxt(nameFile, "Normal Distribution", "Rate (Mbps)",
				"Number of Packets");
		return out;
	}

	/**
	 * Escreve em um arquivo o conte�do do vetor.
	 */
	private void writeVector(String nameFile, ArrayList<String> vet) {
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(
					nameFile));
			for (int i = 0; i < vet.size(); i++) {
				dos.writeBytes((String) vet.get(i) + "\n");
			}
			dos.close();
		} catch (Exception e) {
			System.out.println("ERRO NA ESCRITA DO ARQUIVO OUT.TXT");
		}
	}

	/**
	 * Escreve o arquivo que contem os comandos para executar o gnuplot
	 */
	private void writeGNUTxt(String nameFile, String title, String axleX,
			String axleY) {
		try {

			File file = new File(nameFile + ".txt");
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			Writer w = new BufferedWriter(osw);

			String dat = nameFile + ".dat";

			w.write("reset\n");
			w.write("set title \"" + title + "\"\n");
			w.write("set xlabel \"+" + axleX + "\"\n");
			// w.write("set xrange [0:1]\n");
			w.write("set ylabel \"" + axleY + "\"\n");
			w.write("plot '" + dat
					+ "' using ($1):($2) t\"Normal\" with linespoints 5 5\n");
			w.write("pause -1\n");
			w.close();
			file.deleteOnExit();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "ERRO",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Escreve o arquivo com os dados que ser�o plotados pelo gnuplot
	 */
	private void writeGNUDat(String nameFile, ArrayList<Ponto> vet) {
		try {

			File file = new File(nameFile + ".dat");
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			Writer w = new BufferedWriter(osw);

			Ponto auxPonto;
			for (int i = 0; i < vet.size(); i++) {
				auxPonto = (Ponto) vet.get(i);
				w.write("" + auxPonto.getRateIp() + " "
						+ auxPonto.getNumPackets() + "\n");
			}
			w.close();
			file.deleteOnExit();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "ERRO",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}

class Ponto {

	int numPackets;
	int idleTime;
	double rateIp;

	public Ponto(int numPackets, int idleTime, double rateIp) {
		this.numPackets = numPackets;
		this.idleTime = idleTime;
		this.rateIp = rateIp;
	}

	public int getNumPackets() {
		return numPackets;
	}

	public int getIdleTime() {
		return idleTime;
	}

	public double getRateIp() {
		return rateIp;
	}

	public void setNumPackets(int n) {
		numPackets = n;
	}

	public void setIdleTime(int i) {
		idleTime = i;
	}

	public void setRateIp(int r) {
		rateIp = r;
	}

}