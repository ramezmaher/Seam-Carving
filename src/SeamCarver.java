import edu.princeton.cs.algs4.Picture;
public class SeamCarver {
	private Picture P;
	private ShortestPath sp;
	private double [][] energy;
	private double [][] energyPrime;
	public SeamCarver(Picture picture) {
		if(picture == null)
			throw new IllegalArgumentException();
		P = new Picture(picture);
		setEnergies();
		sp = new ShortestPath();
		energyPrime = transpose(energy);
	}
	//current picture
	public Picture picture() {
		return P;
	}
	// width of current picture
	public int width() {
		return P.width();
	}
	// height of current picture
	public int height() {
		return P.height();
	}
	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		if(!check(x,y))
			throw new IllegalArgumentException();
		return energy[x][y];
	} 
	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		return sp.shortestPath(energyPrime);
	}
	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		return sp.shortestPath(energy);
	}
	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		
	}
	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		
	}
	//  unit testing (optional)
	public static void main(String[] args) {
		
	}
	//checks if coordinates of a pixel is in the picture
	private boolean check(int col,int row) {
		if(col>=0 && col<P.width() && row>=0 && row<P.height())
			return true;
		return false;
	}
	//create 2D array of pixels energies
	private void setEnergies() {
		energy = new double[P.width()][P.height()];
		for(int i=0 ; i<P.width(); i++)
			for(int j=0; j<P.height(); j++)
				energy[i][j] = energyOfpixel(i,j);
	}
	//calculates the energy of a certain pixel
	private double energyOfpixel(int col,int row) {
		if(col == 0 || col == P.width()-1 || row == 0 || row == P.height()-1)
			return 1000.0;
		//previous column pixel 
		int rgb1 = P.getRGB(col-1, row);
		int r1 = (rgb1 >> 16) & 0xFF;
		int g1 = (rgb1 >>  8) & 0xFF;
		int b1 = (rgb1 >>  0) & 0xFF;
		//next column pixel
		int rgb2 = P.getRGB(col+1, row);
		int r2 = (rgb2 >> 16) & 0xFF;
		int g2 = (rgb2 >>  8) & 0xFF;
		int b2 = (rgb2 >>  0) & 0xFF;
		//calculation for delta X
		int Rx = r2 - r1;
		int Gx = g2 - g1;
		int Bx = b2 - b1;
		int DeltaX = (Rx*Rx) + (Gx*Gx) + (Bx*Bx);
		//previous row pixel
		rgb1 = P.getRGB(col, row-1);
		r1 = (rgb1 >> 16) & 0xFF;
		g1 = (rgb1 >>  8) & 0xFF;
		b1 = (rgb1 >>  0) & 0xFF;
		//next row pixel
		rgb2 = P.getRGB(col, row+1);
		r2 = (rgb2 >> 16) & 0xFF;
		g2 = (rgb2 >>  8) & 0xFF;
		b2 = (rgb2 >>  0) & 0xFF;
		//calculations for delta y
		Rx = r2 - r1;
		Gx = g2 - g1;
		Bx = b2 - b1;
		int DeltaY = (Rx*Rx) + (Gx*Gx) + (Bx*Bx);
		return Math.sqrt(DeltaX+DeltaY);
	}
	private double[][] transpose(double[][] arr) {
		int width = arr.length;
		int height = arr[0].length;
		double[][] ans = new double[height][width];
		for(int i=0 ; i<width ; i++) {
			for(int j=0 ; j<height ; j++) {
				ans[j][i] = arr[i][j];
			}
		}
		return ans;
	}
}
