# exporter
Tool for exporting documentation for unix commands

#### Compile instructions
In order to build this application maven is required. After cloning the repository, execute:``mvn package``

After execution a target folder will appear with an executable jar.

#### Execution instructions
Export documentation for commands: ``java -jar exporter-{version}-jar-with-dependencies.jar -c command1 command2 command3 ...``

Export documentation for commands located in file ``java -jar exporter-{version}-jar-with-dependencies.jar -f filename``

*The file must conatin valid commands separated by new line character

Display help menu: ``java -jar exporter-{version}-jar-with-dependencies.jar -h``


After execution a folder named results, will contain the extracted files. 