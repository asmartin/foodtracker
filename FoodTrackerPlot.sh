#!/bin/bash
##########################################################
## Author: 	Andrew Martin <amartin@avidandrew.com
## Date:	11/30/13
## Descr:	Creates a plot with number of occurences
##		of each kind of food from FoodTracker
## Depend:	gnuplot
##########################################################
gp=$(mktemp)
dat=$(mktemp)

function on_exit() {
	rm -f $gp $dat
}

trap on_exit EXIT

if [ $# -ne 2 ]; then
	echo "Usage; $0 input.csv output.png"
	exit 1
fi

which gnuplot > /dev/null 2>/dev/null
if [ $? -ne 0 ]; then
	echo "gnuplot cannot be found - please install it first"
	exit 2
fi

input=$1
output=$2

if [ ! -e "$input" ]; then
	echo "Cannot find $input"
	exit 3
fi

# count the number of occurences of each food
cat $input | awk -F',' '{ print $2 }' | sort | uniq -c > $dat

cat << EOF > $gp
#!/usr/bin/gnuplot
set terminal png
set output "${output}"
set xlabel "Type of Food"
set nokey
set boxwidth 1 relative
set style data histograms
set style histogram gap 1
set style fill solid 1.0 border 2
set xtics offset 0,graph 0, graph 0.3
set offset graph 0, graph 0, graph 0.1, graph 0
plot [][0:] "${dat}" using 1:xticlabels(2)
EOF

# create the plot
gnuplot $gp && echo "Graph written to $output" || echo "There was a problem creating the graph"
exit 0
