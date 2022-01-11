# Freeturilo Frontend

This solution provides the Freeturilo Mobile App: a user interface for users and administrators of the Freeturilo system.

## Download APK
You can download the latest release of the Freeturilo Mobile App [_here_](https://github.com/Freeturilo/FreeturiloFrontend/releases/latest).

## Development details
 - IDE: Android Studio Arctic Fox (2020.3.1)
 - Language: Java 8
 - SDK: Android 31

## Build the solution
1. Clone the repository
2. Open the solution in Android Studio
3. Create a _local.properties_ file with the following content:
    ```INI
    sdk.dir=#<Path to your Android SDK directory>
    MAPS_API_KEY=#<Your key to Google Maps API>
    FREETURILO_API_URL=freeturilowebapi.azurewebsites.net #<Or any URL you wish to connect to for backend>
    ```
4. Create an _app/src/androidTest/resources/test.properties_ file with the following content: (_if you are not an admin, skip this step_)
    ```INI
    ADMIN_EMAIL=#<Email address for your admin account>
    ADMIN_PASSWORD=#<Password for your admin account>
    ```
5. Build the solution

## Test the solution

Unit tests are performed locally. To run unit tests execute `gradlew test` in the Android Studio Terminal. Reports are generated to _app\build\reports\tests_.

UI tests are performed on target device. Some of them will fail without administrative access to the Freeturilo system.
Before testing, enable USB debugging in settings of the target device and connect it to the host with USB.
To run UI tests execute `gradlew connectedAndroidTest` in the Android Studio Terminal. Reports are generated to _app\build\reports\androidTests_.

## Read the documentation

Documentation of the source code of Freeturilo Mobile App can be found [_here_](https://freeturilo.github.io/FreeturiloFrontend/).
