package nc.pub.voucher.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.fi.pub.GLOrgBookAcc;
import nc.itf.gl.pub.IFreevaluePub;
import nc.itf.uap.bd.multibook.IGLOrgBookAcc;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.bd.b54.GlorgbookVO;
import nc.vo.gl.pubvoucher.DetailVO;
import nc.vo.gl.pubvoucher.VoucherVO;
import nc.vo.glcom.ass.AssVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class VoucherChangeUtil {
	private VoucherInfo vInfo;
	private String voucherDealClassName;
	private Map<String, String[]> subjectMap;
	private Map<String, String[]> deptMap;
	private Map<String, String[]> custMap;
	private Map<String, String> currencyTypeMap;
	private Map<String, String[]> checkTypeMap;
	private Map<String, String[]> jobMap;
	private Map<String, String[]> psnMap;

	private BaseDAO dao;

	public final static String SYSTEMNAME = "YZW";

	public final static String PK_SYSTEM = "YZW";

	private BaseDAO getQryService() {
		if (dao == null) {
			dao = new BaseDAO();
		}

		return dao;
	}

	private String[] getpsnByCode(String psnCode) throws BusinessException {
		if (psnCode == null || "".equals(psnCode)) {
			throw new BusinessException("人员编码不能为空");
		}
		if (!psnMap.containsKey(psnCode)) {
			throw new BusinessException("找不到编码为" + psnCode + "人员");
		}
		return psnMap.get(psnCode);
	}

	private String[] getJobInfoByCode(String jobCode) throws BusinessException {
		if (jobCode == null || "".equals(jobCode)) {
			throw new BusinessException("项目编码不能为空");
		}
		if (!jobMap.containsKey(jobCode)) {
			throw new BusinessException("找不到编码为" + jobCode + "项目");
		}
		return jobMap.get(jobCode);
	}

	private String getCurrTypePkByCode(String currtypeCode)
			throws BusinessException {
		if (currtypeCode == null || "".equals(currtypeCode)) {
			return currencyTypeMap.get("CNY");
		}
		if (!currencyTypeMap.containsKey(currtypeCode)) {
			throw new BusinessException("找不到编码为" + currtypeCode + "的币种");
		}
		return currencyTypeMap.get(currtypeCode);
	}

	private String[] getCheckTypeInfoByCode(String checkTypeCode)
			throws BusinessException {
		if (checkTypeCode == null || "".equals(checkTypeCode)) {
			throw new BusinessException("辅助核算类型编码不能为空");
		}
		if (!checkTypeMap.containsKey(checkTypeCode)) {
			throw new BusinessException("找不到编码为" + checkTypeCode + "的辅助核算类型");
		}
		return checkTypeMap.get(checkTypeCode);
	}

	private String[] getCheckValueInfo(String checkTypeCode,
			String checkValueCode) throws BusinessException {
		if (checkValueCode == null || "".equals(checkValueCode)) {
			throw new BusinessException("辅助核算内容编码不能为空");
		}
		String[] checkValueInfo = new String[3];
		if (checkTypeCode.equals("2")) {
			checkValueInfo = getDeptInfoByCode(checkValueCode);
		} else if (checkTypeCode.equals("73") || checkTypeCode.equals("71")
				|| checkTypeCode.equals("72")) {
			checkValueInfo = getCustInfoByCode(checkValueCode);
		} else if (checkTypeCode.equals("jobass")) {
			checkValueInfo = getJobInfoByCode(checkValueCode);
		} else if (checkTypeCode.equals("1")) {
			checkValueInfo = getpsnByCode(checkValueCode);
		} else {
			throw new BusinessException("辅助核算类型不合法");
		}
		return checkValueInfo;
	}

	private String[] getSubjectInfoByCode(String subjcode)
			throws BusinessException {
		if (subjcode == null || "".equals(subjcode)) {
			throw new BusinessException("科目编码不能为空");
		}
		if (!subjectMap.containsKey(subjcode)) {
			throw new BusinessException("找不到编码为" + subjcode + "的科目");
		}
		return subjectMap.get(subjcode);
	}

	private String[] getDeptInfoByCode(String deptCode)
			throws BusinessException {
		if (!deptMap.containsKey(deptCode)) {
			throw new BusinessException("找不到编码为" + deptCode + "的部门");
		}
		return deptMap.get(deptCode);
	}

	private String[] getCustInfoByCode(String custcode)
			throws BusinessException {
		if (!custMap.containsKey(custcode)) {
			throw new BusinessException("找不到编码为" + custcode + "的客商");
		}
		return custMap.get(custcode);
	}

	private void initBaseDoc() throws BusinessException {
		subjectMap = new HashMap<String, String[]>();
		deptMap = new HashMap<String, String[]>();
		custMap = new HashMap<String, String[]>();
		currencyTypeMap = new HashMap<String, String>();
		checkTypeMap = new HashMap<String, String[]>();
		jobMap = new HashMap<String, String[]>();
		psnMap = new HashMap<String, String[]>();
		// 查询出当期公司所有的人员
		String psnQrySql = "select pk_psndoc,psncode,psnname from bd_psndoc where pk_corp='"
				+ getVoucherInfo().getPk_corp() + "' and isnull(dr,0)=0";
		List<Map<String, String>> psnMapList = (List<Map<String, String>>) getQryService()
				.executeQuery(psnQrySql, new MapListProcessor());
		for (Map<String, String> map : psnMapList) {
			String key = map.get("psncode");
			String[] value = new String[] { map.get("pk_psndoc"),
					map.get("psncode"), map.get("psnname") };
			psnMap.put(key, value);
		}
		// 查询出当期公司所有的科目
		String subjectQrySql = "select pk_accsubj,subjcode,subjname from bd_accsubj where pk_glorgbook='"
				+ getVoucherInfo().getPk_glorgbook() + "' and isnull(dr,0)=0";
		List<Map<String, String>> subjMapList = (List<Map<String, String>>) getQryService()
				.executeQuery(subjectQrySql, new MapListProcessor());
		for (Map<String, String> map : subjMapList) {
			String key = map.get("subjcode");
			String[] value = new String[] { map.get("pk_accsubj"),
					map.get("subjcode"), map.get("subjname") };
			subjectMap.put(key, value);
		}
		// 查询出当期公司所有的部门
		String deptQrySql = "select pk_deptdoc,deptcode,deptname from bd_deptdoc where pk_corp='"
				+ getVoucherInfo().getPk_corp() + "' and isnull(dr,0)=0";
		List<Map<String, String>> deptMapList = (List<Map<String, String>>) getQryService()
				.executeQuery(deptQrySql, new MapListProcessor());
		for (Map<String, String> map : deptMapList) {
			String key = map.get("deptcode");
			String[] value = new String[] { map.get("pk_deptdoc"),
					map.get("deptcode"), map.get("deptname") };
			deptMap.put(key, value);
		}
		// 询出当期公司所有的客商
		String custQrySql = "select bd_cubasdoc.pk_cubasdoc,custcode,custname from bd_cubasdoc inner join bd_cumandoc on bd_cubasdoc.pk_cubasdoc=bd_cumandoc.pk_cubasdoc where bd_cumandoc.pk_corp='"
				+ getVoucherInfo().getPk_corp()
				+ "' and isnull(bd_cumandoc.dr,0)=0 and isnull(bd_cubasdoc.dr,0)=0";
		List<Map<String, String>> custMapList = (List<Map<String, String>>) getQryService()
				.executeQuery(custQrySql, new MapListProcessor());
		for (Map<String, String> map : custMapList) {
			String key = map.get("custcode");
			String[] value = new String[] { map.get("pk_cubasdoc"),
					map.get("custcode"), map.get("custname") };
			custMap.put(key, value);
		}
		// 查询出所有币种
		String currerncyTypeQrySql = "select pk_currtype,currtypecode from bd_currtype where isnull(dr,0)=0";
		List<Map<String, String>> currtypeMapList = (List<Map<String, String>>) getQryService()
				.executeQuery(currerncyTypeQrySql, new MapListProcessor());
		for (Map<String, String> map : currtypeMapList) {
			currencyTypeMap
					.put(map.get("currtypecode"), map.get("pk_currtype"));
		}
		// 查询出需要的辅助核算类型
		String checkTypeQrySql = "select pk_bdinfo,bdcode,bdname from bd_bdinfo where bdcode in ('1','2','73','71','72','jobass') and isnull(dr,0)=0";
		List<Map<String, String>> checkTypeMapList = (List<Map<String, String>>) getQryService()
				.executeQuery(checkTypeQrySql, new MapListProcessor());
		for (Map<String, String> map : checkTypeMapList) {
			String key = map.get("bdcode");
			String[] value = new String[] { map.get("pk_bdinfo"),
					map.get("bdcode"), map.get("bdname") };
			checkTypeMap.put(key, value);
		}
		// 项目辅助核算
		String jobQrySql = "select jobcode,jobname,pk_jobbasfil from bd_jobbasfil where  isnull(dr,0)=0";
		List<Map<String, String>> jobMapList = (List<Map<String, String>>) getQryService()
				.executeQuery(jobQrySql, new MapListProcessor());
		for (Map<String, String> map : jobMapList) {
			String key = map.get("jobcode");
			String[] value = new String[] { map.get("pk_jobbasfil"),
					map.get("jobcode"), map.get("jobname") };
			jobMap.put(key, value);
		}
	}

	public VoucherChangeUtil(VoucherInfo info, String voucherDealClassName) {
		this.vInfo = info;
		this.voucherDealClassName = voucherDealClassName;
	}

	private void changeVoucherInfo(VoucherInfo info) throws Exception {
		String corpcode = info.getCorpcode();
		Object pk_corp = getQryService()
				.executeQuery(
						"select pk_corp from bd_corp where unitcode='"
								+ corpcode + "'", new ColumnProcessor(1));
		if (null == pk_corp || "".equals(pk_corp)) {
			throw new BusinessException("公司编码不存在");
		}
		Object userid = getQryService()
				.executeQuery(
						"SELECT userid FROM sm_userandclerk WHERE pk_psndoc in(SELECT pk_psnbasdoc FROM bd_psndoc where psncode='"
								+ info.getPsncode() + "' and pk_corp='"
								+pk_corp
								+"')",
						new ColumnProcessor(1));
		if (null == userid || "".equals(userid)) {
			throw new BusinessException("报销人员编码不存在");
		}
		info.setUserid(userid.toString());
		info.setPk_corp(pk_corp.toString());
		info.setPk_glorgbook(((IGLOrgBookAcc) NCLocator.getInstance().lookup(
				IGLOrgBookAcc.class.getName()))
				.getDefaultGlOrgBookVOByPk_EntityOrg(info.getPk_corp())
				.getPrimaryKey());
	}

	private VoucherInfo getVoucherInfo() {
		return vInfo;
	}

	private void changeVoucherXmlVO(List<VoucherXmlVO> list)
			throws BusinessException {
		for (VoucherXmlVO xmlVO : list) {
			String[] subjectInfo = getSubjectInfoByCode(xmlVO.getSubjcode());
			xmlVO.setSubjname(subjectInfo[2]);
			xmlVO.setPk_subject(subjectInfo[0]);
			xmlVO.setPk_currency(getCurrTypePkByCode(xmlVO.getPk_currency()));
			if (xmlVO.getChecktype1() != null
					&& !"".equals(xmlVO.getChecktype1())) {
				String[] checkTypeInfo = getCheckTypeInfoByCode(xmlVO
						.getChecktype1());
				xmlVO.setChecktype1(checkTypeInfo[0]);
				xmlVO.setChecktypecode1(checkTypeInfo[1]);
				xmlVO.setChecktypevalue1(checkTypeInfo[2]);
				String[] checkValueInfo = getCheckValueInfo(
						xmlVO.getChecktypecode1(), xmlVO.getValuecode1());
				xmlVO.setFreevalueid1(checkValueInfo[0]);
				xmlVO.setValuecode1(checkValueInfo[1]);
				xmlVO.setValuename1(checkValueInfo[2]);
			}
			if (xmlVO.getChecktype2() != null
					&& !"".equals(xmlVO.getChecktype2())) {
				String[] checkTypeInfo = getCheckTypeInfoByCode(xmlVO
						.getChecktype2());
				xmlVO.setChecktype2(checkTypeInfo[0]);
				xmlVO.setChecktypecode2(checkTypeInfo[1]);
				xmlVO.setChecktypevalue2(checkTypeInfo[2]);
				String[] checkValueInfo = getCheckValueInfo(
						xmlVO.getChecktypecode2(), xmlVO.getValuecode2());
				xmlVO.setFreevalueid2(checkValueInfo[0]);
				xmlVO.setValuecode2(checkValueInfo[1]);
				xmlVO.setValuename2(checkValueInfo[2]);
			}
			if (xmlVO.getChecktype3() != null
					&& !"".equals(xmlVO.getChecktype3())) {
				String[] checkTypeInfo = getCheckTypeInfoByCode(xmlVO
						.getChecktype3());
				xmlVO.setChecktype3(checkTypeInfo[0]);
				xmlVO.setChecktypecode3(checkTypeInfo[1]);
				xmlVO.setChecktypevalue3(checkTypeInfo[2]);
				String[] checkValueInfo = getCheckValueInfo(
						xmlVO.getChecktypecode3(), xmlVO.getValuecode3());
				xmlVO.setFreevalueid3(checkValueInfo[0]);
				xmlVO.setValuecode3(checkValueInfo[1]);
				xmlVO.setValuename3(checkValueInfo[2]);
			}
		}
	}

	public VoucherVO changeByVoucherXmlVO(List<VoucherXmlVO> list)
			throws Exception {
		changeVoucherInfo(getVoucherInfo());
		initBaseDoc();
		changeVoucherXmlVO(list);
		VoucherVO voucher = new VoucherVO();
		try {
			DetailVO[] details = new DetailVO[list.size()];
			for (int i = 0; i < list.size(); i++) {
				VoucherXmlVO xmlVO = list.get(i);
				details[i] = new DetailVO();

				details[i].setAccsubjcode(xmlVO.getSubjcode());
				details[i].setAccsubjname(xmlVO.getSubjname());
				details[i].setPk_accsubj(xmlVO.getPk_subject());
				details[i].setCreditamount(xmlVO.getYbdf());
				details[i].setLocalcreditamount(xmlVO.getBbdf());
				details[i].setDebitamount(xmlVO.getYbjf());
				details[i].setLocaldebitamount(xmlVO.getBbjf());
				details[i].setYear(getVoucherInfo().getYear());
				details[i].setPeriod(getVoucherInfo().getPeriod());
				details[i].setPk_corp(getVoucherInfo().getPk_corp());
				details[i].setPk_currtype(xmlVO.getPk_currency());// CNY
				details[i].setExplanation(xmlVO.getExplanation() == null ? "a"
						: xmlVO.getExplanation());
				details[i].setVoucherkind(0);

				List<AssVO> assList = new ArrayList<AssVO>();
				if (xmlVO.getChecktype1() != null
						&& xmlVO.getValuecode1() != null) {
					AssVO vo = new AssVO();
					vo.setChecktypecode(xmlVO.getChecktypecode1());
					vo.setChecktypename(xmlVO.getChecktypevalue1());
					vo.setCheckvaluecode(xmlVO.getValuecode1());
					vo.setCheckvaluename(xmlVO.getValuename1());
					vo.setPk_Checktype(xmlVO.getChecktype1());
					vo.setPk_Checkvalue(xmlVO.getFreevalueid1());

					assList.add(vo);
				}

				if (xmlVO.getChecktype2() != null
						&& xmlVO.getValuecode2() != null) {
					AssVO vo = new AssVO();
					vo.setChecktypecode(xmlVO.getChecktypecode2());
					vo.setChecktypename(xmlVO.getChecktypevalue2());
					vo.setCheckvaluecode(xmlVO.getValuecode2());
					vo.setCheckvaluename(xmlVO.getValuename2());
					vo.setPk_Checktype(xmlVO.getChecktype2());
					vo.setPk_Checkvalue(xmlVO.getFreevalueid2());

					assList.add(vo);
				}

				if (xmlVO.getChecktype3() != null
						&& xmlVO.getValuecode3() != null) {
					AssVO vo = new AssVO();
					vo.setChecktypecode(xmlVO.getChecktypecode3());
					vo.setChecktypename(xmlVO.getChecktypevalue3());
					vo.setCheckvaluecode(xmlVO.getValuecode3());
					vo.setCheckvaluename(xmlVO.getValuename3());
					vo.setPk_Checktype(xmlVO.getChecktype3());
					vo.setPk_Checkvalue(xmlVO.getFreevalueid3());

					assList.add(vo);
				}
				AssVO[] assVos = new AssVO[assList.size()];
				assList.toArray(assVos);
				details[i].setAss(assVos);
			}
			voucher.setDetails(details);
			setVoucherDefault(voucher);
		} catch (Exception e) {
			Logger.error(e);
			throw e;
		}
		return voucher;
	}

	private void setVoByPkGlorgbook(VoucherVO vo) {
		vo.setPk_glorgbook(getVoucherInfo().getPk_glorgbook());
		GlorgbookVO glorgbookvo = null;
		try {
			glorgbookvo = GLOrgBookAcc.getGlOrgBookVOByPk(getVoucherInfo()
					.getPk_glorgbook());
		} catch (BusinessException e) {
			Logger.error("", e);
			Logger.error("根据主体帐簿" + getVoucherInfo().getPk_glorgbook()
					+ "无法取得主体帐簿相关信息..");
		}
		if (glorgbookvo != null) {
			String pk_glbook = glorgbookvo.getPk_glbook();
			vo.setPk_glbook(pk_glbook);
			String pk_glorg = glorgbookvo.getPk_glorg();
			vo.setPk_glorg(pk_glorg);
		}
	}

	private void setGlorgParameter(VoucherVO vouchervo)
			throws BusinessException {
		if ("0001".equals(getVoucherInfo().getPk_corp())) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("pfxx", "UPPpfxx-000221")/*
																		 * @res
																		 * "无法给集团制作凭证，请检查数据文件!"
																		 */);
		} else {
			vouchervo.setPk_corp(getVoucherInfo().getPk_corp());
			setVoByPkGlorgbook(vouchervo);
		}
	}

	public VoucherVO setVoucherDefault(VoucherVO vouchervo) throws Exception {
		String editaddentryflag = "Y";
		String editheadflag = "YYY";
		String editbodyflag = "YYYYYYYYYYYYYYYY";
		vouchervo.setDiscardflag(UFBoolean.FALSE);
		vouchervo.setSystemname(SYSTEMNAME);
		vouchervo.setPk_system(PK_SYSTEM);
		vouchervo.setPk_prepared(getVoucherInfo().getUserid());
		vouchervo.setPrepareddate(getVoucherInfo().getBusidate());
		vouchervo.setVoucherkind(0);
//		vouchervo.setVouchertypename("其他转账");
//		vouchervo.setPk_vouchertype("0001DB100000000018L8");
		vouchervo.setVouchertypename("记");
		vouchervo.setPk_vouchertype("0001DEFAULT000000001");
		vouchervo.setYear(getVoucherInfo().getYear());
		vouchervo.setPeriod(getVoucherInfo().getPeriod());
		// 主体帐簿设置
		setGlorgParameter(vouchervo);

		vouchervo.setAddclass(voucherDealClassName);
		vouchervo.setModifyclass(voucherDealClassName);
		vouchervo.setDeleteclass(voucherDealClassName);
		vouchervo.setModifyflag(editheadflag);
		vouchervo
				.setDetailmodflag(editaddentryflag.equals("Y") ? new UFBoolean(
						true) : new UFBoolean(false));
		vouchervo.setSignflag(new UFBoolean(false));
		vouchervo.setExplanation(getVoucherInfo().getMemo());
		UFDouble m_totaldebit = new UFDouble(0);
		UFDouble m_totalcredit = new UFDouble(0);
		Vector details = new Vector();
		for (int i = 0; i < vouchervo.getNumDetails(); i++) {
			DetailVO detail = vouchervo.getDetail(i);
			detail.setPk_corp(vouchervo.getPk_corp());
			detail.setDetailindex(new Integer(i + 1));
			if (detail.getPrice() == null) {
				detail.setPrice(new UFDouble(0));
			}
			if (detail.getExcrate1() == null) {
				detail.setExcrate1(new UFDouble(0));
			}
			if (detail.getExcrate2() == null) {
				detail.setExcrate2(new UFDouble(0));
			}
			if (detail.getDebitquantity() == null) {
				detail.setDebitquantity(new UFDouble(0));
			}
			if (detail.getDebitamount() == null) {
				detail.setDebitamount(new UFDouble(0));
			}
			if (detail.getFracdebitamount() == null) {
				detail.setFracdebitamount(new UFDouble(0));
			}
			if (detail.getLocaldebitamount() == null) {
				detail.setLocaldebitamount(new UFDouble(0));
			}
			if (detail.getCreditquantity() == null) {
				detail.setCreditquantity(new UFDouble(0));
			}
			if (detail.getCreditamount() == null) {
				detail.setCreditamount(new UFDouble(0));
			}
			if (detail.getFraccreditamount() == null) {
				detail.setFraccreditamount(new UFDouble(0));
			}
			if (detail.getLocalcreditamount() == null) {
				detail.setLocalcreditamount(new UFDouble(0));
			}
			if (detail.getPk_corp() == null) {
				detail.setPk_corp(vouchervo.getPk_corp());
			}
			detail.setModifyflag(editbodyflag);

			if (detail.getAss() != null) {
				// 容纳U860导出的科目备查错误Begin
				AssVO[] assvos = detail.getAss();
				List asslist = new ArrayList(assvos.length);
				for (int j = 0; j < assvos.length; j++) {
					if (assvos[j] != null
							&& assvos[j].getPk_Checkvalue() != null
							&& assvos[j].getPk_Checkvalue().length() != 0
							&& !assvos[j].getPk_Checkvalue().equals("null"))
						asslist.add(assvos[j]);
				}
				if (asslist.size() != 0)
					detail.setAss((AssVO[]) asslist.toArray(new AssVO[0]));
				else
					detail.setAss(null);
				IFreevaluePub freebo = (IFreevaluePub) NCLocator.getInstance()
						.lookup(IFreevaluePub.class.getName());

				String assid = freebo.getAssID(detail.getAss(), new Boolean(
						true));
				if ("".equals(assid)) {
					throw new nc.bs.pfxx.manualload.BusinessProcessException(
							nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("pfxx", "UPPpfxx-000222")/*
																		 * @res
																		 * "无效的辅助核算设置！"
																		 */);
				}
				detail.setAssid(assid);
			}
			m_totalcredit = m_totalcredit.add(detail.getLocalcreditamount());
			m_totaldebit = m_totaldebit.add(detail.getLocaldebitamount());
			details.addElement(detail);
		}
		vouchervo.setTotalcredit(m_totalcredit);
		vouchervo.setTotaldebit(m_totaldebit);
		vouchervo.setDetail(details);

		return vouchervo;
	}
}
