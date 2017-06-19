package com.pace.tsm.service;
import com.pace.tsm.service.IPaceTsmSdkCallBack;
import com.pace.tsm.service.IPaceApduChannel;
import com.pace.data.BleAccessPoint;
interface IPaceTsmSdkAsync {
	void registBle(in BleAccessPoint point);

	void regist(IPaceApduChannel channel);
	/**
	* 获取卡列表
	* @param outputParam 用于接收卡列表信息
	*
	* @return 0:成功；其它:失败
	*/
	void cardListQuery(IPaceTsmSdkCallBack callback);
	
	/**
	* 卡片切换，用于设置默认卡
	*@param 应用aid
	* @return 0:成功；其它:失败
	*/
	void cardSwitch(String instance_id,IPaceTsmSdkCallBack callback);
	
	/**
	* 卡片充值
	* @param inputParam  JSON字符串
	* @param outputParam JSON字符串  用于接收充值后结果信息
	*
	* @return 0:成功；其它:失败
	*/
	void cardTopup(String inputParam,IPaceTsmSdkCallBack callback);
	
	/**
	* 卡片查询
	* @param inputParam  JSON字符串
	* @param outputParam JSON字符串  用于接收卡片信息
	*
	* @return 0:成功；其它:失败
	*/
	void cardQuery(String inputParam, IPaceTsmSdkCallBack callback);
	
	/**
	* 获取SE CPLC
	* @param cplc 用于接收CPLC
	*
	* @return 0:成功；其它:失败
	*/
	void getCplc(IPaceTsmSdkCallBack callback);
	
	/**
	* 从se中查询交易记录
	* @param instance_id appletId
	* @param outputParam JSON字符串  用于接收卡片交易记录信息
	*
	* @return 0:成功；其它:失败
	*/
	void transQuerySe(String instance_id,IPaceTsmSdkCallBack callback);
	
	/**
	* 初始化se
	* 
	* @return 0:成功；其它:失败
	*/
	void initSe(IPaceTsmSdkCallBack callback);

	/**
    	* 合并开卡接口
    	*
    	* @return 0:成功；其它:失败
    */

	void issueCard(String inputParam, IPaceTsmSdkCallBack callback);

	/** 
	*selectAid to openchannel
	*@aid
	*	select channel
	*@resultCode
	*return 0->success, 1->error
	*/
	void selectAid(String aid, IPaceTsmSdkCallBack callback);
	
	/**
	*Shut down the se connection
	*
	*@return 0->success, 1->error
	*/

	void shutdown(IPaceTsmSdkCallBack callback);
}