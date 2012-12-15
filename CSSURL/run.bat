set JAVA_HOME=D:\bea\jdk142_11
set LIB_HOME=lib
set CLASSES=dest/classes

set LOCALCLASSPATH=%LIB_HOME%/commons-codec-1.3.jar;%LIB_HOME%/commons-httpclient-3.1-alpha1.jar;%LIB_HOME%/commons-io-1.3.1.jar;%LIB_HOME%/commons-lang-2.3.jar;%LIB_HOME%/commons-logging-1.1.jar;%LIB_HOME%/log4j-1.2.13.jar

%JAVA_HOME%\bin\java -classpath %LOCALCLASSPATH%;%CLASSES% Cssurl C:\Founder\Templet\939.files\fclistcss.css