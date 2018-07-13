import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


public class ReadAndImplementingPR implements Runnable {

	Scanner scan;
	String alg_type;
	static File file = null;
	static int stop_found =0;
	private static String ln=""; 
	private static int num_proc = 0; 
	private static int num_proc_max = 4; 
	private static int num_proc_cycle_max = 4;
	private static int[] process_priority = new int[num_proc_max]; 
	private static int[][] process_cpu_cycle = new int[num_proc_max][num_proc_cycle_max]; 
	private static int[][] process_io_cycle = new int[num_proc_max][num_proc_cycle_max];
	private static List<Integer> sortPR = new ArrayList<>();
	private static int[] sleep_position = new int[num_proc_max];
	
	private static int[][] start_cpu_time = new int[num_proc_max][num_proc_cycle_max];
	private static int[][] end_cpu_time = new int[num_proc_max][num_proc_cycle_max];
	private static int[][] start_io_time = new int[num_proc_max][num_proc_cycle_max];
	private static int[][] end_io_time = new int[num_proc_max][num_proc_cycle_max];
	private static double total_CPU_burst_time = 0.0, total_sleep_time = 0.0;
	private static double CPU_utilization = 0.0, throughput=0.0, tar_time=0.0, wait_time=0.0, response_time=0.0;
	
	private static List<List<Integer>> Finalsorting;
	private static List<List<Integer>> CollectionList =  new ArrayList<List<Integer>>();
	private static List<Integer> FinalList = new ArrayList<Integer>();
	
	private static List<List<Integer>> process1 = new ArrayList<List<Integer>>() ;
	private static List<List<Integer>> process2 = new ArrayList<List<Integer>>();
	private static List<List<Integer>> process3 = new ArrayList<List<Integer>>();
	private static List<List<Integer>> process4 = new ArrayList<List<Integer>>();

 
	// constructor
	public ReadAndImplementingPR(Scanner sc, String alg)
	{
		this.scan = sc; 
		this.alg_type = alg;
	}
	
	
	public void run(){
		
		start_cpu_time[0][0] = 0; end_cpu_time[0][0] = 0; start_io_time[0][0] = 0; end_io_time[0][0] = 0; 
		
		while(stop_found == 0)
		{
			if(ln.contains("stop"))
			{
				stop_found = 1;
			}
			else 
			{ 
				ln = scan.nextLine();
				String[] line_data = ln.split(" ");
				if(line_data[0].equalsIgnoreCase("proc"))
				{
					process_priority[num_proc] = Integer.parseInt(line_data[1]); 
					int cycle = 0;
					for (int cnt=2;cnt<line_data.length;cnt=cnt+2)
					{
						process_cpu_cycle[num_proc][cycle]=Integer.parseInt(line_data[cnt]);
						if(cnt == (line_data.length -1))
						{ 
							break;
						}
						process_io_cycle[num_proc][cycle]=Integer.parseInt(line_data[cnt+1]);
						cycle++;
					}
					num_proc++;
				}
				if(line_data[0].equalsIgnoreCase("sleep"))
				{
					sleep_position[num_proc] = Integer.parseInt(line_data[1]);
				}
					        
			}
		}
		
		PRListBuilder prList_thread = new PRListBuilder(process_cpu_cycle, process_io_cycle,num_proc, num_proc_cycle_max,
																	process_priority);
		
		try{
			prList_thread.start();
			prList_thread.join();
						
			sortPR = prList_thread.SubsortPR;
			Finalsorting  = getChunkList(sortPR, 3);
			
			for(int i=0; i<Finalsorting.size();i++){
				if(i==0||i==1||i==2){
					process1.add(Finalsorting.get(i));
				}
				else if(i==3||i==4||i==5){
					process2.add(Finalsorting.get(i));
				}
				else if(i==6||i==7||i==8){
					process3.add(Finalsorting.get(i));
				}
				else if(i==9||i==10||i==11){
					process4.add(Finalsorting.get(i));
				}
				
			}
			
			 Collections.sort(process1, Collections.reverseOrder(new Comparator<List<Integer>>(){
				 public int compare(List<Integer> arg0, List<Integer> arg1) {
			         return arg0.get(0).compareTo(arg1.get(0));
			     }
			}));
			
			 Collections.sort(process2, Collections.reverseOrder(new Comparator<List<Integer>>(){
				 public int compare(List<Integer> arg0, List<Integer> arg1) {
			         return arg0.get(0).compareTo(arg1.get(0));
			     }
			}));
			 
			 Collections.sort(process3, Collections.reverseOrder(new Comparator<List<Integer>>(){
				 public int compare(List<Integer> arg0, List<Integer> arg1) {
			         return arg0.get(0).compareTo(arg1.get(0));
			     }
			}));
			 Collections.sort(process4, Collections.reverseOrder(new Comparator<List<Integer>>(){
				 public int compare(List<Integer> arg0, List<Integer> arg1) {
			         return arg0.get(0).compareTo(arg1.get(0));
			     }
			}));
			 
		}catch(InterruptedException e){
			e.printStackTrace();
			}
		
	CollectionList.addAll(process1);
	CollectionList.addAll(process2);
	CollectionList.addAll(process3);
	CollectionList.addAll(process4);
	
	for(int i=0;i<CollectionList.size();i++){
		for(int j=0;j<CollectionList.get(i).size();j++){
			FinalList.add(CollectionList.get(i).get(j));
			
		}
	}
	
	
	PRSchedulingAndLogic cpuScheduling_PR_thread = new PRSchedulingAndLogic(FinalList, num_proc, num_proc_cycle_max, 
								sleep_position, start_cpu_time, end_cpu_time, start_io_time, end_io_time, 
								total_CPU_burst_time, total_sleep_time, CPU_utilization, throughput,
								tar_time, wait_time, response_time);
						
	try
	{
		long start = System.currentTimeMillis();
		cpuScheduling_PR_thread.start();
		cpuScheduling_PR_thread.join();
			
		CPU_utilization = cpuScheduling_PR_thread.cpu_utilization;
		throughput = cpuScheduling_PR_thread.through;
		tar_time = cpuScheduling_PR_thread.tar;
		wait_time = cpuScheduling_PR_thread.wait;
		response_time = cpuScheduling_PR_thread.response;
						
		System.out.println("CPU Utilization" + "\t\t : " + CPU_utilization +" (In percent)");	
		System.out.println("Throughput" + "\t\t : " + throughput );
		System.out.println("Turnaround Time" + "\t\t : " + tar_time );
		System.out.println("Waiting Time" + "\t\t : " + wait_time);
		System.out.println("Response Time" + "\t\t : " + response_time );
		
		long end = System.currentTimeMillis();
        NumberFormat formatter = new DecimalFormat("#0.0000");
        System.out.println("--------------------------------");
        System.out.println("Total Execution time" + "\t : " + formatter.format((end - start) / 1000d) + " seconds");
						
	}catch(InterruptedException e){
		e.printStackTrace();
		}
								
}
	
	 private static List<List<Integer>> getChunkList(List<Integer> largeList , int chunkSize)
	 {
         List<List<Integer>> chunkList = new ArrayList<>();
         for (int i = 0 ; i <  largeList.size() ; i += chunkSize)
         {
             chunkList.add(largeList.subList(i , i + chunkSize >= largeList.size() ? largeList.size() : i + chunkSize));
         }
         return chunkList;
     }
	
}

