# Weather SDK (Java)

A Java SDK for accessing weather information from [OpenWeatherAPI](https://openweathermap.org/api).  
Designed for simplicity, performance, and safe use in applications, with caching, polling, and single-instance management per API key.

---

## Features

- Retrieve **current weather** for a city.
- Cache weather for up to **10 cities** in memory.
- Supports **on-demand** and **polling** modes.
- Prevents creating multiple SDK instances with the same API key.
- Configurable **poll interval** and **cache TTL** via system properties or environment variables.
- Returns **typed POJOs** for convenient access, with optional JSON output.

---

## Installation

Follow these steps to install and use the Weather SDK in your Java project.

---

### 1. Clone the Repository
First, clone the repository from GitHub:

```bash
git clone https://github.com/AkylbekkyzyAsel/weather-sdk.git
cd weather-sdk
```

### 2. Build the SDK JAR
**Using Maven:**
```bash
mvn clean package
```
This will generate a JAR file in the target/ directory, e.g.:
```bash
target/weather-sdk-1.0.0-SNAPSHOT.jar
```
**Using Gradle:**
```bash
./gradlew clean build
```
This will generate a JAR file in the build/libs/ directory, e.g.:
```bash
build/libs/weather-sdk-1.0.0-SNAPSHOT.jar
```

### 3. Install the JAR into your local Maven repository (Recommended)

This makes the SDK available for both Maven and Gradle projects:
```bash
mvn install:install-file \
  -Dfile=path/to/weather-sdk-1.0.0-SNAPSHOT.jar \
  -DgroupId=test.kameleoon \
  -DartifactId=weather-sdk \
  -Dversion=1.0.0-SNAPSHOT \
  -Dpackaging=jar
```
Replace path/to/ with the actual path to your JAR.
### 4. Add the SDK as a Dependency
- Maven
```xml
<dependency>
    <groupId>test.kameleoon</groupId>
    <artifactId>weather-sdk</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
- Gradle
```bash
dependencies {
    implementation 'test.kameleoon:weather-sdk:1.0.0-SNAPSHOT'
}
```
Gradle will automatically find the JAR in your local Maven repository (~/.m2/repository).

### 6. Optional: Use JitPack (GitHub Hosted)
You can use the GitHub repository directly via JitPack
:

1. Add JitPack repository:

- Gradle:
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
- Maven:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
2. Add the SDK dependency:

- Gradle:
```bash
dependencies {
    implementation 'com.github.AkylbekkyzyAsel:weather-sdk:main-SNAPSHOT'
}
```
- Maven:
```bash
<dependency>
    <groupId>com.github.AkylbekkyzyAsel</groupId>
    <artifactId>weather-sdk</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```
---

## Initialization

**1. Create a WeatherSdk instance using the registry:**

```java
WeatherSdk sdk = WeatherSdkRegistry.getInstance(
    "YOUR_API_KEY",            // OpenWeatherAPI key
    WeatherSdk.Mode.ON_DEMAND  // Mode: ON_DEMAND or POLLING
);
```
**2. Retrieve Weather Data**

**Using POJOs:**
```java
WeatherResponse weather = sdk.getWeather("London");
WeatherResponse.Temperature temp = weather.getTemperature();
System.out.println("Feels like: " + temp.getFeelsLike());
```
**Raw JSON:**
```java
String json = sdk.getWeatherJson("New York");
System.out.println(json);
```
**3. Polling Mode (Optional)**
```java
WeatherSdk sdk = WeatherSdkRegistry.getInstance(
    "YOUR_API_KEY",
    WeatherSdk.Mode.POLLING
);
WeatherResponse weather = sdk.getWeather("Tokyo");
System.out.println(weather.getWeather().getDescription());
```
In polling mode, the SDK refreshes cached data in the background.
On-demand TTL is still respected.

**Optional SDK Configuration**

You can configure polling interval, cache TTL, and units directly through the WeatherSdk instance:
```java
WeatherSdk sdk = WeatherSdkRegistry.getInstance(apiKey, SdkMode.POLLING);

// Optional configuration
sdk.setUnits(WeatherUnit.METRIC);     // Default: METRIC
sdk.setPollInterval(300_000);         // Polling interval in ms (e.g., 5 minutes)
sdk.setCacheTtl(600_000);             // Cache TTL in ms (e.g., 10 minutes)
```
If you do not set these values manually, the SDK will use its built-in defaults.


**Error Handling**
```java
try {
    WeatherResponse weather = sdk.getWeather("InvalidCity");
} catch (WeatherSdkException e) {
    System.err.println(e.getMessage()); // e.g., "City not found"
}

```
    
    
