package benchmark.ram;

import benchmark.IBenchmark;
import benchmark.status.BenchStatus;
import logging.ConsoleLogger;
import logging.EmptyLogger;
import logging.ILogger;
import logging.TimeUnit;
import memory.MemoryHandler;
import timing.ITimer;
import timing.Timer;

public class AccessSpeedBenchmark implements IBenchmark {

	private BenchStatus status;
	private long blockSize;
	private MemoryHandler mem;
	private byte patternByte;
	private int patternInt;
	private ITimer timer;

	@Override
	public void run() {
		throw new RuntimeException("Unsuported function, use run(Object... options) instead!");
	}

	@Override
	public void run(Object... options) {
		status.setStatus(true);
		
		Object[] params = (Object[]) options;
		patternByte = Byte.parseByte(params[0].toString());
		patternInt = patternByte << 24 | patternByte << 16 | patternByte << 8 | patternByte;
		double readSpeed = 0;
		double writeSpeed = 0;

		warmUp();

		EmptyLogger cl = new EmptyLogger();

		status.setCurrentExecution("Allocating memory");

		mem.allocateMemory(blockSize);


		status.setCurrentExecution("Writing pattern to block bytesize");

		cl.write("Writing bytes : \n");
		timer.start();

		for(int step = 1 ; step < blockSize / 65536 && status.getStatus(); step*=2){
			for(int st = 0 ; st < step && status.getStatus(); st++){
				for(long i = st ; i < blockSize && status.getStatus(); i+=step){
					mem.writeByte(i, patternByte);
				}
			}
			long wtime = timer.pause();
			writeSpeed  =((double)blockSize/RamBenchmark.MB) / ((double)wtime / 1000000000L);
			status.addScoreAverage(writeSpeed);
			cl.writeTime("Write time for step "+step+" : ", wtime, TimeUnit.Sec);
			cl.write("Write speed : " + writeSpeed + " MB/S\n");
			timer.resume();
		}
		timer.pause();
		
		status.setCurrentExecution("Reading block bytesize");
		int faults = 0; 
		cl.write("Reading bytes : \n");
		
		timer.resume();
		for(int step = 1 ; step < blockSize / 65536 && status.getStatus(); step*=2){
			for(int st = 0 ; st < step && status.getStatus(); st++){
				for(long i = st ; i < blockSize && status.getStatus(); i+=step){
					mem.readByte(i);
				}
			}
			long rtime = timer.pause();
			readSpeed = ((double)blockSize/RamBenchmark.MB) / ((double)rtime / 1000000000L);
			status.addScoreAverage(readSpeed);
			cl.writeTime("Read time for step "+step+" : ", rtime, TimeUnit.Sec);
			cl.write("Read speed : " + readSpeed + " MB/S\n");
			timer.resume();
	
		}
		timer.pause();
		status.setCurrentExecution("Writing pattern to block intsize");
		
		cl.write("Writing ints : \n");
		timer.resume();
		for(int step = 4 ; step < blockSize / 65536 && status.getStatus(); step*=2){
			for(int st = 0 ; st < step && status.getStatus(); st+=4){
				for(long i = st ; i < blockSize && status.getStatus(); i+=step){
					mem.writeInt(i, patternInt);
				}
			}
			long wtime = timer.pause();
			writeSpeed  =((double)blockSize/RamBenchmark.MB) / ((double)wtime / 1000000000L);
			status.addScoreAverage(writeSpeed);
			cl.writeTime("Read time for step "+step+" : ", wtime, TimeUnit.Sec);
			cl.write("Read speed : " + writeSpeed + " MB/S\n");
			timer.resume();
		}
		timer.pause();

		cl.write("Reading ints : \n");
		status.setCurrentExecution("Reading block intsize");
		

		timer.resume();
		for(int step = 4 ; step < blockSize / 65536 && status.getStatus(); step*=2){
			for(int st = 0 ; st < step && status.getStatus(); st+=4){
				for(long i = st ; i < blockSize && status.getStatus(); i+=step){
					mem.readInt(i);
				}
			}
			long rtime = timer.pause();
			readSpeed  =((double)blockSize/RamBenchmark.MB) / ((double)rtime / 1000000000L);
			status.addScoreAverage(readSpeed);
			cl.writeTime("Read time for step "+step+" : ", rtime, TimeUnit.Sec);
			cl.write("Read speed : " + readSpeed + " MB/S\n");
			timer.resume();
	
		}
		timer.pause();

		status.setCurrentExecution("Finished");

		cl.write("Faults found : " + faults +"\n");
		cl.write("\nScore : " + status.getScoreAverage() + "\n");

		status.setProgression(100);
		mem.freeMemory();
		status.setStatus(false);
	}

	@Override
	public void initialize(Object... params) {
		status = new BenchStatus();
		try{
			mem = new MemoryHandler();
		}catch(RuntimeException e){
			throw new RuntimeException("Memory benchmark cant be used!");
		}
		timer = new Timer();
		blockSize = Long.parseLong(params[0].toString());
		
	}

	@Override
	public void clean() {
		status.setScore(0);
	}

	@Override
	public void cancel() {
		status.setStatus(false);
	}

	@Override
	public void warmUp() {

		mem.allocateMemory(blockSize);

		for(long i = 0 ; i < blockSize ; i ++){
			mem.writeByte(i, patternByte);
		}
		
		for(long i = 0 ; i < blockSize ; i ++){
			mem.readByte(i);
		}

		for(long i = 0 ; i < blockSize ; i += 4){
			mem.readInt(i);
		}

		mem.freeMemory();
	}

	@Override
	public BenchStatus getStatus() {
		return status;
	}
	
}