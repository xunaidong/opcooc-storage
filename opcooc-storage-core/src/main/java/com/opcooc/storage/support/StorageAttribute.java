package com.opcooc.storage.support;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class StorageAttribute {

    private String client;

    private String bucket;
}
