/*
 * This file is part of CSV/Excel Utility Package.
 *
 *  CSV/Excel Utility Package is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  CSV/Excel Utility Package is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with CSV/Excel Utility Package.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package csv.performance;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.io.FileFinder;

/**
 * Performs all reading tests and creats KPI figures.
 * @author ralph
 *
 */
public class ReaderTest {

	public static final String READER_TEST_FILE = "worldcitiespop.txt";
	public static final String READER_TEST_FILE_CHARSET = "ISO-8859-1";
	
	private static Logger log = LoggerFactory.getLogger(ReaderTest.class);
	private static NumberFormat FORMATTER = new DecimalFormat("#0.00");
	private static String testClasses[] = {
		CsvExcelUtilityReader.class.getName(),
		CommonsCsvReader.class.getName(),
		GenJavaCsvReader.class.getName(),
		JavaCsvReader.class.getName(),
		OpenCsvReader.class.getName(),
		SkifeCsvReader.class.getName(),
		SuperCsvReader.class.getName()
	};
	private static Map<String, Long> testResults;
	private static File file;
	
	/**
	 * Setup tests
	 * 
	 * @throws Exception - when the test file cannot be opened/read
	 */
	@BeforeAll
	public static void setup() throws Exception {
		testResults = new HashMap<String, Long>();
		URL url = FileFinder.find(ReaderTest.class, READER_TEST_FILE);
		if (url != null) {
			file = new File(url.toURI());
		} else {
			log.info("Performance test will be skipped. Test CSV file not available ("+READER_TEST_FILE+")");
			file = null;
		}
	}

	/**
	 * Test the reader and save execution time.
	 */
	@ParameterizedTest
	@MethodSource("provideReaders")
	public void testPerformance(IReader reader) {
		if (file != null) {
			long startTime = 0;
			long endTime = 0;
			int rows = 0;
			try {
				log.info("Measuring performance of "+reader.getName()+"...");
				startTime = System.currentTimeMillis();
				rows = reader.read(file, READER_TEST_FILE_CHARSET);
				endTime = System.currentTimeMillis();
			} catch (Exception e) {
				log.error("Cannot perform test on "+reader.getName()+":", e);
			}
			if (endTime != 0) {
				testResults.put(reader.getName(), (endTime-startTime));
				log.info("Performance of "+reader.getName()+": "+rows+" rows read in "+(endTime-startTime)+"ms");
			} else {
				testResults.put(reader.getName(), -1L);
			}
		}
			
	}

	/**
	 * Instantiate test classes
	 * @return the test classes for tests
	 * 
	 * @throws Exception - when the test file cannot be opened/read
	 */
	@SuppressWarnings("unchecked")
	private static Stream<Arguments> provideReaders() throws Exception {
		Collection<Arguments> data = new ArrayList<>();
		for (String className : testClasses) {
			Class<? extends IReader> clazz = (Class<? extends IReader>) Class.forName(className);
			data.add(Arguments.of(clazz.getConstructor().newInstance()));
		}
		return data.stream();
	}

	/**
	 * Cleanup and report tests
	 */
	@AfterAll
	public static void cleanup() {
		if (file != null) {
			// compute medium and max run time
			long totalTime = 0;
			long avgTime = 0;
			long maxTime = 0;
			long mediumTime = 0;
			int count = 0;
			int labelSize = 0;
			for (Map.Entry<String, Long> entry : testResults.entrySet()) {
				long time = entry.getValue();
				if (time > 0) {
					totalTime += time;
					if (time > maxTime) maxTime = time;
					count++;
				}
				if (entry.getKey().length() > labelSize) labelSize = entry.getKey().length();
			}
			if (count > 0) {
				mediumTime = maxTime / 2;
				avgTime = totalTime / count;
			}
			
			// Calculate the KPI steps
			// Medium is 5.0 by default, maximum time is 1.0
			long kpiStep = mediumTime / 5;
			
			// Calculate KPI and print it for each result
			log.info("===============================================================");
			log.info("KPI values computed: 1.0 - worst, 5.0 average, 10.0 - excellent");
			printKPI("average", avgTime, maxTime, kpiStep, labelSize);
			log.info("---------------------------------------------------------------");
			//log.info("KPI "+trimLabel("(average)", labelSize+2)+ " ="+FORMATTER.format((double)(maxTime-avgTime)/(double)kpiStep+(double)1)+" "+avgTime+"ms");
			List<String> sortedKeys = new ArrayList<>(testResults.keySet());
			sortedKeys.sort(new PerformanceSorter());
			for (String key : sortedKeys) {
				long time = testResults.get(key);
				printKPI(key, time, maxTime, kpiStep, labelSize);
			}
			log.info("===============================================================");
		}
	}

	private static void printKPI(String label, long time, long maxTime, long kpiStep, int maxLabelSize) {
		Double kpi = getKPI(time, maxTime, kpiStep);
		if (kpi != null) {
			log.info("KPI "+trimLabel("("+label+")", maxLabelSize+2)+" = "+FORMATTER.format(kpi)+" ("+time+"ms)");
		} else {
			log.info("KPI "+trimLabel("("+label+")", maxLabelSize+2)+" = DISQUALIFIED (Parser failed test)");
		}
	}
	
	private static Double getKPI(long time, long maxTime, long kpiStep) {
		if (time > 0) {
			double kpi = 10;
			if (kpiStep != 0) {
				kpi = (double)(maxTime-time)/(double)kpiStep+(double)1;
			} else {
				kpi = 10;
			}
			return Double.valueOf(kpi);
		}
		return null;
	}
	private static String trimLabel(String s, int size) {
		while (s.length() < size) s += " ";
		return s;
	}
	
	private static class PerformanceSorter implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			long l1 = testResults.get(o1);
			long l2 = testResults.get(o2);
			if (l1 < l2) return -1;
			if (l1 > l2) return 1;
			return 0;
		}
		 
	}
}
