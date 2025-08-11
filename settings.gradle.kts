pluginManagement {
    repositories {
        maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }
        maven { url = uri("https://mirrors.cloud.tencent.com/gradle/") }
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "jetbrains_classic_asp_plugin"