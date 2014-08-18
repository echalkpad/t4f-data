#!/bin/bash

###############################################################################
# BODYSIZE LOGISTIC REGRESSION
###############################################################################

set -x

rm ./*.model

source dly-env-0.1

rm -r tmp
mkdir -p tmp

nvar=1

out=coefs.dat
rm -f $out

echo "# feat passes rate intercept bodysize" > $out

for feat in $(seq 4 4)
do
	for passes in $(seq 100 100 1000) $(seq 2000 1000 5000)
	do
		for rate in $(seq 50 50 1000)
		do
			logfile="tmp/${feat}_${passes}_${rate}.log"
			mahout org.apache.mahout.classifier.sgd.TrainLogistic --passes $passes --rate $rate --input /dataset/bodysize/bodysize.csv --predictors bodysize --types n --target survive --categories 2 --features $feat --output bodysize.model > $logfile
			mahout org.apache.mahout.classifier.sgd.RunLogistic --input /dataset/bodysize/bodysize.csv --model ./bodysize.model --auc --confusion --scores >> $logfile
			coefs=$(grep -A$(($nvar)) Intercept $logfile | tail -$(($nvar+1)) | awk '{print $(NF)}')
			echo $feat $passes $rate $coefs >> $out
		done
	done
done

mv $out .bid
cat .bid | column -t > $out
rm .bid


#gnuplot -p -e "plot 1/(1+exp(-0.18401*x+5.50508)), 1/(1+exp(- 0.7527*x+22.7536 ))"

