/****************************************************************
 * Licensed to the AOS Community (AOS) under one or more        *
 * contributor license agreements.  See the NOTICE file         *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The AOS licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package aos.camel;

import javax.sql.DataSource;

import org.apache.camel.Consume;
import org.apache.camel.Produce;
import org.springframework.jdbc.core.JdbcTemplate;

public class InventoryUpdaterAnnotatedWithProduceInterface {
    
    @Produce(uri = "jms:partnerAudit")
    PartnerAudit partnerAudit;
    
    private JdbcTemplate jdbc;

    public InventoryUpdaterAnnotatedWithProduceInterface(DataSource ds) {
        jdbc = new JdbcTemplate(ds);
    }
    
    @Consume(uri = "jms:partnerInventoryUpdate")
    public void updateInventory(Inventory inventory) {
        jdbc.execute(toSql(inventory));
        partnerAudit.audit(inventory);
    }    
    
    private String toSql(Inventory inventory) {
        Object[] args = new Object[] {inventory.getSupplierId(), inventory.getPartId(), inventory.getName(), inventory.getAmount()};
        return String.format("insert into partner_inventory (supplier_id, part_id, name, amount) values ('%s', '%s', '%s', '%s')", args);
    }
}
