import java.lang.Math;
import java.util.Arrays;

public class vector implements Comparable<vector>{
	int x;
	int y;

	public vector() {
		this.x = 0;
		this.y = 0;
	}

	public vector(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void add(vector v2) {
		this.x += v2.x;
		this.y += v2.y;
	}

	public void sub(vector v2) {
		this.x -= v2.x;
		this.y -= v2.y;
	}

	public double length() {
		return Math.sqrt(Math.pow((double) this.x, 2) + Math.pow((double) this.y, 2));
	} 

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int compareTo(vector v2) {
		double angle = arcTan(this);
		double angle2 = arcTan(v2);
		int toReturn = (angle > angle2) ? 1:-1;
		return toReturn;
	}

	public static double arcTan(vector v) {
		double currX = (v.x == 0) ? 0.000001:v.x;	
		double angle = Math.atan((double)v.y/currX);
		if (v.x < 0) {
			angle += Math.PI;
		}
		if (angle < 0) {
			angle += 2*Math.PI;
		}
		return angle;
	}

	public static int gcd(int x, int y) {
		if (x == 0 || y == 0) {
			return Math.abs(x + y);
		}		
		return gcd(y, x % y);
	}

	public boolean close(vector v2) {	
		return Math.abs(arcTan(this) - arcTan(v2)) < 0.00001;
	}

	public String toString() {
		return Integer.toString(this.x) + " " + Integer.toString(this.y);
	}

	public static vector[] genArray(int xLow, int xHigh, int yLow, int yHigh) {
		vector[] vecArray = new vector[(yHigh - yLow + 1)*(xHigh - xLow + 1)-1];
                int numVecs = 0;
                for (int i = xLow; i < xHigh + 1; i++) {
                        for (int j = yLow; j < yHigh + 1; j++) {
				if (i != 0 || j != 0) {
					int gcd_num = gcd(i, j);
                                        vecArray[numVecs] = new vector(i/gcd_num, j/gcd_num);
                                        numVecs++;
                                }
                        }
                }
                Arrays.sort(vecArray);
		int vecCounter = numVecs;
		for (int i = 0; i < numVecs-1; i++) {
			vector v1 = vecArray[i];
			vector v2 = vecArray[i+1];
			if (v1 != null && v2 != null && v1.close(v2)) {
				vecArray[i] = null;
				vecCounter--;
				//System.out.println("Removing vector: " + v1 + " "+ Double.toString(arcTan(v1)) + " from v2: " + v2 + " " + Double.toString(arcTan(v2)));
			}
		}
		vector[] toReturn = new vector[vecCounter];
		vecCounter = 0;

		for (int i = 0; i < numVecs; i++) {
			if (vecArray[i] != null) {
				toReturn[vecCounter] = vecArray[i];
				vecCounter++;
			}
		} 

		return toReturn;
	}

	public static void normalize(vector[] vecArray) {
		double maxLength = 0;
		for (int i = 0; i < vecArray.length; i++) {
			double vecLength = vecArray[i].length();
			maxLength = (vecLength > maxLength) ? vecLength:maxLength;
		}	

		for (int i = 0; i < vecArray.length; i++) {
			vector currVec = vecArray[i];
			int xLen = currVec.x;
			int yLen = currVec.y;
			do {
				currVec.x += xLen;
				currVec.y += yLen;
			} while (currVec.length() < maxLength);
		}
		//System.out.println("The max length is: " + maxLength);
	}

	public static void main(String[] args) {
		/*vector[] vecArray = new vector[120];
		int numVecs = 0;
		for (int i = -5; i < 6; i++) {
			for (int j = -5 ; j < 6; j++) {
				if (i != 0 || j != 0) {
					vecArray[numVecs] = new vector(i, j);
					numVecs++;
				}
			}
		}
		
		for (int i = 0; i < numVecs; i++) {
			System.out.println(i + " " + vecArray[i]);
		}
		Arrays.sort(vecArray);

		for (int i = 0; i < numVecs; i++) {
			System.out.println(i + " " + vecArray[i] + " " + arcTan(vecArray[i]) + " " + vecArray[i].length());
		}*/
/*
		for (int i = 0; i < numVecs-1; i++) {
			vector v1 = vecArray[i];
			vector v2 = vecArray[i+1];
			if (v1 != null && v2 != null && v1.close(v2)) {
				vecArray[i] = null;
			}
		}*/
		vector[] vecArray = genArray(-5, 5, -5, 5);

		for (int i = 0; i < vecArray.length; i++) {
			System.out.println(i + " " + vecArray[i] + " " + arcTan(vecArray[i]));
		}

	}	

}
