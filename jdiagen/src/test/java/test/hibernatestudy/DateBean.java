package test.hibernatestudy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.github.drinkjava2.jdialects.annotation.jdia.UUID25;
import com.github.drinkjava2.jsqlbox.ActiveEntity;

@Entity
public class DateBean implements ActiveEntity<DateBean> {
	@Id
	@UUID25
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "timestamp")
	private java.util.Date d1;

	private java.sql.Date d2;

	private java.sql.Time d3;

	private java.sql.Timestamp d4;
	
	//@CreationTimestamp
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date d5;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public java.util.Date getD1() {
		return d1;
	}

	public void setD1(java.util.Date d1) {
		this.d1 = d1;
	}

	public java.sql.Date getD2() {
		return d2;
	}

	public void setD2(java.sql.Date d2) {
		this.d2 = d2;
	}

	public java.sql.Time getD3() {
		return d3;
	}

	public void setD3(java.sql.Time d3) {
		this.d3 = d3;
	}

	public java.sql.Timestamp getD4() {
		return d4;
	}

	public void setD4(java.sql.Timestamp d4) {
		this.d4 = d4;
	}

	public java.util.Date getD5() {
		return d5;
	}

	public void setD5(java.util.Date d5) {
		this.d5 = d5;
	}

}