# VitaConverter
Vitaconverter adalah alat portabel sederhana berbasis FFmpeg untuk mengonversi satu file video ke format dan kualitas lain dengan cepat dan mudah.

🔁 Konversi sebuah video menjadi:
* 🎥 Pilihan codec video: copy(asli, tanpa perubahan), h264, h265
* 🔊 Pilihan codec audio: copy(asli, tanpa perubahan), aac, mp3
* 📦 Format output: mp4, mkv
* 📐 Resolusi video: 144p hingga 4K
* 🧳 Portabel & ringan
* ⚙️ Menggunakan FFmpeg sebagai backend

vitaconverter cocok untuk pengguna yang hanya ingin mengubah kualitas atau format video secara cepat tanpa harus memahami parameter FFmpeg yang rumit.
dirancang sebagai pembungkus (wrapper) sederhana untuk FFmpeg, dengan tujuan menyederhanakan proses konversi video yang umum dilakukan sehari-hari.
Alih-alih menyediakan ratusan opsi lanjutan, vitaconverter hanya berfokus pada parameter yang paling sering digunakan:

🎥 Video
* Codec video

  * `copy` – menyalin stream video tanpa re-encode (cepat & tanpa penurunan kualitas)
  * `h264` – kompatibilitas luas & ukuran file seimbang
  * `h265` – kompresi lebih efisien dengan ukuran file lebih kecil

* Kualitas / Resolusi
  * 144p
  * 240p
  * 360p
  * 480p
  * 720p (HD)
  * 1080p (Full HD)
  * 1440p (2K)
  * 2160p (4K)

🔊 Audio
* Codec audio
  * `aac` – standar modern, kualitas baik
  * `mp3` – kompatibilitas tinggi

📦 Format Output
* `mp4`
* `mkv`

🎯 Tujuan Proyek
* Menyediakan alat konversi video sederhana untuk penggunaan personal
* Menghindari kompleksitas FFmpeg CLI bagi pengguna umum
* Cocok untuk:
  * Menurunkan resolusi video
  * Mengubah format file
  * Mengganti codec agar kompatibel dengan perangkat tertentu

❗ Catatan:
> Vitaconverter belum mendukung bulk conversion atau pemrosesan banyak file sekaligus.
> Setiap proses hanya ditujukan untuk satu file video per eksekusi.

🛠️ Ketergantungan
* FFmpeg
  vitaconverter memerlukan FFmpeg yang sudah terpasang atau disertakan secara portabel.

📌 Filosofi Desain
> Simple tool for simple needs.

vitaconverter tidak mencoba menggantikan FFmpeg, melainkan membuatnya lebih mudah diakses untuk tugas konversi video yang paling umum.
