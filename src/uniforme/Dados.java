/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uniforme;

/**
 *
 * @author George
 */
public class Dados {


    private int nPacote;
    private int nFlits;
    private int n;
    private int Ox;
    private int Oy;
    private int Dx;
    private int Dy;
    private int Lx;
    private int Ly;
    private String dado = "";


    public Dados(){


    }

    public Dados(int nPacotes, int nFlits,String dado){

    	this.nPacote=nPacotes;
    	this.nFlits=nFlits;

    	this.dado=dado;
    }


	public int getnPacote() {
		return nPacote;
	}


	public void setnPacote(int nPacote) {
		this.nPacote = nPacote;
	}


	public int getnFlits() {
		return nFlits;
	}


	public void setnFlits(int nFlits) {
		this.nFlits = nFlits;
	}


	public int getN() {
		return n;
	}


	public void setN(int n) {
		this.n = n;
	}


	public int getOx() {
		return Ox;
	}


	public void setOx(int ox) {
		Ox = ox;
	}


	public int getOy() {
		return Oy;
	}


	public void setOy(int oy) {
		Oy = oy;
	}


	public int getDx() {
		return Dx;
	}


	public void setDx(int dx) {
		Dx = dx;
	}


	public int getDy() {
		return Dy;
	}


	public void setDy(int dy) {
		Dy = dy;
	}


	public int getLx() {
		return Lx;
	}


	public void setLx(int lx) {
		Lx = lx;
	}


	public int getLy() {
		return Ly;
	}


	public void setLy(int ly) {
		Ly = ly;
	}


	public String getDado() {
		return dado;
	}


	public void setDado(String dado) {
		this.dado = dado;
	}

        public String converte(String destino){

            String aux="";




            return aux;
        }

        public String num_flits(int x,int y,char desX,char destY,int n_Pck){

        String linha="";
        Lx = x;
        Ly = y;
        //linha=linha.concat(priority + target+" ");

        //1`(1,[(1,centro),(2,centro),(3,centro),(4,centro)])
         linha ="1`(" + String.valueOf(n_Pck) + ",["; //getOx()


                 for (int i = 0; i < nFlits; i++) {
                    int t;
                    String centro;
        //string newDado = dado;

        // monta o dado


        // monta o t
        if (i == 0) {
            t = 1;
        } else if (i == (nFlits - 1)) {
            t = 3;
        } else {
            t = 2;
        }


        // monta o centro
                centro = "("
                        //+ n_Pck + ","
                        //+ (i + 1) + ","
                        + "0,0,"
                        //+ "0,0,0,0,0,0,0,"
                        + x + ","
                        + y + ","
                        + desX + ","
                        + destY + ","
                        + Lx + ","
                        + Ly + ","
                        + t + ")";
                        //+ dado + "\")";


        if (i != 0) {
            linha += ",";
        }

        linha += "(" + (i + 1) + "," + centro + ")";

            }

         linha += "])";


           if (n_Pck != (nPacote)) {
            linha += "++";
        }
            //System.out.println("" + linha);

    return linha;
}


}
