 import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FormPanel extends JPanel implements ChangeListener{
	
	private JLabel blockLabel;
	private JLabel speedLabel;
	private JLabel algorithmLabel;
	private JLabel timeCompLabel;
	private JLabel spaceCompLabel;
	private JSlider blockSlider;
	private JSlider speedSlider;
	private JList algorithmList;
	
	private SliderListener sliderListener;
	
	public FormPanel() {
		// Setting the size of the FormPanel
		Dimension dim = getPreferredSize();
		dim.width = 300;
		setPreferredSize(dim);
		
		// Creating the slider that represents how many blocks are in the array
		blockSlider = new JSlider(2, 300, 150);
		blockSlider.addChangeListener(this);
		blockLabel = new JLabel("Total Blocks: 150");
		
		// Creating the slider that represents how fast the animations will play
		speedSlider = new JSlider(0, 999, 999);
		speedSlider.addChangeListener(this);
		speedLabel = new JLabel("Speed: 1 ms");
		
		// Label for the list of algorithms
		algorithmLabel = new JLabel("Algorithms");
		algorithmList = new JList();
		
		// Labels for the space and time complexity of the selected algorithm.  Bubble sort is the default.
		timeCompLabel = new JLabel("Time Complexity: O( n^2 )");
		spaceCompLabel = new JLabel("Space Complexity: O(1)");
		
		// Creating the algorithms box.  First made a model, then added all the algorithms, then set the model to the JList.
		DefaultListModel algorithmModel = new DefaultListModel();
		algorithmModel.addElement("Insertion Sort");
		algorithmModel.addElement("Shell Sort");
		algorithmModel.addElement("Tower of Hanoi");
		algorithmList.setModel(algorithmModel);
		
		// JList settings
		algorithmList.setPreferredSize(new Dimension(180, 189));
		algorithmList.setBorder(BorderFactory.createEtchedBorder());
		algorithmList.setFont(new Font("Fontname", Font.BOLD, 16));
		algorithmList.setSelectedIndex(0);
		
		// Adding a listener to the JList for when the selected algorithm changes.
		algorithmList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
            	if (!e.getValueIsAdjusting()) {
            		int index = algorithmList.getSelectedIndex();
            		
            		// Based on the index, the space and time complexity labels change
            		if(index == 0) {
            			timeCompLabel.setText("Time Complexity: O( n^2 )");
            			spaceCompLabel.setText("Space Complexity: O(1)");
            		}
            		else if(index == 1) {
            			timeCompLabel.setText("Time Complexity: O( n^(3/2) )");
            			spaceCompLabel.setText("Space Complexity: O(1)");
            		}else if(index == 2) {
            			timeCompLabel.setText("Time Complexity: ?");
            			spaceCompLabel.setText("Space Complexity: ?");
            		}
                }
            }
        });
		
		// This is for centering the text in the algorithmList
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) algorithmList.getCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		setBorder(BorderFactory.createEtchedBorder());
		
		
		// Creating the GridBag layout for the position of all the sliders, labels, and list
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		/////////////////// First row ///////////////////
		
		gc.gridx = 0;
		gc.gridy = 0;
		gc.insets = new Insets(0, 0, 0, 0);
		add(blockSlider, gc);
		
		/////////////////// Second row ///////////////////
		
		gc.gridx = 0;
		gc.gridy = 1;
		gc.insets = new Insets(5, 0, 0, 0);
		add(blockLabel, gc);
		
		/////////////////// Third row ////////////////////

		gc.gridx = 0;
		gc.gridy = 2;
		gc.insets = new Insets(40, 0, 0, 0);
		add(speedSlider, gc);

		/////////////////// Fourth row ///////////////////
		
		gc.gridx = 0;
		gc.gridy = 3;
		gc.insets = new Insets(5, 0, 150, 0);
		add(speedLabel, gc);

		/////////////////// Fifth row ///////////////////

		gc.gridx = 0;
		gc.gridy = 4;
		gc.insets = new Insets(0, 0, 0, 0);
		add(algorithmLabel, gc);
		
		/////////////////// Sixth row ///////////////////
		
		gc.gridx = 0;
		gc.gridy = 5;
		gc.insets = new Insets(5, 0, 0, 0);
		add(algorithmList, gc);
		
		/////////////////// Seventh row ///////////////////
		
		gc.gridx = 0;
		gc.gridy = 6;
		gc.insets = new Insets(5, 0, 0, 0);
		add(timeCompLabel, gc);
		
		/////////////////// Eighth row ///////////////////
		
		gc.gridx = 0;
		gc.gridy = 7;
		gc.insets = new Insets(5, 0, 0, 0);
		add(spaceCompLabel, gc);
	}
	
	// Listener for the sliders
	public void setSliderListener(SliderListener listener) {
		this.sliderListener = listener;
	}
	
	// Update the total block and speed labels when the sliders change
	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider)e.getSource();
		
		if(slider == blockSlider) {
			sliderListener.sendNewBlockTotal(blockSlider.getValue());
			blockLabel.setText("Total Blocks: " + blockSlider.getValue());
		}
		else if(slider == speedSlider) {
			sliderListener.sendNewSpeed(1000 - speedSlider.getValue());
			speedLabel.setText("Speed: " + (1000 - speedSlider.getValue()) + " ms");
		}
	}
	
	public int getAlgorithmIndex() {
		return algorithmList.getSelectedIndex();
	}
}