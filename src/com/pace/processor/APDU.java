
package com.pace.processor;

import java.util.List;

public class APDU {
    private List<String> data;

    public APDU(List<String> list) {
        data = list;
    }

    public List<String> req() {
        return data;
    }
}
