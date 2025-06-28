<!---
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->
# Record Validation


# Build

## Minimum requirements
* JDK 17+
* Apache Maven 3.8+

##  Current

* JDK 24+
* Apache Maven 3.9.+

# Code Coverage
Code coverage via:
```bash
mvn clean verify org.jacoco:jacoco-maven-plugin:report
```
Create Mutation coverage via:
```bash
mvn clean verify org.pitest:pitest-maven:mutationCoverage
```
Create an SonarQube analysis including the mutation coverage. The prerequisite is that
the mutation plugin in SonarQube is installed.

[license]: https://www.apache.org/licenses/LICENSE-2.0
