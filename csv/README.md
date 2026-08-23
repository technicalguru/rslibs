# CSV - CSV/Excel Utility Package

## Synopsis
Unfortunately, Java does not offer any methods to simply read CSV files or produce such, not mentioning Excel. 
I for myself found it quite easy to work with such files; especially when you need to deal with data from and 
to Microsoft Excel.

My CSV/Excel Utility Package, published under the [GNU Lesser General Public License](LICENSE.md), allows you to easily 
integrate CSV and Excel functionality into your application, just by using Iterator-like classes for reading, 
and PrintStream-like classes for writing. The CSV tools can be configured to use different column delimiter 
and separator characters in case you need to adopt some other versions of CSV. The default configuration 
conforms to the Excel style of CSV.

The Excel tools conform to the same way that CSV tools behave (see below). Therefore, two new interfaces 
[TableReader](https://www.javadoc.io/doc/eu.ralph-schuster/csv/latest//csv/TableReader.html) 
and [TableWriter](https://www.javadoc.io/doc/eu.ralph-schuster/csv/latest//csv/TableWriter.html)
were introduced to reflect the common functions. The new 
[ExcelWriter](https://www.javadoc.io/doc/eu.ralph-schuster/csv/latest//csv/impl/ExcelWriter.html)
allows you to easily create Excel files while still having the flexibility of formatting issues (see 
[ExcelFormatter](https://www.javadoc.io/doc/eu.ralph-schuster/csv/latest//csv/impl/ExcelFormatter.html)
interface). The implementation is based on [Apache’s POI library](http://poi.apache.org/).

Since this CSV/Excel package uses streams, you are able to read from any stream. And, of course, you can 
write to any stream. You could even synchronize within your application by applying the 
[reader/writer synchronization](https://techblog.ralph-schuster.eu/2008/08/09/synchronizing-reader-and-writer-threads/)
described in one of my articles.

Please notice that some methods are deprecated since V2.0 and CSVReader and CSVWriter classes are moved into 
other packages in favour of readability and structuring of the classes.

Excel functionality is available since version 2.0.

## Maven Coordinates

```
<dependency>
	<groupId>eu.ralph-schuster</groupId>
	<artifactId>csv</artifactId>
	<version>${version}</version>
</dependency>
```

## Code Examples

 * [Reading a CSV file](https://www.javadoc.io/doc/eu.ralph-schuster/csv/latest/csv/impl/CSVReader.html)
 * [Reading an Excel file](https://www.javadoc.io/doc/eu.ralph-schuster/csv/latest/csv/impl/ExcelReader.html)
 * [Writing a CSV file](https://www.javadoc.io/doc/eu.ralph-schuster/csv/latest/csv/impl/CSVWriter.html)
 * [Writing an Excel file](https://www.javadoc.io/doc/eu.ralph-schuster/csv/latest/csv/impl/ExcelWriter.html)
 * [Using StreamMapper to convert between table stream data and Java objects](https://www.javadoc.io/doc/eu.ralph-schuster/csv/latest/csv/mapper/StreamMapper.html)
 * [Reading objects from a table stream](https://www.javadoc.io/doc/eu.ralph-schuster/csv/latest/csv/util/ObjectReader.html)
 * [Writing objects to a table stream](https://www.javadoc.io/doc/eu.ralph-schuster/csv/latest/csv/util/ObjectWriter.html)

## Version Notes

 * Java 21 required since v4.3
 
## API Reference

Javadoc API for latest stable version can be accessed [here](https://www.javadoc.io/doc/eu.ralph-schuster/csv).

## Contributions

 * [Project Homepage](https://github.com/technicalguru/rslibs/tree/master/csv)
 * [Issue Tracker](https://github.com/technicalguru/rslibs/issues)
 
## License

CSV is free software: you can redistribute it and/or modify it under the terms of version 3 of the GNU 
Lesser General Public  License as published by the Free Software Foundation.

CSV is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public 
License for more details.

You should have received a copy of the GNU Lesser General Public License along with CSV.  If not, see 
<http://www.gnu.org/licenses/lgpl-3.0.html>.

Summary:
 1. You are free to use all this code in any private or commercial project. 
 2. You must distribute license and author information along with your project.
 3. You are not required to publish your own source code.
