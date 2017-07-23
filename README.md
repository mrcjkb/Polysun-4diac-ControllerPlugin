

# Polysun-4diac-ControllerPlugin
Open source Polysun plugin with a set of plugin controllers that can be used for communication in co-simulations with IEC 61499 applications running on 4diac-RTE (FORTE).


# Use
Requirements for use:

  - Polysun (http://www.velasolaris.com/)
  - 4diac (https://www.eclipse.org/4diac/)

To load the plugin into Polysun, copy the file ForteActorSensorPlugin.jar from ..\target\dist\ into Polysun's plugins folder (usually C:\Users\..\Polysun\plugins\) and launch Polysun.
A set of 4diac function blocks (fbt and c++ source files) are provided in the ..\4diac_function_blocks\ subdirectory of this project. They can be loaded into 4diac and connected to the Polysun plugin controllers.

For now, a short video is provided demonstrating a simple co-simulation example in which data is sent from Polysun to 4diac.
In due time, a detailed documentation shall be linked to here.

This plugin and the communication framework (which can be used for other Java applications, too) are provided as open source under a BSD-3 clause (see LICENCE).


# Development
Requirements for development:

  - Polysun Plugin Development Kit (comes with Polysun, see http://www.velasolaris.com/files/tutorial_en.pdf)
  - JDK 7 or higher (http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html)
  - Ant (http://ant.apache.org/bindownload.cgi)

Recommended for development:

  - Eclipse (http://www.eclipse.org/downloads/)
  
The Eclipse project can be imported with File > Import... > Existing projects into Workspace.  
A javadoc can be found in this project's ..\docs subdirectory or online at:
https://mrcjkb.github.io/Polysun-4diac-ControllerPlugin/ 

To build the project, run build.xml as an Ant script. In order for the JUnit tests to be passed (required for a successful build), PolysunPluginTests.sys must be loaded into 4diac and deployed to FORTE (Alternatively, the tests "ForteDataBufferLayerTest" and "CommFunctionBlockLayerTest" can be disabled by adding @Ignore to the tests.


NOTE: There is currently a bug in Polysun preventing the correct plugin icon from being loaded. This is not a severe issue, as the default plugin icon is loaded instead.
If you would like to load the 4diacPlugin icon, copy the file Icons.jar from Polysuns' pictures path to the desktop (make a backup of the file first).
You will find the file in: C:\Program Files (x86)\Polysun\pictures or C:\Program Files\Polysun\pictures
Rename it to Icons.zip (make sure to show file extensions in Windows Explorer) and add the image "controller_4diac.png" to the archive.
You can find the image in: ..\src\main\resources\plugin\images\
Then rename the archive to Icons.jar and copy it back to its original location.
