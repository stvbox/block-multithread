plugins {
    java
    id("org.springframework.boot") version "3.1.+"
    id("io.spring.dependency-management") version "1.1.+"
}

group = "foo.bar"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    //implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework:spring-webflux")
    implementation("org.eclipse.jetty.http2:http2-client:11.0.20")
    
    implementation("com.google.guava:guava:33.0.0-jre")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootBuildImage {
    builder.set("paketobuildpacks/builder-jammy-base:latest")
}
