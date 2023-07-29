package tay;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class Shortest_Remaining_Time{
	public static void main(String[] args) throws NumberFormatException, IOException {
	int[][] process = new int[100][2];
	int[][] process1 = new int[100][2];
	
	int process_id = 0;
	int trt = 0;
    BufferedReader reader = new BufferedReader(new FileReader("processes.txt"));
    BufferedWriter writer = new BufferedWriter(new FileWriter("Group11_SRTF.txt"));
    writer.write("Shortest Remaining Time \n\n");
    String processLine;
    while ((processLine = reader.readLine()) != null) {
    	trt++;
    	int cnt = 0;
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
	int idle_time = 0;
	int running_time = 0; 
	int pwsat[] = new int[100]; 

	int cnt = 0;
	int[] turn_aroundtime = new int[trt];
	int[] waiting_time = new int[trt];
	int cnt2 = 0;
	for(int i = 1; i <= trt; i++) {
		for(int j = 1; j <= trt; j++) {
			if(running_time >= process[j][0] && process[j][1] != 0) {
				pwsat[cnt++] = j;
			}		
		
		}
		if(pwsat[0] == 0) {
			idle_time++;
			String idle_process = "time: " + running_time++ + ":     idle" + "\n";
			writer.write(idle_process);
			i--;
			continue;
		}
		int process_id_with_smallest_burst_time = pwsat[0];

		for(int j = 0; j < cnt; j++) {
			if(j + 1 == cnt) {
				//System.out.println(cnt);
				break;
			}
			if(process[process_id_with_smallest_burst_time][1] > process[pwsat[j + 1]][1]) {
				
				process_id_with_smallest_burst_time = pwsat[j + 1];
			
			}

		}
		if(process[process_id_with_smallest_burst_time][1] != 0) {
			process[process_id_with_smallest_burst_time][1]--;
			String result = "time: " + running_time + ":    running_process: " + process_id_with_smallest_burst_time + "\n";
			writer.write(result);
			i--;
		}
		running_time++; 
		if(process[process_id_with_smallest_burst_time][1] == 0) {
			turn_aroundtime[cnt2] = running_time - process[process_id_with_smallest_burst_time][0]; 
			waiting_time[cnt2] = turn_aroundtime[cnt2] - process1[process_id_with_smallest_burst_time][1];
			i++;
			cnt2++;
		}
		
		Arrays.fill(pwsat, 0);
		cnt = 0;
	}
	double total_turnaroundtime = 0.0;
	for(int i = 0; i < trt; i++) {
		total_turnaroundtime += turn_aroundtime[i];
	}
	double average_waitingtime = 0.0;

	for(int i = 0; i < trt; i++) {
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