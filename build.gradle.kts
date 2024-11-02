import org.jetbrains.changelog.markdownToHTML

plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.9.23"
  id("org.jetbrains.intellij") version "1.17.2"
  // https://github.com/JetBrains/gradle-changelog-plugin
  id("org.jetbrains.changelog") version "2.2.1"
}

group = "com.xdd"

repositories {
  mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  version.set("2023.2.5")
  type.set("IC") // Target IDE Platform

  plugins.set(listOf(/* Plugin Dependencies */))
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("com.fleshgrinder.kotlin:case-format:0.2.0")
}

tasks {
  // Set the JVM compatibility versions
  withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
  }
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
  }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  publishPlugin {
    token.set(System.getenv("PUBLISH_TOKEN"))
  }
}

version = "3.5.1"
tasks.patchPluginXml {
  sinceBuild.set("200")
  untilBuild.set("")
  pluginId.set("com.tao.getx")
  pluginDescription.set(markdownToHTML(File(rootDir, "pluginDescription.md").readText()))
  changeNotes.set(markdownToHTML(File(rootDir, "changeNotes.md").readText()))
}
