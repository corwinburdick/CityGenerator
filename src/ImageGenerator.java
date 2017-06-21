import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImageGenerator {
	private int EDGE;
	private int buildingIndex;
	
	private int[][] buildingIndices;
	private Tile[][] grid;
	private Graphics g;
	private FontMetrics fm;
	
	public ImageGenerator(Path dir, Tile[][] grid, int edge, Random rand) {
		this.grid = grid;
		int height = grid.length;
		int width = grid[0].length;
		this.EDGE = edge;
		
		int pixel_width = (width-1) * 2 * EDGE;
		int pixel_height = (height-1) * 2 * EDGE;
		
		buildingIndex = 1; //Start building numbers at 1
		buildingIndices = new int[height][width];
		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				buildingIndices[row][col] = buildingIndex;
				buildingIndex += grid[row][col].buildings.size();
			}
		}
		
		BufferedImage result = new BufferedImage(pixel_width, pixel_height, BufferedImage.TYPE_INT_RGB);
		g = result.getGraphics();
		fm = g.getFontMetrics();
				
		//x coordinate corresponds to column, y to row, and (0,0) is top left
		try{
			//corners
			drawTile(0, 0, 0, 0);
			drawTile(0, width-1, EDGE + 2*EDGE*(width-2), 0);
			drawTile(height-1, width-1, EDGE + 2*EDGE*(width-2), EDGE + 2*EDGE*(height-2));
			drawTile(height-1, 0, 0, EDGE + 2*EDGE*(height-2));
			
			//top & bottom rows
			for(int col = 1; col < width-1; col++) {
				drawTile(0, col, -1*EDGE + 2*EDGE*col, 0);
				drawTile(height-1, col, -1*EDGE + 2*EDGE*col, EDGE + 2*EDGE*(height-2));
			}
			//left & right cols
			for(int row = 1; row < height-1; row++) {
				drawTile(row, 0, 0, -1*EDGE + 2*EDGE*row);
				drawTile(row, width-1, EDGE + 2*EDGE*(width-2), -1*EDGE + 2*EDGE*row);
			}
			
			//centers
			for(int row = 1; row < height-1; row++) {
				for(int col = 1; col < width-1; col++) {
					drawTile(row, col, -1*EDGE + 2*EDGE*col, -1*EDGE + 2*EDGE*row);
				}
			}
		
			String fileName = new SimpleDateFormat("yyyyMMddHHmmss'result.png'").format(new Date());
			ImageIO.write(result,"png",new File(fileName));
		} catch (IOException e) {
			System.out.println("Failed to write output image to file.");
			e.printStackTrace();
		}
	}
	
	private BufferedImage importImage(Tile t) throws IOException {
		File f = new File(t.filename);
		return rotate(ImageIO.read(f), Math.PI/2 * t.timesRotated);
	}
	
	private void drawTile(int row, int col, int x, int y) throws IOException {
		Tile t = grid[row][col];
		BufferedImage image = importImage(t);
		g.drawImage(image, x, y, null);
		
		//draw building numbers
		int index = buildingIndices[row][col];
		for(Point p : t.buildings) {
			String str = String.valueOf(index);
			g.setColor(Color.WHITE);
			Rectangle2D rect = fm.getStringBounds(str, g);
            g.fillRect(p.x + x - (int) rect.getWidth()/2,
                       p.y + y - fm.getAscent() - (int) rect.getHeight()/2,
                       (int) rect.getWidth(),
                       (int) rect.getHeight());
			g.setColor(Color.RED);
			g.drawString(str, p.x + x - (int) rect.getWidth()/2, p.y + y - (int) rect.getHeight()/2);
			index++;
		}
	}
	
	//https://stackoverflow.com/a/4156760/4493583
	public static BufferedImage rotate(BufferedImage image, double angle) {
	    double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
	    int w = image.getWidth(), h = image.getHeight();
	    int neww = (int)Math.floor(w*cos+h*sin), newh = (int) Math.floor(h * cos + w * sin);
	    GraphicsConfiguration gc = getDefaultConfiguration();
	    BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
	    Graphics2D g = result.createGraphics();
	    g.translate((neww - w) / 2, (newh - h) / 2);
	    g.rotate(angle, w / 2, h / 2);
	    g.drawRenderedImage(image, null);
	    g.dispose();
	    return result;
	}
	private static GraphicsConfiguration getDefaultConfiguration() {
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice gd = ge.getDefaultScreenDevice();
	    return gd.getDefaultConfiguration();
	}
}
