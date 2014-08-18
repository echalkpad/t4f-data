#!/bin/bash

# Non-Negated Form
# Uses -n option to read to accept one character without the need to press Enter.

read -p "Are you sure? " -n 1 -r
if [[ $REPLY =~ ^[Yy]$ ]]
then
    # do dangerous stuff
fi

# Negated Form
# However, under some circumstances such as a syntax error caused by 
# the script being run in the wrong shell, the negated form could allow the 
# script to continue to the "dangerous stuff". 
# The failure mode should favor the safest outcome so only the first, non-negated if should be used.

read -p "Are you sure? " -n 1
if [[ ! $REPLY =~ ^[Yy]$ ]]
then
    exit 1
fi

