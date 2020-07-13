package com.biz.score.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.biz.score.config.Config;
import com.biz.score.domain.ScoreVO;

public class ScoreServiceV1 implements ScoreInterface
{
	
	private List<ScoreVO> scoreList;
	private List<ScoreVO> ListConsole;
	private String filePath;
	private Scanner sc;
	
	public ScoreServiceV1() 
	{
		scoreList = new ArrayList<ScoreVO>();
		ListConsole = new ArrayList<ScoreVO>();
		sc = new Scanner(System.in);
		filePath = Config.filePath;
	}
	
	public void sum()
	{
		for(ScoreVO vo : scoreList)
			vo.setSum(vo.getKor() + vo.getEng() + vo.getMath() + vo. getMusic());
	}
	
	public void avg()
	{
		for(ScoreVO vo : scoreList)
			vo.setAvg((float)(vo.getSum()) / Config.subjects);
	}
	
	@Override
	public void printlist() 
	{
		int sum = 0;
		float avg = 0;
		System.out.println("==========================================");
		System.out.println("성적 일람표");
		System.out.println("==========================================");
		System.out.println("학번\t국어\t영어\t수학\t음악\t총점\t평균");
		System.out.println("------------------------------------------");
		for(ScoreVO vo : ListConsole)
		{
			System.out.print(vo.getNum()   + "\t" + 
							 vo.getKor()   + "\t" +
							 vo.getEng()   + "\t" +
							 vo.getMath()  + "\t" +
							 vo.getMusic() + "\t" +
							 vo.getSum()   + "\t" +
							 vo.getAvg() + "\t\n");
			sum += vo.getSum();
		}
		System.out.println("총점 : " + sum);
		System.out.println("평균 : " + (float)sum / ListConsole.size());
		System.out.println("------------------------------------------");
		System.out.println("==========================================");
	}
	
	@Override
	public boolean insert() 
	{
		sc = new Scanner(System.in);
		ScoreVO vo = new ScoreVO();
		
		String strSc = "";
		int intNum = 0;
	
		for(int i=0; i < Config.subjects; ++i)
		{
			System.out.print(Config.ARR_SUBJECTS[i] +" 입력 : ");
			strSc = sc.nextLine();

			try 
			{ intNum = Integer.valueOf(strSc); }
			
			catch (Exception e) 
			{ 
				System.out.println("숫자만 입력가능");
				return false;
			}
			
			if(Config.ARR_SUBJECTS[i].equals("학번"))
			{
				strSc = String.format("%05d",intNum);
				
				for(ScoreVO j : scoreList)
				{
					if(j.getNum().equals(strSc))
					{
						System.out.println("학번이 등록되어 있습니다.");
						return false;
					}
				}
				vo.setNum(strSc);
			}
			
			if(intNum > 100 || intNum < 0)
			{
				System.out.println("점수는 0 - 100점 까지만 입력 가능");
				return false;
			}

			else if(Config.ARR_SUBJECTS[i].equals("국어"))
				vo.setKor(intNum);
					
			else if(Config.ARR_SUBJECTS[i].equals("영어"))
				vo.setEng(intNum);
					
			else if(Config.ARR_SUBJECTS[i].equals("수학"))
				vo.setMath(intNum);
			
			else if(Config.ARR_SUBJECTS[i].equals("음악"))
				vo.setMusic(intNum);
		}
		ListConsole = scoreList;
		
		scoreList.clear();
		
		ListConsole.add(vo);
		
		sum();
		avg();
		path("저장");
		save();
		return true;
	}
	
	@Override
	public void save() 
	{		
		BufferedWriter fw = null;
		String txt = "";
		
		for(ScoreVO one : scoreList)
		{
			txt += one.getNum()   + ":" +
				   one.getKor()   + ":" + 
				   one.getEng()   + ":" + 
				   one.getMath()  + ":" +
				   one.getMusic() + ":" +
				   one.getSum()   + ":" +
				   one.getAvg()   + "\n"; 	  
		}

		try 
		{
			fw = new BufferedWriter(new FileWriter(filePath, true));
            fw.write(txt);
            fw.flush();
            fw.close();
		} 
		
		catch (Exception e) 
		{ System.out.println(filePath + " 파일을 만들수 없음!"); }
		
	}

	@Override
	public boolean load() 
	{	
		FileReader fileReader = null;
		BufferedReader buffer = null;
		
		path("불러오기");
		
		try 
		{
			fileReader = new FileReader(filePath);
			buffer = new BufferedReader(fileReader);
			String lineTemp = "";
			while(true) 
			{
				lineTemp = buffer.readLine();
				
				if(lineTemp == null) 
					break;
				
				String[] scores = lineTemp.split(":");
				ScoreVO vo = new ScoreVO();
				vo.setNum(scores[0]);
				vo.setKor(Integer.valueOf(scores[1]));
				vo.setEng(Integer.valueOf(scores[2]));
				vo.setMath(Integer.valueOf(scores[3]));
				vo.setMusic(Integer.valueOf(scores[4]));
				vo.setSum(Integer.valueOf(scores[5]));
				vo.setAvg(Float.valueOf(scores[6]));
				this.scoreList.add(vo);
			}
			
			buffer.close();
			fileReader.close();
		}
		
		catch (Exception e) 
		{ 
			//System.out.println("파일 로드 할수 없음");
			return false;
			
		}
		
		
		filePath = Config.filePath;
		return true;
	}

	public void path(String path)
	{
		String n = "";
		if(path.equals("불러오기"))
		{
			n = "";
			System.out.print("불러오고 싶은 이름을 지정해 주세요(공백 입력시 Score.txt) : ");
			n = sc.nextLine();
			
			if(n.equals(""))
				filePath = Config.filePath;
			
			else
				filePath = "src/com/biz/score/config/" + n + ".txt";
		}
		
		else if(path.equals("저장"))
		{
			System.out.print("저장하고 싶은 이름을 지정해 주세요(공백 입력시 Score.txt) : ");
			n = sc.nextLine();
			
			if(n.equals(""))
				filePath = Config.filePath;
			
			else
			 filePath = "src/com/biz/score/config/" + n +".txt";
			
		}
	}
	
}

