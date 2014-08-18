#!/bin/bash

###############################################################################
# BODYSIZE LOGISTIC REGRESSION
###############################################################################

rm ./*.model

source dly-env-0.1

mahout org.apache.mahout.classifier.sgd.TrainLogistic --passes 100 --input /dataset/bodysize/bodysize.csv --predictors x y --types n --target color --categories 2 --features 11 --output bodysize.model

mahout org.apache.mahout.classifier.sgd.RunLogistic --input /dataset/bodysize/bodysize.csv --model ./bodysize.model --auc --confusion --scores

gnuplot -p -e "plot 1/(1+exp(-0.18401*x+5.50508)), 1/(1+exp(- 0.7527*x+22.7536 ))"
