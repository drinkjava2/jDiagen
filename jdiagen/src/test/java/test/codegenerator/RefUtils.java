/*
 * jDialects, a tiny SQL dialect tool 
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.codegenerator;

import java.lang.reflect.Field;

import com.github.drinkjava2.jbeanbox.ReflectionUtils;

/**
 * Reflecct utils
 *
 * @author Yong Zhu
 * @since 1.0.0
 */
public class RefUtils {
	public static Object findFieldObject(Object obj, String fieldname) {
		try {
			Field field = ReflectionUtils.findField(obj.getClass(), fieldname);
			field.setAccessible(true);
			Object o = field.get(obj);
			return o;
		} catch (Exception e) {
			return null;
		}
	}
}