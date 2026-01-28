Aplikacja mobilna służąca do sprawdzania pogody w czasie rzeczywistym, wykorzystująca lokalizację użytkownika oraz zewnętrzne API pogodowe. Projekt umożliwia również zarządzanie listą ulubionych miast dzięki lokalnej bazie danych.

## Opis Aplikacji:
Aplikacja pozwala użytkownikowi na szybkie sprawdzenie warunków atmosferycznych (temperatura, opis pogody, wilgotność, ciśnienie) dla bieżącej lokalizacji lub dowolnego miasta na świecie. Użytkownik może dodawać wybrane miasta do listy "Ulubionych", która jest przechowywana w pamięci urządzenia, co pozwala na szybki dostęp do danych bez konieczności ponownego wyszukiwania.

## 🛠️ Zastosowane Technologie

Projekt został zrealizowany w języku **Kotlin**:

* **Interfejs (UI):** Jetpack Compose.
* **Architektura:** MVVM.
* **Nawigacja:** Jetpack Navigation Compose.
* **Komunikacja sieciowa:** Retrofit 2 + Gson Converter.
* **Baza danych:** Room Database.
* **Asynchroniczność:** Kotlin Coroutines + Flow.

##  Wykorzystane Sensory i Funkcje Systemowe

Zgodnie z wymaganiami projektowymi, aplikacja wykorzystuje następujące źródła danych i sensory:

1.  **Moduł GPS (Location Services):**
    * Aplikacja prosi o uprawnienia `ACCESS_FINE_LOCATION`.
    * Pobiera dokładne współrzędne geograficzne (szerokość i długość) urządzenia.
    * Automatycznie wysyła zapytanie do API o pogodę dla miejsca pobytu użytkownika.

2.  **Połączenie Sieciowe (Internet):**
    * Komunikacja z REST API (OpenWeatherMap).
    * Obsługa błędów połączenia i stanów ładowania.

3.  **Baza Danych (Storage):**
    * Trwałe zapisywanie danych w pamięci wewnętrznej telefonu przy użyciu biblioteki Room (SQLite).

##  Funkcjonalności

* **Pogoda lokalna:** Automatyczne wykrywanie pogody po uruchomieniu aplikacji.
* **Wyszukiwarka:** Możliwość wpisania dowolnego miasta i pobrania danych.
* **Ulubione:** Dodawanie miast do listy ulubionych (ikona serca).
* **Zarządzanie:** Przeglądanie listy zapisanych miast i usuwanie ich (ikona kosza).
* **Nawigacja:** Płynne przechodzenie między ekranem głównym a listą ulubionych.

## Zrzuty Ekranu

| Ekran Główny | Wyszukiwanie i Ulubione | 
|:---:|:---:|:---:|
| ![Main Screen](screenshots/screen1.png) | ![Search](screenshots/screen2.png) |



---
**Autor:** Maciej Michalak
**Nr albumu:** 122381
**Przedmiot:** Programowanie aplikacji mobilnych
