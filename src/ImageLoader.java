import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Panel reprezentuj¹cy obszar ³adowania obrazów z pliku
 * @author Ewelina Wardach, Konrad Karaœ
 *
 */
public class ImageLoader extends JPanel implements ActionListener{

	JButton loadButton, testButton;
	JLabel infoLabel;
	ImagePanel imagePanel;
	JFileChooser fileChooser;
	File selectedImageFile = null;
	
	boolean imageLoaded = false;
	
	public ImageLoader(){
		
		loadButton = new JButton("Za³aduj obraz");
		testButton = new JButton("Obraz testowy");
		infoLabel = new JLabel("nie wybrano pliku");
		fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
		fileChooser.setFileFilter(filter);
		imagePanel = new ImagePanel();
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		add(imagePanel,c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		add(new JLabel("Wybrany obraz:"),c);
		c.gridx = 1;
		c.gridy = 1;
		add(infoLabel,c);
		c.gridx = 0;
		c.gridy = 2;
		add(loadButton, c);
		c.gridx = 1;
		c.gridy = 2;
		add(testButton, c);
		
		setBorder(BorderFactory.createTitledBorder("£adowanie obrazu"));
		loadButton.addActionListener(this);
		testButton.addActionListener(this);
		
		imagePanel.loadImageFile(new File("images/no_image.jpg"));
	}

	public File getImageFile(){
		return selectedImageFile;
	}
	
	public boolean imageLoaded(){
		return imageLoaded;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loadButton){
			int returnVal = fileChooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				selectedImageFile = fileChooser.getSelectedFile();
				infoLabel.setText(selectedImageFile.getName());
				imagePanel.loadImageFile(selectedImageFile);
				imageLoaded = true;
			} else {
				infoLabel.setText("nie wybrano pliku");
				selectedImageFile = null;
			}
		}
		
		else if(e.getSource() == testButton){
			selectedImageFile = new File("images/testimage.jpg");
			infoLabel.setText(selectedImageFile.getName());
			imagePanel.loadImageFile(selectedImageFile);
			imageLoaded = true;
		}
		
	}
	
}
