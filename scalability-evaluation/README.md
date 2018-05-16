
# Execute The experiments

## Option A - One line command

In a Ubuntu 16.04, Fresh Virtual Machine, local or in the cloud, paste the following line in the terminal:

``` bash
sudo wget https://raw.githubusercontent.com/lesunb/goalp/master/setup/ubuntu1604lts.sh -v -O /tmp/install-goalp-scal.sh; sudo chmod +x /tmp/install-goalp-scal.sh; /tmp/install-goalp-scal.sh

```
The dependencies will be download, the experiment will be executed.
Then, you can see the log of the execution with:

``` bash
 tail -f ~/scalability-evaluation-log

```
After a couple of hours you get the result at the goalp/scalability-evaluation/result folder.

A file like like this:

 restult_25-Nov-2016 09:14:36Plan size.dat 

with +/-18kb

## Option B - Step by step

Step provided to Ubuntu. Should not be difficult to adapt to another system.

### Requirements: Git, Java8+ and Maven

git is installed by default on ubuntu. Get JDK and Maven.
Ubunt 16.04:
``` bash
sudo apt install openjdk-9-jdk-headless
sudo apt install maven
```

### Clone the Project 
``` bash
git clone https://github.com/lesunb/goalp.git
cd goalp/
```

### Build
``` bash
mvn install
```

## Execute
``` bash
cd scalability-evaluation
export MAVEN_OPTS="-server -Xcomp"
mvn exec:java
```
 
## *Execute with parameters
"-server -Xcomp" makes JVM compile all code and init all needed structures at the beginning. If not done so, the first planning executions will be more slow, afecting the experiments results.   

# Tinkering and Contributing

If you would like to tinker with the project, follow the steps at https://github.com/lesunb/goalp to setup the project.

# That's all, folks.
peace.

