plugins {
    id("java")
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))

    implementation("org.flywaydb:flyway-core:11.5.0")
    implementation("org.flywaydb:flyway-database-postgresql:11.5.0")
    runtimeOnly("org.postgresql:postgresql:42.7.5")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.bootJar {
    archiveFileName.set("db.jar")
    mainClass.set("com.noahhendrickson.db.MigrationRunner")
}

tasks.jar {
    enabled = false
}