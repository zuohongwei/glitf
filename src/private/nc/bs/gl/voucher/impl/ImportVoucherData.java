package nc.bs.gl.voucher.impl;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import nc.bs.framework.common.NCLocator;
import nc.bs.ufida.log.VoucherLogInfo;
import nc.itf.gl.voucher.IImportVoucherData;
import nc.itf.gl.voucher.IVoucher;
import nc.pub.voucher.util.VoucherChangeUtil;
import nc.pub.voucher.util.VoucherInfo;
import nc.pub.voucher.util.VoucherXmlVO;
import nc.vo.gl.pubvoucher.VoucherVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class ImportVoucherData implements IImportVoucherData {

	public void importVoucherData(String json) {
		VoucherLogInfo.info(json);
		String sourceid = "";
		String msg = "";
		if (json != null && !"".equals(json)) {
			try {
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonArray = jsonObject.getJSONArray("billList");
				if (null != jsonArray && jsonArray.length() > 0) {
					JSONObject return_obj = new JSONObject();//����json
					return_obj.put("requestId", jsonObject.getString("requestId"));
					JSONArray return_array = new JSONArray();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject return_obj1 = new JSONObject();
						JSONObject object = jsonArray.getJSONObject(i);
						VoucherInfo info = new VoucherInfo();
						UFDate busidate = new UFDate(
								object.getString("busidate"));
						info.setBusidate(busidate);
						info.setCorpcode(object.getString("corpcode"));
						String psncode = object.getString("psncode");
						info.setPsncode(psncode);
						sourceid = object.getString("sourceid");
						info.setSourceid(sourceid);
						info.setMemo(object.getString("memo"));
						String year = "" + busidate.getYear();
						String period = "" + busidate.getMonth();
						if (period.length() == 1) {
							period = "0" + period;
						}
						info.setYear(year);
						info.setPeriod(period);
						JSONArray details = object.getJSONArray("detail");
						if (null != details && details.length() > 0) {
							List<VoucherXmlVO> list = new ArrayList<VoucherXmlVO>();
							UFDouble df = UFDouble.ZERO_DBL;
							for (int j = 0; j < details.length(); j++) {
								JSONObject detail = details.getJSONObject(j);
								VoucherXmlVO voucherXmlVO = new VoucherXmlVO();
								voucherXmlVO.setSubjcode(detail
										.getString("subjcode"));
								UFDouble jf = new UFDouble(
										detail.getString("amount"));
								voucherXmlVO.setBbjf(jf);
								voucherXmlVO.setYbjf(jf);
								df = df.add(jf);
								voucherXmlVO.setExplanation(detail
										.getString("digest"));
								String deptcode = object.getString("deptcode");
								String custcode = object.getString("custcode");
								String jobcide = object.getString("jobcode");
								if(null != deptcode && !"".equals(deptcode)){
									voucherXmlVO.setChecktype1("2");
									voucherXmlVO.setValuecode1(deptcode);
								}
								if(null != custcode && !"".equals(custcode)){
									voucherXmlVO.setChecktype2("73");
									voucherXmlVO.setValuecode2(custcode);
								}
								if(null != jobcide && !"".equals(jobcide)){
									voucherXmlVO.setChecktype3("jobass");
									voucherXmlVO.setValuecode3(jobcide);
								}
								list.add(voucherXmlVO);
							}
							VoucherXmlVO dfVO = new VoucherXmlVO();
							dfVO.setSubjcode("14050215");// ������Ŀ22020306\Ԥ��һ�����
							dfVO.setBbdf(df);
							dfVO.setYbdf(df);
							dfVO.setExplanation(object.getString("memo"));
							dfVO.setChecktype1("1");
							dfVO.setValuecode1(psncode);
							list.add(dfVO);
							try {
								String voucherNo = createNCVoucher(info, list);
								return_obj1.put("success", "Y");
								return_obj1.put("sourceid", sourceid);
								return_obj1.put("voucher_no", voucherNo);
								return_obj1.put("msg", "");
							} catch (BusinessException e) {
								msg = e.getMessage();
								return_obj1.put("success", "N");
								return_obj1.put("sourceid", sourceid);
								return_obj1.put("voucher_no", "");
								return_obj1.put("msg", msg);
							}
						} else {
							msg = "detailΪ��";
							return_obj1.put("success", "N");
							return_obj1.put("sourceid", sourceid);
							return_obj1.put("voucher_no", "");
							return_obj1.put("msg", msg);
						}
						return_array.put(return_obj1);
					}
					return_obj.put("billList", return_array);
					VoucherLogInfo.info(return_obj.toString());
				} else {
					msg = "billListΪ��";
				}
			} catch (JSONException e) {
				msg = e.getMessage();
			}
			if (!"".equals(msg)) {
				VoucherLogInfo.info("���ݱ�ţ�" + sourceid + "," + msg);
			}
		}
	}

	public String createNCVoucher(VoucherInfo info, List<VoucherXmlVO> list)
			throws BusinessException {
		String voucherNo = "";
		try {
			VoucherChangeUtil util = new VoucherChangeUtil(info,
					"nc.bs.gl.voucher.impl.VoucherDeal");
			VoucherVO vo = util.changeByVoucherXmlVO(list);
			IVoucher voucherbo = (IVoucher) NCLocator.getInstance().lookup(
					IVoucher.class.getName());
			voucherbo.save(vo, new Boolean(true));
			voucherNo = "" + vo.getNo();
		} catch (Exception e) {
			VoucherLogInfo.info(e.getMessage());
			throw new BusinessException(e);
		}
		return voucherNo;
	}

}
