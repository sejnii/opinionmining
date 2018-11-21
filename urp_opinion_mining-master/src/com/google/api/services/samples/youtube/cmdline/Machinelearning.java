package com.google.api.services.samples.youtube.cmdline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

import org.python.util.PythonInterpreter;

public class Machinelearning {
	Vector<VAN> vecvan;
	
	public void conntopython() {//파이썬 결과파일을 가지고 VAN 벡터에 (감성어,감성값) 형태로 저장
		vecvan = new Vector<VAN>();
		FileReader fr;
		try {
			fr = new FileReader(new File("conntojava.txt"));
		
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while((line = br.readLine())!=null) {
			VAN temp = new VAN();
			temp.word = line;
			line = br.readLine();
			temp.value = Double.parseDouble(line);
			vecvan.add(temp);
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public int sentiscore(String inword) { // 벡터에 담긴 감성값 리턴
		for(int i=0; i<vecvan.size();i++) {
		if(inword.equals(vecvan.get(i).word)) { // 머신러닝 결과값에 있으면 1또는 -1 없으면 중립으로 보고 0
		//	System.out.println(vecvan.get(i).word+"ml list에 있어!!");
			if(vecvan.get(i).value >0)
				return 1;
			else return -1;
			
		}
		
			}
		return 0; // 리스트에 없으면 

	}
}
