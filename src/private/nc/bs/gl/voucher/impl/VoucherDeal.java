package nc.bs.gl.voucher.impl;

import nc.bs.gl.pubinterface.IVoucherDelete;
import nc.bs.gl.pubinterface.IVoucherSave;
import nc.vo.gl.pubinterface.VoucherOperateInterfaceVO;
import nc.vo.gl.pubinterface.VoucherSaveInterfaceVO;
import nc.vo.gl.pubvoucher.OperationResultVO;
import nc.vo.pub.BusinessException;

public class VoucherDeal implements IVoucherSave,IVoucherDelete{

	public OperationResultVO[] afterSave(VoucherSaveInterfaceVO vo) throws BusinessException {
		return null ;
	}

	public OperationResultVO[] beforeSave(VoucherSaveInterfaceVO vo) throws BusinessException {
		return null;
	}

	public OperationResultVO[] afterDelete(VoucherOperateInterfaceVO vo) throws BusinessException {
		
		return null;
	}

	public OperationResultVO[] beforeDelete(VoucherOperateInterfaceVO vo) throws BusinessException {
		
		return null;
	}
}
