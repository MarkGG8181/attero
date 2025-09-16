package store.clovr.common.protocol.launcher;

import java.io.Serializable;

public class OwnedProductInfo implements Serializable {
    public final long productId;
    public final String productName;
    public final String productVersion;

    public OwnedProductInfo(long productId, String productName, String productVersion) {
        this.productId = productId;
        this.productName = productName;
        this.productVersion = productVersion;
    }
}