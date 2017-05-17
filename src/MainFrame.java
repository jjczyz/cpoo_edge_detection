import java.awt.Checkbox;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Klasa reprezentuj�ca g��wn� ramk� programu
 * @author Ewelina Wardach, Konrad Kara�
 *
 */
public class MainFrame extends JFrame implements ActionListener {
	
	ImageLoader imageLoader;
	JButton startButton;
	SliderPanel sliderPanel;
	Checkbox originalImageCheckbox, simpleAlgorithmCheckbox;
	
	CannyAlgorithm cannyAlgorithm = new CannyAlgorithm();
	
	public MainFrame(){
		super("Detektor kraw�dzi");
		
		imageLoader = new ImageLoader();
		sliderPanel = new SliderPanel();
		startButton = new JButton("Konwertuj");
		originalImageCheckbox = new Checkbox("Poka� orygina�");
		simpleAlgorithmCheckbox = new Checkbox("Prosty algorytm");
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		add(imageLoader,c);
		c.gridwidth = 1;
		c.gridheight = 3;
		c.gridx = 0;
		c.gridy = 1;
		add(sliderPanel,c);
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 1;
		add(originalImageCheckbox, c);
		c.gridx = 1;
		c.gridy = 2;
		add(simpleAlgorithmCheckbox, c);
		c.gridx = 1;
		c.gridy = 3;
		add(startButton,c);
		
		
		startButton.addActionListener(this);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		pack();
		setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == startButton){
			if(imageLoader.imageLoaded()){
				if(originalImageCheckbox.getState())
				new ImageFrame(imageLoader.getImageFile());
				cannyAlgorithm.run(imageLoader.getImageFile(), sliderPanel.getParameters(), simpleAlgorithmCheckbox.getState());
			}
			else{
				JOptionPane.showMessageDialog(this, "Nie za�adowano pliku!");
			}			
		}		
	}
}
