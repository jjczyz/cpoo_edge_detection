import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Klasa reprezentuj¹ca panel do ³adowania obrazów
 * @author Ewelina Wardach, Konrad Karaœ
 *
 */
public class ImagePanel extends JPanel {

	private BufferedImage image;
	
	/**
	 * Konstruktor do ³adowania obrazu z pliku
	 * @param imageFile plik z obrazem
	 */
	public ImagePanel(File imageFile) {
		super();

		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Problem z wczytaniem pliku!");
			e.printStackTrace();
		}

		Dimension dimension = new Dimension(image.getWidth(), image.getHeight());
		setPreferredSize(dimension);
	}
	
	/**
	 * Konstruktor do ³adowania obrazu stworzonego w programie klasy BufferedImage
	 * @param image obraz klasy BufferedImage
	 */
	public ImagePanel(BufferedImage image) {
		super();

		this.image = image;

		Dimension dimension = new Dimension(image.getWidth(), image.getHeight());
		setPreferredSize(dimension);
	}
	
	/**
	 * Konstruktor domyœlny
	 */
	public ImagePanel() {
		super();
		
		Dimension dimension = new Dimension(350, 300);
		setPreferredSize(dimension);
	}
	
	/**
	 * Funkcja zwraca za³adowany obraz
	 * @return BufferedImage
	 */
	public BufferedImage getImage() {
		return image;
	}
	
	/**
	 * Wczytywanie obrazu z pliku
	 * @param imageFile plik z obrazem
	 */
	public void loadImageFile(File imageFile){		
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Problem z wczytaniem pliku!");
			e.printStackTrace();
		}
		
		this.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}
}