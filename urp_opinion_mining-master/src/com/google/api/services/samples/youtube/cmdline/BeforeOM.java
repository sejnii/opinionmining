package com.google.api.services.samples.youtube.cmdline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;

import scala.collection.Seq;

public class BeforeOM {//명사, 동사, 형용사만 찾아서 verb.txt에 넣는 파일 이거하고 파이썬 파일 실행시키면 된다.

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			File file = new File("C:\\Class_JAVA\\urp_opinion_mining-master\\urp_opinion_mining-master\\out.txt");
			FileReader filereader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(filereader);
			FileWriter fw = new FileWriter(new File("C:\\Class_JAVA\\urp_opinion_mining-master\\urp_opinion_mining-master\\verb.txt"));
			BufferedWriter bw = new BufferedWriter(fw);
			String line ="";
			List ret;
			int n=0;//댓글의 개수 역할
			
			while((line = bufReader.readLine()) != null) {
				Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(line);
				ret = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens);
				
				for(int i=0;i<ret.size();i++) {
					
					StringTokenizer st = new StringTokenizer( ret.get(i).toString(), ",|(|)|:|0|1|2|3|4|5|6|7|8|9|*");
					String word="", pos="";
					for(int j=0;j<2;j++) { // 단어랑 품사만 저장하도록(원형 저장안할랭!)
						String nt = st.nextToken();
						if(j==0) word = nt;
						else {
							pos = nt;
							if(pos.equals("Adjective") | pos.equals("Verb") | pos.equals("Noun")) {
								bw.write(word);
								bw.newLine();
							}
						}
					}

				}
			}
		}
		catch(Exception e) {
			System.out.println(e);
			
		}
	}
}


