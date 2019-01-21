# RSLibs - Templating Library
Java Library for Typo3-alike template replacements

## Synopsis
This Templating Package provides the Typo3-like template mechanism for Java. This package 
provides a single class Templating. Your templates should contain markers enclosed by three 
hash signs, e.g. ###MY_MARKER###. These markers will be replaces accordingly by values you 
provide at runtime. Starting with version 1.2.0, Templating Java Package is part of the RS Library.

The second feature of the package are sub-templates. You can define such sub-template by enclosing it in XML-style comments, e.g.

```
<!-- ###MY SUBSECTION### begin -->
any text
<!-- ###MY SUBSECTION### end -->
```

The Templating class provides a method to extract this sub-template.

## API Reference

Javadoc API for latest stable version can be accessed [here](https://download.ralph-schuster.eu/eu.ralph-schuster.libs/STABLE/templating/apidocs/index.html).

## Contributions

 * [Project Homepage](https://techblog.ralph-schuster.eu/rs-library/templating/)
 * [Issue Tracker](http://jira.ralph-schuster.eu/)
 
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


         