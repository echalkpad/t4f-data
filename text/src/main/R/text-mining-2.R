library("tm")
library("wordcloud")
library("stringr")
library("ff")
library("data.table")
library("plyr")

loadData = TRUE
sampleData = TRUE

INDIR<-paste(Sys.getenv(""),"folder", sep="")
setwd(INDIR)
dir.create("out")

inputFile <- paste("file",SYSTEM,".csv",sep="")

if( loadData ){
  vars <- names(read.csv(file=inputFile,nrows=1,sep=";"))

  v <- rep("NULL",length(vars))
  selected <- c("^DES_CONTENT$")
  tmp<-str_c(selected,collapse="|")
  v[grepl(tmp,vars)] <- "character"
  commFull <- read.csv(file=inputFile,colClasses=v,sep=";")
}

matchKey <- c("test1","test2",)

sampleSize=200000
if (sampleData) {
  comm <- tolower(commFull[sample(nrow(commFull),
                                  sampleSize,
                                  replace =FALSE),])
  commbank <- unlist(laply(str_match_all(comm,str_c(matchKey,collapse="|")),function(x) str_c(x,collapse=" ")))
  commbank <- commbank[commbank!=""]
#  comm<- laply(comm, function(x) gsub("[0-9][0-9]*","",x))
}

wordcloud(commbank)
