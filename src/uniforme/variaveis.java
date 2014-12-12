/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uniforme;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author George
 */
public class variaveis {
	
    public static String chooser()
    {
        JOptionPane.showMessageDialog(null, "Select output path", "Traffic Generator  ", JOptionPane.INFORMATION_MESSAGE);

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //Diz se irá selecionar diretórios, arquivos ou arquivos e diretórios.

        if (chooser.showOpenDialog(null) == 0)
        {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile());

        } else System.exit(0);

        String j = chooser.getSelectedFile().toString();
        //JOptionPane.showMessageDialog(null, "Arquivos serão criados", "AVALIADOR  ", JOptionPane.INFORMATION_MESSAGE);

        j = j.replace("\\", "//");
        j = j + "//";

        return j;
    }
		
}
