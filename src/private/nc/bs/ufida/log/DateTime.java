package nc.bs.ufida.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 重写了Date类的时间处理器, 恢复了一些Date类中被废弃的方法, 方便时间处理, 时间格式与意义对照表如下: <BR>
 * <BLOCKQUOTE>
 * <TABLE cellSpacing=1 cellPadding=1 border=1>
 * <TR bgColor=#ccccff>
 * <TH align=left>字母</TH>
 * <TH align=left>日期或时间元素</TH>
 * <TH align=left>表示</TH>
 * <TH align=left>示例</TH>
 * </TR>
 * <TR>
 * <TD>G</TD>
 * <TD>Era 标志符</TD>
 * <TD>Text</TD>
 * <TD>AD</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>y</TD>
 * <TD>年</TD>
 * <TD>Year</TD>
 * <TD>1996, 96</TD>
 * </TR>
 * <TR>
 * <TD>M</TD>
 * <TD>年中的月份</TD>
 * <TD>Month</TD>
 * <TD>July; Jul; 07</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>w</TD>
 * <TD>年中的周数</TD>
 * <TD>Number</TD>
 * <TD>27</TD>
 * </TR>
 * <TR>
 * <TD>W</TD>
 * <TD>月份中的周数</TD>
 * <TD>Number</TD>
 * <TD>2</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>D</TD>
 * <TD>年中的天数</TD>
 * <TD>Number</TD>
 * <TD>189</TD>
 * </TR>
 * <TR>
 * <TD>d</TD>
 * <TD>月份中的天数</TD>
 * <TD>Number</TD>
 * <TD>10</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>F</TD>
 * <TD>月份中的星期</TD>
 * <TD>Number</TD>
 * <TD>2</TD>
 * </TR>
 * <TR>
 * <TD>E</TD>
 * <TD>星期中的天数</TD>
 * <TD>Text</TD>
 * <TD>Tuesday; Tue</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>a</TD>
 * <TD>Am/pm 标记</TD>
 * <TD>Text</TD>
 * <TD>PM</TD>
 * </TR>
 * <TR>
 * <TD>H</TD>
 * <TD>一天中的小时数（0-23）</TD>
 * <TD>Number</TD>
 * <TD>0</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>k</TD>
 * <TD>一天中的小时数（1-24）</TD>
 * <TD>Number</TD>
 * <TD>24</TD>
 * </TR>
 * <TR>
 * <TD>K</TD>
 * <TD>am/pm 中的小时数（0-11）</TD>
 * <TD>Number</TD>
 * <TD>0</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>h</TD>
 * <TD>am/pm 中的小时数（1-12）</TD>
 * <TD>Number</TD>
 * <TD>12</TD>
 * </TR>
 * <TR>
 * <TD>m</TD>
 * <TD>小时中的分钟数</TD>
 * <TD>Number</TD>
 * <TD>30</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>s</TD>
 * <TD>分钟中的秒数</TD>
 * <TD>Number</TD>
 * <TD>55</TD>
 * </TR>
 * <TR>
 * <TD>S</TD>
 * <TD>毫秒数</TD>
 * <TD>Number</TD>
 * <TD>978</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>z</TD>
 * <TD>时区</TD>
 * <TD>General time zone</TD>
 * <TD>Pacific Standard Time; PST; GMT-08:00</TD>
 * </TR>
 * <TR>
 * <TD>Z</TD>
 * <TD>时区</TD>
 * <TD>RFC 822 time zone</TD>
 * <TD>-0800</TD>
 * </TR>
 * </TABLE>
 * </BLOCKQUOTE>
 * 
 * @see Date
 * @see Calendar
 * @author lixi
 */
public class DateTime extends Date {

	private static final long serialVersionUID = -1356090848890182218L;

	private GregorianCalendar calendar = new GregorianCalendar(1970, 1, 1);

	/**
	 * 当前时间
	 */
	public DateTime() {
		this(System.currentTimeMillis());
	}

	/**
	 * 指定时间
	 * 
	 * @param src
	 */
	public DateTime(Date src) {
		this(src.getTime());
	}

	/**
	 * 按照指定的格式将时间字符串解析成DateTime对象
	 * 
	 * @param pattern
	 *            时间格式 如 "yyyy-MM-dd HH:mm:ss"
	 * @param source
	 *            时间字符串 如 "2009-07-31 12:05:29"
	 * @return 如果格式化出错则抛出异常
	 */
	public DateTime(String pattern, String source) throws Exception {
		SimpleDateFormat s = new SimpleDateFormat(pattern);
		setTime(s.parse(source).getTime());
	}

	/**
	 * 指定时间
	 * 
	 * @param date
	 */
	public DateTime(long date) {
		this.setTime(date);
	}

	/**
	 * 指定年月日
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param date
	 *            日
	 */
	public DateTime(int year, int month, int date) {
		this(year, month, date, 0, 0, 0);
	}

	/**
	 * 指定年月日时分
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param date
	 *            日
	 * @param hrs
	 *            时
	 * @param min
	 *            分
	 */
	public DateTime(int year, int month, int date, int hrs, int min) {
		this(year, month, date, hrs, min, 0);
	}

