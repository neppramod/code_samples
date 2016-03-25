package ...mvc.service.reporting;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import org.joda.time.DateMidnight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ...datatype.generic.enums.UsageDataFormat;
import ...datatype.reporting.EnergyDataUsage;
import ...datatype.reporting.ReportData;
import ...datatype.reporting.abstracts.UserData;
import ...domain.reporting.HourlyReport;
import ...domain.user.User;
import ...mvc.dao.interfaces.HourlyReportDAO;
import ...mvc.service.reporting.interfaces.Report;
import ...util.customdatatype.Date;

/**
 * Daily report service that specifically calculates total, max, min data in
 * hourly basis collected over one day
 * 
 * @author pramod
 * 
 */
@Service
public class DailyReport extends UserData implements Report {

	@Autowired
	private HourlyReportDAO hourlyReportDAO;

	private Date day;
	private User user;
	private boolean empty = true;
	private UsageDataFormat usageDataFormat;

	/** midnight start timestamp of day. */
	private long startTime;

	/** midnight - 1 minute of next day's start */
	private long endTime;

	/** All of our hourly reports over startTime and endTime */
	private List<HourlyReport> localHourlyReports;

	@Override
	public ReportData getReportData() {
		List<HourlyReport> hourlyReports = getLocalHourlyReports();

		EnergyDataUsage energyUsage = new EnergyDataUsage();
		int totalDuration = 0;
		int totalConsumption = 0;
		long reportActualStartTime = startTime;
		long reportActualEndTime = endTime;

		// Based on all the collected hourly reports calculate total consumption
		// and total duration
		for (HourlyReport hourlyReport : hourlyReports) {
			totalConsumption += hourlyReport.getConsumption();
			totalDuration += hourlyReport.getDuration();
		}

		energyUsage.setStartDate(new Date(reportActualStartTime));
		energyUsage.setEndDate(new Date(reportActualEndTime));
		energyUsage.setTotalConsumption(totalConsumption);
		energyUsage.setTotalDuration(totalDuration);

		ReportData reportData = new ReportData();

		reportData.setDataReturnFormat(usageDataFormat);
		reportData.setEneryUsageData(energyUsage);

		return reportData;
	}

	@Override
	public EnergyDataUsage getTotalEnergyUsage() {
		List<HourlyReport> hourlyReports = getLocalHourlyReports();

		EnergyDataUsage energyUsage = new EnergyDataUsage();
		int totalDuration = 0;
		int totalConsumption = 0;
		long reportActualStartTime = startTime;
		long reportActualEndTime = endTime;

		// Based on all the collected hourly reports calculate total consumption
		// and total duration
		for (HourlyReport hourlyReport : hourlyReports) {
			totalConsumption += hourlyReport.getConsumption();
			totalDuration += hourlyReport.getDuration();
		}

		energyUsage.setStartDate(new Date(reportActualStartTime));
		energyUsage.setEndDate(new Date(reportActualEndTime));
		energyUsage.setTotalConsumption(totalConsumption);
		energyUsage.setTotalDuration(totalDuration);

		return energyUsage;
	}

	@Override
	public EnergyDataUsage getPeakUsage() {
		List<HourlyReport> hourlyReports = getLocalHourlyReports();

		EnergyDataUsage energyUsage = new EnergyDataUsage();
		HourlyReport peakHourlyReport = null;

		long reportActualStartTime = startTime;
		long reportActualEndTime = endTime;

		if (hourlyReports.size() > 0) {
			reportActualStartTime = hourlyReports.get(0).getStartTime();
			reportActualEndTime = hourlyReports.get(hourlyReports.size() - 1)
					.getStartTime();
		}

		for (HourlyReport hourlyReport : hourlyReports) {

			if (peakHourlyReport == null)
				peakHourlyReport = hourlyReport;

			if (hourlyReport.getConsumption() > peakHourlyReport
					.getConsumption())
				peakHourlyReport = hourlyReport;
		}

		if (peakHourlyReport != null) {
			energyUsage.setStartDate(new Date(reportActualStartTime));
			energyUsage.setEndDate(new Date(reportActualEndTime));
			energyUsage.setTotalConsumption(peakHourlyReport.getConsumption());

			long peakStartTime = peakHourlyReport.getStartTime();
			Format formatter = new SimpleDateFormat("HH");

			java.util.Date date = new java.util.Date();
			date.setTime(peakStartTime * 1000L);

			int peakFormattedTime = Integer.parseInt(formatter.format(date));

			energyUsage.setTotalDuration(peakFormattedTime);
		}

		return energyUsage;
	}

	@Override
	public EnergyDataUsage getLeastUsage() {
		List<HourlyReport> hourlyReports = getLocalHourlyReports();
		EnergyDataUsage energyUsage = new EnergyDataUsage();

		HourlyReport peakHourlyReport = null;

		long reportActualStartTime = startTime;
		long reportActualEndTime = endTime;

		if (hourlyReports.size() > 0) {
			reportActualStartTime = hourlyReports.get(0).getStartTime();
			reportActualEndTime = hourlyReports.get(hourlyReports.size() - 1)
					.getStartTime();
		}

		for (HourlyReport hourlyReport : hourlyReports) {

			if (peakHourlyReport == null)
				peakHourlyReport = hourlyReport;

			if (hourlyReport.getConsumption() < peakHourlyReport
					.getConsumption())
				peakHourlyReport = hourlyReport;
		}

		if (peakHourlyReport != null) {
			energyUsage.setStartDate(new Date(reportActualStartTime));
			energyUsage.setEndDate(new Date(reportActualEndTime));
			energyUsage.setTotalConsumption(peakHourlyReport.getConsumption());

			long peakStartTime = peakHourlyReport.getStartTime();

			Format formatter = new SimpleDateFormat("HH");

			java.util.Date date = new java.util.Date();
			date.setTime(peakStartTime * 1000L);

			int peakFormattedTime = Integer.parseInt(formatter.format(date));

			energyUsage.setTotalDuration(peakFormattedTime);
		}

		return energyUsage;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void setDay(Date day) {
		this.day = day;
	}

	@Override
	public Date getDay() {
		return day;
	}

	@Override
	public boolean isEmpty() {
		return empty;
	}

	@Override
	public void setUsageDataFormat(UsageDataFormat usageDataFormat) {
		this.usageDataFormat = usageDataFormat;
	}

	// Private methods

	/**
	 * List of all hourly reports collected over this day
	 * 
	 * @return
	 */
	private List<HourlyReport> getLocalHourlyReports() {

		DateMidnight first = new DateMidnight(day.toUnixTimeStamp() * 1000L);

		/** Today night */
		startTime = first.getMillis() / 1000L;

		/** 24 hours - 1 minutes from today night */
		endTime = first.plusDays(1).toDateTime().minusMinutes(1).getMillis() / 1000L;

		// Lazy loading to localHourlyReports
		// Hibernate caches localHourlyReports so we omit it
		// if (localHourlyReports == null)
		localHourlyReports = hourlyReportDAO
				.findHourlyReportsBetweenTwoStartTimes(startTime, endTime, user);

		// In some places where we want to keep or
		if (localHourlyReports.size() > 0)
			empty = false;

		return localHourlyReports;
	}

	/**
	 * Sometimes we need to only extract hourly reports for certain time of day
	 * for that we need to return hourly reports collected for whole day This
	 * method is not complaint with our usual interface or abstract class
	 */
	public List<HourlyReport> getHourlyReports() {
		List<HourlyReport> hourlyReports = getLocalHourlyReports();

		return hourlyReports;
	}
}

