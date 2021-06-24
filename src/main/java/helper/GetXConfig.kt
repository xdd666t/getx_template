package helper

// set default value
object GetXConfig {
    //default true: use high mode
    const val defaultMode = true

    //default true
    const val useFolder = true

    //default false
    const val usePrefix = false

    //auto dispose GetXController
    const val autoDispose = false

    //add Lifecycle
    const val addLifecycle = false

    //add binding
    const val addBinding = false

    //Logical layer name
    const val logicName = "Logic"

    //view layer name
    const val viewName = "Page"
    const val viewFileName = "View"

    //state layer name
    const val stateName = "State"

    //model name
    const val defaultModelName = "Default"
    const val easyModelName = "Easy"
}