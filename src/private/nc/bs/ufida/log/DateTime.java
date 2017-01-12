package nc.bs.ufida.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * ��д��Date���ʱ�䴦����, �ָ���һЩDate���б������ķ���, ����ʱ�䴦��, ʱ���ʽ��������ձ�����: <BR>
 * <BLOCKQUOTE>
 * <TABLE cellSpacing=1 cellPadding=1 border=1>
 * <TR bgColor=#ccccff>
 * <TH align=left>��ĸ</TH>
 * <TH align=left>���ڻ�ʱ��Ԫ��</TH>
 * <TH align=left>��ʾ</TH>
 * <TH align=left>ʾ��</TH>
 * </TR>
 * <TR>
 * <TD>G</TD>
 * <TD>Era ��־��</TD>
 * <TD>Text</TD>
 * <TD>AD</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>y</TD>
 * <TD>��</TD>
 * <TD>Year</TD>
 * <TD>1996, 96</TD>
 * </TR>
 * <TR>
 * <TD>M</TD>
 * <TD>���е��·�</TD>
 * <TD>Month</TD>
 * <TD>July; Jul; 07</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>w</TD>
 * <TD>���е�����</TD>
 * <TD>Number</TD>
 * <TD>27</TD>
 * </TR>
 * <TR>
 * <TD>W</TD>
 * <TD>�·��е�����</TD>
 * <TD>Number</TD>
 * <TD>2</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>D</TD>
 * <TD>���е�����</TD>
 * <TD>Number</TD>
 * <TD>189</TD>
 * </TR>
 * <TR>
 * <TD>d</TD>
 * <TD>�·��е�����</TD>
 * <TD>Number</TD>
 * <TD>10</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>F</TD>
 * <TD>�·��е�����</TD>
 * <TD>Number</TD>
 * <TD>2</TD>
 * </TR>
 * <TR>
 * <TD>E</TD>
 * <TD>�����е�����</TD>
 * <TD>Text</TD>
 * <TD>Tuesday; Tue</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>a</TD>
 * <TD>Am/pm ���</TD>
 * <TD>Text</TD>
 * <TD>PM</TD>
 * </TR>
 * <TR>
 * <TD>H</TD>
 * <TD>һ���е�Сʱ����0-23��</TD>
 * <TD>Number</TD>
 * <TD>0</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>k</TD>
 * <TD>һ���е�Сʱ����1-24��</TD>
 * <TD>Number</TD>
 * <TD>24</TD>
 * </TR>
 * <TR>
 * <TD>K</TD>
 * <TD>am/pm �е�Сʱ����0-11��</TD>
 * <TD>Number</TD>
 * <TD>0</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>h</TD>
 * <TD>am/pm �е�Сʱ����1-12��</TD>
 * <TD>Number</TD>
 * <TD>12</TD>
 * </TR>
 * <TR>
 * <TD>m</TD>
 * <TD>Сʱ�еķ�����</TD>
 * <TD>Number</TD>
 * <TD>30</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>s</TD>
 * <TD>�����е�����</TD>
 * <TD>Number</TD>
 * <TD>55</TD>
 * </TR>
 * <TR>
 * <TD>S</TD>
 * <TD>������</TD>
 * <TD>Number</TD>
 * <TD>978</TD>
 * </TR>
 * <TR bgColor=#eeeeff>
 * <TD>z</TD>
 * <TD>ʱ��</TD>
 * <TD>General time zone</TD>
 * <TD>Pacific Standard Time; PST; GMT-08:00</TD>
 * </TR>
 * <TR>
 * <TD>Z</TD>
 * <TD>ʱ��</TD>
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
	 * ��ǰʱ��
	 */
	public DateTime() {
		this(System.currentTimeMillis());
	}

	/**
	 * ָ��ʱ��
	 * 
	 * @param src
	 */
	public DateTime(Date src) {
		this(src.getTime());
	}

	/**
	 * ����ָ���ĸ�ʽ��ʱ���ַ���������DateTime����
	 * 
	 * @param pattern
	 *            ʱ���ʽ �� "yyyy-MM-dd HH:mm:ss"
	 * @param source
	 *            ʱ���ַ��� �� "2009-07-31 12:05:29"
	 * @return �����ʽ���������׳��쳣
	 */
	public DateTime(String pattern, String source) throws Exception {
		SimpleDateFormat s = new SimpleDateFormat(pattern);
		setTime(s.parse(source).getTime());
	}

	/**
	 * ָ��ʱ��
	 * 
	 * @param date
	 */
	public DateTime(long date) {
		this.setTime(date);
	}

	/**
	 * ָ��������
	 * 
	 * @param year
	 *            ��
	 * @param month
	 *            ��
	 * @param date
	 *            ��
	 */
	public DateTime(int year, int month, int date) {
		this(year, month, date, 0, 0, 0);
	}

	/**
	 * ָ��������ʱ��
	 * 
	 * @param year
	 *            ��
	 * @param month
	 *            ��
	 * @param date
	 *            ��
	 * @param hrs
	 *            ʱ
	 * @param min
	 *            ��
	 */
	public DateTime(int year, int month, int date, int hrs, int min) {
		this(year, month, date, hrs, min, 0);
	}

	/**
	 * ָ��������ʱ����
	 * 
	 * @param year
	 *            ��
	 * @param month
	 *            ��
	 * @param date
	 *            ��
	 * @param hrs
	 *            ʱ
	 * @param min
	 *            ��
	 * @param sec
	 *            ��
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
	 * �õ����
	 */
	public int getYear() {
		return getCalendarTime(Calendar.YEAR);
	}

	/**
	 * �޸����ֵ
	 */
	public void setYear(int year) {
		setCalendarTime(Calendar.YEAR, year);
	}

	/**
	 * �õ��·�
	 */
	public int getMonth() {
		return getCalendarTime(Calendar.MONTH) + 1;
	}

	/**
	 * �޸��·�ֵ, 1~12��
	 */
	public void setMonth(int month) {
		setCalendarTime(Calendar.MONTH, month - 1);
	}

	/**
	 * �õ���һ�����е�����, ������
	 */
	public int getDate() {
		return getCalendarTime(Calendar.DAY_OF_MONTH);
	}

	/**
	 * �޸�����ֵ
	 */
	public void setDate(int date) {
		setCalendarTime(Calendar.DATE, date);
	}

	/**
	 * �õ���һ���е�����, ���ܼ�, 0 ~ 6
	 */
	public int getDay() {
		return getCalendarTime(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * �õ����ڵı����ַ���
	 * 
	 * @return
	 */
	public String getWeek() {
		return format("EE");
	}

	/**
	 * �õ�24СʱֵСʱֵ
	 */
	public int getHours() {
		return getCalendarTime(Calendar.HOUR_OF_DAY);
	}

	/**
	 * ����24Сʱ�Ƶ�Сʱֵ 00 ~ 23
	 */
	public void setHours(int hours) {
		setCalendarTime(Calendar.HOUR_OF_DAY, hours);
	}

	/**
	 * �õ�����ֵ
	 */
	public int getMinutes() {
		return getCalendarTime(Calendar.MINUTE);
	}

	/**
	 * ���÷���ֵ
	 */
	public void setMinutes(int minutes) {
		setCalendarTime(Calendar.MINUTE, minutes);
	}

	/**
	 * �õ���ֵ
	 */
	public int getSeconds() {
		return getCalendarTime(Calendar.SECOND);
	}

	/**
	 * ������
	 */
	public void setSeconds(int seconds) {
		setCalendarTime(Calendar.SECOND, seconds);
	}

	/**
	 * �õ�����ֵ
	 */
	public int getMillisecond() {
		return getCalendarTime(Calendar.MILLISECOND);
	}

	/**
	 * ���ú���
	 */
	public void setMillisecond(int millisecond) {
		setCalendarTime(Calendar.MILLISECOND, millisecond);
	}

	/**
	 * �õ���ָ����ʽ��ʱ���ӡ
	 * 
	 * @param pattern
	 *            ʱ���ʽ, ��: "yyyy-MM-dd HH:mm:ss"
	 * @return ���ض�Ӧ��ʱ���ַ���, �� "2009-07-31 12:05:29"
	 */
	public synchronized String format(String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(this);
	}

	/**
	 * ����fieldָ����ʱ��ֵ
	 * 
	 * @param field
	 *            ��������
	 * @param value
	 *            ����ֵ
	 */
	public synchronized void setCalendarTime(int field, int value) {
		calendar.set(field, value);
		long time = calendar.getTime().getTime();
		super.setTime(time);
	}

	/**
	 * ���ó�����ʱ��ֵ
	 */
	public synchronized void setTime(long time) {
		super.setTime(time);
		calendar.setTimeInMillis(time);
	}

	/**
	 * ����fieldָ����ʱ��ֵ
	 * 
	 * @param field
	 *            �ֶ���
	 * @return �ֶζ�Ӧ��ʱ��ֵ
	 */
	public synchronized int getCalendarTime(int field) {
		return calendar.get(field);
	}

	/**
	 * "yyyy-MM-dd HH:mm:ss:SSS" ��ʽ���ַ���
	 */
	public String toString() {
		return format("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * ����ָ���ĸ�ʽ��ʱ���ַ���������DateTime����
	 * 
	 * @param pattern
	 *            ʱ���ʽ �� "yyyy-MM-dd HH:mm:ss"
	 * @param source
	 *            ʱ���ַ��� �� "2009-07-31 12:05:29"
	 * @return ��������򷵻�null
	 */
	public static DateTime parseDateTime(String pattern, String source) {
		return parseDateTime(pattern, source, null);
	}

	/**
	 * ����ָ���ĸ�ʽ��ʱ���ַ���������DateTime����
	 * 
	 * @param pattern
	 *            ʱ���ʽ �� "yyyy-MM-dd HH:mm:ss"
	 * @param source
	 *            ʱ���ַ��� �� "2009-07-31 12:05:29"
	 * 
	 * @param defaultValue
	 *            ����������ʱ���ص�Ĭ��ֵ
	 * 
	 * @return ��������򷵻�ָ����Ĭ��ֵ
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
