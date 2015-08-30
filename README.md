# A modified version of an address book application using ORMLite andropenguin/AddressBookORMlite7

# What is this?

This is a modified version of andropenguin/AddressBookORMlite7 
( https://github.com/andropenguin/AddressBookORMlite7 ). 
It uses Google Dagger 2 and Espresso for testing of Adapter, Database, AsyncTaskLoader, and UI.
To test them, the dependency injection of database file name is done. Tests uses other database file
 than the one of production environment.

# License
Copyright 2015 andropenguin@gmail.com (twitter id: @penguindaa )

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

# Used codes

This sample uses some codes of projects and blog as follows.

* http://tomoyamkung.net/2014/02/24/android-loader-execute/
* http://baroqueworksdevjp.blogspot.jp/2015/03/espressotoast.html

# References

* http://engineering.circle.com/instrumentation-testing-with-dagger-mockito-and-espresso/
* https://blog.gouline.net/2015/05/04/dagger-2-even-sharper-less-square/
* http://code.tutsplus.com/tutorials/dependency-injection-with-dagger-2-on-android--cms-23345
* https://github.com/gk5885/dagger-android-sample
* http://blog.sqisland.com/2015/04/dagger-2-espresso-2-mockito.html
