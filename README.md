## Giriş
Bu proje, 2 sensörün bir hedefe olan açılarını kullanarak hedefin konumunu hesaplayan bir Java programıdır.

## Kütüphaneler ve Versiyonları
Kullanılan kütüphaneler ve versiyonları ```pom.xml``` dosyasında bulunabilir:
- ```org.apache.kafka``` ```3.2.3```
- ```org.slf4j``` ```1.7.5```
- ```com.googlecode.json-simple``` ```1.1.1```

## Apache Kafka Kurulumu
1. https://kafka.apache.org/quickstart adresinden apache kafka indirilir.
2. Sitedeki adımlar izlenerek apache kafka çalıştırılır.
3. ```target-topic```, ```sensor0-topic``` ve ```sensor1-topic``` adlarında 3 topic oluşturulur.

## Kurulum
1. *maven* programı kullanılarak proje derlenir. Projenin ana dizininde ```pom.xml``` dosyasının bulunduğu yerde ```mvn clean compile``` komutu çalıştırılır.

## Çalıştırma
- Aşağıdaki komutlar ayrı ayrı terminallerde çalıştırılır:
1. ```java -classpath PATHS_TO_DEPENDENCIES targetdetection.Centre``` komutu ile merkezi birim çalıştırılır.
2. ```java -classpath PATHS_TO_DEPENDENCIES targetdetection.Sensor 0 X Y``` komutu ile Sensör 0 çalıştırılır.
```X``` ve ```Y``` değerleri sensörün pozisyonunu belirtir.
3. ```java -classpath PATHS_TO_DEPENDENCIES targetdetection.Sensor 1 X Y``` komutu ile Sensör 1 çalıştırılır.
```X``` ve ```Y``` değerleri sensörün pozisyonunu belirtir.
4. ```java -classpath PATHS_TO_DEPENDENCIES targetdetection.Target X Y``` komutu ile Hedef çalıştırılır.
```X``` ve ```Y``` değerleri hedefin pozisyonunu belirtir.
- Bu işlemlerden sonra Sensör 0 ve Sensör 1, Hedeften aldıkları mesajı ve merkezi birime gönderdiği mesajları 
ekrana yazdırır. Merkezi birim ise hesapladığı pozisyonu ekrana yazdırır.

## Hesaplama
Bir sensörün pozisyonu ve hedefe olan açısı bilindiğinden, hedefin muhtemel pozisyonları 2 boyutlu uzayda
bir doğru oluşturur. İkinci sensörden de bu doğru çizildiğinde -eğer sensörler ve hedef aynı doğru üzerinde
değilse- bu iki doğru bir noktada kesişir be bu nokta hedefin pozisyonudur.\
```x0```, ```y0``` ve ```m0``` sırasıyla sensör 0'ın x koordinatı, y koordinatı ve hedefle yaptığı açı olsun.\
Hedefin x koordinatı ```x = (m0 * x0 - y0 - m1 * x1 + y1) / (m1 - m0)``` formülü ile hesaplanabilir.
Hedefin y koordinatı ise ```y = m0 * x - m0 * x0 + y0``` formülünde ```x```'in değeri yerine konarak hesaplanır.

## Sorunlar
Apache Kafka programına hakim olmadığımdan dolayı aşağıdaki sorunu çözemedim:

- Program tekrar çalıştığında hedefin gönderdiği mesaj sensörler tarafından alınamıyor.
Bu durumu çözmek için Apache Kafka'yı yeniden başlatmak gerekiyor. 

