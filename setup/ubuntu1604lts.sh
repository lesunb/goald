sudo apt install -y openjdk-9-jdk-headless maven
git clone https://github.com/lesunb/goalp.git
cd goalp/
mvn install

cd scalability-evaluation

touch ~/scalability-evaluation-log
export MAVEN_OPTS="-Xmx1024m -Xms1024m -server -Xcomp"
mvn exec:java > ~/scalability-evaluation-log &
tail -f ~/scalability-evaluation-log