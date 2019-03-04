# FileDownloader
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;使用JavaFX作为客户端的一个多线程下载工具。思路是从输入URL中创建一个BufferedInputStream，有一个缓冲数组，每次最多可以读取这个数组这么多字节，大小通过一个配置文件可以修改。然后根据下载后保存目录创建一个RandomAccessFile，开启多个线程往文件中写入。当把这个缓冲数组的当前有效内容读完后，再从输入流中读取后面的字节开启新的线程写入。直到所有字节读取完毕发出结束通知。<br/>
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;其中对于线程数、缓冲数组的大小、默认下载路径、最大线程数、最小线程数、最大缓冲数组字节数、最小缓冲数组字节数可以通过settings.property进行修改，对于下载记录每次会从tasks.xml读取。并做了一些错误检查及提醒。<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;不足之处：没有添加断点续传功能、界面不够美观。
