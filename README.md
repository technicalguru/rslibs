# RS Libs
A Java class library that is useful for day-2-day development

## Synopsis
The RS Library project was established to support my daily Java development. It is a collection of interfaces, classes and static Utils that are part of each of my projects. That’s why I decided to publish them the same way so everyone can benefit of the work. And of course, it will be easier for myself to fulfill common development tasks.

RS Library is currently divided into five areas, each of them published as a Maven module to RS Library:

 * [Base Library](https://github.com/technicalguru/rslibs/baselib) – provides non-specific code such as Java language tasks, IO tasks, bean handling, Configuration helpers and many more.
 * [Data Base Library](https://github.com/technicalguru/rslibs/data) – provides a scheme for Data Factory – Data Access Object – Business Object pattern. It includes parameterized interfaces and first abstract implementations of them.
 * [Data File Library](https://github.com/technicalguru/rslibs/data-file) – provides abstract implementations for usage with file-based storage of data (experimental).
 * [Data Hibernate Library](https://github.com/technicalguru/rslibs/data-hibernate) – provides abstract implementations of Data Base Library scheme for usage with Hibernate.
 * [Templating](https://github.com/technicalguru/rslibs/templating) – provides an easy Typo3-like way of processing text templates within Java.

Most of this code was created when I faced specific problems in my professional work. I usually work out solutions in private projects, test them there and then apply them to my professional work. That’s why few of my employers will find the same code in their products. However, the code was created outside of professional environments and therefore is my own intellectual property.

RS Library modules are maintained in the same Maven project and, hence, follow the same release cycle. That means that each modules will have the same version numbers and are published at the same time.

## Latest Version
Latest version is 2.0.0.

## Upgrading v1 to v2
V2 which has some minor compatibility breaks that result from upgrading [commons-configuration](https://commons.apache.org/proper/commons-configuration/) dependency:

* Replace references to ``org.apache.commons.configuration.Configuration`` by ``org.apache.commons.configuration2.Configuration``
* ``SubnodeConfiguration`` was removed in favour of ``HierarchicalConfiguration`` 
* New utility methods for the ease of creating configurations from properties and XML files were added in [ConfigurationUtils](baselib/src/main/java/rs/baselib/configuration/ConfigurationUtils.java):
    *  getXmlConfiguration() - will create a ``XMLConfiguration`` object from a given file or URL
    *  getPropertiesConfiguration() - will create a ``PropertiesConfiguration`` object from a given file or URL

## API Reference

Javadoc API for latest stable version can be accessed here:

* [baselib](https://www.javadoc.io/doc/eu.ralph-schuster/baselib)
* [data](https://www.javadoc.io/doc/eu.ralph-schuster/date)
* [data-file](https://www.javadoc.io/doc/eu.ralph-schuster/data-file)
* [data-hibernate](https://www.javadoc.io/doc/eu.ralph-schuster/data-hibernate)
* [templating](https://www.javadoc.io/doc/eu.ralph-schuster/templating)

## Contributions

 * [Project Homepage](https://github.com/technicalguru/rslibs)
 * [Issue Tracker](https://github.com/technicalguru/rslibs/issues)
 
## License

RsLibs is free software: you can redistribute it and/or modify it under the terms of version 3 of the GNU 
Lesser General Public  License as published by the Free Software Foundation.

RsLibs is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public 
License for more details.

You should have received a copy of the GNU Lesser General Public License along with RsLibs.  If not, see 
<http://www.gnu.org/licenses/lgpl-3.0.html>.

Summary:
 1. You are free to use all this code in any private or commercial project. 
 2. You must distribute license and author information along with your project.
 3. You are not required to publish your own source code.
