package benchmark.ram;

import java.util.Random;

import benchmark.IBenchmark;
import benchmark.status.BenchStatus;

public class RamBenchmark implements IBenchmark {

    public static final long KB = 1024L;
	public static final long MB = 1024L * 1024;
	public static final long GB = 1024L * 1024 * 1024;

    private static final byte[] patterns = {-86,85,-52,51,102,-103,90,-91,-127,126}; 

    private BenchStatus status;
    private long memSize;
    private int iterations;
    private double scale;

    @Override
    public void run() {
        
        status.setStatus(true);

        Long testSize;
        Random r = new Random();

        for(int i = 1 ; i <= iterations && status.getStatus(); i ++){
            IBenchmark bench = new AccessSpeedBenchmark();
            testSize =  i * (memSize / 100);
            bench.initialize(testSize);
            bench.getStatus().setScale(scale);
            bench.warmUp();
            bench.run(patterns[r.nextInt(10)]); 
            // System.out.println("Score : " + bench.getStatus().getScoreAverage());
            status.addScoreAverage(bench.getStatus().getScoreAverage());
            bench.clean();
        }

        status.setStatus(false);
    }

    @Override
    public void run(Object... options) {
        throw new RuntimeException("Unsuported function, use run() instead!");
    }

    @Override
	public void initialize(Object... params) {
		status = new BenchStatus();
		memSize = Long.parseLong(params[0].toString());
        iterations = Integer.parseInt(params[1].toString());
        iterations = min(iterations,10);
        scale = 1;
    }

    private int min(int a ,int b ) {
        if(a > b) return b;
        return a;
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

    }

    @Override
    public BenchStatus getStatus() {
        return status;
    }
    
    public void setScale(double scale){
        this.scale = scale;
    }
}