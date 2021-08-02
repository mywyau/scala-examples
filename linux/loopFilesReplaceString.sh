#!/bin/zsh

# this will replace the matched string and delete it from the file
for filename in file1 file2
  do
    sed -i '' '/Top Secret!!!/ d' $filename
    done
exit 0