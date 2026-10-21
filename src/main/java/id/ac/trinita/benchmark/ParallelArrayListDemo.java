package id.ac.trinita.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParallelArrayListDemo {

    public static void main(String[] args) {
        List<Integer> hasil = new ArrayList<>();

        IntStream.range(0, 100_000)
                .parallel()
                .forEach(hasil::add);

        System.out.println("Jumlah data seharusnya: 100000");
        System.out.println("Jumlah data yang terkumpul: " + hasil.size());
    }
}