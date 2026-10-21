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
| 1.000 | 2.609 | 3.377 | 9.057 |
| 100.000 | 394.831 | 504.787 | 369.892 |
| 5.000.000 | 30.159,388 | 28.692,233 | 21.007,682 |

## 3. Analisis Hasil

Pada 1.000 elemen, perulangan for memiliki waktu rata-rata terendah. Hal ini karena overhead pembagian tugas pada parallelStream belum sebanding dengan pekerjaan yang dilakukan.

Pada 100.000 dan 5.000.000 elemen, parallelStream menghasilkan waktu rata-rata terendah dalam pengujian ini. Pemrosesan paralel dapat membagi pekerjaan ke beberapa thread sehingga berpotensi mempercepat pemrosesan data berukuran besar.

Namun, hasil benchmark dapat dipengaruhi oleh kondisi CPU, aktivitas sistem, pengelolaan memori, dan overhead thread. Pengujian tambahan diperlukan untuk memastikan kestabilan hasil.

## 4. Simpangan Baku

Simpangan baku dari keluaran JMH:

| Ukuran Data | Plain For (us/op) | Stream (us/op) | Parallel Stream (us/op) |
|---:|---:|---:|---:|
| 1.000 | 0,234 | 0,356 | 0,614 |
| 100.000 | 104,175 | 109,844 | 55,878 |
| 5.000.000 | 5.158,228 | 300,247 | 3.769,663 |

Nilai simpangan baku yang lebih kecil menunjukkan hasil pengukuran yang lebih konsisten dalam iterasi yang dilakukan. Nilai yang besar menunjukkan variasi hasil yang lebih tinggi.

## 5. Kesimpulan

Berdasarkan pengujian ini, plain for paling cepat pada 1.000 elemen, sedangkan parallelStream memiliki waktu rata-rata terendah pada 100.000 dan 5.000.000 elemen. Oleh karena itu, pemilihan metode perlu mempertimbangkan ukuran data, overhead pemrosesan paralel, dan kondisi perangkat. Hasil ini berlaku untuk lingkungan pengujian yang digunakan dan tidak dapat digeneralisasi untuk semua komputer.

## 6. Eksperimen Parallel Stream dan ArrayList

Eksperimen dilakukan dengan menambahkan 100.000 data ke dalam "ArrayList" menggunakan "IntStream.range(0, 100_000).parallel().forEach(hasil::add)".

Hasil pengujian sebanyak enam kali:

Percobaan| Data yang Seharusnya| Data yang Terkumpul
1| 100.000| 100.000
2| 100.000| 100.000
3| 100.000| 99.989
4| 100.000| 99.873
5| 100.000| 99.985
6| 100.000| 100.000

Hasil menunjukkan bahwa pada tiga percobaan, jumlah data yang terkumpul kurang dari jumlah yang seharusnya. Hal ini terjadi karena "ArrayList" bukan koleksi yang aman untuk penambahan data secara bersamaan oleh banyak thread. Kondisi tersebut dapat menyebabkan race condition dan kehilangan data.

Kesimpulannya, penggunaan "parallelStream()" tidak otomatis membuat operasi aman untuk dijalankan secara paralel. Untuk menghindari masalah tersebut, gunakan koleksi yang sesuai atau operasi pengumpulan data yang aman terhadap pemrosesan paralel.

## 7. Ringkasan Menggunakan Collectors.teeing

Program menggunakan "Collectors.teeing()" untuk menggabungkan hasil dua collector, yaitu "Collectors.counting()" dan "Collectors.summingDouble(Transaction::amount)". Kedua hasil tersebut kemudian digunakan untuk membentuk record "Summary" yang berisi jumlah transaksi, total nominal, dan rata-rata nominal.

Hasil pengujian pada tujuh transaksi:

- Jumlah transaksi: 7
- Total nominal: Rp63.750.000,00
- Rata-rata nominal: Rp9.107.142,86

Hasil ini sesuai dengan data transaksi yang digunakan dalam program.