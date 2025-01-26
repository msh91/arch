import com.squareup.anvil.plugin.AnvilExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AnvilLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.squareup.anvil")
            }

            extensions.configure<AnvilExtension> {
                generateDaggerFactories.set(true)
                disableComponentMerging.set(true)
            }
        }
    }
}