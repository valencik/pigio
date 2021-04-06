#!/bin/bash

process () {
  fullpath="$1"
  dirpath=$( dirname "$1" )
  sourcefile=$( basename "$1" )
  targetfile=$(echo "$sourcefile" | cut -f 1 -d '.')'.html'

  pandoc "$fullpath" \
    --output "$dirpath/$targetfile" \
    --from=markdown \
    --to=html5 \
    --standalone \
    --self-contained
}

for post in docs/*.md
do
  process "$post"
done
