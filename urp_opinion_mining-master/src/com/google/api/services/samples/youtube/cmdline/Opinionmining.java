package com.google.api.services.samples.youtube.cmdline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;

import com.vdurmont.emoji.EmojiParser;

import scala.collection.Seq;

public class Opinionmining {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double SLpositives=0, SLnegatives=0, LikertScale;
		try {
		File file = new File("C:\\Class_JAVA\\urp_opinion_mining-master\\urp_opinion_mining-master\\out.txt");
		FileReader filereader = new FileReader(file);
		BufferedReader bufReader = new BufferedReader(filereader);
		FileWriter fw = new FileWriter(new File("C:\\Class_JAVA\\urp_opinion_mining-master\\urp_opinion_mining-master\\nounsenti.txt"));
		BufferedWriter bw = new BufferedWriter(fw);
		FileWriter fw2 = new FileWriter(new File("C:\\Class_JAVA\\urp_opinion_mining-master\\urp_opinion_mining-master\\wordclass.txt"));
		BufferedWriter bw2 = new BufferedWriter(fw2);
		String line ="";
		List ret;
		int n=0;//댓글의 개수 역할
		Vector<Words[]> x = new Vector<Words[]>();
		Words[] words = null;
		while((line = bufReader.readLine()) != null) {
			Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(line);
			ret = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens);
			words = new Words[ret.size()];
			for(int i=0;i<ret.size();i++) {
				
				StringTokenizer st = new StringTokenizer( ret.get(i).toString(), ",|(|)|:|0|1|2|3|4|5|6|7|8|9|*");
				String word="", pos="";
				for(int j=0;j<2;j++) { // 단어랑 품사만 저장하도록(원형 저장안할랭!)
					String nt = st.nextToken();
					if(j==0) {word = nt;
					bw2.write(nt);
					bw2.newLine();
					}
					
					else {
						pos = nt;
						bw2.write(pos);
						bw2.newLine();
						
					}
				}
			
				words[i] = new Words(word, pos);
				
				
			}
			
			x.addElement(words);
			
			
		}
		bw2.close();
		
