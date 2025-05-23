/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.ql.ddl.catalog.alter.location;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.metastore.api.Catalog;
import org.apache.hadoop.hive.ql.ErrorMsg;
import org.apache.hadoop.hive.ql.ddl.DDLOperationContext;
import org.apache.hadoop.hive.ql.ddl.catalog.alter.AbstractAlterCatalogOperation;
import org.apache.hadoop.hive.ql.exec.Utilities;
import org.apache.hadoop.hive.ql.metadata.HiveException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Operation process of altering a catalog's location.
 */
public class AlterCatalogSetLocationOperation extends AbstractAlterCatalogOperation<AlterCatalogSetLocationDesc> {
  public AlterCatalogSetLocationOperation(DDLOperationContext context, AlterCatalogSetLocationDesc desc) {
    super(context, desc);
  }

  @Override
  protected void doAlteration(Catalog catalog) throws HiveException {
    try {
      String newLocation = Utilities.getQualifiedPath(context.getConf(), new Path(desc.getLocation()));

      URI locationURI = new URI(newLocation);
      if (!locationURI.isAbsolute()) {
        throw new HiveException(ErrorMsg.BAD_LOCATION_VALUE, newLocation);
      }

      if (newLocation.equals(catalog.getLocationUri())) {
        LOG.info("AlterCatalog skipped. No change in location.");
      } else {
        catalog.setLocationUri(newLocation);
        LOG.info("Catalog location changed from {} to {}", catalog.getLocationUri(), newLocation);
      }
    } catch (URISyntaxException e) {
      throw new HiveException(e);
    }
  }
}
