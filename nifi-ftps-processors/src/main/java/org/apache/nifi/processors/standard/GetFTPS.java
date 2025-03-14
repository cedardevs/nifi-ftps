package org.apache.nifi.processors.standard;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.nifi.annotation.behavior.InputRequirement;
import org.apache.nifi.annotation.behavior.InputRequirement.Requirement;
import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processors.standard.util.FTPSTransfer;
import org.apache.nifi.processors.standard.util.FileTransfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@InputRequirement(Requirement.INPUT_FORBIDDEN)
@Tags({"FTPS", "get", "retrieve", "files", "fetch", "remote", "ingest", "source", "input"})
@CapabilityDescription("Fetches files from an FTPS Server and creates FlowFiles from them")
@WritesAttributes({
        @WritesAttribute(attribute = "filename", description = "The filename is set to the name of the file on the remote server"),
        @WritesAttribute(attribute = "path", description = "The path is set to the path of the file's directory on the remote server. "
                + "For example, if the <Remote Path> property is set to /tmp, files picked up from /tmp will have the path attribute set "
                + "to /tmp. If the <Search Recursively> property is set to true and a file is picked up from /tmp/abc/1/2/3, then the path "
                + "attribute will be set to /tmp/abc/1/2/3"),
        @WritesAttribute(attribute = "file.lastModifiedTime", description = "The date and time that the source file was last modified"),
        @WritesAttribute(attribute = "file.lastAccessTime", description = "The date and time that the file was last accessed. May not work on "
                + "all file systems"),
        @WritesAttribute(attribute = "file.owner", description = "The numeric owner id of the source file"),
        @WritesAttribute(attribute = "file.group", description = "The numeric group id of the source file"),
        @WritesAttribute(attribute = "file.permissions", description = "The read/write/execute permissions of the source file"),
        @WritesAttribute(attribute = "absolute.path", description = "The full/absolute path from where a file was picked up. The current 'path' "
                + "attribute is still populated, but may be a relative path")})
public class GetFTPS extends GetFileTransfer {

    private List<PropertyDescriptor> properties;

    @Override
    protected void init(final ProcessorInitializationContext context) {
        final List<PropertyDescriptor> properties = new ArrayList<>();
        properties.add(FTPSTransfer.HOSTNAME);
        properties.add(FTPSTransfer.PORT);
        properties.add(FTPSTransfer.USERNAME);
        properties.add(FTPSTransfer.PASSWORD);
        properties.add(FTPSTransfer.CONNECTION_MODE);
        properties.add(FTPSTransfer.TRANSFER_MODE);
        properties.add(FTPSTransfer.REMOTE_PATH);
        properties.add(FTPSTransfer.FILE_FILTER_REGEX);
        properties.add(FTPSTransfer.PATH_FILTER_REGEX);
        properties.add(FTPSTransfer.POLLING_INTERVAL);
        properties.add(FTPSTransfer.RECURSIVE_SEARCH);
        properties.add(FTPSTransfer.FOLLOW_SYMLINK);
        properties.add(FTPSTransfer.IGNORE_DOTTED_FILES);
        properties.add(FTPSTransfer.DELETE_ORIGINAL);
        properties.add(FTPSTransfer.CONNECTION_TIMEOUT);
        properties.add(FTPSTransfer.DATA_TIMEOUT);
        properties.add(FTPSTransfer.MAX_SELECTS);
        properties.add(FTPSTransfer.REMOTE_POLL_BATCH_SIZE);
        properties.add(FTPSTransfer.USE_NATURAL_ORDERING);
        properties.add(FTPSTransfer.BUFFER_SIZE);
        properties.add(FTPSTransfer.UTF8_ENCODING);
        properties.add(FTPSTransfer.ALLOW_SELFSIGNED);
        this.properties = Collections.unmodifiableList(properties);
    }

    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return properties;
    }

    @Override
    protected FileTransfer getFileTransfer(final ProcessContext context) {
        return new FTPSTransfer(context, getLogger());
    }

}