		System.out.println("댓글 개수는 " + x.size());
		Machinelearning ml = new Machinelearning();
		ml.conntopython();
		Emojic ec = new Emojic();
		Vector<Nounsenti> nsvec = new Vector<Nounsenti>();
		int total = 0; // 맞은 개수
		int num = 0; // 명사 몇 갠지
		int emo_total = 0;//이모티콘일 때 맞은 개수
		int emo_num = 0; //이모티콘일 때 개수
		int sentiword =0 ; //몇번째 용언인지
		for(int k=0;k<x.size();k++) { //총 문장의 개수만큼
		for(int i=0;i<x.elementAt(k).length;i++) {//문장의 단어 개수만큼
	
			if(x.get(k)[i].category.equals("Noun")) {
				num++;
				int emo = 0; // 이모티콘 사용 되었을 때만 측정하기
				double noun_senti=0; // 명사의 감성점수
				int senti_score=0; // 용언의 감성점수
				double emo_senti;
				for(int j=-3;j<=3;j++) {
					if(i+j >= 0 && i+j < x.get(k).length && j!=0)
					if(x.get(k)[i+j].category.equals("Adjective") ||x.get(k)[i+j].category.equals("Verb")||x.get(k)[i+j].category.equals("Noun")) {
						int van_senti = ml.sentiscore(x.get(k)[i+j].word);
					
						senti_score += van_senti;
						
						
						}
				}
				if(senti_score != 0) {
					noun_senti = (double) senti_score;
					for(int j=-3;j<=3;j++) {
						if(i+j >= 0 && i+j < x.get(k).length) {
						
							if(x.get(k)[i+j].category.equals( "Adverb")){ 
								noun_senti *= advscore(x.get(k)[i+j].word);
							}
							else if(x.get(k)[i+j].category.equals("Foreign")){
							String result = EmojiParser.parseToAliases(x.get(k)[i+j].word);
							  emo_senti = ec.FindValue(result);
							  emo = 1;
							if(senti_score > 0 && emo_senti > 0)//긍정
								noun_senti *= 1 + emo_senti;
							else if(senti_score > 0 && emo_senti < 0)//긍정
								noun_senti *= 1 - emo_senti;
							else if(senti_score < 0 && emo_senti >0)//부정
								noun_senti *= -1 - emo_senti;
							else if(senti_score < 0 && emo_senti <0)//부정
								noun_senti *= -1 + emo_senti;
							
							/* 한국어 구문 규칙 적용하지 않았을 때
							 * if(emo_senti>0)
							 * noun_senti *= 1 + emo_senti;
							 * 
							 * else
							 * noun_senti *= -1 - emo_senti;
							 * 
							 * 
							 */
						}
						}
					}
					}
					Nounsenti ns = new Nounsenti(x.get(k)[i].word, noun_senti);
					
					System.out.println(x.get(k)[i].word +"의 점수는 : "+ noun_senti);
					/*/////////////정확도 측정부분//////////////////////
					System.out.println("맞는 것 같나욤?");
					Scanner scan = new Scanner(System.in);
					int accuracy = scan.nextInt();
					if(accuracy >0)
						total++;
					//////////////////////////////////////////////*/
					
					//////////////////////이모티콘만 정확도 측정하기 위에꺼 주석 처리 하세요/////////////////
					if(emo == 1)
					{
						emo_num++;
						/*System.out.println("이모티콘만 검사 : 맞는 것 같나욤?");
						Scanner scan = new Scanner(System.in);
						int emo_accuracy = scan.nextInt();
						if(emo_accuracy > 0)
							emo_total++;*/
					}
					//////////////////////////////////////////////////////////
					
					nsvec.add(ns); // 명사, 명사에 대한 감성점수 명사벡터에 추가	
					if(noun_senti>0)
						SLpositives += noun_senti;
					else
						SLnegatives += noun_senti;
					bw.write(x.get(k)[i].word);
					bw.newLine();
					bw.write(Double.toString(noun_senti));
					bw.newLine();
				
					
				}
					}
			}
		
		
		LikertScale = (SLpositives * 5) / (SLpositives + SLnegatives);
		System.out.println("SLPOSITIVES 는 : " + SLpositives);
		System.out.println("SLnegatives 는 : " + SLnegatives);
		System.out.println("리커트 점수 : " + LikertScale);
		System.out.println("정확도는 : " + total / num); 
		System.out.println("명사 개수 : " + num);
		System.out.println("맞은 명사 개수 : " + total);
		
		//////////////이모티콘 정확도 측정할 때//////////////////
		System.out.println("이모티콘 들어간 명사 수는 : " + emo_num);
		System.out.println("이모티콘 들어갔을 때 맞는 명사 수는 : " + emo_total);
		/////////////////////////////////////////////////////////////////
		bufReader.close();
		bw.close();
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
		public static double advscore(String s) { // 부사 미리 저장해놧음, 없으면 입력받아
			double ret = 0;
			if(s.equals("너무"))
				ret = 2.0;
			else if(s.equals("정말"))
				ret =  2.0;
			else if (s.equals("진짜"))
				ret = 2.0;
			else if (s.equals("엄청"))
				ret = 2.0;
			else if (s.equals("많이"))
				ret = 2.0;
			else if (s.equals("특히"))
				ret = 2.0;
			else if (s.equals("너무나"))
				ret = 2.0;
			else if (s.equals("딱"))
				ret = 2.0;
			else if (s.equals("되게"))
				ret = 2.0;
			else if (s.equals("다"))
				ret = 2.0;
			else if (s.equals("빨리"))
				ret = 2.0;
			else if(s.equals("이렇게"))
				ret =  1.0;
			else if(s.equals("조금"))
				ret =  0.5;
			else if(s.equals("약간"))
				ret =  0.5;
			else if(s.equals("안"))
				ret =  -1.0;
			else if(s.equals("앙"))
				ret =  -1.0;
			else if(s.equals("없이"))
				ret =  -1.0;
			else if(s.equals("흑"))
				ret =  -1.0;
			
			return ret;
		}
		public static double emoscore(String s) {//안써
			System.out.println(s + "의 확률은?");
			Scanner scan = new Scanner(System.in);
			return scan.nextDouble();
		}
	

	}
	
	



