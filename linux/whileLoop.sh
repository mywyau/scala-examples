#!/bin/zsh

declare -i counter  # declare -i tag mean of type Int
counter=10
while [ $counter -gt 2 ]; do   #while counter greater than 2 keep looping, starts at 10
  echo The counter is $counter
  counter=counter-1
  done
  echo "Exit the loop, condition met"

exit 0   # exit the program