#!/bin/bash

BETA=0
VER=3.2.0
if [[ ${BETA} -eq 1 ]];then
	VER=${VER}-SNAPSHOT
fi

mvn -f ../pom.xml -P withDemo,withIdea versions:set -DnewVersion=${VER}
mvn -f ../pom.xml -P withDemo,withIdea versions:commit
sed -i -r -e  "s/<project.polaris-plugins.revision>.+<\/project.polaris-plugins.revision>/<project.polaris-plugins.revision>${VER}<\/project.polaris-plugins.revision>/g" ../pom.xml
#echo "wait 5s...."
#sleep 5
