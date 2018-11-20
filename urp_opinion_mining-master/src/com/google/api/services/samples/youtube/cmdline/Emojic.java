package com.google.api.services.samples.youtube.cmdline;
import java.util.Scanner;
import java.sql.*;
import java.util.*;

import com.vdurmont.emoji.EmojiParser;

public class Emojic { // 이모지 들어오면 db(이모지 리스트)랑 연결해서 감성확률 찾아내는 프로그램

	  private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	   private final String DB_URL = "jdbc:mysql://localhost/urp?allowPublicKeyRetrieval=true&useSSL=false";
	      //schema 이름
	  
	   private final String USER_NAME = "root";
	   private final String PASSWORD = "admin";

	   
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		Scanner scan = new Scanner(System.in);
		System.out.println("이모지 입력");
		String input = scan.nextLine(); 
		String str = EmojiParser.parseToAliases(input);
		Emojic sv = new Emojic();
		   float value = sv.FindValue(str);
		      System.out.println("출력:" + value);
		      scan.close();

        
	}
	
	
	
	  public float FindValue (String s){ 
	       Connection conn = null; 
	      Statement state = null;
	      float value = 0.0f;
	       try{
	            Class.forName(JDBC_DRIVER);	
	            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
	            state = conn.createStatement();
	            
	            String sql;
	            sql = "select value from emoticonsenti where name = '" + s + "';";
	            ResultSet rs = state.executeQuery(sql);
	            while (rs.next()) {
	               value = rs.getFloat("value");
	            }

	            rs.close();
	            state.close();
	            conn.close();
	       }
	       catch(Exception e) {
	          e.printStackTrace();
	       }
	       finally {
	          try {
	             if (state != null)
	                state.close();
	          } catch (SQLException ex1) {
	             //
	          }
	          
	          try {
	             if (conn != null)
	                conn.close();
	          } catch (SQLException ex1) {
	             //
	          }
	       }
	      return value;
	   
	   }


}
