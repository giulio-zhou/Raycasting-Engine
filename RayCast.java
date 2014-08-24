import java.util.ArrayList;
import java.util.Stack;
public class RayCast{
	int size;
	int[][] gridPoints;
	int[][] markedGrid;
	int xPos;
	int yPos;
	double angle;
	int angleIndex;
	vector[] vectors;
	double[] angles;
	
	public RayCast(int size) {
		this.size = size;
		this.gridPoints = new int[this.size][this.size];
		this.markedGrid = this.gridCopy();
		this.xPos = 0;
		this.yPos = 0;
		this.angle = 0;//7*Math.PI/32;
		this.angleIndex = 0;
		this.vectors = vector.genArray(-100, 100, -100, 100);	
		this.angles = new double[this.vectors.length];
		double minimum = Double.MAX_VALUE;

		for (int i = 0; i < angles.length; i++) { 
			this.angles[i] = vector.arcTan(this.vectors[i]);
			//System.out.println(Math.abs(angles[i] - currGame.angle) + " " + i + " " + minimum + " " + angles[i]);
			if (Math.abs(angles[i] - this.angle) < minimum) {
				minimum	= Math.abs(this.angles[i] - this.angle);
				this.angleIndex = i;
			}
		}

	}
/*
	public RayCast(int size) {
		this.size = size;
		this.gridPoints = new int[this.size*this.size];
		this.xPos = 0;
		this.yPos = 0;
		this.angle = 0;
		this.angleIndex = 0;
		this.vectors = vector.genArray(-5, 5, -5, 5);
	}
*/

	public int getXPos() {
		return this.xPos;
	}

	public int getYPos() {
		return this.yPos;
	}
	
	public void setPosition(int x, int y) {
		if (this.gridPoints[x][y] == 1) {
			this.gridPoints[this.xPos][this.yPos] = 1;
			this.xPos = x;
			this.yPos = y;
			this.gridPoints[this.xPos][this.yPos] = -5;
		}
	}

	public double getAngle() {
		return this.angle;
	}
	
	public void setAngle(double angle) {
		if (angle < 0) {
			this.angle = angle + 2*Math.PI;
		} else if (angle > 2*Math.PI) {
			this.angle = angle - 2*Math.PI;
		} else {
			this.angle = angle;
		}

		double minimum = Double.MAX_VALUE;
		for (int i = 0; i < angles.length; i++) {
			if (Math.abs(angles[i] - this.angle) < minimum) {
				minimum = Math.abs(this.angles[i] - this.angle);
				this.angleIndex = i;
			}
		}
	}

	public double angleDiff(double angle1, double angle2) {
		//angle1 = Math.min(Math.abs(angle1), Math.min(Math.abs(angle1 + 2*Math.PI), Math.abs(angle1 - 2*Math.PI)));
		//angle2 = Math.min(Math.abs(angle2), Math.min(Math.abs(angle2 + 2*Math.PI), Math.abs(angle2 - 2*Math.PI)));		
		//angle1 += (Math.min(Math.abs(angle1), Math.abs(angle1 + 2*Math.PI)) == Math.abs(angle1 + 2*Math.PI)) ? 2*Math.PI:0;
		//angle1 += (Math.min(Math.abs(angle1), Math.abs(angle1 - 2*Math.PI)) == Math.abs(angle1 - 2*Math.PI)) ? -2*Math.PI:0;
		//angle2 += (Math.min(Math.abs(angle2), Math.abs(angle2 + 2*Math.PI)) == Math.abs(angle2 + 2*Math.PI)) ? 2*Math.PI:0;
		//angle2 += (Math.min(Math.abs(angle2), Math.abs(angle2 - 2*Math.PI)) == Math.abs(angle2 - 2*Math.PI)) ? -2*Math.PI:0;
		if (Math.abs(angle1 - angle2) > Math.PI) {
			return Math.min(Math.abs(Math.max(angle1, angle2) - 2*Math.PI - Math.min(angle1, angle2)), 
				Math.min(angle1, angle2) + 2*Math.PI - Math.max(angle1, angle2));
		}
		
		return Math.abs(angle1 - angle2);
	}

	public void drawVectors() {
		double maxDistance = 100;	
	}

