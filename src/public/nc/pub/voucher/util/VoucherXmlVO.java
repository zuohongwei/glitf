package nc.pub.voucher.util;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class VoucherXmlVO extends SuperVO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pk_subject ;
	private String subjcode ;
	private String subjname ;
	private String explanation ;
	private String pk_currency ;
	private String currency ;
	private UFDouble ybjf ;
	private UFDouble bbjf ;
	private UFDouble ybdf ;
	private UFDouble bbdf ;
	private String checktype1 ;
	private String checktypecode1;
	private String checktypevalue1 ;
	private String freevalueid1 ;
	private String valuecode1 ;
	private String valuename1 ;
	private String checktype2 ;
	private String checktypecode2;
	private String checktypevalue2 ;
	private String freevalueid2 ;
	private String valuecode2 ;
	private String valuename2 ;
	private String checktype3 ;
	private String checktypecode3;
	private String checktypevalue3 ;
	private String freevalueid3 ;
	private String valuecode3 ;
	private String valuename3 ;


	public UFDouble getBbdf() {
		return bbdf;
	}

	public void setBbdf(UFDouble bbdf) {
		this.bbdf = bbdf;
	}

	public UFDouble getBbjf() {
		return bbjf;
	}

	public void setBbjf(UFDouble bbjf) {
		this.bbjf = bbjf;
	}

	public String getChecktype1() {
		return checktype1;
	}

	public void setChecktype1(String checktype1) {
		this.checktype1 = checktype1;
	}

	public String getChecktype2() {
		return checktype2;
	}

	public void setChecktype2(String checktype2) {
		this.checktype2 = checktype2;
	}

	public String getChecktype3() {
		return checktype3;
	}

	public void setChecktype3(String checktype3) {
		this.checktype3 = checktype3;
	}

	public String getChecktypecode1() {
		return checktypecode1;
	}

	public void setChecktypecode1(String checktypecode1) {
		this.checktypecode1 = checktypecode1;
	}

	public String getChecktypecode2() {
		return checktypecode2;
	}

	public void setChecktypecode2(String checktypecode2) {
		this.checktypecode2 = checktypecode2;
	}

	public String getChecktypecode3() {
		return checktypecode3;
	}

	public void setChecktypecode3(String checktypecode3) {
		this.checktypecode3 = checktypecode3;
	}

	public String getChecktypevalue1() {
		return checktypevalue1;
	}

	public void setChecktypevalue1(String checktypevalue1) {
		this.checktypevalue1 = checktypevalue1;
	}

	public String getChecktypevalue2() {
		return checktypevalue2;
	}

	public void setChecktypevalue2(String checktypevalue2) {
		this.checktypevalue2 = checktypevalue2;
	}

	public String getChecktypevalue3() {
		return checktypevalue3;
	}

	public void setChecktypevalue3(String checktypevalue3) {
		this.checktypevalue3 = checktypevalue3;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getFreevalueid1() {
		return freevalueid1;
	}

	public void setFreevalueid1(String freevalueid1) {
		this.freevalueid1 = freevalueid1;
	}

	public String getFreevalueid2() {
		return freevalueid2;
	}

	public void setFreevalueid2(String freevalueid2) {
		this.freevalueid2 = freevalueid2;
	}

	public String getFreevalueid3() {
		return freevalueid3;
	}

	public void setFreevalueid3(String freevalueid3) {
		this.freevalueid3 = freevalueid3;
	}


	public String getPk_currency() {
		return pk_currency;
	}

	public void setPk_currency(String pk_currency) {
		this.pk_currency = pk_currency;
	}

	public String getPk_subject() {
		return pk_subject;
	}

	public void setPk_subject(String pk_subject) {
		this.pk_subject = pk_subject;
	}

	public String getSubjcode() {
		return subjcode;
	}

	public void setSubjcode(String subjcode) {
		this.subjcode = subjcode;
	}

	public String getSubjname() {
		return subjname;
	}

	public void setSubjname(String subjname) {
		this.subjname = subjname;
	}

	public String getValuecode1() {
		return valuecode1;
	}

	public void setValuecode1(String valuecode1) {
		this.valuecode1 = valuecode1;
	}

	public String getValuecode2() {
		return valuecode2;
	}

	public void setValuecode2(String valuecode2) {
		this.valuecode2 = valuecode2;
	}

	public String getValuecode3() {
		return valuecode3;
	}

	public void setValuecode3(String valuecode3) {
		this.valuecode3 = valuecode3;
	}

	public String getValuename1() {
		return valuename1;
	}

	public void setValuename1(String valuename1) {
		this.valuename1 = valuename1;
	}

	public String getValuename2() {
		return valuename2;
	}

	public void setValuename2(String valuename2) {
		this.valuename2 = valuename2;
	}

	public String getValuename3() {
		return valuename3;
	}

	public void setValuename3(String valuename3) {
		this.valuename3 = valuename3;
	}

	public UFDouble getYbdf() {
		return ybdf;
	}

	public void setYbdf(UFDouble ybdf) {
		this.ybdf = ybdf;
	}

	public UFDouble getYbjf() {
		return ybjf;
	}

	public void setYbjf(UFDouble ybjf) {
		this.ybjf = ybjf;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

}
