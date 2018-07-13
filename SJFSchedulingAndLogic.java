import java.util.LinkedList;
import java.util.List;

public class SJFSchedulingAndLogic extends Thread {

	private int[][] cpuqueue ;
	private int[][] ioqueue ;
	int process;
	int process_cycle;
	int[] sleep;
	int[][] start_cpu;
	int[][] end_cpu;
	int[][] start_io;
	int[][] end_io;
	List<Integer> sort;
	
	double cpu_burst_time= 0.0, sleep_time=0.0;
	public double cpu_utilization = 0.0, through=0.0, tar=0.0, wait=0.0, response=0.0;
	
	//Constructor
	public SJFSchedulingAndLogic(List<Integer> sort, int process, int process_cycle, int[] sleep, int[][]start_cpu, int[][] end_cpu, 
			int[][] start_io, int[][] end_io, double cpu_burst_time, double sleep_time, double cpu_utilization, double through,
			double tar, double wait, double response)
	{
		this.sort = sort;
		this.process = process;
		this.process_cycle = process_cycle;
		this.sleep = sleep;
		this.start_cpu = start_cpu;
		this.end_cpu = end_cpu;
		this.start_io = start_io;
		this.end_io = end_io;
		this.cpu_burst_time = cpu_burst_time;
		this.sleep_time = sleep_time;
		this.cpu_utilization = cpu_utilization;
		this.through = through;
		this.tar = tar;
		this.wait = wait;
		this.response = response;
	}

	public void run()
	{
		cpuqueue = new int[process][process_cycle];
		ioqueue = new int[process][process_cycle];
		int cnt=0;
		int count=1;
		
		for(int cnt_proc_cyc=0;cnt_proc_cyc<process_cycle;cnt_proc_cyc++)
		{
			for(int cnt_proc_num=0;cnt_proc_num<process;cnt_proc_num++) 
			{
				cpuqueue[cnt_proc_num][cnt_proc_cyc] = sort.get(cnt);
				cnt=cnt+2;
			}
		
		}
		
		
		for(int cnt_proc_cyc=0;cnt_proc_cyc<process_cycle;cnt_proc_cyc++)
		{
			for(int cnt_proc_num=0;cnt_proc_num<process;cnt_proc_num++) 
			{
				ioqueue[cnt_proc_num][cnt_proc_cyc] = sort.get(count);
				count=count+2;
			}
		}
		
		
	
		//CPU Queue thread implementing 2D Array LinkedList
		Thread cpu_thread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				@SuppressWarnings("unchecked")
				LinkedList<Integer>[][] cpu_LinkedList = new LinkedList[process_cycle][process];
				cpu_LinkedList[0][0] = new LinkedList<>();
				int tmp = end_cpu[0][0];
				
				for(int cnt_proc_cyc=0;cnt_proc_cyc<process_cycle;cnt_proc_cyc++)
				{
					for(int cnt_proc_num=0;cnt_proc_num<process;cnt_proc_num++)
					{
						synchronized (CommonUtil.mLock)
						{
							cpu_LinkedList[0][0].addFirst(cpuqueue[cnt_proc_num][cnt_proc_cyc]);
							end_cpu[cnt_proc_num][cnt_proc_cyc] += tmp + cpu_LinkedList[0][0].getFirst()+sleep[cnt_proc_num];
							tmp = end_cpu[cnt_proc_num][cnt_proc_cyc];
							start_cpu[cnt_proc_num][cnt_proc_cyc] = end_cpu[cnt_proc_num][cnt_proc_cyc] - cpu_LinkedList[0][0].getFirst();
							cpu_burst_time += cpu_LinkedList[0][0].getFirst();
							
							if (cnt_proc_cyc == 0) 
							{
								wait += (start_cpu[cnt_proc_num][cnt_proc_cyc] - 0); 
								response += (start_cpu[cnt_proc_num][cnt_proc_cyc] - 0);
			
							}
							else
							{
								wait += (start_cpu[cnt_proc_num][cnt_proc_cyc] - end_io[cnt_proc_num][cnt_proc_cyc - 1]); 
							}
						
							CommonUtil.mLock.notify();
							try 
							{
								CommonUtil.mLock.wait();
							} catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
							
					}
				}		
			}
			
			wait = wait/process;
			
			for(int cnt_proc_num=0;cnt_proc_num<process;cnt_proc_num++) 
			{
				sleep_time += sleep[cnt_proc_num];
				
				// which cycle to take
				int last_cycle = 0;
				
				for(int cnt_proc_cyc=0;cnt_proc_cyc<process_cycle;cnt_proc_cyc++)
				{
					if ((end_cpu[cnt_proc_num][cnt_proc_cyc]-start_cpu[cnt_proc_num][cnt_proc_cyc]) == 0) 
					{
						last_cycle = cnt_proc_cyc-1; 
						break;
					}
					else 
					{
						last_cycle = cnt_proc_cyc; 
					}
				}
				
				// arrival time for all the processes is zero
				tar += (end_cpu[cnt_proc_num][last_cycle]-0); 
			}
			
			tar = tar/process;
			cpu_utilization = cpu_burst_time/(cpu_burst_time+sleep_time) * 100;
			through = process/(cpu_burst_time+sleep_time);
			
        }
	});
	
	
	//I/O Queue thread implementing 2D Array LinkedList
	Thread io_thread = new Thread(new Runnable()
	{
        @Override
        public void run()
        {
        	@SuppressWarnings("unchecked")
			LinkedList<Integer>[][] io_LinkedList = new LinkedList[process_cycle][process];
        	io_LinkedList[0][0] = new LinkedList<>();
 
        	for(int cnt_proc_cyc=0;cnt_proc_cyc<process_cycle;cnt_proc_cyc++)
        	{
				for(int cnt_proc_num=0;cnt_proc_num<process;cnt_proc_num++)
				{
					synchronized (CommonUtil.mLock) 
					{
						io_LinkedList[0][0].addFirst(ioqueue[cnt_proc_num][cnt_proc_cyc]);
						start_io[cnt_proc_num][cnt_proc_cyc] = end_cpu[cnt_proc_num][cnt_proc_cyc];
						end_io[cnt_proc_num][cnt_proc_cyc] = start_io[cnt_proc_num][cnt_proc_cyc] + io_LinkedList[0][0].getFirst();
			
						CommonUtil.mLock.notify();
						try {
							CommonUtil.mLock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
				}
        	}
        }
 });
 
	//CPU and I/O thread started
	cpu_thread.start();
	io_thread.start();
	try 
	{
		//Only cpu_thread needs to wait for the I/O to complete
		cpu_thread.join();
	} catch (InterruptedException e){
    	e.printStackTrace();
		}
    
	//End of Complete run method of extends thread    
	}

static class CommonUtil{
	final static Object mLock = new Object();
	}

//End of complete class
}

