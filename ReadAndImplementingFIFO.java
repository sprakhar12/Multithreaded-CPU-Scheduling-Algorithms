import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

public class ReadAndImplementingFIFO implements Runnable{
	
	Scanner scan;
	String alg_type;
	private int stop_found =0;
	private String ln=""; 
	private int num_proc = 0; 
	private int num_proc_max = 4; 
	private int num_proc_cycle_max = 4;
	private int[] process_priority = new int[num_proc_max]; 
	private int[][] process_cpu_cycle = new int[num_proc_max][num_proc_cycle_max]; 
	private int[][] process_io_cycle = new int[num_proc_max][num_proc_cycle_max];
	
	private int[] sleep_position = new int[num_proc_max];
	private int[][] start_cpu_time = new int[num_proc_max][num_proc_cycle_max];
	private int[][] end_cpu_time = new int[num_proc_max][num_proc_cycle_max];
	private int[][] start_io_time = new int[num_proc_max][num_proc_cycle_max];
	private int[][] end_io_time = new int[num_proc_max][num_proc_cycle_max];
	private double total_CPU_burst_time = 0.0, total_sleep_time = 0.0;
	private double CPU_utilization = 0.0, throughput=0.0, tar_time=0.0, wait_time=0.0, response_time=0.0;
	
	// constructor
	public ReadAndImplementingFIFO(Scanner sc, String alg) 
	{ 
		this.scan = sc; 
		this.alg_type = alg;
	} 

		
	public void run()
	{
		start_cpu_time[0][0] = 0; end_cpu_time[0][0] = 0; start_io_time[0][0] = 0; end_io_time[0][0] = 0; 
		
		// Input from given file
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
					
		FIFOSchedulingAndLogic cpuScheduling_FIFO_thread = new FIFOSchedulingAndLogic(process_cpu_cycle, process_io_cycle,num_proc, num_proc_cycle_max, 
												sleep_position, start_cpu_time, end_cpu_time, start_io_time, end_io_time, 
												total_CPU_burst_time, total_sleep_time, CPU_utilization, throughput,
												tar_time, wait_time, response_time);
		
		try{
			long start = System.currentTimeMillis();
			cpuScheduling_FIFO_thread.start();
			cpuScheduling_FIFO_thread.join();
						
			CPU_utilization = cpuScheduling_FIFO_thread.cpu_utilization;
			throughput = cpuScheduling_FIFO_thread.through;
			tar_time = cpuScheduling_FIFO_thread.tar;
			wait_time = cpuScheduling_FIFO_thread.wait;
			response_time = cpuScheduling_FIFO_thread.response;
						
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
	
}
