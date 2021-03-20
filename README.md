# SyncMineskinClient

[![Build Status](http://ci.inventivetalent.org/job/MineskinJavaClient/badge/icon)](https://ci.inventivetalent.org/job/MineskinJavaClient/)
![](https://img.shields.io/github/release/inventivetalentDev/MineskinClient.png)

Synchronous Java Client for [api.mineskin.org](https://mineskin.org). This removes the
yucky [SkinCallback](https://github.com/InventivetalentDev/MineskinClient/blob/master/src/main/java/org/mineskin/data/SkinCallback.java) class
from [MineskinClient](https://github.com/InventivetalentDev/MineskinClient).

Can be used to generate valid texture data from skin image files.  
You can also use [this online tool](https://mineskin.org) to directly generate skin data from images.

The API requires official Minecraft accounts to upload the skin textures.  
If you own a Minecraft account you don't actively use and want to contibute to the API's speed, please [contact me](https://inventivetalent.org/contact)!

## Usage

Install to local .m2 via bash or cmd:

```bash
git clone "https://github.com/RuthlessJailer/SyncMineskinClient/"
cd SyncMineskinClient
mvn clean install -e
```

Then, add this dependency:

**Maven** (*pom.xml*)

```xml

<dependency>
	<groupId>org.mineskin</groupId>
	<artifactId>sync-java-client</artifactId>
	<version>1.0.0-SNAPSHOT</version>
	<scope>provided</scope>
</dependency>
```

**Gradle** (*build.gradle*)

```groovy
api 'org.mineskin:sync-java-client:1.0.0-SNAPSHOT'
```

**Kotlin Gradle** (*build.gradle.kts*)

```kotlin
api("org.mineskin:sync-java-client:1.0.0-SNAPSHOT")
```
