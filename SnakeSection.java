public class SnakeSection {
	public int x;
	public int y;
	
	public SnakeSection(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean match(SnakeSection s)
	{
		return this.x==s.x && this.y==s.y;
	}
	public boolean matchSelf(SnakeSection s){ // checks the position of the head to the rest of the body
		
		return this.x==s.x && this.y==s.y;
	}
}