	public ArrayList<vector> drawVectors(double maxDistance) {
		Stack<vector> back = new Stack<vector>();
		ArrayList<vector> seen = new ArrayList<vector>();
		this.markedGrid = this.gridCopy();
		int backCounter = (this.angleIndex - 1 > -1) ? this.angleIndex-1:this.angleIndex-1 + this.angles.length;
		int forwardCounter = this.angleIndex;
		int numPoints = 0;

		while (this.angleDiff(this.angles[forwardCounter], this.angle) < Math.PI/4) {
	//Math.abs(this.angles[forwardCounter] - this.angle) % (2*Math.PI) < Math.PI/4) {//, Math.abs(this.angles[forwardCounter] -
					 //this.angle - 2*Math.PI)) < Math.PI/4) {
			vector currVec = this.vectors[forwardCounter];
			int xOffset = currVec.getX();
			int yOffset = currVec.getY();
			boolean found = false;
			boolean wall = false;
			int newX = -1;
			int newY = -1;
			//System.out.println(currVec + " " + forwardCounter);

			for (int i = 1; !wall && !found && currVec.length()*i < maxDistance; i++) {
				int currX = this.xPos + i*xOffset;
				int currY = this.yPos + i*yOffset;				
				if (currX > -1 && currX < this.gridPoints.length && currY > -1 && currY < this.gridPoints[0].length) {
				//System.out.println("x_offset: " + xOffset + " y_offset: " + yOffset);	
/*
                                for (int j = 0; !wall && j < yOffset; j++) {
                                        for (int k = 0; k < xOffset; k++) {
                                                        //	System.out.println(this.gridPoints[currX - k][currY - j]);
                                                if ((j != 0 || k != 0) && (j != yOffset || k != xOffset)) {
                                                        if (this.gridPoints[currX - k][currY - j] == 0) {
                                                                wall = true;
                                                                break;
                                                        }
                                                }
                                        }
                                }*/
					for (int k = (xOffset > -1) ? 1:-1; !wall && Math.abs(k) < Math.abs(xOffset);) {
						int yShift = (yOffset*Math.abs(k)/xOffset);
						yShift *= (yOffset*yShift > 0) ? 1:-1;
						//System.out.println(currX + " " + currY + " " + (currX - k) + " " + (currY-yShift));
						if (currY-yShift + 1 < this.gridPoints[0].length) {
							if (this.gridPoints[currX-k][currY-yShift] == 0 &&
								 this.gridPoints[currX-k][currY-yShift+1] == 0) {
								wall = true;
								break;
							}
						}
						k += (xOffset > -1) ? 1:-1;
					}			
		

					for (int k = (yOffset > -1) ? 1:-1; !wall && Math.abs(k) < Math.abs(yOffset);) {
						int xShift = (xOffset*Math.abs(k)/yOffset);
						xShift *= (xOffset*xShift > 0) ? 1:-1;
						if (currX-xShift+1 < this.gridPoints.length) {
							if (this.gridPoints[currX-xShift][currY-k] == 0 && 
								this.gridPoints[currX-xShift+1][currY-k] == 0) {
								wall = true;
								break;
							}
						}
						k += (yOffset > -1) ? 1:-1;
					}

					if (!wall && currY < this.gridPoints[0].length && this.gridPoints[currX][currY] == 0) {
						found = true;
						newX = currX;
						newY = currY;
						numPoints--;
						break;
					}	
				}
			}
			//System.out.println(found);
			if (found) {
				this.markedGrid[newX][newY] = numPoints;//(int)Math.sqrt(Math.pow((double) (newX - this.xPos), 2) + 
								//Math.pow((double) (newY - this.yPos), 2));
				//System.out.println(currVec + " " + numPoints);
				back.push(new vector(newX - this.xPos, newY - this.yPos));
			}	
			forwardCounter = (forwardCounter + 1) % this.angles.length;
			//System.out.println(this.angles[forwardCounter] + " " + this.angle + " " + this.angleDiff(this.angles[forwardCounter], this.angle));
		}

			while (!back.isEmpty()) {
				seen.add(back.pop());
			}
		while (this.angleDiff(this.angles[backCounter], this.angle) < Math.PI/4) {
		//(Math.min(Math.abs(this.angles[backCounter] - this.angle), Math.abs(this.angles[backCounter] - this.angle - 2*Math.PI))
			//			 < Math.PI/4) {
			vector currVec = this.vectors[backCounter];
			int xOffset = currVec.getX();
			int yOffset = currVec.getY();
			boolean found = false;
			boolean wall = false;
			int newX = -1;
			int newY = -1;
			//System.out.println(currVec + " " + currVec.length());
			for (int i = 1; !found && !wall && currVec.length()*i < maxDistance; i++) {
				int currX = this.xPos + i*xOffset;
				int currY = this.yPos + i*yOffset;			
				//System.out.println("x_offset: " + xOffset + " y_offset: " + yOffset);		
	                        if (currX > -1 && currX < this.gridPoints.length && currY > -1 && currY < this.gridPoints[0].length) {
				/*for (int j = 0; !wall && Math.abs(j) < Math.abs(yOffset);) {
					for (int k = 0; Math.abs(k) < Math.abs(xOffset);) {
						if ((j != 0 || k != 0) && (j != yOffset || k != xOffset)) {
							//System.out.println(this.gridPoints[currX - k][currY - j]);
							if (this.gridPoints[currX - k][currY - j] == 0) {				
								wall = true;
								break;
							}
						}
						k += (xOffset > -1) ? 1:-1;
					}
					 j += (yOffset > -1) ? 1:-1;
				} */

					for (int k = (xOffset > -1) ? 1:-1; !wall && Math.abs(k) < Math.abs(xOffset);) {
						int yShift = (yOffset*k/xOffset);
						yShift *= (yShift * yOffset > 0) ? 1:-1;
						//System.out.println(currX + " " + currY + " " + (currX - k) + " " + (currY-yShift));
						if (currY-yShift+1 < this.gridPoints[0].length) {
							if (this.gridPoints[currX-k][currY-yShift] == 0 &&
								 this.gridPoints[currX-k][currY-yShift+1] == 0) {
								wall = true;
								break;
							}
						}
						k += (xOffset > -1) ? 1:-1;
					}			
		

					for (int k = (yOffset > -1) ? 1:-1; !wall && Math.abs(k) < Math.abs(yOffset);) {
						int xShift = (xOffset*k/yOffset);
						xShift *= (xShift * xOffset > 0) ? 1:-1;
						if (currX-xShift+1 < this.gridPoints.length) {
							if (this.gridPoints[currX-xShift][currY-k] == 0 && 
								this.gridPoints[currX-xShift+1][currY-k] == 0) {
								wall = true;
								break;
							}
						}
						k += (yOffset > -1) ? 1:-1;
					}
				if (!wall && this.gridPoints[currX][currY] == 0) {
					found = true;
					newX = currX;
					newY = currY;
					numPoints--;
					break;
				}		
				}
			}
				//System.out.println(found);
			if (found) {
				this.markedGrid[newX][newY] = numPoints;//(int)Math.sqrt(Math.pow((double) (newX - this.xPos), 2) + 
								//Math.pow((double) (newY - this.yPos), 2));
				seen.add(new vector(newX - this.xPos, newY - this.yPos));
				//System.out.println(currVec + " " + numPoints);
			}	
			backCounter =  (backCounter - 1 > -1) ? backCounter-1:backCounter-1 + this.angles.length;
		//	System.out.println(this.angleDiff(this.angles[backCounter], this.angle) + " " + this.angles[backCounter] + " " + this.angle);
		}
				
		return seen;
	}

