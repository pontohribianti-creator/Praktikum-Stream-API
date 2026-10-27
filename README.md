# Praktikum 04 — Benchmark Stream API

## 1. Lingkungan Pengujian

- Framework benchmark: JMH 1.37
- Java: JDK 27
- Sistem operasi: Windows 11 64-bit
- Prosesor: Intel(R) Celeron(R) N4020 CPU @ 1.10GHz
- Warmup: 2 iterasi, masing-masing 1 detik
- Pengukuran: 3 iterasi, masing-masing 1 detik
- Fork: 1
- Mode: Average Time
- Satuan: mikrodetik per operasi (us/op)

## 2. Hasil Benchmark

| Ukuran Data | Plain For (us/op) | Stream (us/op) | Parallel Stream (us/op) |
|---:|---:|---:|---:|
| 1.000 | 5,244 | 3,226 | 9,809 |
| 100.000 | 494,421 | 425,593 | 325,002 |
| 5.000.000 | 34.481,375 | 29.255,958 | 21.554,932 |

## 3. Analisis Hasil

Pada 1.000 elemen, "sequentialStream" memiliki waktu rata-rata terendah, yaitu 3,226 µs/op. Pada 100.000 elemen, "parallelStream" menjadi metode tercepat dengan waktu rata-rata 325,002 µs/op. Pada 5.000.000 elemen, "parallelStream" juga menjadi metode tercepat dengan waktu rata-rata 21.554,932 µs/op.

Hasil ini menunjukkan bahwa pemrosesan paralel berpotensi lebih menguntungkan ketika jumlah data meningkat. Namun, hasil benchmark dapat dipengaruhi oleh kondisi CPU, aktivitas sistem, pengelolaan memori, dan overhead thread. Variasi hasil pengukuran juga perlu diperhatikan sebelum menarik kesimpulan umum.

## 4. Analisis Titik Impas (Break-Even Point)

Pada pengujian ini, "parallelStream" membutuhkan waktu lebih lama dibandingkan "plainFor" dan "sequentialStream" saat memproses 1.000 data. Namun, pada 100.000 dan 5.000.000 data, "parallelStream" menghasilkan waktu rata-rata paling rendah. Hal ini menunjukkan bahwa pemrosesan paralel berpotensi lebih menguntungkan ketika jumlah data meningkat. Titik impas yang tepat belum dapat ditentukan karena pengujian hanya dilakukan pada tiga ukuran data, yaitu 1.000, 100.000, dan 5.000.000 elemen. Pengujian tambahan dengan ukuran data di antara 1.000 dan 100.000 elemen diperlukan untuk memperkirakan titik impas secara lebih akurat. Hasil juga dapat dipengaruhi oleh spesifikasi perangkat dan kondisi pengujian.

## 5. Simpangan Baku

Simpangan baku berdasarkan hasil pengujian JMH terbaru:

Ukuran Data| Plain For (us/op)| Sequential Stream (us/op)| Parallel Stream (us/op)
1.000| 1,084| 0,164| 1,531
100.000| 5,053| 7,148| 83,613
5.000.000| 1.234,268| 144,287| 4.411,176

Nilai simpangan baku yang lebih kecil menunjukkan hasil pengukuran yang lebih konsisten dalam iterasi yang dilakukan. Nilai yang besar menunjukkan variasi hasil yang lebih tinggi. Variasi ini dapat dipengaruhi oleh kondisi CPU, aktivitas sistem, dan karakteristik pemrosesan paralel.

## 6. Kesimpulan

Berdasarkan hasil pengujian, "sequentialStream" memiliki waktu rata-rata terendah pada 1.000 elemen, yaitu 3,226 µs/op. Sementara itu, "parallelStream" menjadi metode tercepat pada 100.000 elemen dengan waktu 325,002 µs/op dan pada 5.000.000 elemen dengan waktu 21.554,932 µs/op.

Hasil ini menunjukkan bahwa pemrosesan paralel berpotensi lebih menguntungkan ketika jumlah data meningkat. Namun, pemilihan metode tetap perlu mempertimbangkan ukuran data, overhead pemrosesan paralel, variasi hasil pengukuran, dan spesifikasi perangkat. Kesimpulan ini berlaku untuk lingkungan pengujian yang digunakan dan belum tentu sama pada komputer lain.

## 7. Eksperimen Parallel Stream dan ArrayList

Eksperimen dilakukan dengan menambahkan 100.000 data ke dalam "ArrayList" menggunakan operasi "IntStream.range(0, 100_000).parallel().forEach(hasil::add)".

Hasil pengujian sebanyak enam kali ditampilkan pada tabel berikut.

Percobaan| Data yang Seharusnya| Data yang Terkumpul
1| 100.000| 100.000
2| 100.000| 100.000
3| 100.000| 99.989
4| 100.000| 99.873
5| 100.000| 99.985
6| 100.000| 100.000

Hasil menunjukkan bahwa pada tiga percobaan, jumlah data yang terkumpul kurang dari jumlah yang seharusnya. Hal ini terjadi karena "ArrayList" bukan koleksi yang aman untuk penambahan data secara bersamaan oleh banyak thread. Kondisi tersebut dapat menyebabkan race condition dan kehilangan data.

Kesimpulannya, penggunaan "parallelStream()" tidak otomatis membuat operasi aman untuk dijalankan secara paralel. Untuk menghindari masalah tersebut, gunakan koleksi yang sesuai atau operasi pengumpulan data yang aman terhadap pemrosesan paralel.

## 8. Ringkasan Menggunakan Collectors.teeing

Program menggunakan "Collectors.teeing()" untuk menggabungkan hasil dua collector, yaitu "Collectors.counting()" dan "Collectors.reducing()". Collector kedua menjumlahkan nominal transaksi menggunakan "BigDecimal", sehingga total nominal tidak bergantung pada penjumlahan bilangan "double".

Kedua hasil tersebut kemudian digabungkan menjadi record "Summary" yang berisi jumlah transaksi, total nominal, dan rata-rata nominal. Rata-rata dihitung dengan dua angka desimal dan pembulatan "HALF_UP".

Hasil pengujian pada tujuh transaksi:

- Jumlah transaksi: 7
- Total nominal: Rp63.750.000,00
- Rata-rata nominal: Rp9.107.142,86

Hasil ini sesuai dengan data transaksi yang digunakan dalam program.