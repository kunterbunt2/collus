#!/bin/bash

mvn -version
cd ..
mvn clean install
cd engine
mvn clean install
cd ../core
mvn clean install
cd ../desktop
mvn clean install

read -p "Press any key to resume ..." 