package com.schwarzsword.pip.coursework;

import com.schwarzsword.pip.coursework.bot.BotBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class CourseworkApplication {
    private static
    ApplicationContext context;

    @Autowired
    public CourseworkApplication(ApplicationContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(new Class[]{CourseworkApplication.class}, args);
        BotBean botBean = (BotBean) context.getBean("botBean");
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                botBean.setResults();
            }
        }, 0, 1, TimeUnit.MINUTES);
    }
}

