#!/bin/bash

javac -d . ../../src/Wieczorek/Jakub/ChatApplication/*.java ../../src/Wieczorek/Jakub/ChatApplication/Client/*.java ../../src/Wieczorek/Jakub/ChatApplication/Server/*.java

jar -cvfm WriteToMe.jar META-INF/MANIFEST.MF *
