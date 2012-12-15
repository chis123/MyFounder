set JAVA_HOME=D:\jdk1.5
set LIB_HOME=./lib
set CLASSPATH=.;./dest/classes;%LIB_HOME%/commons-collections-3.2.jar;%LIB_HOME%/commons-configuration-1.4.jar;%LIB_HOME%/commons-io-1.3.2.jar;%LIB_HOME%/;%LIB_HOME%/commons-lang-2.3.jar;%LIB_HOME%/commons-logging-1.1.jar;%LIB_HOME%/log4j-1.2.14.jar

%JAVA_HOME%\bin\java -cp %CLASSPATH% com.founder.enp.io.FileSaver