	public String toString() {
		String output = "";
		for (int i = this.size-1; i > -1; i--) {
			for (int j = 0; j < this.size; j++) {
				//output += Integer.toString(this.gridPoints[j][i]);// + " ";
				output += Integer.toString(this.markedGrid[j][i]);
			}
			output += "\n";
		}
		return output;
	}

	public int[][] gridCopy() {
		int[][] toReturn = new int[this.gridPoints.length][this.gridPoints[0].length];
		for (int i = 0; i < this.gridPoints.length; i++) {
			for (int j = 0; j < this.gridPoints[0].length; j++) {
				toReturn[i][j] = this.gridPoints[i][j];
			}
		}
		return toReturn;
	}

	public static void main(String[] args) {
		RayCast currGame = new RayCast(100);
		for (int i = 0; i < currGame.size*currGame.size; i++) {
			currGame.gridPoints[i%currGame.size][i/currGame.size] = (i % currGame.size == 96) ? 0:1;
			currGame.gridPoints[i%currGame.size][i/currGame.size] = (currGame.gridPoints[i%currGame.size][i/currGame.size] == 0 || 							 i%currGame.size == 75 & (i/currGame.size < 35 || i/currGame.size > 65)) ? 0:1;
		}	
	
		currGame.xPos = 50;
		currGame.yPos = 50;
		currGame.gridPoints[currGame.xPos][currGame.yPos] = -5;	
		System.out.println(currGame);
		System.out.println(currGame.angleIndex + " " + currGame.angles[currGame.angleIndex]);
		currGame.markedGrid = currGame.gridCopy();
		ArrayList<vector> seen = currGame.drawVectors(400);	
		System.out.println(currGame);
		System.out.println(seen);
	}
}
