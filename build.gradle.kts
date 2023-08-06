import korlibs.korge.gradle.*

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.korge)
}

korge {
	id = "io.github.chomade"

// To enable all targets at once

	//targetAll()

// To enable targets based on properties/environment variables
	//targetDefault()

// To selectively enable targets
	
	targetJvm()
	targetJs()
	targetDesktop()
	targetIos()
	targetAndroid()

	serializationJson()
}


dependencies {
    add("commonMainApi", project(":deps"))
    add("commonMainApi", "io.insert-koin:koin-core:3.4.3")
    //add("commonMainApi", project(":korge-dragonbones"))
}

