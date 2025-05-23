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
package org.apache.hadoop.hive.ql.optimizer.calcite;

import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.sql.type.SqlTypeName;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class HiveTypeFactory extends JavaTypeFactoryImpl {
  public HiveTypeFactory() {
    super(new HiveTypeSystemImpl());
    if (Bug.CALCITE_6954_FIXED) {
      throw new IllegalStateException("Class redundant once fix is merged");
    }
  }

  @Override
  protected @Nullable RelDataType leastRestrictiveArrayMultisetType(final List<RelDataType> types,
      final SqlTypeName sqlTypeName) {
    RelDataType type = super.leastRestrictiveArrayMultisetType(types, sqlTypeName);
    if (type != null) {
      return canonize(type);
    }
    return null;
  }

  @Override
  protected @Nullable RelDataType leastRestrictiveMapType(final List<RelDataType> types,
      final SqlTypeName sqlTypeName) {
    RelDataType type = super.leastRestrictiveMapType(types, sqlTypeName);
    if (type != null) {
      return canonize(type);
    }
    return null;
  }
}
