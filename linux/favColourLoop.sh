#!/bin/zsh

echo "What's your favourite colour? "
read text1
echo "What's your best friend's favourite colour? "
read text2
  if test $text1 != $text2; then      # using a semi-colon will declare a new line else you would need to move the 'then' keyword down to a fresh line
    echo "I guess opposites attract."
  else
    echo "You to do think alike!"
  fi
exit 0

