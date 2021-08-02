#!/bin/zsh

declare -i number1
declare -i number2
declare -i total

echo "What's your favourite number? "
  read number1
echo "What number do you really hate? "
read number2

total=$number1*number2
echo "Aha! they equal " $total
exit 0