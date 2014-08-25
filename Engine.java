import java.io.*;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JFrame;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Engine implements KeyListener {
	char nextMove;	
	int[] lengths;
	RayCast game;
	JFrame frame;
	int height;
	int width;

	public static int[] sample(ArrayList<vector> vecArray, int height, int width, RayCast game) {
		int[] distances = new int[width];
		double step = (double) (vecArray.size()-0.0001)/(double) width;
		for (int i = 1; i <= width; i++) {
			int index = (int) (i*step);//Math.round(i*step);
			//if (index >= vecArray.size()) {
			//	index = vecArray.size() - 1;
			//} 
			if (index < 0) {
				index = 0;
			}
			//System.out.println((vecArray.get(index).length()) + " " + i + " " + (i * step) + " " + index);
			//System.out.println(vecArray.get(index).length() + " " + Math.abs(game.angle - Math.cos(vector.arcTan(vecArray.get(index)))));
			distances[i-1] = (int)(height/Math.sqrt(vecArray.get(index).length()*
						Math.cos(game.angleDiff(game.angle, vector.arcTan(vecArray.get(index))))));
			if (distances[i-1] > height) {
				distances[i-1] = height;
			}
		}
		return distances;
	}

	public static int[][] genMap(int[][] currMap) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Scanner input = new Scanner(System.in);
		char next;
		while (true) {
			for (int i = 0; i < currMap[0].length; i++) {
				for (int j = 0; j < currMap.length; j++) {
					System.out.print(currMap[j][i]);
				}
				System.out.print("\n");
			}
			System.out.println("Done? (y/n)");
			next = input.next().charAt(0);
			if (next == 'y') {
				return currMap;
			}
			System.out.println("Select x-coordinate");
			//next = input.next().charAt(0);
			//int xPos = next - '0';
			int xPos = 0, yPos = 0;
			try { 
			xPos = Integer.parseInt(br.readLine());
			while (xPos < 0 || xPos > currMap.length) {
				System.out.println("Please provide a coordinate between 0 and " + currMap.length);
				//next = input.next().charAt(0);
				//xPos = next - '0';
				xPos = Integer.parseInt(br.readLine());
			}
			System.out.println("Select y-coordinate");
			//next = input.next().charAt(0);
			//int yPos = next - '0';
			yPos = Integer.parseInt(br.readLine());
			while (yPos < 0 || yPos > currMap[0].length) {
				System.out.println("Please provide a coordinate between 0 and " + currMap[0].length);
				//next = input.next().charAt(0);
				//next = 10*next = input.next().charAt(0);
				//yPos = 	next - '0';
				yPos = Integer.parseInt(br.readLine());
			}
			} catch (java.io.IOException e) {

			}
			System.out.println("Choose value to set (0, 1)");
			next = input.next().charAt(0);
			int value = next - '0';
			while (value != 0 && value != 1) {
				System.out.println("Please provide a proper value");
				System.out.println(value);
				next = input.next().charAt(0);
				value = next - '0';
			}
			currMap[xPos][yPos] = value;
		}
	
	}

	public static BufferedImage drawImage(int[] heights, int width, int height) {
		BufferedImage toDraw = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		//int[][] heightMap = new int[width][height];	
		for (int i = 0; i < heights.length; i++) {
			int currHeight = heights[i];
			int index = (height - currHeight)/2;
			int j;
			for (j = 0; j < currHeight; j++) {
				//heightMap[i][j + index] = 1;
				//System.out.println(i + " " + (j + index));
				toDraw.setRGB(i, j + index, 255);
			}

			for (; j + index < height; j++) {
				//heightMap[i][j + index] = 2;
				toDraw.setRGB(i, j + index, 35);
			}
		}
		return toDraw;
	}
