References: [VirtualBox][1] | [Vagrant][2] | [Ansible][3] | [Aspera Connect][4] | [Dropwizard][5]

[1]: https://www.virtualbox.org/
[2]: https://www.vagrantup.com/
[3]: https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html
[4]: https://developer.asperasoft.com/web/connect-client/node-connect
[5]: https://www.dropwizard.io/en/latest/manual/core.html

--------------------------------------


VirtualBox + Vagrant + Ansible
======================================

Oracle VirtualBox is a free and open-source hosted hypervisor for x86 and AMD64/Intel64 machines.
It can be installed on Windows, MacOS, Linux etc.

Vagrant is an open-source software product for building and maintaining portable virtual software
development environment; e.g., for VirtualBox, KVM, Hyper-V, VMWare etc.

Ansible is an open-source software provisioning, configuration management, and application deployment tool
used to write infrastructure as code.

Environment:
--------------------------------------

- Install VirtualBox and VirtualBox Extension Pack
```shell script
$: vboxmanage list extpacks
Extension Packs: 1
Pack no. 0:   Oracle VM VirtualBox Extension Pack
```
- Install Vagrant and Vagrant VirtualBox Guest Extension
```shell script
$: vagrant plugin install vagrant-vbguest
$: vagrant plugin list
vagrant-vbguest (0.28.0, global)
```

Launch IBM Aspera Server:
--------------------------------------

The idea here is to use `vagrant` as a one-stop-shop to:
- download a snapshot of `CentOS 7 64-bit` operating system
- launch a copy of the system in `VirtualBox` hypervisor
- use Ansible to configure IBM Aspera Server in the system

A [`Vagrantfile`](./Vagrantfile) is provided to do just that.

Next, provide the following two files in this location.

- IBM Aspera Installation file: `ibm-aspera-hsts-linux-x64.rpm`
- IBM Aspera License file: `TransferServer-unlim.eval.aspera-license`

Then execute the `vagrant` command:

```shell script
$: ansible-galaxy install -f -r requirements.yml

$: vagrant up --provision

# WAIT for the vagrant to complete its task

$: curl -u nodeuser:node123  http://172.27.16.130:9091/
```

--------------------------------------


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

Connect to the IBM Aspera Server running in the VirtualBox:

```

$: cd $ROOT_OF_THIS_PROJECT
$: mvn clean install
$: java -jar target/aspera-connect-$timestamp.jar server config.yml

```

Access the application at http://localhost:8095/

