// import org.jetbrains.grammarkit.tasks.GenerateLexer
// import org.jetbrains.grammarkit.tasks.GenerateParser

plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.3"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

group = "com.ferock"
version = "1.0.9"

// Include the generated files in the source set
sourceSets {
    main {
        java {
            srcDirs("src/main/gen")
        }
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }
    maven { url = uri("https://mirrors.cloud.tencent.com/gradle/") }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.3")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))

    // 禁用 instrumentCode 任务
    instrumentCode.set(false)
}

// 配置 grammarkit
grammarKit {
    jflexRelease.set("1.7.0-1")
    grammarKitRelease.set("2022.3.2")
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    patchPluginXml {
        sinceBuild.set("233")  // 2023.3
        untilBuild.set("252.*")  // 支持到 2025.2 版本
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    // 禁用 instrumentCode 任务
    named("instrumentCode") {
        enabled = false
    }

    // 配置测试任务
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = true
        }
    }

    // 配置 grammar kit 生成任务
    generateLexer {
        sourceFile.set(file("src/main/java/com/ferock/classicasp/ClassicASP.flex"))
        targetOutputDir.set(file("src/main/gen/com/ferock/classicasp"))
    }

    generateParser {
        sourceFile.set(file("src/main/java/com/ferock/classicasp/ClassicASP.bnf"))
        targetRootOutputDir.set(file("src/main/gen"))
        pathToParser.set("com/ferock/classicasp/parser/ClassicASPParser.java")
        pathToPsiRoot.set("com/ferock/classicasp/psi")
    }

    // 确保在编译前生成代码
    compileJava {
        dependsOn(generateLexer, generateParser)
    }
}