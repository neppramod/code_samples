package ...domain.reporting;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import ...domain.user.User;

/**
 * 
 * @author pramod Domain class to capture values of hourly power usage reading
 */
@Entity
@NamedQueries(value = {
		@NamedQuery(name = "findAllHourlyReports", query = "SELECT o FROM HourlyReport o WHERE o.user.id = :uid"),
		@NamedQuery(name = "findHourlyReportByStartTime", query = "SELECT DISTINCT o FROM HourlyReport o where o.startTime = :startTime AND o.user.id = :uid"),
		@NamedQuery(name = "findHourlyReportsBetweenTwoStartTimes", query = "SELECT o FROM HourlyReport o where o.startTime >= :startTime1 AND "
				+ "o.startTime <= :startTime2 AND " + "o.user.id = :uid"),
		@NamedQuery(name = "findLastHourlyReport", query = "SELECT o FROM HourlyReport o WHERE o.user.id = :uid ORDER BY id DESC LIMIT 1"),
		@NamedQuery(name = "findFirstHourlyReport", query = "SELECT o FROM HourlyReport o WHERE o.user.id = :uid ORDER BY id ASC LIMIT 1") })
public class HourlyReport {

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Unique unique time stamp, when the capture started
	 */
	private long startTime;

	/**
	 * In most cases for hourly it is 3600 60 sec * 60 min = 3600 sec
	 */
	private int duration;

	/**
	 * Energy consumed in this duration, starting from startTime
	 */
	private int consumption;

	/**
	 * User associated with this hourly report
	 * 
	 * @return
	 */
	@OneToOne
	@JoinColumn(name = "user_fk")
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getConsumption() {
		return consumption;
	}

	public void setConsumption(int consumption) {
		this.consumption = consumption;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
