# chargerevolutionapp

*Ha segítség kell a futtatással, értelmezéssel kapcsolatban, keressetek a Coospacen!*

## Útmutató

### Regisztráció:
1. Nyitó oldal -> "Regisztráció" gomb
2. Töltsd ki az adatokat (az utolsó, connector type mező automatikusan kitöltődik)
3. Az összes CRUD funkció teszteléséhez kell az Admin checkbox bepipálása
4. "Regisztráció" gomb
5. Home screen fog megjelenni

### Emailes bejelentkezés:
1. Nyitó oldal 
2. Email cím és jelszó kitöltése
3. "Bejelentkezés" gomb
4. Home screen fog megjelenni

### Google Bejelentkezés:
1. Nyitó oldal
2. (email/jelszó kitöltése nem szükséges) 
3. "Google Bejelentkezés" gomb 
4. Home screen fog megjelenni

### Töltők listázása
1. Bejelentkezés -> Home oldal
2. "Töltők listája" gomb
3. Töltők görgethető listája fog megjelenni
4. (FONTOS: a lista a regisztrációnál megadott EV connector típus alapján van szűrve!)

### Felhasználó profiljának (EV típus) módosítása
1. Bejelentkezés -> Home oldal
2. "Profil szerkesztése" gomb
3. A spinnerből az EV típus kiválasztása (a connector type mező automatikusan kitöltődik)
4. "Mentés" gomb

### Egy töltő adatainak megnézése:
1. Bejelentkezés -> Home oldal
2. "Töltők listája" gomb
3. Bármelyik töltőnél a "Megnézem" gomb
4. A töltő adatai megjelennek

### Egy töltés elindítása/leállítása:
1. Bejelentkezés -> Home oldal
2. "Töltők listája" gomb
3. Bármelyik töltőnél a "Megnézem" gomb
4. "Töltök most" gomb
5. "OK" gomb
6. A végén "Töltés befejezése"

### Egy töltés lefoglalása + notification + alarm:
1. Bejelentkezés -> Home oldal
2. "Töltők listája" gomb
3. Bármelyik töltőnél a "Megnézem" gomb
4. "Lefoglalom 10 percre" gomb
5. Értesítés a lefoglalásról
6. 10 mp múlva újabb értesítés a foglalás lejártáról
7. Opcionális: "Foglalás törlése" gomb -> ilyenkor az értesítések eltűnnek


### QRkód olvasás:
1. Menj fel egy QR kód generáló oldalra (pl. https://www.the-qrcode-generator.com/).
2. Ha szükséges, válaszd ki, hogy sima szövegből ("FREE TEXT") csinálja a kódot.
3. Írd be az egyik, adatbázisban lévő töltő nevét (pl. Revolutioncharger1) és ebból generálj egy QR kódot.
4. Az appba való bejelentkezés után válaszd a "QR kód olvasás" opciót és olvasd be a QR kódot.
5. Az app a töltés megkezdése screenre fog vinni.


### Új töltő rögzítése az adatbázisban (CRUD)
1. Bejelentkezés -> Home oldal
2. "Töltők listája" gomb
3. Legfelül az "Új töltő rögzítése (Admin)" gomb
4. Adatok kitöltése
5. "Mentés" gomb

### Egy töltő adatainak módosítása az adatbázisban  (CRUD)
1. Bejelentkezés -> Home oldal
2. "Töltők listája" gomb
3. Bármelyik töltőnél a "Megnézem" gomb
4. Legfelül az "Töltő adatainak módosítása (Admin)" gomb
5. Adatok módosítása
6. "Mentés" gomb


### Egy töltő törlése az adatbázisból  (CRUD)
1. Bejelentkezés -> Home oldal
2. "Töltők listája" gomb
3. Bármelyik töltőnél a "Megnézem" gomb
4. Legfelül az "Töltő törlése az adatbázisból (Admin)" gomb


 
