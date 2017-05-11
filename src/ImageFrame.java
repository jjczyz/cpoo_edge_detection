import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Klasa reprezentuj¹ca ramkê z obrazem
 * @author Ewelina Wardach, Konrad Karaœ
 *
 */
public class ImageFrame extends JFrame implements ActionListener{

	JMenuBar menuBar;
	JMenu applicationMenu;
	JMenuItem saveImageMenuItem;
	JFileChooser fileChooser;
	
	ImagePanel imagePanel;
	
	/**
	 * Konstruktor ³aduj¹cy obraz z pliku
	 * @param imageFile plik z obrazem
	 */
	public ImageFrame(File imageFile) {
		super("Obraz oryginalny");
		ImagePanel imagePanel = new ImagePanel(imageFile);
		add(imagePanel);

		setLocationRelativeTo(null);
		pack();
		setVisible(true);
	}
	
	/**
	 * Konstruktor ³aduj¹cy przteworzony obraz z programu
	 * @param image BufferedImage
	 * @param filterType String, rodzaj zastosowanego filtru
	 */
	public ImageFrame(BufferedImage image, String filterType) {
		super("Obraz przetworzony filtrem: "+filterType);
		
		menuBar = new JMenuBar();
		applicationMenu = new JMenu("Plik");
		menuBar.add(applicationMenu);
		saveImageMenuItem = new JMenuItem("Zapisz obraz...");
		applicationMenu.add(saveImageMenuItem);
		this.setJMenuBar(menuBar);
		saveImageMenuItem.addActionListener(this);
		
		fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
		fileChooser.setFileFilter(filter);
		
		imagePanel = new ImagePanel(image);
		add(imagePanel);

		setLocationRelativeTo(null);
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == saveImageMenuItem){
			int returnVal = fileChooser.showSaveDialog(null);
			if ( returnVal == JFileChooser.APPROVE_OPTION ){
			    File file = new File(fileChooser.getSelectedFile().getAbsolutePath()+".jpg");
			    try {
			    	ImageIO.write(imagePanel.getImage(), "jpg", file);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Problem z zapisaniem pliku!");
					e1.printStackTrace();
				}
			}
		}
		
	}
}