/*
 * Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * 
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.connector;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.connector.core.Connector;

public class FileCreate extends AbstractConnector implements Connector {

	public void connect(MessageContext messageContext) throws ConnectException {
		Object fileLocation = getParameter(messageContext, "filelocation");
		Object filename = getParameter(messageContext, "file");
		Object content = getParameter(messageContext, "content");
		try {

			System.out.println("File creation started..." + filename.toString());
			System.out.println("File Location..." + fileLocation.toString());
			System.out.println("File content..." + content.toString());
			Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwx--x");
			FileAttribute<Set<PosixFilePermission>> fileAttributes =
			                                                         PosixFilePermissions.asFileAttribute(perms);
			Path path = Paths.get(fileLocation.toString(), filename.toString());

			// will not work on windows
			// Files.createFile(path, fileAttributes);

			// creates a file
			Files.createFile(path);
			List<String> lines = new ArrayList<>();
			lines.add(content.toString());

			try {
				Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
				            StandardOpenOption.APPEND);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			// Create temp file in default location
			File.createTempFile("concretepage",
			                    String.valueOf(Calendar.getInstance().getTime().getTime()));

		} catch (Exception e) {
			throw new ConnectException(e);
		}
	}
}
