# Status Monitor Tool

### Test and Build
The project is inside the server directory.
It is a maven project, so all you have to do to test and build it is to run
- mvn clean test
- mvn clean package


### Running the code
After building the project the built artifacts end up in target/
The runnable jar is the one ending with a "-fat.jar"
So, if you stand in the server directory you would do this to run it:
- java -jar target/service-monitor-tool-1.0-fat.jar

The program starts a server that runs on localhost:8882
Easy peasy

### Project Structure
The structure is as a basiv maven project. You shouldn't have any issues finding everything.
As it wasn't a lot of code needed, all classes are in the same package.

##### Comments
This was all I could do in 10 hours. Sorry for the terrible front-end.
The config doesn't work by the way so don't even bother.
