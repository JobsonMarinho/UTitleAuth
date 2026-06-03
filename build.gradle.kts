import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.net.HttpURLConnection
import java.net.URI

plugins {
    java
    // https://github.com/GradleUp/shadow
    id("com.gradleup.shadow") version "9.2.1"
    // https://github.com/eldoriarpg/plugin-yml
    id("de.eldoria.plugin-yml.bukkit") version "0.9.0"
}

group = "com.undeadlydev"
version = "8.3.7"

bukkit {
    name = "UTitleAuth"
    main = "com.undeadlydev.UTA.UTitleAuth"
    version = project.version.toString()
    apiVersion = "1.13"
    author = "UnDeadlyDev"
    depend = listOf("AuthMe")
    softDepend = listOf("PlaceholderAPI", "CMILib", "FastLogin")
}

repositories {
    mavenLocal()
    // mavenCentral()
    // Instead of real maven central we use the maven mirror Paper uses, to ensure all the dependencies we want will
    // also be available to the library loader.
    maven { url = uri("https://maven-central.storage-download.googleapis.com/maven2") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/groups/public/") }
    maven { url = uri("https://maven.enginehub.org/repo/") }
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
    maven { url = uri("https://repo.codemc.io/repository/maven-snapshots/") }
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://repo.tcoded.com/releases") }
}

dependencies {
    // https://hub.spigotmc.org/stash/projects/SPIGOT/repos/spigot/browse
    compileOnly("org.spigotmc:spigot-api:1.21.11-R0.1-SNAPSHOT")

    // https://github.com/PlaceholderAPI/PlaceholderAPI
    compileOnly("me.clip:placeholderapi:2.12.2")

    compileOnly("fr.xephi:authme:5.6.1-SNAPSHOT")

    // https://projectlombok.org
    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")

    // https://github.com/google/guice
    implementation("com.google.inject:guice:7.0.0")

    // https://github.com/Bastian/bstats-metrics
    implementation("org.bstats:bstats-bukkit:3.1.0")

    // https://github.com/CryptoMorin/XSeries
    // 13.7.0 adds support for the year-based MC scheme (v26+, e.g. 26.1.2)
    implementation("com.github.cryptomorin:XSeries:13.7.0")

    // https://github.com/TechnicallyCoded/FoliaLib
    implementation("com.tcoded:FoliaLib:0.5.1")

    compileOnly(files("libs/FastLoginBukkit.jar"))
    compileOnly(files("libs/CMILib1.5.6.9.jar"))
}

tasks.named<ShadowJar>("shadowJar") {
    archiveFileName.set("${rootProject.name}-${version}.jar")

    val path = "com.undeadlydev.UTA.libraries"

    relocate("org.bstats", "$path.bstats")
    relocate("com.cryptomorin.xseries", "$path.xseries")
    relocate("com.tcoded", "$path.tcoded")

    // Guice + its transitive deps (Guava, jakarta.inject, aopalliance) must be relocated
    // so they never clash with the versions the server already ships.
    relocate("com.google.inject", "$path.guice")
    relocate("com.google.common", "$path.guava")
    relocate("com.google.thirdparty", "$path.guava_thirdparty")
    relocate("jakarta.inject", "$path.jakarta_inject")
    relocate("org.aopalliance", "$path.aopalliance")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Xlint:deprecation"))
}

tasks.register("validateLibraryLoaderDeps") {
    doLast {
        val repoUrl = "https://maven-central.storage-download.googleapis.com/maven2"
        val configToCheck = configurations.getByName("library")

        val externalDeps = configToCheck.allDependencies
            .filterIsInstance<ExternalModuleDependency>()
            .filter { it.version != null }

        val missing = mutableListOf<String>()

        externalDeps.forEach { dep ->
            val groupPath = dep.group!!.replace('.', '/')
            val fullUrl = "$repoUrl/$groupPath/${dep.name}/${dep.version}/${dep.name}-${dep.version}.pom"
            val gav = "${dep.group}:${dep.name}:${dep.version}"

            try {
                val connection = URI(fullUrl).toURL().openConnection() as HttpURLConnection
                connection.requestMethod = "HEAD"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                if (connection.responseCode == 200) {
                    println("Found: $gav")
                } else {
                    println("Missing: $gav (HTTP ${connection.responseCode})")
                    missing.add(fullUrl)
                }
            } catch (e: Exception) {
                println("Error checking $gav - ${e.message}")
                missing.add(fullUrl)
            }
        }

        if (missing.isNotEmpty()) {
            println("The following dependencies were not found:")
            missing.forEach { println(" - $it") }
            throw GradleException("Some dependencies were not found in the repository.")
        } else {
            println("All dependencies found in $repoUrl.")
        }
    }
}

tasks.processResources {
    inputs.property("version", project.version)
}
