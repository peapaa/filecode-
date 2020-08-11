import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class EpicHanoi extends JFrame {
	private class DrawCanvas extends JPanel {
		Stack<BlockHanoi> stack;

		public DrawCanvas(Color col, Stack<BlockHanoi> stack) {
			setBackground(col);
			this.stack = stack;
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(new Color(175, 130, 46));
			g.fillRect(90, 25, 20, 295);
			if (stack.empty())
				return;

			int x = 100;
			int y = 320 - 30 * stack.size();
			int width;

			Stack<BlockHanoi> clone = (Stack<BlockHanoi>) stack.clone();
			Stack<BlockHanoi> temp = clone;
			while (!temp.empty()) {
				width = ((BlockHanoi) (temp.pop())).getSize();
				g.setColor(Color.black);
				g.drawRect(x - width / 2, y, width, 30);
				g.setColor(new Color(192, 192, 192));
				g.fillRect(x - width / 2 + 1, y + 1, width - 1, 29);
				y = y + 30;
			}
		}
	}

	private DrawCanvas canvas1, canvas2, canvas3;
	public Stack<BlockHanoi> stack1, stack2, stack3;
	public final static int DELAY = 100;
	int n;

	public EpicHanoi(int n) {
		this.n = n;
		stack1 = new Stack<BlockHanoi>();
		stack2 = new Stack<BlockHanoi>();
		stack3 = new Stack<BlockHanoi>();

		for (int i = 0; i < this.n; i++)
			stack1.push(new BlockHanoi(158 - 12 * i));

		setSize(600, 350);
		setTitle("Epic Hanoi");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridLayout(1, 3));
		canvas1 = new DrawCanvas(new Color(239, 216, 137), stack1);
		canvas2 = new DrawCanvas(new Color(239, 216, 137), stack2);
		canvas3 = new DrawCanvas(new Color(239, 216, 137), stack3);
		add(canvas1);
		add(canvas2);
		add(canvas3);
		setVisible(true);
	}

	public void paintAll() {
		canvas1.repaint();
		canvas2.repaint();
		canvas3.repaint();
	}

	public void hanoi(int n, Stack<BlockHanoi> A, Stack<BlockHanoi> B, Stack<BlockHanoi> C) {
		this.n = n;
		if (n == 1) {
			B.push(A.pop());
			paintAll();
			Wait.manySec(DELAY);
			return;
		} else {
			hanoi(n - 1, A, C, B);
			B.push(A.pop());
			paintAll();
			Wait.manySec(DELAY);
			hanoi(n - 1, C, B, A);
		}
	}

	public static void main(String[] args) {
		EpicHanoi solver = new EpicHanoi(7);
		solver.hanoi(7, solver.stack1, solver.stack3, solver.stack2);
//		solver.setVisible(true);
	}
}

class BlockHanoi {
	private int size;
	private int moved;

	public BlockHanoi(int size) {
		this.size = size;
		this.moved = 0;
	}

	public void move() {
		this.moved++;
	}

	public int getSize() {
		return size;
	}
}

class Wait {
	public static void oneSec() {
		Thread thread = new Thread(new Runnable() {

			ch (InterruptedException e) {
					e.printStackTrace();
				}
			}@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.currentThread().sleep(1000);
				} cat
		});
		thread.start();
	}

	public static void manySec(long s) {
		try {
			Thread.currentThread().sleep(s);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}