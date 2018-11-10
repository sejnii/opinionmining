import java.util.StringTokenizer;

public class SplitTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "❤️❤️(Foreign: 17, 4)";
		
		StringTokenizer st = new StringTokenizer(s, ",|(|)|:|0|1|2|3|4|5|6|7|8|9");
		while(st.hasMoreTokens())
			System.out.println(st.nextToken());

	}

}
