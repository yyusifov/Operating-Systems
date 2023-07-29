package tay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FirstComeFirstServed  {
  public static void main(String[] args) throws NumberFormatException, IOException {
    int process_id = 0;
    int[][] process = new int[100][2];
    int[][] process1 = new int[100][2];
    BufferedReader reader = new BufferedReader(new FileReader("processes.txt"));
    BufferedWriter writer = new BufferedWriter(new FileWriter("Group11_FCRS.txt"));
    writer.write("First Come First Served \n\n");
    String processLine;
    int trt = 0;
    while ((processLine = reader.readLine()) != null) {
    	int cnt = 0;
    	trt++;
        String[] processProperties = processLine.split(" ");
        for (String number : processProperties) {
            int value = Integer.parseInt(number);
            if(cnt == 0) {
            	process_id = value;
            }
            else if(cnt == 1) {

            	int arr_time = value;
                process[process_id][0] = arr_time;
                process1[process_id][0] = arr_time;

            }
            else {

            	int run_time = value;
                process[process_id][1] = run_time;
                process1[process_id][1] = run_time;
            }
            cnt++;
        }
    }
    reader.close();
    int order_of_arrival_time[] = new int[trt + 1]; 
    for(int i = 1; i <= trt; i++) {
    	order_of_arrival_time[i] = process[i][0];
    }
    Arrays.sort(order_of_arrival_time);
    int order_of_processes[] = new int[trt + 1]; 
    for(int i = 1; i <= trt; i++) {
    	for(int j = 1; j <= trt; j++) {
    		if(process1[j][0] == order_of_arrival_time[i]) {
    			process1[j][0] = -5;
    			order_of_processes[i] = j;
    			break;
    		}
    	}
    }
    
    int idle_time = 0;
    int[] turnaroundtime = new int[trt + 1];
    int running_time = 0;
    int[] waiting_time = new int[trt + 1];
    //System.out.println("process_id " + process_id);
    for(int i = 1; i <= trt; i++) {
    	if(running_time >= process[order_of_processes[i]][0]) {
    	while(process1[order_of_processes[i]][1] > 0) {
    		String result = "time: " + running_time++ + ":      running process " + order_of_processes[i] + "\n";
    		writer.write(result);
    		process1[order_of_processes[i]][1]--;
    	}
    	turnaroundtime[order_of_processes[i]] = running_time - process[order_of_processes[i]][0];
    	writer.write("Turnaroundtime for process for " + order_of_processes[i] + ": " + turnaroundtime[order_of_processes[i]] + "\n");
    	waiting_time[order_of_processes[i]] = turnaroundtime[order_of_processes[i]] - process[order_of_processes[i]][1];
    	writer.write("Waiting time for process for " + order_of_processes[i] + ": " + waiting_time[order_of_processes[i]] + "\n");
    	}
    	else {
    		idle_time++;
    		String idle_process = "time: " + running_time++ + ":  idle" + "\n"; 
    		writer.write(idle_process);
    		i--;
    	}
    	
    }

    double total_turnaroundtime = 0.0;
    for(int i = 1; i <= trt; i++) {
    	total_turnaroundtime += turnaroundtime[i];
    }
    
    total_turnaroundtime /= trt;
        
    double total_waiting_time = 0.0;
    
    for(int i = 1; i <= trt; i++) {
    	total_waiting_time += waiting_time[i];
    }
    
    total_waiting_time /= trt;
    double cpu_utilization = (1.0 - ((idle_time * 1.0) / running_time)) * 100.0;
    String result = "Average waiting time: " + total_waiting_time;
    result += "\n" + "Average turnaround time: " + total_turnaroundtime;
    result += "\n" + "Avarage cpu usage: " + cpu_utilization;
    result += "%%";
    writer.write(result);
    writer.close();
  }
  }
