# Execution

## Setup

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

Setup finished. You're good to go.

## Execute Test cases

``` bash
cd fillin-statio-case-study
mvn test
```


## Execute Time Evaluation
``` bash
cd fillin-statio-case-study
mvn exec:java
```
R Script the data set and  previous obtained datasets are avaliable at
https://github.com/lesunb/goalp-evaluation/tree/master/scalability/exp1


# Tinkering and Contributing

If you would like to tinker with the project, follow the steps at https://github.com/lesunb/goalp to setup the project.

# That's all, folks.
peace.