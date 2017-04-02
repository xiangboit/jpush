package com.push;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class timermain implements ServletContextListener{
//	  public void run(){     
//	        //执行定时器的任务     
//	        //创建实例     
//	        Timer timer = new Timer();     
//	        timer.schedule(new timers(),0,1*1000);     
//	       }     
//	  public static void main(String[] args) {
	  //可以直接执行main方法启动定时器任务
//		new timermain().run();
//	}
	public void contextDestroyed(ServletContextEvent arg0) {
		time.cancel();    
		
	}
	private Timer time=null;
	public void contextInitialized(ServletContextEvent arg0) {
		time = new Timer();     
		time.schedule(new timers(),0,10*1000);     
	}
	  
}
