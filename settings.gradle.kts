pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "editor-oss"
include(":app")
include(":core:model", ":core:geometry", ":core:history", ":core:editor", ":core:common")
include(":rendering:renderer", ":rendering:opengl")
include(":image:decoder", ":image:processor", ":image:exporter")
include(":data:project", ":data:media")
include(":feature:editor")
