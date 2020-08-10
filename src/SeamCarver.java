import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.awt.Color;
public class SeamCarver {
	private ShortestPath sp;
	private double [][] energy;
	private Picture Picture;
	private int H,W;
	public SeamCarver(Picture picture) {
		if(picture == null)
			throw new IllegalArgumentException();
		this.W = picture.width();
		this.H = picture.height();
		Picture = new Picture(picture);
		setEnergy(picture);
		sp = new ShortestPath();
	}
	//current picture
	public Picture picture() {
		Picture p = new Picture(Picture);
		return p;
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
	       if (args.length != 3) {
	            StdOut.println("Usage:\njava ResizeDemo [image filename] [num cols to remove] [num rows to remove]");
	            return;
	        }

	        Picture inputImg = new Picture(args[0]);
	        int removeColumns = Integer.parseInt(args[1]);
	        int removeRows = Integer.parseInt(args[2]); 

	        StdOut.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
	        SeamCarver sc = new SeamCarver(inputImg);

	        Stopwatch sw = new Stopwatch();

	        for (int i = 0; i < removeRows; i++) {
	            int[] horizontalSeam = sc.findHorizontalSeam();
	            sc.removeHorizontalSeam(horizontalSeam);
	        }

	        for (int i = 0; i < removeColumns; i++) {
	            int[] verticalSeam = sc.findVerticalSeam();
	            sc.removeVerticalSeam(verticalSeam);
	        }
	        Picture outputImg = sc.picture();

	        StdOut.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());

	        StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
	        inputImg.show();
	        outputImg.show();
	}
	//checks if coordinates of a pixel is in the picture
	private boolean check(int col,int row) {
		if(col>=0 && col<W && row>=0 && row<H)
			return true;
		return false;
	}
	//create 2D array of pixels energies
	private void setEnergy(Picture p) {
		energy = new double[W][H];
		for(int i=0 ; i<W; i++)
			for(int j=0; j<H; j++) {
				energy[i][j] = energyOfpixel(i,j,p);
			}
	}
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
	// transposes energy matrix
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
	//reconstruct the seam after vertical seam removal
	private void reconstructVertical(int w,int h) {
		double[][] newEnergy = new double[w][h];
		Color[][] newPic = new Color[w][h];
		Color[][] P = pic2mat(Picture);
		for(int i=0;i<w;i++) {
			for(int j=0 ; j<h ; j++) {
				if(energy[i][j] == Double.MIN_VALUE) {
					energy[i][j] = energy[i+1][j];
					P[i][j] = P[i+1][j];
					energy[i+1][j] = Double.MIN_VALUE;
				} 
				newEnergy[i][j] = energy[i][j];
				newPic[i][j] = P[i][j];
			}
		}
		mat2pic(newPic);
		setEnergy(Picture);
	}
	//reconstructs the picture after horizontal seam removal
	private void reconstructHorizontal(int w,int h) {
		double[][] newEnergy = new double[w][h];
		Color[][] newPic = new Color[w][h];
		Color[][] P = pic2mat(Picture);
		for(int j=0;j<h;j++) {
			for(int i=0 ; i<w ; i++) {
				if(energy[i][j] == Double.MIN_VALUE ) {
					energy[i][j] = energy[i][j+1];
					P[i][j] = P[i][j+1];
					energy[i][j+1] = Double.MIN_VALUE;
				}
				newEnergy[i][j] = energy[i][j];
				newPic[i][j] = P[i][j];
			}
		}
		mat2pic(newPic);
		setEnergy(Picture);
	}
	//return color matrix representing each pixel
	private Color[][] pic2mat (Picture p){
		Color[][] dummy = new Color[p.width()][p.height()];
		for(int i=0 ; i<p.width() ; i++)
			for(int j=0 ;j<p.height() ;j++)
				dummy[i][j] = p.get(i, j);
		return dummy;
	}
	//convert Color matrix to picture
	private void mat2pic(Color[][] arr) {
		Picture p = new Picture(arr.length,arr[0].length);
		for(int i=0 ; i<arr.length ; i++)
			for(int j=0 ;j<arr[0].length ;j++)
				p.set(i, j, arr[i][j]);
		this.Picture = p;
	}
 }
