References: [Aspera Connect][1] and [Dropwizard][2]

[1]: https://developer.asperasoft.com/web/connect-client/node-connect
[2]: https://www.dropwizard.io/en/latest/manual/core.html

Java Servlet + Aspera Connect Example
======================================

Java Servlet and Apsera Connect allows you to offer file operations to your clients through a web application.
A server is used to communicate between the client and the Java Servlet server.
This server simply receives a request from the client (using POST data)
and returns the JSON from the Node server for the client to process.  

This document covers two possible setups.
The first setup assumes you have a web server and will simply host a normal web application
on that server that will communicate with your client-side JavaScript.
The second setup gives you the ability to create a single application that provides
all aspects of the system (client side GUI, server side logic, and web server).
Both use the same general concept for communicating and loading the page, however,
the single application includes extra code for handling the server side operations and rendering the HTML.

With the first setup (web application) all of the HTML is handled client side and
the server is only receiving requests and returning the Node JSON to the client.
Essentially:

    Client initiates session (visits webpage)
    JavaScript sends POST data to server side web application
    Server side web application requests data from Node
    Server side web application sends Node data back to JavaScript
    JavaScript parses data accordingly
    JavaScript changes the HTML accordingly

In this example the server side code is very minimalistic since the bulk of the work is handled in JavaScript.
The server is mainly for authorizing against Node,
you could think of it as a 'middle-man' between Node and JavaScript.
This example uses AJAX to get the data and perform operations without needing to reload the page and
uses jQuery for targeting elements and creating a clean, user friendly interface.


Running
======================================

```

cd $ROOT_OF_THIS_PROJECT
mvn clean install
java -jar target/aspera-connect-$timestamp.jar server config.yml

```