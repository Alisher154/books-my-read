val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
    id ("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "texnopos.uz"
version = "0.0.1"
application {
    mainClass.set("texnopos.uz.ApplicationKt")
}

tasks.create("stage"){
    dependsOn("installDist")

}
tasks{
    shadowJar{
        manifest {
            attributes(Pair("Main-Class", "com.example.ApplicationKt"))
        }
    }
}



repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")

    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")

    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.ktorm:ktorm-core:3.4.1")
    implementation("mysql:mysql-connector-java:8.0.27")
    implementation("org.mindrot:jbcrypt:0.4")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}