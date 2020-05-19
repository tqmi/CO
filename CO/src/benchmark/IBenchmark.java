package benchmark;

import benchmark.status.BenchStatus;

public interface IBenchmark {
	void run();
    void run(Object... options);
    void initialize (Object...params);
    void clean();
    void cancel();
    void warmUp();
    BenchStatus getStatus();
}
