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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

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
	@Override
	public String toString() {
		String templateText;
		if (StringUtils.isEmpty(getJavaSrcFolder()))
			templateText = getTextFromResourceFile();
		else
			templateText = getTextFromJavaFile();
		return extractSqlInComments(templateText);
	}

	public String extractSqlInComments(String templateText) {
		String thisPublicStaticClassName = this.getClass().getSimpleName();
		String classText = StringUtils.substringBetween(templateText,
				"public static class " + thisPublicStaticClassName, "public static class");
		if (StringUtils.isEmpty(classText))
			throw new RuntimeException(
					"Can not find text template class started with: public static class " + thisPublicStaticClassName);
		String[] comments = StringUtils.substringsBetween(classText, "/*", "*/");
		StringBuilder sb = new StringBuilder();
		for (String str : comments)
			sb.append(str);
		return sb.toString();
	}

	public String getTextFromJavaFile() {
		String className = this.getClass().getName(); // aaa.bbb.TextSupport$DemoString
		String classPath = StringUtils.substringBeforeLast(className, ".");
		classPath = StringUtils.replace(classPath, ".", "/");// aaa/bbb
		className = StringUtils.substringAfterLast(className, ".");// TextSupport$DemoString
		String templateFile = StringUtils.substringBefore(className, "$");// TextSupport
		String javaFileName = getJavaSrcFolder() + classPath + "/" + templateFile + ".java";
		try {
			return FileUtils.readFileToString(new File(javaFileName));
		} catch (Exception e) {
			throw new RuntimeException("Can not read java file:" + javaFileName + "\r\n" + e.getMessage());
		}
	}

	public String getTextFromResourceFile() {
		String className = this.getClass().getName(); // xxx.xxxxx.TextSupport$DemoString
		String classPath = StringUtils.substringBefore(className, ".");// xxx.xxxxx.
		classPath = StringUtils.replace(classPath, ".", "/");// xxx/xxxxx
		className = StringUtils.substringAfterLast(className, ".");// TextSupport$DemoString
		String templateFile = StringUtils.substringBefore(className, "$");// TextSupport

		String srcFileName = templateFile + ".txt";
		String str;
		InputStream input = null;
		try {// NOSONAR
			input = ClassLoader.getSystemResourceAsStream(srcFileName);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] data = new byte[4096];
			int count = -1;// NOSONAR
			while ((count = input.read(data, 0, 4096)) != -1)
				outStream.write(data, 0, count);
			data = null;// NOSONAR
			str = new String(outStream.toByteArray(), "ISO-8859-1");
		} catch (Exception e) {
			throw new RuntimeException("Can not read resource file:" + srcFileName + "\r\n" + e.getMessage());
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (Exception e) {
				throw new RuntimeException("Can not close input stream for file: " + srcFileName);
			}
		}
		return str;
	}

	public static void main(String[] args) {
		System.out.println("Text=" + new DemoString() + "=======");
	}

	//@formatter:off
	public static class DemoString extends TextSupport{   
		/*
		 Hello,
		 this is multiple String demo
		 */
	} 

	//public static class EndTag
}
