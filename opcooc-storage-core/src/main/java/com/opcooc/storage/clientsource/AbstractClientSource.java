package com.opcooc.storage.clientsource;

import com.opcooc.storage.client.Client;
import com.opcooc.storage.exception.ClientSourceException;

/**
 * @author cosmos
 */
public abstract class AbstractClientSource implements ClientSource {

    /**
     * 子类实现决定最终数据源
     *
     * @return 数据源
     */
    protected abstract ClientSource determineClientSource();

    @Override
    public Client getClient() throws ClientSourceException {
        return determineClientSource().getClient();
    }

}
