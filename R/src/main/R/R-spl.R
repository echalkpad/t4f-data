# Licensed to the AOS Community (AOS) under one or more
# contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The AOS licenses this file
# to you under the Apache License, Version 2.0 (the 
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.


## BASICS ##

# 01. HELP
# 02. ENV
# 03. PACKAGES
# 04. IMPORT
# 05. BUILT-IN FUNCTIONS
# 06. FUNCTIONS
# 07. DATA
# 08. DATASET
# 09. DATA STRUCTURE
# 10. CONTINUOUS VS DISCRETE
# 11. SAMPLING
# 12. MATH

## PLOT ##

# 13. CURVE
# 14. PLOT
# 15. QPLOT
# 16. MATPLOT
# 17. PLOT RIBBON
# 18. LATTICE
# 19. ANIMATED PLOT

## ALGORITHMS ##

# 20. LINEAR REGRESSION
# 21. LOGISTIC REGRESSION
# 22. SURVIVAL ANALYSIS
# 23. CONTROL CHART
# 24. CHANGE POINT
# 25. FORECAST


# -----------------------------------------------------------------------------
# H20
# -----------------------------------------------------------------------------

# Download, install, and initialize the H2O package
install.packages("h2o", repos = c("http://h2o-release.s3.amazonaws.com/h2o/rel-lagrange/11/R", getOption("repos")))
library(h2o)
localH2O <- h2o.init()

# List and run some demos to see H2O at work
demo(package = "h2o")
demo(h2o.glm)
demo(h2o.deeplearning)

# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------

# -----------------------------------------------------------------------------
# 
# -----------------------------------------------------------------------------

install.packages("swirl")
library("swirl")
swirl()

# -----------------------------------------------------------------------------
# 01. HELP
# -----------------------------------------------------------------------------

# general help
help.start()
# help about function foo
help(help)
# same thing
?help
?c
# ...
??help
# list all functions containing string foo
apropos("help")
# show an example of function foo
example(help)
# search for foo in help manuals and archived mailing lists
RSiteSearch("help")
# get vignettes on using installed packages
# show available vignettes
vignette()
# show specific vignette 
vignette("datatable-faq")
browseVignettes("datatable-faq")

# -----------------------------------------------------------------------------
# 02. ENV
# -----------------------------------------------------------------------------

# Add your environement variables in
~/.Renviron

Sys.getenv(c("HOME"))
Sys.getenv(c("PATH"))
Sys.getenv(c("SPL_HOME"))
Sys.getenv(c("HADOOP_HOME"))

## whether HOST is set will be shell-dependent e.g. Solaris' csh does not.
Sys.getenv(c("R_HOME", "R_PAPERSIZE", "R_PRINTCMD", "HOST"))

names(s <- Sys.getenv()) # all settings (the values could be very long)

## Language and Locale settings -- but rather use Sys.getlocale()
s[grep("^L(C|ANG)", names(s))]

setwd(".")
getwd()

# -----------------------------------------------------------------------------

list.files(R.home())
## Only files starting with a-l or r
## Note that a-l is locale-dependent, but using case-insensitive
## matching makes it unambiguous in English locales
dir("../..", pattern = "^[a-lr]", full.names = TRUE, ignore.case = TRUE)

list.dirs(R.home("doc"))
list.dirs(R.home("doc"), full.names = FALSE)

fs <- list.files(".")
fs

# -----------------------------------------------------------------------------
# 03. PACKAGES
# -----------------------------------------------------------------------------

# you can specify your repo with repos='http://cran.us.r-project.org'
# install.packages("animation", dependencies=TRUE, repos='http://cran.us.r-project.org')

install.packages("animation", dependencies=TRUE)
install.packages("bcp", dependencies=TRUE)
install.packages("bcpa", dependencies=TRUE)
install.packages("changepoint", dependencies=TRUE)
install.packages("data.table", dependencies=TRUE)
install.packages("ff", dependencies=TRUE)
install.packages("forecast", dependencies=TRUE)
install.packages("foreach", dependencies=TRUE)
install.packages("gcookbook", dependencies=TRUE)
install.packages('IQCC', dependencies=TRUE)
install.packages('iterators', dependencies=TRUE)
install.packages('knitr', dependencies=TRUE)
install.packages('lattice', dependencies=TRUE)
install.packages("popbio", dependencies=TRUE)
install.packages("pmml", dependencies=TRUE)
install.packages("MSQC", dependencies=TRUE)
install.packages("nlme", dependencies=TRUE)
install.packages("nutshell", dependencies=TRUE)
install.packages("plyr", dependencies=TRUE)
install.packages("qcc", dependencies=TRUE)
install.packages("rJava", dependencies=TRUE)
install.packages("RMySQL", dependencies=TRUE)
install.packages("slam", dependencies=TRUE)
install.packages("SnowballC", dependencies=TRUE)
install.packages("stringr", dependencies=TRUE)
install.packages("strucchange", dependencies=TRUE)
install.packages("textir", dependencies=TRUE)
install.packages("topicmodels", dependencies=TRUE)
install.packages("tm", dependencies=TRUE)
install.packages("wordcloud", dependencies=TRUE)
install.packages("yhatr", dependencies=TRUE)

# -----------------------------------------------------------------------------

install.packages("/d/ecp_1.5.5.tar.gz", repos = NULL, type="source")

# -----------------------------------------------------------------------------

install.packages("rJava", dependencies=TRUE)
R CMD javareconf -e

-----------------------------------------------------------------------------

install.packages(c("rj", "rj.gd"), repos="http://download.walware.de/rj-1.1")

script.R
"install.packages(c("rj", "rj.gd"), repos="http://download.walware.de/rj-1.1")"
R CMD BATCH script.R

11 down vote


I got similar issue and was able to resolve it by running

R CMD javareconf -e

Output of the R CMD javareconf -e

Java interpreter : /export/apps/jdk/JDK-1_6_0_27/jre/bin/java
Java version     : 1.6.0_27
Java home path   : /export/apps/jdk/JDK-1_6_0_27
Java compiler    : /export/apps/jdk/JDK-1_6_0_27/bin/javac
Java headers gen.: /export/apps/jdk/JDK-1_6_0_27/bin/javah
Java archive tool: /export/apps/jdk/JDK-1_6_0_27/bin/jar
Java library path: /export/apps/jdk/JDK-1_6_0_27/jre/lib/amd64/server:/export/apps/jdk/JDK-1_6_0_27/jre/lib/amd64:/export/apps/jdk/JDK-1_6_0_27/jre/../lib/amd64:/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib
JNI linker flags : -L/export/apps/jdk/JDK-1_6_0_27/jre/lib/amd64/server -L/export/apps/jdk/JDK-1_6_0_27/jre/lib/amd64 -L/export/apps/jdk/JDK-1_6_0_27/jre/../lib/amd64 -L/usr/java/packages/lib/amd64 -L/usr/lib64 -L/lib64 -L/lib -L/usr/lib -ljvm
JNI cpp flags    : -I/export/apps/jdk/JDK-1_6_0_27/include -I/export/apps/jdk/JDK-1_6_0_27/include/linux

The following Java variables have been exported:
  JAVA_HOME JAVA JAVAC JAVAH JAR JAVA_LIBS JAVA_CPPFLAGS JAVA_LD_LIBRARY_PATH
Running: /bin/bash

After setting LD_LIBRARY_PATH to the same value as JAVA_LD_LIBRARY_PATH as shown in the output above. I was able to install rj.

export LD_LIBRARY_PATH=/export/apps/jdk/JDK-1_6_0_27/jre/lib/amd64/server:/export/apps/jdk/JDK-1_6_0_27/jre/lib/amd64:/export/apps/jdk/JDK-1_6_0_27/jre/../lib/amd64:/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib

Run R and then install rj by

install.packages(c("rj", "rj.gd"), repos="http://download.walware.de/rj-1.1")


# -----------------------------------------------------------------------------

install.packages("devtools")
library(devtools)
install_github("poweRlaw", "csgillespie", subdir="pkg")

# -----------------------------------------------------------------------------

install.packages("pmml", dependencies=TRUE, repos = NULL, type="source")

# -----------------------------------------------------------------------------

install.packages("/path/package.tar.gz", repos = NULL, type="source")

# -----------------------------------------------------------------------------

$ R CMD INSTALL /path/file.tar.gz

# -----------------------------------------------------------------------------
# 04. IMPORT
# -----------------------------------------------------------------------------

?library
?require
# library(package) and library(package) both load the package with name package 
# and put it on the search list. require is designed for use inside other functions; 
# it returns FALSE and gives a warning (rather than an error as library() does by default) 
# if the package does not exist. Both functions check and update the list of currently 
# loaded packages and do not reload a package which is already loaded... 

library(ggplot2)
require(ggplot2)

# -----------------------------------------------------------------------------

?attach
# The database is attached to the R search path. This means that the database is 
# searched by R when evaluating a variable, so objects in the database can be 
# accessed by simply giving their names.
?detach
# Detach a database, i.e., remove it from the search() path of available R objects. 
# Usually this is either a data.frame which has been attached or a package which
# was attached by library.

# -----------------------------------------------------------------------------

getAnywhere(residuals.glm)

# -----------------------------------------------------------------------------
# 05. BUILT-IN FUNCTIONS
# -----------------------------------------------------------------------------

x <- 4; x; x+3; y <- x+4; y

# -----------------------------------------------------------------------------

s <- paste("hello", "R", separator=" ")
s
print(s)

# -----------------------------------------------------------------------------
# 06. FUNCTIONS
# -----------------------------------------------------------------------------

# myfunction <- function(arg1, arg2, ... ){
#   statements
#   return(object)
# }

# -----------------------------------------------------------------------------

l <- function(x) {
  #  list.files(x)
  return(list.files(x))
}
list <- l("/")
list

# -----------------------------------------------------------------------------

p <- function(x) {
  #  x
  #  x*2
  #  x*3
  x*4
  x*5
  #  x*6
}
p(1)
p(2)

p <- function(x) {
  y <- x*2
  y
}
p(1)
p(2)

p <- function(x) {
  x
}
p("1")
p("2")

for(i in 1:5) {
  print(i)
}

# -----------------------------------------------------------------------------

m <- matrix(data=cbind(rnorm(30, 0), rnorm(30, 2), rnorm(30, 5)), nrow=30, ncol=3)
apply(m, 1, mean)
apply(m, 2, mean)

# -----------------------------------------------------------------------------

