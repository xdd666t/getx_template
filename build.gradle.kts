import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "2.3.20"
  id("org.jetbrains.intellij.platform") version "2.18.1"
  id("org.jetbrains.changelog") version "2.5.0"
}

group = "com.xdd"
version = "3.6.0"

repositories {
  mavenCentral()
  intellijPlatform {
    defaultRepositories()
  }
}

dependencies {
  intellijPlatform {
    intellijIdeaCommunity("2023.3.8")
    testFramework(TestFrameworkType.Platform)
  }

  testImplementation(kotlin("test"))
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(25))
  }
}

kotlin {
  jvmToolchain(25)
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
  }
}

intellijPlatform {
  instrumentCode = false

  pluginConfiguration {
    id = "com.tao.getx"
    version = project.version.toString()
    description = markdownToHTML(File(rootDir, "pluginDescription.md").readText())
    changeNotes = markdownToHTML(File(rootDir, "changeNotes.md").readText())

    ideaVersion {
      sinceBuild = "233"
      untilBuild = provider { null }
    }
  }

  pluginVerification {
    ides {
      create(IntelliJPlatformType.IntellijIdeaCommunity, "2023.3.8")
    }
  }
}

tasks {
  withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
    options.release.set(17)
  }

  test {
    useJUnit()
  }

  withType<KotlinJvmCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
  }

  named("buildSearchableOptions") {
    enabled = false
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
