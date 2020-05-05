# Perzisztens réteg az architektúrában

## Ajánlott irodalom

* Core J2EE Patterns
* Adam Bien: Real World Java EE Patterns

## Megfontolandók, problémák, architektúrális döntések

### Adatbázis

* Adatbázis függetlenség
    * Implementációs különbségek az adatbázisok között: sort, indexing, ékezetek kezelése, magyar kettős betűk kezelése, natív query (pl. window function), limit, kulcsgenerálás, séma létrehozása, stb.
    * Függetlenség kezelése: JPA, SQL szabvány használata, Liquibase pl. tud adatbázis függetlenül sémát létrehozni (generálja az SQL-eket XML alapján)
    * Integrációs tesztek gyorsítására a valós adatbázis test double-re (Fake object) cserélhető, pl. egy in-memory H2-re. Ekkor lehet, hogy érdemes az adatbázis függetlenségre figyelni
    * Nap közben futhatnak a tesztek H2-vel, éjszaka, ritkábban pedig valós adatbázissal
* Üzleti logika az adatbázisban - értelmezés kérdése, hogy mit nevezünk itt üzleti logikának
* Constraint - adatintegráció fenntartására, trigger, tárolt eljárások - indokolt esetben használható
* Adatmozgatás adatbázis és alkalmazás között lassú, sokkal gyorsabb az aggregációkat az adatbázisban elvégezni
* Speciálisan tárolandó, visszakeresendő: fa, gráf, idősor, fájl, szabad szöveg
    * Használható NoSQL adatbázis, melyek típusai: Wild column, Document, Key-value, Graph
    * Wild column: pl. Cassandra
    * Document: pl. MongoDB
    * Key-value: pl. Redis, ZooKeeper
    * Graph: Neo4J
    * Idősorok kezelésére Time series database (pl. InfluxDB, Prometheus, RRDtool)
    * Fájlok tárolása: performancia alapján fájlrendszerben, de nehézesebben menthető, nem tranzakcionális
    * Fájlok tárolására esetleg cloud service-k
    * Szöveg indexelésénél speciális igények, pl. szótövesítés, rokonértelmű szavak, implementációk: Lucene, erre épülő [Solr](https://lucene.apache.org/solr/) vagy [Elasticsearch](https://www.elastic.co/)
* Auditálás
    * Az auditálást kétféleképp értelmezhetjük: azt akarjuk tárolni, hogy az adott rekordot ki és mikor módosította utoljára, vagy meg akarjuk őrizni a régi értékét is
    * Ennek töltése triggerrel megoldható, azonban bonyolult neki átadni a bejelentkezett felhasználót (erre a fejlesztőnek kell figyelnie)
    * Ez automatizálható, hogy az entitáshoz kötjük, `@PrePersist` és `@PreUpdate` annotációval (itt kell lekérdezni a bejelentkezett felhasználót), lehet megadni globális entity listenert is `@EntityListeners(class=Audit.class)`
    * Ha az entitás múltját, history-t is tárolni akarunk, akkor érdemes megduplázni a táblákat
    * Envers is egy megoldás, de az felhozza Java szintre (https://www.jtechlog.hu/2010/09/09/entitasok-auditalasa-hibernate-envers.html)
    * Envers jó gyakorlata, hogy minden tranzakció kap egy azonosítót, azt elmenti egy közös táblába, és a duplikált táblákban erre van külső kulcs
    * Ez a tranzakció tábla tartalmazza a tranzakció azonosítót, felhasználót, dátumot, de bármit beleírhatunk (pl. forrás ip cím, stb.)
    * Bizonyos esetekben elgondolkodhatunk, hogy esetleg a bejövő műveleteket naplózzuk
* Adatbázis séma verziózás, séma migráció (Flyway, Liquibase). Adatbázis verziószáma az adatbázisban, egy erre kijelölt táblában. A migrációs szkriptek ez alapján futtatandóak. Futtatható parancssori eszközökkel,
DBA felügyelete mellett, de futtathatja az alkalmazás is.
* Időzónák kezelése, megfontolások:
    * Az adatbázisban tárolt típus lehet `timestamp` vagy `timestamp with time zone`. Ez utóbbi esetén a PostgreSQL mindig UTC-ben tárolja az értéket, csak lekérdezéskor figyelembe veszi a session beállítást. Ezt a következő paranccsal lehet megadni: `SET TIME ZONE 'Europe/Budapest';`.
    * JSON deszerializációkor figyelni kell, hogy melyik időzónába történik az átváltás. Spring és Jackson esetén a `spring.jackson.time-zone` paraméterrel változtatható.
    * A Hibernate-nek is van egy beállítása, hogy melyik időzónába tegye le, Springen belül a következő konfigurációval adható meg: `spring.jpa.properties.hibernate.jdbc.time_zone`
    * Java oldalon mindig ott váltsuk át a megfelelő időzónára, ahol erre szükség van (üzleti logikában, felületen, stb.)
    * A következő típusok jöhetnek szóba: `java.util.Date` (ezt ne használjuk), `LocalDateTime`, `Instant`, `OffsetDateTime`, `ZonedDateTime`. A legjobb összehasonlítás itt található: https://stackoverflow.com/questions/32437550/whats-the-difference-between-instant-and-localdatetime

### Architektúra

#### Háromrétegű architektúrák

* Szabályok: minden réteg csak az alatta lévő réteget veheti igénybe, réteget átugrani nem lehet, réteg felette lévő rétegre nem hivatkozhat,
réteg API-n keresztül kommunikál az alatta lévő réteggel
* Rétegek: prezentációs réteg, üzleti logika réteg, perzisztens réteg
* Data Access Ojbect (DAO) Java EE tervezési minta: perzisztens műveletek kiszervezése, perzisztens rétegben
* DAO előnyei: cserélhető az implementáció, elkülönül az üzleti logika és a perzisztencia
* DAO generálható: Apache DeltaSpike, Spring Data JPA
* Adam Bien nem tartja indokoltnak a használatát, hiszen ott az `EntityManager`, ami a service-ből használható

#### DTO-k

* Java EE tervezési minták könyv első kiadásában _Value Object_, 2. kiadásban lett átnevezve _Transfer Objectté_
* A könyvben maga a _Business Object_ képes előállítani
* Egy Business Object akár több DTO-t is képes előállítani
* Egy Business Object metódus paraméterként akár kaphat is DTO-t, a sok setter helyett
* _Transfer Object Assembler_: újabb Java EE tervezési minta, mely képes létrehozni a DTO-kat
* Mivel a Business Object és a DTO hasonló attribútumokat tartalmaz, jó ötletnek tűnik, hogy öröklődési hierarchia legyen köztük - lehetőleg kerüljük
* DTO-k létrehozására manapság 3rd library-kat használunk:
    * [Dozer](http://dozer.sourceforge.net/) - reflectionnel
    * [ModelMapper](http://modelmapper.org/) - reflectionnel
    * [MapStruct](https://mapstruct.org/) - kódgenerálással működik
    * Itt egy benchmark: https://www.baeldung.com/java-performance-mapping-frameworks
* Update-nél ha a DTO és az Business Object között különbségeket kell keresni, akkor nehéz lehet ezt implementálni (különösen kapcsolatok esetén)
* Adam Bien szerint a _Transfer Object_ és _Transfer Object Assembler_ sokszor felesleges, lehet mozgatni entitásokat is, hiszen detached állapotba kerülnek
* Clean Code CQRS szerint esetleg elválaszthatjuk a lekérdező és módosító funkciókat. Ekkor a lekérdező paraméterek query paraméterek, a visszatérési értékük valamilyen DTO-k. A
módosító műveletek paraméterei pedig command típusó DTO-k. Ezeknek is lehet visszatérési értéke, pl. hogy mit hoztak létre (pl. generált id-t is tartalmazhat).

#### Composite entity

* Amennyiben azt vesszük észre, hogy entitásokban ismétlődnek bizonyos attribútumok csoportok, lehet, hogy érdemes lenne kivezetni egy külön osztályba, ami önmaga nem entitás
* Pl. van egy `Employee` és egy `Site` és mindegyikhez van egy cím, de mindkettőnél az adott táblába, érdemes bevezetni egy `Address` osztályt
* Amennyiben a Business Entity több apróbb osztályból áll össze, akkor az a _Composite entity_
* A JPA ezt támogatja az `@Embeddable` és `@Embedded` annotációk használatával
* Egymásba ágyazhatók, `@Embeddable`-ben lehet `@Embedded`, esetleg lazy-vel betölthető?

#### Value list handler

* Régi Java EE tulajdonságai miatt lapozást úgy kellett implementálni, hogy be kellett tölteni a teljes listát, szerver oldalon, memóriában
tárolni, majd a kliens ettől kérte el a darabkákat
* Ennek neve _Value list handler_
* Adam Bien szerint felesleges, használjunk úgy a lapozást, hogy az adatbázis végezze (`Query.setFirstResult(..)` és `Query.setMaxResults(..)` metódusok)
* Ez modern alkalmazásokban is megoldás lehet arra, hogy amikor a felhasználó lapozni akar, akkor közben módosulhat az adatbázis, tehát alatta a lista
    * Ugyanúgy le lehet menteni, pl. egy elosztott cache-be (megőrizve az állapotfüggetlenséget)
    * Cache-elni lehet: kliens oldalon, szerver oldalon (memóriában), elosztott cache-ben, adatbázisban (temporális táblákkal) - mindegyiknek megvan az előnye és hátránya

#### Domain store

* A _Domain store_ tervezési minta: perzisztencia leválasztása az entitásoktól
* Ezt már nem kell implementálni, a JPA implementációk (pl. Hibernate, EclipseLink) ezt a tervezési mintát valósítják meg

#### Fast Lane Reader

* Régebben amennyiben egy lekérdezést futtattunk, egyszerre sok entitást kellett példányosítanunk és a memóriában tartanunk
* Erre akkor lehet szükség, ha egyszerre akarunk sok entitást betölteni, és azokon műveleteket, számolást végezni
* Erre megoldás a _Fast Lane Reader_ tervezési minta, pl. a JPA-t megkerülve használjunk JDBC-t (fetch size)
* Megoldás lehet, hogy az objektumokat lapozva olvassuk be
* JPA új verziójában már visszakapjuk streamként is

#### Elosztott cache

* A Java EE-ben nincs szabványos cache API (tervezték, de nem sikerült elkészíteni)
* Ezért érdemes külső megoldást használni
* Amennyiben kellően skálázható és hibatűrő megoldást akarunk biztosítani, érdemes elosztott cache-t használni
* Legelterjedtebbek: Ehcache, Hazelcast, Infinispan, Memcached, Coherence, Redis
* Ezt kétféleképp használhatjuk, vagy 3rd party függőségként felvesszük és az alkalmazás része lesz, vagy amennyiben az alkalmazásszerver biztosít ilyet, használhatjuk azt
* A WildFly az Infinispant használja
* Amennyiben az alkalmazásszerverben lévőt használjuk, az a következő jellemzőkkel rendelkezik:
    * Kötődni fogunk az alkalmazásszerverhez, elvesztjük a platformfüggőséget
    * Nem nő meg feleslegesen az EAR fájlunk
    * Ha verziót akarunk frissíteni, lehet, hogy nem tehetjük, mert be hozzá vagyunk ragadva az alkalmazásszerverben lévő verzióhoz
    * Az alkalmazásszerverben lévő cache-t bonyolultabb konfigurálni, és kevesebb dokumentációt, példát találunk hozzá, mint a különállóhoz
    * A dokumentáció szerint az Infinispan a WildFly belső működéséhez szükséges

## Házi feladat

Hozz létre egy `employees` alkalmazást a `bank` könyvtárban lévőhöz hasonlatosan.

Pár architektúrális módosítást próbálj ki:

* Ne használj EJB-ket (segítség lejjebb)!
* `@Produces` és `@Consumes` annotációkat csak osztály szinten használj!

Ez azonban egy darab `Employee` entitást tartalmazzon. Ez azonban tartalmazza
az alkalmazott nevét és anyja nevét is. Azonban külön kell tárolni
mindkét esetben a kereszt- és vezetéknevet (`forename`, `surename`). A táblában ennek
négy oszlopnak kell lennie: `employee_forename`, `employee_surename`, `mother_forename`, `mother_surename`.

Hogy oldanád meg, hogy ne legyen copy-paste (segítség lejjebb). _Composite entity_ mint a kulcs.

Az `EmployeeBean` üzleti logika ne entitásokat adjon vissza, hanem DTO-kat. Valamint commandokat is
várjon paraméterül. Ezt hívja a REST réteg.

```java
public List<EmployeeDto> listEmployees() {        
}

public Employee createEmployee(CreateEmployeeCommand command) {
}
```

Az `EmployeeDto` felépítése megegyezik az `Employee` osztállyal. A `CreateEmployeeCommand` osztály is megegyezik, azzal a különbséggel, hogy
nincs `id` attribútuma.

Implementáld a konvertálást entitás és DTO között MapStruct segítségével! (Vigyázz, a `pom.xml`-ben két `annotationProcessorPaths`-t kell felvenned!)
A `Mapper`-t injektáld (segítség lejjebb)!
A listát is a `Mapper` konvertálja!

Próbáld ki, hogy amikor az entitás és a DTO is ugyanarra a típusra hivatkozik, akkor ott referencia másolás történik-e, vagy deep copy?
Amennyiben NEM deep copy, próbáld megoldani! (Segítség lejjebb.)

### Segítségek

* `DbMigrator` esetén egy `init` metódus `@Observes @Initialized( ApplicationScoped.class )` paraméterrel
* `EmployeeBean` `@SessionBean` helyett `@Named`
* Vegyél fel egy `Name` osztályt, és legyen `@Embeddable`, és két ilyen típusú attribútum legyen
az `Employee` osztályban. Használandó az `@Embedded` és `@AttributeOverrides` annotáció.
* `@Mapper(componentModel = "cdi")`
* Nem deep copy van, ahhoz fel kell venni egy típusról ugyanarra a típusra mapper metódust.
