#!/bin/bash

# Compile the project
echo "Compiling the project..."
mvn clean compile

# Check if compilation was successful
if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

# Run the application
echo "Running the application..."
mvn exec:java -Dexec.mainClass="com.sketchandguess.gui.Application"
