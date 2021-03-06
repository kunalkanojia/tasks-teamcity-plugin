
# Tasks TeamCity plugin

This [TeamCity] plugin provides a new build runner, which will scan the
files in your project and build a report with tasks's based on that.

### Kotlin Fork of Todos plugin 

Original plugin - [TODOs Plugin](https://github.com/rvdginste/todo-teamcity-plugin/blob/master/README.md)

This is my way to learn Kotlin and build a teamcity plugin while learning it.

Additional features over todos plugin(Apart form being in awesome kotlin):

1. Sensible defaults for inputs.

2. Option to mark the build as failed when critical tasks are found.

3. Task count statistics reported to team city directly and can be viewed in the graph.

4. Cleaner results page.

## Getting started

### Installing a release

Installing the plugin is done the same way as installing another
TeamCity plugin.  Everything needed by the plugin is contained in the
zip package that is built for every release.  This zip must be
installed on the TeamCity server in the 'plugins' folder under the
'TeamCity Data Directory'.

More information can be found on the [TeamCity Documentation Site]
from [JetBrains].


### Build your own

Building your own version of the plugin is not difficult if you have
experience with Maven.  Using maven, the plugin can be built as
follows:

    mvn clean package


This will compile all submodules and package everything in a zip,
which is placed in the 'target' folder.


## Configuration

To use the plugin, an extra build step must be added in your TeamCity
project.  This build step has the type 'Task Build Runner' and has
several settings that must be configured.  These settings can be
configured from inside the TeamCity web interface.

### Source filter

* Include patterns: a list of globbing patterns to indicate the files
  that must be scanned for task items
  
  
* Exclude patterns: a list of globbing patterns to indicate files that
  cannot be scanned (first the include pattern is evaluated, and
  afterwards the exclude pattern)

### Task level filter 

* Minor level: regular expression to find task items for level 'minor'
* Major level: regular expression to find task items for level 'major'
* Critical level:  regular expression to find task items for level 'critical'


## Report

The report with the task items in a project is visible in the TeamCity
web interface as part of the build page.  On this page, an extra tab
is provided with the name 'Task Build Runner'.  This tab shows the
files that contain task items, together with the importance level of
the task and the line number where the task was found in the source
file.

Clicking on the file name will reveal or hide extra lines before and
after the task items to provide context.

## Statistics

Stats reported to team city with each build which can be added to the statistics graph.
Reported statistic values - Pending Critical Tasks, Pending Major Tasks, Pending Minor Tasks.


Screenshots
-----------

- **Configuration Options**

![alt text](https://github.com/kunalkanojia/tasks-teamcity-plugin/blob/master/screenshots/task_options.png "Options")

- **Success status**

![alt text](https://github.com/kunalkanojia/tasks-teamcity-plugin/blob/master/screenshots/build_status.png "Success Status")


- **Failure status when critical tasks found**

![alt text](https://github.com/kunalkanojia/tasks-teamcity-plugin/blob/master/screenshots/fail_builds.png "Option to fail builds for critical tasks")

- **Results Page**

![alt text](https://github.com/kunalkanojia/tasks-teamcity-plugin/blob/master/screenshots/view_results.png "Results page")

- **Statistics**

![alt text](https://github.com/kunalkanojia/tasks-teamcity-plugin/blob/master/screenshots/statistics.png "Results page")

[TeamCity]: https://www.jetbrains.com/teamcity
[TeamCity Documentation Site]: https://confluence.jetbrains.com/display/TCD9/Installing+Additional+Plugins
[JetBrains]: https://www.jetbrains.com


