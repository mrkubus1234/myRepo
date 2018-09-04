package net.atos.humaj.jakub;

import net.atos.humaj.jakub.Job.JobToDo;

import java.io.*;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Application {

    public static void main(String[] args){

        String propPath;

        if(args.length == 0)
             propPath = "config.properties";
        else
            propPath = args[0];

        Properties prop = readProperties(propPath);
        Long period = 0L;
        JobToDo jobToDo = new JobToDo(prop);
        try {
            period = Long.parseLong(prop.getProperty("time.period"));
        } catch (NumberFormatException e) {
            System.out.println("Bad data type of time.period property");
            System.exit(-1);
        }

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run(){
                jobToDo.doJob();
            }
        };

        timer.schedule(timerTask, 0, period);
    }

    private static Properties readProperties(String propPath){
        Properties prop = new Properties();
        try{
            File file = new File(propPath);
            InputStream inputStream = new FileInputStream(file);
            prop.load(inputStream);
        } catch (Exception e){
            e.printStackTrace();
        }
        return prop;
    }
}