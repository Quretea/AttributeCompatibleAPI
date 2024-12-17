plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

subprojects {

    group = "com.teaman"
    version = "1.3.0"

    apply(plugin = "java-library")

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
            name = "spigotmc-repo"
        }
        maven("https://oss.sonatype.org/content/groups/public/") {
            name = "sonatype"
        }
    }

    dependencies {
        compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
        compileOnly("org.jetbrains:annotations:24.0.1")
    }

    val targetJavaVersion = 8

    java {
        val javaVersion = JavaVersion.toVersion(targetJavaVersion)
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        if (JavaVersion.current() < javaVersion) {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
            }
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release.set(targetJavaVersion)
        }
    }

    tasks.processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveClassifier.set("dev")
    from(subprojects.flatMap { it.sourceSets.main.get().output })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    dependsOn(subprojects.flatMap { it.tasks.matching { it.name == "build" } })
}

tasks.assemble {
    dependsOn("shadowJar")
}