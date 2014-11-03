-------------------------------------------------------------------------------
```
 _____ ___ _____
|_   _| | |   __|
  | | |_  |   __|
  |_|   |_|__|

 #t4f-data-parser-ebnf
```
-------------------------------------------------------------------------------

To generate synatax and diagram from EBNF, you can use:

+ Railroad Diagram Generator http://bottlecaps.de/rr/ui

If you want to generate EBNF from Scala, you can use:

+ https://github.com/bwmcadams/SyntaxDiagramGenerator.git

-------------------------------------------------------------------------------

After that you have standalone executable jar `./target/sdg-1.0-jar-with-dependencies.jar`

You can use it: `java -jar ./target/sdg-1.0-jar-with-dependencies.jar src/main/scala/ebnf/EBNFParser.scala`

Or exec `mvn scala:run -DaddArgs=<PARSER_SOURCE_FILE>`

For example: `mvn scala:run -DaddArgs=src/main/scala/ebnf/EBNFParser.scala`

I have had issues to get it to run manually via SBT but on CLI can do it with setting CLASSPATH to:

project/boot/scala-2.8.0/lib/scala-library.jar:project/boot/scala-2.8.0/lib/scala-compiler.jar:lib/batik-anim.jar:lib/batik-awt-util.jar:lib/batik-bridge.jar:lib/batik-codec.jar:lib/batik-css.jar:lib/batik-dom.jar:lib/batik-ext.jar:lib/batik-extension.jar:lib/batik-gui-util.jar:lib/batik-gvt.jar:lib/batik-parser.jar:lib/batik-script.jar:lib/batik-svg-dom.jar:lib/batik-svggen.jar:lib/batik-swing.jar:lib/batik-transcoder.jar:lib/batik-util.jar:lib/batik-xml.jar:lib/js.jar:lib/pdf-transcoder.jar:lib/xalan-2.6.0.jar:lib/xerces_2_5_0.jar:lib/xml-apis-ext.jar:lib/xml-apis.jar:target/scala_2.8.0/syntaxdiagramgenerator_2.8.0-1.0.jar

and running:

java -Djava.awt.headless=true net.t32leaves.syntaxDiagramGenerator.Main <INPUT_PARSER_COMBINATOR>

You can test it on the sample included via:

java -Djava.awt.headless=true net.t32leaves.syntaxDiagramGenerator.Main src/main/scala/ebnf/EBNFParser.scala

Creates a whole bunch of files.  Have fun!
-------------------------------------------------------------------------------
+ spark-sql (org.apache.spark.sql.catalyst)
+ scala-mahout-binding
+ https://github.com/stephentu/scala-sql-parser.git
+ https://github.com/mpollmeier/gremlin-scala
+ https://github.com/squeryl/squeryl
+ https://github.com/json4s/json4s.git
+ https://github.com/djspiewak/gll-combinators.git
-------------------------------------------------------------------------------
