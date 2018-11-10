import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;


import scala.collection.Seq;


public class KkmaTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			File file = new File("C:\\Class_JAVA\\urp_opinion_mining-master\\urp_opinion_mining-master\\이니스프리_out0.txt");
			String path = "C:\\Class_JAVA\\urp_opinion_mining-master\\urp_opinion_mining-master\\이니스프리_out0_analyze.txt";
			FileWriter fw = new FileWriter(path);
			FileReader filereader = new FileReader(file);
			BufferedWriter bufWriter = new BufferedWriter(fw);
			BufferedReader bufReader = new BufferedReader(filereader);
			String line ="";
			List ret;
			while((line = bufReader.readLine()) != null) {
				System.out.println(line);
				Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(line);
				ret = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens);
				
				for(int i=0;i<ret.size();i++) {
					System.out.println(ret.get(i));
					StringTokenizer st = new StringTokenizer( ret.get(i).toString(), ",|(|)|:|0|1|2|3|4|5|6|7|8|9");
					for(int j=0;j<2;j++) { // 단어랑 품사만 저장하도록(원형 저장안할랭!)
						String nt = st.nextToken();
						System.out.println(nt);
						bufWriter.write(nt);
						bufWriter.newLine();
					
					}
				}
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}

}
