/*
 * jDialects, a tiny SQL dialect tool 
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.hibernatestudy;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;

//学生实体类
@Entity
public class Students {
	private int sid; // 编号

	private String sname; // 姓名

	private IdCard cardId; // 学生的外键，学生控制身份证

	// 主控类的外键设置比较复杂点，因为有多个主键字段了
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumns({
			// referencedColumnName设置对应数据库的字段名字，name是类的字段名字
			@JoinColumn(name = "pid", referencedColumnName = "pid"),
			@JoinColumn(name = "bloodType", referencedColumnName = "bloodtype") })
	public IdCard getCardId() {
		return cardId;
	}

	public void setCardId(IdCard cardId) {
		this.cardId = cardId;
	}

	@Id
	@GeneratedValue
	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}
}