d <- read.table(text='
                name plate value1 value2
                1    A    P1      1    100
                2    B    P2      2    200
                3    C    P3      3    300
                ', header=T)

f <- function(x, output) {
  wellName <- x[1]
  plateName <- x[2]
  wellID <- 1
  print(paste(wellID, x[3], x[4], sep=","))
  cat(paste(wellID, x[3], x[4], sep=","), file= output, append = T, fill = T)
}

apply(d, 1, f, output = 'outputfile')

# -----------------------------------------------------------------------------

f3 <- function(..., ff) {
  t <- ff
  t
}
f3(ff="test")

# -----------------------------------------------------------------------------

fiborecursive = function(i){
  if (i <= 2) {
    return(1)
  }
  else {
    return(fiborecursive(i-1) + fiborecursive(i-2))
  }
}
fiborecursive(10)

# -----------------------------------------------------------------------------

# Function example - get measures of central tendency
# and spread for a numeric vector x. The user has a
# choice of measures and whether the results are printed.

mysummary <- function(x, npar=TRUE, print=TRUE) {
  if (!npar) {
    center <- mean(x); spread <- sd(x)
  } else {
    center <- median(x); spread <- mad(x)
  }
  if (print & !npar) {
    cat("Mean=", center, "\n", "SD=", spread, "\n")
  } else if (print & npar) {
    cat("Median=", center, "\n", "MAD=", spread, "\n")
  }
  result <- list(center=center,spread=spread)
  return(result)
}

# invoking the function
set.seed(1234)
x <- rpois(500, 4)
y <- mysummary(x)
Median= 4
MAD= 1.4826
# y$center is the median (4)
# y$spread is the median absolute deviation (1.4826)

y <- mysummary(x, npar=FALSE, print=FALSE)
y
# y$center is the mean (4.052)
# y$spread is the standard deviation (2.01927) 

# -----------------------------------------------------------------------------

# Multiple plot function
#
# ggplot objects can be passed in ..., or to plotlist (as a list of ggplot objects)
# - cols:   Number of columns in layout
# - layout: A matrix specifying the layout. If present, 'cols' is ignored.
#
# If the layout is something like matrix(c(1,2,3,3), nrow=2, byrow=TRUE),
# then plot 1 will go in the upper left, 2 will go in the upper right, and
# 3 will go all the way across the bottom.
#
multiplot <- function(..., plotlist=NULL, file, cols=1, layout=NULL) {
  
  library(grid)
  
  # Make a list from the ... arguments and plotlist
  plots <- c(list(...), plotlist)
  
  numPlots = length(plots)
  
  # If layout is NULL, then use 'cols' to determine layout
  if (is.null(layout)) {
    # Make the panel
    # ncol: Number of columns of plots
    # nrow: Number of rows needed, calculated from # of cols
    layout <- matrix(seq(1, cols * ceiling(numPlots/cols)),
                     ncol = cols, nrow = ceiling(numPlots/cols))
  }
  
  if (numPlots==1) {
    print(plots[[1]])
    
  } else {
    # Set up the page
    grid.newpage()
    pushViewport(viewport(layout = grid.layout(nrow(layout), ncol(layout))))
    
    # Make each plot, in the correct location
    for (i in 1:numPlots) {
      # Get the i,j matrix positions of the regions that contain this subplot
      matchidx <- as.data.frame(which(layout == i, arr.ind = TRUE))
      
      print(plots[[i]], vp = viewport(layout.pos.row = matchidx$row,
                                      layout.pos.col = matchidx$col))
    }
  }
  
}

# -----------------------------------------------------------------------------
# 07. DATA
# -----------------------------------------------------------------------------

1.1
2
2^1023

# -----------------------------------------------------------------------------

x <- 1

# -----------------------------------------------------------------------------

x <- c("a", "b", "c", "d", "e")
y <- c("A", "B", "C", "D", "E")
paste(x, y)
paste(x, y, sep="-")
paste(x, y, sep="-", collapse="#")

# -----------------------------------------------------------------------------

x = c()
attributes(x)
attributes(x)$name
att(x, "name")

# -----------------------------------------------------------------------------

pairs(iris)
pairs(iris[1:4], main = "Edgar Anderson's Iris Data", pch = 21, bg = c("red", "green3", "blue")[unclass(iris$Species)])

# -----------------------------------------------------------------------------

library(iterators)
onetofive <- iter(1:5)
nextElem(onetofive)
nextElem(onetofive)
nextElem(onetofive)
nextElem(onetofive)
nextElem(onetofive)
nextElem(onetofive)
#Error: StopIteration

# -----------------------------------------------------------------------------

sqrts.1to5 <- foreach(i=1:5) %do% sqrt(i)
sqrts.1to5

foreach(i=list.files(".")) %do% i

# -----------------------------------------------------------------------------

panel.pearson <- function(x, y, ...) {
  horizontal <- (par("usr")[1] + par("usr")[2]) / 2;
  vertical <- (par("usr")[3] + par("usr")[4]) / 2;
  text(horizontal, vertical, format(abs(cor(x,y)), digits=2))
}
pairs(iris[1:4], main = "Edgar Anderson's Iris Data", pch = 21, bg = c("red","green3","blue")[unclass(iris$Species)], upper.panel=panel.pearson)

# -----------------------------------------------------------------------------

pairs(iris[1:4], main = "Anderson's Iris Data -- 3 species", pch = 21, 
      bg = c("red", "green3", "blue")[unclass(iris$Species)], lower.panel=NULL, labels=c("SL","SW","PL","PW"), 
      font.labels=2, cex.labels=4.5) 

# -----------------------------------------------------------------------------
# 08. DATASET
# -----------------------------------------------------------------------------

?mtcars
mtcars[1, 2]
mtcars["Mazda RX4", "cyl"]
nrow(mtcars)
ncol(mtcars)
help(mtcars)
head(mtcars)

# -----------------------------------------------------------------------------

library(graphics)
pairs(mtcars, main = "mtcars data")
coplot(mpg ~ disp | as.factor(cyl), data = mtcars,
       panel = panel.smooth, rows = 1)
pressure

# -----------------------------------------------------------------------------

# The iris dataset (included with R) contains four measurements for 150 flowers 
# representing three species of iris (Iris setosa, versicolor and virginica). 
iris
class(iris)
colnames(iris)
iris$Petal.Length

# -----------------------------------------------------------------------------

iris3
help(iris3)
dni3 <- dimnames(iris3)
ii <- data.frame(matrix(aperm(iris3, c(1,3,2)), ncol = 4,
                        dimnames = list(NULL, sub(" L.",".Length",
                                                  sub(" W.",".Width", dni3[[2]])))),
                 Species = gl(3, 50, labels = sub("S", "s", sub("V", "v", dni3[[3]]))))
# TRUE
all.equal(ii, iris)

# -----------------------------------------------------------------------------

library(ggplot2)
?diamonds
diamonds
head(diamonds)

# -----------------------------------------------------------------------------
# 09. DATA STRUCTURE
# -----------------------------------------------------------------------------

fib <- c(0,1,1,2,3,5,8,13,21,34)
fib
fib[1]
fib[2]
fib[3]
fib[4]
fib[5]
fib[1:3]
fib[4:9]
fib[c(1,2,4,8)]
fib[-1]
fib[-(1:3)]
fib < 10
fib[fib < 10] 
fib[fib == 8] 
fib %% 2 == 0
fib[fib %% 2 == 0]

# -----------------------------------------------------------------------------

a <- c(.007, 0.012, .022, 0.000)
b <- c( .005, 0.112, .027, 1.000, .037, .001, .061, .055)
x <- matrix(c(a,b), nrow=4, ncol=3, byrow=TRUE)
x
y <- x>0.05
y
x[y]
y <- which(x>0.05)
y
y <- which(x>0.05, arr.in=TRUE)
y

# -----------------------------------------------------------------------------

l <- list(a="tom", b="dick")
c(l, c="harry")
class(l)

# -----------------------------------------------------------------------------

l <- c(a="tom", b="dick")
l <- c(l, c="harry")
l
class(l)

# -----------------------------------------------------------------------------

a <- c(.007, 0.012, .022, 0.000)
b <- c( .005, 0.112, .027, 1.000, .037, .001, .061, .055)
x <- matrix(c(a,b), nrow=4, ncol=3, byrow=TRUE)
x

# -----------------------------------------------------------------------------

d <- data.frame(x=1:10000, y=sample(c(rep(1,100),0), 10000, replace=T))
sum((d$y-d$p)^2 / d$p)

# -----------------------------------------------------------------------------

d <- data.frame(x=1:10000, y=sample(c(rep(1,100),0), 10000, replace=T))
M <- glm(y~x,family="binomial",data=d)
head(residuals(M, type="pearson")^2)

# -----------------------------------------------------------------------------

d <- data.frame(x=1:10000, y=sample(c(rep(1,100), 0), 10000, replace=T))
head((d$y-d$p)^2/d$p)

# -----------------------------------------------------------------------------
# 10. CONTINUOUS VS DISCRETE
# -----------------------------------------------------------------------------

gender_char <- sample(c("female", "male"), 100, replace = TRUE)
gender_char
object.size(gender_char)
gender_fac <- as.factor(gender_char)
gender_fac
object.size(gender_fac)

# -----------------------------------------------------------------------------

library(data.table)
donut <- data.table(read.csv(file="/dataset/donut/donut.csv", sep=","))
donut
donut_factor <- donut[, color:=as.factor(color)]
donut_factor
str(donut_factor)

# -----------------------------------------------------------------------------

x <- runif(100, 0, 100)
u <- cut(x, breaks = c(0, 3, 4.5, 6, 8, Inf), labels = c(1:5))
table(u)

# -----------------------------------------------------------------------------
# 11. SAMPLING
# -----------------------------------------------------------------------------

x <- 1:12
# a random permutation
sample(x)
# bootstrap resampling -- only if length(x) > 1 !
sample(x, replace = TRUE)

# 100 Bernoulli trials
sample(c(0,1), 100, replace = TRUE)

## More careful bootstrapping --  Consider this when using sample()
## programmatically (i.e., in your function or simulation)!

# sample()'s surprise -- example
x <- 1:10
sample(x[x >  8]) # length 2
sample(x[x >  9]) # oops -- length 10!
sample(x[x > 10]) # length 0

resample <- function(x, ...) x[sample.int(length(x), ...)]
# length 2
resample(x[x > 8])
# length 1
resample(x[x > 9])
# length 0
resample(x[x > 10])

## R 3.x.y only
sample.int(1e10, 12, replace = TRUE)
sample.int(1e10, 12) # not that there is much chance of duplicates

# First, we'll create a fake dataset of 20 individuals of different body sizes:
# Generates 20 values, with mean of 30 & s.d.=2
bodysize = rnorm(20, 30, 2)
# sorts these values in ascending order.
bodysize = sort(bodysize)
# assign 'survival' to these 20 individuals non-randomly... most mortality occurs at smaller body size.
survive = c(0,0,0,0,0,1,0,1,0,0,1,1,0,1,1,1,0,1,1,1)
# saves dataframe with two columns: body size & survival.
dat = as.data.frame(cbind(bodysize, survive))
dat

# -----------------------------------------------------------------------------
# 12. MATH
# -----------------------------------------------------------------------------

x <- c(0,1,1,2,3,5,8,13,21,34)
x
plot(x)
mean(x)
median(x)
sd(x)
var(x)
min(x)
max(x)

# -----------------------------------------------------------------------------

library(nutshell)
data(dow30)
mean(dow30$Open)
min(dow30$Open)
max(dow30$Open)

# -----------------------------------------------------------------------------

library(stats);
library(graphics)
# -> one number
min(5:1, pi)
# ->  5  numbers
pmin(5:1, pi)

# -----------------------------------------------------------------------------

?sin
?cos
?tan

# -----------------------------------------------------------------------------

x <- sort(rnorm(100));  cH <- 1.35
pmin(cH, quantile(x)) # no names
pmin(quantile(x), cH) # has names
plot(x, pmin(cH, pmax(-cH, x)), type = "b", main =  "Huber's function")

# -----------------------------------------------------------------------------

integrate(dnorm, -1.96, 1.96)
integrate(dnorm, -Inf, Inf)

## a slowly-convergent integral
integrand <- function(x) {1/((x+1)*sqrt(x))}
integrate(integrand, lower = 0, upper = Inf)

## don't do this if you really want the integral from 0 to Inf
integrate(integrand, lower = 0, upper = 10)
integrate(integrand, lower = 0, upper = 100000)
integrate(integrand, lower = 0, upper = 1000000, stop.on.error = FALSE)

## some functions do not handle vector input properly
f <- function(x) 2.0
try(integrate(f, 0, 1))
integrate(Vectorize(f), 0, 1)  ## correct
integrate(function(x) rep(2.0, length(x)), 0, 1)  ## correct

## integrate can fail if misused
integrate(dnorm, 0, 2)
integrate(dnorm, 0, 20)
integrate(dnorm, 0, 200)
integrate(dnorm, 0, 2000)
integrate(dnorm, 0, 20000) ## fails on many systems
integrate(dnorm, 0, Inf)   ## works

# -----------------------------------------------------------------------------
# 13. CURVE
# -----------------------------------------------------------------------------
?curve
# -----------------------------------------------------------------------------

curve(x^2, -5, 5)
curve(x^2, -500, 500)
curve(x^2- + x - 10, -5, 5)
curve(x^4 + x + 3000, -500, 500)

# -----------------------------------------------------------------------------

cut01 <- function(x) pmax(pmin(x, 1), 0)
curve(x^2 - 1/4, -1.4, 1.5, col = 2)
curve(cut01(x^2 - 1/4), col = "blue", add = TRUE, n = 500)
## pmax(), pmin() preserve attributes of *first* argument
D <- diag(x = (3:1)/4); n0 <- numeric()
stopifnot(identical(D,  cut01(D) ),
          identical(n0, cut01(n0)),
          identical(n0, cut01(NULL)),
          identical(n0, pmax(3:1, n0, 2)),
          identical(n0, pmax(n0, 4)))

# -----------------------------------------------------------------------------

op <- par(mfrow = c(2, 2))
curve(x^3 - 3*x, -2, 2)
curve(x^2 - 2, add = TRUE, col = "violet")
curve(1/(1+exp(-0.18401*x+5.50508)))
curve(([exp(2 + 2x)] / [1 + exp(1 + 3x)]) , main = "Logit")

curve(sin, -2*pi, 2*pi, xname = "t")
curve(tan, xname = "t", add = NA, main = "curve(tan)  --> same x-scale as previous plot")
curve(sin, -100*pi, 100*pi, xname = "t")
curve(cos, xlim = c(-pi, 3*pi), n = 1001, col = "blue", add = TRUE)

chippy <- function(x) sin(cos(x)*exp(-x/2))
curve(chippy, -8, 7, n = 2001)

# -----------------------------------------------------------------------------

for(ll in c("", "x", "y", "xy"))
  curve(log(1+x), 1, 100, log = ll, sub = paste0("log = '", ll, "'"))
par(op)

# -----------------------------------------------------------------------------
# 14. PLOT
# -----------------------------------------------------------------------------

z <- seq(-3, 3, .1)
d <- dnorm(z)
plot(z, d, type="l")
title("The Standard Normal Density", col.main="cornflowerblue")

# -----------------------------------------------------------------------------

v <- read.table(text='
 V1 V2
1 3 4
2 1 5
3 3 6
4 9 4
5 3 3
', header=T)
plot(v)
plot(v$V1)
plot(v$V2)
library(ggplot2)
ggplot(v, aes(x=Index, y=V1))+ geom_line()
ggplot(v, aes(x=V1, y=V2))+ geom_line() + geom_hline(yintercept=4.5)

# -----------------------------------------------------------------------------

## 1-d tables
(Poiss.tab <- table(N = stats::rpois(200, lambda = 5)))
plot(Poiss.tab, main = "plot(table(rpois(200, lambda = 5)))")

plot(table(state.division))

## 4-D :
plot(Titanic, main ="plot(Titanic, main= *)")

# -----------------------------------------------------------------------------

pie(rep(1,16), col=rainbow(16))

# -----------------------------------------------------------------------------

X11(height=8,width=8)
pie(rep(1,16), col=rainbow(16))

# -----------------------------------------------------------------------------

library(qcc)
data(pistonrings)
attach(pistonrings)
summary(pistonrings)
boxplot(diameter ~ sample)
plot(sample, diameter, cex=0.7)
lines(tapply(diameter,sample,mean))
detach(pistonrings)

# -----------------------------------------------------------------------------

fpe <- read.table("/dataset/effort/effort.dat")
fpe = read.table("noheader.dat", col.names=c("setting","effort","change"))
fpe = read.table("noheader.dat")
names(fpe) = c("setting","effort","change")
boxplot(fpe$setting, col="lavender")
title("Boxplot of Setting", col.main="#3366CC")
plot(fpe$effort, fpe$change, pch=21, bg="gold")
title("Scatterplot of Change by Effort", col.main="#3366CC")

# -----------------------------------------------------------------------------

plot(qnorm) # default range c(0, 1) is appropriate here,
# but end values are -/+Inf and so are omitted.
plot(qlogis, main = "The Inverse Logit : qlogis()")
abline(h = 0, v = 0:2/2, lty = 3, col = "gray")

plot((-4:5)^2, main = "Quadratic")

f1 <- function(x) sin(cos(x)*exp(-x/2))
plot(f1, -8, 5)

## simple and advanced versions, quite similar:
plot(cos, -pi,  3*pi)
chippy <- function(x) sin(cos(x)*exp(-x/2))
plot (chippy, -8, -5)

# -----------------------------------------------------------------------------

f1 <- function(x) sin(cos(x)*exp(-x/2))
f2 <- sin
plot(f1, -8, 5)
plot(f2, -8, 5, add=TRUE)

f1 <- function(x) sin(cos(x)*exp(-x/2))
x <- seq(0, 2*pi, 0.01)
plot (x, f1(x), type="l", col="blue", ylim=c(-0.8, 0.8))
points (x, -f1(x), type="l", col="red")

f1 <- function(x) sin(cos(x)*exp(-x/2))
f2 <- function(x) sin(cos(x)*exp(-x/4))
plot(f1, -8,5)
par(new=TRUE)
plot(f2, -8,5)

# -----------------------------------------------------------------------------

volcano
str(volcano)
pairs(volcano)
plot(volcano)

# -----------------------------------------------------------------------------

library(graphics)

dnorm(0) == 1/sqrt(2*pi)
dnorm(1) == exp(-1/2)/sqrt(2*pi)
dnorm(1) == 1/sqrt(2*pi*exp(1))

## Using "log = TRUE" for an extended range :
par(mfrow = c(2,1))
plot(function(x) dnorm(x, log = TRUE), -60, 50, main = "log { Normal density }")
curve(log(dnorm(x)), add = TRUE, col = "red", lwd = 2)
mtext("dnorm(x, log=TRUE)", adj = 0)
mtext("log(dnorm(x))", col = "red", adj = 1)

plot(function(x) pnorm(x, log.p = TRUE), -50, 10, main = "log { Normal Cumulative }")
curve(log(pnorm(x)), add = TRUE, col = "red", lwd = 2)
mtext("pnorm(x, log=TRUE)", adj = 0)
mtext("log(pnorm(x))", col = "red", adj = 1)

## if you want the so-called 'error function'
erf <- function(x) 2 * pnorm(x * sqrt(2)) - 1
## (see Abramowitz and Stegun 29.2.29)
## and the so-called 'complementary error function'
erfc <- function(x) 2 * pnorm(x * sqrt(2), lower = FALSE)
## and the inverses
erfinv <- function (x) qnorm((1 + x)/2)/sqrt(2)
erfcinv <- function (x) qnorm(x/2, lower = FALSE)/sqrt(2)

# -----------------------------------------------------------------------------

# Two objects: 100 numbers each, randomly distributed with a standard deviation of 1. One has mean of 10, the other with mean of 13.
a=rnorm(100, mean=10,sd=1)
b=rnorm(100, mean=13,sd=1)
# print the two histograms side-by-side
par(mfrow=c(1,2))
hist(a, main="")
hist(b, main="")
hist(a, xlim=c(5,18), ylim=c(0,30), breaks=10, col=rgb(1,1,0,0.7), main="", xlab="number")
par(new=TRUE)
hist(b, xlim=c(5,18), ylim=c(0,30), breaks=10, col=rgb(0,1,1,0.4), main="", xlab="", ylab="")

# ----------------------------------------------------------------------------
# 15. QPLOT
# -----------------------------------------------------------------------------

library(ggplot2)

diamonds
head(diamonds)
pairs(diamonds)
# these are equivalent
qplot(carat, price, data=diamonds)
qplot(x=carat, y=price, data=diamonds)
qplot(diamonds$carat, diamonds$price, data=diamonds)
qplot(carat, price, data = diamonds, colour = clarity)

# -----------------------------------------------------------------------------

mtcars
plot(mtcars$wt, mtcars$mpg)
qplot(mtcars$wt, mtcars$mpg)

# -----------------------------------------------------------------------------

library(ggplot2)

pressure
plot(pressure$temperature, pressure$pressure, type="l")
lines(pressure$temperature, pressure$pressure/2, col="red")
points(pressure$temperature, pressure$pressure/2, col="red")
qplot(pressure$temperature, pressure$pressure, geom="line")

# -----------------------------------------------------------------------------

library(ggplot2)
BOD
ggplot(BOD, aes(x=Time, y=demand)) + geom_line()
# Make a copy of the data
BOD1 <- BOD
BOD1$Time <- factor(BOD1$Time)
ggplot(BOD1, aes(x=Time, y=demand, group=1)) + geom_line()
ggplot(BOD, aes(x=Time, y=demand)) + geom_line() + ylim(0, max(BOD$demand))
ggplot(BOD, aes(x=Time, y=demand)) + geom_line() + expand_limits(y=0)
ggplot(BOD, aes(x=Time, y=demand)) + geom_line() + geom_point()

# -----------------------------------------------------------------------------

library(gcookbook)
library(ggplot2)
ggplot(worldpop, aes(x=Year, y=Population)) + geom_line() + geom_point()
# Same with a log y-axis
ggplot(worldpop, aes(x=Year, y=Population)) + geom_line() + geom_point() +scale_y_log10()

# -----------------------------------------------------------------------------

xy.error.bars=function(x,y,xbar,ybar){
  plot(x,y,pch=16,ylim=c(min(y-ybar),max(y+ybar)),
       xlim=c(min(x-xbar),max(x+xbar)))
  arrows(x,y-ybar,x,y+ybar,code=3,angle=90,length=0.1)
  arrows(x-xbar,y,x+xbar,y,code=3,angle=90,length=0.1)}

#Here's a trial run:
x=rnorm(10,25,5)
y=rnorm(10,100,20)
xb=(runif(10)*5)
yb=runif(10)*20
xy.error.bars(x,y,xb,yb)

# -----------------------------------------------------------------------------

# Load crime data
crime <- read.csv("/dataset/crime/crimeRatesByState-formatted.csv")
# Remove Washington, D.C.
crime.new <- crime[crime$state != "District of Columbia",]
# Remove national averages
crime.new <- crime.new[crime.new$state != "United States ",]
# Box plot
boxplot(crime.new$robbery, horizontal=TRUE, main="Robbery Rates in US")
# Box plots for all crime rates
boxplot(crime.new[,-1], horizontal=TRUE, main="Crime Rates in US")
# Histogram
hist(crime.new$robbery, breaks=10)
# Multiple histograms
par(mfrow=c(3, 3))
colnames <- dimnames(crime.new)[[2]]
for (i in 2:8) {
  hist(crime[,i], xlim=c(0, 3500), breaks=seq(0, 3500, 100), main=colnames[i], probability=TRUE, col="gray", border="white")
}
# Density plot
par(mfrow=c(3, 3))
colnames <- dimnames(crime.new)[[2]]
for (i in 2:8) {
  d <- density(crime[,i])
  plot(d, type="n", main=colnames[i])
  polygon(d, col="red", border="gray")
}
# Histograms and density lines
par(mfrow=c(3, 3))
colnames <- dimnames(crime.new)[[2]]
for (i in 2:8) {
  hist(crime[,i], xlim=c(0, 3500), breaks=seq(0, 3500, 100), main=colnames[i], probability=TRUE, col="gray", border="white")
  d <- density(crime[,i])
  lines(d, col="red")
}
# Density and rug
d <- density(crime$robbery)
plot(d, type="n", main="robbery")
polygon(d, col="lightgray", border="gray")
rug(crime$robbery, col="red")
# Violin plot
library(vioplot)
vioplot(crime.new$robbery, horizontal=TRUE, col="gray")
# Bean plot
library(beanplot)
beanplot(crime.new[,-1])

# -----------------------------------------------------------------------------

library(gcookbook)

# Show the three columns we'll use
heightweight[, c("sex", "ageYear", "heightIn"), "weightLb")]
heightweight

ggplot(heightweight, aes(x=ageYear, y=heightIn, colour=sex)) + geom_point()
ggplot(heightweight, aes(x=ageYear, y=heightIn, shape=sex)) + geom_point()

ggplot(heightweight, aes(x=ageYear, y=heightIn, size=weightLb, colour=sex)) +
  geom_point(alpha=.5) +
  scale_size_area() +
  # Make area proportional to numeric value
  scale_colour_brewer(palette="Set1")

ggplot(heightweight, aes(x=weightLb, y=heightIn, fill=ageYear)) +
  geom_point(shape=21, size=2.5) +
  scale_fill_gradient(low="black", high="white")

ggplot(heightweight, aes(x=ageYear, y=heightIn, size=weightLb, colour=sex)) +
  geom_point(alpha=.5) +
  scale_size_area() +
  # Make area proportional to numeric value
  scale_colour_brewer(palette="Set1")

# Make a copy of the data
hw <- heightweight

# Categorize into <100 and >=100 groups
hw$weightGroup <- cut(hw$weightLb, breaks=c(-Inf, 100, Inf),
                      labels=c("< 100", ">= 100"))

# Use shapes with fill and color, and use colors that are empty (NA) and filled
ggplot(hw, aes(x=ageYear, y=heightIn, shape=sex, fill=weightGroup)) +
  geom_point(size=2.5) +
  scale_shape_manual(values=c(21, 24)) +
  scale_fill_manual(values=c(NA, "black"),
                    guide=guide_legend(override.aes=list(shape=21)))

# -----------------------------------------------------------------------------

library(data.table)
library(ggplot2)
soi <- data.table(read.csv(file="/dataset/climate/southern-oscillation-index", sep=","))
soi
ggplot(soi, aes(x=year, y=v1)) + geom_line() + geom_point()
ggplot(soi, aes(x=year, y=v2)) + geom_line() + geom_point()

# -----------------------------------------------------------------------------

library(data.table)
library(ggplot2)

donut <- data.table(read.csv(file="/dataset/donut/donut.csv", sep=","))
donut
qplot(donut$x, donut$y)
ggplot(donut, aes(x=x, y=y, colour=color)) + geom_point()

ggplot(donut, aes(x=x, y=y, color=color)) + geom_point(shape=2)

donut_factor1 <- donut[, color:=as.factor(color)]
donut_factor2 <- donut[, shape:=as.factor(shape)]
donut_factor2

ggplot(donut_factor2, aes(x=x, y=y, shape=color)) + geom_point()

ggplot(donut_factor2, aes(x=x, y=y, shape=shape, colour=color) + geom_point()
  + scale_shape_manual(values=c(21, 22, 23, 24)) 
  + scale_colour_brewer(palette="Set1")

# -----------------------------------------------------------------------------

library(ggplot2)

# create factors with value labels
mtcars$gear <- factor(mtcars$gear,levels=c(3,4,5),
                      labels=c("3gears","4gears","5gears"))

mtcars$am <- factor(mtcars$am,levels=c(0,1),
                    labels=c("Automatic","Manual"))

mtcars$cyl <- factor(mtcars$cyl,levels=c(4,6,8),
                     labels=c("4cyl","6cyl","8cyl"))

# Kernel density plots for mpg grouped by number of gears (indicated by color)
qplot(mpg, data=mtcars, geom="density", fill=gear, alpha=I(.5),
      main="Distribution of Gas Milage", 
      xlab="Miles Per Gallon", ylab="Density")

# Scatterplot of mpg vs. hp for each combination of gears and cylinders
# in each facet, transmittion type is represented by shape and color
qplot(hp, mpg, data=mtcars, shape=am, color=am,
      facets=gear~cyl, size=I(3),
      xlab="Horsepower", ylab="Miles per Gallon")

# Separate regressions of mpg on weight for each number of cylinders
qplot(wt, mpg, data=mtcars, geom=c("point", "smooth"),
      method="lm", formula=y~x, color=cyl,
      main="Regression of MPG on Weight",
      xlab="Weight", ylab="Miles per Gallon")

# Boxplots of mpg by number of gears
# observations (points) are overlayed and jittered
qplot(gear, mpg, data=mtcars, geom=c("boxplot", "jitter"),
      fill=gear, main="Mileage by Gear Number",
      xlab="", ylab="Miles per Gallon") 

# -----------------------------------------------------------------------------

library(ggplot2)
states <- map_data("state")
var <- data.frame(table(states$region)) # using rows as a dummy variable
states$variable <- var$Freq[match(states$region,var$Var1)]
map <- ggplot(states, aes(x=long, y=lat)) + geom_polygon(aes(group=group, fill=variable), col=NA, lwd=0)
map + scale_colour_gradient(low='white', high='grey20')
map + scale_colour_grey()

# -----------------------------------------------------------------------------

library(ggplot2)
states <- map_data("state")
var <- data.frame(table(states$region))
states$variable <- var$Freq[match(states$region,var$Var1)]
map <- ggplot(states, aes(x=long, y=lat,fill=variable,group=group)) + geom_polygon()
map + scale_fill_gradient(low='white', high='grey20')

# -----------------------------------------------------------------------------

library(ggplot2)
library(RColorBrewer)

#Sample data
dat <- data.frame(x = rnorm(100), y = rnorm(100), z = rnorm(100))
dat[sample(nrow(dat), 5), 3] <- NA
dat[sample(nrow(dat), 5), 3] <- Inf

#Subset out the real values
dat.good <- dat[!(is.na(dat$z)) & is.finite(dat$z) ,]
#Create 6 breaks for them
dat.good$col <- cut(dat.good$z, 6)

#Grab the bad ones
dat.bad <- dat[is.na(dat$z) | is.infinite(dat$z) ,]
dat.bad$col <- as.character(dat.bad$z)

#Rbind them back together
dat.plot <- rbind(dat.good, dat.bad)

#Make your own scale with RColorBrewer
yourScale <- c(brewer.pal(6, "Blues"), "red","green")

ggplot(dat.plot, aes(x,y, colour = col)) + 
  geom_point() +
  scale_colour_manual("Intensity", values = yourScale)

# -----------------------------------------------------------------------------

library(ggplot2)

bincol <- function(x, low, medium, high) {
  breaks <- function(x) pretty(range(x), n = nclass.Sturges(x), min.n = 1)
  colfunc <- colorRampPalette(c(low, medium, high))
  binned <- cut(x,breaks(x))
  res <- colfunc(length(unique(binned)))[as.integer(binned)]
  names(res) <- as.character(binned)
  res
}

labels <- unique(names(bincol(iris$Sepal.Length, "blue","yellow","red")))
breaks <- unique(bincol(iris$Sepal.Length,"blue","yellow","red"))
breaks <- breaks[order(labels,decreasing = TRUE)]
labels <- labels[order(labels,decreasing = TRUE)]

ggplot(iris) + 
  geom_point(aes(x=Sepal.Width, y=Sepal.Length,
                 colour=bincol(Sepal.Length,"blue","yellow","red")), size=4) +
  scale_color_identity("Sepal.Length", labels=labels, 
                       breaks=breaks, guide="legend")

# -----------------------------------------------------------------------------

library(ggplot2)
p <- qplot(hp, mpg, data=mtcars, shape=am, color=am,
           facets=gear~cyl, main="Scatterplots of MPG vs. Horsepower",
           xlab="Horsepower", ylab="Miles per Gallon")
# White background and black grid lines
p + theme_bw()
# Large brown bold italics labels and legend placed at top of plot
p + theme(axis.title=element_text(face="bold.italic", size="12", color="brown"), 
          legend.position="top") 

# -----------------------------------------------------------------------------

plot(iris$Petal.Length, iris$Petal.Width, main="Edgar Anderson's Iris Data")
plot(iris$Petal.Length, iris$Petal.Width, pch=c(23,24,25)[unclass(iris$Species)], main="Edgar Anderson's Iris Data")

c(23,24,25)[unclass(iris$Species)]
plot(iris$Petal.Length, iris$Petal.Width, pch=21, bg=c("red","green3","blue")[unclass(iris$Species)], main="Edgar Anderson's Iris Data")

pairs(iris[1:4], main = "Edgar Anderson's Iris Data", pch = 21, bg = c("red", "green3", "blue")[unclass(iris$Species)])

panel.pearson <- function(x, y, ...) {
  horizontal <- (par("usr")[1] + par("usr")[2]) / 2;
  vertical <- (par("usr")[3] + par("usr")[4]) / 2;
  text(horizontal, vertical, format(abs(cor(x,y)), digits=2))
}

pairs(iris[1:4], main = "Edgar Anderson's Iris Data", pch = 21, bg = c("red","green3","blue")[unclass(iris$Species)], upper.panel=panel.pearson)

pairs(iris[1:4], main = "Anderson's Iris Data -- 3 species", pch = 21, bg = c("red", "green3", "blue")[unclass(iris$Species)], lower.panel=NULL, labels=c("SL","SW","PL","PW"), font.labels=2, cex.labels=4.5)

# -----------------------------------------------------------------------------

library(ggplot2)

ggplot(iris) + geom_point(aes(x=Sepal.Width, y=Sepal.Length, colour=Sepal.Length)) + scale_colour_gradient()

# -----------------------------------------------------------------------------

library(ggplot2)

set.seed(955)
# Make some noisily increasing data
dat <- data.frame(cond = rep(c("A", "B"), each=10),
                  xvar = 1:20 + rnorm(20,sd=3),
                  yvar = 1:20 + rnorm(20,sd=3))
# cond         xvar         yvar
#    A -4.252354091  3.473157275
#    A  1.702317971  0.005939612
#   ... 
#    B 17.793359218 19.718587761
#    B 19.319909163 19.647899863

ggplot(dat, aes(x=xvar, y=yvar)) +
  geom_point(shape=1)      # Use hollow circles

ggplot(dat, aes(x=xvar, y=yvar)) +
  geom_point(shape=1) +    # Use hollow circles
  geom_smooth(method=lm)   # Add linear regression line 
#  (by default includes 95% confidence region)


ggplot(dat, aes(x=xvar, y=yvar)) +
  geom_point(shape=1) +    # Use hollow circles
  geom_smooth(method=lm,   # Add linear regression line
              se=FALSE)    # Don't add shaded confidence region


ggplot(dat, aes(x=xvar, y=yvar)) +
  geom_point(shape=1) +    # Use hollow circles
  geom_smooth()            # Add a loess smoothed fit curve with confidence region

# -----------------------------------------------------------------------------

library(ggplot2)

pdf("/d/plot.pdf")
svg("/d/plot.svg")
#png("/d/plot.png")

bincol <- function(x,low,medium,high) {
  breaks <- function(x) pretty(range(x), n = nclass.Sturges(x), min.n = 1)
  
  colfunc <- colorRampPalette(c(low, medium, high))
  
  binned <- cut(x,breaks(x))
  
  res <- colfunc(length(unique(binned)))[as.integer(binned)]
  names(res) <- as.character(binned)
  res
}

labels <- unique(names(bincol(iris$Sepal.Length,"blue","yellow","red")))
breaks <- unique(bincol(iris$Sepal.Length,"blue","yellow","red"))
breaks <- breaks[order(labels,decreasing = TRUE)]
labels <- labels[order(labels,decreasing = TRUE)]


ggplot(iris) + 
  geom_point(aes(x=Sepal.Width, y=Sepal.Length,
                 colour=bincol(Sepal.Length,"blue","yellow","red")), size=4) +
  scale_color_identity("Sepal.Length", labels=labels, 
                       breaks=breaks, guide="legend")

dev.off()

# -----------------------------------------------------------------------------

library(ggplot2)
library(reshape2)
sp <- ggplot(tips, aes(x=total_bill, y=tip/total_bill)) + geom_point(shape=1)
sp
# Divide by levels of "sex", in the vertical direction
sp + facet_grid(sex ~ .)
# Divide by levels of "sex", in the horizontal direction
sp + facet_grid(. ~ sex)
# Divide with "sex" vertical, "day" horizontal
sp + facet_grid(sex ~ day)

# Divide by day, going horizontally and wrapping with 2 columns
sp + facet_wrap( ~ day, ncol=2)

sp + facet_grid(sex ~ day) +
  theme(strip.text.x = element_text(size=8, angle=75),
        strip.text.y = element_text(size=12, face="bold"),
        strip.background = element_rect(colour="red", fill="#CCCCFF"))

mf_labeller <- function(var, value){
  value <- as.character(value)
  if (var=="sex") { 
    value[value=="Female"] <- "Woman"
    value[value=="Male"]   <- "Man"
  }
  return(value)
}
sp + facet_grid(. ~ sex, labeller=mf_labeller)

# A histogram of bill sizes
hp <- ggplot(tips, aes(x=total_bill)) + geom_histogram(binwidth=2,colour="white")
# Histogram of total_bill, divided by sex and smoker
hp + facet_grid(sex ~ smoker)
# Same as above, with scales="free_y"
hp + facet_grid(sex ~ smoker, scales="free_y")
# With panels that have the same scaling, but different range (and therefore different physical sizes)
hp + facet_grid(sex ~ smoker, scales="free", space="free")

# -----------------------------------------------------------------------------
# 16. MATPLOT
# -----------------------------------------------------------------------------

fun1<-function(x) sin(cos(x)*exp(-x/2))
fun2<-function(x) sin(cos(x)*exp(-x/4))
x<-seq(0,2*pi,0.01)
matplot(x,cbind(fun1(x),fun2(x)),type="l",col=c("blue","red"))

# -----------------------------------------------------------------------------

#library(grDevices)

plot((-4:5)^2, main = "Quadratic")
matplot((-4:5)^2, main = "Quadratic") # almost identical to plot(*)
sines <- outer(1:20, 1:4, function(x, y) sin(x / 20 * pi * y))
matplot(sines, pch = 1:4, type = "o", col = rainbow(ncol(sines)))
matplot(sines, type = "b", pch = 21:23, col = 2:5, bg = 2:5,
        main = "matplot(...., pch = 21:23, bg = 2:5)")

x <- 0:50/50
matplot(x, outer(x, 1:8, function(x, k) sin(k*pi * x)),
        ylim = c(-2,2), type = "plobcsSh",
        main= "matplot(,type = \"plobcsSh\" )")
# pch & type =  vector of 1-chars :
matplot(x, outer(x, 1:4, function(x, k) sin(k*pi * x)),
        pch = letters[1:4], type = c("b","p","o"))

lends <- c("round", "butt", "square")
matplot(matrix(1:12, 4), type="c", lty=1, lwd=10, lend=lends)
text(cbind(2.5, 2*c(1,3,5)-.4), lends, col= 1:3, cex = 1.5)

table(iris$Species) # is data.frame with 'Species' factor
iS <- iris$Species == "setosa"
iV <- iris$Species == "versicolor"
op <- par(bg = "bisque")
matplot(c(1, 8), c(0, 4.5), type =  "n", xlab = "Length", ylab = "Width",
        main = "Petal and Sepal Dimensions in Iris Blossoms")
matpoints(iris[iS,c(1,3)], iris[iS,c(2,4)], pch = "sS", col = c(2,4))
matpoints(iris[iV,c(1,3)], iris[iV,c(2,4)], pch = "vV", col = c(2,4))
legend(1, 4, c("    Setosa Petals", "    Setosa Sepals",
               "Versicolor Petals", "Versicolor Sepals"),
       pch = "sSvV", col = rep(c(2,4), 2))

nam.var <- colnames(iris)[-5]
nam.spec <- as.character(iris[1+50*0:2, "Species"])
iris.S <- array(NA, dim = c(50,4,3),
                dimnames = list(NULL, nam.var, nam.spec))
for(i in 1:3) iris.S[,,i] <- data.matrix(iris[1:50+50*(i-1), -5])

matplot(iris.S[, "Petal.Length",], iris.S[, "Petal.Width",], pch = "SCV",
        col = rainbow(3, start = 0.8, end = 0.1),
        sub = paste(c("S", "C", "V"), dimnames(iris.S)[[3]],
                    sep = "=", collapse= ",  "),
        main = "Fisher's Iris Data")
par(op)

# -----------------------------------------------------------------------------
# 17. PLOT RIBBON
# -----------------------------------------------------------------------------

library(gcookbook)
library(ggplot2)

clim <- subset(climate, Source == "Berkeley",
               select=c("Year", "Anomaly10y", "Unc10y"))
ggplot(clim, aes(x=Year, y=Anomaly10y)) +
  geom_ribbon(aes(ymin=Anomaly10y-Unc10y, ymax=Anomaly10y+Unc10y),
              alpha=0.2) + geom_line()

# -----------------------------------------------------------------------------
# 18. LATTICE
# -----------------------------------------------------------------------------

library(lattice)

size=rnorm(20)
d=data.frame(sort(size))
dis=dist(d,diag=TRUE)
m=as.matrix(dis)

levelplot(m[1:ncol(m),ncol(m):1])

new.palette=colorRampPalette(c("black","red","yellow","white"),space="rgb")
levelplot(m[1:ncol(m),ncol(m):1],col.regions=new.palette(20))

---
  
quartz(width=7,height=6) #make a new quartz window of a given size
par(mar=c(2,3,2,1)) #set the margins of the figures to be smaller than default
layout(matrix(c(1,2),1,2,byrow=TRUE),widths=c(7,1)) #set the layout of the quartz window. This will create two plotting regions, with width ratio of 7 to 1
image(m[1:ncol(m),ncol(m):1],col=new.palette(20),xaxt="n",yaxt="n") #plot a heat map matrix with no tick marks or axis labels
axis(1,at=seq(0,1,length=20),labels=rep("",20)) #draw in tick marks
axis(2,at=seq(0,1,length=20),labels=rep("",20))

#adding a color legend
s=seq(min(m),max(m),length=20) #20 values between minimum and maximum values of m
l=matrix(s,ncol=length(s),byrow=TRUE) #coerce it into a horizontal matrix
image(y=s,z=l,col=new.palette(20),ylim=c(min(m),max(m)),xaxt="n",las=1) #plot a 

---
  
heatmap(m,symm=TRUE,col=new.palette(20))

# -----------------------------------------------------------------------------

# Lattice Examples
library(lattice)
attach(mtcars)

# create factors with value labels
gear.f<-factor(gear,levels=c(3,4,5),
               labels=c("3gears","4gears","5gears"))
cyl.f <-factor(cyl,levels=c(4,6,8),
               labels=c("4cyl","6cyl","8cyl"))

# kernel density plot
densityplot(~mpg,
            main="Density Plot",
            xlab="Miles per Gallon")

# kernel density plots by factor level
densityplot(~mpg|cyl.f,
            main="Density Plot by Number of Cylinders",
            xlab="Miles per Gallon")

# kernel density plots by factor level (alternate layout)
densityplot(~mpg|cyl.f,
            main="Density Plot by Numer of Cylinders",
            xlab="Miles per Gallon",
            layout=c(1,3))

# boxplots for each combination of two factors
bwplot(cyl.f~mpg|gear.f,
       ylab="Cylinders", xlab="Miles per Gallon",
       main="Mileage by Cylinders and Gears",
       layout=(c(1,3))
       
# scatterplots for each combination of two factors
xyplot(mpg~wt|cyl.f*gear.f,
      main="Scatterplots by Cylinders and Gears",
      ylab="Miles per Gallon", xlab="Car Weight")

# 3d scatterplot by factor level
cloud(mpg~wt*qsec|cyl.f,
     main="3D Scatterplot by Cylinders")

# dotplot for each combination of two factors
dotplot(cyl.f~mpg|gear.f,
       main="Dotplot Plot by Number of Gears and Cylinders",
       xlab="Miles Per Gallon")

# scatterplot matrix
splom(mtcars[c(1,3,4,5,6)],
     main="MTCARS Data")
       
# -----------------------------------------------------------------------------
# 19. ANIMATED PLOT
# -----------------------------------------------------------------------------

library(animation)
ani.options(interval = 0.3)
lln.ani(FUN = function(n, mu) rchisq(n, df = mu), mu = 5, cex = 0.6)

# -----------------------------------------------------------------------------

library(MASS)
= kde2d(x[, 1], x[, 2])
# perspective plot by persp()
persp(fit$x, fit$y, fit$z)

# -----------------------------------------------------------------------------

library(ggplot2)

dataset <- read.table(text='
    x   y
1   0 123
2   2 116
3   4 113
4  15 100
5  48  87
6  75  84
7 122  77', header=T)

# I think one possible specification would be a cubic linear model
fit <- lm(y ~ x + I(x^2) + I(x^3), data=dataset)
p <- predict(fit) # estimating the model and obtaining the fitted values from the model

# It fits good.
qplot(x, y, data=dataset, geom="line") # your plot black lines
last_plot() + geom_line(aes(x=x, y=p), col=2) # the fitted values red lines

# -----------------------------------------------------------------------------

# construct the data vectors using c()
xdata = c(-2,-1.64,-1.33,-0.7,0,0.45,1.2,1.64,2.32,2.9)
ydata = c(0.699369,0.700462,0.695354,1.03905,1.97389,2.41143,1.91091,0.919576,-0.730975,-1.42001)

# look at it
plot(xdata, ydata)

# some starting values
p1 = 1
p2 = 0.2
# do the fit
fit = nls(ydata ~ p1*cos(p2*xdata) + p2*sin(p1*xdata), start=list(p1=p1,p2=p2))
summary(fit)
new = data.frame(xdata = seq(min(xdata), max(xdata), len=200))
lines(new$xdata, predict(fit, newdata=new))
sum(resid(fit)^2)

# do the fit
fit = nls(ydata ~ sin(p1*xdata), start=list(p1=p1))
summary(fit)
new = data.frame(xdata = seq(min(xdata), max(xdata), len=200))
lines(new$xdata, predict(fit, newdata=new))
sum(resid(fit)^2)

# -----------------------------------------------------------------------------

library(nlme)
# variance increases with a power of the absolute fitted values
fm1 <- gnls(weight ~ SSlogis(Time, Asym, xmid, scal), Soybean,
            weights = varPower())
summary(fm1)

# -----------------------------------------------------------------------------

library(graphics)

DNase1 <- subset(DNase, Run == 1)

## using a selfStart model
fm1DNase1 <- nls(density ~ SSlogis(log(conc), Asym, xmid, scal), DNase1)
summary(fm1DNase1)
## the coefficients only:
coef(fm1DNase1)
## including their SE, etc:
coef(summary(fm1DNase1))

## using conditional linearity
fm2DNase1 <- nls(density ~ 1/(1 + exp((xmid - log(conc))/scal)),
                 data = DNase1,
                 start = list(xmid = 0, scal = 1),
                 algorithm = "plinear")
summary(fm2DNase1)

## without conditional linearity
fm3DNase1 <- nls(density ~ Asym/(1 + exp((xmid - log(conc))/scal)),
                 data = DNase1,
                 start = list(Asym = 3, xmid = 0, scal = 1))
summary(fm3DNase1)

## using Port's nl2sol algorithm
fm4DNase1 <- nls(density ~ Asym/(1 + exp((xmid - log(conc))/scal)),
                 data = DNase1,
                 start = list(Asym = 3, xmid = 0, scal = 1),
                 algorithm = "port")
summary(fm4DNase1)

## weighted nonlinear regression
Treated <- Puromycin[Puromycin$state == "treated", ]
weighted.MM <- function(resp, conc, Vm, K)
{
  ## Purpose: exactly as white book p. 451 -- RHS for nls()
  ##  Weighted version of Michaelis-Menten model
  ## ----------------------------------------------------------
  ## Arguments: 'y', 'x' and the two parameters (see book)
  ## ----------------------------------------------------------
  ## Author: Martin Maechler, Date: 23 Mar 2001
  
  pred <- (Vm * conc)/(K + conc)
  (resp - pred) / sqrt(pred)
}

Pur.wt <- nls( ~ weighted.MM(rate, conc, Vm, K), data = Treated,
               start = list(Vm = 200, K = 0.1))
summary(Pur.wt)

## Passing arguments using a list that can not be coerced to a data.frame
lisTreat <- with(Treated,
                 list(conc1 = conc[1], conc.1 = conc[-1], rate = rate))

weighted.MM1 <- function(resp, conc1, conc.1, Vm, K)
{
  conc <- c(conc1, conc.1)
  pred <- (Vm * conc)/(K + conc)
  (resp - pred) / sqrt(pred)
}
Pur.wt1 <- nls( ~ weighted.MM1(rate, conc1, conc.1, Vm, K),
                data = lisTreat, start = list(Vm = 200, K = 0.1))
stopifnot(all.equal(coef(Pur.wt), coef(Pur.wt1)))

## Chambers and Hastie (1992) Statistical Models in S  (p. 537):
## If the value of the right side [of formula] has an attribute called
## 'gradient' this should be a matrix with the number of rows equal
## to the length of the response and one column for each parameter.

weighted.MM.grad <- function(resp, conc1, conc.1, Vm, K)
{
  conc <- c(conc1, conc.1)
  
  K.conc <- K+conc
  dy.dV <- conc/K.conc
  dy.dK <- -Vm*dy.dV/K.conc
  pred <- Vm*dy.dV
  pred.5 <- sqrt(pred)
  dev <- (resp - pred) / pred.5
  Ddev <- -0.5*(resp+pred)/(pred.5*pred)
  attr(dev, "gradient") <- Ddev * cbind(Vm = dy.dV, K = dy.dK)
  dev
}

Pur.wt.grad <- nls( ~ weighted.MM.grad(rate, conc1, conc.1, Vm, K),
                    data = lisTreat, start = list(Vm = 200, K = 0.1))

rbind(coef(Pur.wt), coef(Pur.wt1), coef(Pur.wt.grad))

## In this example, there seems no advantage to providing the gradient.
## In other cases, there might be.


## The two examples below show that you can fit a model to
## artificial data with noise but not to artificial data
## without noise.
x <- 1:10
y <- 2*x + 3                            # perfect fit
yeps <- y + rnorm(length(y), sd = 0.01) # added noise
nls(yeps ~ a + b*x, start = list(a = 0.12345, b = 0.54321))
## terminates in an error, because convergence cannot be confirmed:
try(nls(y ~ a + b*x, start = list(a = 0.12345, b = 0.54321)))

## the nls() internal cheap guess for starting values can be sufficient:

x <- -(1:100)/10
y <- 100 + 10 * exp(x / 2) + rnorm(x)/10
nlmod <- nls(y ~  Const + A * exp(B * x))

plot(x,y, main = "nls(*), data, true function and fit, n=100")
curve(100 + 10 * exp(x / 2), col = 4, add = TRUE)
lines(x, predict(nlmod), col = 2)

## The muscle dataset in MASS is from an experiment on muscle
## O
## We first use the plinear algorithm to fit an overall model,
## ignoring that alpha and beta might vary with Strip.

musc.1 <- nls(Length ~ cbind(1, exp(-Conc/th)), muscle,
              start = list(th = 1), algorithm = "plinear")
summary(musc.1)

## Then we use nls' indexing feature for parameters in non-linear
## models to use the conventional algorithm to fit a model in which
## alpha and beta vary with Strip.  The starting values are provided
## by the previously fitted model.
## Note that with indexed parameters, the starting values must be
## given in a list (with names):
b <- coef(musc.1)
musc.2 <- nls(Length ~ a[Strip] + b[Strip]*exp(-Conc/th), muscle,
              start = list(a = rep(b[2], 21), b = rep(b[3], 21), th = b[1]))
summary(musc.2)

# -----------------------------------------------------------------------------
# 20. LINEAR REGRESSION
# -----------------------------------------------------------------------------

# Sample data
a=c(1,1,1,1,1,1,2,2,2,2,2,2,3,3,3,3,3,4,4,4,4,4,5,5,5,5,5)
b=c(2,2,2,4,8,2,4,3,7,4,4,4,6,10,6,6,7,8,8,8,3,8,10,2,8,10,10)
dat = as.data.frame(cbind(a,b),colnames=c("a","b"))
dat
# Create an "aggregated" dataset that has three columns: variable a, variable b, and the number of samples that have that combination of values. (you might notice that "newdat" actually has four columns... that is due to the inelegance of my code... but whatever, it does the trick)
newdat = aggregate(dat,by=list(a,b),length)
colnames(newdat)=c("a","b","N")
newdat
# a bubble plot of "newdat"
symbols(newdat$a, newdat$b, circles=newdat$N/20, inches=FALSE, xlab="variable a", ylab="variable b")
# a linear model of the relationship between a and b, from the original dataset ("dat")
mod=lm(dat$b ~ dat$a, data=dat)
summary(mod)
# draw the regression line on the bubble plot
lines(dat$a, predict(mod), lty=1, lwd=2)

# -----------------------------------------------------------------------------

lsfit(iris$Petal.Length, iris$Petal.Width)$coefficients
plot(iris$Petal.Length, iris$Petal.Width, pch=21, bg=c("red","green3","blue")[unclass(iris$Species)], main="Edgar Anderson's Iris Data", xlab="Petal length", ylab="Petal width")
abline(lsfit(iris$Petal.Length, iris$Petal.Width)$coefficients, col="black")

# -----------------------------------------------------------------------------

lm(Petal.Width ~ Petal.Length, data=iris)$coefficients
plot(iris$Petal.Length, iris$Petal.Width, pch=21, bg=c("red","green3","blue")[unclass(iris$Species)], main="Edgar Anderson's Iris Data", xlab="Petal length", ylab="Petal width")
abline(lm(Petal.Width ~ Petal.Length, data=iris)$coefficients, col="black")
summary(lm(Petal.Width ~ Petal.Length, data=iris))

# -----------------------------------------------------------------------------

plot(iris$Sepal.Width, iris$Sepal.Length, pch=21, bg=c("red","green3","blue")[unclass(iris$Species)], main="Edgar Anderson's Iris Data", xlab="Sepal Width", ylab="Sepal Length")
abline(lm(Sepal.Length ~ Sepal.Width, data=iris)$coefficients, col="black")
summary(lm(Sepal.Length ~ Sepal.Width, data=iris))

# -----------------------------------------------------------------------------

plot(iris$Sepal.Width, iris$Sepal.Length, pch=21, bg=c("red","green3","blue")[unclass(iris$Species)], main="Edgar Anderson's Iris Data", xlab="Petal length", ylab="Sepal length")

abline(lm(Sepal.Length ~ Sepal.Width, data=iris)$coefficients, col="black")
abline(lm(Sepal.Length ~ Sepal.Width, data=iris[which(iris$Species=="setosa"),])$coefficients, col="red")
abline(lm(Sepal.Length ~ Sepal.Width, data=iris[which(iris$Species=="versicolor"),])$coefficients, col="green3")
abline(lm(Sepal.Length ~ Sepal.Width, data=iris[which(iris$Species=="virginica"),])$coefficients, col="blue")

lm(Sepal.Length ~ Sepal.Width, data=iris[which(iris$Species=="setosa"),])$coefficients
lm(Sepal.Length ~ Sepal.Width, data=iris[which(iris$Species=="versicolor"),])$coefficients
lm(Sepal.Length ~ Sepal.Width, data=iris[which(iris$Species=="virginica"),])$coefficients

# -----------------------------------------------------------------------------

lm(Sepal.Length ~ Sepal.Width:Species + Species - 1, data=iris)$coefficients
lm(Sepal.Length ~ Sepal.Width:Species + Species - 1, data=iris)$coefficients
lm(Sepal.Length ~ Sepal.Width:Species + Species, data=iris)$coefficients

summary(lm(Sepal.Length ~ Sepal.Width:Species + Species - 1, data=iris))
summary(step(lm(Sepal.Length ~ Sepal.Width * Species, data=iris)))

# -----------------------------------------------------------------------------
# 21. LOGISTIC REGRESSION
# -----------------------------------------------------------------------------

x <- c(32,64,96,118,126,144,152.5,158)
y <- c(99.5,104.8,108.5,100,86,64,35.3,15)
#we will make y the response variable and x the predictor
#the response variable is usually on the y-axis
plot(x, y, pch=19)

#fit first degree polynomial equation:
fit  <- lm(y~x)
#second degree
fit2 <- lm(y ~ poly(x, 2, raw=TRUE))
#third degree
fit3 <- lm(y ~ poly(x, 3, raw=TRUE))
#fourth degree
fit4 <- lm(y ~ poly(x, 4, raw=TRUE))

#generate range of 50 numbers starting from 30 and ending at 160
xx <- seq(30,160, length=50)
plot(x, y, pch=19,ylim=c(0,150))
lines(xx, predict(fit, data.frame(x=xx)), col="red")
lines(xx, predict(fit2, data.frame(x=xx)), col="green")
lines(xx, predict(fit3, data.frame(x=xx)), col="blue")
lines(xx, predict(fit4, data.frame(x=xx)), col="purple")

anova(fit,fit2)

# -----------------------------------------------------------------------------

library("ggplot2")
d <- data.frame(x=1:100,y=sample(c(rep(1,100),0), 10000, replace=T))
plot(d)
M <- glm(y~x, family="binomial", data=d)
d$p <- predict(M, type="response")
chisq.test(table(d$p, d$y))
sum(residuals(M, type="pearson")^2)

# -----------------------------------------------------------------------------

library(data.table)
library(ggplot2)

#bodysize <- data.table(read.csv(file="/dataset/donut/donut.csv", sep=","))
bodysize <- data.table(read.csv(file="/dataset/bodysize/bodysize.csv", sep=","))
bodysize = as.data.frame(cbind(bodysize, survive))
bodysize

survive=bodysize$survive # assign 'survival' to these 20 individuals non-randomly... most mortality occurs at smaller body size
dat=as.data.frame(cbind(bodysize,survive)) # saves dataframe with two columns: body size & survival
dat # just shows you what your dataset looks like. It will look something like this:# creates a quartz window with title.

quartz(title="bodysize vs. survival")

# plot with body size on x-axis and survival (0 or 1) on y-axis.
plot(bodysize$bodysize, survive, xlab="Body size", ylab="Probability of survival")
# run a logistic regression model (in this case, generalized linear model with logit link). see ?glm.
g = glm(survive~bodysize, family=binomial, dat)
summary(g)

# draws a curve based on prediction from logistic regression model.
curve(predict(g, data.frame(bodysize=x), type="resp"), add=TRUE)

# optional: you could skip this draws an invisible set of points of body size survival based on a 'fit' to glm model. pch= changes type of dots.
points(bodysize, fitted(g), pch=20)

library(popbio)
logi.hist.plot(bodysize$bodysize, survive, boxp=FALSE, type="hist", col="gray")
  
# -----------------------------------------------------------------------------

dat <- structure(list(Response = c(1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 
                                   1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 0L, 0L, 0L, 0L, 0L, 
                                   0L, 0L), 
                      Temperature = c(29.33, 30.37, 29.52, 29.66, 29.57, 30.04, 
                                                            30.58, 30.41, 29.61, 30.51, 30.91, 30.74, 29.91, 29.99, 29.99, 
                                                            29.99, 29.99, 29.99, 29.99, 30.71, 29.56, 29.56, 29.56, 29.56, 
                                                            29.56, 29.57, 29.51)), 
                .Names = c("Response", "Temperature"), 
                 class = "data.frame", row.names = c(NA, -27L))

temperature.glm <- glm(Response ~ Temperature, data=dat, family=binomial)

plot(dat$Temperature, dat$Response, xlab="Temperature", 
     ylab="Probability of Response")
curve(predict(temperature.glm, data.frame(Temperature=x), type="resp"), 
      add=TRUE, col="red")
# To add an additional curve, e.g. that which corresponds to 'Set 1':
curve(plogis(-88.4505 + 2.9677*x), min(dat$Temperature), 
      max(dat$Temperature), add=TRUE, lwd=2, lty=3)
legend('bottomright', c('temp.glm', 'Set 1'), lty=c(1, 3), 
       col=2:1, lwd=1:2, bty='n', cex=0.8)

# -----------------------------------------------------------------------------

library(data.table)
library(ggplot2)

donut <- data.table(read.csv(file="/dataset/donut/donut.csv", sep=","))
donut

g = glm(color-1 ~ x+y, family=binomial, donut)
summary(g)

# plot with body size on x-axis and survival (0 or 1) on y-axis
plot(donut$x,(donut$color-1))
curve(predict(g,data.frame(x=x,y=mean(donut$y)), type="resp"), add=TRUE)

# -----------------------------------------------------------------------------

library("MASS")
data(menarche)
str(menarche)
summary(menarche)
plot(Menarche/Total ~ Age, data=menarche)
glm.out = glm(cbind(Menarche, Total-Menarche) ~ Age, family=binomial(logit), data=menarche)
plot(Menarche/Total ~ Age, data=menarche)
lines(menarche$Age, glm.out$fitted, type="l", col="red")
title(main="Menarche Data with Fitted Logistic Regression Line")
summary(glm.out)

# -----------------------------------------------------------------------------

file = "/dataset/gorilla/gorilla.csv"
read.csv(file) -> gorilla
str(gorilla)
cor(gorilla) 
with(gorilla, tapply(W, seen, mean))
with(gorilla, tapply(C, seen, mean))
glm.out = glm(seen ~ W * C * CW, family=binomial(logit), data=gorilla)
summary(glm.out)
anova(glm.out, test="Chisq")
1 - pchisq(8.157, df=7)
plot(glm.out$fitted)
abline(v=30.5,col="red")
abline(h=.3,col="green")
abline(h=.5,col="green")
text(15,.9,"seen = 0")
text(40,.9,"seen = 1")

# -----------------------------------------------------------------------------

ftable(UCBAdmissions, col.vars="Admit") 
dimnames(UCBAdmissions)
margin.table(UCBAdmissions, c(2,1))
ucb.df = data.frame(gender=rep(c("Male","Female"),c(6,6)),
                  dept=rep(LETTERS[1:6],2),
                  yes=c(512,353,120,138,53,22,89,17,202,131,94,24),
                  no=c(313,207,205,279,138,351,19,8,391,244,299,317))
ucb.df
mod.form = "cbind(yes,no) ~ gender * dept"
glm.out = glm(mod.form, family=binomial(logit), data=ucb.df)
options(show.signif.stars=F)         # turn off significance stars (optional)
anova(glm.out, test="Chisq")
summary(glm.out)
exp(-1.0521)
1/exp(-1.0521)

exp(-2.2046)
exp(-2.2046) / exp(-2.1662)

exp(-2.2046)

# -----------------------------------------------------------------------------

# Confusion Matrix
library(MASS)
iris.lda <- lda(Species ~ . , data = iris)
table(predict(iris.lda, type="class")$class, iris$Species)

# -----------------------------------------------------------------------------
# 22. SURVIVAL ANALYSIS
# -----------------------------------------------------------------------------

# Mayo Clinic Lung Cancer Data
library(survival)

# learn about the dataset
help(lung)

# create a Surv object
survobj <- with(lung, Surv(time,status))

# Plot survival distribution of the total sample
# Kaplan-Meier estimator
fit0 <- survfit(survobj~1, data=lung)
summary(fit0)
plot(fit0, xlab="Survival Time in Days",
     ylab="% Surviving", yscale=100,
     main="Survival Distribution (Overall)")

# Compare the survival distributions of men and women
fit1 <- survfit(survobj~sex,data=lung)

# plot the survival distributions by sex
plot(fit1, xlab="Survival Time in Days",
     ylab="% Surviving", yscale=100, col=c("red","blue"),
     main="Survival Distributions by Gender")
legend("topright", title="Gender", c("Male", "Female"),
       fill=c("red", "blue"))

# test for difference between male and female
# survival curves (logrank test)
survdiff(survobj~sex, data=lung)

# predict male survival from age and medical scores
MaleMod <- coxph(survobj~age+ph.ecog+ph.karno+pat.karno,
                 data=lung, subset=sex==1)

# display results
MaleMod

# evaluate the proportional hazards assumption
cox.zph(MaleMod) 

# -----------------------------------------------------------------------------
# 23. CONTROL CHART
# -----------------------------------------------------------------------------
library(ggplot2)
library(plyr)
library(qcc)
# -----------------------------------------------------------------------------

v <- read.table(text='
    V1   V2
1   0 123
2   2 16
3   4 123
4  15 10
5  48  123
6  75  84
7 122  77', header=T)
lcl <- mean(v$V2) - 3.0 * sd(v$V2)
ucl <- mean(v$V2) + 3.0 * sd(v$V2)
ymin <- mean(v$V2) - 4.0 * sd(v$V2)
ymax <- mean(v$V2) + 4.0 * sd(v$V2)
xgr <- c(0,dim(v)[1])
ygr <- c(ymin,ymax)
X11(height=4,width=8)
plot(xgr, ygr, type="n", xlab="Observation", ylab="Density", main="Density Control Chart",col.main="darkblue")
lines(xgr, c(lcl,lcl), col="#bfbfcf",lty=2)
lines(xgr, c(ucl,ucl), col="#bfbfcf",lty=2)
m <- (ucl + lcl) / 2.0
lines(xgr, c(m,m))
lines(v, col="firebrick", lwd=2)

# -----------------------------------------------------------------------------

x <- c(50, 50)
qcc(x, type = "xbar.one")

x <- c(45, 55)
qcc(x, type = "xbar.one")

x <- c(30, 50)
qcc(x, type = "xbar.one")

x <- c(36, 41, 44, 41, 40, 41, 40, 41, 40, 50, 40, 47, 43, 41, 40, 41, 40, 41, 40, 39, 40, 41, 40, 41, 40, 41, 40, 41)
qcc(x, type = "xbar.one")
m <- matrix(x, ncol=1, byrow=FALSE)
m
qcc(m, type = "xbar.one")

# -----------------------------------------------------------------------------

x <- c(1, 2, 3)
x
y <- c(30, 50, 45)
y
c <- c(x, y)
c
m <- matrix(c, nrow=3, ncol=2, byrow=FALSE)
m
qcc(m, type = "xbar")
mqcc(m)

# -----------------------------------------------------------------------------

v <- read.table(text='
    V1
1   123
2   116
3   123
4   20
5   123
6   84
7   77
8   77
9   77
10  77
11  77
', header=T)
v
qcc(v, type = "xbar.one")
m <- as.matrix(v)
m
qcc(m, type = "xbar.one")

# -----------------------------------------------------------------------------

v <- read.table(text='
V1 V2
1   1  123
2   2  16
3   3  123
4   4  10
5   5  123
6   6  84
7   7  77
8   8  77
9   9  77
10 10  77
11 11  77
', header=T)
v
qcc(v, type = "xbar")
m <- as.matrix(v)
m
qcc(m, type = "xbar")

# -----------------------------------------------------------------------------

data(pistonrings)
attach(pistonrings)
diameter <- qcc.groups(diameter, sample)
diameter[1:25,]
diameter[1:25,1:2]
qcc(diameter[1:25,1:2], type="xbar")
qcc(diameter[1:25,], type="xbar")
qcc(diameter[1:25,], type="xbar", newdata=diameter[26:40,])
q <- qcc(diameter[1:25,], type="xbar", newdata=diameter[26:40,], plot=FALSE)
plot(q, chart.all=FALSE)
detach(pistonrings)

# -----------------------------------------------------------------------------

#make 2 plots in 1 figure
par(mfrow=c(1,2))
#points have base value of 10 w/ normally distributed error
lugnuts <- rep(10, 100) + rnorm(100, mean=0, sd=0.5)
qcc(lugnuts, type="xbar.one", center=10, add.stats=FALSE,
    title="1st Batch", xlab="i-th lugnut produced")
#first 90 points have base value of 10 w/ normally distributed error,
#last 10 points have base value of 11 w/ normally distributed error
lugnuts <- c(rep(10, 90), rep(11, 10)) + rnorm(100, mean=0, sd=0.5)
qcc(lugnuts, type="xbar.one", center=10, add.stats=FALSE,
    title="2nd Batch", xlab="i-th lugnut produced")
#example using holdout/test sets
lugnuts <- rep(10, 100) + rnorm(100, mean=0, sd=0.5)
qcc(lugnuts, newdata=rep(11, 10) + rnorm(10, mean=0, sd=0.5),
    type="xbar.one", center=10, add.stats=FALSE, title="2nd Batch", xlab="i-th lugnut produced")

# -----------------------------------------------------------------------------

data(pistonrings)
attach(pistonrings)
diameter <- qcc.groups(diameter, sample)
q <- cusum(diameter[1:25,], decision.interval = 4, se.shift = 1)
summary(q)
q <- cusum(diameter[1:25,], newdata=diameter[26:40,])
summary(q)
plot(q, chart.all=FALSE)

# -----------------------------------------------------------------------------

# Multivariate Quality Control Subgrouped data
# Ryan (2000, Table 9.2) data with p = 2 variables, m = 20 samples, n = 4 sample size:
X1 = matrix(c(72, 56, 55, 44, 97, 83, 47, 88, 57, 26, 46,
              49, 71, 71, 67, 55, 49, 72, 61, 35, 84, 87, 73, 80, 26, 89, 66,
              50, 47, 39, 27, 62, 63, 58, 69, 63, 51, 80, 74, 38, 79, 33, 22,
              54, 48, 91, 53, 84, 41, 52, 63, 78, 82, 69, 70, 72, 55, 61, 62,
              41, 49, 42, 60, 74, 58, 62, 58, 69, 46, 48, 34, 87, 55, 70, 94,
              49, 76, 59, 57, 46), ncol = 4)
X2 = matrix(c(23, 14, 13, 9, 36, 30, 12, 31, 14, 7, 10,
              11, 22, 21, 18, 15, 13, 22, 19, 10, 30, 31, 22, 28, 10, 35, 18,
              11, 10, 11, 8, 20, 16, 19, 19, 16, 14, 28, 20, 11, 28, 8, 6,
              15, 14, 36, 14, 30, 8, 35, 19, 27, 31, 17, 18, 20, 16, 18, 16,
              13, 10, 9, 16, 25, 15, 18, 16, 19, 10, 30, 9, 31, 15, 20, 35,
              12, 26, 17, 14, 16), ncol = 4)
X1
plot(X1)
X2
plot(X2)
X = list(X1 = X1, X2 = X2)
X
q = mqcc(X, type = "T2")
summary(q)
ellipseChart(q)
ellipseChart(q, show.id = TRUE)
q = mqcc(X, type = "T2", pred.limits = TRUE)

# Ryan (2000) discussed Xbar-charts for single variables computed adjusting the
# confidence level of the T^2 chart:
q1 = qcc(X1, type = "xbar", confidence.level = q$confidence.level^(1/2))
summary(q1)
q2 = qcc(X2, type = "xbar", confidence.level = q$confidence.level^(1/2))
summary(q2)
require(MASS)
# generate new "in control" data
Xnew = list(X1 = matrix(NA, 10, 4), X2 = matrix(NA, 10, 4))
for(i in 1:4) { 
  x = mvrnorm(10, mu = q$center, Sigma = q$cov)
  Xnew$X1[,i] = x[,1]
  Xnew$X2[,i] = x[,2]
}
qq = mqcc(X, type = "T2", newdata = Xnew, pred.limits = TRUE)
summary(qq)
# generate new "out of control" data
Xnew = list(X1 = matrix(NA, 10, 4), X2 = matrix(NA, 10, 4))
for(i in 1:4) { 
  x = mvrnorm(10, mu = 1.2*q$center, Sigma = q$cov)
  Xnew$X1[,i] = x[,1]
  Xnew$X2[,i] = x[,2]
}
qq = mqcc(X, type = "T2", newdata = Xnew, pred.limits = TRUE)
summary(qq)

# -----------------------------------------------------------------------------

data(pistonrings)
attach(pistonrings)
diameter <- qcc.groups(diameter, sample)
beta <- oc.curves.xbar(qcc(diameter, type="xbar", nsigmas=3, plot=FALSE))
print(round(beta, digits=4))
# or to identify points on the plot use
# Not run: oc.curves.xbar(qcc(diameter, type="xbar", nsigmas=3, plot=FALSE), identify=TRUE)
detach(pistonrings)

# -----------------------------------------------------------------------------

# Individual observations data
data(boiler)
q = mqcc(boiler, type = "T2.single", confidence.level = 0.999)
summary(q)
# generate new "in control" data
boilerNew = mvrnorm(10, mu = q$center, Sigma = q$cov)
qq = mqcc(boiler, type = "T2.single", confidence.level = 0.999, newdata = boilerNew, pred.limits = TRUE)
summary(qq)
# generate new "out of control" data
boilerNew = mvrnorm(10, mu = 1.01*q$center, Sigma = q$cov)
qq = mqcc(boiler, type = "T2.single", confidence.level = 0.999, newdata = boilerNew, pred.limits = TRUE)
summary(qq)

# -----------------------------------------------------------------------------

# provides "robust" estimates of means and covariance matrix
library(MASS)
rob = cov.rob(boiler)
qrob = mqcc(boiler, type = "T2.single", center = rob$center, cov = rob$cov)
summary(qrob)

# -----------------------------------------------------------------------------

find_zones <- function(x) {
  x.mean <- mean(x)
  x.sd <- sd(x)
  boundaries <- seq(-4, 4)
  # creates a set of zones for each point in x
  zones <- sapply(boundaries, function(i) {
    i * rep(x.sd, length(x))
  })
  zones + x.mean
}

head(find_zones(x))

evaluate_zones <- function(x) {
  zones <- find_zones(x)
  colnames(zones) <- paste("zone", -4:4, sep="_")
  x.zones <- rowSums(x > zones) - 4
  x.zones
}

evaluate_zones(x)
# [1] 0 2 0 1 2 0 0 1 -1 0 -1 1 1 1 -2 1 ...

find_violations <- function(x.zones, i) {
  values <- x.zones[max(i-8, 1):i]
  rule4 <- ifelse(all(values > 0), 1,
                  ifelse(all(values < 0), -1,
                         0))
  
  values <- x.zones[max(i-5, 1):i]
  rule3 <- ifelse(sum(values >= 2) >= 4, 1,
                  ifelse(sum(values <= -2) >= 4, -1,
                         0))
  values <- x.zones[max(i-3, 1):i]
  rule2 <- ifelse(sum(values >= 3) >= 2, 1,
                  ifelse(sum(values <= -3) >= 2, -1,
                         0))
  values <- x.zones[i]
  rule1 <- ifelse(any(values > 3), 1,
                  ifelse(any(values < -3), -1,
                         0))
  c("rule1"=rule1, "rule2"=rule2, "rule3"=rule3, "rule4"=rule4)
}

find_violations(evaluate_zones(x), 70)

compute_violations <- function(x, start=1) {
  x.zones <- evaluate_zones(x)
  results <- ldply(start:length(x), function(i) {
    find_violations(x.zones, i)
  })
  results$color <- ifelse(results$rule1!=0, "pink",
                          ifelse(results$rule2!=0, "red",
                                 ifelse(results$rule3!=0, "orange",
                                        ifelse(results$rule4!=0, "yellow",
                                               "black"))))
  results
}

tail(compute_violations(x))

plot.wer <- function(x, holdout) {
  wer <- compute_violations(x, length(x) - holdout)
  bands <- find_zones(x)
  plot.data <- x[(length(x) - holdout):length(x)]
  plot(plot.data, col=wer$color, type='b', pch=19,
       ylim=c(min(bands), max(bands)),
       main="Western Eletric Rule Ouput",
       xlab="", ylab="")
  for (i in 1:7) {
    lines(bands[,i], col=cols[i], lwd=0.75, lty=2)
  }
}

x <- c(rep(10, 90), rep(10.5, 10)) + rnorm(100, mean=0, sd=0.5)
plot.wer(x, 30)

# -----------------------------------------------------------------------------
# 24. CHANGE POINT
# -----------------------------------------------------------------------------
library(bcp)
library(bcpa)
library(changepoint)
library(ecp)
library(strucchange)
# -----------------------------------------------------------------------------

# change in mean
y = c(rnorm(100,0,1), rnorm(100,5,1))
ansmean = cpt.mean(y)
plot(ansmean,cpt.col)
print(ansmean)
attributes(ansmean)

# change in mean and variance
z = c(rnorm(100,0,1), rnorm(100,2,10), rnorm(100,0,1))
ansmeanvar = cpt.meanvar(z)
plot(ansmeanvar, cpt.width=3)
print(ansmeanvar)

# change in variance
x = c(rnorm(100,0,1),rnorm(100,0,10))
ansvar = cpt.var(x)
plot(ansvar)
print(ansvar)

# change in variance
x = c(rnorm(100,0,1), rnorm(100,0,10), rnorm(100,0,1))
ansvar = cpt.var(x)
plot(ansvar)
cpts(ansvar)
print(ansvar)
attributes(ansvar)
attr(ansvar, "date")
a <- attr(ansvar, "cpts")
a[1]
a[2]

# -----------------------------------------------------------------------------

data("Nile")
plot(Nile)
bp.nile <- breakpoints(Nile ~ 1)
ci.nile <- confint(bp.nile, breaks = 1)
lines(ci.nile)

# -----------------------------------------------------------------------------

set.seed(250)
period1 <- rnorm(100)
period2 <- rnorm(100,0,3)
period3 <- rnorm(100,2,1)
period4 <- rnorm(100,2,4)
Xnorm <- matrix(c(period1,period2,period3,period4),ncol=1)
Xnorm
output1 <- e.divisive(Xnorm, R = 499, alpha = 1)
output2 <- e.divisive(Xnorm, R = 499, alpha = 2)
output2$estimates
output1$k.hat
output1$order.found
output1$estimates
output1$considered.last
output1$p.values
output1$permutations
ts.plot(Xnorm,ylab='Value',main='Change in a Univariate Gaussian Sequence')
abline(v=c(101,201,301), col='blue')
abline(v=output1$estimates[],col='red',lty=2)
abline(v=output1$estimates[c(-1,-5)],col='red',lty=2)

# -----------------------------------------------------------------------------

# Example of multiple changes in mean and variance at 50,100,150 in simulated Exponential data.
set.seed(1)
x = c(rexp(50,rate=1), rexp(50,rate=5), rexp(50,rate=1), rexp(50,rate=10))
plot(x)
binseg.meanvar.exp(x,Q=5,pen=2*log(200)) # returns optimal number as 3 and the locations as
#c(50,100,150)
binseg.meanvar.exp(x,Q=2,pen=2*log(200))
# returns optimal number as 2 as this is the maximum number of changepoints it can find. If you get the maximum number, you need to increase Q until this is not the case.
# Example no change in mean or variance
set.seed(1)
x = rexp(200, rate=1)
plot(x)
binseg.meanvar.exp(x,pen=2*log(200)) # returns optimal number as 0

# -----------------------------------------------------------------------------

# A random sample from a few normal distributions.
testdata <- c(rnorm(50), rnorm(50, 5, 1), rnorm(50))
bcp.0 <- bcp(testdata)
plot.bcp(bcp.0)
#legacyplot(bcp.0)
f <- fitted.bcp(bcp.0)
f
plot(f)

# -----------------------------------------------------------------------------

testdata <- cbind( c(rnorm(50), rnorm(50, -5, 1), rnorm(50)),
                   c(rnorm(50), rnorm(50, 10.8, 1), rnorm(50, -3, 1)) )
bcp.0 <- bcp(testdata)
plot.bcp(bcp.0)
plot.bcp(bcp.0, separated=TRUE)
attributes(bcp.0)
summary.bcp(bcp.0)
bcp.0$data
bcp.0$blocks
bcp.0$p0

# -----------------------------------------------------------------------------

# A random sample from a few normal distributions #####
testdata <- c(rnorm(50), rnorm(50, 5, 1), rnorm(50))
bcp.0 <- bcp(testdata, return.mcmc = TRUE)
plot.bcp(bcp.0)
fitted.bcp(bcp.0)

# -----------------------------------------------------------------------------

# Simulating a gappy, Gaussian, time series
rho <- 0.8
x.full <- arima.sim(1000, model = list(ar = rho))
t.full <- 1:1000
keep <- sort(sample(1:1000, 200))
x <- x.full[keep]
t <- t.full[keep]
plot(t, x, type = "l")

rhos <- seq(0,.99,.01)
L <- rep(NA, length(rhos))
for(i in 1:length(rhos))
  L[i] <- GetL(x,t,rhos[i])
# plot likelihood profile
plot(rhos, L, type="l")
abline(v = rho, lty=2, lwd=2); abline(v = rhos[L == max(L)], lty=3, lwd=2)
legend("bottomleft", legend=c("true value","MLE"), lty=2:3, lwd=2)

GetRho(x, t, tau = FALSE)
GetRho(x, t, tau = TRUE)

# -----------------------------------------------------------------------------

par(bty="l")
mu1 <- 5; mu2 <- 3
sigma1 <- 2; sigma2 <- 1
rho1 <- 0.5; rho2 <- 0.5
SimTS <- function(n, mu, rho, sigma) {
  X.standard <- arima.sim(n, model=list(ar = rho))
  X.standard/sd(X.standard)*sigma + mu
}
# create time series with break at 500
t.full <- 1:1000
t.break <- 500
x.full <- c(SimTS(t.break, mu1, rho1, sigma1),
            SimTS(max(t.full)-t.break+1, mu2, rho2, sigma2))
# subsample 100 observations and estimate
keep <- sort(sample(1:length(x.full), 100))
x <- x.full[keep]
t <- t.full[keep]
(BB <- GetBestBreak(x,t, tau=FALSE))

plot(t,x, type="l")
abline(v = 500, col=2, lwd=2, lty=2); abline(v = BB[2], col=2, lwd=2, lty=3)
legend("topright", legend=c("true break", "estimated break"), col=2, lwd=2, lty=2:3)

# -----------------------------------------------------------------------------

data("RealInt")
bcp.ri <- bcp(as.vector(RealInt))
plot.bcp(bcp.ri)
# to see bcp and Bai and Perron results run:
if (require("strucchange")) {
  bp <- breakpoints(RealInt ~ 1, h = 2)$breakpoints
  rho <- rep(0, length(RealInt))
  rho[bp] <- 1
  b.num<-1 + c(0,cumsum(rho[1:(length(rho)-1)]))
  bp.mean <- unlist(lapply(split(RealInt,b.num),mean))
  bp.ri <- rep(0,length(RealInt))
  for (i in 1:length(bp.ri)) bp.ri[i] <- bp.mean[b.num[i]]
  xax <- seq(1961, 1987, length=103)
  op<-par(mfrow=c(2,1),col.lab="black",col.main="black")
  op2 <- par(mar=c(0,4,4,2),xaxt="n", cex.axis=0.75)
  plot(1:length(bcp.ri$data), bcp.ri$data, col="grey", pch=20,
       xlab="", ylab="Posterior Mean", main="U.S. Ex-Post Interest Rate")
  lines(bcp.ri$posterior.mean, lwd=2)
  lines(bp.ri, col="blue")
  par(op2)
  op3 <- par(mar=c(5,4,0,2), xaxt="s", cex.axis=0.75)
  plot(xax, bcp.ri$posterior.prob, yaxt="n", type="l", ylim=c(0,1),
       xlab="Year", ylab="Posterior Probability", main="")
  for (i in 1:length(bp.ri)) abline(v=xax[bp[i]], col="blue")
  axis(2, yaxp=c(0, 0.9, 3))
  par(op3)
  par(op)
} else {
  cat("strucchange is not loaded")
}

# -----------------------------------------------------------------------------
# 25. FORECAST
# -----------------------------------------------------------------------------
library(forecast)
# -----------------------------------------------------------------------------

value <- c(1.2, 1.7, 1.6, 1.2, 1.6, 1.3, 1.5, 1.9, 5.4, 4.2, 5.5, 6, 5.6, 
           6.2, 6.8, 7.1, 7.1, 5.8, 0, 5.2, 4.6, 3.6, 3, 3.8, 3.1, 3.4, 
           2, 3.1, 3.2, 1.6, 0.6, 3.3, 4.9, 6.5, 5.3, 3.5, 5.3, 7.2, 7.4, 
           7.3, 7.2, 4, 6.1, 4.3, 4, 2.4, 0.4, 2.4)
sensor<-ts(value,frequency=24)
fit <- auto.arima(sensor)
LH.pred<-predict(fit,n.ahead=24)
plot(sensor,ylim=c(0,10),xlim=c(0,5),type="o", lwd="1")
lines(LH.pred$pred,col="red",type="o",lwd="1")
grid()

# -----------------------------------------------------------------------------

value <- c(1.2,1.7,1.6, 1.2, 1.6, 1.3, 1.5, 1.9, 5.4, 4.2, 5.5, 6.0, 5.6, 6.2, 6.8, 
           7.1, 7.1, 5.8, 0.0, 5.2, 4.6, 3.6, 3.0, 3.8, 3.1, 3.4, 2.0, 3.1, 3.2, 1.6, 
           0.6, 3.3, 4.9, 6.5, 5.3, 3.5, 5.3, 7.2, 7.4, 7.3, 7.2, 4.0, 6.1, 4.3, 4.0, 
           2.4, 0.4, 2.4, 1.2,1.7,1.6, 1.2, 1.6, 1.3, 1.5, 1.9, 5.4, 4.2, 5.5, 6.0, 
           5.6, 6.2, 6.8, 7.1, 7.1, 5.8, 0.0, 5.2, 4.6, 3.6, 3.0, 3.8, 3.1, 3.4, 2.0,
           3.1, 3.2, 1.6, 0.6, 3.3, 4.9, 6.5, 5.3, 3.5, 5.3, 7.2, 7.4, 7.3, 7.2, 4.0, 
           6.1, 4.3, 4.0, 2.4, 0.4, 2.4)
# consider adding a start so you get nicer labelling on your chart.
sensor <- ts(value,frequency=24)
fit <- auto.arima(sensor)
fcast <- forecast(fit)
plot(fcast)
grid()
fcast

# -----------------------------------------------------------------------------
