package com.opcooc.storage.clientsource;

import com.opcooc.storage.client.FileClient;
import com.opcooc.storage.exception.ClientSourceException;

public interface ClientSource {

    FileClient getClient() throws ClientSourceException;
}
