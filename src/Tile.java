import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Tile {
	public String filename;
	public List<Point> buildings;
	public int timesRotated;
	private int height, width;
	
	public Tile(String filename, int width, int height) {
		this.filename = filename;
		this.width = width;
		this.height = height;
		
		//get buildings
		this.buildings = new ArrayList<Point>();
		String buildingsFilename = filename.split("\\.(?=[^\\.]+$)")[0] + ".txt";
		try {
			Scanner sc = new Scanner(new File(buildingsFilename));
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				if(line.isEmpty()) {
					continue;
				}
				String[] point = line.split(",");
				buildings.add(new Point(Integer.parseInt(point[0]), Integer.parseInt(point[1])));
			}
			System.out.println("Found buildings for " + filename);
			sc.close();
		} catch (FileNotFoundException e) {
//			System.out.println("Did not find buildings for " + filename);
		}
		
		//sort buildings by Y value
		Collections.sort(buildings);
	}
	
	//number of times to rotate clockwise
	public void rotate(int rotations) {
		//does not rotate image, only buildings
		rotations = rotations % 4;
		if(rotations == 0) {
			return;
		}
		timesRotated = (timesRotated + rotations) % 4;
		
		List<Point> buildingsNew = new ArrayList<Point>();
		for(Point p : buildings) {
			if(rotations == 1) {
				buildingsNew.add(new Point(height-p.y,p.x));
			} else if(rotations == 2) {
				buildingsNew.add(new Point(width-p.x,height-p.y));
			} else if(rotations == 3) {
				buildingsNew.add(new Point(p.y,width-p.x));
			}
		}
		buildings = buildingsNew;
		
		Collections.sort(buildings);
	}
}
