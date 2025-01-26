# Convention Plugins

The `build-logic` folder contains project-specific convention plugins for common
module configurations. Convention plugins help keep the build script configuration
DRY and avoid the need for duplicated configuration or complex `subproject`
configurations.

The `convention` module inside `build-logic` defines a set of plugins that all
normal modules can apply to configure themselves.