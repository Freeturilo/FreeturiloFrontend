# Freeturilo Frontend

## Development
 - IDE: Android Studio Arctic Fox (2020.3.1)
 - Language: Java 8
 - SDK: Android 31

## Build
1. Clone the repository
2. Open the solution in Android Studio
3. Read chapter `Module` and follow the steps
4. Build the solution

## Tests

Unit tests are performed locally. To run unit tests execute `gradlew test` in the Android Studio Terminal. Reports are generated to `app\build\reports\tests`.

UI tests are performed on target device. Some of them will fail without administrative access to the Freeturilo system (see: `Module`).
Before testing, enable USB debugging in settings of the target device and connect it to the host with USB.
To run UI tests execute `gradlew connectedAndroidTest` in the Android Studio Terminal. Reports are generated to `app\build\reports\androidTests`.

## Module: Freeturilo Mobile App
 - Purpose
    
    Module provides user interface for users and administrators of the Freeturilo system.

 - Pre-build

    You need to create two files:

    - `local.properties` with the following content:
    ```INI
    sdk.dir=#<Path to your Android SDK directory>
    MAPS_API_KEY=#<Your key to Google Maps API>
    FREETURILO_API_URL=freeturilowebapi.azurewebsites.net #<Or any URL you wish to connect to for backend>
    ```
    - `app/src/androidTest/resources/test.properties` with the following content: (_if you don't have administrative access, skip this step_)
    ```INI
    ADMIN_EMAIL=#<Email address for your admin account>
    ADMIN_PASSWORD=#<Password for your admin account>
    ```
 - Usage

    Enable USB debugging in settings of the target device and connect it to the host with USB. Launch the application remotely with Android Studio.
