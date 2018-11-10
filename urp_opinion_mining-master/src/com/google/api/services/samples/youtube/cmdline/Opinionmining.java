package com.google.api.services.samples.youtube.cmdline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;

import scala.collection.Seq;

public class Opinionmining {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double SLpositives=0, SLnegatives=0, LikertScale;
		try {
		File file = new File("C:\\Class_JAVA\\urp_opinion_mining-master\\urp_opinion_mining-master\\이니스프리_out0.txt");
		FileReader filereader = new FileReader(file);
		BufferedReader bufReader = new BufferedReader(filereader);
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
				
				StringTokenizer st = new StringTokenizer( ret.get(i).toString(), ",|(|)|:|0|1|2|3|4|5|6|7|8|9");
				String word="", pos="";
				for(int j=0;j<2;j++) { // 단어랑 품사만 저장하도록(원형 저장안할랭!)
					String nt = st.nextToken();
					if(j==0) word = nt;
					else pos = nt;
				}
			
				words[i] = new Words(word, pos);
				
				
			}
			
			x.addElement(words);
			
		}
		System.out.println("댓글 개수는 " + x.size());
		Machinelearning ml = new Machinelearning();
		Vector<Nounsenti> nsvec = new Vector<Nounsenti>();
		int total = 0;
		for(int k=0;k<x.size();k++) { //총 문장의 개수만큼
		for(int i=0;i<x.elementAt(k).length;i++) {//문장의 단어 개수만큼
	
			if(x.get(k)[i].category.equals("Noun")) {
				double noun_senti=0; // 명사의 감성점수
				int senti_score=0; // 용언의 감성점수
				double emo_senti;
				for(int j=-3;j<=3;j++) {
					if(i+j >= 0 && i+j < x.get(k).length && j!=0)
					if(x.get(k)[i+j].category.equals("Adjective") ||x.get(k)[i+j].category.equals("Verb")||x.get(k)[i+j].category.equals("Noun"))
						senti_score += ml.sentiscore(x.get(k)[i+j].word);
				}
				if(senti_score != 0) {
					noun_senti = (double) senti_score;
					for(int j=-3;j<=3;j++) {
						if(i+j >= 0 && i+j < x.get(k).length) {
						
							if(x.get(k)[i+j].category.equals( "Adverb")){ 
								noun_senti *= advscore(x.get(k)[i+j].word);
							}
							else if(x.get(k)[i+j].category.equals("foreign")){
							emo_senti = emoscore(x.get(k)[i+j].word);
							if(senti_score > 0 && emo_senti > 0)//긍정
								noun_senti *= 1 + emo_senti;
							else if(senti_score > 0 && emo_senti < 0)//긍정
								noun_senti *= 1 - emo_senti;
							else if(senti_score < 0 && emo_senti >0)//부정
								noun_senti *= -1 - emo_senti;
							else if(senti_score < 0 && emo_senti <0)//부정
								noun_senti *= -1 + emo_senti;
							break;
						}
						}
					}
					}
					Nounsenti ns = new Nounsenti(x.get(k)[i].word, noun_senti);
					System.out.println(x.get(k)[i].word +"의 점수는 : "+ noun_senti);
					System.out.println("맞는 것 같나욤?");
					Scanner scan = new Scanner(System.in);
					int accuracy = scan.nextInt();
					if(accuracy >0)
						total++;
					
					nsvec.add(ns); // 명사, 명사에 대한 감성점수 명사벡터에 추가	
					if(noun_senti>0)
						SLpositives += noun_senti;
					else
						SLnegatives += noun_senti;
				
				}
					}
			}
		
		
		LikertScale = (SLpositives * 5) / (SLpositives + SLnegatives);
		System.out.println("리커트 점수 : " + LikertScale);
		System.out.println("정확도는 : " + total / nsvec.size()); 
		
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
		public static double advscore(String s) {
			double ret = 0;
			if(s == "너무")
				ret = 2.0;
			else if(s=="정말")
				ret =  2.0;
			else if (s=="진짜")
				ret = 2.0;
			else if(s=="조금")
				ret =  0.5;
			else if(s=="약간")
				ret =  0.5;
			else if(s=="안")
				ret =  -1.0;
			else 
			{
				System.out.println(s + "의 점수눈?");
				Scanner scan = new Scanner(System.in);
				ret = scan.nextDouble();
				
			}
			return ret;
		}
		public static double emoscore(String s) {
			System.out.println(s + "의 확률은?");
			Scanner scan = new Scanner(System.in);
			return scan.nextDouble();
		}
	

	}
	
	



