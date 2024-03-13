# weatherapp Android

A weather app that shows daily weather conditions, and weather forecasts; built with `Kotlin` using `MVVM` and `Clean Architecture` design pattern which uses `weatherapi` for weather data.

### Screenshots

<img width="319" alt="Screenshot 2024-03-13 at 2 24 59 PM" src="https://github.com/saad100104006/WeatherApp/assets/8910479/18f0fd5b-936c-45f4-8fd4-478710612239">
<img width="321" alt="Screenshot 2024-03-13 at 2 25 10 PM" src="https://github.com/saad100104006/WeatherApp/assets/8910479/3e1ba3c4-66f8-4581-8ad7-2c0622e5faca">
<img width="319" alt="Screenshot 2024-03-13 at 2 24 31 PM" src="https://github.com/saad100104006/WeatherApp/assets/8910479/4bd7bd7f-171a-44d1-add7-8f983b62b2f2">
<img width="319" alt="Screenshot 2024-03-13 at 2 24 40 PM" src="https://github.com/saad100104006/WeatherApp/assets/8910479/9e724cdf-6b3c-46cf-9436-c5169fe260bf">

### Recording


https://github.com/saad100104006/WeatherApp/assets/8910479/f9243205-da98-41ca-8375-b2051d95fd25



### Technologies

    - MVVM with Clean Architecture for design pattern
    - Hilt for dependency injection
    - Jetpack Compose for UI component design
    - Retrofit & OkHttp for network requests
### API
This project uses API data from [weatherapi.com](https://www.weatherapi.com/)

### Folder Structure
```bash
.
├── README.md
├── app
│   ├── build. gradle
│   ├── proguard-rules.pro
│   └── src
│       ├── androidTest
│       ├── main
│       │   ├── AndroidManifest.xml
│       │   ├── java
│       │   │   └── com
│       │   │       └── app
│       │   │           └── weather
│       │   │               ├── WeatherApplication.kt
│       │   │               ├── data
│       │   │               │   ├── repositories
│       │   │               │   └── source
│       │   │               │       ├── device
│       │   │               │       └── weather
│       │   │               ├── domain
│       │   │               │   ├── models
│       │   │               │   └── usecases
│       │   │               │       ├── device
│       │   │               │       └── weather
│       │   │               ├── infrastructure
│       │   │               │   └── di
│       │   │               └── presentation
│       │   │                   ├── framework
│       │   │                   │   └── theme
│       │   │                   └── screen
│       │   │                       ├── home
│       │   │                       │   └── model
│       │   │                       └── weather_detail
│       │   └── res
│       │       ├── drawable
│       │       ├── values
│       │       └── xml
│       └── test
├── build.gradle
├── gradle
│   └── wrapper
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
└── settings.gradle
```

### Data Flow

<img width="929" alt="261384194-ad496208-1e07-42ac-8c3b-81a33d3394fc" src="https://github.com/saad100104006/WeatherApp/assets/8910479/23d864f0-019a-4c9a-9ee2-75520286a11a">


### Setup Instruction
You can setup/run the project in two ways - either by downloading the project and compiling locally using Android Studio or by downloading this apk from google drive
https://drive.google.com/file/d/10eo_hsixdD8_UCNDX_RUW0ps-nnB2CFI/view?usp=sharing


#### Run the project locally

> Make sure you have all the local dependencies setup i.e Android Studio & the Android SDK, [check here](https://developer.android.com/studio/install)
> The Android Studio Version Android Studio Hedgehog | 2023.1.1 Patch 2 has been used to build this project
> JDK version 17.0.7 has been used
> After install if you face issues in fetching weather data, please change the API_KEY from WeatherDataSource, genarate your own token from weatherapi and replace the existing token with your token.

- First off, `git clone` this project
    - `git clone https://github.com/bijaykumarpun/weather-app-clean-architecture`
- Open the project in `Android Studio`pointing to the root folder's `build.gradle` file
- After successfully building the project, run `./gradlew installDebug` in the root project directory to install in any of the available device/emulator
