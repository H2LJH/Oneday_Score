package com.biz.score.main;

import java.util.Scanner;

import com.biz.score.service.ScoreInterface;
import com.biz.score.service.ScoreServiceV1;


public class ScoreEx_01 
{
	public static void main(String[] args) 
	{
		ScoreInterface scoreService = new ScoreServiceV1();
		Scanner sc = new Scanner(System.in);
		String str;
		int n = 0;
		
		
		while(true)
		{
			System.out.println("======================================");
			System.out.print("1, 성적 입력\n"  + "2, 성적 출력\n" + "3, 파일 읽어오기\n");
			System.out.println("======================================");
			System.out.print("메뉴 선택(end 입력시 종료) : ");
			str = sc.nextLine();
			
			if(str.equalsIgnoreCase("end"))
				break;
			
			try 
			{ n = Integer.valueOf(str); } 
			catch (Exception e) 
			{
				System.out.println("잘못 입력 하셨습니다.");
				continue;
			}
		
			switch (n) 
			{
			case 1:
				scoreService.load();
				while(!scoreService.insert()) {}
				continue;

			case 2:
				scoreService.printlist();
				break;
				
			case 3:
				if(scoreService.load())
					scoreService.printlist();
				else
					System.out.println("파일이 없습니다.");
			}
			
		}
		
		sc.close();
	}
}
