
package com.pace.processor;

import com.pace.common.ApduHelper;
import com.pace.common.RET;
import com.pace.constants.ApduConstants;
import com.pace.processor.bean.ParamBean;
import com.pace.processor.internal.CardCplc;
import com.pace.processor.internal.CardListQuery;
import com.pace.processor.internal.CardNetBusiness;
import com.pace.processor.internal.CardQuery;
import com.pace.processor.internal.CardSwitch;
import com.pace.processor.internal.CardTransaction;
import com.pace.processor.internal.base.ApduChainController;

public class Dispatcher {

    private static volatile Dispatcher sInstance = null;

    public static Dispatcher getInstance() {
        if (sInstance == null) {
            synchronized (Dispatcher.class) {
                if (sInstance == null) {
                    sInstance = new Dispatcher();
                }
            }
        }
        return sInstance;
    }

    public final RET invoke(ParamBean msg, IBusinessType type) {
        if (type == null) {
            return RET.err(msg.getData());
        }
        return type.call(msg);
    }

    public static interface IBusinessType {
        public RET call(ParamBean msg);
    }

    public static class CardListQueryType implements IBusinessType {

        @Override
        public RET call(ParamBean msg) {
            ApduChainController controller = new ApduChainController();
            controller.add(new CardCplc());
            controller.add(new CardListQuery());
            return controller.invoke(msg);
        }
    }

    public static class CardQueryType implements IBusinessType {

        @Override
        public RET call(ParamBean msg) {
            ApduChainController controller = new ApduChainController();
            controller.add(new CardCplc());
            controller.add(new CardQuery());
            return controller.invoke(msg);
        }
    }

    public static class CardCplcType implements IBusinessType {

        @Override
        public RET call(ParamBean msg) {
            ApduChainController controller = new ApduChainController();
            controller.add(new CardCplc());
            return controller.invoke(msg);
        }
    }

    public static class CardNetBusinessType implements IBusinessType {

        @Override
        public RET call(ParamBean msg) {
            ApduChainController controller = new ApduChainController();
            controller.add(new CardCplc());
            controller.add(new CardNetBusiness());
            return controller.invoke(msg);
        }
    }

    public static class CardSwitchType implements IBusinessType {

        @Override
        public RET call(ParamBean msg) {
            ApduChainController controller = new ApduChainController();
            controller.add(new CardListQuery(ApduConstants.AID_CRS));
            controller.add(new CardSwitch());
            return controller.invoke(msg);
        }
    }

    public static class CardTransactionType implements IBusinessType {

        @Override
        public RET call(ParamBean msg) {
            ApduChainController controller = new ApduChainController();
            controller.add(new CardTransaction());
            return controller.invoke(msg);
        }

    }
}
