package ...domain.target;

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
 * @author pramod Domain class to record success or failure of current target
 */
@Entity
@NamedQueries(value = { 
		@NamedQuery(name = "findAllCurrentTargets", 
				query = "SELECT o FROM CurrentTarget o WHERE o.user.id = :uid"),
		@NamedQuery(name = "findCurrentTargetByTargetType",
				query = "SELECT DISTINCT o FROM CurrentTarget o where o.targetType = :targetType AND o.user.id = :uid")
})
public class CurrentTarget {
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * The target type this target is associated with
	 */
	private int targetType;

	/**
	 * How many targets did you meet
	 */
	private int achieved;

	/**
	 * Amount saved
	 */
	private double amountSaved;

	/**
	 * Corresponding user
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

	public int getTargetType() {
		return targetType;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public int isAchieved() {
		return achieved;
	}

	public void setAchieved(int achieved) {
		this.achieved = achieved;
	}

	public double getAmountSaved() {
		return amountSaved;
	}

	public void setAmountSaved(double amountSaved) {
		this.amountSaved = amountSaved;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

