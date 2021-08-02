#!/bin/zsh

echo "What tomorrow's weather going to be like?"

read weather
  case $weather in
    sunny | warm ) echo "Nice! I love it when it's " $weather
    ;;
    cloudy | cool ) echo "Not bad..." $weather" is nice too"
    ;;
    rainy | cold ) echo "Yuck!" $weather "weather is depressing"
    ;;
    * ) echo "Sorry. I am not familiar with that type of weather"
    ;;
  esac

exit 0
