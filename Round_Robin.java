package tay;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class Round_Robin{
	public static void main(String[] args) throws NumberFormatException, IOException {
	
	int[][] process = new int[100][2];
	int[][] process1 = new int[100][2];
	
	int process_id = 0;
	
    BufferedReader reader = new BufferedReader(new FileReader("processes.txt"));
    BufferedWriter writer = new BufferedWriter(new FileWriter("Group11_RR.txt"));
    writer.write("Round Robin \n\n");
    int trt = 0;
    String processLine;
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
	
	int running_time = 0;
	
	int pwsat[] = new int[100]; // processes_with_same_arrival_time
	
	int cnt2 = 0;
	
	int[] turn_aroundtime = new int[trt + 1];
	int[] waiting_time = new int[trt + 1];
	int[] completed_process = new int[trt + 1];
    int idle_time = 0;
	for(int i = 1; i <= trt; i++) {
		
		for(int j = 1; j <= trt; j++) {
			if(running_time >= process[j][0] && process[j][1] != 0) {
				pwsat[cnt2++] = j;
				//System.out.print(j + " ");
			}		
		
		}
		
		if(pwsat[0] == 0) {
			String idle = "time: " + running_time + ":   idle" + "\n";
			writer.write(idle);
			idle_time++;
			running_time++;
			i--;
			continue;
		}
		//System.out.println();
		
		// In the following loop, we compare burst time of processes with same arrival time that we found above
		int last = pwsat[0];
		
		for(int j = 0; j < cnt2; j++) {
			if(j + 1 == cnt2) {
				//System.out.println(cnt2);
				break;
			}
			if(process[last][1] > process[pwsat[j + 1]][1]) {
				last = pwsat[j + 1];
			}
			//System.out.println("last " + last + " process burst time " + process[pwsat[j + 1]][1]);

		}
		
		int elapsed_time = 0;
		while(process[last][1] > 0) {
			elapsed_time++;
			process[last][1]--;
		}
		
		
		running_time +=  elapsed_time * cnt2;
		writer.write("Exit time for process: " + last + " is " + running_time + "\n");
		//System.out.println("elapsed time " + elapsed_time);
		//System.out.println("runtime" + running_time);
		completed_process[last] = 1;
		turn_aroundtime[last] = running_time - process[last][0]; 
	    //System.out.println("tr" + turn_aroundtime[last]);
		waiting_time[last] = turn_aroundtime[last] - process1[last][1];
		for(int j = 1; j <= trt; j++) {
			int g = 0;

			for(int k = 1; k < cnt2; k++) {
				if(j == pwsat[k]) {
					g++;
					break;
				}
			}
			if(g == 0) {
				continue;
			}
			if(completed_process[j] == 1) {
				//System.out.println("j " + j);
				
				continue;
			}
			else {
				process[j][1] = process[j][1] - elapsed_time;
				if(process[j][1] == 0) {
					i++;
					completed_process[j] = 1;
					turn_aroundtime[j] = running_time - process[j][0]; 
					waiting_time[j] = turn_aroundtime[j] - process1[j][1];

				}
				//System.out.println("process id " + j + " " + process[j][1]);
			}
			
			
		}
	
		Arrays.fill(pwsat, 0);
		cnt2 = 0;
	}
	double total_turnaroundtime = 0.0;
	double average_waitingtime = 0.0;
	
	for(int i = 1; i <= trt; i++) {
		total_turnaroundtime += turn_aroundtime[i];
	}
	for(int i = 1; i <= trt; i++) {
		average_waitingtime += waiting_time[i];
	}
	total_turnaroundtime /= trt;
	average_waitingtime /= trt;
    double cpu_utilization = (1.0 - ((idle_time * 1.0) / running_time)) * 100.0;
    String result = "Average waiting time: " + average_waitingtime;
    result += "\n" + "Average turnaround time: " + total_turnaroundtime;
    result += "\n" + "Avarage cpu usage: " + cpu_utilization;
    result += "%%";
    writer.write(result);
    writer.close();
	}
}