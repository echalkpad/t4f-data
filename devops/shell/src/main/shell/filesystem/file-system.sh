#!/bin/bash

###############################################################################
# Open a terminal and type the following shell commands.                      #
###############################################################################

pwd                  # print working directory
mkdir folder         # make the given directory
ls                   # list current directory
ls folder            # list the given directory
cd folder            # change to the given directory
mkdir subfolder
ls
ls -alp              # list folder with details
cd subfolder
touch testfile
ls
vi testfile.txt      # edit a file, use 'i' to insert text, 'esc', ':wq' to save and exit
cat testfile.txt
rm testfile.txt
cd ..
rm -fr subfolder     # remove force+recurring the (sub)folder
ls
cd ..                # change to parent directory
rm -fr folder
ls
cd                   # change to the home directory
cd ~                 # change to the home directory
cd Desktop
ls -alp