/*
	public static void paint(BufferedImage toDraw) {

	}
*/
	public static void printMap(int[] heights, int width, int height) {
		//boolean evenWidth = width % 2 == 0;
		int[][] heightMap = new int[width][height];
		for (int i = 0; i < heights.length; i++) {
			int currHeight = heights[i];
			//boolean evenHeight = currHeight % 2 == 0;
			int index = (height - currHeight)/2;/*
			if (evenWidth && evenHeight) {
				index = (width - currHeight)/2;
			} else if (evenWidth) {
				index = (width - 
			}*/
			//System.out.println(currHeight + " " + index + " " + width);
			int j;
			for (j = 0; j < currHeight; j++) {
				heightMap[i][j + index] = 1;
			}

			for (; j + index< heightMap[0].length; j++) {
				heightMap[i][j + index] = 2;
			}
		}
		
		String toPrint = "";
		for (int i = 0; i < heightMap[0].length; i++) {
			for (int j = 0; j < heightMap.length; j++) {
				int currItem = heightMap[j][i];		
				if (currItem == 0) {
					toPrint += " ";
				} else if (currItem == 1) {
					toPrint += "%";
				} else if (currItem == 2) {
					toPrint += ".";
				}
			}
		}
		System.out.println(toPrint);
	}

	public static void turn(RayCast game, char dir) {
		double angle = 0;
		if (dir == 'a') {
			angle = Math.PI/32;
		} else if (dir == 'd') {
			angle = -Math.PI/32;
		} else {
			return;
		}
		game.setAngle(game.getAngle() + angle);
	}

	public static void move(RayCast game, char dir) {
		vector currVec = game.vectors[game.angleIndex];
		double max = (double)Math.max(Math.abs(currVec.getX()), Math.abs(currVec.getY()));
		double offset = 0;
		if (dir == 'w') {
			offset = 1;
		} else if (dir == 's') {
			offset = -1;
		} else {
			return;
		}
		int dx = (currVec.getX() > 0) ? 1:-1;
		int dy = (currVec.getY() > 0) ? 1:-1;
		dx = (currVec.getX() == 0) ? 0:dx;
		dy = (currVec.getY() == 0) ? 0:dy;

		int newX = game.getXPos();
		int newY = game.getYPos();
		newX += (Math.random() < (Math.abs(currVec.getX())/max)) ? offset*dx:0;
		newY += (Math.random() < (Math.abs(currVec.getY())/max)) ? offset*dy:0;
		game.setPosition(newX, newY);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_A: this.nextMove = 'a';
								   break;
			case KeyEvent.VK_D: this.nextMove = 'd';
								   break;
			case KeyEvent.VK_W: this.nextMove = 'w';
								 break;
			case KeyEvent.VK_S: this.nextMove = 's';
								   break;
			default: this.nextMove = 'c';
		}
		System.out.println(e.getKeyCode() + " " + this.nextMove);
		turn(game, nextMove);
		move(game, nextMove);
		lengths = sample(game.drawVectors(400), height, width, game);		
        this.frame.setContentPane(new Container());
        this.frame.getContentPane().setLayout(new FlowLayout());
        this.frame.getContentPane().add(new JLabel(new ImageIcon(drawImage(this.lengths, width, height))));
        this.frame.pack();
        this.frame.setVisible(true);
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {

	}

	public static void main(String[] args) {
		int width = 0;
		int height = 0;	
		if (args.length == 2) {
			height = Integer.parseInt(args[1]);
			width = Integer.parseInt(args[0]);
		} else if (args.length == 0) {
			height = 39;
			width = 143;
		} else {
			throw new IllegalArgumentException("Input width and height or use default values");
		}

		RayCast currGame = new RayCast(100);
                for (int i = 0; i < currGame.size*currGame.size; i++) {
                        currGame.gridPoints[i%currGame.size][i/currGame.size] = (i % currGame.size == 96) ? 0:1;
                        currGame.gridPoints[i%currGame.size][i/currGame.size] = (currGame.gridPoints[i%currGame.size][i/currGame.size] == 0 ||                                                   i%currGame.size == 75 & (i/currGame.size < 45 || i/currGame.size > 55)) ? 0:1;
			if (i % currGame.size == 0 || i / currGame.size == 0 ||
				 i % currGame.size == currGame.size-1 || i / currGame.size == currGame.size-1)
				currGame.gridPoints[i%currGame.size][i/currGame.size] = 0;
                }

                currGame.xPos = 90;
                currGame.yPos = 50;
                currGame.gridPoints[currGame.xPos][currGame.yPos] = -5;
		genMap(currGame.gridPoints);
		currGame.markedGrid = currGame.gridCopy();
		System.out.println(currGame);
		ArrayList<vector> seen = currGame.drawVectors(400);
		System.out.println(currGame);
		System.out.println(seen);
		Engine engine = new Engine();
		engine.game = currGame;
		engine.lengths = sample(seen, height, width, currGame);
		engine.height = height;
		engine.width = width;
		//int[] lengths = sample(seen, height, width, currGame);
		for (int i = 0; i < engine.lengths.length; i++) {
			System.out.print(engine.lengths[i] + " " );
		}
		Scanner input = new Scanner(System.in);
		engine.frame = new JFrame();
		engine.frame.setContentPane(new Container());
		engine.frame.getContentPane().setLayout(new FlowLayout());
		engine.frame.getContentPane().add(new JLabel(new ImageIcon(drawImage(engine.lengths, width, height))));
		engine.frame.pack();
		engine.frame.setVisible(true);
		engine.frame.addKeyListener(engine);

/*
		while (true) {
			//System.out.println(currGame.xPos + " " + currGame.yPos + " " + currGame.getAngle());
			//System.out.println(currGame);
			//System.out.println(currGame.angles[currGame.angleIndex] + " " + currGame.vectors[currGame.angleIndex]);
			//printMap(lengths, width, height);
			frame.setContentPane(new Container());
			frame.getContentPane().setLayout(new FlowLayout());
			frame.getContentPane().add(new JLabel(new ImageIcon(drawImage(lengths, width, height))));
			frame.pack();
			frame.setVisible(true);
		
			//char nextMove = input.next().charAt(0);
			turn(currGame, nextMove);
			move(currGame, nextMove);
			lengths = sample(currGame.drawVectors(400), height, width, currGame);		
			//System.out.println(currGame.getAngle());
		}*/
	}
}
