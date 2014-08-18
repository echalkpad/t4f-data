# Text files created on DOS/Windows machines have different line endings than files created on Unix/Linux. 
# DOS uses carriage return and line feed ("\r\n" = CRLF) as a line ending, which Unix uses just line feed ("\n" = LF).
# You need to be careful about transferring files between Windows machines and Unix machines to make sure the line 
# endings are translated properly.
# dos2unix

echo -e \x41   # x41
echo -e '\x41' # A
echo A | sed 's/\x41/B/g'

lf="$(printf "\n")"; echo $lf | sed 's/\x0A/LF/g'
sed 's/\x0A/LF/g' 1.txt

grep -c $'\x0F' *.txt # \r CR
grep -c $'\x0A' *.txt # \n LF

find . -name *.eml | xargs grep -IUr --color $'\x0D'
find . -name *.eml | xargs grep -IUr --color $'\x0A'

cr="$(printf "\r")"; echo $cr; find . -name *.eml | xargs grep -IUr --color "${cr}"

find . -name *.eml | xargs grep -IUr --color '\r\n' .
find . -name *.eml | xargs grep -IUr --color "^M"
find . -name *.eml | xargs grep -IUrl --color '^M' . | xargs -ifile fromdos 'file'
find . -name *.eml | xargs grep ^M

find -type f | xargs grep "..." {}