	/**
	 * 指定年月日时分秒
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param date
	 *            日
	 * @param hrs
	 *            时
	 * @param min
	 *            分
	 * @param sec
	 *            秒
	 */
	public DateTime(int year, int month, int date, int hrs, int min, int sec) {
		setTime(0);

		setYear(year);
		setMonth(month);
		setDate(date);
		setHours(hrs);
		setMinutes(min);
		setSeconds(sec);
		setMillisecond(0);
	}

	/**
	 * 得到年份
	 */
	public int getYear() {
		return getCalendarTime(Calendar.YEAR);
	}

	/**
	 * 修改年份值
	 */
	public void setYear(int year) {
		setCalendarTime(Calendar.YEAR, year);
	}

	/**
	 * 得到月份
	 */
	public int getMonth() {
		return getCalendarTime(Calendar.MONTH) + 1;
	}

	/**
	 * 修改月份值, 1~12月
	 */
	public void setMonth(int month) {
		setCalendarTime(Calendar.MONTH, month - 1);
	}

	/**
	 * 得到在一个月中的天数, 即几号
	 */
	public int getDate() {
		return getCalendarTime(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 修改日期值
	 */
	public void setDate(int date) {
		setCalendarTime(Calendar.DATE, date);
	}

	/**
	 * 得到在一周中的天数, 即周几, 0 ~ 6
	 */
	public int getDay() {
		return getCalendarTime(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 得到星期的本地字符串
	 * 
	 * @return
	 */
	public String getWeek() {
		return format("EE");
	}

	/**
	 * 得到24小时值小时值
	 */
	public int getHours() {
		return getCalendarTime(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 设置24小时制的小时值 00 ~ 23
	 */
	public void setHours(int hours) {
		setCalendarTime(Calendar.HOUR_OF_DAY, hours);
	}

	/**
	 * 得到分钟值
	 */
	public int getMinutes() {
		return getCalendarTime(Calendar.MINUTE);
	}

	/**
	 * 设置分钟值
	 */
	public void setMinutes(int minutes) {
		setCalendarTime(Calendar.MINUTE, minutes);
	}

	/**
	 * 得到秒值
	 */
	public int getSeconds() {
		return getCalendarTime(Calendar.SECOND);
	}

	/**
	 * 设置秒
	 */
	public void setSeconds(int seconds) {
		setCalendarTime(Calendar.SECOND, seconds);
	}

	/**
	 * 得到毫秒值
	 */
	public int getMillisecond() {
		return getCalendarTime(Calendar.MILLISECOND);
	}

	/**
	 * 设置毫秒
	 */
	public void setMillisecond(int millisecond) {
		setCalendarTime(Calendar.MILLISECOND, millisecond);
	}

	/**
	 * 得到安指定格式的时间打印
	 * 
	 * @param pattern
	 *            时间格式, 如: "yyyy-MM-dd HH:mm:ss"
	 * @return 返回对应的时间字符串, 如 "2009-07-31 12:05:29"
	 */
	public synchronized String format(String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(this);
	}

	/**
	 * 设置field指定的时间值
	 * 
	 * @param field
	 *            参数类型
	 * @param value
	 *            参数值
	 */
	public synchronized void setCalendarTime(int field, int value) {
		calendar.set(field, value);
		long time = calendar.getTime().getTime();
		super.setTime(time);
	}

	/**
	 * 设置长整型时间值
	 */
	public synchronized void setTime(long time) {
		super.setTime(time);
		calendar.setTimeInMillis(time);
	}

	/**
	 * 返回field指定的时间值
	 * 
	 * @param field
	 *            字段名
	 * @return 字段对应的时间值
	 */
	public synchronized int getCalendarTime(int field) {
		return calendar.get(field);
	}

	/**
	 * "yyyy-MM-dd HH:mm:ss:SSS" 形式的字符串
	 */
	public String toString() {
		return format("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 按照指定的格式将时间字符串解析成DateTime对象
	 * 
	 * @param pattern
	 *            时间格式 如 "yyyy-MM-dd HH:mm:ss"
	 * @param source
	 *            时间字符串 如 "2009-07-31 12:05:29"
	 * @return 如果出错则返回null
	 */
	public static DateTime parseDateTime(String pattern, String source) {
		return parseDateTime(pattern, source, null);
	}

	/**
	 * 按照指定的格式将时间字符串解析成DateTime对象
	 * 
	 * @param pattern
	 *            时间格式 如 "yyyy-MM-dd HH:mm:ss"
	 * @param source
	 *            时间字符串 如 "2009-07-31 12:05:29"
	 * 
	 * @param defaultValue
	 *            当解析出错时返回的默认值
	 * 
	 * @return 如果出错则返回指定的默认值
	 */
	public static DateTime parseDateTime(String pattern, String source,
			DateTime defaultValue) {
		try {
			DateTime dt = new DateTime(pattern, source);
			return dt;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return defaultValue;
	}

}
