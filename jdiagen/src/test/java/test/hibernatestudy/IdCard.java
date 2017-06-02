/*
 * jDialects, a tiny SQL dialect tool 
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.hibernatestudy;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

//身份证实体类
@Entity
public class IdCard {
    private IdCardPK pk;// 身份证的主键是联合主键 IdCardPK pk

    private String province;// 省份

    private Students stu; // 身份证持有学生类的引用

//    把控制权交给学生
    @OneToOne(mappedBy="cardId")//只要是双向关联，就一定要指定mappedBy
    public Students getStu() {
        return stu;
    }

    public void setStu(Students stu) {
        this.stu = stu;
    }

//    联合主键的主键形式是Xxxxid类型，具体的主键设置在联合主键类里进行
    @EmbeddedId
    public IdCardPK getPk() {
        return pk;
    }

    public void setPk(IdCardPK pk) {
        this.pk = pk;
    }
    
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
 