package com.pace.tsm.service;
import com.pace.tsm.service.IPaceApduChannel;

interface IPaceTsmSdk {
	void regist(IPaceApduChannel channel);
	/**
	* 获取卡列表
	* @param outputParam 用于接收卡列表信息
	*
	* @return 0:成功；其它:失败
	*/
	int cardListQuery(out String[] outputParam);
	/**
	* 卡片切换，用于设置默认卡
	*@param 应用aid
	* @return 0:成功；其它:失败
	*/
	int cardSwitch(String instance_id);	
	/**
	* 卡片充值
	* @param inputParam  JSON字符串
	* @param outputParam JSON字符串  用于接收充值后结果信息
	*
	* @return 0:成功；其它:失败
	*/
	int cardTopup(String inputParam, out String[] outputParam);
	
	/**
	* 卡片查询
	* @param inputParam  JSON字符串
	* @param outputParam JSON字符串  用于接收卡片信息
	*
	* @return 0:成功；其它:失败
	*/
	int cardQuery(String inputParam, out String[] outputParam);
	
	/**
	* 获取SE CPLC
	* @param cplc 用于接收CPLC
	*
	* @return 0:成功；其它:失败
	*/
	int getCplc(out String[] cplc);
	
	/**
	* 从se中查询交易记录
	* @param instance_id appletId
	* @param outputParam JSON字符串  用于接收卡片交易记录信息
	*
	* @return 0:成功；其它:失败
	*/
	int transQuerySe(String instance_id,out String[] outputParam);

	/**
	* 初始化se
	* 
	* @return 0:成功；其它:失败
	*/
	int initSe();

	/**
    	* 合并开卡接口
    	*
    	* @return 0:成功；其它:失败
    */
	int issueCard(String inputParam, out String[] outputParam);

	/** 
	*selectAid to openchannel
	*@aid
	*	select channel
	*@resultCode
	*return 0->success, 1->error
	*/
	byte[] selectAid(String aid, out int[] resultCode);
	
	
	/**
	*Shut down the se connection
	*
	*@return 0->success, 1->error
	*/

	void close();
}