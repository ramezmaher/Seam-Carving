import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
public class SeamCarver {
	private ShortestPath sp;
	private double [][] energy;
	private Color[][] Picture;
	private int H,W;
	public SeamCarver(Picture picture) {
		if(picture == null)
			throw new IllegalArgumentException();
		this.W = picture.width();
		this.H = picture.height();
		setMatrices(picture);
		sp = new ShortestPath();
	}
	/*private SeamCarver(double[][] e,Picture p) {
		this.energy = e;
		this.H = 5;
		this.W = 6;
		Picture = new Color[6][5];
		for(int i=0 ; i<W ; i++)
			for(int j=0 ; j<H; j++) {
				Picture[i][j] = Color.BLACK;
			}
		sp = new ShortestPath();
	}
	*/
	//current picture
	public Picture picture() {
		Picture P = new Picture(W,H);
		for(int i=0 ; i<W ; i++)
			for(int j=0 ; j<H ; j++)
				P.set(i, j, Picture[i][j]);
		return P;
	}
	// width of current picture
	public int width() {
		return W;
	}
	// height of current picture
	public int height() {
		return H;
	}
	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		if(!check(x,y))
			throw new IllegalArgumentException();
		return energy[x][y];
	} 
	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		if(H == 1) {
			int[] ans = new int[W];
			return ans;
 		}
		return sp.shortestPath(transpose(energy));
	}
	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		if(W == 1) {
			int[] ans = new int[H];
			return ans;
 		}
		return sp.shortestPath(energy);
	}
	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		if (seam == null || seam.length != W || H == 1)
			throw new IllegalArgumentException();
		for(int i=0 ; i<seam.length ; i++) {
			if(!check(i,seam[i]) )
				throw new IllegalArgumentException();
			if(i<seam.length-1 && Math.abs(seam[i+1]-seam[i]) > 1)
				throw new IllegalArgumentException();
			energy[i][seam[i]] = Double.MIN_VALUE;
		}
		H--;
		reconstructHorizontal(W,H);
	}
	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		if (seam == null || seam.length != H || W == 1 )
			throw new IllegalArgumentException();
		for(int i=0 ; i<seam.length ; i++) {
			if(!check(seam[i],i))
				throw new IllegalArgumentException();
			if(i<seam.length-1 && Math.abs(seam[i+1]-seam[i]) > 1)
				throw new IllegalArgumentException("dif more than 1");
			energy[seam[i]][i] = Double.MIN_VALUE;
		}
		W--;
		reconstructVertical(W,H);
	}
	//  unit testing (optional)
	public static void main(String[] args) {
		/*double[][] arr = new double[6][5];
		arr[0][0] = 1000;
		arr[0][1] = 1000;
		arr[0][2] = 1000;
		arr[0][3] = 1000;
		arr[0][4] = 1000;
		arr[1][0] = 1000;
		arr[1][1] = 237.35;
		arr[1][2] = 138.69;
		arr[1][3] = 153.88;
		arr[1][4] = 1000;
		arr[2][0] = 1000;
		arr[2][1] = 151.02;
		arr[2][2] = 288.10;
		arr[2][3] = 174.07;
		arr[2][4] = 1000;
		arr[3][0] = 1000;
		arr[3][1] = 234.09;
		arr[3][2] = 133.01;
		arr[3][3] = 284.01;
		arr[3][4] = 1000;
		arr[4][0] = 1000;
		arr[4][1] = 107.89;
		arr[4][2] = 211.57;
		arr[4][3] = 194.50;
		arr[4][4] = 1000;
		arr[5][0] = 1000;
		arr[5][1] = 1000;
		arr[5][2] = 1000;
		arr[5][3] = 1000;
		arr[5][4] = 1000;
		Picture p = new Picture(6,5);
		SeamCarver s = new SeamCarver(arr,p);
		int[] sp = {1,3,2,5,4};
		s.removeVerticalSeam(sp);*/
	}
	/*private void printE() {
		for(int j=0 ; j<H ; j++) {
			for(int i=0 ; i<W ; i++) {
				System.out.print(energy[i][j]+" ");
			}
			System.out.println(" ");
		}
	}*/
	//checks if coordinates of a pixel is in the picture
	private boolean check(int col,int row) {
		if(col>=0 && col<W && row>=0 && row<H)
			return true;
		return false;
	}
	//create 2D array of pixels energies
	private void setMatrices(Picture p) {
		Picture = new Color[W][H];
		energy = new double[W][H];
		for(int i=0 ; i<W; i++)
			for(int j=0; j<H; j++) {
				energy[i][j] = energyOfpixel(i,j,p);
				Picture[i][j] = p.get(i, j);
			}
	}
	/*private void recalculateEnergy() {
		for(int i=0 ; i<W; i++)
			for(int j=0; j<H; j++) {
				energy[i][j] = energyOfpixel(i,j,);
			}
	}*/
	//calculates the energy of a certain pixel
	private double energyOfpixel(int col,int row,Picture P) {
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
	private void reconstructVertical(int w,int h) {
		double[][] newEnergy = new double[w][h];
		Color[][] newPic = new Color[w][h];
		for(int i=0;i<w;i++) {
			for(int j=0 ; j<h ; j++) {
				if(energy[i][j] == Double.MIN_VALUE) {
					energy[i][j] = energy[i+1][j];
					Picture[i][j] = Picture[i+1][j];
					energy[i+1][j] = Double.MIN_VALUE;
				} 
				newEnergy[i][j] = energy[i][j];
				newPic[i][j] = Picture[i][j];
			}
		}
		this.Picture = newPic;
		this.energy = newEnergy;
	}
	private void reconstructHorizontal(int w,int h) {
		double[][] newEnergy = new double[w][h];
		Color[][] newPic = new Color[w][h];
		for(int j=0;j<h;j++) {
			for(int i=0 ; i<w ; i++) {
				if(energy[i][j] == Double.MIN_VALUE ) {
					energy[i][j] = energy[i][j+1];
					Picture[i][j] = Picture[i][j+1];
					energy[i][j+1] = Double.MIN_VALUE;
				}
				newEnergy[i][j] = energy[i][j];
				newPic[i][j] = Picture[i][j];
			}
		}
		this.Picture = newPic;
		this.energy = newEnergy;
	}
}
