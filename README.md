#Makhana

Makhana is a multiplayer game framework built upon jMonkeyEngine 3.

##Dependencies
Building of this project relies on Gradle 1.10+ and JDK 1.8+
###External Dependencies
The following are fetched via the gradle build script.
####Common Project (Used by Client, Gameserver, & Master)
* jMonkeyEngine 3.0.+

* Apache Commons - CLI 1.2
* Apache Commons - Collections 3.2
* Apache Commons - IO 2.4
* Apache Commons - Lang 3.3.2
* Eventbus 1.4
* Google Guava 17.0
* Groovy Language - All 2.3.+
* JBCrypt 0.3m
* Simple Logging Facade for Java (SLF4J) 1.7.7

####Master
* Mysql Connector 5.1.30
* Spring - Boot 1.0.2-RELEASE
* Spring - Context 4.04-RELEASE
* Spring - Core 4.04-RELEASE
* Spring - JDBC 4.04-RELEASE

###Included Dependencies
The following are included in the repository as they aren't available via maven/gradle.

* Lemur
* LemurProto
* Zay-ES (Entity System)
* Zay-ES-Net (Entity System Networking Extension)