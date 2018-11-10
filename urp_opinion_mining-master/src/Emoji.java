import java.util.Scanner;

import com.vdurmont.emoji.EmojiParser;

public class Emoji {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scan = new Scanner(System.in);
		System.out.println("이모지 입력");
		String input = scan.nextLine(); 
		String result = EmojiParser.parseToAliases(input);
		System.out.println("결과 " );
         System.out.println(result);
        
	}

}
