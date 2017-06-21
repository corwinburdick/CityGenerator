import java.util.List;
import java.util.Random;

public class Generator {
	private Random rand;
	private Tile[][] grid;
	private int width, height;
	private int EDGE;

	public Generator(int width, int height, List<String> corners, List<String> edges, List<String> centers, int edge, Random rand) {
//		this.fileGrid = new String[height+2][width+2];
		this.grid = new Tile[height+2][width+2];
		this.width = width;
		this.height = height;
		this.EDGE = edge;
		this.rand = rand;
		
		placeCorners(corners);
		placeEdges(edges);
		placeCenters(centers);
	}
	
	private void placeCorners(List<String> corners) {
//		fileGrid[0][0] = pickRandom(corners);
//		fileGrid[0][width+1] = pickRandom(corners);
//		fileGrid[height+1][0] = pickRandom(corners);
//		fileGrid[height+1][width+1] = pickRandom(corners);
		grid[0][0] = new Tile(pickRandom(corners),EDGE,EDGE);
		grid[0][width+1] = new Tile(pickRandom(corners),EDGE,EDGE);
		grid[height+1][0] = new Tile(pickRandom(corners),EDGE,EDGE);
		grid[height+1][width+1] = new Tile(pickRandom(corners),EDGE,EDGE);
	}
	
	private void placeEdges(List<String> edges) {
		//topRow
		for(int col = 1; col < width+1; col++) {
//			fileGrid[0][col] = pickRandom(edges);
			grid[0][col] = new Tile(pickRandom(edges),EDGE,2*EDGE);
			grid[0][col].rotate(1);
		}
		
		//bottomRow
		for(int col = 1; col < width+1; col++) {
//			fileGrid[height+1][col] = pickRandom(edges);
			grid[height+1][col] = new Tile(pickRandom(edges),EDGE,2*EDGE);
			grid[height+1][col].rotate(3);
		}
		
		//leftCol
		for(int row = 1; row < height+1; row++) {
//			fileGrid[row][0] = pickRandom(edges);
			grid[row][0] = new Tile(pickRandom(edges),EDGE,2*EDGE);
		}
		
		//rightCol
		for(int row = 1; row < height+1; row++) {
//			fileGrid[row][width+1] = pickRandom(edges);
			grid[row][width+1] = new Tile(pickRandom(edges),EDGE,2*EDGE);
			grid[row][width+1].rotate(2);
		}
	}
	
	private void placeCenters(List<String> centers) {
		for(int row = 1; row < height+1; row++) {
			for(int col = 1; col < width+1; col++) {
//				fileGrid[row][col] = pickRandom(centers);
				grid[row][col] = new Tile(pickRandom(centers),2*EDGE,2*EDGE);
				grid[row][col].rotate(rand.nextInt(4));
			}
		}
	}
	
	//Picks randomly, does not care about duplicates
	private <T> T pickRandom(List<T> files) {
		return files.get(rand.nextInt(files.size()));
	}

	public Tile[][] getGrid() {
		return this.grid;
	}
	
	//Picks randomly but never picks a duplicate unless all tiles are used (and then resets used tile list)
//	private <T> T pickRandomAvoidDupes() {
//		
//	}
}
