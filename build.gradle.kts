import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging

plugins {
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    id("nu.studer.jooq") version "8.2"
//    id("org.flywaydb.flyway") version "5.2.4" // stackoverflow
    id("org.flywaydb.flyway") version "9.21.1" // https://plugins.gradle.org/plugin/org.flywaydb.flyway
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.flywaydb:flyway-core") // added later
    runtimeOnly("org.postgresql:postgresql")
    jooqGenerator("org.postgresql:postgresql:42.5.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5") // jwt
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5") // jwt

}

//jdbc:postgresql://${{db_host}}:${{db_port}}/${{your_db_name}}?sslmode=disable&amp;


jooq {
    configurations {
        create("main") {  // name of the jOOQ configuration

            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/loungy"
                    user = "postgres"
                    password = "postgres"
                }
            }
        }
    }
}

flyway {
    url = "jdbc:postgresql://localhost:5432/loungy"
    user = "postgres"
    password = "postgres"
//    locations = ['classpath:db/migration']
}

buildscript {
    configurations["classpath"].resolutionStrategy.eachDependency {
        if (requested.group == "org.jooq") {
            useVersion("3.17.3")
        }
    }
}
apply(plugin = "org.flywaydb.flyway")


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") { allInputsDeclared.set(true) }

// by default, generateJooq will use the configured java toolchain, if any
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
    // generateJooq can be configured to use a different/specific toolchain
    (launcher::set)(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(17))
    })
}