plugins {
	id("java")
	id("io.papermc.paperweight.userdev") version "1.7.7"
	id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "org.mockbukkit"
version = "1.2-SNAPSHOT"

repositories {
	mavenCentral();
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	paperweight.paperDevBundle("1.21.3-R0.1-SNAPSHOT") // TODO: paperweight does not work on latest version yet.
}

tasks {
	compileJava {
		options.encoding = Charsets.UTF_8.name()
		options.release.set(21)
	}

	processResources {
		filesMatching("**/plugin.yml") { expand(project.properties) }
	}

	runServer {
		minecraftVersion("1.21.4")
	}
}
