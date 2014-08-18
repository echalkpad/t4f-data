#!/bin/bash

rm ./model*

source dly-env-0.1

mahout org.apache.mahout.classifier.sgd.TrainLogistic --input /apa/bodysize/data/bodysize.csv --lambda 0 --features 22 --output model.model --target survive --categories 2 --predictors bodysize --types n  --rate 100 --passes 100


# Add --scores to print scores
mahout org.apache.mahout.classifier.sgd.RunLogistic --input /apa/bodysize/data/bodysize.csv --model ./model.model --auc --confusion

