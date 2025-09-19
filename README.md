## Staj Library System (Spring Boot)

[English README](README.en.md)

Son Güncelleme: 19.09.2025

Basit bir kütüphane yönetim sistemidir. Spring Boot 3, Java 21, PostgreSQL, Spring Security (JWT), MapStruct ve Lombok kullanır.

### Özellikler
- Kullanıcı kayıt ve giriş (JWT)
- Kütüphane, raf, yazar ve kitap CRUD
- Ödünç alma süreçleri
- Swagger UI ile dokümantasyon

### Gereksinimler
- Java 21
- Maven 3.9+
- PostgreSQL 14+

### Kurulum
1) Veritabanını ve kullanıcıyı oluşturun (örnek):
```sql
CREATE DATABASE librarydb;
CREATE USER library_user WITH ENCRYPTED PASSWORD 'libpass';
GRANT ALL PRIVILEGES ON DATABASE librarydb TO library_user;
```

2) `src/main/resources/application.properties` dosyasını kontrol edin:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/librarydb
spring.datasource.username=library_user
spring.datasource.password=libpass

spring.jpa.hibernate.ddl-auto=update
springdoc.swagger-ui.path=/swagger-ui.html

application.jwt.secret=changeThisSecretKey_DevOnly_ChangeThisToAtLeast32Chars!!
application.jwt.expiration=86400000
```

Güvenli bir ortamda `application.jwt.secret` değerini değiştirin.

3) Derleme ve çalıştırma:
```bash
mvn clean package
mvn spring-boot:run
```

Uygulama varsayılan olarak `http://localhost:8080` üzerinde çalışır.

### Swagger UI
`http://localhost:8080/swagger-ui.html`

Swagger üzerinde JWT ile yetkili istek yapmak için:
- Önce `/api/auth/login` ile token alın.
- Swagger UI sağ üstte "Authorize" butonuna tıklayın.
- Açılan pencerede `Bearer <token>` formatında girin (örnek: `Bearer eyJhbGciOiJI...`).
- Artık korumalı uç noktaları deneyebilirsiniz.

### Varsayılan Yönetici Kullanıcısı
Uygulama ilk açılışta aşağıdaki yönetici hesabını tohumlar:
- E-posta: `admin@lms.local`
- Parola: `Admin#123`

Bu kullanıcı `com.ibb.library.config.DataSeeder` içerisinde oluşturulmaktadır.

### Örnek Alan Tohumları
İlk çalıştırmada alan nesneleri (kütüphane, raf, yazar, kitap) `com.ibb.library.config.DomainSeeder` ile yalnızca veri boşsa eklenir.

### Kimlik Doğrulama Uç Noktaları
Base path: `/api/auth`

- POST `/register`
  - Body: `{ "firstName", "lastName", "email", "password" }`
- POST `/login`
  - Body: `{ "email", "password" }`
  - Response: `{ "token", ... }`

Sonraki isteklerde `Authorization: Bearer <token>` başlığını kullanın.

### Postman ile JWT Akışı
1) Register (isteğe bağlı):
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "firstName": "Test",
  "lastName": "User",
  "email": "test@lms.local",
  "password": "Test#123"
}
```

2) Login (token alma):
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@lms.local",
  "password": "Admin#123"
}
```
Yanıt:
```json
{ "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." }
```

3) Korumalı uç noktaya istek (ör. kitap arama):
```http
GET http://localhost:8080/api/books/search?title=Sapiens
Authorization: Bearer <token>
```

4) cURL örnekleri:
```bash
# Login ve token'i değişkene al
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@lms.local","password":"Admin#123"}' | jq -r .token)

# Korumalı GET (listeleme)
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/books

# Admin yetkisi gerektiren oluşturma
curl -X POST http://localhost:8080/api/books \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"title":"Yeni Kitap","bookshelfId":1,"authorIds":[1]}'
```

