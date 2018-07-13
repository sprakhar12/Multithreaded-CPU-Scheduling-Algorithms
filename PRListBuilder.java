import java.util.ArrayList;
import java.util.List;

public class PRListBuilder extends Thread {

	int[][] cpuqueue;
	int[][] ioqueue;
	int process;
	int process_cycle;
	int[] priority;
	public List<Integer> SubsortPR = new ArrayList<Integer>();
	
	//Constructor
	public PRListBuilder(int[][] cpuqueue, int[][] ioqueue, int process, int process_cycle, int[] priority)
	{
		this.cpuqueue = cpuqueue;
		this.ioqueue = ioqueue;
		this.process = process;
		this.process_cycle = process_cycle;
		this.priority = priority;
	}

	public void run()
	{
		Thread t1 = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				for(int cnt_proc_cyc=0;cnt_proc_cyc<process_cycle;cnt_proc_cyc++)
				{
					for(int cnt_proc_num=0;cnt_proc_num<process;cnt_proc_num++)
					{
						synchronized (CommonUtil.mLock)
						{
							SubsortPR.add(priority[cnt_proc_num]);
							SubsortPR.add(cpuqueue[cnt_proc_num][cnt_proc_cyc]);
							CommonUtil.mLock.notify();
							try
							{
								CommonUtil.mLock.wait();
							} catch (InterruptedException e){
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
		
		
		Thread t2 = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				for(int cnt_proc_cyc=0;cnt_proc_cyc<process_cycle;cnt_proc_cyc++)
				{
					for(int cnt_proc_num=0;cnt_proc_num<process;cnt_proc_num++)
					{
						synchronized (CommonUtil.mLock) 
						{
							SubsortPR.add(ioqueue[cnt_proc_num][cnt_proc_cyc]);
                    
							CommonUtil.mLock.notify();
							try
							{
								CommonUtil.mLock.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
    
    t1.start();
    t2.start();
    try
    {
    	t1.join();
	} catch (InterruptedException e) {
		e.printStackTrace();
		}
    
    	 
   
	}
	
static class CommonUtil
	{
		static final Object mLock = new Object();
	} 
	
}


