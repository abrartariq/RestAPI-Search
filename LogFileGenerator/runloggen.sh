#!/bin/bash



while true; do
    # Your command or script goes here

    sbt compile
    sbt run

    # Sleep for 5 minutes (300 seconds)
    sleep 300
done


# Rememeber to run this CMD before running the script
# chmod +x runloggen.sh