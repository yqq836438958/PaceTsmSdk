
package com.pace.processor;

import com.pace.common.RET;
import com.pace.processor.internal.CardCplc;
import com.pace.processor.internal.CardListQuery;
import com.pace.processor.internal.CardNetBusiness;
import com.pace.processor.internal.CardQuery;
import com.pace.processor.internal.base.ApduChainController;

public class Dispatcher {

    public final RET invoke(String msg, IBusinessType type) {
        if (type == null) {
            return RET.err(msg);
        }
        return type.call(msg);
    }

    public static interface IBusinessType {
        public RET call(String msg);
    }

    public static class CardListQueryType implements IBusinessType {

        @Override
        public RET call(String msg) {
            ApduChainController controller = new ApduChainController();
            controller.add(new CardCplc());
            controller.add(new CardListQuery());
            return controller.invoke(msg);
        }
    }

    public static class CardQueryType implements IBusinessType {

        @Override
        public RET call(String msg) {
            ApduChainController controller = new ApduChainController();
            controller.add(new CardCplc());
            controller.add(new CardQuery());
            return controller.invoke(msg);
        }
    }

    public static class CardCplcType implements IBusinessType {

        @Override
        public RET call(String msg) {
            ApduChainController controller = new ApduChainController();
            controller.add(new CardCplc());
            return controller.invoke(msg);
        }
    }

    public static class CardNetBusinessType implements IBusinessType {

        @Override
        public RET call(String msg) {
            ApduChainController controller = new ApduChainController();
            controller.add(new CardCplc());
            controller.add(new CardNetBusiness());
            return controller.invoke(msg);
        }
    }

    public static class CardSwitchType implements IBusinessType {

        @Override
        public RET call(String msg) {
            ApduChainController controller = new ApduChainController();
            controller.add(new CardCplc());
            controller.add(new CardNetBusiness());
            return controller.invoke(msg);
        }
    }

}
