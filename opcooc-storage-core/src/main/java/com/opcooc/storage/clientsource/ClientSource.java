package com.opcooc.storage.clientsource;

import com.opcooc.storage.client.Client;
import com.opcooc.storage.exception.ClientSourceException;

public interface ClientSource {

    Client getClient() throws ClientSourceException;
}
