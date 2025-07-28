# galaxy-java-client
Galaxy java client to access the end points exposed by Galaxy.
RSpace web will be using the main facade `GalaxyClient`

# Dependencies
This module has not dependencies with any other RSpace module

# Java version and run the tests
The `Java version` use for this module is `17.0.2`.

In order to run the Integration E2E tests you need to run the following command from the root folder of the project: 

`mvn clean test -DGALAXY_API_KEY=<Your key> -DTEST_PROPERTIES=<Your property file> -Dnightly=true`

You need to create a property file with values for key terms matching the key terms in the rspace-test.properties

In order to run the unit tests alone, with no Integration tests, use: `mvn clean test `