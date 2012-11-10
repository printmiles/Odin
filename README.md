Odin
====

Summary
-------
A data curation and archival tool created as part of my MSc dissertation. Users can use a client (Sleipnir) that can be downloaded from the server (Odin) to upload documents to the server's repository.

Documents are scanned by the client and the detected metadata is shown to the user for them to make any amendments (adding author details or extra keywords to aid later retrieval). The metadata and document are sent to a web-service on the server where they are stored for later retrieval.

Currently the local file system is used for storage of the original documents (as individual entities) and the database of the repository metadata (as a XML database). Future plans are to provide support for Apache Hadoop's HDFS for filing and alternative database back-ends (MySQL and Apache Cassandra).

Interesting Code
----------------
See Apache Tika in action in src/odin/sleipnir/MetadataWorker.java - lines 68 to 110.

Planned Features
----------------
- Improve the GUI
- Provide database reporting for SysAdmins
- Provide communication (SOAP) between servers
- Provide a means for rule creation on a server
- Allow rules to be negotiated between servers and propogated accordingly

Required Libraries
------------------
- Apache Tika
- Apache Commons Compress
- Saxon HE 
- XQuery for Java (XQJ)

More Information
----------------
For more information about this project (as part of my MSc dissertation) please visit: https://sites.google.com/site/printmiles/home/projects/postgraduate-dissertation