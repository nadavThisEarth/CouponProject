package com.nadav.phase3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.nadav.phase3.threads.Job;

@SpringBootApplication
public class Phase3Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(Phase3Application.class, args);
            Job job = ctx.getBean(Job.class);
            job.start();
            
            //job.StopRunning();
	}

}
