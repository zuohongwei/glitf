package nc.bs.gl.voucher.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import cn.bizfocus.ezw.expense.framework.common.util.RestClientUtil;

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
					JSONObject return_obj = new JSONObject();//返回json
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
								if(null != deptcode && !"".equals(deptcode) && !"null".equals(deptcode)){
									voucherXmlVO.setChecktype1("2");
									voucherXmlVO.setValuecode1(deptcode);
								}
								if(null != custcode && !"".equals(custcode) && !"null".equals(custcode)){
									voucherXmlVO.setChecktype2("73");
									voucherXmlVO.setValuecode2(custcode);
								}
								if(null != jobcide && !"".equals(jobcide) && !"null".equals(jobcide)){
									voucherXmlVO.setChecktype3("jobass");
									voucherXmlVO.setValuecode3(jobcide);
								}
								list.add(voucherXmlVO);
							}
							VoucherXmlVO dfVO = new VoucherXmlVO();
							dfVO.setSubjcode("22020306");// 贷方科目22020306\预提一般费用
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
							msg = "detail为空";
							return_obj1.put("success", "N");
							return_obj1.put("sourceid", sourceid);
							return_obj1.put("voucher_no", "");
							return_obj1.put("msg", msg);
						}
						return_array.put(return_obj1);
					}
					return_obj.put("billList", return_array);
					VoucherLogInfo.info(return_obj.toString());
//					String remoteUrl = "https://211.95.28.171:4430/SynchroService/voucherDataRestful/receiveVoucherDataResponse/3";
					String remoteUrl = "https://www.e-zw.cn/SynchroService/voucherDataRestful/receiveVoucherDataResponse/3";
					RestClientUtil.initHttpsURLConnection("123456", "C:\\Program Files\\Java\\jdk1.5.0_12\\bin\\ezw-sit.jks", 
							"C:\\Program Files\\Java\\jdk1.5.0_12\\bin\\ezw-sit.jks");
					RestClientUtil.restPosthttps(remoteUrl, return_obj.toString());
				} else {
					msg = "billList为空";
				}
			} catch (Exception e) {
				msg = e.getMessage();
				e.printStackTrace();
			}
			if (!"".equals(msg)) {
				VoucherLogInfo.info("单据编号：" + sourceid + "," + msg);
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

	public static void main(String[] args) {
		String remoteUrl = "https://211.95.28.171:4430/SynchroService/voucherDataRestful/receiveVoucherDataResponse/3";
		try {
			RestClientUtil.initHttpsURLConnection("123456", "C:\\Program Files (x86)\\Java\\jdk1.6.0_45\\bin\\ezw-sit.jks", "C:\\Program Files (x86)\\Java\\jdk1.6.0_45\\bin\\ezw-sit.jks");
			RestClientUtil.restPosthttps(remoteUrl, "1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
