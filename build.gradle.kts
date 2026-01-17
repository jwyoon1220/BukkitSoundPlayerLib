import java.time.LocalDate

plugins {
    kotlin("jvm") version "2.2.21"
    id("com.vanniktech.maven.publish") version "0.35.0"
}
group = "io.github.jwyoon1220"
version = "0.0.1"
val gg = group.toString()
val vv: String = version.toString()
repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-server-core:3.3.3")
    implementation("io.ktor:ktor-server-websockets:3.3.3")
    // Source: https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    implementation("ch.qos.logback:logback-classic:1.5.24")
    //implementation("io.ktor:ktor-server-websockets")
    implementation("io.ktor:ktor-server-netty:3.3.3")
}
mavenPublishing {
    coordinates(
        groupId = gg,
        artifactId = rootProject.name,
        version = vv
    )
    pom {
        name.set(rootProject.name) // Project Name
        description.set("WebSocket library for BukkitSoundPlayer Mod.") // Project Description
        inceptionYear.set(LocalDate.now().year.toString()) // 개시년도
        url.set("https://github.com/jwyoon1220/BukkitSoundPlayerLib") // Project URL

        licenses { // License Information
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers { // Developer Information
            developer {
                id.set("jwyoon1220")
                name.set("Junwon Yoon")
                email.set("jwyoon1220@naver.com")
            }
        }

        scm { // SCM Information
            connection.set("scm:git:git://github.com/cares0/rest-docs-kdsl.git")
            developerConnection.set("scm:git:ssh://github.com/jwyoon1220/BukkitSoundPlayerLib")
            url.set("https://github.com/jwyoon1220/BukkitSoundPlayerLib.git")
        }
    }
    publishToMavenCentral()

    signAllPublications() // GPG/PGP 서명
}

kotlin {
    jvmToolchain(21)
}
tasks.register("testTask") {
    println("This is a test task. ${gg}, ${vv}")
}

tasks.test {
    useJUnitPlatform()
}