Postman koleksiyonu bu repo içinde `postman/LibrarySystem.postman_collection.json` olarak yer alır. Önce `Login` isteğini çalıştırın, koleksiyon yanıt token'ını otomatik olarak `{{token}}` değişkenine yazar; diğer istekler `Authorization: Bearer {{token}}` başlığını kullanır.

### Swagger'dan Postman'a Export
Swagger UI üzerinden tüm uç noktaları Postman'a aktarmak için:
1) `http://localhost:8080/v3/api-docs` adresinden OpenAPI JSON'ı alın (veya Swagger UI içinde `Export` → `Raw OpenAPI` kopyalayın).
2) Postman → Import → Link veya Raw Text sekmesine OpenAPI JSON'ı verin.
3) Postman, koleksiyonu otomatik oluşturacaktır. Gerekirse `Authorization` ayarını koleksiyon düzeyinde `Bearer Token` seçip `{{token}}` olarak belirleyin.
4) Bu repo ile gelen hazır koleksiyon da kullanılabilir: `postman/LibrarySystem.postman_collection.json`.

### Staj Özeti (25.08.2025 - 19.09.2025)
Bu proje, İstanbul Büyükşehir Belediyesi / Bilgi İşlem Daire Başkanlığı / Bilgi Teknolojileri Şube Müdürlüğü bünyesinde yürütülen staj kapsamında geliştirilmiştir.

Bu staj, İstanbul Büyükşehir Belediyesi’nin dijital dönüşüm stratejilerine katkı sağlamak amacıyla gerçekleştirilmiştir. Çalışmanın temel amacı, Java Spring Boot ve PostgreSQL tabanlı bir altyapı üzerinde kurumsal ölçekte kullanılabilecek bir Kütüphane Yönetim Sistemi çekirdeği geliştirmektir. Proje yalnızca bu alanla sınırlı kalmamış, ileride belediyenin farklı servislerinde kullanılabilecek ortak (core) servis mimarisi için ön-prototip işlevi de görmüştür.

Staj süresince öncelikle PostgreSQL veritabanı ortamı hazırlanmış, Library–Bookshelf–Book–Author varlıkları ve ilişkileri tanımlanmıştır. DTO ve Mapper (MapStruct) katmanları ile veri transferi standartlaştırılmıştır. Spring Security ve JWT kullanılarak kimlik doğrulama ve rol tabanlı erişim (ADMIN/USER) kuralları uygulanmıştır. Kullanıcı, kütüphane ve kitap yönetimi için CRUD uçları geliştirilmiş; Postman ve Swagger arayüzleri üzerinden test edilmiştir.

Ödünç alma–iade süreçlerinde çok kopya (multi-copy) desteği, adet/stok kontrolü ve eşzamanlı işlem güvenliği (pessimistic lock) kurgulanmıştır. Ayrıca sayfalama, filtreleme ve sıralama mekanizmaları ile performanslı arama uçları sağlanmıştır.

Stajın sonunda proje, GitHub üzerinden sorumlu personele teslim edilmiştir. Çalışma boyunca hem teknik (Spring Boot, JPA/Hibernate, PostgreSQL, JWT, Exception Handling) hem de mesleki (kurumsal yazılım geliştirme süreçleri, ekip içi iletişim, dokümantasyon) kazanımlar elde edilmiştir. Proje, temel hedeflerini büyük ölçüde karşılamış ve gelecekte kullanıcı arayüzü (GUI), raporlama ve ceza/bildirim mekanizmaları ile genişletilebilecek sağlam bir altyapı oluşturmuştur.

### Geliştirme Notları
- Java sürümü: 21 (POM)
- Swagger bağımlılığı: `springdoc-openapi-starter-webmvc-ui`
- JWT: `io.jsonwebtoken:jjwt-*`
- Eşleşen MapStruct/Lombok için annotation processing Maven tarafından yapılandırılmıştır.

### Proje Çalıştırma Kısayolu
```bash
mvn spring-boot:run
```

### Lisans
Bu proje eğitim amaçlıdır.


