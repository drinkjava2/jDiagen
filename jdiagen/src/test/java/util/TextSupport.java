/** 
 * Copyright (C) 2016 Yong Zhu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * TextSupport is base class for Java text(multiple line Strings) template. Can
 * see the demo. if getJavaSrcFolder return not empty, read from java source
 * code file, otherwise read from class root folder (for Maven is resource root
 * folder).
 * 
 * @author Yong Zhu (Yong9981@gmail.com)
 * @since 1.0.0
 */
public class TextSupport {

	public String getJavaSrcFolder() {
		return "F:/gproj/jdiagen/jDiagen/jdiagen/src/test/java/";
	}

	/*
	 * Get the multiple line String from java source code or resource
	 */
	public String toString() {
		try {
			if (StringUtils.isEmpty(getJavaSrcFolder()))
				return extractTextFromComments(getTextFromResourceFile());
			else
				return extractTextFromComments(getTextFromJavaFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String extractTextFromComments(String templateText) {
		String thisPublicStaticClassName = this.getClass().getSimpleName();
		String classText = StringUtils.substringBetween(templateText,
				"public static class " + thisPublicStaticClassName, "*/}");
		if (StringUtils.isEmpty(classText))
			throw new RuntimeException("Can not find text between \"public static class " + thisPublicStaticClassName
					+ " and end tag \"*/}\"");
		StringBuilder sb = new StringBuilder();
		for (String str : StringUtils.substringsBetween(classText + "*/", "/*", "*/"))
			sb.append(str);
		return sb.toString();
	}

	private String getTextFromJavaFile() throws IOException {
		String className = this.getClass().getName(); // aaa.bbb.CCC$DDD
		String fileName = getJavaSrcFolder()
				+ StringUtils.replace(StringUtils.substringBeforeLast(className, "$"), ".", "/") + ".java";
		return FileUtils.readFileToString(new File(fileName));
	}

	private String getTextFromResourceFile() throws IOException {
		String className = StringUtils.substringAfterLast("." + this.getClass().getName(), ".");// CCC$DDD
		String resFile = StringUtils.substringBefore(className, "$") + ".txt";// CCC.txt
		URL url = this.getClass().getResource("/" + resFile);
		if (url == null)
			throw new RuntimeException("Can not find resource file \"" + resFile + "\"");
		return FileUtils.readFileToString(new File(url.getFile()));
	}

	public static void main(String[] args) {
		System.out.println("Text=" + new DemoString());
	}

	//@formatter:off
	public static class DemoString extends TextSupport{   
	 /*   
	  Hello,
	  This is multiple line strings demo
     */} 	
}
