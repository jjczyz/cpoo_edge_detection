import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Klasa reprezentuj¹ca panel do wprowadzania parametrów przetwarzania obrazu przez u¿ytkownika
 * @author Ewelina Wardach, Konrad Karaœ
 *
 */
public class SliderPanel extends JPanel implements ChangeListener, ActionListener{
	
	private JSlider thresholdLowSlider, thresholdHighSlider, gaussSlider;
	private JLabel thresholdLowLabel, thresholdHighLabel, gaussLabel;
	private JButton resetButton;
	private JComboBox filtersComboBox;
	private String[] filtersStrings = {"Prewitt", "Sobel", "Krzy¿ Robertsa", "Brak filtru"};
	
	public SliderPanel(){
		
		thresholdLowSlider = new JSlider(JSlider.HORIZONTAL,0,100,4);
		thresholdHighSlider = new JSlider(JSlider.HORIZONTAL,0,100,10);
		gaussSlider = new JSlider(JSlider.HORIZONTAL,0,500,159);
		
		thresholdLowLabel = new JLabel("[...]");
		thresholdHighLabel = new JLabel("[...]");
		gaussLabel = new JLabel("[...]");
		
		resetButton = new JButton("Reset");
		
		thresholdLowSlider.setMajorTickSpacing(20);
		thresholdLowSlider.setMinorTickSpacing(5);
		thresholdLowSlider.setPaintTicks(true);
		thresholdLowSlider.setPaintLabels(true);
		
		thresholdHighSlider.setMajorTickSpacing(20);
		thresholdHighSlider.setMinorTickSpacing(5);
		thresholdHighSlider.setPaintTicks(true);
		thresholdHighSlider.setPaintLabels(true);
		
		gaussSlider.setMajorTickSpacing(100);
		gaussSlider.setMinorTickSpacing(10);
		gaussSlider.setPaintTicks(true);
		gaussSlider.setPaintLabels(true);
		
		filtersComboBox = new JComboBox(filtersStrings);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.WEST;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 0;
		add(new JLabel("Threshold (low):"), c);
		c.gridx = 1;
		add(thresholdLowSlider,c);
		c.gridx = 2;
		add(thresholdLowLabel, c);
		c.gridx = 0;
		c.gridy = 1;
		add(new JLabel("Threshold (high):"), c);
		c.gridx = 1;
		add(thresholdHighSlider,c);
		c.gridx = 2;
		add(thresholdHighLabel, c);
		c.gridx = 0;
		c.gridy=2;
		add(new JLabel("Rozmycie gaussa:"), c);
		c.gridx = 1;
		add(gaussSlider, c);
		c.gridx = 2;
		add(gaussLabel, c);
		c.gridy = 3;
		add(resetButton, c);
		c.gridx = 0;
		c.gridy = 3;
		add(new JLabel("Filtracja: "), c);
		c.gridx = 1;
		add(filtersComboBox, c);
		
		thresholdLowLabel.setText(""+thresholdLowSlider.getValue());
		thresholdHighLabel.setText(""+thresholdHighSlider.getValue());
		gaussLabel.setText(""+gaussSlider.getValue());
		
		setBorder(BorderFactory.createTitledBorder("Parametry"));
		
		thresholdLowSlider.addChangeListener(this);
		thresholdHighSlider.addChangeListener(this);
		gaussSlider.addChangeListener(this);
		
		resetButton.addActionListener(this);
	}
	
	public Object[] getParameters(){
		Object[] parameters = new Object[4];
		parameters[0] = thresholdLowSlider.getValue();
		parameters[1] = thresholdHighSlider.getValue();
		parameters[2] = gaussSlider.getValue();
		parameters[3] = filtersComboBox.getSelectedItem().toString();
		return parameters;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == thresholdLowSlider){
			thresholdLowLabel.setText(""+thresholdLowSlider.getValue());
		}
		else if(e.getSource() == thresholdHighSlider){
			thresholdHighLabel.setText(""+thresholdHighSlider.getValue());
		}
		else if(e.getSource() == gaussSlider){
			gaussLabel.setText(""+gaussSlider.getValue());
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == resetButton){
			thresholdLowSlider.setValue(4);
			thresholdHighSlider.setValue(10);
			gaussSlider.setValue(159);
			filtersComboBox.setSelectedIndex(0);
			
			thresholdLowLabel.setText(""+thresholdLowSlider.getValue());
			thresholdHighLabel.setText(""+thresholdHighSlider.getValue());
			gaussLabel.setText(""+gaussSlider.getValue());
		}
		
	}

}
