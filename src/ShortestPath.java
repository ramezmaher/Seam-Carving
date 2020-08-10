public class ShortestPath {
	//calculates the shortest seam of a given 2D array of energies of a picture using Topological algorithm 
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
		}
		int[] ans = new int[h];
		for(int i= h-1 ; i>=0 ;i--) {
			index = vertexTo[index];
			ans[i] = index % w;
		}
		return ans;
	}
}
