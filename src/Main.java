import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
	private static String parentDirectory = "E:\\Corwin\\Coding\\City Generator\\tiles";
	private static final int EDGE = 150;
	
	public static void main(String[] args) {
		if (args.length < 2){
			System.out.println("Too few arguments. Example use:\njava Main 5 6");
			System.exit(0);
		}
		int width = 0, height = 0;
		try{
			width = Integer.parseInt(args[0]);
			height = Integer.parseInt(args[1]);
		} catch(NumberFormatException e){
			System.out.println("Width or height wsd not integer");
			System.exit(0);
		}
		
		//Create directory
		Path dir = Paths.get(parentDirectory);
		
		//Get tiles
		List<String> corners = fileList(dir.resolve("corners"));
		List<String> edges = fileList(dir.resolve("edges"));
		List<String> centers = fileList(dir.resolve("centers"));
		
		Random rand = null;
		if(args.length > 2) {
			rand = new Random(Long.parseLong(args[3]));
		} else {
			rand = new Random();
		}
				
		Generator gen = new Generator(width, height, corners, edges, centers, EDGE, rand);
		
		ImageGenerator imageGen = new ImageGenerator(dir, gen.getGrid(), EDGE, rand);
	}
	
	//http://www.adam-bien.com/roller/abien/entry/listing_directory_contents_with_jdk
	public static List<String> fileList(Path directory) {
        List<String> fileNames = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
            for (Path path : directoryStream) {
            	String ext = path.toString().split("\\.(?=[^\\.]+$)")[1];
            	if(!ext.equals("gif"))
            		continue;
                fileNames.add(path.toString());
            }
        } catch (IOException ex) {}
        return fileNames;
    }
}
