import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class SortPanel extends JPanel implements ActionListener {
	
	public Timer timer = new Timer(1, this);
	private int total;
	private Block allBlocks[];
	private Block sortArray[];
	private List<Animation> animations;
	private boolean isStopped;
	
	public SortPanel() {
		// Setting the size of the SortPanel
		Dimension dim = getPreferredSize();
		dim.width = 900;
		setPreferredSize(dim);
		
		// Giving the panel an etched border and dark gray background 
		setBorder(BorderFactory.createEtchedBorder());
		setBackground(Color.DARK_GRAY);
		
		total = 150;
		randomSet();
		animations = new ArrayList<Animation>();
		go();
		
		timer.start();
	}
	
	// This function is what paints all the blocks inside the allBlocks array
	public void paint(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;

		double canvasWidth = 900.0d;
		double canvasHeight = 700.0d;
		
		for(int i = 0; i < total; i++) {
			Rectangle2D.Double rect = new Rectangle2D.Double(i*(canvasWidth/total), canvasHeight - allBlocks[i].getValue(), canvasWidth/total, allBlocks[i].getValue());

			g2.setColor(allBlocks[i].getColor());
			g2.fill(rect);

			g2.setColor(Color.black);
			g2.draw(rect);
		}
	}
	
	// This function is called as long as the timer is on.  If the timer delay is 1 ms, then this function is called every 1 ms.
	public void actionPerformed(ActionEvent e) {
		/*
			The animations list is what gives us a visual of what is happening.  It is a List of Animaiton objects, which each contain information
			about the index, block, and whether or not a swap took place.
			
			When the list is empty, we see no change.  When there are items present however, we get the first animation in the list.
			
			If its swapAni boolean value is false, it means that only a block highlight is happening.  So, we just change the block data 
			in allBlocks at the recorded index.  Once this is done, we remove that animation from the list.  We do this until the list is empty.  
			
			If its swapAni value is true, we do the same as above but twice.  This is to make swaps more natural.  If we were to do it one block
			at a time, one block would seemingly disappear and then reappear in a different spot, which looks terrible.
		*/
		if(!animations.isEmpty() && isStopped == false) {
			Animation a = animations.get(0);
			if(a.getSwapAni()) {
				allBlocks[a.getIndex()] = a.getBlock();
				animations.remove(0);
				
				a = animations.get(0);
				allBlocks[a.getIndex()] = a.getBlock();
				animations.remove(0);
			}
			else {
				allBlocks[a.getIndex()] = a.getBlock();
				animations.remove(0);
			}
		}
		
		repaint();
	}
	
	// Gets the index of the selected algorithm in the menuPanel, then executes the correct algorithm
	public void start(int index) {
		stop();
		
		// Clearing out the animations list and making a fresh one
		animations = null;
		animations = new ArrayList<Animation>();
		
		// We use a sortArray because allBlocks can only update through animations
		sortArray = null;
		sortArray = new Block[total];
		
		// Copy the content from allBlocks into sortArray
		for(int i = 0; i < total; i++) {
			sortArray[i] = new Block(allBlocks[i].getValue(), Color.white);
		}
		
		go();
		
		if(index == 0) {
			insertionSort();
		}
		else if(index == 1) {
			shellSort();
		}
		else if(index == 2) {
			String inputValue = JOptionPane.showInputDialog("Enter number of tower: ");
			int input = Integer.parseInt(inputValue);
			JOptionPane.showMessageDialog(null, input);
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					EpicHanoi solver = new EpicHanoi(input);
					solver.hanoi(input, solver.stack1, solver.stack3, solver.stack2);
				}
			});
			thread.start();
		}
	}
	
	// stop and go functions to control the flow of animations
	public void stop() {
		isStopped = true;
	}
	
	public void go() {
		isStopped = false;
	}
	
	// Creates a set for allBlocks that is random
	public void randomSet() {
		allBlocks = null;
		allBlocks = new Block[total];
		
		animations = null;
		animations = new ArrayList<Animation>();
		
		Random random = new Random();
		for(int i = 0; i < total; i++) {
			allBlocks[i] = new Block((random.nextDouble() * 620.0d) + 55.0d);
		}
		
		stop();
	}
	
	// Creates a set for allBlocks that is in ascending order
	public void ascSet() {
		allBlocks = null;
		allBlocks = new Block[total];
		
		randomSet();
		sortAsc();
		
		stop();
	}
	
	// Creates a set for allBlocks that is in descending order
	public void descSet() {
		allBlocks = null;
		allBlocks = new Block[total];
		
		randomSet();
		sortDesc();
		
		stop();
	}

	// Uses a simple bubble sort (because 300 is the max, bubble is fast enough)
	public void sortAsc() {
		boolean isSorted = true;
		
		for(int i = 0; i < total - 1; i++) {
			for(int j = total - 1; j > i; j--) {
				if(allBlocks[j].getValue() < allBlocks[j - 1].getValue()) {
					swap(allBlocks, j, j-1);
					isSorted = false;
				}
			}
			
			if(isSorted) {
				break;
			}

			isSorted = true;
		}
	}
	
	// Reverse bubble sort
	public void sortDesc() {
		boolean isSorted = true;
		
		for(int i = 0; i < total - 1; i++) {
			for(int j = total - 1; j > i; j--) {
				if(allBlocks[j].getValue() > allBlocks[j - 1].getValue()) {
					swap(allBlocks, j, j-1);
					isSorted = false;
				}
			}
			
			if(isSorted) {
				break;
			}

			isSorted = true;
		}
	}

	// This is for swapping the locations of two blocks
	public void swap(Block[] arr, int i, int j) {
		Block tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}
	
	/*
	 	Below is where animations are created.
	 	
	 	When we create an animation, we need to send an integer value for the index, a Block for block data,
	 	and a boolean for if a swap occurred.
		
		Because we have to create new blocks for each animation (I have tried sending blocks from sortArray, but things break when doing that),
		the code can get a little messy.
	*/
	
	public void insertionSort() {
		for(int i = 1; i <= total - 1; i++) {
			for(int j = i; j > 0; j--) {
				sortArray[j].setColor(Color.red);
				animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
				
				if(sortArray[j].getValue() < sortArray[j-1].getValue()) {
					swap(sortArray, j, j-1);
					
					animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), true));
					animations.add(new Animation(j-1, new Block(sortArray[j-1].getValue(), sortArray[j-1].getColor()), false));
				}
				else {
					sortArray[j].setColor(Color.white);
					animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), true));
					sortArray[j-1].setColor(Color.white);
					animations.add(new Animation(j-1, new Block(sortArray[j-1].getValue(), sortArray[j-1].getColor()), false));
					break;
				}
				
				if(j == 1) {
					sortArray[j-1].setColor(Color.white);
					animations.add(new Animation(j-1, new Block(sortArray[j-1].getValue(), sortArray[j-1].getColor()), false));
				}
			}
		}
		
		for(int i = 0; i < total; i++) {
			sortArray[i].setColor(Color.green);
			animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), false));
		}
	}
	
	public void shellSort() {
		int gap = total/2;
		
		while(gap > 0) {
	        for(int i = gap; i < total; i++) {
	        	sortArray[i].setColor(Color.red);
	        	animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), true));
	            
	            sortArray[i-gap].setColor(Color.blue);
	        	animations.add(new Animation(i-gap, new Block(sortArray[i-gap].getValue(), sortArray[i-gap].getColor()), false));
	            
	            int j;
	            for(j = i; j >= gap && sortArray[j - gap].getValue() > sortArray[j].getValue(); j -= gap) {
	            	if(sortArray[j-gap].getColor() != Color.blue) {
	            		sortArray[j-gap].setColor(Color.blue);
	    	        	animations.add(new Animation(j-gap, new Block(sortArray[j-gap].getValue(), sortArray[j-gap].getColor()), false));
	            	}
	            	
	            	swap(sortArray, j, j-gap);
	            	
	            	animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), true));
	            	animations.add(new Animation(j-gap, new Block(sortArray[j-gap].getValue(), sortArray[j-gap].getColor()), false));

	            	sortArray[j].setColor(Color.white);
	            	animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
	            }
	            
	            sortArray[i-gap].setColor(Color.white);
		        animations.add(new Animation(i-gap, new Block(sortArray[i-gap].getValue(), sortArray[i-gap].getColor()), true));
		            

		        sortArray[j].setColor(Color.white);
		        animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
	            
	        }
	        gap = gap/2;
	    }
		
		for(int i = 0; i < total; i++) {
			sortArray[i].setColor(Color.green);
            animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), false));
		}
	}
	
	public void setTotal(int total) {
		stop();
		this.total = total;
		randomSet();
		go();
	}
	
	public void setSpeed(int speed) {
		timer.setDelay(speed);
	}
} 