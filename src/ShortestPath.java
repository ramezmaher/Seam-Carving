public class ShortestPath {
	public int[] shortestPath(double[][] arr) {
		int h = arr[0].length;
		int w = arr.length;
		int[] vertexTo = new int[(w*h)+1];
		int index,ind;
		double v;
		double[] distanceTo = new double[(w*h)+1];
		for(int i=0 ; i<h-1 ; i++){ 
			for(int j=0; j<w ; j++) {
				index = i*w + j;
				if(i == 0) 
					distanceTo[index] = arr[j][0];
				if(j==0) {
					for(int k=j ; k<2 ; k++) {
						ind = (i+1)*w + k;
						v = arr[k][i+1];
						if(distanceTo[ind] == 0) {
							distanceTo[ind] = v + distanceTo[index];
							vertexTo[ind] = index;
						}
						else if(distanceTo[ind] > (v + distanceTo[index])) {
							distanceTo[ind] = v + distanceTo[index];
							vertexTo[ind] = index;
						}
					}
				}
				else if(j == w-1) {
					for(int k=j-1 ; k<w ; k++) {
						ind = (i+1)*w + k;
						v = arr[k][i+1];
						if(distanceTo[ind] == 0) {
							distanceTo[ind] = v + distanceTo[index];
							vertexTo[ind] = index;
						}
						else if(distanceTo[ind] > (v + distanceTo[index])) {
							distanceTo[ind] = v + distanceTo[index];
							vertexTo[ind] = index;
						}
					}
				}
				else {
					for(int k=j-1 ; k<j+2 ; k++) {
						ind = (i+1)*w + k;
						v = arr[k][i+1];
						if(distanceTo[ind] == 0) {
							distanceTo[ind] = v + distanceTo[index];
							vertexTo[ind] = index;
						}
						else if(distanceTo[ind] > (v + distanceTo[index])) {
							distanceTo[ind] = v + distanceTo[index];
							vertexTo[ind] = index;
						}
					}
				}
				
			/*	System.out.println("Distances");
				print(distanceTo);
				System.out.println(" ");
				System.out.println("vertex");
				print(vertexTo);
				System.out.println(" ");
				System.out.println("////////////////");*/
			}
		}
		index = h*w;
		distanceTo[index] = Double.MAX_VALUE;
		for(int i=0 ; i<w ; i++) {
			ind = ((h-1)*w)+i;
			if(distanceTo[index] > distanceTo[ind]){
				distanceTo[index] = distanceTo[ind];
				vertexTo[index] = ind; 
			}
		/*	System.out.println("Distances");
			print(distanceTo);
			System.out.println(" ");
			System.out.println("vertex");
			print(vertexTo);
			System.out.println(" ");
			System.out.println("////////////////"); */
		}
		int[] ans = new int[h];
		for(int i= h-1 ; i>=0 ;i--) {
			index = vertexTo[index];
			ans[i] = index % w;
		}
		return ans;
	}
	/*private void print(double[] d) {
		for(int i=0 ; i<d.length ; i++)
			System.out.print(d[i]+" ");
	}
	private void print(int[] d) {
		for(int i=0 ; i<d.length ; i++)
			System.out.print(d[i]+" ");
	}*/
	public static void main (String[] args) {
		/*ShortestPath sp = new ShortestPath();
		double[][] arr = new double[1][8];
		arr[0][1] = 1000;
		arr[0][2] = 1000;
		arr[0][3] = 1000;
		arr[0][4] = 1000;
		arr[0][5] = 1000;
		arr[0][6] = 1000;
		arr[0][7] = 1000;
		int[] ans = sp.shortestPath(arr);
		for(int i=0 ; i<ans.length ; i++)
			System.out.print(ans[i]+"  ");*/
	}
}
