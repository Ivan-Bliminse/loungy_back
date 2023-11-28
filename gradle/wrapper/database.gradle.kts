import nu.studer.gradle.jooq.JooqExtension
import nu.studer.gradle.jooq.JooqGenerate
import nu.studer.gradle.jooq.JooqPlugin
import org.flywaydb.gradle.FlywayExtension
import org.flywaydb.gradle.FlywayPlugin
import org.flywaydb.gradle.task.FlywayMigrateTask
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.GeneratedAnnotationType.JAVAX_ANNOTATION_GENERATED
import org.jooq.meta.jaxb.Logging.WARN

buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("gradle.plugin.org.flywaydb:gradle-plugin-publishing:7.12.1")
        classpath("nu.studer:gradle-jooq-plugin:6.0")
    }
}

apply<FlywayPlugin>()
apply<JooqPlugin>()

val jooqGenerator by configurations

dependencies {
    jooqGenerator("org.postgresql:postgresql:42.6.0")
}

val dbUsername = System.getProperty("app.spring.datasource.username") ?: "postgres"
val dbPassword = System.getProperty("app.spring.datasource.password") ?: "postgres"
val datasourceUrl = System.getProperty("app.spring.datasource.url")
    ?: "jdbc:postgresql://localhost:5432/loungy"

val jooqSources = "$buildDir/generated/source/jooq/main"

the<SourceSetContainer>()["main"].java.srcDir(jooqSources)

configure<FlywayExtension> {
    url = datasourceUrl
    user = dbUsername
    password = dbPassword
    schemas = arrayOf("public", "backups")
    configurations = arrayOf("jooqGenerator")
    cleanOnValidationError = true
    ignoreMissingMigrations = true
}

configure<JooqExtension> {
    version.set("3.18.5")
    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = WARN
                jdbc.apply {
                    url = datasourceUrl
                    user = dbUsername
                    password = dbPassword
                }
                generator.apply {
                    // TODO uncomment after Jooq 3.15
                    // name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        inputSchema = "public"
                        excludes = listOf(
                            "flyway_schema_history",
                            "crosstab.*",
                            "connectby",
                            "normal_rand",
                            "tablefunc.*",
                            "removeme.*",
                        ).joinToString("|")
                        isIncludeExcludeColumns = true
                        forcedTypes.add(
                            ForcedType()
                                .withName("text")
                                .withIncludeExpression(".*")
                                .withIncludeTypes("TSVECTOR")
                        )
                    }
                    generate.apply {
                        generatedAnnotationType = JAVAX_ANNOTATION_GENERATED
                        isDeprecated = false
                        isGlobalRoutineReferences = false
                        isGlobalSequenceReferences = false
                        isGlobalTableReferences = false
                        isGlobalUDTReferences = false
                        isUdts = true
                        isJavaTimeTypes = true
                        isRecords = true
                        isImmutablePojos = true
                        isValidationAnnotations = true
                    }
                    target.apply {
                        directory = jooqSources
                    }
                }
            }
        }
    }
}

val flywayMigrate by tasks.existing(FlywayMigrateTask::class)
tasks.named<JooqGenerate>("generateJooq") {
    dependsOn(flywayMigrate)
    allInputsDeclared.set(true)
}
