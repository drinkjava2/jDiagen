## jdiagen
licnese: [LGPL 2.1](http://www.gnu.org/licenses/lgpl-2.1.html)  
jDiagen is a source code generator tool to create source code for jDialects, it requires JDK8+.  
This tool is not a automatic tool, need manually run each unit test one by one, and copy output in console or output files(at e:\ folder) into jDialects's corresponding java source code.  

Some note:  
* jUnit in jDiagen can not pass because there is a Hibernate study unit test used exit(0), this unit test only used to study hibernate but I want keep it here.
* Console buffer size change to 500000