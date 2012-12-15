set JAVA_HOME=D:\bea\jdk142_11
set LIB_HOME=./lib
set CLASSES=./bin

set LOCALCLASSPATH=%LIB_HOME%/ojdbc14.jar

%JAVA_HOME%\bin\java -cp %LOCALCLASSPATH%;%CLASSES% NodePic %1