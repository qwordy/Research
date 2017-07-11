# How do Programmers Maintain Concurrent Code

This project studies how programmers maintain concurrent code.

Concurrent programming is pervasive in nowadays software development. Many programmers believe that concurrent programming is difficult, and maintaining concurrency code is error-prone. Although researchers have conducted empirical studies to understand concurrent programming, they still rarely study how programmers maintain concurrent code. To the best of our knowledge, only a recent study explored the modifications on critical sections, and many related questions are still open. In this paper, we conduct an empirical study to explore how programmers maintain concurrent code. We analyze more concurrency-related commits and explore more issues such as the change patterns of maintaining concurrent code than the previous study. We summarize five change patterns according to our analysis on 696 concurrency-related commits. We apply our change patterns to three open source projects, and synthesize three pull requests. Until now, two of them have been accepted. Furthermore, we analyze other issues such as the usages of parallel API classes and the correlations between total commits and concurrency-related commits. Our results show that some of such usages follow specific trends and there is a strong correlation between total commits and concurrency-related commits. Such findings can be useful for programmers to maintain concurrent code and for researchers to implement treating techniques.

Full list of concurrency-related keywords can be found in [ConcurrentKeywords.java](crr/src/main/java/com/yfy/crr/ConcurrentKeywords.java).

Studied data range:

hadoop 2016.11.11 - 2010.4.1
flink 2016.11.4 - 2011.3.23
tomcat 2016.10.31 - 2006.4.23
cassandra 2016.6.10 - 2009.4.2
lucune 2016.7.26 - 2003.8.12
netty 2016.10.21 - 2008.8.19

