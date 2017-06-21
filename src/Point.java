public class Point implements Comparable{
	public int x, y;
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int compareTo(Object o) {
		Point other = (Point) o;
		int i = this.y - other.y;
		if(i != 0)
			return i;
		
		return this.x - other.x;
	}
}
