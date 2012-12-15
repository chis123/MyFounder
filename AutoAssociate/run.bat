set JAVA_HOME=D:\bea\jdk142_11
set CLASSPATH=.;./lib;./lib/commons-logging-1.1.jar;./lib/log4j-1.2.13.jar;./lib/ojdbc14.jar;./lib/commons-dbutils-1.1.jar;./lib/cms4-tools-associate.jar

%JAVA_HOME%\bin\java com.founder.enp.aa.Main jdbc:oracle:thin:@128.0.168.15:1521:cms40 cmsuser xyz %1