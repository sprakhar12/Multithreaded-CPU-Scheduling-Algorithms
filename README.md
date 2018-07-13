# Multithreaded-CPU-Scheduling-Algorithms
-- Operating System Course Project --

CPU scheduling algorithms through process/thread synchronization mechanisms by implementing a multithreaded program that allow to measure the performance (i.e. CPU utilization, Throughput, Turnaround time, Waiting time and Response time) of three basic CPU scheduling algorithms ( FIFO, SJF and PR) by simulating the processes whose priority, sequence of CPU burst time (ms) and I/O burst time (ms) given in an input file.

<p><strong>ASSUMPTIONS</strong><br>
  
1.	All scheduling algorithms except PR will ignore process priorities.<br>
2.	All processes have the same priority in FIFO and SJF.<br>
3.	There is only one IO device and all IO requests will be served using that device in a FIFO manner.
</p>

<p><strong>CPU SCHEDULER AND I/O SYSTEM</strong><br>
  
1.	CPU scheduler thread will check ready queue; if there is a process, it will pick one according to the scheduling algorithm from ready queue and hold CPU resource for the given CPU burst time. Then it will release CPU resource and put this process into IO queue or just terminate if there is no more CPU or IO burst. Then CPU scheduler thread will check ready queue again and repeat the same.<br>
2.	 I/O system thread will check IO queue; if there is a process, it will hold IO device for the given IO burst time and then put this process back into ready queue. Then it will check IO queue and repeat the same.
</p>
