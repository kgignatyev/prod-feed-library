build status
---
[![Build status](https://ci.appveyor.com/api/projects/status/peydlotvtaysqga3?svg=true)](https://ci.appveyor.com/project/kgignatyev/prod-feed-library)


latest [snapshot](https://ci.appveyor.com/api/buildjobs/j68rxc716mltesqd/artifacts/target%2Fprod-feed-service-1.0-SNAPSHOT.jar)

use
---

from command line:

        mvn clean package exec:java
        

![](docs/exec-sample.png)

from  tests        

![](docs/use-sample.png)



build prerequisites
---

 1. Maven
 2. Java 8+


build
---

        mvn clean package dokka:dokka
        

documentation is generated in [target/dokka](target/dokka) folder



test and see test results
---

        mvn test
        open target/cucumber/index.html


sample output

![](docs/cucumber-result-sample.png)
