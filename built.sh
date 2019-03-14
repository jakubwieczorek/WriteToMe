#!/bin/bash

cd built/client
sh built.sh
cp WriteToMe.jar ../../
rm -rf Wieczorek/ WriteToMe.jar

cd -

cd built/server
sh built.sh
cp WriteToMeServer.jar ../../
rm -rf Wieczorek/ WriteToMeServer.jar

cd -
