plugins {
    id("java")
}

allprojects {
    group = "com.noahhendrickson"
    version = "0.1.0-alpha.1"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

subprojects {
    apply(plugin = "java")
    
    repositories {
        mavenCentral()
    }
    
    dependencies {
        implementation("ch.qos.logback:logback-classic:1.5.18")
        implementation("ch.qos.logback:logback-core:1.5.18")
    }
    
    tasks.test {
        useJUnitPlatform()
    }
}

tasks.test {
    useJUnitPlatform()
}