package com.opcooc.storage.clientsource;

import com.opcooc.storage.client.FileClient;
import com.opcooc.storage.exception.ClientSourceException;

public class DynamicRoutingClientSource implements ClientSource{


    @Override
    public FileClient getClient() throws ClientSourceException {
        return null;
    }
}
