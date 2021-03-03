#!/bin/bash

counter=1
while [ $counter -le $1 ]
do
	mkdir "peer$counter"
	((counter++))
done
echo "All done!"
