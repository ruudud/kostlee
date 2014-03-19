# kostlee
Provides data collected from Norsk Tipping - Grasrotandelen via a REST API for
[KrissLee festivalen](http://krisslee.no/)


## Installation
To run locally, you need JDK and [leiningen](http://leiningen.org/#install) on
your `$PATH`.


## Developing
`lein ring server [ PORT ]` starts a web server that reloads whenever files
change.


## Lein Build
Running `lein ring uberjar` will create a JAR file with the app running
using an embedded Jetty.

The resulting JAR can be run fairly straight forward:

    $ java -jar target/kostlee-0.1.0-standalone.jar [args]


## Docker Build
A Docker build is also provided. Run `make install`, this will also install
Docker if needed. After it's done, do
`docker run -d -e PORT=<port> -p <hostPort>:<port> ruudud/kostlee`.
The app can be re-built using `make app`.


## License
Copyright © 2014 Pål Ruud

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished to do
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

