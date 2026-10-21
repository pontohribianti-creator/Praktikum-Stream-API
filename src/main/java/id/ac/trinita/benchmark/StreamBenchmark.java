package id.ac.trinita.benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class StreamBenchmark {

    @Param({"1000", "100000", "5000000"})
    public int size;

    private List<Integer> data;

    @Setup(Level.Trial)
    public void setup() {
        data = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            data.add(i);
        }
    }

    @Benchmark
    public long plainFor() {
        long sum = 0;

        for (int value : data) {
            if (value % 2 == 0) {
                sum += (long) value * 2;
            }
        }

        return sum;
    }

    @Benchmark
    public long sequentialStream() {
        return data.stream()
                .filter(value -> value % 2 == 0)
                .mapToLong(value -> (long) value * 2)
                .sum();
    }

    @Benchmark
    public long parallelStream() {
        return data.parallelStream()
                .filter(value -> value % 2 == 0)
                .mapToLong(value -> (long) value * 2)
                .sum();
    }
}