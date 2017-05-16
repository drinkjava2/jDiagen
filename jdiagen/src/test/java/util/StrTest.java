package util;

import util.TextSupport;

/**
 * TextSupport is base class for Java text(multiple line Strings) template. Can
 * see the demo. if getJavaSrcFolder return not empty, read from java source
 * code file, otherwise read from class root folder (for Maven is resource root
 * folder).
 * 
 * @author Yong Zhu (Yong9981@gmail.com)
 * @since 1.0.0
 */

public class StrTest {

	public static void main(String[] args) {
		System.out.println(new S2());
	}

	public static class S1 extends TextSupport {
		public String getJavaSrcFolder() {
			return "F:/gproj/jdiagen/jDiagen/jdiagen/src/test/java/";
		}
	}

	//@formatter:off
	public static class S2 extends S1 {
		/*
		 hi
		 hi
		 */}

}
