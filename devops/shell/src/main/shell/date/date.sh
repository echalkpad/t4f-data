#!/bin/bash

# Exercising the 'date' command

d=date;
echo "`date -u`";

echo "The number of days since the year's beginning is `date +%j`."
# Needs a leading '+' to invoke formatting.
# %j gives day of year.

echo "The number of seconds elapsed since 01/01/1970 is `date +%s`."
#  %s yields number of seconds since "UNIX epoch" began,
#+ but how is this useful?

prefix=temp
suffix=$(date +%s)  # The "+%s" option to 'date' is GNU-specific.
filename=$prefix.$suffix
echo "Temporary filename = $filename"
#  It's great for creating "unique and random" temp filenames,
#+ even better than using $$.

# Read the 'date' man page for more formatting options.

hour="date +%k%M"
# test on 8h = 60m * 8 = 480m
echo $(eval "$hour")
if [[ $(eval "$hour") -ge 800 ]] || [[ $(eval "$hour") -le 1755 ]] ; then
 echo 'it is now after 08:00:00'
else
 echo 'it is now before 08:00:00'
fi

exit 0
