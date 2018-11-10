package com.google.api.services.samples.youtube.cmdline;

import java.util.Scanner;

public class Machinelearning {

	public int sentiscore(String word) {
		System.out.println(word + "의 감성점수 입력 : ");
		Scanner scan = new Scanner(System.in);
		int ret = scan.nextInt();
		return ret;
	}
}
