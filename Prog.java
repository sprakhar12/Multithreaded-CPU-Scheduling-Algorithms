import java.io.*;
import java.util.Scanner;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Prog {

	public static void main(String args[])
	{
		 String alg_type=""; 
		 int quantum_value=0;
		 String input_file_name="";
		 File file = null;
		 Scanner scan;
			
		 try {
		    for(int cnt=0; cnt < args.length;cnt++)
		    {
		    	if(args[cnt].equalsIgnoreCase("-alg"))
		    	{
		    		alg_type = args[cnt+1]; 
		    	}
		    	
		    	if(!((alg_type.equalsIgnoreCase("FIFO"))||(alg_type.equalsIgnoreCase("SJF"))||(alg_type.equalsIgnoreCase("PR"))))
		    	{
		    	if(args[cnt].equalsIgnoreCase("-quantum"))
			    { 
			    	quantum_value = Integer.parseInt(args[cnt+1]); 
			    }
		    	else
		    	{
		    		continue;
		    	}
		    	}
		    	if(args[cnt].equalsIgnoreCase("-input"))
		    	{ 
		    		input_file_name = args[cnt+1]; 
		    	}
		    	
		    }
		    
		    if (!((alg_type.equalsIgnoreCase("FIFO")) || (alg_type.equalsIgnoreCase("SJF")) || (alg_type.equalsIgnoreCase("PR"))|| (alg_type.equalsIgnoreCase("RR")))) 
		    {
		    	throw new IOException("Input issues with alg -only allowed name type is FIFO,SJF,PR or RR");
		    }
		    
		    if(!((alg_type.equalsIgnoreCase("FIFO"))||(alg_type.equalsIgnoreCase("SJF"))||(alg_type.equalsIgnoreCase("PR"))))
		    {
		    	if (quantum_value <= 0.0 || quantum_value == 0)
		    	{
		    		throw new IOException("Enter -quantum as command line argument/Enter quantum value as positive integer");
		    	}
		    	else
		    	{
		    		return;
		    	}
		    }
		    
		    
    		file = new File(input_file_name);
    		if (!file.canRead())
    		{
    			System.out.println("File doesnt exist");
    		}
			scan = new Scanner(file);
			
			//Thread Creations
			ReadAndImplementingFIFO fifo = new ReadAndImplementingFIFO (scan,alg_type);
			ReadAndImplementingSJF sjf = new ReadAndImplementingSJF (scan,alg_type);
			ReadAndImplementingPR pr = new ReadAndImplementingPR (scan,alg_type);
			
			System.out.println("Input file name" + "\t\t : " + input_file_name);
			System.out.println("CPU Scheduling Alg" + "\t : " + alg_type + "  (" + quantum_value + ")");
			
			
			if(alg_type.equalsIgnoreCase("FIFO")){
				
				//main thread: Thread 0 assigned for main program
				Thread t0 = new Thread (fifo);
				long start = System.currentTimeMillis();
				
				// this thread calls the run method of ReadAndImplementingFIFO
				t0.start(); 
		    	t0.join();
		    	
		    	long end = System.currentTimeMillis();
		        NumberFormat formatter = new DecimalFormat("#0.0000");
		        System.out.println("--------------------------------");
		        System.out.println("Overall Program time" + "\t : " + formatter.format((end - start) / 1000d) + " seconds");
			}
			
			if(alg_type.equalsIgnoreCase("SJF")){
				
				//main thread: Thread 0 assigned for main program
				Thread t0 = new Thread (sjf);
				long start = System.currentTimeMillis();
				
				// this thread calls the run method of ReadAndImplementingSJF
				t0.start(); 
		    	t0.join();
		    	
		    	long end = System.currentTimeMillis();
		        NumberFormat formatter = new DecimalFormat("#0.0000");
		        System.out.println("--------------------------------");
		        System.out.println("Overall Program time" + "\t : " + formatter.format((end - start) / 1000d) + " seconds");
			}
			
			if(alg_type.equalsIgnoreCase("PR")){
				
				//main thread: Thread 0 assigned for main program
				Thread t0 = new Thread (pr);
				long start = System.currentTimeMillis();
				
				// this thread calls the run method of ReadAndImplementingPR
				t0.start(); 
		    	t0.join();
		    	
		    	long end = System.currentTimeMillis();
		        NumberFormat formatter = new DecimalFormat("#0.0000");
		        System.out.println("--------------------------------");
		        System.out.println("Overall Program time" + "\t : " + formatter.format((end - start) / 1000d) + " seconds");
			}
		    

		 } catch (Exception e) {
		    	System.out.println("Command line input error!!!");
		    	e.printStackTrace();
		 }
	}
}


