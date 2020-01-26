/*
 * jDialects, a tiny SQL dialect tool 
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.config.po;

import com.github.drinkjava2.jsqlbox.ActiveRecord;

/**
 * Entity class is not a POJO, need extends from EntityBase or implements
 * EntityInterface interface<br/>
 * 
 * @author Yong Zhu
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class Customer extends ActiveRecord<Customer> {
	public static final String CREATE_SQL = "create table customertable("//
			+ "id varchar(36),"//
			+ "customer_name varchar(50),"//
			+ "constraint customer_pk primary key (id)" //
			+ ")";//
	private String id;
	private String customerName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}