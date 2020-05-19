package testbench;
import benchmark.IBenchmark;
import benchmark.ram.AccessSpeedBenchmark;
import benchmark.ram.RamBenchmark;

public class App {
    public static void main(String[] args) throws Exception {
        
        
        IBenchmark bench = new RamBenchmark();

        bench.initialize(RamBenchmark.GB * 16 , 10);
        bench.run();

        System.out.println("Overall score : "+ bench.getStatus().getScoreAverage() +"\n");
    
    }
}
