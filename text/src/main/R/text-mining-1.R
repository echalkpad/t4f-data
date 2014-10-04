library("NLP")
library("tm")
library("RColorBrewer")
library("wordcloud")
library("stringr")
#library("ff")
library("data.table")
library("plyr")
library("ggplot2")
library("qdapDictionaries")
library("qdapTools")
library("qdap")
library("bitops")
library("RCurl")
library("RWeka")
library("textcat")
library("SnowballC")

loadData = TRUE
sampleData = TRUE

INDIR<-paste(Sys.getenv(""),"folder", sep="")
setwd(INDIR)
dir.create("out")

inputFile <- paste("CDS_INTERACTION_NOTESV2", Sys.getenv("SYSTEM"),".csv",sep="")

inputFilePernalBk <- paste("PersonnalBkNoteId",S ys.getenv("SYSTEM"),".csv",sep="")

if( loadData ){
  
  vars <- names(read.csv(file=inputFile,nrows=1,sep=";"))
  v <- rep("NULL",length(vars))
  selected <- c("^DES_CONTENT$")
  tmp<-str_c(selected,collapse="|")
  v[grepl(tmp,vars)] <- "character"
  commFull <- read.csv(file=inputFile,
                       sep=";",
                       stringsAsFactor=FALSE,
                       colClasses=v,
#                       fileEncoding="UTF-8-BOM", # BOM thingy, specific to windows (actually from windows system!)
                       quote="")    # disable quoting
}

#------------------------------------------------------------------------------

wordPattern = 'fortis'

sampleSize=1400000 #necessary if we do not want to use the grep on the vector in the code below

if (sampleData) {

	comm <- tolower(commFull[sample(nrow(commFull),
                                  sampleSize,
                                  replace =FALSE),])
  #comm <- tolower(commFull)
  comm <- comm[comm!=""]
  
  comm <- removePunctuation(comm) 
  comm <- removeNumbers(comm) #temporary not used 

  comm <- comm[grep(wordPattern,comm)] # select a subset based on a word
  
  commCorp <- Corpus(VectorSource(comm))
  #Stop word vector creation
  swNL <- as.character(read.csv("Stop_word_Dutch",
                                header=FALSE,
                                fileEncoding="utf-8")$V1)
  swNL2 <- as.character(read.csv("stop-words_dutch_2_nl.txt",
                                header=FALSE,
                                fileEncoding="utf-8")$V1)
  swFR <- as.character(read.csv("Stop_word_French",
                                header=FALSE,
                                fileEncoding="utf-8")$V1)
  swFR2 <- as.character(read.csv("stop-words_french_2_fr.txt",
                                header=FALSE,
                                fileEncoding="utf-8")$V1)
  swING <- as.character(read.csv("stop-words-ING-DD",
                                 header=FALSE,
                                 fileEncoding="utf-8")$V1)

  
  commCorp <- tm_map(commCorp,stripWhitespace)  # has to be done in two steps
  commCorp <- tm_map(commCorp, removeWords, c(stopwords("dutch"),stopwords("french"),swNL,swFR,swNL2,swFR2,swING))
  
  comm <- laply(commCorp,function(x) x$content)
  

  #stemming creation
  commStem <- strsplit(comm, " ") # place word into vector
  #not necessary during text mining with word target for a start
  commNotVowel <- llply(commStem, function(x) x[!grepl("a|e|i|o|u|y",x)])
  commNotVowel <-word_list(laply(commNotVowel,function(x) str_c(x[x!=""],collapse=" ")),alphabetical=FALSE,cut.n=1000)
  commStem <- llply(commStem, function(x) wordStem(x,language="dutch"))
  commStem <- laply(commStem, function(x) paste(x,collapse=" "))

  #termDocumentMatrix creation
  if(length(comm) < 30000){ # due to the limit of R to process a matrix of mio colomn and line, I decided to put a limit o his creation in order to prevent dump crash
    commTDM <- TermDocumentMatrix(commCorp, control=list(c(wordLengths=c(2,Inf))))
  }
}

#------------------------------------------------------------------------------

#print a wordcloud 
#wordcloud(comm,min.freq=sampleSize/100)

pal2 <- brewer.pal(8,"Dark2")
wordcloud(comm,min.freq=sampleSize/1000,scale=c(9,.1), max.words=Inf, random.order=F,rot.per=.15, colors = pal2)

#word count

  #   count te number of words in a texr
  #   word_count(comm,byrow=TRUE,names=TRUE)

wc <- word_list(comm,alphabetical=FALSE,cut.n=100)
wc

findFreqTerms(commTDM,lowfreq=50)
findAssocs(commTDM, c('w1','w2'),0.20) # provide a correlation with a keyword
findAssocs(commTDM, c('w4','w5'),0.20)
findAssocs(commTDM, c('w5'),0.1)

#------------------------------------------------------------------------------

word_proximity(comm, terms=c("kbc","dexia"))
commTDM <- TermDocumentMatrix(commCorp, control=list(tokenize = NGramTokenizer))

commTest <- tm_map(comm,stemDocument)

#------------------------------------------------------------------------------

#freq of word for dematerialisatie
wc <- word_list(comm,alphabetical=TRUE,cut.n=1000)
wctab<-wc$rfswl$all
findAssocs(commTDM, c('fortis'),0.10)
wctab[wctab$WORD=="fortis",]
